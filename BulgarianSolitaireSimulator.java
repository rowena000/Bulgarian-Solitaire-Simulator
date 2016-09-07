import java.util.Scanner;

/**
 * this main class is to simulate the game Bulgarian Solitaire 
 */

public class BulgarianSolitaireSimulator {

	public static void main(String[] args) {
		boolean singleStep = false;
		boolean userConfig = false;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-u")) {
				userConfig = true;
			} else if (args[i].equals("-s")) {
				singleStep = true;
			}
		}

		SolitaireBoard board = initSolitaireBoard(userConfig);
		play(board,singleStep);
	}

	/**
	 * This method initialize SolitaireBoard method based on user's configuration
	 * @param userConfig
	 * @return board a solitaire board
	 */
	private static SolitaireBoard initSolitaireBoard(boolean userConfig) {
		SolitaireBoard board = null;
		if (userConfig) {
			// if userConfig is true, prompt user for input
			System.out.println("Number of total cards is " + SolitaireBoard.CARD_TOTAL);
			System.out.println(
					"You will be entering the initial configuration of the cards (i.e., how many in each pile).");
			System.out.println("Please enter a space-separated list of positive integers followed by newline:");
			Scanner in = new Scanner(System.in);
			while (in.hasNextLine()) {
				String configString = in.nextLine();
				boolean isValidString = SolitaireBoard.isValidConfigString(configString);
				if (isValidString) {
					// user input is valid, construe solitaire board using user's input, 
					// break the while loop since we don't need user's input anymore
					board = new SolitaireBoard(configString);
					break;
				} else {
					// if the string is invalid, print out error message
					System.out.println("ERROR: Each pile must have at least one card "
							+ "and the total number of cards must be " + SolitaireBoard.CARD_TOTAL);
					System.out.println("Please enter a space-separated list of positive integers followed by newline:");
				}
			}
		} else {
			board = new SolitaireBoard();
		}
		return board;
	}

	/**
	 * This method simulate the play game process
	 * @param board a solitaire board
	 * @param singleStep take a single step at one time
	 */
	private static void play(SolitaireBoard board, boolean singleStep) {
		System.out.println("Initial configuration: " + board.configString());
		int step = 0;
		while (!board.isDone()) {
			board.playRound();
			step++;
			System.out.println("[" + step +"] Current configuration: " + board.configString());
			//check if we will wait user's type enter to continue, or not
			if (singleStep) {
				System.out.print("<Type return to continue>");
				Scanner in = new Scanner(System.in);
				if (in.hasNextLine()) {
					continue;
				}
			} 
		}
		System.out.println("Done!");
	}
}