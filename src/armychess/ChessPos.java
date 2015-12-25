package armychess;

//棋盘位置类，记录每个棋子的类别，所有者，是否有提示框，是否翻面
public class ChessPos {
   public int chessid;  //棋子类别
   public int owner;    //所有者
   public int tipstate; //是否有提示框
   public int visionstate; //是否翻面
   public ChessPos(){
	   chessid=-1;
	   owner=-1;
	   tipstate = 0;
	   visionstate = 1;
   }
}
