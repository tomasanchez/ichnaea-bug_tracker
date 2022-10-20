package org.ichnaea.core.mvc.controller;

import org.ichnaea.core.mvc.view.View;

public abstract class Controller {

    private View View = new View();

    public void show() {
        this.getView().show();
    }

    protected View getView() {
        return this.View;
    }

    /**
     * Changes the view of the controller.
     *
     * @param View the view to be set
     * @return the Controller instance
     */
    public Controller setView(View View) {
        this.View = View;
        return this;
    }

    public void repaint() {
        this.getView().repaint();
    }

    /**
     * Obtains the controller name. <br/>
     * <p>
     * ? Example: NewObjectController => NewObject
     *
     * @return the controller name
     */
    public String getName() {
        return getClass().getSimpleName().replace("Controller", "");
    }

    /**
     * This method is called upon initialization of the View. The controller can perform its
     * internal setup in this hook. It is only called once per View instance, unlike the
     * onBeforeRendering and onAfterRendering hooks.
     */
    protected abstract void onInit();
}
