package AdminMode.UI;

import AdminMode.EbookManagement.ManageBook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.List;

public class AdminMenu extends JFrame
{

    private JPanel mainPanel;

    public AdminMenu() {
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
        JMenuItem logoutItem = new JMenuItem("로그아웃");
        logoutItem.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                JOptionPane.showMessageDialog(null, "로그아웃되었습니다.");
            }
        });
        myPageMenu.add(userManageItem);
        myPageMenu.add(requestItem);
        myPageMenu.addSeparator();
        myPageMenu.add(logoutItem);

        //EBook 메뉴
        JMenu bookMenu = new JMenu("E-Book");
        JMenuItem allBooksItem = new JMenuItem("전체 도서 목록");
        JMenuItem manageBookItem = new JMenuItem("도서 관리");
        bookMenu.add(allBooksItem);
        bookMenu.add(manageBookItem);

        allBooksItem.addActionListener((ActionEvent e) -> {
            showBookListPanel();
        });

        JMenu loanMenu = new JMenu("대출관리");
        loanMenu.add(new JMenuItem("신청 확인"));
        loanMenu.add(new JMenuItem("반납 확인"));
        loanMenu.add(new JMenuItem("대출 내역 조회"));
        loanMenu.add(new JMenuItem("연체자 및 도서 확인"));

        JMenu infoMenu = new JMenu("정보");
        infoMenu.add(new JMenuItem("앱 사용법"));
        infoMenu.add(new JMenuItem("개발자 정보"));
        infoMenu.add(new JMenuItem("버전 정보"));

        JMenu exitMenu = new JMenu("종료");
        JMenuItem exitItem = new JMenuItem("프로그램 종료");
        exitItem.addActionListener(e -> System.exit(0));
        exitMenu.add(exitItem);

        menuBar.add(myPageMenu);
        menuBar.add(bookMenu);
        menuBar.add(loanMenu);
        menuBar.add(infoMenu);
        menuBar.add(exitMenu);

        setJMenuBar(menuBar);
        add(mainPanel);
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

            deleteButton.addActionListener(e ->
            {
                String selected = bookList.getSelectedValue();
                if (selected == null) return;

                int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "도서 삭제", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION)
                {
                    File dir = new File("Books");
                    File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".nov"));
                    if (files != null) {
                        for (File file : files)
                        {
                            try (BufferedReader reader = new BufferedReader(new FileReader(file)))
                            {
                                String title = reader.readLine();
                                if (selected.equals(title))
                                {
                                    if (file.delete())
                                    {
                                        JOptionPane.showMessageDialog(this, "도서가 삭제되었습니다.");
                                        showBookListPanel();
                                    }
                                    
                                    else
                                    {
                                        JOptionPane.showMessageDialog(this, "삭제 실패");
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
                }
            });

            mainPanel.add(container);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new AdminMenu());
    }
}
