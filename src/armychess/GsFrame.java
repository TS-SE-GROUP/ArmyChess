/*
 * 服务器端游戏类
 */
package armychess;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

@SuppressWarnings("serial")
public class GsFrame extends JFrame {
	//界面相关变量
	private JPanel contentPane;
	private JLabel txtLabel;
	private JScrollPane scrollPane;
	//套接字相关变量
	CreateServerThread[] clientsocket = new CreateServerThread[4];
    private int count;
    private int[] socketstate = {0,0,0,0};
    //游戏相关变量
    private int gamestate = 0;
    private int gamemodei = 0; //游戏模式
    private int[] playerready = {0,0,0,0};
    private int[] playerpeace = {0,0,0,0};
    private String[] layoutstring = {"","","",""};
	/**
	 * Create the frame.
	 */
	public GsFrame(String gamemode) {
		addWindowListener(new WindowAdapter() {
			@Override
			/*
			 * 点击红叉会出现对话框，防止用户误操作！
			 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
			 */
			public void windowClosing(WindowEvent e) {
				try
				{
					if(JOptionPane.showConfirmDialog(null, "此操作将关闭该服务器并结束游戏，是否继续？","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION)
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
		if(gamemode.equals("四人——四暗")){
			gamemodei = 0;
		}else if(gamemode.equals("四人——双明")){
			gamemodei = 1;
		}else if(gamemode.equals("四人——全明")){
			gamemodei = 2;
		}
	   initFrame();
	   setVisible(true);
	   AddTxt("已创建军旗游戏，游戏模式为"+gamemode+"<br>");
	   try {
       new Thread(new GameServer()).start();
	} catch (IOException e1) {
		// TODO 自动生成的 catch 块
		e1.printStackTrace();
	}
	}
	//初始化服务器界面
	public void initFrame(){
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(0, 0, 450, 300);
		setLocationRelativeTo(null); 
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		txtLabel= new JLabel("<html>");
		scrollPane.setViewportView(txtLabel);
	}
	//打印信息
    public void AddTxt(String s){
    	String txt0 = txtLabel.getText();
    	txtLabel.setText(txt0+s);
    }
    /*
     * 发送字符串函数
     */
    public void sendstring(String S,int id){
    	for(int i=0;i<4;i++){
			if(i!=id&&socketstate[i]>0){
				clientsocket[i].out.println(S);
			}
		}
    }
    /*
     * 接收字符串处理
     */
    public void handlestring(String S,int id){
    	System.out.println("To be handled: "+S);
    	String[] respl = S.split(" ") ;
    	if(respl.length>0){
    		if(respl[0].equals("peace")){
    		 int msgplyid = Integer.parseInt(respl[1]);
			 int msgtype = Integer.parseInt(respl[2]);
			 if(msgtype == 2){
				 playerpeace[msgplyid] = 1;
				 sendstring(S,msgplyid);
			 }else if(msgtype == 1){
				 playerpeace[msgplyid] = 1;
				 int flag = 1 ,flag1 = 1,loc = 0;
				 for(int i=0;i<4;i++){
					 if(playerpeace[i]==0){
						 flag = -1;
						 break;
					 }else if(playerpeace[i] == -1){
						 flag1 = -1;
						 loc = i;
						 break;
					 }
				 }
				 if(flag == 1){
					 if(flag1==1)
					  sendstring("peace -1 3",-1);
					 else
				      sendstring("peace "+String.valueOf(loc)+" -2",loc);
					 for(int i=0;i<4;i++) playerpeace[i]=0;
				 }
			 }else if(msgtype == -1){
				 playerpeace[msgplyid] = -1;
				 if(playerpeace[0]!=0&&playerpeace[1]!=0&&playerpeace[2]!=0&&playerpeace[3]!=0){
					 sendstring("peace "+String.valueOf(msgplyid)+" -2",msgplyid);
					 for(int i=0;i<4;i++) playerpeace[i]=0;
				 }
			 }
    		}else{
    			int msgtype = Integer.parseInt(respl[0]);
    			if(msgtype == 0){
    				int msgplayerid = Integer.parseInt(respl[1]);
    				layoutstring[msgplayerid] = S;
    				playerready[msgplayerid] = 1;
    				if(playerready[0]>0&&playerready[1]>0&&playerready[2]>0&&playerready[3]>0){
    					gamestate = 1;
    					for(int i=0;i<4;i++){
    						sendstring(layoutstring[i],i);
    					}
    				}
    			}else if(msgtype == -1){
    				gamestate = 0;
    				for(int i=0;i<4;i++){
    					playerready[i]=0;
    				}
    			}else{
    				sendstring(S,id);
    			}
    		}
    	}
    }
    /*
     * 关闭服务器操作。
     */
    public void close(){
    	gamestate = -1;
    	for(int i=0;i<4;i++){
    		if(socketstate[i]>0)
    			clientsocket[i].out.println("--- See you, bye! ---");
    		   System.out.println("hehe!");
    	}
    	GsFrame.this.dispose();
    }
    //服务端口
    public class GameServer extends ServerSocket implements Runnable{
        
        private static final int SERVER_PORT = 10000;
      
        public GameServer() throws IOException{
        	super(SERVER_PORT);
         	AddTxt("建立服务器地址   "+InetAddress.getLocalHost().getHostAddress()+":10000"+"<br>");
        }
        
        public void run(){
            int i=0;
            count = 0;
            boolean flag = true;
            try{
                while (true){
                	if(!flag){
                		if(socketstate[0]==0&&socketstate[1]==0&&socketstate[2]==0&&socketstate[3]==0) break;
                		if(gamestate == -1) break;
                	}
                	if(gamestate>0&&flag){
                		flag = false;
                	}
                	if(count<4&&gamestate == 0){
                    Socket socket = accept();
                    for(i=0;i<4;i++){
                    	if(socketstate[i]==0){
                         clientsocket[i] = new CreateServerThread(socket,i);
                         new Thread(clientsocket[i]).start();
                         socketstate[i] = 1;
                         clientsocket[i].out.println("-1 "+String.valueOf(gamemodei)+" "+String.valueOf(i)+'\n');
                         AddTxt("From "+socket.getInetAddress()+":"+socket.getPort()+" player "+String.valueOf(i+1)+" entered<br>");
                         //sendstring(String.valueOf(i)+" entered",i);
                         break;
                    	}
                    }
                    count++;}
                	Thread.sleep(100);
                }
            }catch (IOException | InterruptedException e){
                
            }   
                AddTxt("server socket closed");
                try {
    				close();
    			} catch (IOException e) {
    				// TODO 自动生成的 catch 块
    				e.printStackTrace();
    			}
        }
     
        //--- CreateServerThread
    }
    //端口运行线程
    class CreateServerThread implements Runnable{
        
        public Socket client;
        public BufferedReader in;
        public PrintWriter out;
        public int id;
        public CreateServerThread(Socket s,int i) throws IOException{
            client = s;
            in = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF8"));
            out = new PrintWriter(client.getOutputStream(), true);
            id = i;
        }
        
        public void run(){
            try{
                String line = in.readLine();
                while (!line.equals("--- See you, bye! ---")){
                    handlestring(line,id);
                    System.out.println(line);
                    line = in.readLine();
                    if(gamestate == -1){
                    	break;
                    }
                }
                out.println("--- See you, bye! ---");
                AddTxt("Player "+String.valueOf(id+1)+" exited<br>");
                if(gamestate > 0)
                  sendstring("-2 "+String.valueOf(id)+" exited<br>",id);
                socketstate[id] = 0;
                client.close();
                in.close();
                out.close();
                count -- ;
            }catch (IOException e){
            }
        }
    }
    
}
    
