package org.example.View.dashboardAdmin.menu;

/**
 * Interface for handling menu selection events.
 */
public interface MenuEvent {

    /**
     * Called when a menu item is selected.
     *
     * @param index    the index of the selected menu
     * @param subIndex the sub-index of the selected menu
     * @param action   the action associated with the menu selection
     */
    void menuSelected(int index, int subIndex, MenuAction action);
}
