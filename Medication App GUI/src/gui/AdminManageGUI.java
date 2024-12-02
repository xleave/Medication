package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import services.User;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class AdminManageGUI extends JPanel {

    private User currentUser;

    public AdminManageGUI(User user) {
        this.currentUser = user;
        initialize();
    }

    private void initialize() {
        this.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        this.add(titleLabel, BorderLayout.NORTH);

        // Get a list of all users
        ArrayList<String[]> userList = getAllUsers();

        // Create user forms
        String[] columnNames = { "Username", "Privilege", "Action" };
        Object[][] data = new Object[userList.size()][3];

        for (int i = 0; i < userList.size(); i++) {
            data[i][0] = userList.get(i)[0];
            data[i][1] = userList.get(i)[2];

            // Create a “Delete” button.
            JButton deleteButton = new JButton("Delete");
            data[i][2] = deleteButton;

            String userName = userList.get(i)[0];

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Confirmation dialog pops up
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete user: " + userName + "?", "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Delete user
                        deleteUser(userName);
                    }
                }
            });
        }

        // Use a customized table model
        UserTableModel model = new UserTableModel(data, columnNames);
        JTable userTable = new JTable(model);
        userTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane tableScrollPane = new JScrollPane(userTable);
        this.add(tableScrollPane, BorderLayout.CENTER);
    }

    private ArrayList<String[]> getAllUsers() {
        ArrayList<String[]> userList = new ArrayList<>();
        try {
            File userFile = new File("src/resources/users/users.csv");
            BufferedReader br = new BufferedReader(new FileReader(userFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length >= 3) {
                    // Do not show the current administrator himself
                    if (!userDetails[0].equals(currentUser.getName())) {
                        userList.add(userDetails);
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

    private void deleteUser(String userName) {
        try {
            // Remove users from users.csv
            File userFile = new File("src/resources/users/users.csv");
            File tempFile = new File("src/resources/users/users_temp.csv");

            BufferedReader reader = new BufferedReader(new FileReader(userFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if (trimmedLine.startsWith(userName + ",")) {
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();

            // Delete the original file and rename the temporary file
            if (!userFile.delete()) {
                System.out.println("Failed to delete the original user file.");
            }
            if (!tempFile.renameTo(userFile)) {
                System.out.println("Failed to rename the temp user file.");
            }

            // Delete the user's medication file
            File medicationFile = new File("src/resources/medications/" + userName + "_medications.csv");
            if (medicationFile.exists()) {
                medicationFile.delete();
            }

            // Refresh the interface
            removeAll();
            initialize();
            revalidate();
            repaint();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
            return col == 2; // Only the action column is editable
        }

        @Override
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }

        @Override
        public Class<?> getColumnClass(int col) {
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
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String userName;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    // Button Editor
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete user: " + userName + "?", "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Delete user
                        deleteUser(userName);
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            userName = (String) table.getValueAt(row, 0);
            button.setText((value != null) ? "Delete" : "");
            return button;
        }
    }
}