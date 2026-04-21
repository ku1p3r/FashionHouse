package common.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Serializes and deserializes primitive wrappers to and from CSV files.
 *
 * Primary Author: Jase Beaubien
 */
public class Serializer {
    // Use constructor with "column 1 name", "column 2 name", ... as constructor for new pipe-delimited files.
    // Use constructor with the file path for existing files.

    // Use push(attribute 1, attribute 2, ...) to add data. (mutator)
    // Use get([column name], [type].class) for ArrayList of data. (accessor)
    // Use get([column name], [row], [type].class) for data at row. (accessor)

    // Use save() for files that were loaded from existing files.
    // Use save([path]) for files that were not yet saved.

    private HashMap<String, ArrayList<String>> attributes;
    private ArrayList<String> attributeList;
    private String path = "";
    private String delimiter = "|";
    public record Criterion<T>(String key, T value) {}

    // Construct new file.
    public Serializer(String[] columns){
        attributes = new HashMap<>();
        attributeList = new ArrayList<>();
        for(String key : columns){
            attributes.put(key, new ArrayList<>());
            attributeList.add(key);
        }
    }

    // Construct from path.
    public Serializer(String path){
        this.path = path;
        attributes = new HashMap<>();
        attributeList = new ArrayList<>();

        // This is NOT the most efficient way to do this, but it's the most readable and I can fix it later.
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line = reader.readLine();

            // For first line.
            for(String s : line.split("\\" + delimiter)){
                attributes.put(s, new ArrayList<>());
                attributeList.add(s);
                //System.out.println("Added attribute '" + s + "'\n");
            }

            while((line = reader.readLine()) != null){
                // For body lines.
                int index = 0;
                for(String s : line.split("\\" + delimiter)){
                    String key = attributeList.get(index++);
                    attributes.get(key).add(s);
                    //System.out.println("Added value '" + s + "' under key '" + key + "'\n");
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void push(Object... args) throws Exception {
        int col = attributeList.size();
        if(args.length > col){
            throw new Exception("Cannot push more columns than in header.");
        }

        if(args.length < col){
            throw new Exception("Cannot push less columns than in header.");
        }

        int index = 0;
        for(Object arg : args){
            String key = attributeList.get(index++);
            attributes.get(key).add(arg.toString());
        }
    }

    public void save() throws Exception {
        if(path.isEmpty()){
            throw new Exception("Cannot implicitly save new files without having path as an argument.");
        }
        save(path);
    }

    public void save(String path) throws Exception {
        int columnCount = attributeList.size();

        String result = "";

        int index = 0;
        for(String s : attributeList){
            result += s + (++index >= columnCount ? '\n' : delimiter);
        }

        int count = -1;
        for(String s : attributeList){
            if(count == -1){
                count = attributes.get(s).size();
                continue;
            }

            if(attributes.get(s).size() != count){
                throw new Exception("Attributes not assigned evenly for each row.");
            }
            count = attributes.get(s).size();
        }

        for(int i = 0; i < count; i++){
            index = 0;
            for(String s : attributeList){
                result += attributes.get(s).get(i) + (++index >= columnCount ? '\n' : delimiter);
            }
        }

        //System.out.println(result);

        try (FileOutputStream fos = new FileOutputStream(path)) {
            fos.write(result.getBytes());
            fos.flush();
            fos.getFD().sync();
        }
    }

    public <T> ArrayList<T> get(String attribute, Class<T> type){
        ArrayList<String> att = attributes.get(attribute);
        ArrayList<T> arr = new ArrayList<>();

        for(int i = 0; i < att.size(); i++){
            arr.add(convert(att.get(i), type));
        }

        return arr;
    }

    public <T> T get(String attribute, int row, Class<T> type){
        return convert(attributes.get(attribute).get(row), type);
    }

    private <T> T convert(String value, Class<T> type){
        if(type == String.class) return type.cast(value);
        if(type == Integer.class) return type.cast(Integer.valueOf(value));
        if(type == Long.class) return type.cast(Long.valueOf(value));
        if(type == Float.class) return type.cast(Float.valueOf(value));
        if(type == Double.class) return type.cast(Double.valueOf(value));
        if(type == Boolean.class) return type.cast(Boolean.valueOf(value));

        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    public int size(){
        if(attributeList.isEmpty()) return 0;
        return attributes.get(attributeList.get(0)).size();
    }

    public <T> void set(String attribute, int row, T value){
        attributes.get(attribute).set(row, value.toString());
    }

    public ArrayList<Integer> getRows(Iterable<Criterion> criteria){
        ArrayList<Integer> matches = new ArrayList<>();

        /*for(Criterion criterion : criteria){
            String key = criterion.key();
            String value = criterion.value().toString();

            if(!attributes.containsKey(key)){
                return matches; // No matches if key doesn't exist.
            }

            for(int i = 0; i < attributes.get(key).size(); i++){
                if(attributes.get(key).get(i).equals(value)){
                    matches.add(i);
                }
            }
        }*/

        for(int i = 0; i < size(); i++){
            boolean match = true;
            for(Criterion criterion : criteria){
                String key = criterion.key();
                String value = criterion.value().toString();

                if(!attributes.containsKey(key) || !attributes.get(key).get(i).equals(value)){
                    match = false;
                    break;
                }
            }
            if(match){
                matches.add(i);
            }
        }

        return matches;
    }
}