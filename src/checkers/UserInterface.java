package checkers;

import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener 
{
	static int mouseX, mouseY, newMouseX, newMouseY;
	static ArrayList<String> userPossibility = new ArrayList<String>();
	static int squareSize = 48;
	
	@Override 
	public void paintComponent(Graphics g) 
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de ghi de paintComponent cua JPanel.
		 */
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawCheckersBoard(g2d);
		
		this.setOpaque(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void drawCheckersBoard(Graphics2D g) 
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de ve ban co cua tro choi.
		 * Bao gom ban co chinh, cac quan co va cac quan co co the thuc hien mot nuoc di hop le.
		 */
		drawBoard(g);
		drawPieces(g);
		drawMoves(g);
	}
		

	public void drawBoard(Graphics2D g)
	{
		/* Da sua: 10/2016
		 * Ham nay dung de ve ban co 8x8.
		 */
		for(int i = 0; i < 64; i+=2)
		{
			g.setColor(new Color(255, 200, 100));
			g.fillRect((i%8 + (i/8)%2)*squareSize, (i/8)*squareSize, squareSize, squareSize); //In ra cac o den
			g.setColor(new Color(150, 50, 30));
			g.fillRect(((i+1)%8 - ((i+1)/8)%2)*squareSize, ((i+1)/8)*squareSize, squareSize, squareSize); //In ra cac o trang
		}
	}
	
	public void drawPieces(Graphics2D g) 
	{
		/* Da sua: 10/2016
		 * Ham nay dung de ve cac quan co.
		 */
		for(int i = 0; i < 64; i++)
		{
			int xSquare = (i%8)*squareSize + squareSize/4;
			int ySquare = (i/8)*squareSize + squareSize/4;
			switch(Game.checkersBoard[i/8][i%8]){
				case "b":
					g.setColor(Color.BLACK);
					g.fillOval(xSquare, ySquare, squareSize/2, squareSize/2);
					break;
				case "w":
					g.setColor(Color.WHITE);
					g.fillOval(xSquare, ySquare, squareSize/2, squareSize/2);
					break;
				case "B":
					g.setColor(Color.BLACK);
					g.fillOval(xSquare, ySquare, squareSize/2, squareSize/2);
					g.setColor(Color.YELLOW);
					g.fillOval(xSquare + squareSize/8, ySquare + squareSize/8, squareSize/4, squareSize/4);
					break;
				case "W":
					g.setColor(Color.WHITE);
					g.fillOval(xSquare, ySquare, squareSize/2, squareSize/2);
					g.setColor(Color.YELLOW);
					g.fillOval(xSquare + squareSize/8, ySquare + squareSize/8, squareSize/4, squareSize/4);
					break;
			}
		}
	}
	
	private void drawMoves(Graphics2D g) 
	{
		/* Da sua: 10/2016
		 * Ham nay dung de ve cac o vuong bao quanh cac quan co co the thuc hien mot nuoc di hop le o trang thai hien tai.
		 */
		ArrayList<String> list = AlphaBetaCheckers.chooseList();
		for(int i = 0; i < list.size(); i++)
		{
			String move = list.get(i);
			int fromX = Character.getNumericValue(move.charAt(0));
			int fromY = Character.getNumericValue(move.charAt(1));
			int xSquare = fromY*squareSize + squareSize/6;
			int ySquare = fromX*squareSize + squareSize/6;
			
			g.setColor(new Color(51, 255, 255));
			g.setStroke(new BasicStroke(3));
			g.drawRect(xSquare, ySquare, 2*squareSize/3, 2*squareSize/3);
		}
	}
	
	/* Cac phuong thuc can phai ghi de cua Interface MouseListener
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	
	@Override
	public void mouseMoved(MouseEvent e){

	}
	
	@Override
	public void mouseClicked(MouseEvent e){

	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e){
		
	}
	
	@Override
	public void mouseExited(MouseEvent e){
		
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		/* Da sua: 10/2016
		 * Ham nay dung de luu lai vi tri cua mot o (o trong ban co) khi nhan chuot vao o nay.
		 */
		if(e.getX() < 8*squareSize && e.getY() < 8*squareSize)
		{
			mouseX = e.getX();
			mouseY = e.getY();
			repaint();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		/* Da sua: 10/2016
		 * Ham nay dung de thuc hien mot nuoc di cua nguoi choi.
		 */
		if(e.getX() < 8*squareSize && e.getY() < 8*squareSize)
		{
			String dragMove = "7700  ";
			newMouseX = e.getX();
			newMouseY = e.getY();
			
			if(e.getButton() == MouseEvent.BUTTON1){		// BUTTON1 la chuot trai
				int toX = newMouseY/squareSize;
				int toY = newMouseX/squareSize;
				int fromX = mouseY/squareSize;
				int fromY = mouseX/squareSize;
				
				dragMove = getDragMove(fromX, fromY, toX, toY);
				userPossibility = userPossibleMoves(userPossibility, toX, toY);
				
				if(isBelongTo(dragMove, userPossibility)){	// Neu nuoc di la hop le thi cho phep di.
					AlphaBetaCheckers.makeMove(dragMove);
					
					if(CheckersRules.isItWin()){			// Neu nguoi choi thang.
						repaint();
						JOptionPane.showMessageDialog(Game.application, "Congratulations! You won.");
						Game.countWin++;
						Game.restart();
						return;
					}else if(CheckersRules.isItDraw()){		// Neu nguoi choi hoa.
						repaint();
						JOptionPane.showMessageDialog(Game.application, "We're tied.");
						Game.countDraw++;
						Game.restart();
						return;
					}
					
					if(((dragMove.charAt(5) == 'w')||(dragMove.charAt(5) == 'W')) && (CheckersRules.isAttackMove(toX, toY).size()!=0))
					{										// Neu nuoc moi di la mot nuoc an va dan den mot day cac nuoc di lien tiep
						mouseReleased(e);
						repaint();
					}else{
						CheckersRules.countValidMovesToDraw(dragMove);
						long startTime = System.currentTimeMillis();

						AlphaBetaCheckers.flipBoard();
						
						String computer = AlphaBetaCheckers.alphaBeta(Game.globalDepth, 100000, -100000, "", 0);
						String move = AlphaBetaCheckers.cutMove(computer);
						AlphaBetaCheckers.makeRealMove(move);
						
						System.out.println("Computer moved: " + move);
						System.out.println("count: " + Game.countToDraw);
						
						if(CheckersRules.isItWin()){
							AlphaBetaCheckers.flipBoard();
							repaint();
							JOptionPane.showMessageDialog(Game.application, "You lose. Try again!");
							Game.countLose++;
							Game.restart();
						}else if(CheckersRules.isItDraw()){
							AlphaBetaCheckers.flipBoard();
							repaint();
							JOptionPane.showMessageDialog(Game.application, "We're tied.");
							Game.countDraw++;
							Game.restart();
						}else{
							AlphaBetaCheckers.flipBoard();
							CheckersRules.countValidMovesToDraw(move);	// Phai sau khi flipBoard vi no se luon ghi trang thai theo chieu cua trang thai ban dau.
							repaint();
						}
					}
				}
			}
		}
	}
	
	private String getDragMove(int fromX, int fromY, int toX, int toY) 
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de luu lai mot nuoc di cua nguoi choi.
		 */
		String fromPiece = Game.checkersBoard[fromX][fromY]; 
		String oPiece = Game.checkersBoard[(fromX+toX)/2][(fromY + toY)/2]; 
		String dragMove = "";
		
		if("b".equals(fromPiece)){
			if(fromX - toX == 1){
				if(fromX == 1)
					dragMove = "" + fromX + fromY + toX + toY + "3 ";
				else 
					dragMove = "" + fromX + fromY + toX + toY + "1 ";
			}else if(fromX - toX == 2){
				if(fromX == 2){
					if("w".equals(oPiece))
						dragMove = "" + fromX + fromY + toX + toY + "4w";
					else if("W".equals(oPiece))
						dragMove = "" + fromX + fromY + toX + toY + "4W";
				}else{ 
					if("w".equals(oPiece)){
						dragMove = "" + fromX + fromY + toX + toY + "2w";
					}
					else if("W".equals(oPiece))
						dragMove = "" + fromX + fromY + toX + toY + "2W";
				}
			}
		}else{
			if(fromX - toX == 1||fromX - toX == -1){
				dragMove = "" + fromX + fromY + toX + toY + "5 ";
			}else{
				if("w".equals(oPiece))
					dragMove = "" + fromX + fromY + toX + toY + "6w";
				else if("W".equals(oPiece))
					dragMove = "" + fromX + fromY + toX + toY + "6W";
			}
		}
		
		return dragMove;
	}

	public ArrayList<String> userPossibleMoves(ArrayList<String> userPossibility, int toX, int toY) 
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de tra ve cac nuoc di ma nguoi choi co the di duoc.
		 * Khong chua cac nuoc di lien tiep.
		 */
		if(userPossibility.size() == 0)
		{													// Neu nhu userPossibility ban dau khong co gi thi no se khoi dong mot luot di moi
			if(CheckersRules.attackMoves().size() == 0)
				return CheckersRules.possibleMoves();
			else{
				ArrayList<String> moves = CheckersRules.attackMoves();
				for(int i = 0; i < moves.size(); i++){
					String move = moves.get(i);
					userPossibility.add(move.substring(0, 6));
				}
				return userPossibility;
			}
		}else{												// Nguoc lai, day chinh la mot chuoi cac nuoc di lien tiep.
			return CheckersRules.isAttackMove(toX, toY);
		}
	}
	
	public boolean isBelongTo(String move, ArrayList<String> moves)
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung kiem tra xem nuoc di cua nguoi choi co la nuoc di hop le hay khong.
		 */
		for(int i = 0; i < moves.size(); i++)
			if(move.equals(moves.get(i)))
				return true;
		return false;
	}
}

