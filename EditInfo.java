package UserMode.PrivateInfo;

import UserMode.User;
import javax.swing.*;
import java.awt.*;

import static UserMode.User.isDuplicateID;

public class EditInfo extends JPanel {
    public EditInfo(User loginUser) {
        setLayout(new FlowLayout());

        JButton nameBtn = new JButton("이름 변경");
        JButton pwBtn = new JButton("비밀번호 변경");

        nameBtn.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(this, "새 이름을 입력하세요:");
            if(isDuplicateID(newName))
            {
                JOptionPane.showMessageDialog(this, "이미 존재하는 ID입니다.");
            }
            else if (newName != null && !newName.trim().isEmpty()) {
                loginUser.setName(newName);
                Register.SaveFile(loginUser);
                JOptionPane.showMessageDialog(this, "이름이 변경되었습니다.");
            }
        });

        pwBtn.addActionListener(e -> {
            JPasswordField curPw = new JPasswordField();
            JPasswordField newPw = new JPasswordField();
            JPasswordField checkPw = new JPasswordField();

            Object[] msg = {
                    "현재 비밀번호:", curPw,
                    "새 비밀번호:", newPw,
                    "새 비밀번호 확인:", checkPw
            };

            int opt = JOptionPane.showConfirmDialog(this, msg, "비밀번호 변경", JOptionPane.OK_CANCEL_OPTION);

            if (opt == JOptionPane.OK_OPTION) {
                String current = new String(curPw.getPassword());
                String newPass = new String(newPw.getPassword());
                String confirm = new String(checkPw.getPassword());

                if (!loginUser.getPassword().equals(current)) {
                    JOptionPane.showMessageDialog(this, "현재 비밀번호가 일치하지 않습니다.");
                } else if (!newPass.equals(confirm)) {
                    JOptionPane.showMessageDialog(this, "새 비밀번호가 일치하지 않습니다.");
                } else {
                    loginUser.setPassword(newPass);
                    Register.SaveFile(loginUser);
                    JOptionPane.showMessageDialog(this, "비밀번호가 변경되었습니다.");
                }
            }
        });

        add(new JLabel("회원 정보 수정", SwingConstants.CENTER));
        add(nameBtn);
        add(pwBtn);
    }
}