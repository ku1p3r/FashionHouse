import security.SecurityProgram;

final class SecurityDepartmentState implements DepartmentState {
    @Override
    public void open(String[] args) {
        SecurityProgram.main(args);
    }
}
