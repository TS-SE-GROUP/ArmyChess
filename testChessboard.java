package armychess;

import java.io.*;

public class testChessboard {
 public static void main (String[] args) throws IOException {
    Chessboard test = new Chessboard() ;
    test.initial();
    InputStream is = new FileInputStream("fupan.junqi");
    test.readboard(is);
	for (int i = 0; i < 20; i++)
		test.readreplay(is);
	System.out.print(test.success());
    /*test.readlayout(1);
    test.readlayout(2);
    test.readlayout(3);
    test.readlayout(4);
    test.layoutchange(1, 0, 8);
    test.layoutchange(1, 4, 7);
    test.layoutchange(2, 5, 6);
    test.layoutchange(2, 20, 22);
    test.layoutchange(4, 7, 6);
    test.layoutchange(4, 26, 28);
    test.layoutchange(4, 22, 23);
    OutputStream os = new FileOutputStream("fupan.junqi");
    test.saveboard(os);
    if(test.fight(0, 94))
        test.savereplay(os, 0, 94);
    if(test.fight(94, 97))
    	test.savereplay(os, 94, 97); 
    if(test.fight(97, 101))
    	test.savereplay(os, 97, 101);
    if(test.fight(101, 104))
    	test.savereplay(os, 101, 104);
    if(test.fight(104, 114))
    	test.savereplay(os, 104, 114);
    if(test.fight(114, 113))
    	test.savereplay(os, 114, 113);
    if(test.fight(113, 118))
    	test.savereplay(os, 113, 118);
    if(test.fight(4, 30))
    	test.savereplay(os, 4, 30);
    if(test.fight(30, 35))
    	test.savereplay(os, 30, 35);
    if(test.fight(35, 38))
    	test.savereplay(os, 35, 38);
    if(test.fight(38, 42))
    	test.savereplay(os, 38, 42);
    if(test.fight(42, 50))
    	test.savereplay(os, 42, 50);
    if(test.fight(50, 51))
    	test.savereplay(os, 50, 51);
    if(test.fight(51, 56))
    	test.savereplay(os, 51, 56);
    System.out.print(test.pos[56].type);
    System.out.print(test.pos[118].type);
    System.out.print(test.success());
    os.close();*/
    
    //System.out.print(test.check(1));
    //test.savelayout(1);
//    test.savelayout(2);
//    test.savelayout(3);
//    test.savelayout(4);
    //System.out.print(test.pos[12].rail[0]);
    //System.out.print(test.pos[12].rail[1]);
   // System.out.print(test.pos[12].rail[2]);
//    System.out.print(test.line[16][0]);
//    System.out.print(test.line[16][1]);
//    System.out.print(test.line[16][2]);
//    System.out.print(test.line[16][3]);
//    System.out.print(test.line[16][4]);
//    System.out.print(test.line[16][5]);
//    System.out.print(test.line[16][6]);
//    System.out.print(test.line[16][7]);
//    System.out.print(test.line[16][8]);
//    test.pos[121].type = 38;
//    test.pos[124].type = 38;
//    test.pos[120].type = 32 ;
//    test.pos[127].type = 38;
//    test.pos[2].type = 42;
//    test.fight(120, 122);
//    System.out.print(test.pos[122].type);
    return ;
 }
}
