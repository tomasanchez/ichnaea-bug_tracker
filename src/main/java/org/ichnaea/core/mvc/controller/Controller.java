package org.ichnaea.core.mvc.controller;

import org.ichnaea.core.mvc.view.View;
import org.ichnaea.core.security.auth.SecurityContext;

public abstract class Controller {

    private static SecurityContext securityContext;
    private View View = new View();

    public static SecurityContext getSecurityContext() {
        return securityContext;
    }

    public static void setSecurityContext(SecurityContext securityContext) {
        Controller.securityContext = securityContext;
    }

    /**
     * Replaces the previous controller view with a new one.
     */
    public void show() {
        onBeforeRendering();
        getView().show();
        onAfterRendering();
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

    /**
     * Repaints the controller view.
     */
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

    /**
     * This method is called every time the View is rendered, before the Renderer is called and the
     * Components are placed in the JFrame. It can be used to perform clean-up-tasks and
     * set-up-task before re-rendering.
     */
    protected abstract void onBeforeRendering();

    /**
     * This method is called every time the View is rendered, after the Components are placed in the
     * JFrame. It can be used to apply additional changes to the Model after the Rendering has
     * finished.
     */
    protected abstract void onAfterRendering();
}
