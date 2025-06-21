package UserMode.PrivateInfo;

import javax.swing.*;

import AdminMode.UI.AdminMenu;

import java.awt.*;
import java.io.*;
import UserMode.User;
import UserMode.UI.BookMenu;
import UserMode.UI.RegisterWindow;

public class SignIn
{

    private static JFrame frame;
    private static JPanel mainPanel;
    
    public SignIn()
    {
    	SwingUtilities.invokeLater(SignIn::createAndShowGUI);
    }
    
    public static void main(String[] args)
    {
        new SignIn();
    }

    //로그인 선택 창
    private static void createAndShowGUI()
    {
        frame = new JFrame("로그인 선택");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 200);

        showSelectionPanel();

        frame.setVisible(true);
    }

    //로그인 선택 창 패널
    private static void showSelectionPanel()
    {
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new GridLayout(1, 3, 10, 10));

        JButton userButton = new JButton("유저 로그인");
        JButton adminButton = new JButton("관리자 로그인");
        JButton registerButton = new JButton("회원가입");

        userButton.addActionListener(e -> showLoginPanel("user"));
        adminButton.addActionListener(e -> showLoginPanel("admin"));
        registerButton.addActionListener(e -> openRegisterWindow());

        mainPanel.add(userButton);
        mainPanel.add(adminButton);
        mainPanel.add(registerButton);

        frame.setTitle("로그인 선택");
        frame.setContentPane(mainPanel);
        frame.revalidate();
        frame.repaint();
    }

    //선택에 따른 로그인 창 띄우기
    private static void showLoginPanel(String mode)
    {
        JPanel loginPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel idLabel = new JLabel("ID:");
        JTextField idInput = new JTextField();

        JButton switchButton = new JButton(
            mode.equals("user") ? "관리자 로그인으로 전환" : "유저 로그인으로 전환"
        );

        JLabel pwLabel = new JLabel("Password:");
        JPasswordField pwInput = new JPasswordField();

        JButton loginButton = new JButton("로그인");

        formPanel.add(idLabel);
        formPanel.add(idInput);
        formPanel.add(switchButton);

        formPanel.add(pwLabel);
        formPanel.add(pwInput);
        formPanel.add(loginButton);

        loginPanel.add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backButton = new JButton("돌아가기");
        bottomPanel.add(backButton);
        loginPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setTitle(mode.equals("user") ? "유저 로그인" : "관리자 로그인");
        frame.setContentPane(loginPanel);
        frame.revalidate();
        frame.repaint();

        backButton.addActionListener(e -> showSelectionPanel());

        switchButton.addActionListener(e ->
        {
            if(mode.equals("user"))
            {
                showLoginPanel("admin");
            }

            else
            {
                showLoginPanel("user");
            }
        });

        loginButton.addActionListener(e ->
        {
            String id = idInput.getText();
            String pw = new String(pwInput.getPassword());

            User loginUser = login(id, pw, mode);
            if (loginUser != null)
            {
                JOptionPane.showMessageDialog(frame, "로그인되었습니다.");
                frame.dispose();
            }
            
            else
            {
                JOptionPane.showMessageDialog(frame, "로그인 실패: ID 또는 비밀번호가 잘못되었습니다.");
            }
        });
    }

    //회원가입 창 띄우기
    private static void openRegisterWindow()
    {
        RegisterWindow registerWindow = new RegisterWindow();
        registerWindow.setVisible(true);
    }

    //ID와 비밀번호 확인
    private static User login(String id, String password, String mode)
    {
        String folder = mode.equals("admin") ? "Admin" : "User";
        File file = new File(folder + "/" + id + ".dat");
        boolean isAdmin = mode.equals("admin") ? true : false;

        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file)))
        {
            User user = (User) ois.readObject();
            if (user.getPassword().equals(password) && isAdmin == false)
            {
            	new BookMenu(user);
                return user;
            }
            
            if(user.getPassword().equals(password) && isAdmin == true)
            {
            	new AdminMenu();
            	return user;
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}