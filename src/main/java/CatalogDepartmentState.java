import catalog.CatalogProgram;

final class CatalogDepartmentState implements DepartmentState {
    @Override
    public void open(String[] args) {
        CatalogProgram.main(args);
    }
}
