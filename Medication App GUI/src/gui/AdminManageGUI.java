package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import modules.User;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

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

        // 获取所有用户列表
        ArrayList<String[]> userList = getAllUsers();

        // 创建用户表格
        String[] columnNames = { "Username", "Privilege", "Action" };
        Object[][] data = new Object[userList.size()][3];

        for (int i = 0; i < userList.size(); i++) {
            data[i][0] = userList.get(i)[0];
            data[i][1] = userList.get(i)[2];

            // 创建“删除”按钮
            JButton deleteButton = new JButton("Delete");
            data[i][2] = deleteButton;

            String userName = userList.get(i)[0];

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 弹出确认对话框
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete user: " + userName + "?", "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // 删除用户
                        deleteUser(userName);
                    }
                }
            });
        }

        // 使用自定义的表格模型
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
                    // 不显示当前管理员自己
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
            // 从 users.csv 中删除用户
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

            // 删除原文件，重命名临时文件
            if (!userFile.delete()) {
                System.out.println("Failed to delete the original user file.");
            }
            if (!tempFile.renameTo(userFile)) {
                System.out.println("Failed to rename the temp user file.");
            }

            // 删除用户的药品文件
            File medicationFile = new File("src/resources/medications/" + userName + "_medications.csv");
            if (medicationFile.exists()) {
                medicationFile.delete();
            }

            // 刷新界面
            removeAll();
            initialize();
            revalidate();
            repaint();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 自定义表格模型
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
            return col == 2; // 仅操作列可编辑
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

    // 按钮渲染器
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

    // 按钮编辑器
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
                    // 弹出确认对话框
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete user: " + userName + "?", "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // 删除用户
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