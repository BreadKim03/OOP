package AdminMode.UI;

import AdminMode.EbookManagement.ManageBook;
import AdminMode.EbookManagement.RequestManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.List;

import AdminMode.UserManagement.AdminSuggestview;
import AdminMode.UserManagement.AdminUserManage;
import UserMode.User;
import UserMode.PrivateInfo.SignIn;

public class AdminMenu extends JFrame
{
    private JPanel mainPanel;

    public AdminMenu()
    {
        setTitle("E-Book 관리자 시스템");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        mainPanel = new JPanel(new CardLayout());

        //MyPage 메뉴
        JMenu myPageMenu = new JMenu("MyPage");
        JMenuItem userManageItem = new JMenuItem("유저 관리");
        JMenuItem requestItem = new JMenuItem("유저 요청");
        requestItem.addActionListener(e -> new AdminSuggestview());
        JMenuItem logoutItem = new JMenuItem("로그아웃");
        logoutItem.addActionListener(e ->
        {
            int choice = JOptionPane.showConfirmDialog(this, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION)
            {
                dispose();
                SwingUtilities.invokeLater(() -> new SignIn());
            }
        });

        userManageItem.addActionListener(e -> {
            new AdminUserManage();
        });

        myPageMenu.add(userManageItem);
        myPageMenu.add(requestItem);
        myPageMenu.addSeparator();
        myPageMenu.add(logoutItem);

        //EBook 메뉴
        JMenu bookMenu = new JMenu("E-Book");
        JMenuItem allBooksItem = new JMenuItem("전체 도서 목록");

        allBooksItem.addActionListener((ActionEvent e) ->
        {
            showBookListPanel();
        });

        JMenuItem manageBookItem = new JMenuItem("도서 관리");
        manageBookItem.addActionListener(e ->
        {
            showBookManagePanel();
        });

        bookMenu.add(manageBookItem);
        bookMenu.add(allBooksItem);
        bookMenu.add(manageBookItem);

        JMenu loanMenu = new JMenu("대출관리");

        JMenuItem request = new JMenuItem("신청확인");
        request.addActionListener(e ->
        {
            new RequestManager().setVisible(true);
        });
        loanMenu.add(request);

        JMenuItem requestResult = new JMenuItem("대출 내역 조회");
        loanMenu.add(requestResult);
        requestResult.addActionListener(e -> showMyRequestedBooks());

        JMenu exitMenu = new JMenu("종료");
        JMenuItem exitItem = new JMenuItem("프로그램 종료");
        exitItem.addActionListener(e -> System.exit(0));
        exitMenu.add(exitItem);

        menuBar.add(myPageMenu);
        menuBar.add(bookMenu);
        menuBar.add(loanMenu);
        menuBar.add(exitMenu);

        setJMenuBar(menuBar);
        add(mainPanel);
        showWelcomePanel();
        setVisible(true);
    }

    private void showWelcomePanel()
    {
        mainPanel.removeAll();

        JPanel welcomePanel = new JPanel(new BorderLayout(10, 10));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("관리자 모드로 로그인되었습니다.");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("함초롬바탕", Font.BOLD, 18));
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton allBooksButton = new JButton("전체 도서 확인하기");
        JButton searchBooksButton = new JButton("도서 관리하기");
        JButton myRequestsButton = new JButton("대출 신청 확인하기");

        buttonPanel.add(allBooksButton);
        buttonPanel.add(searchBooksButton);
        buttonPanel.add(myRequestsButton);
        welcomePanel.add(buttonPanel, BorderLayout.CENTER);

        allBooksButton.addActionListener(e -> showBookListPanel());
        searchBooksButton.addActionListener(e -> showBookManagePanel());
        myRequestsButton.addActionListener(e -> new RequestManager());

        mainPanel.add(welcomePanel);
        mainPanel.revalidate();
        mainPanel.repaint();
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
            bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane listScrollPane = new JScrollPane(bookList);
            container.add(listScrollPane, BorderLayout.WEST);

            JTextArea contentArea = new JTextArea();
            contentArea.setEditable(false);
            JScrollPane contentScrollPane = new JScrollPane(contentArea);
            container.add(contentScrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton deleteButton = new JButton("도서 삭제");
            JButton editButton = new JButton("정보 수정");
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);
            container.add(buttonPanel, BorderLayout.NORTH);

