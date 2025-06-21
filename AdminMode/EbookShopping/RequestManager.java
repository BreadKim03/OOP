package AdminMode.EbookShopping;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public class RequestManager extends JFrame {
    private JTable requestTable;
    private DefaultTableModel tableModel;
    private List<Path> pendingFiles;

    public RequestManager() {
        setTitle("대출 신청 관리");
        setSize(600, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "이름", "책", "신청시간", "상태"};
        tableModel = new DefaultTableModel(columnNames, 0);
        requestTable = new JTable(tableModel);

        
        pendingFiles = new ArrayList<>();
        File dir = new File("Permissions");
        if (!dir.exists()) dir.mkdir();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".per"));
        if (files != null) {
            for (File file : files) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    br.readLine(); // 헤더 건너뛰기
                    String line = br.readLine();
                    if (line != null) {
                        String[] fields = line.split(",");
                        if (fields.length >= 5 && fields[4].equals("대기중")) {
                            pendingFiles.add(file.toPath());
                            tableModel.addRow(fields);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        JButton acceptButton = new JButton("수락");
        JButton rejectButton = new JButton("거절");

        acceptButton.addActionListener(e -> handleRequest("허락됨"));
        rejectButton.addActionListener(e -> handleRequest("취소됨"));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);

        setLayout(new BorderLayout());
        add(new JScrollPane(requestTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void handleRequest(String newStatus) {
        int row = requestTable.getSelectedRow();
        if (row >= 0) {
            Path file = pendingFiles.get(row);
            try {
                // 파일 읽기
                List<String> lines = Files.readAllLines(file);
                if (lines.size() >= 2) {
                    // 상태 변경
                    String[] fields = lines.get(1).split(",");
                    fields[4] = newStatus;
                    lines.set(1, String.join(",", fields));
                    // 파일에 다시 저장
                    Files.write(file, lines, StandardOpenOption.TRUNCATE_EXISTING);
                    // 테이블에서 제거
                    pendingFiles.remove(row);
                    tableModel.removeRow(row);
                    JOptionPane.showMessageDialog(this,
                        "신청이 " + newStatus + "로 처리되었습니다.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "파일 처리 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RequestManager());
    }
}
