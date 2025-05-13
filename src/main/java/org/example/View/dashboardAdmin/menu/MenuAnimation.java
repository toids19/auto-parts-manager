package org.example.View.dashboardAdmin.menu;

import com.formdev.flatlaf.util.Animator;

import java.util.HashMap;

/**
 * Utility class for animating menu items.
 */
public class MenuAnimation {

    // HashMap to store menu items and their associated animators
    private static final HashMap<MenuItem1, Animator> hash = new HashMap<>();

    /**
     * Animates the specified menu item to show or hide.
     *
     * @param menu the menu item to animate
     * @param show true to animate showing the menu item, false to animate hiding it
     */
    public static void animate(MenuItem1 menu, boolean show) {
        // Stop any ongoing animation for this menu item
        if (hash.containsKey(menu) && hash.get(menu).isRunning()) {
            hash.get(menu).stop();
        }

        // Set the menu item's visibility
        menu.setMenuShow(show);

        // Create and start a new animator for this menu item
        Animator animator = new Animator(400, new Animator.TimingTarget() {
            @Override
            public void timingEvent(float f) {
                // Set the animation progress
                if (show) {
                    menu.setAnimate(f);
                } else {
                    menu.setAnimate(1f - f);
                }
                menu.revalidate(); // Ensure proper layout revalidation during animation
            }

            @Override
            public void end() {
                // Remove the animator from the HashMap when animation ends
                hash.remove(menu);
            }
        });

        // Configure animator settings
        animator.setResolution(1);
        animator.setInterpolator((float f) -> (float) (1 - Math.pow(1 - f, 3))); // Cubic easing
        animator.start(); // Start the animator
        hash.put(menu, animator); // Store the menu item and its animator in the HashMap
    }
}
