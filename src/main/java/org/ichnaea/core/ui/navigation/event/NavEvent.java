package org.ichnaea.core.ui.navigation.event;

import java.awt.*;

public interface NavEvent {
    /**
     * The navigation event type.
     *
     * @param com  the component that triggered the event
     * @param open weather the navigation is open or closed
     * @return the event type
     */
    boolean navigationPressed(Component com, boolean open);
}