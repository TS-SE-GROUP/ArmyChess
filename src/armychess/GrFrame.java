/*
 * 回放游戏的主程序
 * create by c，s  2015.12.17
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
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GrFrame extends JFrame {
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
	private AudioClip flagsound; 
	private JMenuItem StartReplayMenuItem;
	private JMenuItem PauseReplayMenuItem; 
	private JMenuItem NextStepMenuItem; 
	private JMenuItem LastStepMenuItem; 
	private ChessPanel cp1 = new ChessPanel();
	private ChessPos[][] chesslayout = new ChessPos[17][17];
	private PlayerState[] playerstate = new PlayerState[4];
	private Chessboard chessboard = new Chessboard();
	private int playerid;  // 0,1,2,3 each replays  red,blue,green,purple
	private int turn;     //当前处于第几个玩家的回合
	private int totalturn;  //回合数总和
	private int peacetick; //和棋标志
	private String[] gameload = new String[1200];
	private int gsloc;       //当前存档的位置
	private String[] gsspl;  //存档分割
	private boolean gserr; //存档是否有误
	private boolean addcontrol; //自动播放控制，暂停时为0
	private boolean rpend; // 结束标志，停止自动播放的标志
    private Timer timer = new Timer(); //自动播放定时器
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
	private int[] clickloc = {0,0,0,0}; //前两回合的点击位置
	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public GrFrame(String S) throws IOException {
		addWindowListener(new WindowAdapter() {
			@Override
			/*
			 * 点击红叉会出现对话框，防止用户误操作！
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			public void windowClosing(WindowEvent e) {
				try
				{
					if(JOptionPane.showConfirmDialog(GrFrame.this, "此操作将退出回放，是否继续？","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION)
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
		BufferedReader in =new BufferedReader(new FileReader(S));
		int j=0;
		while((gameload[j]=in.readLine())!=null){
			//System.out.println(gameload[j]);
			j++;
		}
		in.close();
		gsloc = 0;
		gserr = false;
		gsspl = gameload[gsloc].split(" ");
		gsloc = gsloc +1;
		//System.out.println(gsspl[0].equals("playerid"));
		if(gsspl.length==2&&gsspl[0].equals("playerid")){
			playerid = Integer.parseInt(gsspl[1]);
		}else{
			gserr = true;
		}
		BufferedImageGet();
		AudioClipGet();
		for(int i=0;i<4;i++){
			playerstate[i]=new PlayerState();
		}
		InitLayout();
		InitFrame();
		if(!gserr){
		gamestart();
		setVisible(true);
		}else{
	    JOptionPane.showMessageDialog(GrFrame.this,"读取存档有误！","存档出错",JOptionPane.ERROR_MESSAGE);
		}
	}
	//初始化窗口界面
	public void InitFrame() throws IOException{
		setBounds(0, 0, 726, 729);
		setTitle("军棋达人游戏回放");
		GrFrame.this.setResizable(false); 
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null); 	
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		setJMenuBar(menuBar); 
		
		JMenu GameMenu = new JMenu("播放控制");
		menuBar.add(GameMenu);
		
		StartReplayMenuItem = new JMenuItem("自动播放");
		StartReplayMenuItem.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					addcontrol = true;
					PauseReplayMenuItem.setEnabled(true);
					StartReplayMenuItem.setEnabled(false);
				}
		  }
		);
		GameMenu.add(StartReplayMenuItem);
		
		PauseReplayMenuItem = new JMenuItem("暂停播放");
		PauseReplayMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addcontrol = false;
				PauseReplayMenuItem.setEnabled(false);
				StartReplayMenuItem.setEnabled(true);
			}
		});
		PauseReplayMenuItem.setEnabled(false);
		GameMenu.add(PauseReplayMenuItem);

		NextStepMenuItem = new JMenuItem("下一步");
		NextStepMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addcontrol = false;
				PauseReplayMenuItem.setEnabled(false);
				StartReplayMenuItem.setEnabled(true);
				addstep();
			}
		});
		GameMenu.add(NextStepMenuItem);
		
		JMenu VisionMenu = new JMenu("明暗反转");
		menuBar.add(VisionMenu);
		
		JMenuItem Hide1MenuItem = new JMenuItem("改变橙色");
		Hide1MenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<17;i++){
					for(int j=0;j<17;j++){
						if(chesslayout[i][j].owner==0){
							chesslayout[i][j].visionstate = 1 - chesslayout[i][j].visionstate;
						}
					}
				}
				cp1.repaint();
			}
		});
		VisionMenu.add(Hide1MenuItem);
		
		JMenuItem Hide2MenuItem = new JMenuItem("改变蓝色");
		Hide2MenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<17;i++){
					for(int j=0;j<17;j++){
						if(chesslayout[i][j].owner==1){
							chesslayout[i][j].visionstate = 1 - chesslayout[i][j].visionstate;
						}
					}
				}
				cp1.repaint();
			}
		});
		VisionMenu.add(Hide2MenuItem);
		
		JMenuItem Hide3MenuItem = new JMenuItem("改变绿色");
		Hide3MenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<17;i++){
					for(int j=0;j<17;j++){
						if(chesslayout[i][j].owner==2){
							chesslayout[i][j].visionstate = 1 - chesslayout[i][j].visionstate;
						}
					}
				}
				cp1.repaint();
			}
		});
		VisionMenu.add(Hide3MenuItem);
		
		JMenuItem Hide4MenuItem = new JMenuItem("改变紫色");
		Hide4MenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<17;i++){
					for(int j=0;j<17;j++){
						if(chesslayout[i][j].owner==3){
							chesslayout[i][j].visionstate = 1 - chesslayout[i][j].visionstate;
						}
					}
				}
				cp1.repaint();
			}
		});
		VisionMenu.add(Hide4MenuItem);
		
		
		LastStepMenuItem = new JMenuItem("上一步");
		LastStepMenuItem.setEnabled(false);
		LastStepMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addcontrol = false;
				PauseReplayMenuItem.setEnabled(false);
				StartReplayMenuItem.setEnabled(true);
				minusstep();
			}
		});
		
		GameMenu.add(LastStepMenuItem);
		
		cp1.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				ClickControl(e.getX(),e.getY());
				cp1.repaint();
			}
		});
		setContentPane(cp1);
	}
	
	//处理点击位置
	
	public void ClickControl(int x,int y){
		int clocx=0,clocy=0;
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
		if(clocx>-1&&clocx<17&&clocy>-1&&clocy<17){
			  if(chesslayout[clocx][clocy].owner>-1){
				  chesslayout[clocx][clocy].visionstate = 1 - chesslayout[clocx][clocy].visionstate;
			  }
		}
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
				movesound.play();
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
					flagsound.play();
				}else{
					bombsound.play();
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
				eatsound.play();
			}else{
				flag = 0;
				if(chesslayout[x1][y1].chessid==3){
					ShowFlag(chesslayout[x1][y1].owner);
					flag =1;
				}
				if(flag == 1){
					flagsound.play();
				}else{
					killedsound.play();
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
	//初始化布局,读取gamesave文件中的布局
	public void InitLayout() throws IOException{
		for(int i=0;i<17;i++){
			for(int j=0;j<17;j++){
				chesslayout[i][j]=new ChessPos();
			}
		}
		//System.out.println(gameload[gsloc]);
		gsspl = gameload[gsloc].split(" "); gsloc++;
		if(gsspl[0].equals("InitLayout")){
		for(int i=0;i<17;i++){
			gsspl = gameload[gsloc].split(" ");
			gsloc++;
			if(gsspl.length == 17){
			for(int j=0;j<17;j++){
				chesslayout[i][j].chessid = Integer.parseInt(gsspl[j]);
				
				if(chesslayout[i][j].chessid>12||chesslayout[i][j].chessid<-1){
					gserr = true;
				}else{
					if(chesslayout[i][j].chessid == -1){
						chesslayout[i][j].owner = -1;
					}else if(i>10){
						chesslayout[i][j].owner = playerid; 
					}else if(i<6){
						chesslayout[i][j].owner = (playerid+2)%4; 
					}else if(j<6){
						chesslayout[i][j].owner = (playerid+3)%4; 
					}else {
						chesslayout[i][j].owner = (playerid+1)%4;
					}
				}
				//System.out.println(String.valueOf(chesslayout[i][j].chessid)+" "+String.valueOf(chesslayout[i][j].owner)+" "+String.valueOf(chesslayout[i][j].visionstate)+'\n');
			  }
			}else{
				gserr = true;
			}
	      }
		}else{
			gserr = true;
		}
		 chessboard.initial();
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
		Applet.newAudioClip(new File("./res/sound/start.wav").toURL());
		Applet.newAudioClip(new File("./res/sound/select.wav").toURL());
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
	//开始游戏
	public void gamestart(){
		for(int i=0;i<17;i++){
			for(int j=0;j<17;j++){
				sync2to1(i,j);
				//System.out.println(String.valueOf(chesslayout[i][j].chessid)+" "+String.valueOf(chesslayout[i][j].owner)+" "+String.valueOf(chesslayout[i][j].visionstate)+'\n');
			}
		}
		if(gameload[gsloc].equals("game begin")){
			gsloc++;
		}else{
			gserrclose();
		}
		peacetick = 0;
		rpend = false;
		addcontrol = false;
		turn = -1;
		totalturn = 0;
		timer.schedule(new TimerRunTask(),0);
		addturn();
		beginsound.play();
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
		totalturn =totalturn+1;
		if(totalturn>1){
			LastStepMenuItem.setEnabled(true);
		}
		cp1.repaint();
		//System.out.println("turn="+(char)('0'+turn));
		gameend();
	}
	//播放一步
	public void addstep(){
		
		gsspl = gameload[gsloc].split(" "); gsloc++;
		if(gsspl[0].equals("peace")){
			peacetick = 1;
			while(gsspl[0].equals("failure")){
				gsloc++;
				gsspl = gameload[gsloc].split(" ");
			}
			gameend();
		}else{
		if(gsspl[0].equals("step")&&Integer.parseInt(gsspl[1])==totalturn){
		if(gsspl.length==10){
			int tempx1 = Integer.parseInt(gsspl[2]);
			int tempy1 = Integer.parseInt(gsspl[3]);
			int tempx2 = Integer.parseInt(gsspl[6]);
			int tempy2 = Integer.parseInt(gsspl[7]);
		  if(PlayJudge(tempx1,tempy1,tempx2,tempy2)>0){
			  chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
			  chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
			  chesslayout[tempx1][tempy1].tipstate = 1;
			  chesslayout[tempx2][tempy2].tipstate = 1;
			  clickloc[0] = tempx1;
			  clickloc[1] = tempy1;
			  clickloc[2] = tempx2;
			  clickloc[3] = tempy2;
			  gsspl = gameload[gsloc].split(" ");
			  if(gsspl[0].equals("failure")){
				  gsloc++;
			  }
		      addturn();    
		  }else gserrclose();
	   	  }else if(gsspl.length == 4){
	   		  if(gsspl[2].equals("jump")&&Integer.parseInt(gsspl[3])==turn){
	   			  playerstate[turn].jumpcounts++;
	   		  }else if(gsspl[2].equals("sur")&&Integer.parseInt(gsspl[3])==turn){
	   			  playerstate[turn].surstate = 1;
	   		  }else{
	   			  gserrclose();
	   		  }
	   		  gsspl = gameload[gsloc].split(" ");
			  if(gsspl[0].equals("failure")){
				  gsloc++;
			  }
		      addturn(); 
		  }else gserrclose();
		}else gserrclose();
		}
	}
	//后退一步
	public void minusstep(){
		int id,i=0;
		gsloc --;
		gsspl = gameload[gsloc].split(" ");
		while(gsspl[0].equals("failure")){
			id = Integer.parseInt(gsspl[1]);
			playerstate[id].failurestate = 0;
			for(int j=0;j<(gsspl.length-2)/3;j++){
				chesslayout[Integer.parseInt(gsspl[3*j+2])][Integer.parseInt(gsspl[3*j+3])].chessid = Integer.parseInt(gsspl[3*j+4]);
				chesslayout[Integer.parseInt(gsspl[3*j+2])][Integer.parseInt(gsspl[3*j+3])].owner = id;
				chesslayout[Integer.parseInt(gsspl[3*j+2])][Integer.parseInt(gsspl[3*j+3])].visionstate = 1;
				sync2to1(Integer.parseInt(gsspl[3*j+2]),Integer.parseInt(gsspl[3*j+3]));
			}
			gsloc --;
			gsspl = gameload[gsloc].split(" ");
		}
		while(i<3){
			turn = turn-1;
			if(turn == -1 ){
				turn = 3;
			}
			if(playerstate[turn].failurestate==0){
				break;
			}
			i++;
		}
		totalturn --;
		if(totalturn == 1){
			LastStepMenuItem.setEnabled(false);
		}
		if(gsspl[0].equals("peace")){
			peacetick = 0;
			gsloc --;
			gsspl = gameload[gsloc].split(" ");
		}else if(gsspl[0].equals("step")){
			if(gsspl[2].equals("sur")){
			 playerstate[turn].surstate = 0;
			}else if(gsspl[2].equals("jump")){
			 playerstate[turn].jumpcounts --;	
			}else{
				int tempx1 = Integer.parseInt(gsspl[2]);
				int tempy1 = Integer.parseInt(gsspl[3]);
				int tempx2 = Integer.parseInt(gsspl[6]);
				int tempy2 = Integer.parseInt(gsspl[7]);
				chesslayout[tempx1][tempy1].chessid = Integer.parseInt(gsspl[4]);
				chesslayout[tempx1][tempy1].owner = Integer.parseInt(gsspl[5]);
				chesslayout[tempx2][tempy2].chessid = Integer.parseInt(gsspl[8]);
				chesslayout[tempx2][tempy2].owner = Integer.parseInt(gsspl[9]);
				sync2to1(tempx1,tempy1);
				sync2to1(tempx2,tempy2);
				chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
			    chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
				chesslayout[tempx1][tempy1].tipstate = 1;
				chesslayout[tempx2][tempy2].tipstate = 1;
				clickloc[0] = tempx1;
				clickloc[1] = tempy1;
				clickloc[2] = tempx2;
				clickloc[3] = tempy2;
			}
		}
		NextStepMenuItem.setEnabled(true);
		movesound.play();
		cp1.repaint();
	}
	//失败判定
	public void failurejudge(int id){
		boolean flag = true;
		if(chessboard.failture(id +1)==1){
		   flag =false;
		   JOptionPane.showMessageDialog(GrFrame.this,"玩家"+String.valueOf(id+1)+"军旗被擒，全军覆没！","游戏信息",JOptionPane.INFORMATION_MESSAGE);
		}else if(chessboard.failture(id +1)==2){
		   flag =false;
		   JOptionPane.showMessageDialog(GrFrame.this,"玩家"+String.valueOf(id+1)+"无棋可走，全军覆没！","游戏信息",JOptionPane.INFORMATION_MESSAGE);
		}
		if(playerstate[id].jumpcounts==5) {flag = false;
		   JOptionPane.showMessageDialog(GrFrame.this,"玩家"+String.valueOf(id+1)+"累计跳过次数达到5次，全军覆没！","游戏信息",JOptionPane.INFORMATION_MESSAGE);
		}
		if(playerstate[id].surstate==1) {flag = false;
		   JOptionPane.showMessageDialog(GrFrame.this,"玩家"+String.valueOf(id+1)+"投降，全军覆没！","游戏信息",JOptionPane.INFORMATION_MESSAGE);
		}
		if(!flag){
			playerstate[id].failurestate = 1;
			//System.out.println(id);
			//System.out.println("failure");
			for(int i=0;i<17;i++){
				for(int j=0;j<17;j++){
					if(chesslayout[i][j].owner==id){
						chesslayout[i][j].chessid = -1;
						chesslayout[i][j].owner = -1;
						sync2to1(i,j);
					}
				}
			}
			deadsound.play();
		}
	}
	//游戏结束
	public void gameend(){
		boolean flag= false;
		if((playerstate[playerid].failurestate==1)&&(playerstate[(playerid+2)%4].failurestate==1)){
			flag = true;
			JOptionPane.showMessageDialog(GrFrame.this,"您输了！请再接再厉","游戏失败",JOptionPane.INFORMATION_MESSAGE);
		}else if((playerstate[(playerid+1)%4].failurestate==1)&&(playerstate[(playerid+3)%4].failurestate==1))
		{   
			flag = true;
			JOptionPane.showMessageDialog(GrFrame.this,"您赢了！恭喜！","游戏获胜",JOptionPane.INFORMATION_MESSAGE);
		}else if(peacetick>0||totalturn>999)
		{   
			flag = true;
			JOptionPane.showMessageDialog(GrFrame.this,"所有玩家同意和棋，游戏平局！","游戏平局",JOptionPane.INFORMATION_MESSAGE);
		}
		if(flag){
	      rpend = true;
		  NextStepMenuItem.setEnabled(false);
		  LastStepMenuItem.setEnabled(false);
		  PauseReplayMenuItem.setEnabled(false);
		  timer.cancel();
		  cp1.repaint();
		}
	}
	//关闭游戏
	 public void close(){
	    	GrFrame.this.dispose();
	 }
	 //存档有误
	 public void gserrclose(){
		    JOptionPane.showMessageDialog(GrFrame.this,"读取存档有误！","存档出错",JOptionPane.ERROR_MESSAGE);
	    	GrFrame.this.dispose();
	 }
     //JPanel扩展类，用于绘制棋盘
	 class ChessPanel extends JPanel
	 {
		 public void paint(Graphics g){
			 g.drawImage(chessbg,0,0,null);
			 if(totalturn>=0){
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
	 
	 class PlayerState{
		 public int failurestate = 0;
		 public int readystate = 0;
		 public int jumpcounts = 0;
		 public int peacetick = 0;    // 0 表示默认状态,1表示同意求和,2表示发起求和,-1表示拒绝求和请求
		 public int surstate = 0;
	 }
	 //自动运行时间控制
	 class TimerRunTask extends TimerTask {  
		  
		    @Override  
		    public void run() {  
		        while(!rpend){
		        try {  
		            TimeUnit.MILLISECONDS.sleep(1500);  
		            if(addcontrol){
		            	addstep();
		            }
		        } catch (InterruptedException e) {  
		            e.printStackTrace();  
		        }  
		        }
		    }  
		  
		}  

}

