import advertising.AdvertisingMain;

final class AdvertisingDepartmentState implements DepartmentState {
    @Override
    public void open(String[] args) {
        AdvertisingMain.main(args);
    }
}
