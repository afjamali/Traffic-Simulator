package sim.main;

import sim.ui.*;
import sim.model.*;
import sim.model.swing.SwingAnimatorBuilder;
import sim.model.text.TextAnimatorBuilder;

/**
 *
 * @author Admin
 */
class Control {

    private static final int EXITED = 0;
    private static final int EXIT = 1;
    private static final int START = 2;
    private static final int SETTINGS = 3;
    private static final int NUMSTATES = 4;
    private UIMenu[] _menus;
    private int _state;
    private UI _ui;

    private UIForm _trafficPattern;
    private UIForm _minMax;
    private UIForm _gridSize;
    private UIForm _oneDouble;
    private UIForm _runTime;
    private UIFormTest _numberTest;
    private UIFormTest _doubleTest;

    Control(UI ui) {
        _ui = ui;

        _menus = new UIMenu[NUMSTATES];
        _state = START;
        addSTART(START);
        addEXIT(EXIT);
        addSETTINGS(SETTINGS);

        _numberTest = new UIFormTest() {
            public boolean run(String input) {
                try {
                    Integer.parseInt(input);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        };

        _doubleTest = new UIFormTest() {
            public boolean run(String input) {
                try {
                    Double.parseDouble(input);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        };

        UIFormBuilder pattern = new UIFormBuilder();
        pattern.add("1 for simple pattern and 2 for alternating: ", _numberTest);
        _trafficPattern = pattern.toUIForm("Enter Value");

        UIFormBuilder oneDouble = new UIFormBuilder();
        oneDouble.add("value: ", _doubleTest);
        _oneDouble = oneDouble.toUIForm("Enter Value");

        UIFormBuilder twoDoubles = new UIFormBuilder();
        twoDoubles.add("Minimum: ", _doubleTest);
        twoDoubles.add("Maximum: ", _doubleTest);
        _minMax = twoDoubles.toUIForm("Enter Value");

        UIFormBuilder grid = new UIFormBuilder();
        grid.add("numbers of rows (default value is " + MP.gridRows
                + "):", _numberTest);
        grid.add("numbers of columns (default value is " + MP.gridColumns
                + "):", _numberTest);
        _gridSize = grid.toUIForm("Enter Value: ");

        UIFormBuilder runT = new UIFormBuilder();
        runT.add("value (default value is " + MP.runTime + "):",
                _doubleTest);
        _runTime = runT.toUIForm("Enter value: ");
    }

    void run() {
        try {
            while (_state != EXITED) {
                _ui.processMenu(_menus[_state]);
            }
        } catch (UIError e) {
            _ui.displayError("UI closed");
        }
    }

    private void addSTART(int stateNum) {
        UIMenuBuilder m = new UIMenuBuilder();

        m.add("Default",
                new UIMenuAction() {
                    public void run() {
                        _ui.displayError("doh!");
                    }
                });

        m.add("Run Simulation",
                new UIMenuAction() {
                    public void run() {
                        {
                            Model m = new Model(new SwingAnimatorBuilder());
                            m.run();
                            m.dispose();
                        }
                    }
                });

        m.add("Change simulation parameters",
                new UIMenuAction() {
                    public void run() {
                        _state = 3;
                    }
                });

        m.add("Exit",
                new UIMenuAction() {
                    public void run() {
                        _state = EXIT;
                    }
                });

        _menus[stateNum] = m.toUIMenu("Afshin's Traffic Simulator (Fasten your seatbelt!)");
    }

    private void addSETTINGS(int stateNum) {
        UIMenuBuilder m = new UIMenuBuilder();

        m.add("Default",
                new UIMenuAction() {
                    public void run() {

                        _ui.displayError("doh!");
                    }
                });

        m.add("Show current values",
                new UIMenuAction() {
                    public void run() {

                        StringBuilder sb = MP.getCurrentValues();
                        _ui.displayMessage(sb.toString());
                    }
                });

        m.add("Simulation time step",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_oneDouble);
                        MP.timeStep = Double.parseDouble(result[0]);
                    }
                });

        m.add("Simulation run time",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_runTime);
                        MP.runTime = Double.parseDouble(result[0]);
                    }
                });

        m.add("Grid size",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_gridSize);
                        MP.gridRows = Integer.parseInt(result[0]);
                        MP.gridColumns = Integer.parseInt(result[1]);
                    }
                });

        m.add("Traffic pattern",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_trafficPattern);
                        if (Integer.parseInt(result[0]) == 1) {
                            MP.trafficPattern = "simple";
                        } else {
                            MP.trafficPattern = "alternating";
                        }
                    }
                });

        m.add("Car entry rate",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_minMax);
                        MP.entryRateMin = Double.parseDouble(result[0]);
                        MP.entryRateMax = Double.parseDouble(result[1]);

                    }
                });

        m.add("Road segment length",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_minMax);
                        MP.roadLengthMin = Double.parseDouble(result[0]);
                        MP.roadLengthMax = Double.parseDouble(result[1]);
                    }
                });

        m.add("Intersection length",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_minMax);
                        MP.intersectionLengthMin = Double.parseDouble(result[0]);
                        MP.intersectionLengthMax = Double.parseDouble(result[1]);
                    }
                });

        m.add("Car length",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_minMax);
                        MP.carLengthMin = Double.parseDouble(result[0]);
                        MP.carLengthMax = Double.parseDouble(result[1]);
                    }
                });

        m.add("Car maximum velocity",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_minMax);
                        MP.velocityMin = Double.parseDouble(result[0]);
                        MP.velocityMax = Double.parseDouble(result[1]);
                    }
                });

        m.add("Car stop distance",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_minMax);
                        MP.stopDistanceMin = Double.parseDouble(result[0]);
                        MP.stopDistanceMax = Double.parseDouble(result[1]);
                    }
                });

        m.add("Car brake distance",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_minMax);
                        MP.brakeDistanceMin = Double.parseDouble(result[0]);
                        MP.brakeDistanceMax = Double.parseDouble(result[1]);
                    }
                });

        m.add("Traffic light green time",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_minMax);
                        MP.greenTimeMin = Double.parseDouble(result[0]);
                        MP.greenTimeMax = Double.parseDouble(result[1]);
                    }
                });

        m.add("Traffic light yellow time",
                new UIMenuAction() {
                    public void run() {

                        String[] result = _ui.processForm(_minMax);
                        MP.entryRateMin = Double.parseDouble(result[0]);
                        MP.entryRateMax = Double.parseDouble(result[1]);
                    }
                });

        m.add("Reset simulation and return to the main menu",
                new UIMenuAction() {
                    public void run() {

                        MP.reset();
                        _state = 2;
                    }
                });

        m.add("Return to main menu",
                new UIMenuAction() {
                    public void run() {

                        _state = 2;
                    }
                });

        _menus[stateNum] = m.toUIMenu("Modify settings:");
    }

    private void addEXIT(int stateNum) {
        UIMenuBuilder m = new UIMenuBuilder();

        m.add("Default", new UIMenuAction() {
            public void run() {
            }
        });
        m.add("Yes",
                new UIMenuAction() {
                    public void run() {
                        _state = EXITED;
                    }
                });
        m.add("No",
                new UIMenuAction() {
                    public void run() {
                        _state = START;
                    }
                });

        _menus[stateNum] = m.toUIMenu("Are you sure you want to exit?");
    }
}
