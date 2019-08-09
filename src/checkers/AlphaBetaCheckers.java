package checkers;

import java.util.*;

public class AlphaBetaCheckers 
{
	public static String alphaBeta(int depth, int beta, int alpha, String move, int player)
	{
		/* Da sua: 3/11/2016
		 * Ham nay la ham thuc hien thuat toan Alpha - Beta cat tia.
		 */
		ArrayList<String> list = chooseList();	 // Chon possibleMoveList hoac attackMoveList
		
		if(depth == 0||list.size() == 0){
			return move + (Rating.evaluateBoard(list, depth)*(2*player - 1));
		}
		
		player = 1 - player;
//		list = sortMoves(list);
		for(int i = 0; i < list.size(); i++){
			String newMove = list.get(i);		// Chon mot nuoc di trong list cac nuoc di.
			String returnString; 				// Tra ve chuoi: Nuoc di + gia tri nuoc di cua cac node con.
			int len;                       		// Do dai cua returnString.
			int value;				       		// Gia tri cua node con.  
			
			makeRealMove(newMove);
			flipBoard(); 						// Doi de tinh nuoc di tiep theo cua doi thu.
			
			returnString = alphaBeta(depth-1, beta, alpha, newMove, player);
			len = returnString.length();
			
			if(returnString.charAt(6*(len/6 - 1)) != '-')
				value = Integer.valueOf(returnString.substring(6*(len/6)));
			else 
				value = Integer.valueOf(returnString.substring(6*(len/6 - 1)));
					/* 1. Dung substring vi returnstring khong chi tra ve mot nuoc di ma kem theo cac gia tri so.
					 *    Neu la 1 chuoi an lien tiep thi returnString se co chieu dai = 6*So_buoc + value -> value bat dau tu vi tri 6*(len/6)
					 * 2. Chu y la co truong hop value = -12345 chang han, khi do may tuong nham la chuoi lien tiep.
					 * 	  Nen dung them mot buoc kiem tra xem gia tri o dau mot chuoi 6 ky tu co phai la '-' hay khong?
					 *    Neu dung thi cat di/ nguoc lai thi lam binh thuong.
					 */
			flipBoard();
			undoRealMove(newMove);
			
			if(player == 0){ 
				if(value <= beta){ beta = value;  if(depth == Game.globalDepth) move = cutMove(returnString); }
			}else{
				if(value > alpha){ alpha = value; if(depth == Game.globalDepth) move = cutMove(returnString); }
			}
			
			if(alpha >= beta) 					// Day chinh la phan cat tia.
				if(player == 0) return move + beta; 
				else            return move + alpha;
		}
		
		if(player == 0) return move + beta;     // Day chinh la cac lenh sau khi duyet het tat ca cac nuoc con. 
		else{
			return move + alpha;
		}
	}	
	
	public static void flipBoard()
	{
		/* Da sua: 10/2016
		 * Ham nay dung de doi quan den thanh quan trang va nguoc lai.
		 * Khong can phai tao hai ham, thuan tien cho viec fix bug va kiem thu.
		 */
		String temp = "";
		for(int i = 0; i<32; i++)
		{
			int r = i/8, c = i%8;
			if("b".equals(Game.checkersBoard[r][c]))
				temp = "w";
			else if("w".equals(Game.checkersBoard[r][c]))
				temp = "b";
			else if("B".equals(Game.checkersBoard[r][c]))
				temp = "W";
			else if("W".equals(Game.checkersBoard[r][c]))
				temp = "B";
			else 
				temp = " ";
			
			if("b".equals(Game.checkersBoard[7-r][7-c]))
				Game.checkersBoard[r][c] = "w";
			else if("w".equals(Game.checkersBoard[7-r][7-c]))
				Game.checkersBoard[r][c] = "b";
			else if("B".equals(Game.checkersBoard[7-r][7-c]))
				Game.checkersBoard[r][c] = "W";
			else if("W".equals(Game.checkersBoard[7-r][7-c]))
				Game.checkersBoard[r][c] = "B";
			else 
				Game.checkersBoard[r][c] = " ";
			
			Game.checkersBoard[7-r][7-c] = temp;
		}
	}
	
	public static void makeMove(String move)
	{
		/* Da sua: 10/2016
		 * Ham nay dung de thay doi Game.checkersBoard sau di thuc hien mot nuoc di.
		 * Khong bao gom cac nuoc di lien tiep.
		 */
		
		int x1 = Character.getNumericValue(move.charAt(0));
		int y1 = Character.getNumericValue(move.charAt(1));
		int x2 = Character.getNumericValue(move.charAt(2));
		int y2 = Character.getNumericValue(move.charAt(3));
		if(move.charAt(5) == ' '){
			if(move.charAt(4) == '1'){
				Game.checkersBoard[x2][y2] = "b";
				Game.checkersBoard[x1][y1] = " ";
			}else{ 								// Truong hop 3 va 5 tuong tu nhau.
				Game.checkersBoard[x2][y2] = "B";
				Game.checkersBoard[x1][y1] = " ";
			}
		}else{									// Truong hop w/W deu nhu nhau.
			if(move.charAt(4) == '2'){
				Game.checkersBoard[x2][y2] = "b";
				Game.checkersBoard[(x1+x2)/2][(y1+y2)/2] = " ";
				Game.checkersBoard[x1][y1] = " ";
			}else{								// Truong hop 4 va 6 tuong tu nhau.
				Game.checkersBoard[x2][y2] = "B";
				Game.checkersBoard[(x1+x2)/2][(y1+y2)/2] = " ";
				Game.checkersBoard[x1][y1] = " ";
			}
		}
	}
	