            //책 선택 내용 표시
            bookList.addListSelectionListener(e ->
            {
                if (!e.getValueIsAdjusting())
                {
                    String selected = bookList.getSelectedValue();
                    StringBuilder content = new StringBuilder();

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

                                if (selected.equals(title))
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
                    }
                    contentArea.setText(content.toString());
                }
            });

            //정보 수정 버튼 클릭
            editButton.addActionListener(e ->
            {
                String selected = bookList.getSelectedValue();
                if (selected == null)
                {
                    JOptionPane.showMessageDialog(this, "수정할 도서를 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                File dir = new File("Books");
                File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".nov"));
                if (files != null) {
                    for (File file : files) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
                        {
                            String title = reader.readLine();
                            String writer = reader.readLine();
                            String genre = reader.readLine();
                            String price = reader.readLine()
                                    ;
                            if (title != null && title.equals(selected))
                            {
                                new BookEditor(this, file, title, writer, price, genre, () ->
                                {
                                    showBookListPanel();
                                }).setVisible(true);
                                break;
                            }
                        }

                        catch (IOException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            });

            //도서 삭제
            deleteButton.addActionListener(e ->
            {
                String selected = bookList.getSelectedValue();
                if (selected == null) return;

                int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "도서 삭제", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION)
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
                                    reader.close();
                                    boolean deleted = file.delete();

                                    if (deleted)
                                    {
                                        JOptionPane.showMessageDialog(this, "도서가 삭제되었습니다.");
                                        showBookManagePanel();
                                    }
                                    else
                                    {
                                        JOptionPane.showMessageDialog(this, "삭제 실패: 파일을 삭제할 수 없습니다.\n경로: " + file.getAbsolutePath());
                                    }

                                    break;
                                }
                            }

                            catch (IOException ex)
                            {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(this, "오류 발생: " + ex.getMessage());
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

    //도서 관리 탭
    private void showBookManagePanel()
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

            //상단 패널
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton editButton = new JButton("정보 수정");
            JButton deleteButton = new JButton("도서 삭제");
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);
            container.add(buttonPanel, BorderLayout.NORTH);

            //도서 선택 시 기본 정보 출력
            bookList.addListSelectionListener(e ->
            {
                if (!e.getValueIsAdjusting()) {
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

                                if (title != null && title.equals(selected)) {
                                    infoArea.setText("제목: " + title + "\n"
                                            + "작가: " + writer + "\n"
                                            + "장르: " + genre);
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

            //정보 수정
            editButton.addActionListener(e ->
            {
                String selected = bookList.getSelectedValue();
                if (selected == null)
                {
                    JOptionPane.showMessageDialog(this, "수정할 도서를 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
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
                            String writer = reader.readLine();
                            String genre = reader.readLine();
                            String price = reader.readLine();

                            if (title != null && title.equals(selected))
                            {
                                new BookEditor(this, file, title, writer, genre, price, this::showBookManagePanel).setVisible(true);
                                break;
                            }
                        }

                        catch (IOException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            });

            //도서 삭제
            deleteButton.addActionListener(e ->
            {
                String selected = bookList.getSelectedValue();
                if (selected == null) return;

                int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "도서 삭제", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION)
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
                                    reader.close();
                                    boolean deleted = file.delete();

                                    if (deleted)
                                    {
                                        JOptionPane.showMessageDialog(this, "도서가 삭제되었습니다.");
                                        showBookManagePanel();
                                    }
                                    else
                                    {
                                        JOptionPane.showMessageDialog(this, "삭제 실패: 파일을 삭제할 수 없습니다.\n경로: " + file.getAbsolutePath());
                                    }

                                    break;
                                }
                            }

                            catch (IOException ex)
                            {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(this, "오류 발생: " + ex.getMessage());
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

    private void showMyRequestedBooks()
    {
        mainPanel.removeAll();

        JPanel requestPanel = new JPanel(new BorderLayout(10, 10));
        JLabel titleLabel = new JLabel("신청된 도서 목록");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        requestPanel.add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        File dir = new File("Permissions");

        if (dir.exists() && dir.isDirectory())
        {
            File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".per"));
            if (files != null)
            {
                for (File file : files)
                {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file)))
                    {
                        String line = reader.readLine();
                        if (line != null)
                        {
                            String[] parts = line.split(",");
                            String entry = "제목: " + parts[2] + " | 신청일: " + parts[3] + " | 상태: " + parts[4];
                            listModel.addElement(entry);
                        }
                    }

                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

        JList<String> requestList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(requestList);
        requestPanel.add(scrollPane, BorderLayout.CENTER);

        if (listModel.isEmpty())
        {
            listModel.addElement("대출 신청된 도서가 없습니다.");
        }

        mainPanel.add(requestPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}