/*
 * ������Ϣ�ĶԻ���
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
		
		JButton RequestButton = new JButton("�����ĵ�");
		RequestButton.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			    Desktop desk=Desktop.getDesktop();  
			    try  
			    {  
			        File file=new File(".\\doc\\�������.pdf");//����һ��java�ļ�ϵͳ  
			        desk.open(file); //����open��File f���������ļ�   
			    }catch(Exception e)  
			    {  
			        System.out.println(e.toString());  
			    }  
			}
		});	
		panel.add(RequestButton);
		JButton IntroButton = new JButton("˵���ĵ�");
		IntroButton.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
			    Desktop desk=Desktop.getDesktop();  
			    try  
			    {  
			        File file=new File(".\\doc\\˵����.pdf");//����һ��java�ļ�ϵͳ  
			        desk.open(file); //����open��File f���������ļ�   
			    }catch(Exception e)  
			    {  
			        System.out.println(e.toString());  
			    }  
			}
		});	
		panel.add(IntroButton);
		JButton ConfirmButton = new JButton("�ص���Ϸ");
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
		
		JLabel HelpLabel = new JLabel("<html>&nbsp&nbsp&nbsp&nbsp�Ĺ�������Ϊһ������������Ϸ��������<br>��ʵ��������ҵĳ�ͻ��ʽ����ս������<br>��������ʵ��������˵ļ��䡢˼ά����<br>�Ϻ����������˵�Ʒ��<br>&nbsp&nbsp&nbsp&nbsp���˷ֱ�ִ�졢�ϡ���������ɫ���ӣ�ÿ<br>���䱸��ʮ��ö�����졢˾�������һö��<br>ը����ʦ�����ó����ų���Ӫ������ö��������<br>�ų������������׸���ö�����ӵĴ�С˳��<br>����Ϊ��˾�������ʦ�����ó����ų���Ӫ<br>�����������ų���������˾����������������<br>�����ײ����ƶ������������������ף�ը����<br>����ͬȥ�����������������׽�ȥ��ը���͵�<br>���κ�����������ͬ���ھ����������졣����<br>�����ƶ���������ԣ�ȫ����û��  <br>&nbsp&nbsp&nbsp&nbsp��������Ϊ�Ź����ֱ�Ϊ�ĸ���λ���ĸ���<br>�����м���й����й�Ҳ��Ϊ��Ԫ������·��<br>������·�ߺ���·�ߡ���ʾ��ϸ���ǹ�·�ߣ�<br>�κ������ڹ�·����ֻ����һ������ʾ�ֺڵ�<br>Ϊ��·�ߣ����������赲״���¿�����·��<br>���������ߣ�������������·����ֻ��ֱ�߻�<br>�������ߣ�����תֱ���䡣������������վ��<br>��Ӫ����Ӫ����Ӫ�Ǹ���ȫ���������Ժ󣬵з�<br>���Ӳ��ܳ���Ӫ�е����ӡ����������Ӫ�ֱ�<br>����ǰӪ����ǰӪ����Ӫ�����Ӫ���ҵ�Ӫ��<br>�����Ϸ��ĵ�Ӫ�ֽ���Ӫ�����������ڴ�<br>Ӫ�У������κδ�Ӫ�����Ӳ������ƶ���<br>&nbsp&nbsp&nbsp&nbsp 25ö���ӷֱ�ڷ����Լ���Χ�ڵ�23����վ<br>��������Ӫ�У�ը�����ܷ���һ�ߣ�����ֻ<br>�ܷ���������ߣ�����ֻ�ܷ��ڴ�Ӫ������<br>��һ�����屻���������������ȫ����û���Ծ�<br>������һ����ͣ�������ͬ��Ϊ���塣",JLabel.CENTER);
		scrollPane.setViewportView(HelpLabel);

	}

}
