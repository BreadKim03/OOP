package AdminMode.EbookManagement;

import java.awt.Component;
import java.io.*;
import javax.swing.*;

import AdminMode.UI.BookEditor;

public class EditBook
{
    public EditBook(String selected, Component parentComponent, Runnable onComplete)
    {
        File dir = new File("Books");
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".nov"));

        if (files != null)
        {
            for (File file : files)
            {
                try (BufferedReader reader = new BufferedReader(new FileReader(file)))
                {
                    String title = reader.readLine();
                    String writer = reader.readLine();
                    String genre = reader.readLine();
                    String price = reader.readLine();

                    if (title != null && title.equals(selected))
                    {
                        new BookEditor((JFrame) parentComponent, file, title, writer, genre, price, onComplete).setVisible(true);
                        break;
                    }
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(parentComponent, "오류 발생: " + ex.getMessage());
                }
            }
        }
    }
}