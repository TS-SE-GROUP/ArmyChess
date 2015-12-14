package armychess;


public class Position {

	public int typepo = 0;// 位置种类，普通=0，营=1，大本营=2，中间9格=3；
	public int id;// 位置标号
	public int[] rail = { -1, -1, -1 };// 在哪条铁路线上，0代表不在铁路线上
	public int[] norail = { -1, -1, -1, -1, -1, -1, -1, -1 };// 非铁路连接位置id
	public int type;// 棋子种类,30=炸弹,31=军棋,32=工兵,...,40=司令，41=地雷,42=空
	public int party;// 势力，1=red,2=purple,3=blue,4=green，5=空；
	// public int chessline=0;//在哪条线上，一共18条火车线
	// public boolean chess=true;//1=youzi
	public int sign = 0;// 标记
	public boolean isdisplay = false;// 是否显示棋子种类,1=显示
	public boolean isappear = false;// 是否显示出棋子，1=显示

}
