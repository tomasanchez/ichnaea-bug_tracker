package org.ichnaea;

import org.ichnaea.core.mvc.controller.ControllerLoader;
import org.ichnaea.core.mvc.view.ViewLoader;
import org.ichnaea.core.ui.app.AppUI;
import org.ichnaea.core.ui.app.SideNavApp;
import org.ichnaea.model.Role;
import org.ichnaea.model.RoleName;
import org.ichnaea.model.User;
import org.ichnaea.service.UserService;
import org.slf4j.Logger;

import java.awt.*;
import java.util.Objects;

public class IchnaeaApplication {

    private final static String APP_TITLE = "Ichnaea - Issue Tracking";

    private final static String BRAND = "BT";

    private final static String ICON_PATH = "/icon/ichnaea-icon.png";

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(IchnaeaApplication.class);

    private static final String VIEW_CLASS_PATH = "org.ichnaea.view";

    private static final String CONTROLLER_CLASS_PATH = "org.ichnaea.controller";

    public static void main(String[] args) {

        LOGGER.info("Starting Ichnaea Application...");

        User admin = new User("admin", "admin", new Role(RoleName.ADMIN));

        LOGGER.info("Admin user created: {}", new UserService().save(admin));


        EventQueue.invokeLater(() -> {
            try {
                AppUI window = new SideNavApp(
                        APP_TITLE,
                        BRAND,
                        ICON_PATH,
                        ICON_PATH);
                ViewLoader.INSTANCE.loadViews(window, VIEW_CLASS_PATH);
                ControllerLoader.INSTANCE.loadControllers(CONTROLLER_CLASS_PATH);
                Objects.requireNonNull(ControllerLoader.INSTANCE.getController("SignIn")).show();
            } catch (Exception e) {
                LOGGER.error("Could not start application", e);
            }
        });
    }
}
