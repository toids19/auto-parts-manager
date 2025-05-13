package org.example.View.dashboardUser.application.form;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;
import org.example.Manager.*;
import org.example.Model.*;
import org.example.View.*;
import org.example.View.dashboardUser.application.form.other.DefaultForm;
import org.example.View.dashboardUser.application.form.other.FormDashboard;
import org.example.View.dashboardUser.menu.Menu;
import org.example.View.dashboardUser.menu.MenuAction;
import org.example.Main.Main;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

@SuppressWarnings("ALL")
public class MainUserForm extends JLayeredPane {

    public MainUserForm() {
        init();
    }

    private void init() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new MainFormLayout());
        menu = new Menu();
        panelBody = new JPanel(new BorderLayout());
        initMenuArrowIcon();
        menuButton.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.button.background;"
                + "arc:999;"
                + "focusWidth:0;"
                + "borderWidth:0");
        menuButton.addActionListener((ActionEvent e) -> {
            setMenuFull(!menu.isMenuFull());
        });
        initMenuEvent();
        setLayer(menuButton, JLayeredPane.POPUP_LAYER);
        add(menuButton);
        add(menu);
        add(panelBody);
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);
        initMenuArrowIcon();
    }

    private void initMenuArrowIcon() {
        if (menuButton == null) {
            menuButton = new JButton();
        }
        String icon = (getComponentOrientation().isLeftToRight()) ? "org/example/View/dashboardUser/icon/svg/menu_left.svg" : "org/example/View/dashboardUser/icon/svg/menu_right.svg";
        menuButton.setIcon(new FlatSVGIcon("/Users/moldovanutudor/Desktop/PT/PT2024_30421_Moldovanu_Tudor/pt2024_30421_moldovanu_tudor_assignment_3/src/main/java/org/example/View/dashboardUser/icon/svg" + icon, 0.8f));
    }

    private void initMenuEvent() {
        menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
            if (index == 0) {
                if (subIndex == 0) {
                    Main.showForm(new DefaultForm(""));
                } else if (subIndex == 1) {
                    Main.showForm(new CreateOrderView());
                } else if (subIndex == 2) {
                    Main.showForm(new BillListView());
                } else {
                    action.cancel();
                }
            } else if (index == 1) {
                Main.logout();
            } else {
                action.cancel();
            }
        });
    }

    private void setMenuFull(boolean full) {
        String icon;
        if (getComponentOrientation().isLeftToRight()) {
            icon = (full) ? "org/example/View/dashboardUser/icon/svg/menu_left.svg" : "org/example/View/dashboardUser/icon/svg/menu_right.svg";
        } else {
            icon = (full) ? "org/example/View/dashboardUser/icon/svg/menu_right.svg" : "org/example/View/dashboardUser/icon/svg/menu_left.svg";
        }
        menuButton.setIcon(new FlatSVGIcon("org/example/View/dashboardUser/icon/svg/" + icon, 0.8f));
        menu.setMenuFull(full);
        revalidate();
    }

    public void hideMenu() {
        menu.hideMenuItem();
    }

    public void showForm(Component component) {
        panelBody.removeAll();
        panelBody.add(component);
        panelBody.repaint();
        panelBody.revalidate();
    }

    public void setSelectedMenu(int index, int subIndex) {
        menu.setSelectedMenu(index, subIndex);
    }

    private Menu menu;
    private JPanel panelBody;
    private JButton menuButton;

    private class MainFormLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = UIScale.scale(parent.getInsets());
                int x = insets.left;
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
                int menuX = ltr ? x : x + width - menuWidth;
                menu.setBounds(menuX, y, menuWidth, height);
                int menuButtonWidth = menuButton.getPreferredSize().width;
                int menuButtonHeight = menuButton.getPreferredSize().height;
                int menubX;
                if (ltr) {
                    menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f)));
                } else {
                    menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f)));
                }
                menuButton.setBounds(menubX, UIScale.scale(30), menuButtonWidth, menuButtonHeight);
                int gap = UIScale.scale(5);
                int bodyWidth = width - menuWidth - gap;
                int bodyHeight = height;
                int bodyx = ltr ? (x + menuWidth + gap) : x;
                int bodyy = y;
                panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
            }
        }
    }
}
