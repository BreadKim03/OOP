package UserMode.OwnBooks;

import java.io.*;
import java.util.*;

public class ShowBooks
{
 private static final String BOOKS_FOLDER = "books";

 // 책 제목 목록
 public List<String> getAllBooks()
 {
     return getBooksByGenre(null);
 }

 //장르별 책 제목
 public List<String> getBooksByGenre(String genreFilter)
 {
     List<String> titles = new ArrayList<>();
     File folder = new File(BOOKS_FOLDER);
     File[] files = folder.listFiles((dir, name) -> name.endsWith(".nov"));

     if (files == null) return titles;

     for (File file : files)
     {
         try (BufferedReader reader = new BufferedReader(new FileReader(file)))
         {
             String title = reader.readLine();
             reader.readLine(); // 저자 skip
             String genre = reader.readLine();

             if (genreFilter == null || genre.equals(genreFilter))
             {
                 titles.add(title);
             }
         }
         
         catch (IOException e)
         {
             e.printStackTrace();
         }
     }

     return titles;
 }

 //책 반환
 public String getBookContentByTitle(String title)
 {
     File folder = new File(BOOKS_FOLDER);
     File[] files = folder.listFiles((dir, name) -> name.endsWith(".nov"));

     if (files == null) return "책을 찾을 수 없습니다.";

     for (File file : files)
     {
         try (BufferedReader reader = new BufferedReader(new FileReader(file)))
         {
             String fileTitle = reader.readLine();
             if (fileTitle != null && fileTitle.equals(title))
             {
                 StringBuilder content = new StringBuilder();
                 String line;
                 
                 while ((line = reader.readLine()) != null)
                 {
                     content.append(line).append("\n");
                 }
                 return content.toString();
             }
         }
         
         catch (IOException e)
         {
             e.printStackTrace();
         }
     }

     return "내용을 불러올 수 없습니다.";
 }
}