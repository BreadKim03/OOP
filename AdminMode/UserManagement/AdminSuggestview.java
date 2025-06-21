package AdminMode.UserManagement;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class AdminSuggestview extends JFrame {

    private DefaultListModel<String> model;
    private JList<String> list;
    private JTextArea contentArea;

    public AdminSuggestview() {
        setTitle("유저 건의사항 확인");
        setSize(600, 400);
        setLocationRelativeTo(null);

        model = new DefaultListModel<>();
        list = new JList<>(model);
        contentArea = new JTextArea();
        contentArea.setEditable(false);

        loadSuggestions();

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedSuggestion();
            }
        });

        JButton deleteBtn = new JButton("선택된 건의 삭제");
        deleteBtn.addActionListener(e -> deleteSelectedSuggestion());

        // 레이아웃 구성
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        rightPanel.add(deleteBtn, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(list), rightPanel);
        split.setDividerLocation(200);

        add(split);
        setVisible(true);
    }

    private void loadSuggestions() {
        model.clear();
        File fileD = new File("Requests");
        File[] userD = fileD.listFiles(File::isDirectory);

        if (userD != null) {
            for (File userDir : userD) {
                String userId = userDir.getName();
                File[] userFiles = userDir.listFiles((dir, name) -> name.endsWith(".txt"));
                if (userFiles != null) {
                    for (File f : userFiles) {
                        model.addElement(userId + "/" + f.getName()); 
                    }
                }
            }
        }
    }

    //건의사항 읽기
    private void showSelectedSuggestion() {
        String selected = list.getSelectedValue();
        if (selected != null) {
            File file = new File("Requests/" + selected);
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                contentArea.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    contentArea.append(line + "\n");
                }
            } catch (IOException ex) {
                contentArea.setText("파일 읽기 실패: " + ex.getMessage());
            }
        }
    }

    //건의사항 삭제
    private void deleteSelectedSuggestion() {
        String selected = list.getSelectedValue();
        if (selected == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "해당 내용을 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        File file = new File("Requests/" + selected);
        if (file.exists() && file.delete()) {
            JOptionPane.showMessageDialog(this, "삭제되었습니다.");
            loadSuggestions();
            contentArea.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "삭제에 실패했습니다. 파일에 오류가 있는지 확인해주세요.");
        }
    }
}