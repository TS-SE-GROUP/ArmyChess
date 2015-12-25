/*
 * �汾��Ϣ�ĶԻ���
 */

package armychess;

import javax.swing.JDialog;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class VersionDialog extends JDialog {

	/**
	 * Launch the application.
	 */
	/**
	 * Create the dialog.
	 */
	public VersionDialog(java.awt.Frame parent,String title, boolean modal) {
		super(parent, title, modal);
		setBounds(100, 100, 400, 200);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(4, 1, 5, 5));
		
		JLabel txtLabel1 = new JLabel("��Ϸ�汾��   1.0",JLabel.CENTER);
		panel.add(txtLabel1);
		
		JLabel txtLabel2 = new JLabel("��ͨ�˲�����   ����",JLabel.CENTER);
		panel.add(txtLabel2);
		
		JLabel txtLabel3 = new JLabel("��ϵ��ʽ : aclowpassfilter@gmail.com",JLabel.CENTER);
		panel.add(txtLabel3);
		
		JLabel txtLabel4 = new JLabel("����ά��   https://github.com/TS-SE-GROUP/ArmyChess",JLabel.CENTER);
		panel.add(txtLabel4);
		
		JPanel btnpanel = new JPanel();
		getContentPane().add(btnpanel, BorderLayout.SOUTH);
		
		JButton ConfirmButton = new JButton("ȷ��");
		btnpanel.add(ConfirmButton);
		
		JButton SourceButton = new JButton("����Դ��");
		btnpanel.add(SourceButton);
		
		ConfirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				VersionDialog.this.dispose();
			}
		});	
		
		SourceButton.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				java.net.URI uri = java.net.URI.create("https://github.com/TS-SE-GROUP/ArmyChess");
				java.awt.Desktop dp = java.awt.Desktop.getDesktop();
				try {
					dp.browse(uri);
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
			    }
			}
		});	

	}

}
