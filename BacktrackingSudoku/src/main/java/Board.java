import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class Board extends JPanel {
	
	private static int frameThickness = 4;

	private double size, fieldSize;
	
	private NumberField[][] gridElements;
	
	Board(float s) {
		super();
		
		this.size = s;
		
		this.setSize((int)size, (int)size);
		this.setLayout(new GridLayout(9, 9, 0, 0));
		
		this.fieldSize = size / 9.0;
		int space = 10;
		
		gridElements = new NumberField[9][9];
		
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{	
				NumberField tmpField = new NumberField(i*fieldSize + space / 2, j*fieldSize + space / 2, fieldSize - space);
				tmpField.setFiledValue((int)((Math.random() * 9)));
				gridElements[i][j] = tmpField;
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//rysowanie pol
		for (NumberField[] lines : gridElements)
		{
			for(NumberField field : lines)
			{
				field.doDrawing(g);
			}
		}
		
		//rysowanie siatki
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.BLACK);
		
		double diff = (size / 3.0);
		
		for (int i = 1; i < 3; i++)
		{
			g2d.fillRect((int)(i * diff - (frameThickness / 2.0)), 0, frameThickness, (int)size);
		}
		
		for (int j = 1; j < 3; j++)
		{
			g2d.fillRect(0, (int)(j * diff - (frameThickness / 2.0)), (int)size, frameThickness);
		}
	}

}
