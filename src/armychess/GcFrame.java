/*
 * 游戏客户端主程序
 */
package armychess;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class GcFrame extends JFrame {
	//界面和特效变量
	private BufferedImage[][] chessimg = new BufferedImage[12][13];
	private BufferedImage chessbg = null;
	private BufferedImage[] tipbox = new BufferedImage[2];
	private BufferedImage[] numimg = new BufferedImage[10];
	private AudioClip eatsound; 
	private AudioClip beginsound; 
	private AudioClip bombsound; 
	private AudioClip deadsound; 
	private AudioClip killedsound; 
	private AudioClip movesound; 
	private AudioClip selectsound; 
	private AudioClip flagsound; 
	private AudioClip startsound; 
	private JMenuItem GameReadyMenuItem;
	private JMenuItem GameSkipMenuItem; 
	private JMenuItem GameSurMenuItem;
	private JMenuItem GamePeaceMenuItem;
	private JMenuItem LoadLayoutMenuItem;
	private JMenuItem SaveLayoutMenuItem;
	private JMenuItem AutosaveMenuItem;
	private JMenuItem SoundeffectMenuItem;
	private Chessboard chessboard = new Chessboard();
	private ChessPanel cp1 = new ChessPanel();
	private ChessPos[][] chesslayout = new ChessPos[17][17];
	//游戏相关变量
	private String serverIP;
	private String gamesave;	
	private PlayerState[] playerstate = new PlayerState[4];
	private boolean peacestate = false; //是否和局
	private int playerid;  // 0,1,2,3 each replays  red,blue,green,purple
	private int gamestate; //0: 布局阶段 1布局结束等待阶段 4测试阶段
	private int gamemode;  //0: 四暗, 1: 双明， 2： 全明
	private int turn;     //当前处于第几个玩家的回合
	private int totalturn;  //回合数总和
	private int clickstate = 0; //点击状态
	private int[] clickloc = {0,0,0,0}; //前两回合的点击位置
	private boolean autosave = true;
	private boolean soundeffect = true;
	//通信相关变量
	private String sendstring = "";
	private String receivestring = "";
	private client gameclient;
	private int connectstate = 0; //连接状态
	//界面控制变量
	private int[] locxs = {39,78,118,157,195,236,268,309,348,387,427,468,507,546,585,625,664}; //每一列的初始位置
	private int[] locys = {14,53,90,129,169,208,247,280,325,359,402,441,478,519,559,598,637};  //每一行的初始位置（中间）
	private int[] locys2 = {14,53,90,129,169,208,241,280,319,359,398,441,478,519,559,598,637}; //每一行的初始位置（两边）
	private int[] clickjudgex = {10,45,85,123,163,203,236,279,318,357,396,437,474,513,553,592,631,666}; //判断点击x的位置
	private int[] clickjudgey = {35,72,112,150,190,229,262,305,344,383,423,466,500,538,578,617,657,694}; //判断点击y的位置
	private int[][] num2to1 ={{-1,-1,-1,-1,-1,-1,89,88,87,86,85,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,84,83,82,81,80,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,74,79,73,78,72,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,71,70,77,69,68,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,67,76,66,75,65,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,64,63,62,61,60,-1,-1,-1,-1,-1,-1},
			{115,110,102,98,95,90,120,-1,121,-1,122,34,37,41,44,54,59},
			{116,111,108,99,105,91,-1,-1,-1,-1,-1,33,46,40,49,53,58},
			{117,112,103,107,96,92,123,-1,124,-1,125,32,36,47,43,52,57},
			{118,113,109,100,106,93,-1,-1,-1,-1,-1,31,45,39,48,51,56},
			{119,114,104,101,97,94,126,-1,127,-1,128,30,35,38,42,50,55},
			{-1,-1,-1,-1,-1,-1,0,1,2,3,4,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,5,15,6,16,7,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,8,9,17,10,11,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,12,18,13,19,14,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,20,21,22,23,24,-1,-1,-1,-1,-1,-1},
			{-1,-1,-1,-1,-1,-1,25,26,27,28,29,-1,-1,-1,-1,-1,-1}
            };//将17*17个位置转换为129位标记
	private int[] kind2to1 = {42,30,41,40,39,38,37,36,35,34,33,32,31}; 
	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public GcFrame(String S) throws IOException {
		addWindowListener(new WindowAdapter() {
			@Override
			/*
			 * 点击红叉会出现对话框提示，防止用户误操作！
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			public void windowClosing(WindowEvent e) {
				try
				{
					if(JOptionPane.showConfirmDialog(GcFrame.this, "此操作将退出游戏，是否继续？","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION)
					{
						close();
					}
				}
				catch(Exception ee)
				{
					ee.printStackTrace();
				}
			}
		});
		//从构造函数的参数读取服务端IP
		serverIP = S;
		//初始化游戏变量
		playerid = 0;
		gamestate = -1;
		gamemode = 2;
		totalturn = 0;
		for(int i=0;i<4;i++){
			playerstate[i]=new PlayerState();
		}
		
		//读取图片、音乐，装载配置文件，初始化布局和窗体
		BufferedImageGet();
		AudioClipGet();
		LoadConfig();
		InitLayout();
		InitFrame();
		
		//创建客户端通信进程
		gameclient = new client();
		new Thread(gameclient).start();
		
		setVisible(true);
	}
	/*
	 * 游戏界面和初始化相关函数
	 */
	//初始化窗口界面
	public void InitFrame() throws IOException{
		setBounds(0, 0, 726, 729);
		setTitle("连接到 "+serverIP+" 的军棋达人游戏");
		GcFrame.this.setResizable(false); 
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null); 	
		//创建菜单条
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		setJMenuBar(menuBar); 
		
		JMenu GameMenu = new JMenu("游戏");
		menuBar.add(GameMenu);
		//游戏准备菜单项
		GameReadyMenuItem = new JMenuItem("准备");
		GameReadyMenuItem.addActionListener(new GameReady());
		GameMenu.add(GameReadyMenuItem);
		//游戏跳过菜单项
		GameSkipMenuItem = new JMenuItem("跳过");
		GameSkipMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(turn == playerid){
					playerstate[playerid].jumpcounts++;
					clickstate = 0;
					 chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
					 chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
					 gamesave = gamesave + "step "+String.valueOf(totalturn)+" jump "+String.valueOf(turn)+'\n';
					sendstring = String.valueOf(totalturn) + " " + String.valueOf(playerid) + " jump";
					deliverystring();
					addturn();
				}
			}
		});
		GameSkipMenuItem.setEnabled(false);
		GameMenu.add(GameSkipMenuItem);
		//游戏投降菜单项
		GameSurMenuItem = new JMenuItem("投降");
		GameSurMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(GcFrame.this, "是否确定投降？","投降确认",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
				if(turn == playerid){
					playerstate[playerid].surstate = 1;
					clickstate = 0;
					chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
					chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
					sendstring = String.valueOf(totalturn) + " " + String.valueOf(playerid) + " sur";
					deliverystring();
					addturn();
				}
			  }
			}
		});
		GameSurMenuItem.setEnabled(false);
		GameMenu.add(GameSurMenuItem);
		//游戏求和菜单栏
		GamePeaceMenuItem = new JMenuItem("求和");
		GamePeaceMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(turn == playerid){
					//playerstate[playerid].peacetick = 2;
					sendstring = "peace " + String.valueOf(playerid) + " 2";
					deliverystring();
				}
			}
		});
		GamePeaceMenuItem.setEnabled(false);
		GameMenu.add(GamePeaceMenuItem);
		
		JMenu LayoutMenu = new JMenu("布局");
		menuBar.add(LayoutMenu);
		//载入布局菜单项
		LoadLayoutMenuItem = new JMenuItem("载入布局");
		LoadLayoutMenuItem.addActionListener(new LoadLayout());
		LayoutMenu.add(LoadLayoutMenuItem);
		
		//保存布局菜单项
		SaveLayoutMenuItem = new JMenuItem("保存布局");
		SaveLayoutMenuItem.addActionListener(new SaveLayout());
		LayoutMenu.add(SaveLayoutMenuItem);
		
		JMenu SetMenu = new JMenu("设置");
		menuBar.add(SetMenu);
		//设置保存菜单项
		AutosaveMenuItem = new JMenuItem("自动保存（已打开）");
		if(!autosave){
			AutosaveMenuItem.setText("自动保存（已关闭）");
		}
		AutosaveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
						
				if(autosave){
					autosave = false;
					AutosaveMenuItem.setText("自动保存（已关闭）");
				}else{
					autosave = true;
					AutosaveMenuItem.setText("自动保存（已打开）");
				}
				FileOutputStream configwrite;
				try {
					configwrite = new FileOutputStream("./save/config.ini");
					String config= "soundeffect = "+String.valueOf(soundeffect)+'\n'+"autosave = "+ String.valueOf(soundeffect);	
					configwrite.write(config.getBytes());
					configwrite.close();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				} 
				
			}
		});
		SetMenu.add(AutosaveMenuItem);
		//设置音效菜单项
		SoundeffectMenuItem = new JMenuItem("背景音效（已打开）");
		if(!soundeffect){
			SoundeffectMenuItem.setText("背景音效（已关闭）");
		}
		SoundeffectMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
						
				if(soundeffect){
					soundeffect = false;
					SoundeffectMenuItem.setText("背景音效（已关闭）");
				}else{
					soundeffect = true;
					SoundeffectMenuItem.setText("背景音效（已打开）");
				}
				FileOutputStream configwrite;
				try {
					configwrite = new FileOutputStream("./save/config.ini");
					String config= "soundeffect = "+String.valueOf(soundeffect)+'\n'+"autosave = "+ String.valueOf(soundeffect);	
					configwrite.write(config.getBytes());
					configwrite.close();
				} catch (IOException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				} 
				
			}
		});
		SetMenu.add(SoundeffectMenuItem);
		//鼠标点击菜单项
		cp1.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				ClickControl(e.getX(),e.getY());
				cp1.repaint();
			}
		});
		//BgPanel ChessBgPanel = new BgPanel("./img/qipan.png");
		//ChessBgPanel.setBounds(0, 0, 720, 675); 
		setContentPane(cp1);
	}
	//读取默认配置文件
	@SuppressWarnings("resource")
	public void LoadConfig(){
		String tempstring;
		String[] splstring;
		try {
			BufferedReader in =new BufferedReader(new FileReader("./save/config.ini"));
			while((tempstring=in.readLine())!=null){
				//System.out.println(gameload[j]);
				splstring = tempstring.split(" ");
				if(splstring.length==3){
					if(splstring[0].equals("soundeffect")){
						if(splstring[2].equals("true")){
							soundeffect = true;
						}else if(splstring[2].equals("false")){	
							soundeffect = false;
						}
					}else if(splstring[0].equals("autosave")){
						if(splstring[2].equals("true")){
							autosave = true;
						}else if(splstring[2].equals("false")){	
							autosave = false;
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	//初始化布局，读取"./save/default.als"
	public void InitLayout() throws IOException{
		for(int i=0;i<17;i++){
			for(int j=0;j<17;j++)
				chesslayout[i][j]=new ChessPos();
		}
		//ReadLayoutFile("./save/layoutsave/default.als",playerid);
		/*
		for(int i=0;i<4;i++){
			ReadLayoutFile("./save/layoutsave/default.als",i);
		}*/
		chessboard.initial();  //初始化棋盘
	}
	//读取声音到clip中
	@SuppressWarnings("deprecation")
	public void AudioClipGet() throws MalformedURLException{
			eatsound = Applet.newAudioClip(new File("./res/sound/eat.wav").toURL());
			beginsound = Applet.newAudioClip(new File("./res/sound/begin.wav").toURL());
			bombsound = Applet.newAudioClip(new File("./res/sound/bomb.wav").toURL());
			deadsound = Applet.newAudioClip(new File("./res/sound/dead.wav").toURL());
			killedsound = Applet.newAudioClip(new File("./res/sound/killed.wav").toURL());
			movesound = Applet.newAudioClip(new File("./res/sound/move.wav").toURL());
			flagsound = Applet.newAudioClip(new File("./res/sound/showflag.wav").toURL());
			startsound = Applet.newAudioClip(new File("./res/sound/start.wav").toURL());
			selectsound = Applet.newAudioClip(new File("./res/sound/select.wav").toURL());
		}
	 //读取棋子、棋盘的图片到内存中
	public void BufferedImageGet() throws IOException{
			int i=0;
			chessbg = ImageIO.read(new File("./res/img/qipan.bmp"));
			tipbox[0]=ImageIO.read(new File("./res/img/tip_box1.png"));
			tipbox[1]=ImageIO.read(new File("./res/img/tip_box2.png"));
			BufferedImage temp=ImageIO.read(new File("./res/img/QZChengCenter.bmp"));
			for(i=0;i<13;i++){
			   chessimg[0][i] = temp.getSubimage(36*i,0,36,27);
			}
			temp=ImageIO.read(new File("./res/img/QZChengLeft.bmp"));
			for(i=0;i<13;i++){
			   chessimg[1][i] = temp.getSubimage(27*i,0,27,36);
			}
			temp=ImageIO.read(new File("./res/img/QZChengRight.bmp"));
			for(i=0;i<13;i++){
			   chessimg[2][i] = temp.getSubimage(27*i,0,27,36);
			}
			temp=ImageIO.read(new File("./res/img/QZLanCenter.bmp"));
			for(i=0;i<13;i++){
			   chessimg[3][i] = temp.getSubimage(36*i,0,36,27);
			}
			temp=ImageIO.read(new File("./res/img/QZLanLeft.bmp"));
			for(i=0;i<13;i++){
			   chessimg[4][i] = temp.getSubimage(27*i,0,27,36);
			}
			temp=ImageIO.read(new File("./res/img/QZLanRight.bmp"));
			for(i=0;i<13;i++){
			   chessimg[5][i] = temp.getSubimage(27*i,0,27,36);
			}
			temp=ImageIO.read(new File("./res/img/QZLvCenter.bmp"));
			for(i=0;i<13;i++){
			   chessimg[6][i] = temp.getSubimage(36*i,0,36,27);
			}
			temp=ImageIO.read(new File("./res/img/QZLvLeft.bmp"));
			for(i=0;i<13;i++){
			   chessimg[7][i] = temp.getSubimage(27*i,0,27,36);
			}
			temp=ImageIO.read(new File("./res/img/QZLvRight.bmp"));
			for(i=0;i<13;i++){
			   chessimg[8][i] = temp.getSubimage(27*i,0,27,36);
			}
			temp=ImageIO.read(new File("./res/img/QZZiCenter.bmp"));
			for(i=0;i<13;i++){
			   chessimg[9][i] = temp.getSubimage(36*i,0,36,27);
			}
			temp=ImageIO.read(new File("./res/img/QZZiLeft.bmp"));
			for(i=0;i<13;i++){
			   chessimg[10][i] = temp.getSubimage(27*i,0,27,36);
			}
			temp=ImageIO.read(new File("./res/img/QZZiRight.bmp"));
			for(i=0;i<13;i++){
			   chessimg[11][i] = temp.getSubimage(27*i,0,27,36);
			}
			temp=ImageIO.read(new File("./res/img/num.bmp"));
			for(i=0;i<10;i++){
				numimg[i] = temp.getSubimage(13*i, 0, 13, 23);
			}
	}
	//从String S对应的路径中读取布局
	public void ReadLayoutFile(String S,int id) throws IOException{
			int[][] save = new int[6][5];
			int i=0,j=0,bool=0;
			String rline;
			String[] cfstrs;
			BufferedReader in =new BufferedReader(new FileReader(S));
			while((rline=in.readLine())!=null){
				if(i<6){
					cfstrs = rline.split(" ");
					if(cfstrs.length==5){
						for(j=0;j<5;j++){
							save[i][j]=Integer.parseInt(cfstrs[j]);
						}
					}
					else{
						bool=1;
						break;
					}
				}
				else{
					bool=1;
					break;
				}
				i++;
			}
			in.close();
			if(save[1][1]!=-1||save[1][3]!=-1||save[2][2]!=-1||save[3][1]!=-1||save[3][3]!=-1){
				bool = 1;
			}
			int[] count = new int[13];
			for(i=0;i<13;i++) count[i] = 0;
			for(i=0;i<6;i++ ){
				for(j = 0;j<5;j++){
					if(save[i][j]!=-1){
						count[save[i][j]]++;
						if(save[i][j]==12){
							if(i!=5||j!=1){
								bool = 1;
								break;
							}
						}else if(save[i][j]==1){
							if(i==0){
								bool = 1;
								break;
							}
						}else if(save[i][j]==2){
							if(i<4){
								bool = 1;
								break;
							}
						}
					}
				}
			}
			if(count[0]!=0||count[1]!=2||count[2]!=3||count[3]!=1||count[4]!=1||count[5]!=2||count[6]!=2||count[7]!=2||count[8]!=2||count[9]!=3||count[10]!=3||count[11]!=3||count[12]!=1){
				bool = 1;
			}
			if(bool==0){
				ReadLayout(save,id);
			}else{
				JOptionPane.showMessageDialog(GcFrame.this,"布局有误，请选择其他布局!","读取布局",JOptionPane.ERROR_MESSAGE);
			}
		}
	//读取布局,a为6*5矩阵，b为第几号人
	public void ReadLayout(int[][] a,int b){
			if(a.length!=6||a[0].length!=5){
				JOptionPane.showMessageDialog(GcFrame.this,"布局有误，请选择其他布局!","读取布局",JOptionPane.ERROR_MESSAGE);
			}else{
				int sub = b-playerid;
				if(sub<0) sub=sub+4;
				if(sub%4==0){
					for(int i=0;i<6;i++){
						for(int j=0;j<5;j++){
							if((chesslayout[i+11][j+6].chessid = a[i][j])!=-1)
							chesslayout[i+11][j+6].owner = b;
							chesslayout[i+11][j+6].tipstate = 0;
						}
					}
				}else if(sub%4==1){
					for(int i=0;i<6;i++){
						for(int j=0;j<5;j++){
							if((chesslayout[10-j][i+11].chessid = a[i][j])!=-1)
							chesslayout[10-j][i+11].owner = b;
							chesslayout[10-j][i+11].tipstate = 0;
							if(gamemode<2){
							chesslayout[10-j][i+11].visionstate = 0;
							}
						}
					}
				}else if(sub%4==2){
					for(int i=0;i<6;i++){
						for(int j=0;j<5;j++){
							if((chesslayout[5-i][10-j].chessid = a[i][j])!=-1)
							chesslayout[5-i][10-j].owner = b;
							chesslayout[5-i][10-j].tipstate = 0;
							if(gamemode<1){
							chesslayout[5-i][10-j].visionstate = 0;
							}
						}
					}
				}else if(sub%4==3){
					for(int i=0;i<6;i++){
						for(int j=0;j<5;j++){
							if((chesslayout[j+6][5-i].chessid = a[i][j])!=-1)
							chesslayout[j+6][5-i].owner = b;
							chesslayout[j+6][5-i].tipstate = 0;
							if(gamemode<2){
							chesslayout[j+6][5-i].visionstate = 0;
							}
						}
					}
				}
			}
			
		}
	//保存布局
	public void SaveLayoutFile(String S) throws IOException{
			FileWriter fw=new FileWriter(new File(S));
			for(int i=11;i<17;i++){
				for(int j=6;j<11;j++){
					fw.write(String.valueOf(chesslayout[i][j].chessid)+" ");
				}
				fw.write("\n");
			}
			fw.close();
		}
	//同步布局，将17*17同步到129中去
	public void sync2to1(int x,int y){
			int z = num2to1[x][y];
			if(z>-1){
				if(chesslayout[x][y].chessid==-1){
					chessboard.pos[z].party = 5;
					chessboard.pos[z].type = 42;
				}else {
					chessboard.pos[z].type = kind2to1[chesslayout[x][y].chessid];
					chessboard.pos[z].party = chesslayout[x][y].owner+1;
				}
			}
		}
	/*
	 * 用户点击处理相关函数
	 */
	//处理点击位置
	public void ClickControl(int x,int y){
		int clocx=0,clocy=0,temp,tempchessid1,tempchessid2,tempowner1,tempowner2;
		while(clocx<18){
			if(y<clickjudgex[clocx])
				break;
			clocx=clocx+1;}
		clocx = clocx-1;
		while(clocy<18){
		    if(x<clickjudgey[clocy])
				break;
			clocy=clocy+1;}
		clocy = clocy-1;
		if(clocx>-1&&clocx<17&&clocy>-1&&clocy<17){     //获取到点击区域的坐标
			if(gamestate==0){            //布局状态
			 if(chesslayout[clocx][clocy].owner==playerid){  //布局状态下必须要点自己的棋
			  if(clickstate==0){
				  chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
				  chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
				  chesslayout[clocx][clocy].tipstate = 1;
				  clickstate = 1;
				  clickloc[2]=clocx;
				  clickloc[3]=clocy;
				  if(soundeffect) selectsound.play();
			  }else{
				  if(clocx==clickloc[2]&&clocy==clickloc[3]){
					  chesslayout[clocx][clocy].tipstate = 0;
					  
				  }else{
					  if(LayoutJudge(clocx-11,clocy-6,chesslayout[clocx][clocy].chessid,clickloc[2]-11,clickloc[3]-6,chesslayout[clickloc[2]][clickloc[3]].chessid)>0){
						  temp=chesslayout[clocx][clocy].chessid;
						  chesslayout[clocx][clocy].chessid=chesslayout[clickloc[2]][clickloc[3]].chessid;
						  chesslayout[clickloc[2]][clickloc[3]].chessid=temp;
						  clickloc[0]=clocx;
						  clickloc[1]=clocy;
						  chesslayout[clocx][clocy].tipstate = 1;
						  if(soundeffect)
						    movesound.play();
					  }else
						  chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
				  }
				  clickstate = 0;
			  }
			}else{
			   chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
			   chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
			   clickstate = 0;
			}
		   }else if(gamestate == 4){        //调试模式
				 if(clickstate==0){
					 if(chesslayout[clocx][clocy].owner==turn){
					  chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
					  chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
					  chesslayout[clocx][clocy].tipstate = 1;
					  clickstate = 1;
					  clickloc[2]=clocx;
					  clickloc[3]=clocy;
					  if(soundeffect) selectsound.play();
					  }
				 }else{
					 tempchessid1 = chesslayout[clickloc[2]][clickloc[3]].chessid;
					 tempchessid2 = chesslayout[clocx][clocy].chessid;
					 tempowner1 = chesslayout[clickloc[2]][clickloc[3]].owner;
					 tempowner2 = chesslayout[clocx][clocy].owner;
					 if(PlayJudge(clickloc[2],clickloc[3],clocx,clocy)>0){
						 clickloc[0]=clocx;
						 clickloc[1]=clocy;
						 chesslayout[clocx][clocy].tipstate = 1;
						 gamesave = gamesave + "step "+ String.valueOf(totalturn)+" "+ String.valueOf(clickloc[2])+" "+String.valueOf(clickloc[3])+" "+ String.valueOf(tempchessid1)+" "+String.valueOf(tempowner1)+" "+String.valueOf(clocx)+" "+String.valueOf(clocy)+" "+String.valueOf(tempchessid2)+" "+String.valueOf(tempowner2)+'\n';
						 addturn();
					 }else{
						 chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
					 }
					 clickstate = 0;
				 }
		   }else if(gamestate == 2){        //正常游戏模式
			   if(turn == playerid){        //只有在自己回合才会有操作
			   if(clickstate==0){
					 if(chesslayout[clocx][clocy].owner==turn){
					  chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
					  chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
					  chesslayout[clocx][clocy].tipstate = 1;
					  clickstate = 1;
					  clickloc[2]=clocx;
					  clickloc[3]=clocy;
					  if(soundeffect) selectsound.play();
					  }
				 }else{
					 tempchessid1 = chesslayout[clickloc[2]][clickloc[3]].chessid;
					 tempchessid2 = chesslayout[clocx][clocy].chessid;
					 tempowner1 = chesslayout[clickloc[2]][clickloc[3]].owner;
					 tempowner2 = chesslayout[clocx][clocy].owner;
					 if(PlayJudge(clickloc[2],clickloc[3],clocx,clocy)>0){
						 clickloc[0]=clocx;
						 clickloc[1]=clocy;
						 chesslayout[clocx][clocy].tipstate = 1;
						 gamesave = gamesave + "step "+ String.valueOf(totalturn)+" "+ String.valueOf(clickloc[2])+" "+String.valueOf(clickloc[3])+" "+ String.valueOf(tempchessid1)+" "+String.valueOf(tempowner1)+" "+String.valueOf(clocx)+" "+String.valueOf(clocy)+" "+String.valueOf(tempchessid2)+" "+String.valueOf(tempowner2)+'\n';
						 sendstring = String.valueOf(totalturn)+" "+String.valueOf(playerid)+" "+String.valueOf(clickloc[2])+" "+String.valueOf(clickloc[3])+" "+String.valueOf(clocx)+" "+String.valueOf(clocy);
						 deliverystring();
						 addturn();
					 }else{
						 chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
					 }
					 clickstate = 0;
				}
			   }
		   }
		}
	}
	//判定能否交换棋子位置 x为列，取值为0到5,y为行，取值为0到4
	public int LayoutJudge(int x1,int y1,int chess1,int x2,int y2,int chess2){
		if(chess1 == 12)
		{
			if(!(x2==5&&(y2==1||y2==3))){
				return 0;
			}
		}else if(chess1 == 1){
			if(x2 == 0){
				return 0;
			}
		}
		else if(chess1 == 2){
			if(x2<4){
				return 0;
			}
		}
		if(chess2 == 12)
		{
			if(!(x1==5&&(y1==1||y1==3))){
				return 0;
			}
		}else if(chess2 == 1){
			if(x1 == 0){
				return 0;
			}
		}
		else if(chess2 == 2){
			if(x1<4){
				return 0;
			}
		}
		return 1;
	}
	//攻击时判断,x1、y1为起始位置
	public int PlayJudge(int x1,int y1,int x2,int y2){
		int z1 = num2to1[x1][y1];
		int z2 = num2to1[x2][y2];
		int rec;
		int flag;
		if(z2<0||z1<0){
			return 0;
		}else{
			/*
			System.out.println(chessboard.pos[z1].type);
			System.out.println(chessboard.pos[z1].party);
			System.out.println(chessboard.pos[z2].type);
			System.out.println(chessboard.pos[z2].party);*/
			rec = chessboard.fight(z1,z2);
			//System.out.println(rec);
			if(rec==0){
				return 0;
			}else if(rec==1){
				chesslayout[x2][y2].chessid=chesslayout[x1][y1].chessid;
				chesslayout[x2][y2].owner=chesslayout[x1][y1].owner;
				chesslayout[x2][y2].visionstate=chesslayout[x1][y1].visionstate;
				chesslayout[x1][y1].chessid = -1;
				chesslayout[x1][y1].owner = -1;
				if(soundeffect) movesound.play();
			}else if(rec==2){
				flag = 0;
				if(chesslayout[x1][y1].chessid==3){
					ShowFlag(chesslayout[x1][y1].owner);
					flag = 1;
				}
				if(chesslayout[x2][y2].chessid==3){
					ShowFlag(chesslayout[x2][y2].owner);
					flag = 1;
				}
				if(flag == 1){
				if(soundeffect)	flagsound.play();
				}else{
				if(soundeffect)	bombsound.play();
				}
				chesslayout[x1][y1].chessid = -1;
				chesslayout[x1][y1].owner = -1;
				chesslayout[x2][y2].chessid = -1;
				chesslayout[x2][y2].owner = -1;
			}else if(rec==3){
				chesslayout[x2][y2].chessid=chesslayout[x1][y1].chessid;
				chesslayout[x2][y2].owner=chesslayout[x1][y1].owner;
				chesslayout[x2][y2].visionstate=chesslayout[x1][y1].visionstate;
				chesslayout[x1][y1].chessid = -1;
				chesslayout[x1][y1].owner = -1;
				if(soundeffect) eatsound.play();
			}else{
				flag = 0;
				if(chesslayout[x1][y1].chessid==3){
					ShowFlag(chesslayout[x1][y1].owner);
					flag =1;
				}
				if(flag == 1){
				if(soundeffect)	flagsound.play();
				}else{
				if(soundeffect)	killedsound.play();
				}
				chesslayout[x1][y1].chessid = -1;
				chesslayout[x1][y1].owner = -1;
			}
		}
		return 1;
	}
	//司令死，亮军旗
	public void ShowFlag(int id){
		int[] x={7,9,0,16,0,16,7,9};
		int[] y={0,0,7,7,9,9,16,16};
		for(int i=0;i<8;i++){
			if(chesslayout[x[i]][y[i]].chessid==12&&chesslayout[x[i]][y[i]].owner==id){
				chesslayout[x[i]][y[i]].visionstate = 1;
			}
		}
	}
    /*
     * 游戏过程相关函数
     */
	//某玩家已经准备好
	public void gameprepared(int id){
		for(int i=0;i<17;i++){
			for(int j=0;j<17;j++){
				sync2to1(i,j);
			}
		}
       //if(!chessboard.check(id+1)){
	    	playerstate[id].readystate = 1;
			if(soundeffect)	startsound.play();
			if(playerstate[0].readystate>0&&playerstate[1].readystate>0&&playerstate[2].readystate>0&&playerstate[3].readystate>0)
				    gamestart();
	   // }
	}
	//开始游戏
	public void gamestart(){
		for(int i=0;i<17;i++){
			for(int j=0;j<17;j++){
				sync2to1(i,j);
			}
		}
		gamesave = "playerid "+String.valueOf(playerid)+"\n";
		gamesave = gamesave +"InitLayout\n";
		for(int i=0;i<17;i++){
			for(int j=0;j<17;j++){
				gamesave = gamesave + String.valueOf(chesslayout[i][j].chessid)+" ";
			}
			gamesave = gamesave + '\n';
		}
		gamesave = gamesave + "game begin\n";
		//gamestate = 4;
		gamestate = 2;
		turn = -1;
		totalturn = 0;
		addturn();
		if(soundeffect) beginsound.play();
	}
	//增加回合
	public void addturn(){
		cp1.repaint();
		int i=0;
		for(i=0;i<4;i++){
			if(playerstate[i].failurestate==0)
			   failurejudge(i);
		}
		i=0;
		while(i<3){
			turn = turn+1;
			if(turn == 4 ){
				turn = 0;
			}
			if(playerstate[turn].failurestate==0){
				break;
			}
			i++;
		}
		cp1.repaint();
		totalturn =totalturn+1;
		if(turn != playerid){
		GameSkipMenuItem.setEnabled(false);
		GamePeaceMenuItem.setEnabled(false);
		GameSurMenuItem.setEnabled(false);
		}else{
		GameSkipMenuItem.setEnabled(true);
		GamePeaceMenuItem.setEnabled(true);
		GameSurMenuItem.setEnabled(true);
		}
		//System.out.println("turn="+(char)('0'+turn));
		/*
		if(turn == playerid){
		for(int j=0;j<4;j++){
			i = (turn+j)%4;
			//System.out.println(i);
			//System.out.println(playerstate[i].peacetick);
			if(playerstate[i].peacetick == 2){
				if(i!=turn){
				char c = (char) ('1'+i);
				if(JOptionPane.showConfirmDialog(GcFrame.this, "玩家"+c+"请求和局，是否同意？","和局请求",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
					playerstate[turn].peacetick = 1;
				}else{
					playerstate[turn].peacetick = -1;
					playerstate[i].peacetick = 1;
				}}
			}else if(playerstate[i].peacetick < 0){
				if(turn == i){
					playerstate[i].peacetick = 0;
				}else if(playerstate[turn].peacetick > 0){
					char c = (char) ('1'+i);
					JOptionPane.showMessageDialog(GcFrame.this,"玩家"+c+"拒绝和棋！","和局回复",JOptionPane.INFORMATION_MESSAGE);
					playerstate[turn].peacetick = 0;
				}
			}
		 }
		}
		*/
		gameend();
	}
	//失败判定
	public void failurejudge(int id){
		boolean flag = true;
		if(chessboard.failture(id +1)==1){
		   flag =false;
		   JOptionPane.showMessageDialog(GcFrame.this,"玩家"+String.valueOf(id+1)+"军旗被擒，全军覆没！","游戏信息",JOptionPane.INFORMATION_MESSAGE);
		}else if(chessboard.failture(id +1)==2){
		   flag =false;
		   JOptionPane.showMessageDialog(GcFrame.this,"玩家"+String.valueOf(id+1)+"无棋可走，全军覆没！","游戏信息",JOptionPane.INFORMATION_MESSAGE);
		}
		boolean flag1 = (id==playerid);
		if(playerstate[id].jumpcounts==5) {flag = false;
		   JOptionPane.showMessageDialog(GcFrame.this,"玩家"+String.valueOf(id+1)+"累计跳过次数达到5次，全军覆没！","游戏信息",JOptionPane.INFORMATION_MESSAGE);
		   gamesave = gamesave +"jumpcounts=5 "+String.valueOf(id)+'\n';
		}
		if(playerstate[id].surstate==1) {flag = false;
		   JOptionPane.showMessageDialog(GcFrame.this,"玩家"+String.valueOf(id+1)+"投降，全军覆没！","游戏信息",JOptionPane.INFORMATION_MESSAGE);
		   gamesave = gamesave +"step "+String.valueOf(totalturn)+" sur "+String.valueOf(id)+'\n';
		}
		if(!flag){
			playerstate[id].failurestate = 1;
			gamesave = gamesave +"failure "+String.valueOf(id);
			//System.out.println(id);
			//System.out.println("failure");
			for(int i=0;i<17;i++){
				for(int j=0;j<17;j++){
					if(chesslayout[i][j].owner==id){
						gamesave = gamesave + " "+String.valueOf(i)+" "+String.valueOf(j)+" "+String.valueOf(chesslayout[i][j].chessid);
						chesslayout[i][j].chessid = -1;
						chesslayout[i][j].owner = -1;
						sync2to1(i,j);
					}if(flag1){
						if(chesslayout[i][j].owner==(playerid+2)%4){
							chesslayout[i][j].visionstate = 1;
						}
					}
				}
			}
			gamesave =gamesave +'\n';
			if(soundeffect) deadsound.play();
		}
	}
	//游戏结束
	public void gameend(){
		boolean flag= false;
		int allcounts = 0,wincounts = 0;
		File cfile=new File(".\\save\\count.ini");
		if(cfile.isFile()&&cfile.exists()){
		try {
			BufferedReader in =new BufferedReader(new FileReader(cfile));
			String[] strspl = in.readLine().split(" ");
			if(strspl.length == 3){
				allcounts = Integer.parseInt(strspl[0]);
				wincounts = Integer.parseInt(strspl[1]);
			}else{
				allcounts = 0;
				wincounts = 0;
			}
			in.close();
		} catch (IOException e2) {
			// TODO 自动生成的 catch 块
			e2.printStackTrace();
		}}else{
			allcounts = 0;
			wincounts = 0;
		}
		if((playerstate[playerid].failurestate==1)&&(playerstate[(playerid+2)%4].failurestate==1)){
			flag = true;
			JOptionPane.showMessageDialog(GcFrame.this,"您输了！请再接再厉","游戏失败",JOptionPane.INFORMATION_MESSAGE);
		}else if((playerstate[(playerid+1)%4].failurestate==1)&&(playerstate[(playerid+3)%4].failurestate==1))
		{   
			flag = true;
			wincounts ++;
			JOptionPane.showMessageDialog(GcFrame.this,"您赢了！恭喜！","游戏获胜",JOptionPane.INFORMATION_MESSAGE);
		//}else if(((playerstate[0].peacetick>0)&&(playerstate[1].peacetick>0)&&(playerstate[2].peacetick>0)&&(playerstate[3].peacetick>0))||totalturn>999)
		}else if(peacestate)
		{   
			gamesave = gamesave + "peace\n";
			for(int i=0;i<4;i++){
				if(playerstate[i].failurestate == 0){
					gamesave = gamesave + "failure "+String.valueOf(i);
					for(int j=0;j<17;j++){
						for(int k=0;k<17;k++){
							if(chesslayout[j][k].owner == i)
							gamesave = gamesave + " "+String.valueOf(j)+" "+String.valueOf(k)+" "+String.valueOf(chesslayout[j][k].chessid);
						}
					}
					gamesave = gamesave + '\n';
				}
			}
			flag = true;
			JOptionPane.showMessageDialog(GcFrame.this,"所有玩家同意和棋，游戏平局！","游戏平局",JOptionPane.INFORMATION_MESSAGE);
		}
		if(flag){
			allcounts ++;
			FileOutputStream clearout;
			try {
				clearout = new FileOutputStream(cfile);
				double winrate = (double) wincounts / (double) allcounts *100;
				String scount= String.valueOf(allcounts)+" "+String.valueOf(wincounts)+" "+String.format("%.2f",winrate);
				clearout.write(scount.getBytes());
				clearout.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} 
		
		try {
			InitLayout();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		gamesave = gamesave +"game end";
		if(autosave){
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm");//设置日期格式
		//System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		FileOutputStream gamesaveout;
		try {
			gamesaveout = new FileOutputStream(new File("./save/gamesave/jq"+df.format(new Date())+".ags"));
			gamesaveout.write(gamesave.getBytes());
			gamesaveout.close();
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} 
		}else{
			if(JOptionPane.showConfirmDialog(GcFrame.this, "是否保存布局？","保存布局",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
				ExtensionFileFilter filter = new ExtensionFileFilter();
 				filter.addExtension("ags");
 				filter.setDescription("army chess gave save文件(*.ags)");
 				JFileChooser chooser = new JFileChooser("./save/gamesave");
 				chooser.setAcceptAllFileFilterUsed(false); 
 				chooser.addChoosableFileFilter(filter);
 				int result = chooser.showSaveDialog(GcFrame.this);
 				if(result == JFileChooser.APPROVE_OPTION)
 				{
 					String GSname = chooser.getSelectedFile().getPath();
 					if(GSname.indexOf(".ags")<0){
 						GSname = GSname+".ags";
 					}
 					//System.out.println(LSname+".ags");
 					try {
 						FileOutputStream gamesaveout = new FileOutputStream(new File(GSname));
 						gamesaveout.write(gamesave.getBytes());
 						gamesaveout.close();
 					} catch (IOException e1) {
 						// TODO 自动生成的 catch 块
 						e1.printStackTrace();
 					}
 				}
			}
		}
		sendstring = "-1";
		deliverystring();
		GameReadyMenuItem.setEnabled(true);
		LoadLayoutMenuItem.setEnabled(true);
		SaveLayoutMenuItem.setEnabled(true);
		GameSkipMenuItem.setEnabled(false);
		GamePeaceMenuItem.setEnabled(false);
		GameSurMenuItem.setEnabled(false);
		gamestate = 0;
		clickstate = 0;
		totalturn = 0;
		peacestate = false;
		chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
		chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
		for(int i=0;i<4;i++){
			playerstate[i].readystate = 0;
			playerstate[i].failurestate = 0;
			playerstate[i].jumpcounts = 0;
			//playerstate[i].peacetick = 0;
			playerstate[i].surstate = 0;
		}
		try {
			ReadLayoutFile("./save/layoutsave/default.als",playerid);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		cp1.repaint();
		}
	}
	/*
	 * 通信相关函数
	 */	
	//发送字符串
	 public void deliverystring(){
		 System.out.println(sendstring);
		 gameclient.out.println(sendstring);
	 }
	//读取字符串
	 public void readstring(){
		int msgid,msgplyid,msgtype; 
		String[] respl = receivestring.split(" ") ;
		if(respl.length > 1){
			if(respl[0].equals("---")){
				connectstate = 0;
				if(gamestate > 1){
				JOptionPane.showMessageDialog(GcFrame.this,"与服务端断开连接，游戏结束！","连接出错",JOptionPane.ERROR_MESSAGE);
				}
			}else if(respl[0].equals("peace")){
				msgplyid = Integer.parseInt(respl[1]);
				char c = (char)('1'+msgplyid);
				msgtype = Integer.parseInt(respl[2]);
				if(msgplyid != playerid){
					if(msgtype == 2){
						if(JOptionPane.showConfirmDialog(GcFrame.this, "玩家"+c+"请求和局，是否同意？","和局请求",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
							sendstring = "peace "+String.valueOf(playerid)+ " 1";
							deliverystring();
						}else{
							sendstring = "peace "+String.valueOf(playerid)+ " -1";
							deliverystring();
						}
					}else if(msgtype == 3){
						peacestate = true;
						gameend();
					}else if(msgtype == -2){
						JOptionPane.showMessageDialog(GcFrame.this,"玩家"+c+"拒绝和棋！","和局回复",JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}else{
			msgid = Integer.parseInt(respl[0]);
			if(msgid == -1){       //刚建立连接时接收到的游戏模式和自己的id
				gamemode = Integer.parseInt(respl[1]);
				playerid = Integer.parseInt(respl[2]);
				try {
					ReadLayoutFile("./save/layoutsave/default.als",playerid);
					totalturn = 0;
					gamestate = 0;
					cp1.repaint();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}else if(msgid == -2){
				msgplyid = Integer.parseInt(respl[1]);
				playerstate[playerid].surstate = 0;
				if(turn == msgplyid) addturn();
			}else if(msgid == 0){  //接收到的布局消息
				msgplyid = Integer.parseInt(respl[1]);
				if(msgplyid!=playerid&&respl.length == 32){    //接收到其他人布局
					int[][] olayout = new int[6][5];
					for(int i=0;i<6;i++){
						for(int j=0;j<5;j++){
							olayout[i][j] = Integer.parseInt(respl[5*i+j+2]);
						}
					}
					ReadLayout(olayout,msgplyid);
					gameprepared(msgplyid);
				}
			}else if(msgid == totalturn)
			{   
				msgplyid = Integer.parseInt(respl[1]);
				if(turn!=playerid&&msgplyid == turn){
					if(respl[2].equals("jump")){
						playerstate[msgplyid].jumpcounts++;
						gamesave = gamesave + "step "+String.valueOf(totalturn)+" jump "+String.valueOf(turn)+'\n';
						addturn();
					}else if(respl[2].equals("sur")){
						playerstate[msgplyid].surstate = 1;
						addturn();
					}else if(respl.length == 6){
						 int tlocx1 = Integer.parseInt(respl[2]);
						 int tlocy1 = Integer.parseInt(respl[3]);
						 int tlocx2 = Integer.parseInt(respl[4]);
						 int tlocy2 = Integer.parseInt(respl[5]);
						 int locx1 = 0,locy1 = 0,locx2 = 0,locy2 = 0;
						 if(msgplyid == (playerid+1)%4){
							 locx1 = 16 - tlocy1;
							 locy1 = tlocx1;
							 locx2 = 16 - tlocy2;
							 locy2 = tlocx2;
						 }else  if(msgplyid == (playerid+2)%4){
							 locx1 = 16 - tlocx1;
							 locy1 = 16 - tlocy1;
							 locx2 = 16 - tlocx2;
							 locy2 = 16 - tlocy2;
						 }else if(msgplyid == (playerid+3)%4){
							 locx1 = tlocy1;
							 locy1 = 16 - tlocx1;
							 locx2 = tlocy2;
							 locy2 = 16 - tlocx2;
						 } 
						 int tempchessid1 = chesslayout[locx1][locy1].chessid;
						 int tempchessid2 = chesslayout[locx2][locy2].chessid;
						 int tempowner1 = chesslayout[locx1][locy1].owner;
						 int tempowner2 = chesslayout[locx2][locy2].owner;
						 if(PlayJudge(locx1,locy1,locx2,locy2)>0){
							 chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
							 chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
							 clickloc[2]=locx1;
							 clickloc[3]=locy1;
							 clickloc[0]=locx2;
							 clickloc[1]=locy2;
							 chesslayout[locx1][locy1].tipstate = 1;
							 chesslayout[locx2][locy2].tipstate = 1;
							 gamesave = gamesave + "step "+ String.valueOf(totalturn)+" "+ String.valueOf(locx1)+" "+String.valueOf(locy1)+" "+ String.valueOf(tempchessid1)+" "+String.valueOf(tempowner1)+" "+String.valueOf(locx2)+" "+String.valueOf(locy2)+" "+String.valueOf(tempchessid2)+" "+String.valueOf(tempowner2)+'\n';
							 addturn();
						 }
					}
				}
			 }
			}
		}
	 }
	//关闭菜单
	 public void close(){
		 if(connectstate == 1){
			 sendstring = "--- See you, bye! ---";
			 deliverystring();
			 connectstate = 0;
		 }
	    	GcFrame.this.dispose();
	 }
	//装载布局菜单事件响应
     class LoadLayout implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				ExtensionFileFilter filter = new ExtensionFileFilter();
				filter.addExtension("als");
				filter.setDescription("army chess layout save文件(*.als)");
				JFileChooser chooser = new JFileChooser("./save/layoutsave");
				chooser.setAcceptAllFileFilterUsed(false); 
				chooser.addChoosableFileFilter(filter);
				int result = chooser.showDialog(GcFrame.this , "调入布局");
				if(result == JFileChooser.APPROVE_OPTION)
				{
					String LSname = chooser.getSelectedFile().getPath();
					//System.out.println(LSname);
					try {
						ReadLayoutFile(LSname,playerid);
						cp1.repaint();
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}
				
			}
		}
	//保存布局菜单事件响应
     class SaveLayout implements ActionListener{

 			@Override
 			public void actionPerformed(ActionEvent e) {
 				// TODO 自动生成的方法存根
 				ExtensionFileFilter filter = new ExtensionFileFilter();
 				filter.addExtension("als");
 				filter.setDescription("army chess layout save文件(*.als)");
 				JFileChooser chooser = new JFileChooser("./save/layoutsave");
 				chooser.setAcceptAllFileFilterUsed(false); 
 				chooser.addChoosableFileFilter(filter);
 				int result = chooser.showSaveDialog(GcFrame.this);
 				if(result == JFileChooser.APPROVE_OPTION)
 				{
 					String LSname = chooser.getSelectedFile().getPath();
 					if(LSname.indexOf(".als")<0){
 						LSname = LSname+".als";
 					}
 					//System.out.println(LSname+".als");
 					try {
 						SaveLayoutFile(LSname);
 					} catch (IOException e1) {
 						// TODO 自动生成的 catch 块
 						e1.printStackTrace();
 					}
 				}
 				
 			}
 		}
    //准备游戏菜单事件响应
     class GameReady implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				for(int i=0;i<17;i++){
					for(int j=0;j<17;j++){
						sync2to1(i,j);
					}
				}
				/*
				for(int i=0;i<30;i++){
					System.out.println(chessboard.pos[i].type);
					System.out.println(chessboard.pos[i].party);
				}*/
				//boolean flag = true;
				//just for test
				//for(int i=1;i<5;i++){
					//if(!chessboard.check(0)){
					//	JOptionPane.showMessageDialog(GcFrame.this,"布局有误，请选择其他布局!","准备游戏",JOptionPane.ERROR_MESSAGE);
					//	flag = false;
				//		break;
					//}else{
						playerstate[playerid].readystate = 1;
						if(soundeffect)	startsound.play();
					//}
				//}
				//if(flag){
				gamestate = 1; //准备阶段，等待其他玩家布局
				clickstate = 0;
				chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
				chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
				GameReadyMenuItem.setEnabled(false);
				LoadLayoutMenuItem.setEnabled(false);
				SaveLayoutMenuItem.setEnabled(false);
				try {
					SaveLayoutFile("./save/layoutsave/default.als");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				//}
				sendstring = "0 "+String.valueOf(playerid);
				for(int i=11;i<17;i++){
					for(int j=6;j<11;j++){
						sendstring = sendstring + " " + String.valueOf(chesslayout[i][j].chessid);
					}
				}
				deliverystring();
				//send string
				if(playerstate[0].readystate>0&&playerstate[1].readystate>0&&playerstate[2].readystate>0&&playerstate[3].readystate>0)
				    gamestart();
			}
     }
	//JPanel扩展类，用于绘制棋盘
	 class ChessPanel extends JPanel
	 {
		 public void paint(Graphics g){
			 g.drawImage(chessbg,0,0,null);
			 if(gamestate>1&&turn>-1){
				 g.drawImage(chessimg[turn*3][0],90,13,null);
				 int a = totalturn%10;
				 int b = (totalturn/10)%10;
				 int c = totalturn/100;
			     g.drawImage(numimg[c],40,15,null);
			     g.drawImage(numimg[b],53,15,null);
			     g.drawImage(numimg[a],66,15,null);
			 }
			 int lockind,chesskind,olkind;
			 for(int j=0;j<17;j++){
				 if(j<6) lockind = 1;
				 else if(j<11) lockind = 0;
				 else lockind = 2;
				 for(int i=0;i<17;i++){
					 if((chesskind=chesslayout[i][j].chessid)!=-1){
					  if(chesslayout[i][j].visionstate == 0)  chesskind = 0;
					  olkind = chesslayout[i][j].owner*3+lockind;
					  if(lockind==0)
					    g.drawImage(chessimg[olkind][chesskind],locxs[j],locys[i],null);
					  else
					    g.drawImage(chessimg[olkind][chesskind],locxs[j],locys2[i],null);
					 }
					 if(chesslayout[i][j].tipstate==1){
					   if(lockind==0)
					     g.drawImage(tipbox[0],locxs[j]-2,locys[i]-2,null);
					   else
					     g.drawImage(tipbox[1],locxs[j]-2,locys2[i]-2,null); 
					 }
				 }
			 }
		 }
	 }
	 //玩家状态类，记录玩家状态
	 class PlayerState{
		 public int failurestate = 0;
		 public int readystate = 0;
		 public int jumpcounts = 0;
		 public int surstate = 0;
	 }
	 //客户端线程
	 public class client implements Runnable{
		 public Socket socket;
		 public BufferedReader in;
		 public PrintWriter out;
		 public boolean flag;
		 public client(){
			 flag = true;
		     try{
		    	 try{
		        socket = new Socket(serverIP, 10000);
		    	 }catch(UnknownHostException e1){
		        	JOptionPane.showMessageDialog(GcFrame.this,"无法连接，请确认输入IP地址的有效性!","连接游戏",JOptionPane.ERROR_MESSAGE);
		        	close();
		        	flag = false;
		        }
		    	 if(flag){		       
		          out = new PrintWriter(socket.getOutputStream(),true);
		          in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
		    	 }
		        }catch (IOException e ){
		        	close();
		        }
		         connectstate = 1;
		    }
		 		 
		 public void run(){
			 if(flag){
			  receivestring = "";
		   	  while (!receivestring.equals("--- See you, bye! ---")){
			     System.out.println(receivestring);
			     try {
				     receivestring = in.readLine();
				     readstring();
				  } catch (IOException e) {
				         e.printStackTrace();
				 }
			     if(connectstate == 0){
			    	 break;
			     }
			  }
		    	
			try {
				in.close();
				socket.close();
				 out.close();
				 close();
			 } catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			 }
			 }
		 }
	}
}
