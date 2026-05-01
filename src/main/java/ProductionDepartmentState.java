import production.ProductionProgram;

final class ProductionDepartmentState implements DepartmentState {
    @Override
    public void open(String[] args) {
        ProductionProgram.main(args);
    }
}
