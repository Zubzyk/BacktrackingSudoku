import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow extends JFrame implements ActionListener, KeyListener, ItemListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String newGameButtonText = "Nowa gra";
	private static final String clearButtonText = "Restart";
	private static final String modeButtonText_mode1 = "Tryb: Du¿e cyfry";
	private static final String modeButtonText_mode2 = "Tryb: Ma³e cyfry";
	private static final String solveButtonText = "Rozwi¹¿";
	private static final String solveStepButtonText = "Rozwi¹¿ (krok po kroku)";
	private static final String fastFowardButtonText = "Prêdkoœæ 20x";
	private static final String windowName = "Backtracking Sudoku";
	
	private static final int[][] initialState = new int[][] {{ 3, 0, 6, 5, 0, 8, 4, 0, 0},
															{5, 2, 0, 0, 0, 0, 0, 0, 0},
															{0, 8, 7, 0, 0, 0, 0, 3, 1},
															{0, 0, 3, 0, 1, 0, 0, 8, 0},
															{9, 0, 0, 8, 6, 3, 0, 0, 5},
															{0, 5, 0, 0, 9, 0, 6, 0, 0},
															{1, 3, 0, 0, 0, 0, 2, 5, 0},
															{0, 0, 0, 0, 0, 0, 0, 7, 4},
															{0, 0, 5, 2, 0, 6, 3, 0, 0}};
	
	private Board gameBoard;
	private JPanel controlPanel;
	private JButton newGameButton;
	private JButton clearButton;
	private JButton modeButton;
	private JButton solveButton;
	private JButton solveStepButton;
	private JToggleButton fastForward;
	private JPanel playbackControl;

	public MainWindow() {
		super(windowName);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLocation(300,100); 
		this.setResizable(false);
		
        Container contentPane = this.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);
        
        try
        {
        	gameBoard = new Board(this, 450, initialState);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        	System.out.println(e);
        	
        	System.exit(ERROR);
        }
        
        controlPanel = new JPanel(new GridLayout(2, 3));
        newGameButton = new JButton(newGameButtonText);
        clearButton = new JButton(clearButtonText);
        solveButton = new JButton(solveButtonText);
        solveStepButton = new JButton(solveStepButtonText);
        fastForward = new JToggleButton(fastFowardButtonText);
        
        String text = gameBoard.getMode() == Board.INPUT_MODE.BIG_NUMBERS ? modeButtonText_mode1 : modeButtonText_mode2;
       
        modeButton = new JButton(text);
        playbackControl = new JPanel(new GridLayout(2, 3));
        
        contentPane.add(gameBoard);
        controlPanel.add(newGameButton);
        controlPanel.add(clearButton);
        controlPanel.add(modeButton);
        controlPanel.add(solveButton);
        controlPanel.add(solveStepButton);
        controlPanel.add(fastForward);
        contentPane.add(controlPanel);
        contentPane.add(playbackControl);
        
        layout.putConstraint(SpringLayout.WEST, contentPane, -10, SpringLayout.WEST, gameBoard);
        layout.putConstraint(SpringLayout.NORTH, contentPane, -10, SpringLayout.NORTH, gameBoard);
        layout.putConstraint(SpringLayout.EAST, contentPane, 10, SpringLayout.EAST, gameBoard);
        layout.putConstraint(SpringLayout.SOUTH, contentPane, 10, SpringLayout.SOUTH, playbackControl);
        
        layout.putConstraint(SpringLayout.WEST, gameBoard, 10, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, gameBoard, 10, SpringLayout.NORTH, contentPane);
        
        controlPanel.setPreferredSize(new Dimension(0,50));
        layout.putConstraint(SpringLayout.NORTH, controlPanel, 10, SpringLayout.SOUTH, gameBoard);
        layout.putConstraint(SpringLayout.EAST, controlPanel, -10, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.WEST, controlPanel, 10, SpringLayout.WEST, contentPane);
        
        layout.putConstraint(SpringLayout.NORTH, playbackControl, 10, SpringLayout.SOUTH, controlPanel);
        layout.putConstraint(SpringLayout.EAST, playbackControl, -10, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.WEST, playbackControl, 10, SpringLayout.WEST, contentPane);
        playbackControl.setVisible(false);        
        
		this.pack();
	    this.setVisible(true);   
	    
	    newGameButton.addActionListener(this);
	    clearButton.addActionListener(this);
	    modeButton.addActionListener(this);
	    solveButton.addActionListener(this);
	    solveStepButton.addActionListener(this);
	    fastForward.addItemListener(this);
	    this.addKeyListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newGameButton);
		if (e.getSource() == clearButton) gameBoard.clearBoard();
		if (e.getSource() == modeButton) toggleMode();
		if (e.getSource() == solveButton) gameBoard.solveUsingBacktracking();
		if (e.getSource() == solveStepButton) gameBoard.solveStepByStep();
		
		gameBoard.repaint();
	}

	private void toggleMode()
	{
		this.gameBoard.toggleMode();
		String text = gameBoard.getMode() == Board.INPUT_MODE.BIG_NUMBERS ? modeButtonText_mode1 : modeButtonText_mode2;
		this.modeButton.setText(text);
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) toggleMode(); //TODO: pausing in playback
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			gameBoard.setTimerDelay(5);
		} else {
			gameBoard.setTimerDelay(100);
		}
		
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
