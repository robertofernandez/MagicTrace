package studio.itpex.magictrace.tests.ide;

import javax.swing.JFrame;

public class MagicTraceTestide extends JFrame {
    private static final long serialVersionUID = 6733330978640433656L;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    protected static void createAndShowGUI() {
        MagicTraceTestide ide = new MagicTraceTestide();
        ide.pack();
        ide.setVisible(true);
    }
}
