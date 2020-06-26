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
	
	private Board gameBoard;

	public MainWindow() {
		super("Backtracking Sudoku");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLocation(300,100); 
		setResizable(false); 
		this.setSize(500,500);
		
		gameBoard = new Board(450);
		add(gameBoard);
	
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
