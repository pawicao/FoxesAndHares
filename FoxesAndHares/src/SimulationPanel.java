import javax.swing.*;
import java.awt.*;

public class SimulationPanel extends JPanel {
    private static SimulationPanel instance = null;

    public static SimulationPanel getInstance() {
        if (instance == null)
            instance = new SimulationPanel();

        return instance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Animal animal : Animal.animals) {
            animal.paint(g);
        }
    }
}
