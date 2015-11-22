package armychess;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;


public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private JPanel contentPane;
	String GameMode;

	/**
	 * Launch the application.
	 */
	/*
	 public static void main (String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("E:\\ProgramProject\\eclipse\\MilitaryChess\\img\\background.jpg"));
		setTitle("\u56DB\u56FD\u519B\u68CB");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 810, 560);
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		setJMenuBar(menuBar);
		
		JMenu GameMenu = new JMenu("\u6E38\u620F");
		menuBar.add(GameMenu);
		
		JMenuItem CreateGameMenuItem = new JMenuItem("\u521B\u5EFA\u6E38\u620F");
		CreateGameMenuItem.addActionListener(new CreateGame());
	
		GameMenu.add(CreateGameMenuItem);
		
		JMenuItem JoinGameMenuItem = new JMenuItem("\u52A0\u5165\u6E38\u620F");
		GameMenu.add(JoinGameMenuItem);
		
		JMenuItem GameReplayMenuItem = new JMenuItem("\u6E38\u620F\u56DE\u653E");
		GameMenu.add(GameReplayMenuItem);
		
		JMenuItem QuitGameMenuItem = new JMenuItem("\u9000\u51FA\u6E38\u620F");
		QuitGameMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		GameMenu.add(QuitGameMenuItem);
		
		JMenu ControlMenu = new JMenu("\u8BBE\u7F6E");
		menuBar.add(ControlMenu);
		
		JMenuItem SoundControlMenuItem = new JMenuItem("\u58F0\u97F3\u63A7\u5236");
		ControlMenu.add(SoundControlMenuItem);
		
		JMenuItem CountMenuItem = new JMenuItem("\u6E38\u620F\u7EDF\u8BA1");
		ControlMenu.add(CountMenuItem);
		
		JMenu HelpMenu = new JMenu("\u5E2E\u52A9");
		menuBar.add(HelpMenu);
		
		JMenuItem HelpMenuItem = new JMenuItem("\u6E38\u620F\u5E2E\u52A9");
		HelpMenu.add(HelpMenuItem);
		
		JMenuItem VersionMenuItem = new JMenuItem("\u7248\u672C\u4FE1\u606F");
		HelpMenu.add(VersionMenuItem);
		
		
		contentPane =new BgPanel("img/background.jpg");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setLocationRelativeTo(null); 
	}
	


class CreateGame implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		GameMode = (String) JOptionPane.showInputDialog(MainFrame.this, "选择游戏模式", "创建游戏", JOptionPane.OK_CANCEL_OPTION,
				null,	new String[] {"双人", "四人——四暗","四人——双明","四人——四明"}, 
				"四人——四暗");
		 System.out.println(GameMode);
	}
}

}
