package catalog.service;

import common.model.Product;
import java.io.IOException;
import java.util.List;

/**
 * Persistence port for catalog products.
 */
public interface CatalogRepository {

    List<Product> loadAll() throws IOException;

    void saveAll(List<Product> products) throws IOException;
}
