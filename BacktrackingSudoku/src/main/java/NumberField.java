import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;

import javax.swing.JPanel;

public class NumberField {
	
	static final long serialVersionUID = 1L;
	
	private double posX, posY, size;
	private boolean drawColor;
	private boolean isSearched;
	private boolean isHighlighted;
	private boolean isMarked;
	private static int frameThickness = 3;
	private int fieldValue = 0;
	private HashSet<Integer> notedNumbers = new HashSet<Integer>(); 

	NumberField()
	{
		super();
		
		posX = 0.0;
		posY = 0.0;
		size = 0.0;
	}
	
	NumberField(double size)
	{
		super();
		
		this.posX = 0.0;
		this.posY = 0.0;
		this.size = size;
	}
	
	NumberField(double posX, double posY, double size)
	{
		super();
		
		this.posX = posX;
		this.posY = posY;
		this.size = size;
	}
	
	void doDrawing(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		
		//rysuj t³o
		if (isHighlighted)
		{
			g2d.setColor(Color.YELLOW);
			g2d.fillRect((int)(posX+frameThickness), (int)(posY+frameThickness), (int)(size-2*frameThickness), (int)(size-2*frameThickness));
		}
		
		//rysuj obramowanie
		if (drawColor)
		{
			if (isSearched) g2d.setColor(Color.green); else g2d.setColor(Color.red);
		}
		else
		{
			if (isMarked) g2d.setColor(Color.BLUE); else g2d.setColor(Color.black);
		}
		
		g2d.fillRect((int)(posX+frameThickness), (int)posY, (int)(size-2*frameThickness), frameThickness);
		g2d.fillRect((int)(posX+size-frameThickness), (int)(posY+frameThickness), frameThickness, (int)(size-2*frameThickness));
		g2d.fillRect((int)(posX+frameThickness), (int)(posY+size-frameThickness), (int)(size-2*frameThickness), frameThickness);
		g2d.fillRect((int)posX, (int)(posY+frameThickness), frameThickness, (int)(size-2*frameThickness));
		
		g2d.fillArc((int)posX, (int)posY, 2*frameThickness, 2*frameThickness, 90, 90);
		g2d.fillArc((int)(posX+size-2*frameThickness), (int)posY, 2*frameThickness, 2*frameThickness, 0, 90);
		g2d.fillArc((int)posX, (int)(posY+size-2*frameThickness), 2*frameThickness, 2*frameThickness, 180, 90);
		g2d.fillArc((int)(posX+size-2*frameThickness), (int)(posY+size-2*frameThickness), 2*frameThickness, 2*frameThickness, 270, 90);
		
		//rysowanie tekstu
		g2d.setColor(Color.BLACK);
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHints(rh);
		
		Font font = new Font("URW Chancery L", Font.BOLD, (int)(size - 4*frameThickness));
		g2d.setFont(font);
		
		//rysuj glowna liczbe
		if (fieldValue > 0) {centerString(g2d, new Rectangle((int)posX, (int)posY, (int)size, (int)size), Integer.toString(fieldValue), font);}
		else
		{
			//rysuj mniejsze liczby
			font = new Font("URW Chancery L", Font.PLAIN, 12);
			g2d.setColor(Color.gray);
			
			//konwersja na string
			Integer[] tmpArray = new Integer[notedNumbers.size()];
			StringBuilder s1 = new StringBuilder();
			StringBuilder s2 = new StringBuilder();
			StringBuilder s3 = new StringBuilder();
			notedNumbers.toArray(tmpArray);
			for (int i = 0; i < tmpArray.length; i++)
			{
				if (i < 3) {s1.append(tmpArray[i] + " ");}
				else if (i < 6) {s2.append(tmpArray[i] + " ");}
				else {s3.append(tmpArray[i] + " ");}
			}
			//pozbywanie sie dodatkowej spacji na koncu
			s1.setLength(Math.max(s1.length()-1, 0));
			s2.setLength(Math.max(s2.length()-1, 0));
			s3.setLength(Math.max(s3.length()-1, 0));
			
			int tmpPos = (int)(size / 4.0);
			centerString(g2d, new Rectangle((int)(posX+2*frameThickness), (int)(posY+1 * tmpPos - 10), (int)(size - 4*frameThickness), 20), s1.toString(), font);
			centerString(g2d, new Rectangle((int)(posX+2*frameThickness), (int)(posY+2 * tmpPos - 10), (int)(size - 4*frameThickness), 20), s2.toString(), font);
			centerString(g2d, new Rectangle((int)(posX+2*frameThickness), (int)(posY+3 * tmpPos - 10), (int)(size - 4*frameThickness), 20), s3.toString(), font);
		}
	}
	
	/**
	 * This method centers a <code>String</code> in 
	 * a bounding <code>Rectangle</code>.
	 * @param g - The <code>Graphics</code> instance.
	 * @param r - The bounding <code>Rectangle</code>.
	 * @param s - The <code>String</code> to center in the
	 * bounding rectangle.
	 * @param font - The display font of the <code>String</code>
	 * 
	 * @see java.awt.Graphics
	 * @see java.awt.Rectangle
	 * @see java.lang.String
	 */
	public void centerString(Graphics g, Rectangle r, String s, Font font) {
	    
		FontRenderContext frc =  new FontRenderContext(null, true, true);

	    Rectangle2D r2D = font.getStringBounds(s, frc);
	    int rWidth = (int) Math.round(r2D.getWidth());
	    int rHeight = (int) Math.round(r2D.getHeight());
	    int rX = (int) Math.round(r2D.getX());
	    int rY = (int) Math.round(r2D.getY());

	    int a = (r.width / 2) - (rWidth / 2) - rX;
	    int b = (r.height / 2) - (rHeight / 2) - rY;

	    g.setFont(font);
	    g.drawString(s, r.x + a, r.y + b);
	}
	
	public void setFieldValue(int fv)
	{
		fieldValue = fv;
	}
	
	public void updateNotedNumbers(int value)
	{
		if ((value >= 1) & (value <= 9))
		{
			if (!notedNumbers.add(value)) notedNumbers.remove(value);
		}
	}

	public void fieldValueInc() {
		fieldValue++;
		if (fieldValue > 9) fieldValue = 0;
	}

	public boolean checkClick(double posX, double posY) {
		
		return (((posX >= this.posX)&(posX <= this.posX + this.size))&((posY >= this.posY)&(posY <= this.posY + this.size)));
	}
	
	public void setHighlight(boolean b)
	{
		this.isHighlighted = b;
	}
	
	public void setMarked(boolean b)
	{
		this.isMarked = b;
	}
	
	public void setSearched(boolean b)
	{
		this.isSearched = b;
	}
	
	public void setDrawColor(boolean b)
	{
		this.drawColor = b;
	}

	public void clearNoted() 
	{
		this.notedNumbers.clear();
	}
}
