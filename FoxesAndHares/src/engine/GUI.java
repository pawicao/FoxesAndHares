package engine;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    private JPanel mainPanel = SimulationPanel.getInstance();
    private JPanel settingsPanel = SettingsPanel.getInstance();
    private JPanel controlPanel = ControlPanel.getInstance();
    private JPanel plotPanel = PlotPanel.getInstance();

    private GUI() {
        super("Title");
        buildGUI();
        //setContentPane(mainPanel);
        Dimension camRes = SimulationPanel.getInstance().getSize();
        setSize(1200, 600);

        setVisible(true);
    }

    private static GUI instance = null;

    public static GUI getInstance() {
        if (instance == null)
            instance = new GUI();

        return instance;
    }

    private void buildGUI() {
        add(mainPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.PAGE_START);
        add(plotPanel, BorderLayout.PAGE_END);
        add(settingsPanel, BorderLayout.EAST);
    }
}