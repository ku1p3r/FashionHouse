import hr.HumanResourcesProgram;

final class HumanResourcesDepartmentState implements DepartmentState {
    @Override
    public void open(String[] args) {
        HumanResourcesProgram.main(args);
    }
}
