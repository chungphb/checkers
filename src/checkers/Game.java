package checkers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Game 
{
	static String checkersBoard[][] = {
			{" ", "w", " ", "w", " ", "w", " ", "w"},
			{"w", " ", "w", " ", "w", " ", "w", " "},
			{" ", "w", " ", "w", " ", "w", " ", "w"},
			{" ", " ", " ", " ", " ", " ", " ", " "},
			{" ", " ", " ", " ", " ", " ", " ", " "},
			{"b", " ", "b", " ", "b", " ", "b", " "},
			{" ", "b", " ", "b", " ", "b", " ", "b"},
			{"b", " ", "b", " ", "b", " ", "b", " "}};
		
	static int globalDepth = 4;							// Do sau ma thuat toan se cham den.
	static int whoPlayFirst = -1; 						// Nguoi choi truoc.
	static int countWin = 0;							// So luot thang cua nguoi choi.
	static int countLose = 0;							// So luot thua cua nguoi choi.
	static int countDraw = 0;							// So luot hoa cua nguoi choi.
	static int countToDraw = 0;							// Dem so nuoc di hop le lien tiep dan den mot trang thai hoa
	
	static ArrayList<String> drawPositionList = new ArrayList<String>();	
														// Day cac trang thai cua ban co (da di) ma chung co the duoc lap lai.
														
	static BufferedImage image = null;					// Anh nen cua JFrame.
	static JFrame application = null;					
	
	static JButton btnRestart = new JButton();	// Nut restart.
	static JButton btnExit = new JButton();		// Nut exit.
	
	static JMenuItem restart, instruction, close;
	
	static JTable resultTable;
	
	public static void main(String[] args) 
	{
		/* Da sua: 3/11/2016
		 * Ham nay dung la ham main.
		 */
		application = makeFrame();
		init(application);
	}
		
	public static JFrame makeFrame()
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de tao frame.
		 */
		JFrame application = new JFrame("Checkers");
		int squareSize = UserInterface.squareSize;
		try { image = ImageIO.read(new File("wooden.jpg")); } catch (IOException e) { e.printStackTrace(); }
		ImageIcon checkers = new ImageIcon("checkers.png");
		ImageIcon banner = new ImageIcon("banner.png");
		ImageIcon restartIcon = new ImageIcon("restart.png");
		ImageIcon insIcon = new ImageIcon("instruction.png");
		ImageIcon closeIcon = new ImageIcon("close.png");
		
		UserInterface ui = new UserInterface();
		ui.setPreferredSize(new Dimension(8*squareSize, 8*squareSize));
		
		makeButton(restartIcon, closeIcon, squareSize);
		
		application.add(makePanel(ui, checkers, banner));
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.setJMenuBar(makeMenu(restartIcon, insIcon, closeIcon));
		application.setSize(600, 500);
		application.setResizable(false);
		application.setLocationRelativeTo(null);
		application.setVisible(true); 
		return application;
	}
	
	public static JMenuBar makeMenu(ImageIcon icon1, ImageIcon icon2, ImageIcon icon3)
	{
		/* Da sua: 20/11/2016
		 * Ham nay dung de tao frame.
		 */
		JMenuBar menuBar = new JMenuBar();
		JMenu thisGame = new JMenu("Game");
		JMenu help = new JMenu("Help");
		int width = 12, height = 12;
		
		restart = new JMenuItem("Restart", makeIcon(icon1, width, height));
		restart.addActionListener(new CustomActionListener());
		
		instruction = new JMenuItem("How to play checkers?", makeIcon(icon2, width, height));
		instruction.addActionListener(new CustomActionListener());
		
		close = new JMenuItem("Close", makeIcon(icon3, width, height));
		close.addActionListener(new CustomActionListener());
		
		thisGame.add(restart);
		thisGame.add(instruction);
		thisGame.addSeparator();
		thisGame.add(close);
		
		menuBar.add(thisGame);
		menuBar.add(help);
		return menuBar;	
	}
	
	public static ImageIcon makeIcon(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		return newIcon;
	}
	
	
	public static JPanel makePanel(UserInterface ui, ImageIcon icon1, ImageIcon icon2) 
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de tao panel.
		 */
		JPanel myPanel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, 1360, 1024, null);
				this.setSize(1360, 1024);
			};
		};
		
		myPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		int squareSize = UserInterface.squareSize;
		
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.gridheight = 6;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(0, 0, 0, 0);
		myPanel.add(ui, gbc);
		
		gbc.fill = GridBagConstraints.BOTH;
		
		JLabel checkersLabel = makeLabel(icon1, 160, 40, 2*squareSize, squareSize);
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		gbc.insets = new Insets(10, 15, 0, 0);
		myPanel.add(checkersLabel, gbc);
		
		JLabel hiLabel = makeHiLabel(squareSize);
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.insets = new Insets(20, 15, 0, 0);
		myPanel.add(hiLabel, gbc);
		
		makeTable();
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.insets = new Insets(0, 15, 0, 0);
		myPanel.add(resultTable, gbc);
		
		JLabel bannerLabel = makeLabel(icon2, 80, 60, 60, 45);
		gbc.insets = new Insets(10, 55, 0, 0);
		myPanel.add(bannerLabel, gbc);
		
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.insets = new Insets(squareSize/2, 3*squareSize/4, 0, squareSize/2);
		myPanel.add(btnRestart, gbc);
		
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(squareSize, 3*squareSize/4, squareSize, squareSize/2);
		myPanel.add(btnExit, gbc);
		
		return myPanel;
	}
		
	public static void makeTable()
	{
		Object[] columnNames = {"Result", "No."};
		Object[][] data = {{"Win", countWin}, {"Lose", countLose}, {"Draw", countDraw}};
		resultTable = new JTable(data, columnNames);
		resultTable.setGridColor(new Color(150, 50, 30));
		resultTable.setFont(new Font("Cambria", Font.PLAIN, 15));
		resultTable.setEnabled(false);
		
		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
		    @Override
		    public boolean isCellEditable(int row, int scolumn) {
		       //all cells false
		       return false;
		    }
		};
		
		resultTable.setModel(tableModel);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		resultTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		resultTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
	}
	
	public static void makeButton(ImageIcon icon1, ImageIcon icon2, int squareSize)
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de tao cac button.
		 */
		int width = 24, height = 24;
		
		btnRestart.setIcon(makeIcon(icon1, width, height));
		btnRestart.addActionListener(new CustomActionListener());
		btnRestart.setMinimumSize(new Dimension(2*squareSize, squareSize));
		
		btnExit.setIcon(makeIcon(icon2, width, height));
		btnExit.addActionListener(new CustomActionListener());
		btnExit.setMinimumSize(new Dimension(2*squareSize, squareSize));
	}
	
	public static JLabel makeHiLabel(int squareSize) 
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de tao HiLabel.
		 */
		JLabel theLabel = new JLabel("RESULT", SwingConstants.CENTER);
		theLabel.setFont(new Font("Cambria", Font.BOLD, 18));
		theLabel.setOpaque(true);
		theLabel.setBackground(new Color(150, 50, 30));
		theLabel.setForeground(new Color(255, 200, 100));
		theLabel.setMinimumSize(new Dimension(2*squareSize, squareSize));
		return theLabel;
	}
	
	public static JLabel makeLabel(ImageIcon icon, int width, int height, int minWidth, int minHeight) 
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de tao HiLabel.
		 */
		JLabel newLabel = new JLabel();
		ImageIcon newBanner = makeIcon(icon, width, height);
		
		newLabel.setIcon(newBanner);
		newLabel.setOpaque(false);
		newLabel.setMinimumSize(new Dimension(minWidth, minHeight));
		return newLabel;
	}
	
	public static void init(JFrame application)
	{
		/* Da sua: 19/11/2016
		 * Ham khoi tao trang thai ban dau cua tro choi.
		 */
		Object[] option = {"Computer", "Human"};
		whoPlayFirst = JOptionPane.showOptionDialog(application, "Who should play first?", "ABC Options", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]);
		countToDraw = 0;
		drawPositionList.clear();
		CheckersRules.isIt3rdPosition = false;
		System.out.println(countToDraw);
		System.out.println(drawPositionList);
		if(whoPlayFirst == 0){
			AlphaBetaCheckers.flipBoard();
			
			String computer = AlphaBetaCheckers.alphaBeta(globalDepth, 100000, -100000, "", 0);
			String move = AlphaBetaCheckers.cutMove(computer);						
			AlphaBetaCheckers.makeRealMove(move);			
			System.out.println("Computer moved: " + move);

			AlphaBetaCheckers.flipBoard();
			application.repaint();
		}
	}

	public static void restart() 
	{
		/* Da sua: 19/11/2016
		 * Ham nay dung de restart lai trang thai tro choi.
		 */
		Object[] options = {"Restart", "Cancel"};
    	int restart = JOptionPane.showOptionDialog(Game.application, "Do you want to restart?", "ABC Options", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if(restart == 0){
			for(int i = 0; i < 64; i++){
				int j = 63 - i;
				if(i==0||i==2||i==4||i==6||i==9||i==11||i==13||i==15||i==16||i==18||i==20||i==22)
					Game.checkersBoard[i/8][i%8] = "w";
				else if(j==0||j==2||j==4||j==6||j==9||j==11||j==13||j==15||j==16||j==18||j==20||j==22)
					Game.checkersBoard[i/8][i%8] = "b";
				else 
					Game.checkersBoard[i/8][i%8] = " ";
			}
			
			resultTable.getModel().setValueAt(countWin, 0, 1);
			resultTable.getModel().setValueAt(countLose, 1, 1);
			resultTable.getModel().setValueAt(countDraw, 2, 1);
			Game.init(Game.application);
			Game.application.repaint();
		}
	}
	
	public static String checkersBoardToString() 
	{
		/* Da sua: 20/11/2016
		 * Ham nay dung de chuyen mot trang thai checkersBoard ve mot xau.
		 * Luu duoi dang: xV1yV2...cz trong do x la gia tri i tuong ung voi o [i/8, i%8], khong tinh o ' '.
		 * c la ky tu viet tat cua count. x la so lan lap lai trang thai nay.
		 */
		String pieceList = "";
		for(int i = 0; i < 64; i++){
			String piece = checkersBoard[i/8][i%8];
			if (!" ".equals(piece)) 
				pieceList += i + piece;
		}
		return pieceList + "c0";
	}
}
