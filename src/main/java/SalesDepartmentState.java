import sales.SalesSystem;

final class SalesDepartmentState implements DepartmentState {
    @Override
    public void open(String[] args) {
        SalesSystem.main(args);
    }
}
