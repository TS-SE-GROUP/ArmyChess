package armychess;


public class Position {

	public int typepo = 0;// λ�����࣬��ͨ=0��Ӫ=1����Ӫ=2���м�9��=3��
	public int id;// λ�ñ��
	public int[] rail = { -1, -1, -1 };// ��������·���ϣ�0��������·����
	public int[] norail = { -1, -1, -1, -1, -1, -1, -1, -1 };// ����·����λ��id
	public int type;// ��������,30=ը��,31=����,32=����,...,40=˾�41=����,42=��
	public int party;// ������1=red,2=blue,3=green,4=purple,5=�գ�
	// public int chessline=0;//���������ϣ�һ��18������
	// public boolean chess=true;//1=youzi
	public int sign = 0;// ���
	public boolean isdisplay = false;// �Ƿ���ʾ��������,1=��ʾ
	public boolean isappear = false;// �Ƿ���ʾ�����ӣ�1=��ʾ

}
