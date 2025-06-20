package AdminMode.EbookManagement;

import java.io.*;
import java.util.*;

public class ManageBook
{

    public List<String> getBookTitles(String directoryPath)
    {
        List<String> titles = new ArrayList<>();
        File folder = new File(directoryPath);

        if (!folder.exists() || !folder.isDirectory())
        {
            System.out.println("디렉토리가 존재하지 않거나 올바르지 않습니다.");
            return titles;
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".nov"));
        if (files == null || files.length == 0)
        {
            System.out.println("파일이 없습니다.");
            return titles;
        }

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file)))
            {
                String title = reader.readLine();
                if (title != null && !title.trim().isEmpty())
                {
                    titles.add(title.trim());
                }
                
                else
                {
                    titles.add("(제목 없음)");
                }
            }
            
            catch (IOException e)
            {
                System.out.println("파일 읽기 오류: " + file.getName());
                e.printStackTrace();
            }
        }

        return titles;
    }

    //테스트
    public static void main(String[] args)
    {
        ManageBook manager = new ManageBook();
        List<String> titles = manager.getBookTitles("Books");
        
        System.out.println("책 제목 목록:");
        for (String title : titles)
        {
            System.out.println("- " + title);
        }
    }
}
