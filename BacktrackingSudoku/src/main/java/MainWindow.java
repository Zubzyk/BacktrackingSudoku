import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//odniesienia do pol siatki
	JLabel[][] gridElements = new JLabel[9][9];

	public MainWindow() {
		super("Backtracking Sudoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLocation(300,100); 
		setResizable(false); 
		setSize(400,400);

		setLayout(new GridLayout(9, 9, 10, 10));
		
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				JLabel tmpLabel = new JLabel(Integer.toString((int)((Math.random() * 8) + 1)));
				tmpLabel.setBackground(Color.WHITE);
				tmpLabel.setOpaque(true);
				tmpLabel.setHorizontalAlignment(SwingConstants.CENTER);
				this.add(tmpLabel);
				gridElements[i][j] = tmpLabel;
			}
		}
		
		setVisible(true);
	}

}
