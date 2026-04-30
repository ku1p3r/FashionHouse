package production;

import common.base.ScreenProgramTemplate;
import common.util.Terminal;

/**
 * Material detail actions using {@link ScreenProgramTemplate}.
 */
final class MaterialDetailMenuScreen extends ScreenProgramTemplate<Material, String> {

    private final MaterialModule module;
    private final Material material;
    private boolean done;

    MaterialDetailMenuScreen(MaterialModule module, Material material) {
        this.module = module;
        this.material = material;
    }

    @Override
    protected Material initialScreen() {
        return material;
    }

    @Override
    protected void render(Material m) {
        module.drawMaterialDetail(m);
    }

    @Override
    protected String readInput(Material m) {
        return Terminal.prompt("Choice:");
    }

    @Override
    protected Material nextScreen(Material m, String choice) {
        if (module.handleMaterialDetailChoice(m, choice)) {
            done = true;
        }
        return m;
    }

    @Override
    protected boolean shouldExit(String input) {
        return done;
    }
}
