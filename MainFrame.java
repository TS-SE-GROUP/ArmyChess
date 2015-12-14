/*
 * 刚进入游戏的主界面，可通过菜单开始游戏、设置、查看游戏、帮助等信息。
 * 
 */
package armychess;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private JPanel contentPane;
    private GsFrame gameserver;
	
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
		JoinGameMenuItem.addActionListener(new JoinGame());
		GameMenu.add(JoinGameMenuItem);
		
		JMenuItem GameReplayMenuItem = new JMenuItem("\u6E38\u620F\u56DE\u653E");
		GameReplayMenuItem.addActionListener(new ReplayGame());
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
		SoundControlMenuItem.addActionListener(new SoundControl());
		ControlMenu.add(SoundControlMenuItem);
		
		JMenuItem CountMenuItem = new JMenuItem("\u6E38\u620F\u7EDF\u8BA1");
		CountMenuItem.addActionListener(new GameCount());
		ControlMenu.add(CountMenuItem);
		
		JMenu HelpMenu = new JMenu("\u5E2E\u52A9");
		menuBar.add(HelpMenu);
		
		JMenuItem HelpMenuItem = new JMenuItem("\u6E38\u620F\u5E2E\u52A9");
		HelpMenuItem.addActionListener(new GameHelp());
		HelpMenu.add(HelpMenuItem);
		
		JMenuItem VersionMenuItem = new JMenuItem("\u7248\u672C\u4FE1\u606F");
		VersionMenuItem.addActionListener(new VersionInfo());
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
		String GameMode = (String) JOptionPane.showInputDialog(MainFrame.this, "选择游戏模式", "创建游戏", JOptionPane.OK_CANCEL_OPTION,
				null,new String[] {"双人", "四人——四暗","四人——双明","四人——四明"}, 
				"四人——四暗");
		gameserver = new GsFrame(GameMode);
	}
}

class JoinGame implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		String ServerIP = (String) JOptionPane.showInputDialog(MainFrame.this, "输入服务端地址", "加入游戏", JOptionPane.OK_CANCEL_OPTION);
	}
}

class ReplayGame implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		ExtensionFileFilter filter = new ExtensionFileFilter();
		filter.addExtension("ags");
		filter.setDescription("army chess game save文件(*.ags)");
		JFileChooser chooser = new JFileChooser(".\\save");
		chooser.setAcceptAllFileFilterUsed(false); 
		chooser.addChoosableFileFilter(filter);
		int result = chooser.showDialog(MainFrame.this , "打开游戏文件");
		if(result == JFileChooser.APPROVE_OPTION)
		{
			String GSname = chooser.getSelectedFile().getPath();
			System.out.println(GSname);
		}
		
	}
}

class SoundControl implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		JDialog jd1 = new JDialog(MainFrame.this,"音效设置",false);
		JPanel jp1 = new JPanel();
		JRadioButton bmusic1 = new JRadioButton("背景音乐开",true);
		JRadioButton bmusic2 = new JRadioButton("背景音乐关",false);
		ButtonGroup bmusic = new ButtonGroup();
		bmusic.add(bmusic1);
		bmusic.add(bmusic2);
		JRadioButton effectsound1 = new JRadioButton("游戏音效开 ",true);
		JRadioButton effectsound2 = new JRadioButton("游戏音效关",false);
		ButtonGroup effectsound = new ButtonGroup();
		effectsound.add(effectsound1);
		effectsound.add(effectsound2);
		JButton confirmbu = new JButton("确认");
		confirmbu.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jd1.dispose();
			}
		});	
		JButton canclebu = new JButton("取消");
		canclebu.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jd1.dispose();
			}
		});
		jp1.add(bmusic1);
		jp1.add(bmusic2);
		jp1.add(effectsound1);
		jp1.add(effectsound2);
		jp1.add(confirmbu);
		jp1.add(canclebu);
		jd1.getContentPane().add(jp1);
		jd1.setBounds(0, 0, 250, 150);
		jd1.setLocationRelativeTo(null); 
		jd1.setVisible(true);
	}
}

class GameCount implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		File cfile=new File(".\\save\\count.inf");
		int ch,ret=0;
		String cfinfo = "";
		if(cfile.isFile()&&cfile.exists()){
		  try {
			InputStreamReader cfread = new InputStreamReader(new FileInputStream(cfile));
			while((ch = cfread.read())!=-1){    
			    cfinfo=cfinfo+(char)ch;
		  }
		   String[] cfstrs = cfinfo.split(" ");
		   //System.out.println(cfstrs.length);
		   if(cfstrs.length==3){
			 String CountMsg = "您一共完成"+cfstrs[0]+"场游戏， 其中胜利"+cfstrs[1]+"场， 胜率为"+cfstrs[2]+"。";
		     ret = JOptionPane.showOptionDialog(MainFrame.this, CountMsg, "游戏统计", JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new String[]{"确认","清空"},"确认");
		     }
		   else
			 JOptionPane.showMessageDialog(MainFrame.this,"统计信息不完整，请完成一局游戏后再查看信息!","游戏统计",JOptionPane.ERROR_MESSAGE);
			cfread.close();
		   if(ret==1){
			FileOutputStream  clearout = new FileOutputStream(cfile); 
			clearout.write("0 0 0%".getBytes());
			clearout.close();
		   }
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();}
		}
		else
		{
		   JOptionPane.showMessageDialog(MainFrame.this,"未找到统计信息,请完成一局游戏后再查看信息!","游戏统计",JOptionPane.ERROR_MESSAGE);
		}
		
		}
	}
class GameHelp implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		HelpDialog jd1 = new HelpDialog(MainFrame.this,"游戏帮助",false);
		jd1.setLocationRelativeTo(null); 
		jd1.setVisible(true);
	}
}
class VersionInfo implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		VersionDialog jd1 = new VersionDialog(MainFrame.this,"版本信息",false);
		jd1.setLocationRelativeTo(null); 
		jd1.setVisible(true);
	}
}
}
 
