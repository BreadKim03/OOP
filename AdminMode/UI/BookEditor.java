package AdminMode.UI;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class BookEditor extends JDialog
{
    private JTextField titleField;
    private JTextField writerField;
    private JTextField genreField;
    private JTextField priceField;

    private JButton saveButton;
    private JButton cancelButton;

    private File bookFile;
    private Runnable Save;

    public BookEditor(JFrame parent, File bookFile, String title, String writer, String genre, String price, Runnable onSaveCallback)
    {
        super(parent, "도서 정보 수정", true);
        this.bookFile = bookFile;
        this.Save = onSaveCallback;

        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(400, 200);
        setLocationRelativeTo(parent);

        add(new JLabel("제목 :"));
        titleField = new JTextField(title);
        add(titleField);

        add(new JLabel("작가 :"));
        writerField = new JTextField(writer);
        add(writerField);

        add(new JLabel("장르 :"));
        genreField = new JTextField(genre);
        add(genreField);
        
        add(new JLabel("가격 : "));
        priceField = new JTextField(price);
        add(priceField);
        

        saveButton = new JButton("저장");
        cancelButton = new JButton("취소");

        add(saveButton);
        add(cancelButton);

        saveButton.addActionListener(e ->
        {
            String newTitle = titleField.getText().trim();
            String newWriters = writerField.getText().trim();
            String newGenre = genreField.getText().trim();

            if (newTitle.isEmpty() || newWriters.isEmpty() || newGenre.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "모든 항목을 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try
            {
                BufferedReader reader = new BufferedReader(new FileReader(bookFile));
                reader.readLine();
                reader.readLine();
                reader.readLine();

                StringBuilder restContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) 
                {
                    restContent.append(line).append(System.lineSeparator());
                }
                reader.close();

                BufferedWriter bw = new BufferedWriter(new FileWriter(bookFile, false));
                bw.write(newTitle);
                bw.newLine();
                bw.write(newWriters);
                bw.newLine();
                bw.write(newGenre);
                bw.newLine();
                bw.write(restContent.toString()); // 내용 유지
                bw.close();

            }
            
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(this, "파일 저장 오류: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "정보가 저장되었습니다.");
            if (onSaveCallback != null) onSaveCallback.run();
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());
    }
}
