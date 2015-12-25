/*
 * ������Ϸ������
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
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class GlFrame extends JFrame {
	//�������Ч����
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
	private JMenuItem LoadLayoutMenuItem;
	private JMenuItem SaveLayoutMenuItem;
	private JMenuItem AutosaveMenuItem;
	private JMenuItem SoundeffectMenuItem;
	private Chessboard chessboard = new Chessboard();
	private ChessPanel cp1 = new ChessPanel();
	private ChessPos[][] chesslayout = new ChessPos[17][17];
	//��Ϸ��ر���	
	private PlayerState[] playerstate = new PlayerState[4];
	private boolean peacestate = false; //�Ƿ�;�
	private int playerid;  // 0,1,2,3 each replays  red,blue,green,purple
	private int gamestate; //0: ���ֽ׶� 1���ֽ����ȴ��׶� 4���Խ׶�
	private int gamemode;  //0: ��, 1: ���� 2�� ��
	private int turn;     //��ǰ���ڵڼ�����ҵĻغ�
	private int totalturn;  //�غ����ܺ�
	private int clickstate = 0; //���״̬
	private int[] clickloc = {0,0,0,0}; //ǰ���غϵĵ��λ��
	private boolean autosave = true;
	private boolean soundeffect = true;
	//������Ʊ���
	private int[] locxs = {39,78,118,157,195,236,268,309,348,387,427,468,507,546,585,625,664}; //ÿһ�еĳ�ʼλ��
	private int[] locys = {14,53,90,129,169,208,247,280,325,359,402,441,478,519,559,598,637};  //ÿһ�еĳ�ʼλ�ã��м䣩
	private int[] locys2 = {14,53,90,129,169,208,241,280,319,359,398,441,478,519,559,598,637}; //ÿһ�еĳ�ʼλ�ã����ߣ�
	private int[] clickjudgex = {10,45,85,123,163,203,236,279,318,357,396,437,474,513,553,592,631,666}; //�жϵ��x��λ��
	private int[] clickjudgey = {35,72,112,150,190,229,262,305,344,383,423,466,500,538,578,617,657,694}; //�жϵ��y��λ��
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
            };//��17*17��λ��ת��Ϊ129λ���
	int[][] num1to2 ={{11,11,11,11,11,12,12,12,13,13,13,13,14,14,14,12,12,13,14,14,15,15,15,15,15,16,16,16,16,16,10,9,8,7,6,10,8,6,10,9,7,6,10,8,6,9,7,8,9,7,10,9,8,7,6,10,9,8,7,6,5,5,5,5,5,4,4,4,3,3,3,3,2,2,2,4,4,3,2,2,1,1,1,1,1,0,0,0,0,0,6,7,8,9,10,6,8,10,6,7,9,10,6,8,10,7,9,8,7,9,6,7,8,9,10,6,7,8,9,10,6,6,6,8,8,8,10,10,10,},
			{6,7,8,9,10,6,8,10,6,7,9,10,6,8,10,7,9,8,7,9,6,7,8,9,10,6,7,8,9,10,11,11,11,11,11,12,12,12,13,13,13,13,14,14,14,12,12,13,14,14,15,15,15,15,15,16,16,16,16,16,10,9,8,7,6,10,8,6,10,9,7,6,10,8,6,9,7,8,9,7,10,9,8,7,6,10,9,8,7,6,5,5,5,5,5,4,4,4,3,3,3,3,2,2,2,4,4,3,2,2,1,1,1,1,1,0,0,0,0,0,6,8,10,6,8,10,6,8,10}
            };//��17*17��λ��ת��Ϊ129λ���
	private int[] kind2to1 = {42,30,41,40,39,38,37,36,35,34,33,32,31}; 
	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public GlFrame() throws IOException {
		addWindowListener(new WindowAdapter() {
			@Override
			/*
			 * ���������ֶԻ��򣬷�ֹ�û��������
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			public void windowClosing(WindowEvent e) {
				try
				{
					if(JOptionPane.showConfirmDialog(GlFrame.this, "�˲������˳���Ϸ���Ƿ������","��ʾ",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION)
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
		
		playerid = 0;
		gamestate = -1;
		gamemode = 2;
		totalturn = 0;
		
		BufferedImageGet();
		AudioClipGet();
		for(int i=0;i<4;i++){
			playerstate[i]=new PlayerState();
		}
		
		LoadConfig();
		InitLayout();
		InitFrame();
		
		
		setVisible(true);
	}
	//��ʼ�����ڽ���
	public void InitFrame() throws IOException{
		setBounds(0, 0, 726, 729);
		setTitle("�����Ĺ�������Ϸ");
		GlFrame.this.setResizable(false); 
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null); 	
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
		setJMenuBar(menuBar); 
		
		JMenu GameMenu = new JMenu("��Ϸ");
		menuBar.add(GameMenu);
		
		GameReadyMenuItem = new JMenuItem("׼��");
		GameReadyMenuItem.addActionListener(new GameReady());
		GameMenu.add(GameReadyMenuItem);
		
		GameSkipMenuItem = new JMenuItem("����");
		GameSkipMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(turn == playerid){
					playerstate[playerid].jumpcounts++;
					clickstate = 0;
					 chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
					 chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
					addturn();
				}
			}
		});
		GameSkipMenuItem.setEnabled(false);
		GameMenu.add(GameSkipMenuItem);
		
		GameSurMenuItem = new JMenuItem("Ͷ��");
		GameSurMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(GlFrame.this, "�Ƿ�ȷ��Ͷ����","Ͷ��ȷ��",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION){
				if(turn == playerid){
					playerstate[playerid].surstate = 1;
					clickstate = 0;
					chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
					chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
					addturn();
				}
			  }
			}
		});
		GameSurMenuItem.setEnabled(false);
		GameMenu.add(GameSurMenuItem);
		
		JMenu LayoutMenu = new JMenu("����");
		menuBar.add(LayoutMenu);
		
		LoadLayoutMenuItem = new JMenuItem("���벼��");
		LoadLayoutMenuItem.addActionListener(new LoadLayout());
		LayoutMenu.add(LoadLayoutMenuItem);
		
		
		SaveLayoutMenuItem = new JMenuItem("���沼��");
		SaveLayoutMenuItem.addActionListener(new SaveLayout());
		LayoutMenu.add(SaveLayoutMenuItem);
		
		JMenu SetMenu = new JMenu("����");
		menuBar.add(SetMenu);
		
		AutosaveMenuItem = new JMenuItem("�Զ����棨�Ѵ򿪣�");
		if(!autosave){
			AutosaveMenuItem.setText("�Զ����棨�ѹرգ�");
		}
		AutosaveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
						
				if(autosave){
					autosave = false;
					AutosaveMenuItem.setText("�Զ����棨�ѹرգ�");
				}else{
					autosave = true;
					AutosaveMenuItem.setText("�Զ����棨�Ѵ򿪣�");
				}
				FileOutputStream configwrite;
				try {
					configwrite = new FileOutputStream("./save/config.ini");
					String config= "soundeffect = "+String.valueOf(soundeffect)+'\n'+"autosave = "+ String.valueOf(soundeffect);	
					configwrite.write(config.getBytes());
					configwrite.close();
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				} 
				
			}
		});
		SetMenu.add(AutosaveMenuItem);
		
		SoundeffectMenuItem = new JMenuItem("������Ч���Ѵ򿪣�");
		if(!soundeffect){
			SoundeffectMenuItem.setText("������Ч���ѹرգ�");
		}
		SoundeffectMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
						
				if(soundeffect){
					soundeffect = false;
					SoundeffectMenuItem.setText("������Ч���ѹرգ�");
				}else{
					soundeffect = true;
					SoundeffectMenuItem.setText("������Ч���Ѵ򿪣�");
				}
				FileOutputStream configwrite;
				try {
					configwrite = new FileOutputStream("./save/config.ini");
					String config= "soundeffect = "+String.valueOf(soundeffect)+'\n'+"autosave = "+ String.valueOf(soundeffect);	
					configwrite.write(config.getBytes());
					configwrite.close();
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				} 
				
			}
		});
		SetMenu.add(SoundeffectMenuItem);
		
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
	
	public void LoadConfig(){
		String tempstring;
		String[] splstring;
		try {
			BufferedReader in =new BufferedReader(new FileReader("./save/config.ini"));
			while((tempstring=in.readLine())!=null){
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	//������λ��
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
		if(clocx>-1&&clocx<17&&clocy>-1&&clocy<17){
			if(gamestate==0){            //����״̬
			 if(chesslayout[clocx][clocy].owner==playerid){
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
		   }else if(gamestate == 4){        //����ģʽ
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
						 addturn();
						 AIplay();
					 }else{
						 chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
					 }
					 clickstate = 0;
				 }
		   }else if(gamestate == 2){        //������Ϸģʽ
			   if(turn == playerid){        //ֻ�����Լ��غϲŻ��в���
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
						 addturn();
						 AIplay();
					 }else{
						 chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
					 }
					 clickstate = 0;
				}
			   }
		   }
		}
	}
	
	//�ж��ܷ񽻻�����λ�� xΪ�У�ȡֵΪ0��5,yΪ�У�ȡֵΪ0��4
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
	//����ʱ�ж�,x1��y1Ϊ��ʼλ��
	public int PlayJudge(int x1,int y1,int x2,int y2){
		int z1 = num2to1[x1][y1];
		int z2 = num2to1[x2][y2];
		int rec;
		int flag;
		if(z2<0||z1<0){
			return 0;
		}else{
			rec = chessboard.solofight(z1,z2);
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
	//˾������������
	public void ShowFlag(int id){
		int[] x={7,9,0,16,0,16,7,9};
		int[] y={0,0,7,7,9,9,16,16};
		for(int i=0;i<8;i++){
			if(chesslayout[x[i]][y[i]].chessid==12&&chesslayout[x[i]][y[i]].owner==id){
				chesslayout[x[i]][y[i]].visionstate = 1;
			}
		}
	}
	//��ʼ�����֣���ȡ"./save/default.als"
	public void InitLayout() throws IOException{
		for(int i=0;i<17;i++){
			for(int j=0;j<17;j++)
				chesslayout[i][j]=new ChessPos();
		}
		ReadLayoutFile("./save/layoutsave/default.als",playerid);
		chessboard.initial();
	}
	
	//��String S��Ӧ��·���ж�ȡ����
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
			JOptionPane.showMessageDialog(GlFrame.this,"����������ѡ����������!","��ȡ����",JOptionPane.ERROR_MESSAGE);
		}
	}
	//��ȡ����,aΪ6*5����bΪ�ڼ�����
	public void ReadLayout(int[][] a,int b){
		if(a.length!=6||a[0].length!=5){
			JOptionPane.showMessageDialog(GlFrame.this,"����������ѡ����������!","��ȡ����",JOptionPane.ERROR_MESSAGE);
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
	//���沼��
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
	//ͬ�����֣���17*17ͬ����129��ȥ
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
	//ͬ�����֣���129ͬ����17*17��ȥ
	/*
	public void sync1to2(int z){
		int x = num1to2[0][z];
		int y = num1to2[1][z];
		if((chesslayout[x][y].chessid=kind1to2[chessboard.pos[z].type-30])==-1){
			chesslayout[x][y].owner = -1;
		}else{
			chesslayout[x][y].owner = chessboard.pos[z].party - 1;
		}
    }*/
	//��ȡ������clip��
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
    //��ȡ���ӡ����̵�ͼƬ���ڴ���
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
	//ĳ����Ѿ�׼����
	public void gameprepared(int id){
		for(int i=0;i<17;i++){
			for(int j=0;j<17;j++){
				sync2to1(i,j);
			}
		}
      
	    	playerstate[id].readystate = 1;
			if(soundeffect)	startsound.play();
			if(playerstate[0].readystate>0)
				    gamestart();
	}
	//��ʼ��Ϸ
	public void gamestart(){
		try {
			ReadLayoutFile("./save/layoutsave/bj002.als",2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<17;i++){
			for(int j=0;j<17;j++){
				sync2to1(i,j);
			}
		}
		//gamestate = 4;
		gamestate = 2;
		turn = -2;
		totalturn = 0;
		addturn();
		if(soundeffect) beginsound.play();
	}
	//���ӻغ�
	public void addturn(){
		cp1.repaint();
		int i=0;
		for(i=0;i<4;i=i+2){
			if(playerstate[i].failurestate==0)
			   failurejudge(i);
		}
		i=0;
		while(i<3){
			turn = turn+2;
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
		GameSurMenuItem.setEnabled(false);
		}else{
		GameSkipMenuItem.setEnabled(true);
		GameSurMenuItem.setEnabled(true);
		}
		gameend();
	}
	//ʧ���ж�
	public void failurejudge(int id){
		boolean flag = true;
		if(chessboard.failture(id +1)==1){
		   flag =false;
		   JOptionPane.showMessageDialog(GlFrame.this,"���"+String.valueOf(id+1)+"���챻�ܣ�ȫ����û��","��Ϸ��Ϣ",JOptionPane.INFORMATION_MESSAGE);
		}else if(chessboard.failture(id +1)==2){
		   flag =false;
		   JOptionPane.showMessageDialog(GlFrame.this,"���"+String.valueOf(id+1)+"������ߣ�ȫ����û��","��Ϸ��Ϣ",JOptionPane.INFORMATION_MESSAGE);
		}
		boolean flag1 = (id==playerid);
		if(playerstate[id].jumpcounts==5) {flag = false;
		   JOptionPane.showMessageDialog(GlFrame.this,"���"+String.valueOf(id+1)+"�ۼ����������ﵽ5�Σ�ȫ����û��","��Ϸ��Ϣ",JOptionPane.INFORMATION_MESSAGE);
		}
		if(playerstate[id].surstate==1) {flag = false;
		   JOptionPane.showMessageDialog(GlFrame.this,"���"+String.valueOf(id+1)+"Ͷ����ȫ����û��","��Ϸ��Ϣ",JOptionPane.INFORMATION_MESSAGE);
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
					}if(flag1){
						if(chesslayout[i][j].owner==(playerid+2)%4){
							chesslayout[i][j].visionstate = 1;
						}
					}
				}
			}
			if(soundeffect) deadsound.play();
		}
	}
	//��Ϸ����
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
			// TODO �Զ����ɵ� catch ��
			e2.printStackTrace();
		}}else{
			allcounts = 0;
			wincounts = 0;
		}
		if(playerstate[0].failurestate==1){
			flag = true;
			JOptionPane.showMessageDialog(GlFrame.this,"�����ˣ����ٽ�����","��Ϸʧ��",JOptionPane.INFORMATION_MESSAGE);
		}else if(playerstate[2].failurestate==1)
		{   
			flag = true;
			wincounts ++;
			JOptionPane.showMessageDialog(GlFrame.this,"��Ӯ�ˣ���ϲ��","��Ϸ��ʤ",JOptionPane.INFORMATION_MESSAGE);
		//}else if(((playerstate[0].peacetick>0)&&(playerstate[1].peacetick>0)&&(playerstate[2].peacetick>0)&&(playerstate[3].peacetick>0))||totalturn>999)
		}else if(peacestate)
		{   
			flag = true;
			JOptionPane.showMessageDialog(GlFrame.this,"�������ͬ����壬��Ϸƽ�֣�","��Ϸƽ��",JOptionPane.INFORMATION_MESSAGE);
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
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} 
		
		try {
			InitLayout();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		GameReadyMenuItem.setEnabled(true);
		LoadLayoutMenuItem.setEnabled(true);
		SaveLayoutMenuItem.setEnabled(true);
		GameSkipMenuItem.setEnabled(false);
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
			playerstate[i].surstate = 0;
		}
		try {
			ReadLayoutFile("./save/layoutsave/default.als",playerid);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		cp1.repaint();
		}
	}
	//�ر���Ϸ
	 public void close(){
	    	GlFrame.this.dispose();
	 }
	//װ�ز��ֲ˵��¼���Ӧ
     class LoadLayout implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				ExtensionFileFilter filter = new ExtensionFileFilter();
				filter.addExtension("als");
				filter.setDescription("army chess layout save�ļ�(*.als)");
				JFileChooser chooser = new JFileChooser("./save/layoutsave");
				chooser.setAcceptAllFileFilterUsed(false); 
				chooser.addChoosableFileFilter(filter);
				int result = chooser.showDialog(GlFrame.this , "���벼��");
				if(result == JFileChooser.APPROVE_OPTION)
				{
					String LSname = chooser.getSelectedFile().getPath();
					//System.out.println(LSname);
					try {
						ReadLayoutFile(LSname,playerid);
						cp1.repaint();
					} catch (IOException e1) {
						// TODO �Զ����ɵ� catch ��
						e1.printStackTrace();
					}
				}
				
			}
		}
	//���沼�ֲ˵��¼���Ӧ
     class SaveLayout implements ActionListener{

 			@Override
 			public void actionPerformed(ActionEvent e) {
 				// TODO �Զ����ɵķ������
 				ExtensionFileFilter filter = new ExtensionFileFilter();
 				filter.addExtension("als");
 				filter.setDescription("army chess layout save�ļ�(*.als)");
 				JFileChooser chooser = new JFileChooser("./save/layoutsave");
 				chooser.setAcceptAllFileFilterUsed(false); 
 				chooser.addChoosableFileFilter(filter);
 				int result = chooser.showSaveDialog(GlFrame.this);
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
 						// TODO �Զ����ɵ� catch ��
 						e1.printStackTrace();
 					}
 				}
 				
 			}
 		}
    //׼����Ϸ�˵��¼���Ӧ
     class GameReady implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO �Զ����ɵķ������
				for(int i=0;i<17;i++){
					for(int j=0;j<17;j++){
						sync2to1(i,j);
					}
				}
			    playerstate[playerid].readystate = 1;
				if(soundeffect)	startsound.play();
				gamestate = 1; //׼���׶Σ��ȴ�������Ҳ���
				clickstate = 0;
				chesslayout[clickloc[0]][clickloc[1]].tipstate = 0;
				chesslayout[clickloc[2]][clickloc[3]].tipstate = 0;
				GameReadyMenuItem.setEnabled(false);
				LoadLayoutMenuItem.setEnabled(false);
				SaveLayoutMenuItem.setEnabled(false);
				try {
					SaveLayoutFile("./save/layoutsave/default.als");
				} catch (IOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
				if(playerstate[0].readystate>0)
				    gamestart();
			}
     }
		
     //JPanel��չ�࣬���ڻ�������
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
	 
	 class PlayerState{
		 public int failurestate = 0;
		 public int readystate = 0;
		 public int jumpcounts = 0;
		 public int surstate = 0;
	 }

	 public void AIplay(){
		 ChessAI AI = new ChessAI();
		 int result[] = AI.chessAI(chessboard , 2 , 3 , 3);
		 int x1 = num1to2[0][result[0]];
		 int y1 = num1to2[1][result[0]];
		 int x2 = num1to2[0][result[1]];
		 int y2 = num1to2[1][result[1]];
		 PlayJudge(x1,y1,x2,y2);
		 addturn();
	 }
}
