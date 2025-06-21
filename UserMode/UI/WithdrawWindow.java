package UserMode.UI;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import UserMode.User;
import UserMode.PrivateInfo.*;

public class WithdrawWindow extends JFrame {
	private User user;
	private JFrame BookMenu;
	
	public WithdrawWindow(User loginUser, JFrame BookMenu) {
		this.BookMenu = BookMenu;
		this.user = loginUser;
		setTitle("회원탈퇴");
		setSize(350,200);
		setLocationRelativeTo(null);
		
		JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JLabel idLabel = new JLabel("ID:");
		JTextField idField = new JTextField(user.getID());
		idField.setEditable(false);
		
		JLabel pwLabel = new JLabel("비밀번호:");
		JPasswordField pwField = new JPasswordField();
		
		panel.add(idLabel);
		panel.add(idField);
		panel.add(pwLabel);
		panel.add(pwField);
		
		JButton withdrawButton = new JButton("탈퇴하기");
		JButton cancelButton = new JButton("취소");
		
		withdrawButton.addActionListener(e -> {
            String inputPw = new String(pwField.getPassword());
            if (inputPw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요.", "오류", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!inputPw.equals(user.getPassword())) {
                JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int choice = JOptionPane.showConfirmDialog(this, "정말 탈퇴하시겠습니까?",
                    "회원탈퇴 확인", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                File file = new File("User/" + user.getID() + ".dat");
                if (file.delete()) {
                    JOptionPane.showMessageDialog(this, "회원탈퇴가 완료되었습니다.");
                    dispose(); 
                    if (BookMenu != null) {
                        BookMenu.dispose(); 
                    }
                    SwingUtilities.invokeLater(() -> new SignIn()); 
                } else {
                    JOptionPane.showMessageDialog(this, "회원탈퇴에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(withdrawButton);
        buttonPanel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
	}
}
