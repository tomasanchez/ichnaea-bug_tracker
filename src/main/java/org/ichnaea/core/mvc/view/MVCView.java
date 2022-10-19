package org.ichnaea.core.mvc.view;

import org.ichnaea.core.ui.app.AppUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MVCView {

    protected final JPanel panel;

    protected final Map<String, Object> viewModel = new HashMap<>();

    public AppUI frame = null;


    public MVCView() {
        this.panel = new JPanel();
        this.panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        this.panel.setLayout(null);
        this.panel.setBackground(Color.WHITE);
    }

    public boolean isFullScreen() {
        return true;
    }

    public MVCView setFrame(AppUI frame) {
        this.frame = frame;
        return this;
    }

    public void remove(Component component) {
        panel.remove(component);
        repaint();
    }

    public void show() {
        if (Objects.nonNull(this.frame)) {
            this.frame.setBody(this.panel, isFullScreen());
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

    public MVCView set(String key, Component value, String constraints) {
        this.viewModel.put(key, value);
        this.panel.add(value, constraints);
        return this;
    }

    public MVCView set(String key, Component value, JPanel container) {
        this.viewModel.put(key, value);
        container.add(value);
        return this;
    }

    public MVCView set(Component value) {
        this.panel.add(value);
        return this;
    }

    public MVCView set(Component value, String constraints) {
        this.panel.add(value, constraints);
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

    public AppUI getApp() {
        return this.frame;
    }

    public void repaint() {
        this.panel.revalidate();
        this.panel.repaint();
    }
}
