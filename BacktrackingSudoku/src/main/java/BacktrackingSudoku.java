import java.awt.EventQueue;

public class BacktrackingSudoku {
	
	private static MainWindow instance;

	public static void main(String[] args) {
		
		// Dwu wymiarowa zmienna przechowywuj¹ca stan planszy (0-puste pole)
		//int[][] gameBoard = new int[9][9];
		/*int[][] gameBoard = new int[][]{{3, 0, 6, 5, 0, 8, 4, 0, 0},
										{5, 2, 0, 0, 0, 0, 0, 0, 0},
										{0, 8, 7, 0, 0, 0, 0, 3, 1},
										{0, 0, 3, 0, 1, 0, 0, 8, 0},
										{9, 0, 0, 8, 6, 3, 0, 0, 5},
										{0, 5, 0, 0, 9, 0, 6, 0, 0},
										{1, 3, 0, 0, 0, 0, 2, 5, 0},
										{0, 0, 0, 0, 0, 0, 0, 7, 4},
										{0, 0, 5, 2, 0, 6, 3, 0, 0}};
		*/
												
		//Okno				
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				instance = new MainWindow();
			}
		});
	}

}
