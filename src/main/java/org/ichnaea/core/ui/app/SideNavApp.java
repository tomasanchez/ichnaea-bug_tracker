package org.ichnaea.core.ui.app;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class SideNavApp extends JFrame {

    static final int DEFAULT_WIDTH = 720;
    static final int DEFAULT_HEIGHT = 600;

    private JPanel body = new JPanel();

    public SideNavApp() {

        Dimension defaultSize = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Ichnaea - Issue Tracking");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(defaultSize);
        setPreferredSize(defaultSize);
        setLocationRelativeTo(null);

        URL location;

        try {
            location = Objects
                    .requireNonNull(
                            getClass().getResource("/icon/ichnaea-icon.png")
                    );
            ImageIcon icon = new ImageIcon(location);
            setIconImage(icon.getImage());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        pack();
        setVisible(true);
    }

    public void setBody(JPanel body) {
        this.body = body;
        revalidate();
        repaint();
        pack();
    }


}
