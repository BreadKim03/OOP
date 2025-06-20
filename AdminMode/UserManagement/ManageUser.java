package AdminMode.UserManagement;

import UserMode.User;
import java.io.*;
import java.util.Scanner;

public class ManageUser {

    //모든 유저 출력
    private void AllUsers() {
        File folder = new File("User");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".dat"));

        if (files == null || files.length == 0) {
            System.out.println("등록된 유저가 없습니다.");
            return;
        }

        System.out.println("\n📋 등록된 유저 목록:");
        for (File file : files) {
            String userId = file.getName().replace(".dat", "");
            System.out.println("- " + userId);
        }
    }

    //특정 유저 개인 정보 출력
    private void searchUser() {
        Scanner sc = new Scanner(System.in);
        System.out.print("검색할 유저 ID 입력: ");
        String id = sc.nextLine();

        File file = new File("User/" + id + ".dat");

        if (!file.exists()) {
            System.out.println(id + "해당 ID의 유저는 존재하지 않습니다.");
            return;
        }

        ObjectInputStream ois = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

            User user = (User) ois.readObject();

            System.out.println("\n유저 정보:");
            System.out.println("ID: " + user.getID());
            System.out.println("비밀번호: " + user.getPassword());

        } 
        catch (IOException e) {
            System.out.println("파일을 읽는 도중 오류가 발생했습니다");
        } 
        catch (ClassNotFoundException e) {
            System.out.println("User 클래스를 찾을 수 없습니다!");
        } 
        finally {
        	
            try {
                if (ois != null) {
                    ois.close();
                }
            } 
            catch (IOException e) {
                System.out.println("파일 닫기 중 오류 발생!");
            }
        }
    }

    public void Menu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== 유저 관리 메뉴 =====");
            System.out.println("1. 전체 유저 목록 보기");
            System.out.println("2. 특정 유저 개인 정보 출력");
            System.out.println("3. 유저 삭제");
            System.out.println("0. 종료하기");

            System.out.print("항목을 선택해주세요: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    AllUsers();
                    break;
                case 2:
                    searchUser();
                    break;
                case 3:
                    new DeleteUser().deleteUser();
                    break;
                case 0:
                    System.out.println("유저 관리 모드가 종료됩니다.");
                    return;
                default:
                    System.out.println("해당 항목은 존재하지 않습니다 다시 시도해주세요.");
            }
        }
    }
}
