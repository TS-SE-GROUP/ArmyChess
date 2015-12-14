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

@SuppressWarnings("serial")
public class GsFrame extends JFrame {

	private JPanel contentPane;
	private JLabel txtLabel;
	private JScrollPane scrollPane;
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
	   initFrame();
	   setVisible(true);
	   AddTxt("已创建军旗游戏，游戏模式为"+gamemode+"<br>");
	}
	
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
	
    public void AddTxt(String s){
    	String txt0 = txtLabel.getText();
    	txtLabel.setText(txt0+s);
    }
    
    /*
     * 关闭服务器操作。
     */
    public void close(){
    	GsFrame.this.dispose();
    }
    
}
    
