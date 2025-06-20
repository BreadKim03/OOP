package AdminMode.UserManagement;

import java.io.File;
import java.util.Scanner;

public class DeleteUser {

    public void deleteUser() {
        Scanner sc = new Scanner(System.in);
        System.out.print("\n삭제할 유저 ID를 입력: ");
        String inputId = sc.nextLine();

        File userFile = new File("User/" + inputId + ".dat");

        if (!userFile.exists()) {
            System.out.println("ID '" + inputId + "'를 가진 유저는 존재하지 않습니다.");
            return;
        }

        if (userFile.exists()) {
            if (userFile.delete()) {
                System.out.println(inputId + " 유저 정보가 삭제되었습니다.");
            } 
            else {
                System.out.println("파일 삭제에 실패했습니다.");
            }
        } 
        
    }
}
