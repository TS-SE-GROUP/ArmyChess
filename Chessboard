package armychess;

import java.io.*;

/**
 * @author lijianfeng,wangruimin
 */
public class Chessboard {

	Position pos[] = new Position[129];// 129个位置
	int line[][] = new int[18][13];// 一共18条火车线，记录位置
	boolean point[][] = new boolean[18][13];// 记录是否为关键点,true为关键点
	int a = 0;// a代表我方势力为；
	boolean pollute[] = new boolean[129];// 标记关键点是否已经访问过
	// public int game;//0表示游戏开始前，1表示游戏中，2表示游戏结束
	// public boolean save;//1表示调入复盘

	// 初始化
	public void initial() {
		for (int i = 0; i < 18; i++) {
			for (int j = 0; j < 13; j++) {
				line[i][j] = -1;
				point[i][j] = false;
			}
			point[i][0] = true;
			point[i][4] = true;
			if (i == 8 || i == 10 || i == 12 || i == 14 || i == 16 || i == 17) {
				point[i][2] = true;
			}
			if (i == 16 || i == 17) {
				point[i][1] = true;
				point[i][3] = true;
			}
			if (i >= 0 && i < 4) {
				point[i][5] = true;
				point[i][9] = true;
			}
			if (i > 3 && i < 8) {
				point[i][5] = true;
				point[i][6] = true;
				point[i][7] = true;
				point[i][8] = true;
				point[i][12] = true;
			}
		}

		for (int i = 0; i < 129; i++) {
			pos[i] = new Position();
			pos[i].id = i;
			if (i % 30 >= 15 && i % 30 <= 19)
				pos[i].typepo = 1;
			else if (i % 30 == 26 || i % 30 == 28)
				pos[i].typepo = 2;
			else if (i > 119 && i < 129) {
				pos[i].typepo = 3;
			}
		}

		for (int i = 0; i < 120; i++) {
			int k = i % 30;
			switch (k) {
			case 0:
				pos[i].type = 38;
				break;
			case 1:
				pos[i].type = 32;
				break;
			case 2:
				pos[i].type = 35;
				break;
			case 3:
				pos[i].type = 36;
				break;
			case 4:
				pos[i].type = 36;
				break;
			case 5:
				pos[i].type = 30;
				break;
			case 6:
				pos[i].type = 32;
				break;
			case 7:
				pos[i].type = 40;
				break;
			case 8:
				pos[i].type = 39;
				break;
			case 9:
				pos[i].type = 30;
				break;
			case 10:
				pos[i].type = 32;
				break;
			case 11:
				pos[i].type = 34;
				break;
			case 12:
				pos[i].type = 37;
				break;
			case 13:
				pos[i].type = 33;
				break;
			case 14:
				pos[i].type = 33;
				break;
			case 15:
				pos[i].type = 42;
				break;
			case 16:
				pos[i].type = 42;
				break;
			case 17:
				pos[i].type = 42;
				break;
			case 18:
				pos[i].type = 42;
				break;
			case 19:
				pos[i].type = 42;
				break;
			case 20:
				pos[i].type = 41;
				break;
			case 21:
				pos[i].type = 38;
				break;
			case 22:
				pos[i].type = 37;
				break;
			case 23:
				pos[i].type = 41;
				break;
			case 24:
				pos[i].type = 34;
				break;
			case 25:
				pos[i].type = 41;
				break;
			case 26:
				pos[i].type = 31;
				break;
			case 27:
				pos[i].type = 34;
				break;
			case 28:
				pos[i].type = 35;
				break;
			case 29:
				pos[i].type = 33;
			}
			switch (i / 30) {
			case 0:
				pos[i].party = 1;
				break;
			case 1:
				pos[i].party = 2;
				break;
			case 2:
				pos[i].party = 3;
				break;
			case 3:
				pos[i].party = 4;
				break;
			}
			if (pos[i].type == 42)
				pos[i].party = 5;
		}
		for (int i = 120; i < 129; i++) {
			pos[i].type = 42;
			pos[i].party = 5;
		}
		int[] count = new int[18];
		for (int i = 0; i < 129; i++) {

			if (i == 0 || i == 5 || i == 8 || i == 12 || i == 20 || i == 94 || i == 97 || i == 101 || i == 104
					|| i == 114) {
				pos[i].rail[0] = 1;
				if (count[0] < 5)
					line[0][4 - count[0]] = i;
				else
					line[0][count[0]] = i;
				count[0]++;
			}
			if (i == 4 || i == 7 || i == 11 || i == 14 || i == 24 || i == 30 || i == 35 || i == 38 || i == 42
					|| i == 50) {
				pos[i].rail[0] = 2;
				if (count[1] < 5)
					line[1][4 - count[1]] = i;
				else
					line[1][count[1]] = i;
				count[1]++;
			}
			if (i == 34 || i == 37 || i == 41 || i == 44 || i == 54 || i == 60 || i == 65 || i == 68 || i == 72
					|| i == 80) {
				pos[i].rail[0] = 3;
				if (count[2] < 5)
					line[2][5 - count[2]] = i;
				else
					line[2][count[2]] = i;
				count[2]++;
			}
			if (i == 64 || i == 67 || i == 71 || i == 74 || i == 84 || i == 90 || i == 95 || i == 98 || i == 102
					|| i == 110) {
				pos[i].rail[0] = 4;
				if (count[3] < 5)
					line[3][5 - count[3]] = i;
				else
					line[3][count[3]] = i;
				count[3]++;
			}
			if (i == 0 || i == 5 || i == 8 || i == 12 || i == 20 || i == 64 || i == 67 || i == 71 || i == 74
					|| i == 84) {
				pos[i].rail[1] = 5;
				if (count[4] < 5)
					line[4][4 - count[4]] = i;
				else
					line[4][count[4] + 3] = i;
				count[4]++;
			}
			if (i == 120 || i == 123 || i == 126) {
				pos[i].rail[0] = 5;
				line[4][7 - count[5]] = i;
				count[5]++;
			}
			if (i == 4 || i == 7 || i == 11 || i == 14 || i == 24 || i == 60 || i == 65 || i == 68 || i == 72
					|| i == 80) {
				pos[i].rail[1] = 6;
				if (count[6] < 5)
					line[5][4 - count[6]] = i;
				else
					line[5][count[6] + 3] = i;
				count[6]++;
			}
			if (i == 122 || i == 125 || i == 128) {
				pos[i].rail[0] = 6;
				line[5][7 - count[7]] = i;
				count[7]++;
			}
			if (i == 34 || i == 37 || i == 41 || i == 44 || i == 54 || i == 90 || i == 95 || i == 98 || i == 102
					|| i == 110 || i == 120 || i == 122) {
				pos[i].rail[1] = 7;
				if (count[8] < 5)
					line[6][4 - count[8]] = i;
				else if (count[8] == 10)
					line[6][7] = i;
				else if (count[8] == 11)
					line[6][5] = i;
				else
					line[6][count[8] + 3] = i;
				count[8]++;
			}
			if (i == 121) {
				pos[i].rail[0] = 7;
				line[6][6] = i;
			}
			if (i == 30 || i == 35 || i == 38 || i == 42 || i == 50 || i == 94 || i == 97 || i == 101 || i == 104
					|| i == 114 || i == 126 || i == 128) {
				pos[i].rail[1] = 8;
				if (count[9] < 5)
					line[7][4 - count[9]] = i;
				else if (count[9] == 10)
					line[7][7] = i;
				else if (count[9] == 11)
					line[7][5] = i;
				else
					line[7][count[9] + 3] = i;
				count[9]++;
			}
			if (i == 127) {
				pos[i].rail[0] = 8;
				line[7][6] = i;
			}
			if (i > 0 && i < 4) {
				pos[i].rail[0] = 9;
				line[8][i] = i;
			}
			if (i == 0 || i == 4) {
				pos[i].rail[2] = 9;
				line[8][i] = i;
			}
			if (i > 20 && i < 24) {
				pos[i].rail[0] = 10;
				line[9][i - 20] = i;
			}
			if (i == 20 || i == 24) {
				pos[i].rail[2] = 10;
				line[9][i - 20] = i;
			}
			if (i > 30 && i < 34) {
				pos[i].rail[0] = 11;
				line[10][i - 30] = i;
			}
			if (i == 30 || i == 34) {
				pos[i].rail[2] = 11;
				line[10][i - 30] = i;
			}
			if (i > 50 && i < 54) {
				pos[i].rail[0] = 12;
				line[11][i - 50] = i;
			}
			if (i == 50 || i == 54) {
				pos[i].rail[2] = 12;
				line[11][i - 50] = i;
			}
			if (i > 60 && i < 64) {
				pos[i].rail[0] = 13;
				line[12][i - 60] = i;
			}
			if (i == 60 || i == 64) {
				pos[i].rail[2] = 13;
				line[12][i - 60] = i;
			}
			if (i > 80 && i < 84) {
				pos[i].rail[0] = 14;
				line[13][i - 80] = i;
			}
			if (i == 80 || i == 84) {
				pos[i].rail[2] = 14;
				line[13][i - 80] = i;
			}
			if (i > 90 && i < 94) {
				pos[i].rail[0] = 15;
				line[14][i - 90] = i;
			}
			if (i == 90 || i == 94) {
				pos[i].rail[2] = 15;
				line[14][i - 90] = i;
			}
			if (i > 110 && i < 114) {
				pos[i].rail[0] = 16;
				line[15][i - 110] = i;
			}
			if (i == 110 || i == 114) {
				pos[i].rail[2] = 16;
				line[15][i - 110] = i;
			}
			if (i == 32 || i == 92 || i == 123 || i == 125)
				pos[i].rail[1] = 17;
			if (i == 124)
				pos[i].rail[0] = 17;
			if (i == 62 || i == 2 || i == 121 || i == 124 || i == 127)
				pos[i].rail[1] = 18;
			if (i % 30 == 15) {
				pos[i].norail[0] = i - 15;
				pos[i].norail[1] = i - 14;
				pos[i].norail[2] = i - 13;
				pos[i].norail[3] = i - 10;
				pos[i].norail[4] = i - 9;
				pos[i].norail[5] = i - 7;
				pos[i].norail[6] = i - 6;
				pos[i].norail[7] = i + 2;
			}
			if (i % 30 == 16) {
				pos[i].norail[0] = i - 14;
				pos[i].norail[1] = i - 13;
				pos[i].norail[2] = i - 12;
				pos[i].norail[3] = i - 10;
				pos[i].norail[4] = i - 9;
				pos[i].norail[5] = i - 6;
				pos[i].norail[6] = i - 5;
				pos[i].norail[7] = i + 1;
			}
			if (i % 30 == 17) {
				pos[i].norail[0] = i - 11;
				pos[i].norail[1] = i - 8;
				pos[i].norail[2] = i - 7;
				pos[i].norail[3] = i - 4;
				pos[i].norail[4] = i - 2;
				pos[i].norail[5] = i - 1;
				pos[i].norail[6] = i + 1;
				pos[i].norail[7] = i + 2;
			}
			if (i % 30 == 18) {
				pos[i].norail[0] = i - 10;
				pos[i].norail[1] = i - 9;
				pos[i].norail[2] = i - 6;
				pos[i].norail[3] = i - 5;
				pos[i].norail[4] = i - 1;
				pos[i].norail[5] = i + 2;
				pos[i].norail[6] = i + 3;
				pos[i].norail[7] = i + 4;
			}
			if (i % 30 == 19) {
				pos[i].norail[0] = i - 9;
				pos[i].norail[1] = i - 8;
				pos[i].norail[2] = i - 6;
				pos[i].norail[3] = i - 5;
				pos[i].norail[4] = i - 2;
				pos[i].norail[5] = i + 3;
				pos[i].norail[6] = i + 4;
				pos[i].norail[7] = i + 5;
			}
			if (i % 30 == 6)
				pos[i].norail[0] = i - 4;
			if (i % 30 == 9)
				pos[i].norail[0] = i - 1;
			if (i % 30 == 10)
				pos[i].norail[0] = i + 1;
			if (i % 30 == 13)
				pos[i].norail[0] = i + 9;
			if (i % 30 > 24 && i % 30 < 30)
				pos[i].norail[0] = i - 5;
			if (i % 30 == 26) {
				pos[i].norail[1] = i - 1;
				pos[i].norail[2] = i + 1;
			}
			if (i % 30 == 28) {
				pos[i].norail[1] = i - 1;
				pos[i].norail[2] = i + 1;
			}
		}
		line[16][0] = 32;
		line[16][1] = 125;
		line[16][2] = 124;
		line[16][3] = 123;
		line[16][4] = 92;
		line[17][0] = 2;
		line[17][1] = 127;
		line[17][2] = 124;
		line[17][3] = 121;
		line[17][4] = 62;
	}

