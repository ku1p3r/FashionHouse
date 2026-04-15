package production;

import common.util.Serializer;
import common.util.Terminal;
import java.util.ArrayList;
import java.util.List;

public class MaterialRepository {

    private List<Material>  materials  = new ArrayList<>();
    private List<Supplier>  suppliers  = new ArrayList<>();

    public MaterialRepository() {
        loadSuppliers();
        loadMaterials();
    }

    // Loading

    private void loadMaterials() {
        try {
            Serializer s = new Serializer(DataPaths.MATERIALS);
            for (int i = 0; i < s.size(); i++) {
                materials.add(new Material(
                        s.get("id",               i, String.class),
                        s.get("name",             i, String.class),
                        s.get("unit",             i, String.class),
                        s.get("quantity",         i, Double.class),
                        s.get("reorderThreshold", i, Double.class),
                        s.get("pricePerUnit",     i, Double.class),
                        s.get("supplierId",       i, String.class),
                        s.get("supplierName",     i, String.class),
                        s.get("active",           i, Boolean.class)
                ));
            }
        } catch (Exception e) {
            Terminal.printError("Could not load materials: " + e.getMessage());
        }
    }

    private void loadSuppliers() {
        try {
            Serializer s = new Serializer(DataPaths.SUPPLIERS);
            for (int i = 0; i < s.size(); i++) {
                suppliers.add(new Supplier(
                        s.get("id",           i, String.class),
                        s.get("name",         i, String.class),
                        s.get("contactEmail", i, String.class),
                        s.get("phone",        i, String.class),
                        s.get("active",       i, Boolean.class)
                ));
            }
        } catch (Exception e) {
            Terminal.printError("Could not load suppliers: " + e.getMessage());
        }
    }

    // Saving

    public void saveMaterials() {
        try {
            Serializer s = new Serializer(new String[]{
                    "id","name","unit","quantity","reorderThreshold",
                    "pricePerUnit","supplierId","supplierName","active"
            });
            for (Material m : materials) {
                s.push(m.getId(), m.getName(), m.getUnit(), m.getQuantity(),
                        m.getReorderThreshold(), m.getPricePerUnit(),
                        m.getSupplierId(), m.getSupplierName(), m.isActive());
            }
            s.save(DataPaths.MATERIALS);
        } catch (Exception e) {
            Terminal.printError("Failed to save materials: " + e.getMessage());
        }
    }

    public void saveSuppliers() {
        try {
            Serializer s = new Serializer(new String[]{"id","name","contactEmail","phone","active"});
            for (Supplier sup : suppliers) {
                s.push(sup.getId(), sup.getName().replace(" [INACTIVE]",""),
                        sup.getContactEmail(), sup.getPhone(), sup.isActive());
            }
            s.save(DataPaths.SUPPLIERS);
        } catch (Exception e) {
            Terminal.printError("Failed to save suppliers: " + e.getMessage());
        }
    }

    // Queries

    public List<Material> getAll()         { return materials; }
    public List<Supplier> getAllSuppliers() { return suppliers; }

    public List<Material> getLowStock() {
        List<Material> low = new ArrayList<>();
        for (Material m : materials) if (m.isLowStock()) low.add(m);
        return low;
    }

    public Material findById(String id) {
        for (Material m : materials) if (m.getId().equalsIgnoreCase(id)) return m;
        return null;
    }

    public Supplier findSupplierById(String id) {
        for (Supplier s : suppliers) if (s.getId().equalsIgnoreCase(id)) return s;
        return null;
    }

    public List<Material> search(String query) {
        String q = query.toLowerCase();
        List<Material> results = new ArrayList<>();
        for (Material m : materials) {
            if (m.getId().toLowerCase().contains(q) || m.getName().toLowerCase().contains(q)
                    || m.getSupplierName().toLowerCase().contains(q)) {
                results.add(m);
            }
        }
        return results;
    }

    public List<Supplier> getActiveSuppliers() {
        List<Supplier> active = new ArrayList<>();
        for (Supplier s : suppliers) if (s.isActive()) active.add(s);
        return active;
    }

    // Mutations

    public void addMaterial(Material m) { materials.add(m); }

    /** Generate a new unique material ID */
    public String generateMaterialId() {
        int max = 0;
        for (Material m : materials) {
            try {
                int n = Integer.parseInt(m.getId().replace("M-", ""));
                if (n > max) max = n;
            } catch (NumberFormatException ignored) {}
        }
        return String.format("M-%03d", max + 1);
    }
}
