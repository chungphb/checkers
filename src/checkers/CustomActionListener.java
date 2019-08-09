package checkers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class CustomActionListener implements ActionListener 
{
	@Override
	public void actionPerformed(ActionEvent e) {
		/* Da sua: 19/11/2016
		 * Ham nay dung de kiem tra cac event.
		 */
		if (e.getSource() == Game.btnRestart || e.getSource() == Game.restart) {
	    	Game.restart();
		}else if(e.getSource() == Game.btnExit || e.getSource() == Game.close){
			Object[] options = {"Exit", "Cancel"};
	    	int exit = JOptionPane.showOptionDialog(Game.application, "Do you want to exit?", "ABC Options", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
	    	if(exit == 0){
				System.exit(0);
			}
		}
	}
}
