package armychess;

//����λ���࣬��¼ÿ�����ӵ���������ߣ��Ƿ�����ʾ���Ƿ���
public class ChessPos {
   public int chessid;  //�������
   public int owner;    //������
   public int tipstate; //�Ƿ�����ʾ��
   public int visionstate; //�Ƿ���
   public ChessPos(){
	   chessid=-1;
	   owner=-1;
	   tipstate = 0;
	   visionstate = 1;
   }
}
