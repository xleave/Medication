package gui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import services.User;
import services.AdminManage;

public class AdminManageGUI extends JPanel {

    private User currentUser;
    private AdminManage adminManage;

    public AdminManageGUI(User user) {
        this.currentUser = user;
        this.adminManage = new AdminManage(currentUser);
        initialize();
    }

    private void initialize() {
        this.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titleLabel, BorderLayout.NORTH);

        // Get a list of all users
        ArrayList<String[]> userList = adminManage.getAllUsers();

        // Create user forms
        String[] columnNames = { "Username", "Privilege", "Action" };
        Object[][] data = new Object[userList.size()][3];

        for (int i = 0; i < userList.size(); i++) {
            data[i][0] = userList.get(i)[0];
            data[i][1] = userList.get(i)[2];
            data[i][2] = "Delete"; // Action button
        }

        UserTableModel model = new UserTableModel(data, columnNames);
        JTable userTable = new JTable(model);
        userTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("Action").setCellEditor(new ButtonEditor(adminManage, this));

        JScrollPane tableScrollPane = new JScrollPane(userTable);
        this.add(tableScrollPane, BorderLayout.CENTER);
    }

    public void refreshInterface() {
        removeAll();
        initialize();
        revalidate();
        repaint();
    }

    // Customize the form model
    class UserTableModel extends AbstractTableModel {

        private String[] columnNames;
        private Object[][] data;

        public UserTableModel(Object[][] data, String[] columnNames) {
            this.data = data;
            this.columnNames = columnNames;
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return col == 2;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            if (col != 2) {
                data[row][col] = value;
                fireTableCellUpdated(row, col);
            }
        }

        @Override
        public Class<?> getColumnClass(int col) {
            if (col == 2) {
                return Object.class;
            }
            return getValueAt(0, col).getClass();
        }
    }

    // Button Renderer
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Button Editor
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private String userName;
        private AdminManage adminManage;
        private AdminManageGUI adminManageGUI;

        public ButtonEditor(AdminManage adminManage, AdminManageGUI adminManageGUI) {
            this.adminManage = adminManage;
            this.adminManageGUI = adminManageGUI;
            button = new JButton("Delete");
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete user: " + userName + "?", "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        adminManage.deleteUser(userName);
                        adminManageGUI.refreshInterface();
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            userName = (String) table.getValueAt(row, 0);
            button.setText((value != null) ? value.toString() : "Delete");
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Delete"; 
        }
    }
}