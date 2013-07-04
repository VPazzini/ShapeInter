package gotcg;

import java.awt.event.*;

public class MyFinishWindow extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
}
