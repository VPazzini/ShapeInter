package gotcg;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MyFinishWindow extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
}
