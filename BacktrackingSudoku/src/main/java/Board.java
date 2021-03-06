import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static int frameThickness = 6;

	private double size, fieldSize;
	
	private NumberField[][] gridElements;
	
	private NumberField marked;
	
	private int[][] hardState;
	
	enum INPUT_MODE {BIG_NUMBERS, SMALL_NUMBERS;}
	
	INPUT_MODE inputMode;
	
	MainWindow parentWindow;
	
	private int[][] solvingArray = new int[9][9];
	private int[][] solvingHardState = new int[9][9];
	
	private int step_i, step_j;
	
	Timer solveClock;
	ActionListener taskPerformer;
	
	private boolean isLocked = false;
	
	Board(MainWindow parent, float s, int[][] initialState) throws Exception {
		super();
		
		this.parentWindow = parent;
		this.size = s;
		
		Dimension boardSize = new Dimension((int)size, (int)size);
		this.setPreferredSize(boardSize);
		this.setMinimumSize(boardSize);
		this.setMaximumSize(boardSize);
		
		this.fieldSize = size / 9.0;
		int space = 10;
		
		gridElements = new NumberField[9][9];
		
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{	
				NumberField tmpField = new NumberField(i*fieldSize + space / 2, j*fieldSize + space / 2, fieldSize - space);
				gridElements[i][j] = tmpField;
			}
		}
		
		this.hardState = new int[9][9];
		try 
		{
			this.setHardState(initialState);
		}
		catch (Exception e)
		{
			throw new Exception("Failed to initialize board", e);
		}
		this.clearBoard();
		
		this.inputMode = Board.INPUT_MODE.BIG_NUMBERS;
		
		this.setFocusable(true);
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addKeyListener(this);
		
		int delay = 100; //milliseconds
		taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
		        stepSolve();
		    }
		};
		solveClock = new Timer(delay, taskPerformer);
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
	
	private NumberField findFieldUnderPosition(int positionX, int positionY)
	{
		for (NumberField[] lines : gridElements)
		{
			for(NumberField field : lines)
			{
				if (field.checkClick(positionX, positionY)) return field;
			}
		}
		
		return null;
	}
	
	private void clearHighlight()
	{
		for (NumberField[] lines : gridElements)
		{
			for(NumberField field : lines)
			{
				field.setHighlight(false);
			}
		}
	}
	
	private void setLineColumnHighlight(NumberField reference)
	{
		if (reference != null)
		{
			int line = 10;
			int column = 10;
			
			for (int i = 0; i < gridElements.length; i++)
			{
				for(int j = 0; j < gridElements.length; j++)
				{
					if (gridElements[i][j] == reference) 
					{
						line = i;
						column = j;
						break;
					}
						
				}
			}
			
			
			if ((line < 10) & (column < 10))
			{
				for(int i = 0; i < gridElements.length; i++)
				{
					gridElements[i][column].setHighlight(true);
				}
				
				for(int i = 0; i < gridElements.length; i++)
				{
					gridElements[line][i].setHighlight(true);
				}
			}
		}
		
	}
	
	private void setMarked(NumberField m)
	{
		if (m != null) 
		{
			marked = m;
			marked.setMarked(true);
		}
	}
	
	private void unSetMarked()
	{
		if (marked != null) marked.setMarked(false);
		marked = null;
	}
	
	public void clearBoard()
	{
		for (int i = 0; i < gridElements.length; i++)
		{
			for(int j = 0; j < gridElements.length; j++)
			{
				gridElements[i][j].setFieldValue(hardState[i][j]);
				gridElements[i][j].clearNoted();
			}
		}
		
		unSetMarked();
	}
	
	public void setHardState(int[][] newState) throws Exception
	{
		if (newState.length != 9) throw new Exception("Improper board state data");
		if (newState[0] == null) throw new Exception("Improper board state data");
		if (newState[0].length != 9) throw new Exception("Improper board state data");
		try
		{
			for (int i = 0; i < hardState.length; i++)
			{
				for(int j = 0; j < hardState.length; j++)
				{
					hardState[i][j] = newState[j][i];
				}
			}
		}
		catch (Exception e)
		{
			throw new Exception("Improper board state data", e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		
		if (!isLocked)
		{
			this.requestFocusInWindow();
			
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				unSetMarked();
				
				NumberField tmp = findFieldUnderPosition(e.getX(), e.getY());
				
				setMarked(tmp);
			}
			
			if (e.getButton() == MouseEvent.BUTTON2);//TODO: changing entermode
			
			if (e.getButton() == MouseEvent.BUTTON3) unSetMarked();
		}
		
		this.repaint();
	}

	public void keyPressed(KeyEvent e) {
		
		if (!isLocked)
		{
			int keyCode = e.getKeyCode();
			
			if ((keyCode >= KeyEvent.VK_0) & (keyCode <= KeyEvent.VK_9))
			{
				enterNumber(keyCode - KeyEvent.VK_0); 
			}
			
			if ((keyCode >= KeyEvent.VK_NUMPAD0) & (keyCode <= KeyEvent.VK_NUMPAD9))
			{
				enterNumber(keyCode - KeyEvent.VK_NUMPAD0); 
			}
			
			if ((keyCode == KeyEvent.VK_BACK_SPACE) & (marked != null)) marked.setFieldValue(0);
			if ((keyCode == KeyEvent.VK_DELETE) & (marked != null)) marked.setFieldValue(0);
			if (keyCode == KeyEvent.VK_ESCAPE) unSetMarked();
			if (keyCode == KeyEvent.VK_SPACE) parentWindow.dispatchEvent(e);
		}
		
		this.repaint();
	}

	private void enterNumber(int value) 
	{	
		if (marked == null) return;
		
		for (int i = 0; i < gridElements.length; i++)
		{
			for(int j = 0; j < gridElements.length; j++)
			{
				if (gridElements[i][j] == marked) 
				{
					if (hardState[i][j] != 0) return;
				}
					
			}
		}
		
		switch (inputMode)
		{
		case BIG_NUMBERS:
			marked.setFieldValue(value);
			break;
		case SMALL_NUMBERS:
			marked.updateNotedNumbers(value);
			break;
		default:
			break;
		}
	}



	public void mouseMoved(MouseEvent e) {
		
		if (!isLocked)
		{
			clearHighlight();
			
			NumberField tmp = findFieldUnderPosition(e.getX(), e.getY());
			
			if (tmp != null) setLineColumnHighlight(tmp);
			
			this.repaint();
		}
	}

	public void toggleMode() {
		this.inputMode = this.inputMode == Board.INPUT_MODE.BIG_NUMBERS ? Board.INPUT_MODE.SMALL_NUMBERS : Board.INPUT_MODE.BIG_NUMBERS;
	}

	public Board.INPUT_MODE getMode() {
		return this.inputMode;
	}
	
	private void prepareForSolving()
	{
		//skopiuj aktualny stan planszy do zmiennych roboczych
		for (int i = 0; i < solvingArray.length; i++)
		{
			for (int j = 0; j < solvingArray[0].length; j++)
			{
				solvingArray[i][j] = gridElements[i][j].getValue();
				solvingHardState[i][j] = gridElements[i][j].getValue();
			}
		}
	}
	
	private void copySolution()
	{
		for (int i = 0; i < solvingArray.length; i++)
		{
			for (int j = 0; j < solvingArray[0].length; j++)
			{
				 gridElements[i][j].setFieldValue(solvingArray[i][j]);
			}
		}
	}
	
	
	public void solveUsingBacktracking()
	{
		//przygotowac wstepnie macierz do rozwiazywania		
		prepareForSolving();
		
		//rozwiazywanie sudoku (backtracking rekursywnie) jezeli true znaleziono rozwiaznie
		if (solveRecursive(0, 0))
		{
			//przepisanie rozwiazania
			copySolution();
			reportSuccess();
		} else
		{
			//nie znaleziono rozwiazania
			reportFailure();
		}
	}

	private boolean solveRecursive(int i, int j)
	{
		//jezeli dotarlismy do konca zwroc true
		if ((i >= solvingArray.length) || (j >= solvingArray[0].length)) return true;
		
		//jezeli natrafi na podana liczbe w problemie to przejdz dalej
		if (isHardState(i,j)) 
		{
			if (j == 8) 
			{
				return solveRecursive(i+1, 0);
			} else {
				return solveRecursive(i, j+1);
			}
		} else
		{
			//jezeli brak jakiejkolwiek liczby przejdz do 1
			if (solvingArray[i][j] == 0) solvingArray[i][j] = 1;
			
			//sprawdzaj liczby rosn�co
			while (solvingArray[i][j] <= 9)
			{
				//jezeli dana liczba moze zajmowac sprawdzana pozycje przechodzimy dalej
				if (checkRules(i, j))
				{
					if (j == 8) 
					{
						if (solveRecursive(i+1, 0)) return true;
					} else {
						if (solveRecursive(i, j+1)) return true;
					}
				}
					
				solvingArray[i][j]++;
			}
		}
		
		//skoro nie znaleziono zwracamy false i backtrackujemy
		solvingArray[i][j] = 0;
		return false;
	}
	
	private boolean checkRules(int i, int j)
	{
		//sprawdz w wierszu (liczba nie moze sie powtrzac w wierszu)
		for (int a = 0; a < solvingArray.length; a++)
		{
			if ((a != i) && (solvingArray[a][j] == solvingArray[i][j])) return false;
		}
		
		//sprawdz w kolumnie (liczba nie moze sie powtarzac w kolumnie)
		for (int a = 0; a < solvingArray.length; a++)
		{
			if ((a != j) && (solvingArray[i][a] == solvingArray[i][j])) return false;
		}
		
		//sprawdz w polu 3x3 (liczba nie moze sie powtarzac)
		int subFieldX = i / 3;
		int subFieldY = j / 3;
		
		for (int a = 0; a < 3; a++)
		{
			for (int b = 0; b < 3; b++)
			{
				int subX = subFieldX * 3 + a;
				int subY = subFieldY * 3 + b;
				if ((subX != i) && (subY != j) && solvingArray[subX][subY] == solvingArray[i][j]) return false;
			}
		}
		
		return true;
	}
	
	public void solveStepByStep()
	{
		//przygotowanie zmiennych kotrolnych
		this.step_i = 0;
		this.step_j = 0;
		
		//przygotowac wstepnie macierz do rozwiazywania		
		prepareForSolving();
		
		//przejdz do pierszego nie rozwiazanego pola
		if (isHardState(step_i, step_j)) stepForward();
		
		//jezeli nie ma co rozwiazywac
		if (step_i > 8) return;
		
		//rozpoczecie timera rozwiazujacego
		solveClock.restart();
	}
	
	private void stepSolve()
	{
		//odswiez podglad
		if ((step_i < 9) & (step_j < 9))
		{
			unSetMarked();
			setMarked(gridElements[step_i][step_j]);
			this.marked.setFieldValue(solvingArray[step_i][step_j]);
			this.repaint();
		}
		
		//zinkrementuj liczbe sprawdzana
		solvingArray[step_i][step_j]++;
		
		if (solvingArray[step_i][step_j] == 10) 
		{
			//jezeli nie znaleziono wroc do poprzedneij liczby
			solvingArray[step_i][step_j] = 0;
			this.marked.setFieldValue(solvingArray[step_i][step_j]);
			this.repaint();
			
			if (stepBackward()) return;
		} else {
			//sprawdz czy pasuje liczba i jezeli tak przejdz dalej
			if (checkRules(step_i, step_j))
			{
				if (stepForward()) return;
			}
		}
	}
	
	private boolean isHardState(int i, int j)
	{
		return (solvingArray[i][j] == solvingHardState[i][j]) & (solvingArray[i][j] != 0);
	}
	
	private boolean stepForward()
	{
		//odswiez podglad dla pol ktore pomijamy gdy 2 pola z hardState znajduja sie przed i po rozwiazywanlnego pola
		if ((step_i < 9) & (step_j < 9))
		{
			unSetMarked();
			setMarked(gridElements[step_i][step_j]);
			this.marked.setFieldValue(solvingArray[step_i][step_j]);
			this.repaint();
		}
		
		step_j++;
		
		if (step_j > 8)
		{
			step_i++;
			step_j = 0;
		}
		
		if (step_i > 8) 
		{
			stopSolving(); //success
			reportSuccess();
			return true;
		}
		
		if (isHardState(step_i,step_j)) stepForward();
		
		return false;
	}
	
	private boolean stepBackward()
	{
		//odswiez podglad dla pol ktore pomijanych...
			if ((step_i < 9) & (step_j < 9))
			{
				unSetMarked();
				setMarked(gridElements[step_i][step_j]);
				this.marked.setFieldValue(solvingArray[step_i][step_j]);
				this.repaint();
			}
			
		step_j--;
		if (step_j < 0)
		{
			step_i--;
			step_j = 8;
		}
		
		if (step_i < 0) 
		{
			stopSolving(); //failed
			reportFailure();
			return true;
		}
		
		if (isHardState(step_i,step_j)) stepBackward();
		
		return false;
	}
	
	private void stopSolving()
	{
		solveClock.stop();
	}

	public void setTimerDelay(int ms) {
		solveClock.setDelay(ms);
	}
	

	public void stopSolveStepByStep() {
		unSetMarked();
		stopSolving();
	}
	
	public void lockControl()
	{
		isLocked = true;
	}
	
	public void unlockControl()
	{
		isLocked = false;
	}
	
	private void reportSuccess()
	{
		unSetMarked();
		parentWindow.setInfoText("Success", Color.GREEN);
	}
	
	private void reportFailure()
	{
		unSetMarked();
		parentWindow.setInfoText("Failure", Color.RED);
	}
	
	//nieuzywane event listnery
	public void keyReleased(KeyEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void keyTyped(KeyEvent e) {}

}

