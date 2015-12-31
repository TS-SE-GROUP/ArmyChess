package armychess;
/*
 * AI 模块
 */
public class ChessAI {
    long duration = 0;
	public int[] chessAI(Chessboard board, int mode, int party ,int depth) {
		int out[] = new int[3];
		if (mode == 1) {
			int temp[] = nextstep1(board, party ,party ,1 ,depth);
			out[0] = temp[0];
			out[1] = temp[1];
			out[2] = temp[2];
		}else if (mode == 2){
			int temp[] = nextstep2(board, party ,party ,1 ,depth);
			out[0] = temp[0];
			out[1] = temp[1];
			out[2] = temp[2];
		}
		return out;
	}

	public int[] getvalue(Chessboard board) {
		int value[] = { 0, 0, 0, 0 ,0};// 0=Red剩余兵力,1=Blue剩余兵力,2=Green剩余兵力,3=Purple剩余兵力
		int num32[] = { 0, 0, 0, 0 ,0};
		int num30[] = { 0, 0, 0, 0 ,0};
		int num41[] = { 0, 0, 0, 0 ,0};// 记录剩余几个工兵、炸弹或地雷
		int flag[] = { 0, 0, 0, 0, 0 };// 记录有没有军棋
		for (int i = 0; i < 129; i++) {
			int j = board.pos[i].party - 1;
			if (board.pos[i].type == 31)
				flag[j] = 1;
			else if (board.pos[i].type == 40)
				value[j] = value[j] + 192;// 司令价值192
			else if (board.pos[i].type == 39)
				value[j] = value[j] + 128;// 军长价值128
			else if (board.pos[i].type == 38)
				value[j] = value[j] + 64;// 师长价值64
			else if (board.pos[i].type == 37)
				value[j] = value[j] + 32;// 旅长价值32
			else if (board.pos[i].type == 36)
				value[j] = value[j] + 16;// 团长价值16
			else if (board.pos[i].type == 35)
				value[j] = value[j] + 8;// 营长价值8
			else if (board.pos[i].type == 34)
				value[j] = value[j] + 2;// 连长价值2
			else if (board.pos[i].type == 33)
				value[j] = value[j] + 1;// 排长价值1
			else if (board.pos[i].type == 32) {
				if (num32[j] == 0) {
					value[j] = value[j] + 42;// 最后一个工兵价值42
					num32[j]++;
				} else if (num32[j] == 1) {
					value[j] = value[j] + 26;// 第二个工兵价值26
					num32[j]++;
				} else
					value[j] = value[j] + 18;// 第一个工兵价值18
			} else if (board.pos[i].type == 30) {
				if ( board.pos[i].id % 30 > 14 && board.pos[i].id % 30 < 20 && board.pos[i].id % 30 != 17)
					value[j] = value[j] + 1;//炸弹进营价值1
				if (num30[j] == 0) {
					value[j] = value[j] + 64;// 最后一个炸弹价值64
					num30[j]++;
				} else
					value[j] = value[j] + 42;// 第一个炸弹价值42
			} else if (board.pos[i].type == 41) {
				if (num41[j] == 0) {
					value[j] = value[j] + 18;// 最后一个地雷价值18
					num41[j]++;
				} else if (num41[j] == 1) {
					value[j] = value[j] + 18;// 第二个地雷价值18
					num41[j]++;
				} else
					value[j] = value[j] + 18;// 第一个地雷价值18
			}
		}
		for (int i = 0; i < 4; i++)
			if (flag[i] == 0)
				value[i] = 0;
		return value;
	}

