package common.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Serializer {
    // Use constructor with "column 1 name", "column 2 name", ... as constructor for new CSVs.
    // Use constructor with the file path for existing CSvs.

    // Use push(attribute 1, attribute 2, ...) to add data. (mutator)
    // Use get([column name], [type].class) for ArrayList of data. (accessor)
    // Use get([column name], [row], [type].class) for data at row. (accessor)

    // Use save() for files that were loaded from existing CSVs.
    // Use save([path]) for files that were not yet saved.

    private HashMap<String, ArrayList<String>> attributes;
    private ArrayList<String> attributeList;
    private String path = "";

    // Construct new CSV.
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

        // TS is NOT the most efficient way to do this, but it's the most readable and I can fix it later.
        try(BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line = reader.readLine();

            // For first line.
            for(String s : line.split(",")){
                attributes.put(s, new ArrayList<>());
                attributeList.add(s);
                System.out.println("Added attribute '" + s + "'\n");
            }

            while((line = reader.readLine()) != null){
                // For body lines.
                int index = 0;
                for(String s : line.split(",")){
                    String key = attributeList.get(index++);
                    attributes.get(key).add(s);
                    System.out.println("Added value '" + s + "' under key '" + key + "'\n");
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
        int columnCount = attributes.keySet().size();

        String result = "";

        int index = 0;
        for(String s : attributes.keySet()){
            result += s + (++index >= columnCount ? '\n' : ',');
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
                result += attributes.get(s).get(i) + (++index >= columnCount ? '\n' : ',');
            }
        }

        System.out.println(result);

        FileWriter writer = new FileWriter(path);
        writer.write(result);
        writer.flush();
        writer.close();
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
        if(type == Integer.class) return type.cast(Integer.parseInt(value));
        if(type == Long.class) return type.cast(Long.parseLong(value));
        if(type == Float.class) return type.cast(Float.parseFloat(value));
        if(type == Double.class) return type.cast(Double.parseDouble(value));
        if(type == Boolean.class) return type.cast(Boolean.parseBoolean(value));

        throw new IllegalArgumentException("Unsupported type: " + type);
    }
}
