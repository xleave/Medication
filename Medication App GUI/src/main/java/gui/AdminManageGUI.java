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
        String[] columnNames = { "Username", "Privilege", "Consistent", "Action" };
        Object[][] data = new Object[userList.size()][4];

        for (int i = 0; i < userList.size(); i++) {
            String username = userList.get(i)[0];
            data[i][0] = username;
            data[i][1] = userList.get(i)[2];
            data[i][2] = adminManage.checkUserConsistency(username) ? "Yes" : "No";
            data[i][3] = "Delete/Edit"; // Action buttons
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
            return col == 3;
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            if (col != 3) {
                data[row][col] = value;
                fireTableCellUpdated(row, col);
            }
        }

        @Override
        public Class<?> getColumnClass(int col) {
            if (col == 2) {
                return String.class;
            }
            return getValueAt(0, col).getClass();
        }
    }

    // Button Renderer
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton deleteButton;
        private JButton editButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            deleteButton = new JButton("Delete");
            editButton = new JButton("Edit");
            add(deleteButton);
            add(editButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Button Editor
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton deleteButton;
        private JButton editButton;
        private String userName;
        private AdminManage adminManage;
        private AdminManageGUI adminManageGUI;

        public ButtonEditor(AdminManage adminManage, AdminManageGUI adminManageGUI) {
            this.adminManage = adminManage;
            this.adminManageGUI = adminManageGUI;
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            deleteButton = new JButton("Delete");
            editButton = new JButton("Edit");
            panel.add(deleteButton);
            panel.add(editButton);

            deleteButton.addActionListener(new ActionListener() {
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

            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    String newPassword = JOptionPane.showInputDialog(null,
                            "Enter new password for user: " + userName, "Edit Password",
                            JOptionPane.PLAIN_MESSAGE);
                    if (newPassword != null && !newPassword.trim().isEmpty()) {
                        adminManage.updateUserPassword(userName, newPassword.trim());
                        JOptionPane.showMessageDialog(null, "Password updated successfully.");
                        adminManageGUI.refreshInterface();
                    } else {
                        JOptionPane.showMessageDialog(null, "Password cannot be empty.");
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            userName = (String) table.getValueAt(row, 0);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}