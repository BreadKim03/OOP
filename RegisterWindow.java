package UserMode.PrivateInfo;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import UserMode.User;

public class RegisterWindow extends JFrame
{
    public RegisterWindow()
    {
        setTitle("회원가입");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("이름:");
        JTextField nameInput = new JTextField();

        JLabel idLabel = new JLabel("ID:");
        JTextField idInput = new JTextField();

        JLabel pwLabel = new JLabel("비밀번호:");
        JPasswordField pwInput = new JPasswordField();

        JLabel pwCheckLabel = new JLabel("비밀번호 확인:");
        JPasswordField pwCheckInput = new JPasswordField();

        panel.add(nameLabel);
        panel.add(nameInput);
        panel.add(idLabel);
        panel.add(idInput);
        panel.add(pwLabel);
        panel.add(pwInput);
        panel.add(pwCheckLabel);
        panel.add(pwCheckInput);

        JButton registerButton = new JButton("가입하기");

        registerButton.addActionListener(e ->
        {
            String name = nameInput.getText();
            String id = idInput.getText();
            String pw = new String(pwInput.getPassword());
            String pwCheck = new String(pwCheckInput.getPassword());
            
            if(name.isEmpty())
            {
            	JOptionPane.showMessageDialog(this, "이름은 공백일 수 없습니다.", "오류", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if(id.isEmpty() || pw.isEmpty())
            {
            	JOptionPane.showMessageDialog(this, "ID나 비밀번호는 공백일 수 없습니다.", "오류", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!pw.equals(pwCheck))
            {
                JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (isDuplicateID(id))
            {
                JOptionPane.showMessageDialog(this, "이미 존재하는 ID입니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User newUser = new User(name, id, pw);
            saveUser(newUser);

            JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.");
            dispose();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(registerButton);

        add(panel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private boolean isDuplicateID(String id)
    {
        File file = new File("User/" + id + ".dat");
        return file.exists();
    }

    private void saveUser(User user)
    {
        File dir = new File("User");
        if (!dir.exists()) dir.mkdir();

        File file = new File(dir, user.ID + ".dat");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file)))
        {
            oos.writeObject(user);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
