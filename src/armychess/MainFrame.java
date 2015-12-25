/*
 * 刚进入游戏的主界面，可通过菜单开始游戏、设置、查看游戏、帮助等信息。
 * 
 */
package armychess;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentpane; //内容
	private GsFrame gameserver;
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		//标题和大小设置
		setTitle("军棋达人");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 810, 560);
		
		//菜单栏设置
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		setJMenuBar(menuBar);
		
		JMenu GameMenu = new JMenu("\u6E38\u620F");
		menuBar.add(GameMenu);
		//创建游戏菜单
		JMenuItem CreateGameMenuItem = new JMenuItem("\u521B\u5EFA\u6E38\u620F");
		CreateGameMenuItem.addActionListener(new CreateGame());
		GameMenu.add(CreateGameMenuItem);
		//加入游戏菜单
		JMenuItem JoinGameMenuItem = new JMenuItem("\u52A0\u5165\u6E38\u620F");
		JoinGameMenuItem.addActionListener(new JoinGame());
		GameMenu.add(JoinGameMenuItem);
		//单人游戏菜单
		JMenuItem SoloGameMenuItem = new JMenuItem("单人游戏");
		SoloGameMenuItem.addActionListener(new SoloGame());
		GameMenu.add(SoloGameMenuItem);
		//游戏回放菜单
		JMenuItem GameReplayMenuItem = new JMenuItem("\u6E38\u620F\u56DE\u653E");
		GameReplayMenuItem.addActionListener(new ReplayGame());
		GameMenu.add(GameReplayMenuItem);
		//退出游戏菜单
		JMenuItem QuitGameMenuItem = new JMenuItem("\u9000\u51FA\u6E38\u620F");
		QuitGameMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		GameMenu.add(QuitGameMenuItem);
		//控制菜单
		JMenu ControlMenu = new JMenu("\u8BBE\u7F6E");
		menuBar.add(ControlMenu);
		//游戏统计
		JMenuItem CountMenuItem = new JMenuItem("\u6E38\u620F\u7EDF\u8BA1");
		CountMenuItem.addActionListener(new GameCount());
		ControlMenu.add(CountMenuItem);
		
		JMenu HelpMenu = new JMenu("\u5E2E\u52A9");
		menuBar.add(HelpMenu);
		//帮助菜单
		JMenuItem HelpMenuItem = new JMenuItem("\u6E38\u620F\u5E2E\u52A9");
		HelpMenuItem.addActionListener(new GameHelp());
		HelpMenu.add(HelpMenuItem);
		//版本控制菜单
		JMenuItem VersionMenuItem = new JMenuItem("\u7248\u672C\u4FE1\u606F");
		VersionMenuItem.addActionListener(new VersionInfo());
		HelpMenu.add(VersionMenuItem);
		
		
		contentpane =new BgPanel(".\\res\\img\\background.jpg");
		contentpane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentpane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentpane);
		setLocationRelativeTo(null); 
	}
	

//创建游戏菜单响应
class CreateGame implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// 选择游戏模式
		String gamemode = (String) JOptionPane.showInputDialog(MainFrame.this, "选择游戏模式", "创建游戏", JOptionPane.OK_CANCEL_OPTION,
				null,new String[] {"四人——四暗","四人——双明","四人——全明"}, 
				"四人——四暗");
		if(gamemode!=null){
		  gameserver = new GsFrame(gamemode);  //创建游戏服务器
		  try {
			new GcFrame("");     //创建本地游戏客户端
		  } catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		  }
		}
	}
}

//加入游戏菜单响应
class JoinGame implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// 输入服务端IP地址
		String ServerIP = (String) JOptionPane.showInputDialog(MainFrame.this, "输入服务端地址", "加入游戏", JOptionPane.OK_CANCEL_OPTION);
		if(ServerIP!=null){
		try {
			new GcFrame(ServerIP);
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		}
	}
}
//游戏回放菜单响应
class ReplayGame implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		ExtensionFileFilter filter = new ExtensionFileFilter();  //创建文件过滤器
		filter.addExtension("ags");                              //过滤出后缀为".ags"的游戏保存文件
		filter.setDescription("army chess game save文件(*.ags)");
		JFileChooser chooser = new JFileChooser("./save/gamesave/");
		chooser.setAcceptAllFileFilterUsed(false); 
		chooser.addChoosableFileFilter(filter);
		int result = chooser.showDialog(MainFrame.this , "打开游戏文件");  
		if(result == JFileChooser.APPROVE_OPTION)
		{
			String gsname = chooser.getSelectedFile().getPath();   //获取选择的文件名
		    try {
				new GrFrame(gsname);    //创建游戏回放
			} catch (IOException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		
	}
}

//游戏统计菜单响应
class GameCount implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		File cfile=new File(".\\save\\count.ini"); //打开游戏统计文件
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
			 String CountMsg = "您一共完成"+cfstrs[0]+"场游戏， 其中胜利"+cfstrs[1]+"场， 胜率为"+cfstrs[2]+"%。";
		     ret = JOptionPane.showOptionDialog(MainFrame.this, CountMsg, "游戏统计", JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new String[]{"确认","清空"},"确认");
		     }
		   else
			 JOptionPane.showMessageDialog(MainFrame.this,"统计信息不完整，请完成一局游戏后再查看信息!","游戏统计",JOptionPane.ERROR_MESSAGE);
			cfread.close();
		   if(ret==1){
			FileOutputStream  clearout = new FileOutputStream(cfile); 
			clearout.write("0 0 0".getBytes());
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

//游戏统计菜单响应
class GameHelp implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		HelpDialog jd1 = new HelpDialog(MainFrame.this,"游戏帮助",false);
		jd1.setLocationRelativeTo(null); 
		jd1.setVisible(true);
	}
}
//版本信息菜单响应
class VersionInfo implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 自动生成的方法存根
		VersionDialog jd1 = new VersionDialog(MainFrame.this,"版本信息",false);
		jd1.setLocationRelativeTo(null); 
		jd1.setVisible(true);
	}
}
//单人游戏菜单响应
class SoloGame implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			new GlFrame();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
}
 
