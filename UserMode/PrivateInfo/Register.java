package UserMode.PrivateInfo;
import java.util.*;
import java.io.*;
import UserMode.User;

import static UserMode.User.isDuplicateID;
import static UserMode.User.userList;

public class Register {
    Scanner sc = new Scanner(System.in);

    public void register() {
        String name;
        String ID;
        String Password;
        String PasswordCheck;
        System.out.println("name 입력 :");
        name = sc.next();

        while(true) {
            System.out.println("ID 입력 :");
            ID = sc.next();
            if(!isDuplicateID(ID)) {
                System.out.println("이미 존재하는 ID입니다.");
                break;
            }
            System.out.println("다른 ID를 입력하세요.");
        }

        while(true) {
            System.out.println("password 입력 :");
            Password = sc.next();
            System.out.println("password 확인 :");
            PasswordCheck = sc.next();
            if(Password.equals(PasswordCheck)) {
                break;
            }
            System.out.println("비밀번호가 일치하지 않습니다.");
        }
        User newUser = new User(name,ID,Password);
        userList.add(newUser);
        SaveFile(newUser);
        System.out.println("회원가입이 완료되었습니다");
    }

    public static void SaveFile(User user) {
        File dir = new File("UserMode.User");
        if(!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, user.ID+ ".dat");
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            oos.writeObject(user);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
