import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IntegralCalculatorGUI gui = new IntegralCalculatorGUI();
            gui.setVisible(true);
        });
    }
}
