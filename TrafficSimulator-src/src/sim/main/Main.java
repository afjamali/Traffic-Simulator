package sim.main;

import sim.ui.*;
/**
 *
 * @author Admin
 */
public class Main {

    private Main() {
    }

    public static void main(String[] args) {
        UI ui;
        ui = new PopupUI();
        
        Control control = new Control(ui);
        control.run();
    }
}