	// 保存棋盘
	public void saveboard(OutputStream os) {
		try {
			// OutputStream os = new FileOutputStream("fupan.junqi");
			for (int i = 0; i < 129; i++) {
				os.write(pos[i].type);// writes the bytes
			}
			// os.close();
		} catch (IOException e) {
			System.out.print("Exception");
		}
	}

	// 读取棋盘
	public void readboard(InputStream is) {
		try {
			// int size = is.available();
			// InputStream is = new FileInputStream("fupansave.junqi");
			for (int i = 0; i < 129; i++) {
				pos[i].type = is.read();
			}
			// is.close();
		} catch (IOException e) {
			System.out.print("Exception");
		}
	}

	// 保存复盘
	public void savereplay(OutputStream os, int attack, int defend) {
		try {
			// OutputStream os = new FileOutputStream("fupan.junqi");
			os.write(attack);
			os.write(defend);
			// os.close();
		} catch (IOException e) {
			System.out.print("Exception");
		}
	}

	// 读取复盘
	public void readreplay(InputStream is) {
		try {
			int attack, defend;
			// OutputStream os = new FileOutputStream("fupan.junqi");
			attack = is.read();
			defend = is.read();
			if (attack >= 0 && defend >= 0)
				fight(attack, defend);
			// os.close();
		} catch (IOException e) {
			System.out.print("Exception");
		}
		return;
	}

