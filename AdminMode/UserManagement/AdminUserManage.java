package AdminMode.UserManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminUserManage extends JFrame {
    private JTable userTable;
    private JTextField inputField;
    private JLabel resultLabel;
    private DefaultTableModel tableModel;

    public AdminUserManage() {
        setTitle("유저 관리 GUI");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"ID"};
        tableModel = new DefaultTableModel(ManageUser.getAllUserIds(), columns);
        userTable = new JTable(tableModel);
        userTable.setRowHeight(28);

        JScrollPane tableScroll = new JScrollPane(userTable);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JLabel inputLabel = new JLabel("ID: ");

        inputField = new JTextField(15);

        JButton viewBtn = new JButton("개인 정보 조회");
        JButton deleteBtn = new JButton("삭제");

        resultLabel = new JLabel(" ");
        resultLabel.setForeground(new Color(30, 60, 150));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        viewBtn.addActionListener((ActionEvent e) -> ViewUserInfo());
        deleteBtn.addActionListener((ActionEvent e) -> DeleteUser());

        controlPanel.add(inputLabel);
        controlPanel.add(inputField);
        controlPanel.add(viewBtn);
        controlPanel.add(deleteBtn);

        add(resultLabel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void ViewUserInfo() {
        String id = inputField.getText().trim();
        UserMode.User user = ManageUser.loadUser(id);
        if (user == null) {
            resultLabel.setText("존재하지 않는 유저 ID입니다.");
        } else {
            resultLabel.setText("이름: " + user.getName() + "  ID: " + user.getID() + "  PW: " + user.getPassword());
        }
    }

    private void DeleteUser() {
        String id = inputField.getText().trim();
        boolean success = ManageUser.deleteUser(id);
        if (success) {
            resultLabel.setText("삭제 완료: " + id);
            tableModel.setDataVector(ManageUser.getAllUserIds(), new String[]{"ID"});
        } else {
            resultLabel.setText("삭제 실패: 삭제 할 유저가 없습니다.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminUserManage());
    }
}
