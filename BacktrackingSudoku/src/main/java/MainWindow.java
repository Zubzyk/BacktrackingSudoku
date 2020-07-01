import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
	
	//private static final String newGameButtonText = "Nowa gra";
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
	//private JButton newGameButton;
	private JButton clearButton;
	private JButton modeButton;
	private JButton solveButton;
	private JButton solveStepButton;
	private JToggleButton fastForward;
	private JPanel infoPanel;
	private JLabel infoLabel;
	
	private boolean isStepSolving = false;

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
        //newGameButton = new JButton(newGameButtonText);
        clearButton = new JButton(clearButtonText);
        solveButton = new JButton(solveButtonText);
        solveStepButton = new JButton(solveStepButtonText);
        fastForward = new JToggleButton(fastFowardButtonText);
        infoLabel = new JLabel("test", JLabel.CENTER);
        
        String text = gameBoard.getMode() == Board.INPUT_MODE.BIG_NUMBERS ? modeButtonText_mode1 : modeButtonText_mode2;
       
        modeButton = new JButton(text);
        infoPanel = new JPanel(new FlowLayout());
        
        contentPane.add(gameBoard);
        //controlPanel.add(newGameButton);
        controlPanel.add(clearButton);
        controlPanel.add(modeButton);
        controlPanel.add(solveButton);
        controlPanel.add(solveStepButton);
        controlPanel.add(fastForward);
        contentPane.add(controlPanel);
        infoPanel.add(infoLabel);
        contentPane.add(infoPanel);
        
        layout.putConstraint(SpringLayout.WEST, contentPane, -10, SpringLayout.WEST, gameBoard);
        layout.putConstraint(SpringLayout.NORTH, contentPane, -10, SpringLayout.NORTH, gameBoard);
        layout.putConstraint(SpringLayout.EAST, contentPane, 10, SpringLayout.EAST, gameBoard);
        layout.putConstraint(SpringLayout.SOUTH, contentPane, 10, SpringLayout.SOUTH, infoPanel);
        
        layout.putConstraint(SpringLayout.WEST, gameBoard, 10, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, gameBoard, 10, SpringLayout.NORTH, contentPane);
        
        controlPanel.setPreferredSize(new Dimension(0,50));
        layout.putConstraint(SpringLayout.NORTH, controlPanel, 10, SpringLayout.SOUTH, gameBoard);
        layout.putConstraint(SpringLayout.EAST, controlPanel, -10, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.WEST, controlPanel, 10, SpringLayout.WEST, contentPane);
        
        layout.putConstraint(SpringLayout.NORTH, infoPanel, 10, SpringLayout.SOUTH, controlPanel);
        layout.putConstraint(SpringLayout.EAST, infoPanel, -10, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.WEST, infoPanel, 10, SpringLayout.WEST, contentPane);
        
		this.pack();
		fastForward.setVisible(false);
		infoLabel.setVisible(false);
	    this.setVisible(true);   
	    
	    //newGameButton.addActionListener(this);
	    clearButton.addActionListener(this);
	    modeButton.addActionListener(this);
	    solveButton.addActionListener(this);
	    solveStepButton.addActionListener(this);
	    fastForward.addItemListener(this);
	    this.addKeyListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (isStepSolving)
		{
			if (e.getSource() == solveStepButton) stopStepSolving();
		} else {
			//if (e.getSource() == newGameButton);
			if (e.getSource() == clearButton) gameBoard.clearBoard();
			if (e.getSource() == modeButton) toggleMode();
			if (e.getSource() == solveButton) gameBoard.solveUsingBacktracking();
			if (e.getSource() == solveStepButton) startStepSolving();
			
			setInfoText("", Color.WHITE);
		}

		gameBoard.repaint();
	}

	private void toggleMode()
	{
		if (!isStepSolving)
		{
			this.gameBoard.toggleMode();
			String text = gameBoard.getMode() == Board.INPUT_MODE.BIG_NUMBERS ? modeButtonText_mode1 : modeButtonText_mode2;
			this.modeButton.setText(text);
		}
	}

	public void keyPressed(KeyEvent e) {
		if (!isStepSolving)
		{
			if (e.getKeyCode() == KeyEvent.VK_SPACE) stopStepSolving(); 
		} else {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) toggleMode(); 
		}
	}

	private void startStepSolving() {
		gameBoard.solveStepByStep();
		gameBoard.lockControl();
		fastForward.setVisible(true);
		infoLabel.setText("");
		isStepSolving = true;
	}
	
	private void stopStepSolving() {
		gameBoard.stopSolveStepByStep();
		gameBoard.unlockControl();
		fastForward.setVisible(false);
		isStepSolving = false;
	}

	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED)
		{
			gameBoard.setTimerDelay(5);
		} else {
			gameBoard.setTimerDelay(100);
		}
		
	}
	
	public void setInfoText(String msg, Color c)
	{
		infoLabel.setVisible(true);
		infoLabel.setText(msg);
		infoLabel.setForeground(c);
	}
	
	//nieuzywane event listnery
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}
