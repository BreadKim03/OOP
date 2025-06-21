package AdminMode.EbookManagement;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

public class DeleteBook
{
	public DeleteBook(String selected, Component parentComponent)
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

                    if (title != null && title.equals(selected))
                    {
                        boolean deleted = file.delete();

                        if (deleted)
                        {
                            JOptionPane.showMessageDialog(parentComponent, "도서가 삭제되었습니다.");
                        }
                        
                        else
                        {
                            JOptionPane.showMessageDialog(parentComponent, "삭제 실패: 파일을 삭제할 수 없습니다.\n경로: " + file.getAbsolutePath());
                        }

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