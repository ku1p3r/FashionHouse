package sales.ui;

import sales.ScreenInput;
import sales.service.SalesService;

public class CartScreen implements Screen {

    private SalesService service;

    public CartScreen(SalesService service){
        this.service = service;
    }

    public void show(){

    }

    @Override
    public ScreenInput processInput() {
        return null;
    }

    @Override
    public Screen next() {
        return null;
    }

}
