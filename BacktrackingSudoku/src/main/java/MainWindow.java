import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.*;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//odniesienia do pol siatki
	NumberField[][] gridElements = new NumberField[9][9];
	JPanel gameBoard;

	public MainWindow() {
		super("Backtracking Sudoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLocation(300,100); 
		setResizable(false); 
		this.setSize(500,500);
		
		gameBoard = new JPanel();
		this.add(gameBoard);
		gameBoard.setSize(400,400);
		gameBoard.setLayout(new GridLayout(9, 9, 0, 0));
		
		double fieldSize = 400.0 / 9.0;
		
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{	
				NumberField tmpField = new NumberField(fieldSize);
				tmpField.setFiledValue((int)((Math.random() * 9)));
				gameBoard.add(tmpField);
				gridElements[i][j] = tmpField;
			}
		}
		
		setVisible(true);
	}
	
//	@Override
//	public void paintComponents(Graphics g) {
//		super.paintComponents(g);
//		
//		Graphics2D g2d = (Graphics2D) g;
//		Dimension windowSize = this.getSize();
//		int frameSize = 20;
//		int lineThickness = 10;
//		
//		int linePosWidth = (int)(windowSize.width / 3);
//		int linePosHeight = (int)(windowSize.height / 3);
//		
//		g2d.setColor(Color.black);
//		//g2d.fillRect((int)(linePosWidth - lineThickness / 2), frameSize, 2 * lineThickness, windowSize.height - 2 * frameSize);
//		g2d.fillRect(0, 0, 200, 200);
//		//g2d.fillRect(frameThickness, 0, (int)(size-2*frameThickness), frameThickness);
//		//g2d.fillRect(frameThickness, 0, (int)(size-2*frameThickness), frameThickness);
//		
//	}

}
