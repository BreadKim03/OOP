package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookMenu extends JFrame{

        public BookMenu() {
            JFrame frame = new JFrame("E-Book 시스템");
            JMenuBar menuBar = new JMenuBar();
            JPanel mainPanel = new JPanel(new CardLayout());

            setTitle("E-Book 대출 시스템");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            // 1. 회원관리 메뉴
            JMenu memberMenu = new JMenu("회원관리");
            JMenuItem editItem = new JMenuItem("회원 정보 수정");
            JMenuItem deleteItem = new JMenuItem("회원 탈퇴");
            JMenuItem logoutItem = new JMenuItem("로그아웃");

            memberMenu.add(editItem);
            memberMenu.add(deleteItem);
            memberMenu.addSeparator();
            memberMenu.add(logoutItem);

            // 2. E-Book 메뉴
            JMenu bookMenu = new JMenu("E-Book");
            JMenuItem allBooksItem = new JMenuItem("전체 도서 목록");
            JMenu genreSubMenu = new JMenu("장르별 보기");
            genreSubMenu.add(new JMenuItem("소설"));
            genreSubMenu.add(new JMenuItem("비문학"));
            genreSubMenu.add(new JMenuItem("전공서적"));
            JMenuItem searchItem = new JMenuItem("도서 검색");

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
            exitMenu.add(exitItem);

            // 메뉴바에 메뉴들 추가
            menuBar.add(memberMenu);
            menuBar.add(bookMenu);
            menuBar.add(loanMenu);
            menuBar.add(infoMenu);
            menuBar.add(exitMenu);

            setJMenuBar(menuBar);

            // 이벤트 예시
            searchItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "도서 검색 창 열기"));
            exitItem.addActionListener(e -> System.exit(0));

            setVisible(true);
        }

    public static void main(String[] args) {
        new BookMenu();
    }
}

