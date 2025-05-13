package org.example.View.dashboardAdmin.menu;

/**
 * An action associated with a menu item.
 */
public class MenuAction {

    private boolean cancel = false;

    /**
     * Checks if the action is canceled.
     *
     * @return true if the action is canceled, false otherwise
     */
    protected boolean isCancel() {
        return cancel;
    }

    /**
     * Cancels the action.
     */
    public void cancel() {
        this.cancel = true;
    }
}
