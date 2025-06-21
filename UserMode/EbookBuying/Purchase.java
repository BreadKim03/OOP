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
}
