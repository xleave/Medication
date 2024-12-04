package gui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import services.MedicationManage;
import services.User;
import services.AdminManage;

public class AdminManageGUI extends JPanel {

    private User currentUser;
    private AdminManage adminManage;
    private Font applicationFont;

    public AdminManageGUI(User user) {
        this.currentUser = user;
        this.adminManage = new AdminManage(currentUser);
        initializeFont();
        initialize();
    }

    private void initializeFont() {
        try {
            applicationFont = MedicationManage
                    .loadFont("src/main/resources/fonts/RobotoCondensed-VariableFont_wght.ttf");
        } catch (RuntimeException e) {
            System.out.println("Font could not be found!");
            throw e;
        }
    }

    private void initialize() {
        this.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(applicationFont.deriveFont(Font.BOLD, 24f));
        this.add(titleLabel, BorderLayout.NORTH);

        // Get a list of all users
        ArrayList<String[]> userList = adminManage.getAllUsers();

        // Create user forms
        String[] columnNames = { "Username", "Privilege", "Consistent", "Action" };
        Object[][] data = new Object[userList.size()][4];

        for (int i = 0; i < userList.size(); i++) {
            String username = userList.get(i)[0];
            String privilege = userList.get(i)[2];
            data[i][0] = username;
            data[i][1] = privilege;
            data[i][2] = adminManage.checkUserConsistency(username) ? "Yes" : "No";
            data[i][3] = "Actions"; 
        }

        UserTableModel model = new UserTableModel(data, columnNames);
        JTable userTable = new JTable(model);
        userTable.setRowHeight(40); // Set the table not to automatically adjust column widths

        // Set the table not to automatically adjust column widths
        userTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set the content to be centered
        TableColumnModel columnModel = userTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200); // Username
        columnModel.getColumn(1).setPreferredWidth(150); // Privilege
        columnModel.getColumn(2).setPreferredWidth(150); // Consistent
        columnModel.getColumn(3).setPreferredWidth(500); // Action

        // Set the content to be centered
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columnNames.length - 1; i++) { // Action列不需要居中
            centerRenderer.setFont(applicationFont.deriveFont(14f));
            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set the header font
        JTableHeader header = userTable.getTableHeader();
        header.setFont(applicationFont.deriveFont(Font.BOLD, 14f));

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
        private JButton changePrivilegeButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            deleteButton = createButton("Delete");
            editButton = createButton("Edit");
            changePrivilegeButton = createButton("Change Privilege");
            add(deleteButton);
            add(editButton);
            add(changePrivilegeButton);
        }

        private JButton createButton(String text) {
            JButton button = new JButton(text);
            button.setPreferredSize(new Dimension(150, 30)); // Uniform button sizes
            button.setFont(applicationFont.deriveFont(12f));
            return button;
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
        private JButton changePrivilegeButton;
        private String userName;
        private AdminManage adminManage;
        private AdminManageGUI adminManageGUI;

        public ButtonEditor(AdminManage adminManage, AdminManageGUI adminManageGUI) {
            this.adminManage = adminManage;
            this.adminManageGUI = adminManageGUI;
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            deleteButton = createButton("Delete");
            editButton = createButton("Edit");
            changePrivilegeButton = createButton("Change Privilege");
            addButtons();
        }

        private JButton createButton(String text) {
            JButton button = new JButton(text);
            button.setPreferredSize(new Dimension(110, 30)); // Uniform button sizes
            button.setFont(applicationFont.deriveFont(12f));
            return button;
        }

        private void addButtons() {
            panel.add(deleteButton);
            panel.add(editButton);
            panel.add(changePrivilegeButton);

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

            changePrivilegeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    String[] options = { "user", "admin" };
                    String currentPrivilege = getCurrentPrivilege(userName);
                    String newPrivilege = (String) JOptionPane.showInputDialog(null,
                            "Select new privilege for user: " + userName,
                            "Change Privilege",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            currentPrivilege);
                    if (newPrivilege != null && !newPrivilege.equals(currentPrivilege)) {
                        adminManage.updateUserPrivilege(userName, newPrivilege);
                        JOptionPane.showMessageDialog(null, "Privilege updated successfully.");
                        adminManageGUI.refreshInterface();
                    }
                }

                /**
                 * Get the current user's permissions
                 * 
                 * @param userName User name
                 * @return Current permissions
                 */
                private String getCurrentPrivilege(String userName) {
                    ArrayList<String[]> users = adminManage.getAllUsers();
                    for (String[] user : users) {
                        if (user[0].equals(userName)) {
                            return user[2];
                        }
                    }
                    return "user"; // Default permissions
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