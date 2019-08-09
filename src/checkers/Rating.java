package checkers;

import java.util.ArrayList;

public class Rating 
{
	public static final int POINT_WON = 10000; 
	/* Neu lay 10000 thi se co luc no nhan gia tri chang han -10000 => 6 ky tu. 
	 * No se tuong day la mot nuoc di ke tiep. 
	 * Nhu vay thi can them mot dieu kien nho nua la charAt(6*(length/6)) # '-' vi no khong bao gio vuot qua 100000 
	 */
	public static final int POINT_KING = 140;
	public static final int POINT_NORMAL = 100;
	public static final int POINT_MOVE = 4;
	public static final int POINT_PROMOTION = 16;		// Moi mot hang gia tri tang them 2 diem.
	public static final int POINT_UNOCCUPIED = 3;		// Moi o con trong o hang cuoi duoc 3 diem.
    public static final int POINT_LONERKING = 3;		// Moi quan vua "dung mot minh" co gia tri 3.
    public static final int POINT_LONERPAWN = 2;		// Moi quan tot "dung mot minh"
    public static final int POINT_HOLE = -3;			// Moi "cho trong" giua 3 hoac 4 quan co doi minh bi tru 3 diem.
    
	/* Moi quan tot nam o canh duoc 2 diem.
	 * Moi quan tot nam 1 trong 8 o trung tam duoc 10 diem.
	 * Moi quan tot nam o 3 hang tren cung duoc 15 diem.
	 * Moi quan tot nam o 2 hang duoi cung duoc 10 diem.
	 * Moi quan tot nam o tren duong cheo chinh duoc 10 diem.
	 * Moi quan tot nam tren cac duong cheo phu duoc 5 diem.
	 */
    
    static int pawnBoard[][] = {
    		{ 0, 22,  0, 17,  0, 17,  0, 27},
    		{22,  0, 20,  0, 15,  0, 25,  0},
    		{ 0, 20,  0, 20,  0, 25,  0, 17},
    		{ 2,  0, 15,  0, 15,  0,  0,  0},
    		{ 0,  0,  0, 25,  0, 15,  0,  2},
    		{ 2,  0, 10,  0, 10,  0,  5,  0},
    		{ 0, 20,  0, 10,  0, 15,  0, 17},
    		{22,  0, 12,  0, 12,  0, 17,  0}};
    
	/* Moi quan vua o canh duoc 2 diem.
	 * Moi quan vua nam 1 trong 8 o trung tam duoc 25 diem.
	 * Moi quan vua nam o 3 hang tren cung duoc 15 diem.
	 * Moi quan vua nam o 2 hang duoi cung duoc 10 diem.
	 * Moi quan vua nam o tren duong cheo chinh duoc 15 diem.
	 * Moi quan vua nam tren cac duong cheo phu duoc 5 diem.
	 */
    
    static int kingBoard[][] = {
    		{ 0, 27,  0, 22,  0, 22,  0, 37},
    		{27,  0, 25,  0, 20,  0, 35,  0},    		
    		{ 0, 25,  0, 25,  0, 35,  0, 22},
    		{ 2,  0, 30,  0, 45,  0,  0,  0},
    		{ 0,  0,  0, 45,  0, 30,  0,  2},
    		{ 2,  0, 15,  0,  5,  0,  5,  0},
    		{ 0, 25,  0, 10,  0, 15,  0, 17},
    		{27,  0, 12,  0, 12,  0, 17,  0}};
    
	public static int evaluateBoard(ArrayList<String> list, int depth)
	{
		/* Da sua: 3/11/2016
		 * Ham nay dung de tinh gia tri cua mot trang thai ban co.
		 */
		int counter = 0;
		counter += evaluateValueOfBoard();
		counter += evaluateMaterial();
		counter += evaluateMoveability(list);
		counter += evaluatePositional();
		counter += evaluateAnotherComponents();
		AlphaBetaCheckers.flipBoard();
		counter -= evaluateValueOfBoard();
		counter -= evaluateMaterial();
		counter -= evaluateMoveability(list);
		counter -= evaluatePositional();
		counter -= evaluateAnotherComponents();
		AlphaBetaCheckers.flipBoard();
		return -(counter + 30*depth);
	}
	
	public static int evaluateValueOfBoard()
	{
		/* Da sua: 3/11/2016
		 * Ham nay dung de tinh gia tri ban co.
		 */
		if(CheckersRules.isItWin())
			return POINT_WON;
		else
			return evaluateMaterial();
	}
	
	public static int evaluateMaterial()
	{
		/* Da sua: 3/11/2016
		 * Ham nay dung de tinh gia tri cac quan co tren ban co.
		 */
		int counter = 0;
		for(int i = 0; i < 64; i++)
		{
			int row = i/8, column = i%8;
			String piece = Game.checkersBoard[row][column];
			if("b".equals(piece))
				counter += POINT_NORMAL;
			else if("B".equals(piece))
				counter += POINT_KING;
		}
		return counter;
	}
	
	public static int evaluateMoveability(ArrayList<String> list) {
		int counter = 0;
		for(String move: list) {
			counter += POINT_MOVE*move.length()/6;
		}
		return counter;
	}
	
	public static int evaluatePositional() {
		int counter = 0;
		for(int i = 0; i < 64; i++) {
			int row = i/8, column = i%8;
			String piece = Game.checkersBoard[row][column];
			if("b".equals(piece))
				counter += pawnBoard[row][column];
			else if("B".equals(piece))
				counter += kingBoard[row][column];
		}
		return counter;
	}
	
	public static int evaluateAnotherComponents() {
		int counter = 0;
		for(int i = 0; i < 64; i++) {
			int row = i/8, column = i%8;
			String piece = Game.checkersBoard[row][column];
			if("b".equals(piece)) {
				counter += POINT_PROMOTION - 2*row;
				if(isLonerPiece(row, column))
					counter += POINT_LONERPAWN;
			} else if("B".equals(piece)) {
				if(isLonerPiece(row, column))
					counter += POINT_LONERKING;
			} else if(" ".equals(piece)) {
				if(row == 0 && (column % 2 ==1))
					counter += POINT_UNOCCUPIED;
				if(isHole(row, column))
					counter += POINT_HOLE;
			}
		}
		return counter;
	}
	
	public static boolean isLonerPiece(int row, int column) {
		try {
			if("".equals(Game.checkersBoard[row - 1][column - 1]) && "".equals(Game.checkersBoard[row - 1][column + 1]) &&
			   "".equals(Game.checkersBoard[row + 1][column - 1]) && "".equals(Game.checkersBoard[row + 1][column + 1]))
				return true;
		}catch(Exception e) {}
		return false;
	}
	
	public static boolean isHole(int row, int column) {
		try {
			int counter = 0;
			for(int i = -1; i <= 1; i += 2)
				for(int j = -1; j <= 1; j += 2)
					if("b".equals(Game.checkersBoard[row+i][column+j]) || "B".equals(Game.checkersBoard[row+i][column+j]))
						counter++;
			if(counter >= 3) return true;
		}catch(Exception e) {
			return false;
		}
		return false;
	}
}