	// 保存布局
	public void savelayout(int party) {
		try {
			OutputStream os = new FileOutputStream("layout" + party + ".fupan");
			for (int i = 30 * (party - 1); i < 30 * party; i++) {
				os.write(pos[i].type);// writes the bytes
			}
			os.close();
		} catch (IOException e) {
			System.out.print("Exception");
		}
	}

	// 读取布局
	public void readlayout(int party) {
		try {
			InputStream is = new FileInputStream("layout" + party + ".fupan");
			// int size = is.available();

			for (int i = 30 * (party - 1); i < 30 * party; i++) {
				pos[i].type = is.read();
				pos[i].party = party;
			}
			is.close();
		} catch (IOException e) {
			System.out.print("Exception");
		}
	}

	// 布局交换两子
	public void layoutchange(int party, int a, int b) {
		boolean can = true;
		int A = a + 30 * (party - 1);
		int B = b + 30 * (party - 1);
		if (pos[A].type == 42 || pos[B].type == 42)
			can = false;
		if (pos[A].party != party || pos[B].party != party)
			can = false;
		int temp = pos[A].type;
		pos[A].type = pos[B].type;
		pos[B].type = temp;
		can = check(party);
		if (!can) {
			pos[B].type = pos[A].type;
			pos[A].type = temp;
		}
		return;
	}
	