	public int[] nextstep1(Chessboard board, int AIparty, int party, int round, int depth) {
		int out[] = new int[3];
		int value[] = new int[4];
		boolean flag = true;
		for (int i = 0; i < 129; i++) {
			if (board.pos[i].party == party) {
				for (int j = 0; j < 129; j++) {
					if (board.pos[i].type == 32 || (board.pos[i].type != 32
							&& (board.connect_straight(i, j) || board.isnorailconnect(i, j)))) {
						int attack_type = board.pos[i].type;
						int attack_party = board.pos[i].party;
						int defend_type = board.pos[j].type;
						int defend_party = board.pos[j].party;
						if (board.fight(i, j) != 0) {
							if (round == depth) {
								value = getvalue(board);
								int valuesum;
								if (AIparty == 1 || AIparty == 3)
									valuesum = value[0] - value[1] + value[2] - value[3];
								else
									valuesum = -value[0] + value[1] - value[2] + value[3];
								if (flag) {
									out[0] = i;
									out[1] = j;
									out[2] = valuesum;
									flag = false;
								}
								if ((party - AIparty) % 2 == 0 && valuesum > out[2]) {
									out[0] = i;
									out[1] = j;
									out[2] = valuesum;
								}
								if ((party - AIparty) % 2 != 0 && valuesum < out[2]) {
									out[0] = i;
									out[1] = j;
									out[2] = valuesum;
								}
							} else {
								int nextparty = party + 1;
								if (nextparty == 5)
									nextparty = 1;
								while (board.failture(nextparty) != 0) {
									nextparty++;
									if (nextparty == 5)
										nextparty = 1;
								}
								int temp[] = nextstep1(board, AIparty, nextparty, round + 1, depth);
								if (flag) {
									out[0] = i;
									out[1] = j;
									out[2] = temp[2];
									flag = false;
								}
								if ((party - AIparty) % 2 == 0 && temp[2] > out[2]) {
									out[0] = i;
									out[1] = j;
									out[2] = temp[2];
								}
								if ((party - AIparty) % 2 != 0 && temp[2] < out[2]) {
									out[0] = i;
									out[1] = j;
									out[2] = temp[2];
								}
							}
							board.pos[i].type = attack_type;
							board.pos[i].party = attack_party;
							board.pos[j].type = defend_type;
							board.pos[j].party = defend_party;
						}
					}
				}
			}
		}
		return out;
	}

	public int[] nextstep2(Chessboard board, int AIparty, int party, int round, int depth) {
		int out[] = new int[3];
		int value[] = new int[4];
		boolean flag = true;
		for (int i = 0; i < 129; i++)
			if (i < 30 || (i > 59 && i < 90) || i > 119) {
				if (board.pos[i].party == party)
					for (int j = 0; j < 129; j++) {
						if (j < 30 || (j > 59 && j < 90) || j > 119) {
							if ( board.pos[i].party != board.pos[j].party &&
									(board.pos[i].type >= board.pos[j].type || ( board.pos[j].type == 41 && 
									board.pos[i].type == 32) || board.pos[j].type == 42 || board.pos[i].type == 30) &&
									(board.pos[i].type == 32 || (board.pos[i].type != 32
									&& (board.connect_straight(i, j) || board.isnorailconnect(i, j))))) {
								int attack_type = board.pos[i].type;
								int attack_party = board.pos[i].party;
								int defend_type = board.pos[j].type;
								int defend_party = board.pos[j].party;
								if (board.solofight(i, j) != 0) {
									if (round == depth) {
										value = getvalue(board);
										int valuesum;
										if (AIparty == 1)
											valuesum = value[0] - value[2];
										else
											valuesum = -value[0] + value[2];
										if (flag) {
											out[0] = i;
											out[1] = j;
											out[2] = valuesum;
											flag = false;
										}
										if (party == AIparty && valuesum > out[2]) {
											out[0] = i;
											out[1] = j;
											out[2] = valuesum;
										}
										if (party != AIparty && valuesum < out[2]) {
											out[0] = i;
											out[1] = j;
											out[2] = valuesum;
										}
									} else {
										int nextparty = party + 2;
										if (nextparty >= 5)
											nextparty = 1;
										while (board.failture(nextparty) != 0) {
											nextparty++;
											if (nextparty >= 5)
												nextparty = 1;
										}
										int temp[] = nextstep2(board, AIparty, nextparty, round + 1, depth);
										if (flag) {
											out[0] = i;
											out[1] = j;
											out[2] = temp[2];
											flag = false;
										}
										if (party == AIparty && temp[2] > out[2]) {
											out[0] = i;
											out[1] = j;
											out[2] = temp[2];
										}
										if (party != AIparty && temp[2] < out[2]) {
											out[0] = i;
											out[1] = j;
											out[2] = temp[2];
										}
									}
									board.pos[i].type = attack_type;
									board.pos[i].party = attack_party;
									board.pos[j].type = defend_type;
									board.pos[j].party = defend_party;
								}
							}
						}
					}
			}
		return out;
	}
}
