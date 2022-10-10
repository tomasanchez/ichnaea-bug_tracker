package org.ichnaea.core.mvc.controller;

import org.ichnaea.core.mvc.view.MVCView;

public abstract class MVCController {

    private MVCView MVCView = new MVCView();

    public void show() {
        this.getView().show();
    }

    private MVCView getView() {
        return this.MVCView;
    }

    /**
     * Changes the view of the controller.
     *
     * @param MVCView the view to be set
     * @return the Controller instance
     */
    public MVCController setView(MVCView MVCView) {
        this.MVCView = MVCView;
        return this;
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

}
