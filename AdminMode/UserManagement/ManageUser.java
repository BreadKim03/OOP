package AdminMode.UserManagement;

import java.io.*;
import UserMode.User;

public class ManageUser{

    public static String[][] getAllUserIds() {
        File folder = new File("User");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".dat"));
        if (files == null || files.length == 0) {
        	return new String[0][1];
        }

        String[][] data = new String[files.length][1];
        for (int i = 0; i < files.length; i++) {
            try (ObjectInputStream readf = new ObjectInputStream(new FileInputStream(files[i]))) {
                User user = (User) readf.readObject();
                data[i][0] = user.getID();
            } catch (Exception e) {
                data[i][0] = "오류";
            }
        }
        return data;
    }

    public static User loadUser(String id) {
        File file = new File("User/" + id + ".dat");
        if (!file.exists()){
        	return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (User) ois.readObject();
        } 
        catch (Exception e) {
            return null;
        }
    }

    public static boolean deleteUser(String id) {
        File file = new File("User/" + id + ".dat");

        if (!file.exists()) {
            System.out.println("삭제 실패: " + id + " 파일이 존재하지 않습니다.");
            return false;
        }
        
        boolean deleted = file.delete();

        if (deleted) {
            System.out.println("삭제 성공: " + id + " 유저 정보가 삭제되었습니다.");
            return true;
        } else {
            System.out.println("삭제 실패: 파일을 삭제하지 못했습니다.");
            return false;
        }
    }
}
