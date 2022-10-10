package org.ichnaea.core.mvc.view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MVCView {

    protected final JPanel panel;

    protected final Map<String, Object> viewModel = new HashMap();

    protected JFrame frame = null;

    public MVCView() {
        this.panel = new JPanel();
        this.panel.setBorder(BorderFactory.createEmptyBorder(30, 15, 10, 15));
        this.panel.setLayout(null);
        this.panel.setBackground(Color.WHITE);
    }

    public void show() {
        if (Objects.nonNull(this.frame)) {
            this.frame.add(this.panel);
        }
    }

    public Map<String, Object> getModel() {
        return this.viewModel;
    }

    public MVCView set(String key, Component value) {
        this.viewModel.put(key, value);
        this.panel.add(value);
        return this;
    }

    public MVCView setFrame(JFrame frame) {
        this.frame = frame;
        return this;
    }

    /**
     * Obtains the View name. <br/>
     * <p>
     * ? Example: NewObjectView => NewObject
     *
     * @return the View name
     */
    public String getName() {
        return getClass().getSimpleName().replace("View", "");
    }

}
