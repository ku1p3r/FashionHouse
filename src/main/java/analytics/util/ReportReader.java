package analytics.util;

import common.util.Serializer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class ReportReader {
    /**
     * Reads a report file and returns a mapping of product names to quantities sold.
     *
     * @param path
     * @return
     */
    public static HashMap<String, Integer> read(String path){
        HashMap<String, Integer> result = new HashMap<>();

        Serializer serializer = new Serializer(path);
        ArrayList<String> products = serializer.get("product", String.class);
        ArrayList<Integer> sold = serializer.get("sold", Integer.class);

        for(int i = 0; i < products.size(); i++){
            result.put(products.get(i), sold.get(i));
        }

        return result;
    }

    /**
     * Basic helper for converting a report filename to a more user-friendly name by removing the directory and suffix.
     *
     * @param path
     * @param suffix
     * @return
     */
    public static String getName(Path path, String suffix){
        String fileName = path.getFileName().toString();
        return fileName.substring(0, fileName.length() - suffix.length());
    }
}