	// 检查布局,返回true表示布局正常
	// 检查某方布局是否符合规则
	public boolean check(int party) {
		int[] count = new int[13];
		for (int i = 30 * (party - 1); i < 30 * party; i++) {
			if ((pos[i].typepo == 1 || pos[i].typepo == 3) && pos[i].type != 42) // 棋子不在营内和中间地方
				return false;
			if (i % 30 >= 0 && i % 30 < 5 && pos[i].type == 30) // 炸弹不能放在第一批
				return false;
			if (i % 30 >= 0 && i % 30 < 15 && pos[i].type == 41) // 地雷只放在后两排
				return false;
			if (i % 30 != 26 && i % 30 != 28 && pos[i].type == 31) // 军棋不在大本营
				return false;
			if (i < 120)
				count[pos[i].type - 30]++;
		}
		boolean rev = false;
		rev = ((count[0] == 2) & (count[1] == 1) & (count[2] == 3) & (count[3] == 3) & (count[4] == 3) & (count[5] == 2)
				& (count[6] == 2) & (count[7] == 2) & (count[8] == 2) & (count[9] == 1) & (count[10] == 1)
				& (count[11] == 3));
		return rev;
	}
	

	// 判断两个点是否在一条铁路线上，返回铁路线编号
	// 两个棋子是否在同一轨道上
	public int straight(int attack, int defend) {
		int size = -1;
		for (int k = 0; k < 3; k++)
			for (int j = 0; j < 3; j++)
				if (pos[attack].rail[k] != -1 && pos[attack].rail[k] == pos[defend].rail[j]) {
					size = pos[attack].rail[k] - 1;
					break;
				}
		return size;
	}

	// 两个棋子是否在同一轨道上且没有阻挡
	public boolean connect_straight(int attack, int defend) {
		int sizex = 0, sizey = 0;
		int size = straight(attack, defend);
		if (size != -1) {
			for (int k = 0; k < 13; k++) {
				if (line[size][k] == pos[attack].id)
					sizex = k;
				if (line[size][k] == pos[defend].id)
					sizey = k;
			}
			if (sizex > sizey) {
				int temp = sizex;
				sizex = sizey;
				sizey = temp;
			}
			if (sizex != sizey) {
				for (int k = sizex + 1; k < sizey; k++) {
					if (pos[line[size][k]].type != 42)
						return false;
				}
			} else
				return true;
		} else
			return false;
		return true;
	}
	

