package org.ichnaea;

import org.ichnaea.core.mvc.controller.ControllerLoader;
import org.ichnaea.core.mvc.view.ViewLoader;
import org.ichnaea.core.ui.app.SideNavApp;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class IchnaeaApplication {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(IchnaeaApplication.class);
    private static final String VIEW_CLASS_PATH = "org.ichnaea.view";
    private static final String CONTROLLER_CLASS_PATH = "org.ichnaea.controller";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JFrame window = new SideNavApp();
                ViewLoader.INSTANCE.loadViews(window, VIEW_CLASS_PATH);
                ControllerLoader.INSTANCE.loadControllers(CONTROLLER_CLASS_PATH);
                Objects.requireNonNull(ControllerLoader.INSTANCE.getController("SignIn")).show();
            } catch (Exception e) {
                LOGGER.error("Could not start application", e);
            }
        });
    }
}