	public static void undoMove(String move)
	{
		/* Da sua: 10/2016
		 * Ham nay dung de khoi phuc Game.checkersBoard ve trang thai truoc khi thuc hien mot nuoc di.
		 * Khong bao gom cac nuoc di lien tiep.
		 */
		int x1 = Character.getNumericValue(move.charAt(0));
		int y1 = Character.getNumericValue(move.charAt(1));
		int x2 = Character.getNumericValue(move.charAt(2));
		int y2 = Character.getNumericValue(move.charAt(3));
		if(move.charAt(5) == ' '){
			if(move.charAt(4) == '5'){
				Game.checkersBoard[x1][y1] = "B";
				Game.checkersBoard[x2][y2] = " ";
			}else{								// Truong hop 1 va 3 tuong tu nhau.
				Game.checkersBoard[x1][y1] = "b";
				Game.checkersBoard[x2][y2] = " ";
			}
		}else if(move.charAt(5) == 'w'){		// Truong hop w/W deu nhu nhau.
			if(move.charAt(4) == '6'){
				Game.checkersBoard[x1][y1] = "B";
				Game.checkersBoard[(x1+x2)/2][(y1+y2)/2] = "w";
				Game.checkersBoard[x2][y2] = " ";
			}else{								// Truong hop 2 va 4 tuong tu nhau.
				Game.checkersBoard[x1][y1] = "b";
				Game.checkersBoard[(x1+x2)/2][(y1+y2)/2] = "w";
				Game.checkersBoard[x2][y2] = " ";
			}
		}else{
			if(move.charAt(4) == '6'){
				Game.checkersBoard[x1][y1] = "B";
				Game.checkersBoard[(x1+x2)/2][(y1+y2)/2] = "W";
				Game.checkersBoard[x2][y2] = " ";
			}else{								// Truong hop 2 va 4 tuong tu nhau.
				Game.checkersBoard[x1][y1] = "b";
				Game.checkersBoard[(x1+x2)/2][(y1+y2)/2] = "W";
				Game.checkersBoard[x2][y2] = " ";
			}
		}
	}
	
	public static String cutMove(String computer)
	{
		/* Da sua: 10/2016
		 * Ham nay dung de cat nuoc di ra khoi mot chuoi bao gom: Nuoc di + gia tri nuoc di.
		 */
		int len = computer.length();
		if(computer.charAt(6*(len/6 - 1)) != '-')
			return computer.substring(0, 6*(len/6));
		else 
			return computer.substring(0, 6*(len/6 - 1));
	}
	
	public static void makeRealMove(String move)
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de thuc hien mot nuoc di.
		 * Bao gom cac nuoc di lien tiep.
		 */
		if(move.length() == 6)
			makeMove(move); 					// Thong tin moi nuoc di chiem 6 ky tu.
		else
			for(int i = 0; i < move.length(); i += 6) 
				makeMove(move.substring(i, i+6));
	}
	
	public static void undoRealMove(String newMove)
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de khoi phuc trang thai truoc khi thuc hien mot nuoc di.
		 * Bao gom cac nuoc di lien tiep.
		 */
		if(newMove.length() == 6)
			undoMove(newMove); 					
		else
			for(int j = newMove.length() - 1; j >= 0; j -= 6)
				undoMove(newMove.substring(j-5, j+1));
	}
	
	public static ArrayList<String> chooseList() 
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de chon danh sach can xet giua possibleMoveList va attackMoveList.
		 * Vi luat cua tro choi la neu co nuoc di thi phai di nuoc day.
		 */
		if(CheckersRules.attackMoves().size() == 0) 
			return CheckersRules.possibleMoves();
		else 
			return CheckersRules.attackMoves();
	}
	
	public static ArrayList<String> sortMoves(ArrayList<String> list) {
		String[] sortedlist = new String[list.size()];
		int[] score = new int[list.size()];
		for(int i = 0; i < list.size(); i++) {
			String move = list.get(i);
			makeRealMove(move);
			sortedlist[i] = move;
			score[i] = - Rating.evaluateBoard(new ArrayList<String>(), 0);
			undoRealMove(move);
		}
				
		for(int i = 0; i < Math.min(5, list.size()); i++) {
			int max = -100000;
			int maxLocation = 0;
			for(int j = i; j < list.size(); j++)
				if(score[j] > max) {
					max = score[j];
					maxLocation = j;
				}
			score[maxLocation] = score[i];
			String temp = sortedlist[i];
			sortedlist[i] = sortedlist[maxLocation];
			sortedlist[maxLocation] = temp;
		}
		return new ArrayList<String>(Arrays.asList(sortedlist));
	}
}

