package common;

import common.util.Serializer;

/**
 * @author Mason Hart
 */
public interface Serializable {

    /**
     * Convert itself into a csv line
     * @return a pipe-delimited String to be stored in a csv file
     */
    public String serialize(Serializer serializer);

    /**
     * Convert a String back into the object
     * @param line  String from the csv file
     * @return      the object that the csv represents
     */
    public Object deserialize(String[] line);
}
