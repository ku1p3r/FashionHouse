import imageconsulting.ImageConsultingSystem;

final class ImageConsultingDepartmentState implements DepartmentState {
    @Override
    public void open(String[] args) {
        try {
            ImageConsultingSystem.main(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
