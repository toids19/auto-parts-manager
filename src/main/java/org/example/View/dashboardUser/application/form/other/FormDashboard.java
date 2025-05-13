package org.example.View.dashboardUser.application.form.other;

import com.formdev.flatlaf.FlatClientProperties;
import raven.toast.Notifications;

import javax.swing.*;

/**
 * A form panel representing the user dashboard.
 */
public class FormDashboard extends javax.swing.JPanel {

    /**
     * Constructs a new FormDashboard.
     */
    public FormDashboard() {
        initComponents();
        lb.putClientProperty(FlatClientProperties.STYLE, "font:$h1.font");
    }

    /**
     * Initializes the components of the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        lb = new javax.swing.JLabel();
        javax.swing.JButton jButton1 = new javax.swing.JButton();

        lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb.setText("Dashboard");

        jButton1.setText("Show Notifications Test");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(325, 325, 325)
                                .addComponent(jButton1)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lb)
                                .addGap(173, 173, 173)
                                .addComponent(jButton1)
                                .addContainerGap(237, Short.MAX_VALUE))
        );
    }// </editor-fold>

    /**
     * Action performed when the button is clicked to show notifications.
     *
     * @param evt the action event
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        Notifications.getInstance().show(Notifications.Type.INFO, Notifications.Location.TOP_CENTER, "Hello sample message");
    }

    // Variables declaration - do not modify
    private javax.swing.JLabel lb;
    // End of variables declaration
}
