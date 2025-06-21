package UserMode.UI;

import javax.swing.*;

import AdminMode.EbookManagement.ManageBook;
import AdminMode.UI.BookEditor;
import UserMode.PrivateInfo.SignIn;
import UserMode.User;
import UserMode.PrivateInfo.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class BookMenu extends JFrame
{
		
	private User loginUser; // 로그인한 유저 정보, 나중에 지워도 될거같은데 일단 남기고 이쪽이 편하면 이렇게 사용할 예정
	JPanel mainPanel;
	
        public BookMenu(User loginUser) {
        	this.loginUser = loginUser;
			mainPanel = new JPanel(new CardLayout());
            JFrame frame = new JFrame("E-Book 시스템");
            JMenuBar menuBar = new JMenuBar();
            add(mainPanel);

            setTitle("E-Book 대출 시스템");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            // 1. 회원관리 메뉴
            JMenu memberMenu = new JMenu("MyPage");
            JMenuItem editItem = new JMenuItem("회원 정보 수정");
            JMenuItem deleteItem = new JMenuItem("회원 탈퇴");
            JMenuItem logoutItem = new JMenuItem("로그아웃");
            logoutItem.addActionListener(e -> 
            {
            	int choice = JOptionPane.showConfirmDialog(this, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
            	if(choice == JOptionPane.YES_NO_OPTION)
            	{
            		dispose();
            		SwingUtilities.invokeLater(() -> new SignIn());
            	}
            });
            deleteItem.addActionListener(e ->
            {
                WithdrawWindow withdrawwindow = new WithdrawWindow(loginUser,this);
                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();
            });
            editItem.addActionListener(e ->
            {
                EditInfo EditInfoWindow = new EditInfo(loginUser);
                mainPanel.removeAll();
                mainPanel.add(EditInfoWindow);
                add(mainPanel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            });
            
            memberMenu.add(editItem);
            memberMenu.add(deleteItem);
            memberMenu.addSeparator();
            memberMenu.add(logoutItem);

            // 2. E-Book 메뉴
            JMenu bookMenu = new JMenu("E-Book");
            JMenuItem allBooksItem = new JMenuItem("전체 도서 목록");
            allBooksItem.addActionListener(e ->
            {
            	showBookListPanel();
            });
            //장르별 보기
            JMenu genreSubMenu = new JMenu("장르별 보기");
            JMenuItem novelItem = new JMenuItem("소설");
            JMenuItem nonfictionItem = new JMenuItem("비문학");
            JMenuItem majorItem = new JMenuItem("전공서적");

            novelItem.addActionListener(e -> showGenreBookPanel("소설"));
            nonfictionItem.addActionListener(e -> showGenreBookPanel("비문학"));
            majorItem.addActionListener(e -> showGenreBookPanel("전공서적"));

            genreSubMenu.add(novelItem);
            genreSubMenu.add(nonfictionItem);
            genreSubMenu.add(majorItem);
            
            //도서 검색
            JMenuItem searchItem = new JMenuItem("도서 검색");
            searchItem.addActionListener(e -> showSearchResultPanel());

            bookMenu.add(allBooksItem);
            bookMenu.add(genreSubMenu);
            bookMenu.add(searchItem);

            // 3. 대출관리 메뉴
            JMenu loanMenu = new JMenu("대출관리");
            loanMenu.add(new JMenuItem("대출 신청"));
            loanMenu.add(new JMenuItem("반납 처리"));
            loanMenu.add(new JMenuItem("대출 내역 조회"));
            loanMenu.add(new JMenuItem("연체 도서 확인"));

            // 4. 정보
            JMenu infoMenu = new JMenu("정보");
            infoMenu.add(new JMenuItem("앱 사용법"));
            infoMenu.add(new JMenuItem("개발자 정보"));
            infoMenu.add(new JMenuItem("버전 정보"));

            // 5. 종료
            JMenu exitMenu = new JMenu("종료");
            JMenuItem exitItem = new JMenuItem("프로그램 종료");
            exitItem.addActionListener(e -> System.exit(0));
            exitMenu.add(exitItem);

            // 메뉴바에 메뉴들 추가
            menuBar.add(memberMenu);
            menuBar.add(bookMenu);
            menuBar.add(loanMenu);
            menuBar.add(infoMenu);
            menuBar.add(exitMenu);

            setJMenuBar(menuBar);

            setVisible(true);
        }
        
        private void showBookListPanel()
        {
            mainPanel.removeAll();

            ManageBook manager = new ManageBook();
            List<String> titles = manager.getBookTitles("Books");

            if (titles.isEmpty())
            {
                JLabel noBooksLabel = new JLabel("도서가 없습니다.");
                noBooksLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(noBooksLabel);
            }
            
            else
            {
                JPanel container = new JPanel(new BorderLayout());

                JList<String> bookList = new JList<>(titles.toArray(new String[0]));
                JScrollPane listScrollPane = new JScrollPane(bookList);
                container.add(listScrollPane, BorderLayout.WEST);

                JTextArea contentArea = new JTextArea();
                contentArea.setEditable(false);
                JScrollPane contentScrollPane = new JScrollPane(contentArea);
                container.add(contentScrollPane, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton deleteButton = new JButton("상세정보");
                deleteButton.addActionListener(e -> showBookInfoPanel());
                JButton editButton = new JButton("대출 신청");
                buttonPanel.add(editButton);
                buttonPanel.add(deleteButton);
                container.add(buttonPanel, BorderLayout.NORTH);

                bookList.addListSelectionListener(e ->
                {
                    if (!e.getValueIsAdjusting())
                    {
                        String selected = bookList.getSelectedValue();
                        StringBuilder content = new StringBuilder();

                        File dir = new File("Books");
                        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".nov"));
                        if (files != null) {
                            for (File file : files)
                            {
                                try (BufferedReader reader = new BufferedReader(new FileReader(file)))
                                {
                                    String title = reader.readLine();
                                    reader.readLine(); // writer
                                    reader.readLine(); // genre
                                    reader.readLine(); // price

                                    if (selected.equals(title)) {
                                        String line;
                                        
                                        while ((line = reader.readLine()) != null)
                                        {
                                            content.append(line).append("\n");
                                        }
                                        break;
                                    }
                                }
                                
                                catch (IOException ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        contentArea.setText(content.toString());
                    }
                });

                mainPanel.add(container);
            }

            mainPanel.revalidate();
            mainPanel.repaint();
        }
        
        private void showBookInfoPanel()
        {
            mainPanel.removeAll();

            ManageBook manager = new ManageBook();
            List<String> titles = manager.getBookTitles("Books");

            if (titles.isEmpty())
            {
                JLabel noBooksLabel = new JLabel("도서가 없습니다.");
                noBooksLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(noBooksLabel);
            }
            
            else
            {
                JPanel container = new JPanel(new BorderLayout());

                JList<String> bookList = new JList<>(titles.toArray(new String[0]));
                bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                JScrollPane listScrollPane = new JScrollPane(bookList);
                container.add(listScrollPane, BorderLayout.WEST);

                JTextArea infoArea = new JTextArea();
                infoArea.setEditable(false);
                infoArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
                JScrollPane infoScrollPane = new JScrollPane(infoArea);
                container.add(infoScrollPane, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton backButton = new JButton("목록으로 돌아가기");
                buttonPanel.add(backButton);
                container.add(buttonPanel, BorderLayout.NORTH);

                //돌아가기
                backButton.addActionListener(e -> showBookListPanel());

                bookList.addListSelectionListener(e ->
                {
                    if (!e.getValueIsAdjusting())
                    {
                        String selected = bookList.getSelectedValue();
                        if (selected == null) return;

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
                                        infoArea.setText("제목 : " + title + "\n" + "작가 : " + writer + "\n" + "장르 : " + genre + "\n" + "가격 : " + price);
                                        break;
                                    }
                                }
                                catch (IOException ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                });

                mainPanel.add(container);
            }

            mainPanel.revalidate();
            mainPanel.repaint();
        }
        
        
        //장르별 출력
        private void showGenreBookPanel(String genreType)
        {
            mainPanel.removeAll();

            File dir = new File("Books");
            File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".nov"));

            DefaultListModel<String> filteredTitles = new DefaultListModel<>();

            if (files != null)
            {
                for (File file : files)
                {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file)))
                    {
                        String title = reader.readLine();
                        String writer = reader.readLine();
                        String genre = reader.readLine(); // 장르
                        reader.readLine(); // price

                        if (genre != null && genre.equalsIgnoreCase(genreType))
                        {
                            filteredTitles.addElement(title);
                        }
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }

            if (filteredTitles.isEmpty())
            {
                JLabel noBooksLabel = new JLabel("해당 장르의 도서가 없습니다.");
                noBooksLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(noBooksLabel);
            }
            else
            {
                JPanel container = new JPanel(new BorderLayout());

                JList<String> bookList = new JList<>(filteredTitles);
                JScrollPane listScrollPane = new JScrollPane(bookList);
                container.add(listScrollPane, BorderLayout.WEST);

                JTextArea contentArea = new JTextArea();
                contentArea.setEditable(false);
                JScrollPane contentScrollPane = new JScrollPane(contentArea);
                container.add(contentScrollPane, BorderLayout.CENTER);

                JButton backButton = new JButton("목록으로 돌아가기");
                backButton.addActionListener(e -> showBookListPanel());

                JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                topPanel.add(backButton);
                container.add(topPanel, BorderLayout.NORTH);

                bookList.addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting())
                    {
                        String selected = bookList.getSelectedValue();
                        if (selected == null) return;

                        StringBuilder content = new StringBuilder();

                        for (File file : files)
                        {
                            try (BufferedReader reader = new BufferedReader(new FileReader(file)))
                            {
                                String title = reader.readLine();
                                reader.readLine(); // writer
                                String genre = reader.readLine();
                                reader.readLine(); // price

                                if (title != null && genre != null &&
                                    title.equals(selected) && genre.equalsIgnoreCase(genreType))
                                {
                                    String line;
                                    while ((line = reader.readLine()) != null)
                                    {
                                        content.append(line).append("\n");
                                    }
                                    break;
                                }
                            }
                            catch (IOException ex)
                            {
                                ex.printStackTrace();
                            }
                        }

                        contentArea.setText(content.toString());
                    }
                });

                mainPanel.add(container);
            }

            mainPanel.revalidate();
            mainPanel.repaint();
        }
        
        private void showSearchResultPanel()
        {
            mainPanel.removeAll();

            JPanel searchPanel = new JPanel(new BorderLayout());

            JLabel label = new JLabel("검색할 도서의 제목 또는 내용을 입력하세요.");
            JTextField searchField = new JTextField();
            JButton searchButton = new JButton("검색");

            JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
            inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            inputPanel.add(label, BorderLayout.NORTH);
            inputPanel.add(searchField, BorderLayout.CENTER);
            inputPanel.add(searchButton, BorderLayout.EAST);

            DefaultListModel<String> resultModel = new DefaultListModel<>();
            JList<String> resultList = new JList<>(resultModel);
            resultList.addListSelectionListener(e -> 
            {
            	if (!e.getValueIsAdjusting())
            	{
                    String selected = resultList.getSelectedValue();
                    if (selected != null && !selected.equals("검색 결과가 없습니다."))
                    {
                        showBookSearchResult(selected);
                    }
            	}
            }
            );
            JScrollPane scrollPane = new JScrollPane(resultList);

            searchPanel.add(inputPanel, BorderLayout.NORTH);
            searchPanel.add(scrollPane, BorderLayout.CENTER);

            searchButton.addActionListener(e ->
            {
                resultModel.clear();
                String keyword = searchField.getText().trim().toLowerCase();
                
                if (keyword.isEmpty())
                {
                    JOptionPane.showMessageDialog(this, "검색어를 입력하세요.");
                    return;
                }

                File dir = new File("Books");
                File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".nov"));

                if (files != null)
                {
                    for (File file : files)
                    {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
                        {
                            String title = reader.readLine();
                            reader.readLine(); // writer
                            reader.readLine(); // genre
                            reader.readLine(); // price

                            StringBuilder content = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null)
                            {
                                content.append(line).append("\n");
                            }

                            if (title != null &&
                                (title.toLowerCase().contains(keyword) || content.toString().toLowerCase().contains(keyword)))
                            {
                                resultModel.addElement(title);
                            }
                        }
                        
                        catch (IOException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }

                if (resultModel.isEmpty())
                {
                    resultModel.addElement("검색 결과가 없습니다.");
                }
            });

            mainPanel.add(searchPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
        
        private void showBookSearchResult(String selectedTitle)
        {
            mainPanel.removeAll();

            JPanel container = new JPanel(new BorderLayout());

            JTextField titleField = new JTextField(selectedTitle);
            titleField.setEditable(false);
            container.add(titleField, BorderLayout.WEST);

            JTextArea previewArea = new JTextArea();
            previewArea.setEditable(false);
            JScrollPane centerScroll = new JScrollPane(previewArea);
            container.add(centerScroll, BorderLayout.CENTER);

            JPanel rightPanel = new JPanel(new BorderLayout());
            JTextArea infoArea = new JTextArea();
            infoArea.setEditable(false);
            JScrollPane infoScroll = new JScrollPane(infoArea);

            JButton backButton = new JButton("목록으로 돌아가기");
            backButton.addActionListener(e -> showBookListPanel());

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            topPanel.add(backButton);

            rightPanel.add(topPanel, BorderLayout.NORTH);
            rightPanel.add(infoScroll, BorderLayout.CENTER);

            container.add(rightPanel, BorderLayout.EAST);

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

                        if (title != null && title.equals(selectedTitle))
                        {
                            // 내용 일부 읽기
                            StringBuilder preview = new StringBuilder();
                            String line;
                            int lineCount = 0;
                            while ((line = reader.readLine()) != null && lineCount < 7)
                            {
                                preview.append(line).append("\n");
                                lineCount++;
                            }
                            
                            if (reader.readLine() != null)
                            {
                                preview.append("\n...");
                            }
                            previewArea.setText(preview.toString());

                            // 상세 정보 표시
                            infoArea.setText("제목 : " + title + "\n"
                                    + "작가 : " + writer + "\n"
                                    + "장르 : " + genre + "\n"
                                    + "가격 : " + price);
                            break;
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            mainPanel.add(container);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
        
}


