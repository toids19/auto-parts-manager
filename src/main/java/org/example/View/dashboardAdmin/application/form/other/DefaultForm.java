package org.example.View.dashboardAdmin.application.form.other;

import com.formdev.flatlaf.FlatClientProperties;

/**
 * A default form panel with a single label.
 */
public class DefaultForm extends javax.swing.JPanel {

    /**
     * Constructs a new DefaultForm with the specified text.
     *
     * @param text the text to display on the form
     */
    public DefaultForm(String text) {
        initComponents();
        lb.putClientProperty(FlatClientProperties.STYLE, "font:$h1.font");
        lb.setText(text);
    }

    /**
     * Initializes the components of the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        lb = new javax.swing.JLabel();

        lb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb.setText("Form");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lb, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }// </editor-fold>

    // Variables declaration - do not modify
    private javax.swing.JLabel lb;
    // End of variables declaration
}
