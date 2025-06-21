package UserMode.EbookBuying;

import javax.swing.*;

import AdminMode.EbookManagement.ManageBook;
import AdminMode.UI.BookEditor;
import UserMode.PrivateInfo.SignIn;
import UserMode.User;
import UserMode.PrivateInfo.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.*;



public class Purchase {
	
	public static void MakeRequest(String userId, String userName, String bookTitle) {
	    String requestTime = java.time.LocalDate.now().toString(); // "2025-06-21"
	    String status = "대기중";
	    String fileName = String.format("%s_%s_%s.per",
	        userId,
	        bookTitle,
	        requestTime.replace("-", "")); // "ken4098_소나기_20250621.per"
	    File dir = new File("Permissions");
	    if (!dir.exists()) dir.mkdir();
	    File file = new File(dir, fileName);
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
	        String content = String.format("%s,%s,%s,%s,%s",
	            userId,
	            userName,
	            bookTitle,
	            requestTime,
	            status);
	        bw.write(content);
	    } catch (IOException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "대출 신청에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	public static void purchasePanel(String userId, String userName, String bookTitle, String correctPassword)
	{
	    JFrame frame = new JFrame("전자책 결제");
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setSize(400, 250);
	    frame.setLocationRelativeTo(null);
	    frame.setLayout(new GridLayout(5, 1));

	    JTextField nameField = new JTextField();
	    JTextField accountField = new JTextField();
	    JPasswordField passwordField = new JPasswordField();

	    frame.add(new JLabel("이름:"));
	    frame.add(nameField);
	    frame.add(new JLabel("계좌번호:"));
	    frame.add(accountField);
	    frame.add(new JLabel("비밀번호:"));
	    frame.add(passwordField);

	    JPanel buttonPanel = new JPanel();
	    JButton payButton = new JButton("결제하기");
	    JButton cancelButton = new JButton("취소");
	    buttonPanel.add(payButton);
	    buttonPanel.add(cancelButton);

	    frame.add(buttonPanel);
	    frame.setVisible(true);

	    payButton.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	            String inputName = nameField.getText();
	            String inputAccount = accountField.getText();
	            String inputPassword = new String(passwordField.getPassword());

	            if (!inputPassword.equals(correctPassword))
	            {
	                JOptionPane.showMessageDialog(frame, "비밀번호를 확인해 주세요.", "오류", JOptionPane.WARNING_MESSAGE);
	            }
	            
	            else
	            {
	                MakeRequest(userId, userName, bookTitle);
	                JOptionPane.showMessageDialog(frame, "대출 신청이 완료되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
	                frame.dispose();
	            }
	        }
	    });

	    cancelButton.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	            frame.dispose();
	        }
	    });
	}
}
