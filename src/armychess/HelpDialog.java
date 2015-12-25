/*
 * 帮助信息的对话框。
 */

package armychess;

import javax.swing.JDialog;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class HelpDialog extends JDialog {
	/**
	 * Create the dialog.
	 */
	public HelpDialog(java.awt.Frame parent,String title, boolean modal) {
		super(parent, title, modal);
		
		setBounds(100, 100, 400, 300);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton RequestButton = new JButton("需求文档");
		RequestButton.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			    Desktop desk=Desktop.getDesktop();  
			    try  
			    {  
			        File file=new File(".\\doc\\需求分析.pdf");//创建一个java文件系统  
			        desk.open(file); //调用open（File f）方法打开文件   
			    }catch(Exception e)  
			    {  
			        System.out.println(e.toString());  
			    }  
			}
		});	
		panel.add(RequestButton);
		JButton IntroButton = new JButton("说明文档");
		IntroButton.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			    Desktop desk=Desktop.getDesktop();  
			    try  
			    {  
			        File file=new File(".\\doc\\说明书.pdf");//创建一个java文件系统  
			        desk.open(file); //调用open（File f）方法打开文件   
			    }catch(Exception e)  
			    {  
			        System.out.println(e.toString());  
			    }  
			}
		});	
		panel.add(IntroButton);
		JButton ConfirmButton = new JButton("回到游戏");
		ConfirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				HelpDialog.this.dispose();
			}
		});	
		panel.add(ConfirmButton);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JLabel HelpLabel = new JLabel("<html>&nbsp&nbsp&nbsp&nbsp四国军棋作为一种艺术化的游戏，凝练于<br>现实生活中最激烈的冲突形式——战争，又<br>超脱于现实生活，锤炼人的记忆、思维、判<br>断和心理，升华人的品格。<br>&nbsp&nbsp&nbsp&nbsp四人分别执红、紫、蓝、绿四色棋子，每<br>人配备二十五枚，军旗、司令、军长各一枚，<br>炸弹、师长、旅长、团长、营长各两枚，连长、<br>排长、工兵、地雷各三枚。棋子的大小顺序<br>依次为：司令、军长、师长、旅长、团长、营<br>长、连长、排长、工兵。司令阵亡，军旗亮开<br>。地雷不能移动，工兵可以消除地雷，炸弹与<br>地雷同去，其它棋子遇到地雷皆去。炸弹和敌<br>方任何棋子相遇则同归于尽，包括军旗。军旗<br>不能移动，如果被吃，全军覆没。  <br>&nbsp&nbsp&nbsp&nbsp棋盘中央为九宫，分别为四个星位、四个边<br>宫和中间的中宫，中宫也称为天元。行走路线<br>包括公路线和铁路线。显示较细的是公路线，<br>任何棋子在公路线上只能走一步。显示粗黑的<br>为铁路线，工兵在无阻挡状况下可在铁路线<br>上任意行走，其它棋子在铁路线上只能直走或经<br>过弧形线，不能转直角弯。棋子落点包括兵站、<br>行营、大本营。行营是个安全岛，进入以后，敌方<br>棋子不能吃行营中的棋子。己方五个行营分别<br>是左前营、右前营、中营、左底营和右底营，<br>军旗上方的底营又叫旗营。军旗必须放在大本<br>营中，进入任何大本营的棋子不能再移动。<br>&nbsp&nbsp&nbsp&nbsp 25枚棋子分别摆放在自己范围内的23个兵站<br>和两个大本营中，炸弹不能放在一线，地雷只<br>能放在最后两线，军棋只能放在大本营。行棋<br>的一方军棋被扛或无棋可走则其全军覆没。对局<br>中如有一方求和，各方均同意为和棋。",JLabel.CENTER);
		scrollPane.setViewportView(HelpLabel);

	}

}
