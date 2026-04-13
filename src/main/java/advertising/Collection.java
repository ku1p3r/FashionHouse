package advertising;
/*
 * 
 * Advertising Module - Iteration 1
 *
 * Represents a clothing collection promoted in advertisements.
 * Author: Gilbert
 */
public class Collection {
    private final int collectionId;
    private final String name;

    public Collection(int collectionId, String name) {
        this.collectionId = collectionId;
        this.name = name;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public String getName() {
        return name;
    }
}
