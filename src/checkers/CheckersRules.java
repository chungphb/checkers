package checkers;

import java.util.ArrayList;

public class CheckersRules 
{
	static boolean isIt3rdPosition = false;
	public static ArrayList<String> possibleMoves()
	{
		/* Da sua: 10/2016
		 * Ham nay dung de dua ra tat cac cac nuoc di (khong phai la nuoc an) hop le tren ban co.
		 */
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < 64; i++){
			switch(Game.checkersBoard[i/8][i%8]){
				case "b":
					for(int j = 0; j < possible_b(i).size(); j++)
						list.add(possible_b(i).get(j));
					break;
				case "B":
					for(int j = 0; j < possible_B(i).size(); j++)
						list.add(possible_B(i).get(j));
					break;
			}
		}
		return list; 
	}
		
	public static ArrayList<String> attackMoves()
	{
		/* Da sua: 10/2016
		 * Ham nay dung de dua ra tat cac cac nuoc an hop le tren ban co.
		 */
		ArrayList<String> attackList = new ArrayList<String>();
		for(int i = 0; i < 64; i++){
			int row = i/8, column = i%8;
			ArrayList<String> attackMoveOfEachPiece = isAttackMove(row, column);
			if(attackMoveOfEachPiece.size() != 0){
				for(int j = 0; j < attackMoveOfEachPiece.size(); j++)
				{													// Dua moi nuoc jump cua trang thai hien tai vao list
					String move = attackMoveOfEachPiece.get(j);
					ArrayList<String> eachMultiJump = multiJump(move);
					for(int k = 0; k < eachMultiJump.size(); k++)
						attackList.add(eachMultiJump.get(k));
				}
			}
		}
		return attackList;
	}
		
		
	
	public static ArrayList<String> multiJump(String root)
	{
		/* Da sua: 3/11/2016
		 * Ham nay tuong tu voi ham in cac duong di tren mot cay.
		 * No se tra ve tat ca cac nuoc di (chua nuoc di lien tiep) bat dau tu nuoc di root.
		 */
		
		ArrayList<String> stringList = new ArrayList<String>();		// Danh sach cac nuoc di tra ve.
		ArrayList<String> childStringList = new ArrayList<String>();// Danh sach cac nuoc di hop le ngay sau nuoc di vua thuc hien.
		int newRow = Character.getNumericValue(root.charAt(2));	
		int newColumn = Character.getNumericValue(root.charAt(3));
			
		AlphaBetaCheckers.makeMove(root); 							// Phai co makeMove, sau do undoMove thi moi ngan chan duoc viec lap vo han
		childStringList = isAttackMove(newRow, newColumn); 			// Danh sach cac nuoc di ngay sau nuoc di o root
		
		if(childStringList.size() == 0)
		{															// Neu khong co nuoc di nao ngay sau no
			stringList.add(root);
			AlphaBetaCheckers.undoMove(root);
			return stringList;
		}else
		{
			for(int i = 0; i < childStringList.size(); i++)
			{														// Doi voi moi nuoc di (tuong ung voi trang thai con cua root), ta tim tap tat ca nhung day nuoc di lien tiep ke tu no
				String move = childStringList.get(i);
				ArrayList<String> newChildStringList = multiJump(move);
				for(int j = 0; j < newChildStringList.size(); j++){
					stringList.add(root + newChildStringList.get(j));// Moi day nuoc di ke tu nuoc di root o day
				}	
			}
			AlphaBetaCheckers.undoMove(root);
		}	
		return stringList;
	}

	public static ArrayList<String> possible_b(int i)
	{
		/* Da sua: 10/2016
		 * Ham nay tra ve cac nuoc di hop le (khong tinh nuoc an) bat dau tu mot quan tot (b).
		 */
		ArrayList<String> list = new ArrayList<String>();
		String temp = "";
		int row = i/8;
		int column = i%8;
		for(int j = -1; j <= 1; j+= 2)
		{															// Moi vong lap ung voi mot truong hop: Tien len trai, tien len phai
			try{													// Dung try...catch vi co the ra ngoai ban co.	
				if(" ".equals(Game.checkersBoard[row-1][column+j])){ 
					if(i>=16)
					{
						/* Chu y: O day dung i > =16 vi trong truong hop quan B nam o hang thu 2 (i=8->15)
						 * thi khi sang hang 1 se tro thanh vua - TH khac. 
						 */
						temp = "" + row + column + (row-1) + (column+j) + "1 ";
						list.add(temp);
					}else{
						temp = "" + row + column + (row-1) + (column+j) + "3 ";
						list.add(temp);
					}
				}
			}catch(Exception e){}
		}
		return list;
	}
		
	public static ArrayList<String> possible_B(int i)
	{
		/* Da sua: 10/2016
		 * Ham nay tra ve cac nuoc di hop le (khong tinh nuoc an) bat dau tu mot quan tot (b).
		 */
		ArrayList<String> list = new ArrayList<String>();
		String temp = "";
		int row = i/8;
		int column = i%8;
		for(int k = -1; k <= 1; k += 2)	
			for(int j = -1; j <= 1; j+= 2)
				try{ 
					/* Moi vong lap ung voi mot truong hop:
					 * Tien len trai, tien len phai, lui trai, lui phai
					 */
					if(" ".equals(Game.checkersBoard[row+k][column+j])){ 
						temp = "" + row + column + (row+k) + (column+j) + "5 ";
						list.add(temp);
					}
				}catch(Exception e){}
		return list;
	}
		
	public static ArrayList<String> isAttackMove(int row, int column)
	{
		/* Da sua: 10/2016
		 * Ham nay tra ve cac nuoc an hop le bat dau tu mot quan o o thu (row, column).
		 */
		ArrayList<String> moveList = new ArrayList<String>();
		String move = "";
		String piece = Game.checkersBoard[row][column];
		
		if("b".equals(piece)){
			for(int j = -1; j <= 1; j+= 2){
				/* Moi vong lap ung voi mot truong hop: 
				 * Tien len trai 2 o (an), tien len phai 2 o (an).
				 */
				try{
					if(" ".equals(Game.checkersBoard[row-2][column+2*j])){
						if(row>2){
							/* 1. O day dung row > 2 vi trong truong hop quan B nam o hang thu 3 (row = 2)
							 *    thi khi sang hang 2 no tro thanh vua
							 * 2. Dung move + vi no co the co nhieu hon mot nuoc di.
							 */
							if("w".equals(Game.checkersBoard[row-1][column+j])){
									move = "" + row + column + (row-2) + (column+2*j) + "2w";
									moveList.add(move);
							}else if ("W".equals(Game.checkersBoard[row-1][column+j])){
									move = "" + row + column + (row-2) + (column+2*j) + "2W";
									moveList.add(move);
							}
						}else{
							if("w".equals(Game.checkersBoard[row-1][column+j])){
								move = "" + row + column + (row-2) + (column+2*j) + "4w";
								moveList.add(move);
							}else if ("W".equals(Game.checkersBoard[row-1][column+j])){
								move = "" + row + column + (row-2) + (column+2*j) + "4W";
								moveList.add(move);
							}
						}	
					}
				}catch(Exception e){}
			}
		}else if ("B".equals(piece)){
			for(int k = -1; k <= 1; k+= 2)
				for(int j = -1; j <= 1; j+= 2)
					try{
						/* Moi vong lap ung voi mot truong hop: 
						 * Tien len trai 2 o (an), tien len phai 2 o (an)
						 */
						if(" ".equals(Game.checkersBoard[row+2*k][column+2*j])){
							if("w".equals(Game.checkersBoard[row+k][column+j])){
								move = "" + row + column + (row+2*k) + (column+2*j) + "6w";
								moveList.add(move);
							}else if("W".equals(Game.checkersBoard[row+k][column+j])){
								move = "" + row + column + (row+2*k) + (column+2*j) + "6W";
								moveList.add(move);
							}
						}
					}catch(Exception e){}
		}
		return moveList;
	}
		
	public static boolean isItWin()
	{
		/* Da sua: 3/11/2016
		 * Ham nay tra ve mot gia tri true/false kiem tra xem da thang hay chua.
		 */

		if (noMoreMoves()||noMoreWhitePieces()){ 
			return true;
		}else 
			return false;	
	}
		
	public static boolean noMoreWhitePieces()
	{
		/* Da sua: 3/11/2016
		 * Ham nay tra ve mot gia tri true/false kiem tra xem doi thu da het quan hay chua.
		 */
		for(int i = 0; i < 64; i++)
		{												
			String piece = Game.checkersBoard[i/8][i%8];
			if("w".equals(piece) || "W".equals(piece)) 
				return false;
		}
		return true;			
	}
		
	public static boolean noMoreMoves()
	{
		/* Da sua: 3/11/2016
		 * Ham nay tra ve mot gia tri true/false kiem tra xem doi thu da het nuoc di hop le hay chua.
		 */
		AlphaBetaCheckers.flipBoard();
		if((possibleMoves().size() == 0) && (attackMoves().size() == 0)){
			AlphaBetaCheckers.flipBoard();
			return true;
		}else{ 
			AlphaBetaCheckers.flipBoard();
			return false;
		}
	}
	
	public static boolean isItDraw()
	{
		/* Da sua: 19/11/2016
		 * Ham nay tra ve mot gia tri true/false kiem tra xem da hoa hay chua.
		 */
		if(Game.countToDraw == 40||isIt3rdPosition)
			return true;
		else
			return false;
	}
	
	public static void countValidMovesToDraw(String move)
	{
		/* Da sua: 19/11/2016
		 * Ham nay tang gia tri so luong cac nuoc di ma khong lam tang loi the cho mot doi thu nao (cac nuoc di o trang thai 5).
		 * Hoac khoi tao lai bang countToDraw khi co mot nuoc di vi pham dieu kien nay.
		 */
		char status = move.charAt(4);
		if(status == '5'){
			Game.countToDraw++;
			addDrawList();
		}else{
			Game.countToDraw = 0;
			Game.drawPositionList.clear();
		}
	}

	public static void addDrawList() {
		/* Da sua: 20/11/2016
		 * Ham nay se them mot trang thai vao drawList hoac sua lai count trong trang thai ay.
		 */
		String currentBoard = Game.checkersBoardToString();
		for(int i = 0; i < Game.drawPositionList.size(); i++){
			String board = Game.drawPositionList.get(i);
			int len = board.length();
			int count = Character.getNumericValue(board.charAt(len-1));
			if(cut(board).equals(cut(currentBoard))){
				count++;
				if(count == 3){isIt3rdPosition = true; return;}
				
				String recountBoard = cut(board) + count;
				Game.drawPositionList.set(i, recountBoard);

				return;
			}
		}
		Game.drawPositionList.add(currentBoard);
	}
	
	public static String cut(String board)
	{
		/* Da sua: 20/11/2016
		 * Ham nay se them mot trang thai vao drawList hoac sua lai count trong trang thai ay.
		 */
		int len = board.length();
		return board.substring(0, len - 1);
		
	}
}
