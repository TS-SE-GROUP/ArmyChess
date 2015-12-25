/*
 * �ս�����Ϸ�������棬��ͨ���˵���ʼ��Ϸ�����á��鿴��Ϸ����������Ϣ��
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

	private JPanel contentpane; //����
	private GsFrame gameserver;
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		//����ʹ�С����
		setTitle("�������");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 810, 560);
		
		//�˵�������
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		setJMenuBar(menuBar);
		
		JMenu GameMenu = new JMenu("\u6E38\u620F");
		menuBar.add(GameMenu);
		//������Ϸ�˵�
		JMenuItem CreateGameMenuItem = new JMenuItem("\u521B\u5EFA\u6E38\u620F");
		CreateGameMenuItem.addActionListener(new CreateGame());
		GameMenu.add(CreateGameMenuItem);
		//������Ϸ�˵�
		JMenuItem JoinGameMenuItem = new JMenuItem("\u52A0\u5165\u6E38\u620F");
		JoinGameMenuItem.addActionListener(new JoinGame());
		GameMenu.add(JoinGameMenuItem);
		//������Ϸ�˵�
		JMenuItem SoloGameMenuItem = new JMenuItem("������Ϸ");
		SoloGameMenuItem.addActionListener(new SoloGame());
		GameMenu.add(SoloGameMenuItem);
		//��Ϸ�طŲ˵�
		JMenuItem GameReplayMenuItem = new JMenuItem("\u6E38\u620F\u56DE\u653E");
		GameReplayMenuItem.addActionListener(new ReplayGame());
		GameMenu.add(GameReplayMenuItem);
		//�˳���Ϸ�˵�
		JMenuItem QuitGameMenuItem = new JMenuItem("\u9000\u51FA\u6E38\u620F");
		QuitGameMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		GameMenu.add(QuitGameMenuItem);
		//���Ʋ˵�
		JMenu ControlMenu = new JMenu("\u8BBE\u7F6E");
		menuBar.add(ControlMenu);
		//��Ϸͳ��
		JMenuItem CountMenuItem = new JMenuItem("\u6E38\u620F\u7EDF\u8BA1");
		CountMenuItem.addActionListener(new GameCount());
		ControlMenu.add(CountMenuItem);
		
		JMenu HelpMenu = new JMenu("\u5E2E\u52A9");
		menuBar.add(HelpMenu);
		//�����˵�
		JMenuItem HelpMenuItem = new JMenuItem("\u6E38\u620F\u5E2E\u52A9");
		HelpMenuItem.addActionListener(new GameHelp());
		HelpMenu.add(HelpMenuItem);
		//�汾���Ʋ˵�
		JMenuItem VersionMenuItem = new JMenuItem("\u7248\u672C\u4FE1\u606F");
		VersionMenuItem.addActionListener(new VersionInfo());
		HelpMenu.add(VersionMenuItem);
		
		
		contentpane =new BgPanel(".\\res\\img\\background.jpg");
		contentpane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentpane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentpane);
		setLocationRelativeTo(null); 
	}
	

//������Ϸ�˵���Ӧ
class CreateGame implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// ѡ����Ϸģʽ
		String gamemode = (String) JOptionPane.showInputDialog(MainFrame.this, "ѡ����Ϸģʽ", "������Ϸ", JOptionPane.OK_CANCEL_OPTION,
				null,new String[] {"���ˡ����İ�","���ˡ���˫��","���ˡ���ȫ��"}, 
				"���ˡ����İ�");
		if(gamemode!=null){
		  gameserver = new GsFrame(gamemode);  //������Ϸ������
		  try {
			new GcFrame("");     //����������Ϸ�ͻ���
		  } catch (IOException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		  }
		}
	}
}

//������Ϸ�˵���Ӧ
class JoinGame implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// ��������IP��ַ
		String ServerIP = (String) JOptionPane.showInputDialog(MainFrame.this, "�������˵�ַ", "������Ϸ", JOptionPane.OK_CANCEL_OPTION);
		if(ServerIP!=null){
		try {
			new GcFrame(ServerIP);
		} catch (IOException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		}
	}
}
//��Ϸ�طŲ˵���Ӧ
class ReplayGame implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �Զ����ɵķ������
		ExtensionFileFilter filter = new ExtensionFileFilter();  //�����ļ�������
		filter.addExtension("ags");                              //���˳���׺Ϊ".ags"����Ϸ�����ļ�
		filter.setDescription("army chess game save�ļ�(*.ags)");
		JFileChooser chooser = new JFileChooser("./save/gamesave/");
		chooser.setAcceptAllFileFilterUsed(false); 
		chooser.addChoosableFileFilter(filter);
		int result = chooser.showDialog(MainFrame.this , "����Ϸ�ļ�");  
		if(result == JFileChooser.APPROVE_OPTION)
		{
			String gsname = chooser.getSelectedFile().getPath();   //��ȡѡ����ļ���
		    try {
				new GrFrame(gsname);    //������Ϸ�ط�
			} catch (IOException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}
		}
		
	}
}

//��Ϸͳ�Ʋ˵���Ӧ
class GameCount implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �Զ����ɵķ������
		File cfile=new File(".\\save\\count.ini"); //����Ϸͳ���ļ�
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
			 String CountMsg = "��һ�����"+cfstrs[0]+"����Ϸ�� ����ʤ��"+cfstrs[1]+"���� ʤ��Ϊ"+cfstrs[2]+"%��";
		     ret = JOptionPane.showOptionDialog(MainFrame.this, CountMsg, "��Ϸͳ��", JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new String[]{"ȷ��","���"},"ȷ��");
		     }
		   else
			 JOptionPane.showMessageDialog(MainFrame.this,"ͳ����Ϣ�������������һ����Ϸ���ٲ鿴��Ϣ!","��Ϸͳ��",JOptionPane.ERROR_MESSAGE);
			cfread.close();
		   if(ret==1){
			FileOutputStream  clearout = new FileOutputStream(cfile); 
			clearout.write("0 0 0".getBytes());
			clearout.close();
		   }
		} catch (IOException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();}
		}
		else
		{
		   JOptionPane.showMessageDialog(MainFrame.this,"δ�ҵ�ͳ����Ϣ,�����һ����Ϸ���ٲ鿴��Ϣ!","��Ϸͳ��",JOptionPane.ERROR_MESSAGE);
		}
		
		}
	}

//��Ϸͳ�Ʋ˵���Ӧ
class GameHelp implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �Զ����ɵķ������
		HelpDialog jd1 = new HelpDialog(MainFrame.this,"��Ϸ����",false);
		jd1.setLocationRelativeTo(null); 
		jd1.setVisible(true);
	}
}
//�汾��Ϣ�˵���Ӧ
class VersionInfo implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO �Զ����ɵķ������
		VersionDialog jd1 = new VersionDialog(MainFrame.this,"�汾��Ϣ",false);
		jd1.setLocationRelativeTo(null); 
		jd1.setVisible(true);
	}
}
//������Ϸ�˵���Ӧ
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
 
