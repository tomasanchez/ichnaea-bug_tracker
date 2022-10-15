package org.ichnaea;

import org.ichnaea.core.mvc.controller.ControllerLoader;
import org.ichnaea.core.mvc.view.ViewLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class IchnaeaApplication extends JFrame {

    static final int DEFAULT_WIDTH = 720;
    static final int DEFAULT_HEIGHT = 600;

    private static final String CONTROLLER_CLASS_PATH = "org.ichnaea.controller";

    private static final String VIEW_CLASS_PATH = "org.ichnaea.view";

    private static final Logger LOGGER = LoggerFactory.getLogger(IchnaeaApplication.class);

    public IchnaeaApplication() {

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
            LOGGER.error("Could not load icon");
        }

        pack();
        setVisible(true);
        LOGGER.info("Ichnaea application started");
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JFrame window = new IchnaeaApplication();
                ViewLoader.INSTANCE.loadViews(window, VIEW_CLASS_PATH);
                ControllerLoader.INSTANCE.loadControllers(CONTROLLER_CLASS_PATH);
                Objects.requireNonNull(ControllerLoader.INSTANCE.getController("SignIn")).show();
            } catch (Exception e) {
                LOGGER.error("Could not start application", e);
            }
        });
    }

}
