/*
   ������Ϸ������ڡ�
 */

package armychess;

import java.awt.EventQueue;

public class ArmyChess {

	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
