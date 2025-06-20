package UserMode.PrivateInfo;
import java.util.*;
import java.io.*;
import UserMode.User;
import UserMode.PrivateInfo.*;

public class EditInfo {
	
	public void editInfo(User loginUser) {
		Scanner sc = new Scanner(System.in);
		System.out.println("1.이름 변경    2.비밀번호 변경");
		String newPassword;
		String newPasswordCheck;
		
		int input = sc.nextInt();
		
		if(input == 1) {
			System.out.println("새 이름 입력 :");
			String newName = sc.next();
			loginUser.name = newName;
		}
		
		else if(input == 2) {
			System.out.println("현재 비밀번호 입력 :");
			String currentPassword = sc.next();
			if(!loginUser.getPassword().equals(currentPassword)) {
				System.out.println("현재 비밀번호가 일치하지 않습니다.");
				return;
			}
			
			while(true) {
				System.out.println("새 비밀번호 입력 :");
				newPassword = sc.next();
				System.out.println("새 비밀번호 재입력 :");
				newPasswordCheck = sc.next();
				
				if(!newPassword.equals(newPasswordCheck)) {
					System.out.println("비밀번호가 일치하지 않습니다.");
					continue;
				}
				if(newPassword.equals(currentPassword)) {
					System.out.println("기존 비밀번호와 동일합니다.");
					continue;
				}
				loginUser.setPassword(newPassword);
				Register.SaveFile(loginUser);
				System.out.println("비밀번호가 변경되었습니다.");
				break;
			}
		}
	}
}
