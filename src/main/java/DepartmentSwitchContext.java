final class DepartmentSwitchContext {

    private final String[] args;
    private DepartmentState currentState;

    DepartmentSwitchContext(String[] args) {
        this.args = args;
    }

    void setState(DepartmentState state) {
        this.currentState = state;
    }

    void switchDepartment() {
        if (currentState != null) {
            currentState.open(args);
        }
    }
}
