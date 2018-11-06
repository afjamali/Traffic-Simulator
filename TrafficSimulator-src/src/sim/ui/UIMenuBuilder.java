package sim.ui;

import java.util.ArrayList;
import java.util.List;

public class UIMenuBuilder {

    private final List<UIMenu.Pair> _menu = new ArrayList<UIMenu.Pair>();

    public void add(String prompt, UIMenuAction action) {
        if (null == action) {
            throw new IllegalArgumentException();
        }
        _menu.add(new UIMenu.Pair(prompt, action));
    }

    public UIMenu toUIMenu(String heading) {
        if (null == heading) {
            throw new IllegalArgumentException();
        }
        if (_menu.size() <= 1) {
            throw new IllegalStateException();
        }
        return new UIMenu(heading, _menu.toArray(new UIMenu.Pair[_menu.size()]));
    }
}
