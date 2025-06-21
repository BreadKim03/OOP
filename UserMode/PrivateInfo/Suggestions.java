package UserMode.PrivateInfo;

import javax.swing.*;
import java.io.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Suggestions {
    public static void writesuggestion(String userID) {
        JTextArea area = new JTextArea(10, 30);
        int result = JOptionPane.showConfirmDialog(null, new JScrollPane(area), "건의사항 작성", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String text = area.getText().trim();

            if (!text.isEmpty()) {
                File userD = new File("Requests/" + userID);
                if (!userD.exists()) userD.mkdirs();

                int count = Objects.requireNonNull(userD.listFiles()).length + 1;
                String fileName = userD.getPath() + "/" + count + ".txt";

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                    writer.write(text);
                    JOptionPane.showMessageDialog(null, "요청이 전송되었습니다.");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "저장 실패: " + e.getMessage());
                }
            }
        }
    }
}