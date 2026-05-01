final class ExitDepartmentState implements DepartmentState {

    private final boolean[] running;

    ExitDepartmentState(boolean[] running) {
        this.running = running;
    }

    @Override
    public void open(String[] args) {
        running[0] = false;
    }
}