	// 判断铁路线上两位置之间是否导通，0表示不可达，1表示正常可达，2表示工兵可达
	// 两轨道棋子是否能到达，0=不能，1=普通能，2=工兵能
	public int jouson(int attack, int defend) {
		int result = 1, mark = 0;
		if (!connect_straight(attack, defend)) {
			result = 0;
			for (int k = 0; k < 3; k++) {
				int wire = pos[attack].rail[k] - 1;
				if (wire >= 0) {
					for (int j = 0; j < 13; j++) {
						if (point[wire][j] == true && pos[line[wire][j]].type == 42
								&& connect_straight(attack, line[wire][j]) && pollute[line[wire][j]] == false
								&& mark == 0) {
							// System.out.print( line[wire][j]);
							pollute[line[wire][j]] = true;
							mark = jouson(line[wire][j], defend);

						}
					}
				}
			}
		}
		if (result == 0 && (mark == 1 || mark == 2))
			result = 2;
		return result;
	}
	

	// 判断两位置之间是否导通，0表示不可达，1表示正常可达，2表示工兵可达
	// 两棋子是否能到达，0=不能，1=普通能，2=工兵能
	public int connect(int attack, int defend) {
		int result = 0;
		if (pos[attack].rail[0] == -1 || pos[defend].rail[0] == -1) {
			for (int k = 0; k < 8; k++) {
				if ((pos[attack].norail[k] == pos[defend].id) || (pos[defend].norail[k] == pos[attack].id)) {
					result = 1;
					break;
				}
			}
		} else
			result = jouson(attack, defend);
		return result;
	}
	

	// 游戏行棋,返回0时表示行棋不符合规则
	// 行棋
	public boolean fight(int attack, int defend) {
		int con = connect(attack, defend);
		if (pos[attack].type == 31 || pos[attack].type == 41 || pos[attack].type == 42 || con == 0)
			return false;
		if (pos[attack].type != 32 && con == 2)
			return false;
		if (pos[attack].id % 30 == 26 || pos[attack].id % 30 == 28)
			return false;// 动大本营内的棋子
		if (pos[defend].typepo == 1 && pos[defend].type != 42)
			return false; // 营内有棋子
		if (pos[defend].type == 42) {
			pos[defend].type = pos[attack].type;
			pos[attack].type = 42;
			pos[defend].party = pos[attack].party;
			pos[attack].party = 5;
			return true;
		} // 移动棋子
		if ((pos[attack].party - pos[defend].party) % 2 == 0)
			return false; // 不是对立势力
		if (pos[defend].type == pos[attack].type) {
			pos[defend].type = 42;
			pos[attack].type = 42;
			pos[attack].party = 5;
			pos[defend].party = 5;
			return true;
		} // 两相同棋子相撞
		if (pos[attack].type > pos[defend].type && pos[defend].type != 30) {
			pos[defend].type = pos[attack].type;
			pos[defend].party = pos[attack].party;
			pos[attack].type = 42;
			pos[attack].party = 5;
			return true;
		} // 吃棋
		if (pos[attack].type == 30 || pos[defend].type == 30) {
			pos[attack].type = 42;
			pos[defend].type = 42;
			pos[attack].party = 5;
			pos[defend].party = 5;
			return true;
		} // 任一方是炸弹
		if (pos[attack].type == 32 && pos[defend].type == 41) {
			pos[defend].type = pos[attack].type;
			pos[defend].party = pos[attack].party;
			pos[attack].type = 42;
			pos[attack].party = 5;
			return true;
		} // 工兵飞地雷
		if (pos[attack].type < pos[defend].type) {
			pos[attack].type = 42;
			pos[attack].party = 5;
			return true;
		} // 进攻方被吃
		return true;
	}
	

	// 判断某方是否死掉,0代表死掉
	// 判断某方是否失败
	public boolean failture(int party) {
		int mark1 = 0, mark2 = 0;
		for (int i = 0; i < 129 && mark1 == 0; i++) {
			if (pos[i].type == 31 && pos[i].party == party)
				mark1 = 1;
		}
		for (int i = 0; i < 129 && mark2 == 0; i++) {
			if (pos[i].party == party && pos[i].type != 41 && pos[i].type != 31)
				mark2 = 1;
		}
		if (mark1 == 0 || mark2 == 0)
			return false;
		else
			return true;
	}
	

	// 判断棋局是否结束 0表示未结束，1表示甲方胜利，2表示乙方胜利；
	// 判断棋局是否结束，1=1，3方获胜，2=2，4方获胜，0=未结束
	public int success() {
		boolean a[] = new boolean[4];
		for (int i = 0; i < 4; i++) {
			a[i] = failture(i + 1);
		}
		if (a[0] == false && a[2] == false)
			return 2;
		if (a[1] == false && a[3] == false)
			return 1;
		else
			return 0;
	}

}
