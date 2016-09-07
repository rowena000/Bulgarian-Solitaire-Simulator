import java.util.Random;
import java.util.Scanner;

/*
   class SolitaireBoard
   The board for Bulgarian Solitaire.  
 */


public class SolitaireBoard {

	public static final int NUM_FINAL_PILES = 9;
	// number of piles in a final configuration
	// (note: if NUM_FINAL_PILES is 9, then CARD_TOTAL below will be 45)

	public static final int CARD_TOTAL = NUM_FINAL_PILES * (NUM_FINAL_PILES + 1) / 2;
	// bulgarian solitaire only terminates if CARD_TOTAL is a triangular number.
	// see: http://en.wikipedia.org/wiki/Bulgarian_solitaire for more details
	// the above formula is the closed form for 1 + 2 + 3 + . . . + NUM_FINAL_PILES


	/**
	numPiles is the number of current piles
	cardPiles is the array to store card piles, it could be a partially filled array
	the cards are in cardPile[0] - cardPile[numPiles -1]
	 */
	private int[] cardPiles;
	private int numPiles;
	
	/**
	Representation invariant: 
	0 < numPiles <= CARD_TOTAL
	cardPiles.length = CARD_TOTAL
	the sum from cardPile[0] to cardPile[numPiles-1] equals to CARD_TOTAL
	elements in cardPile[0] to cardPile[numPiles-1] are greater than 0;
	 */

	/**
     Creates a solitaire board with the given configuration.
     PRE: SolitaireBoard.isValidConfigString(String configString) is true
	 */
	public SolitaireBoard(String configString) {
		cardPiles= convertStringToArray(configString);
		numPiles = getNumPiles(cardPiles);
		assert isValidSolitaireBoard();  
	}

	/**
      Creates a solitaire board with a random initial configuration.
	 */
	public SolitaireBoard() {
		cardPiles = generateRandomArray();
		numPiles = getNumPiles(cardPiles);
		assert isValidSolitaireBoard();
	}

	/**
	 * Takes a array, return the numb of piles in this array
	 * (number of elements greater than 0)
	 * @param cardPile an array of card piles
	 * @return num number of card piles
	 */
	private static int getNumPiles(int[] cardPile){
		int num =0;
		while (cardPile[num] != 0){
			num++;
		}				
		return num;
	}	

	/**
      Plays one round of Bulgarian solitaire.  Updates the configuration according to the rules
      of Bulgarian solitaire: Takes one card from each pile, and puts them all together in a new pile.
      The old piles that are left will be in the same relative order as before, 
      and the new pile will be at the end.
      PRE: SolitaireBoard.isValidSolitaireBoard() is true
	 */
	public void playRound() {
		//takes one card from each pile, and puts them all together in a new pile.
		int newPile = 0;
		for (int i = 0; i < numPiles; i++) { 
			cardPiles[i] -= 1;
			newPile++;
		}

		//delete piles with 0 card in it.
		int j = 0;
		for (int i = 0; i < numPiles; i++) {
			if (cardPiles[i] != 0) {
				cardPiles[j] = cardPiles[i];
				j++;
			}
		}

		cardPiles[j] = newPile;
		j++;
		numPiles = j;

		assert isValidSolitaireBoard();
	}

	/**
      Returns true iff the current board is at the end of the game.  That is, there are NUM_FINAL_PILES
      piles that are of sizes 1, 2, 3, . . . , NUM_FINAL_PILES, in any order.
      PRE: SolitaireBoard.isValidSolitaireBoard() is true
      @return done a boolean variable to indicate whether the game is done
	 */
	public boolean isDone() {
		Boolean done = true;
		//check if numPiles equals NUM_FINSL_PILES
		if (numPiles != NUM_FINAL_PILES) {
			done = false;
		}
		/* if numPile is correct, check each element
		 * Use an array to keep track of different card number in each pile
		 * If detect card number i, set array[i-1] = 1.
		 * At the end, all the array element should be 1, otherwise, it's not done.
		 */
		if (done) {
			int[] validArr = new int[NUM_FINAL_PILES];
			for (int i = 0; i < numPiles; i++) {
				int num = cardPiles[i];
				//if there is pile that has more cards then total num of pile, it's not done.
				if (num > NUM_FINAL_PILES) {
					done =  false;
					break;
				} else {
					if (validArr[num-1] == 1) {
						done = false;
						break;
					} else {
						validArr[num-1] = 1;
					}
				}
			}

			for (int i = 0; i < numPiles; i++) {
				if (validArr[i] != 1) {
					done = false;
					break;
				}
			}
		}
		assert isValidSolitaireBoard(); 
		return done;
	}

	/**
      Returns current board configuration as a string with the format of
      a space-separated list of numbers with no leading or trailing spaces.
      The numbers represent the number of cards in each non-empty pile.
      PRE: SolitaireBoard.isValidSolitaireBoard() is true
      @return configStr a string of configuration
	 */
	public String configString() {
		String configStr = new String();

		for (int i = 0; i < numPiles - 1; i++) {
			configStr += cardPiles[i] + " ";
		}
		configStr += cardPiles[numPiles-1];

		assert isValidSolitaireBoard(); 
		return configStr;
	}

	/**
      Returns true iff configString is a space-separated list of numbers that
      is a valid Bulgarian solitaire board with card total SolitaireBoard.CARD_TOTAL
      @param configString a string of configuration
      @return isValid a boolean variable indicating whether the string is valid
	 */
	public static boolean isValidConfigString(String configString) {
		boolean allNumbers = checkStringNumber(configString);
		if (!allNumbers) {
			return false;
		}

		int[] piles = convertStringToArray(configString);
		int nums = getNumPiles (piles);
		return isValidConfiguration(piles, nums);  
	}

	/**
      Returns true iff the solitaire board data is in a valid state
      @return isValid a boolean variable indicating whether the Solitaire Board is valid
	 */
	private boolean isValidSolitaireBoard() {
		return isValidConfiguration(cardPiles, numPiles);  
	}

	/**
	 * A helper method to test whether a given array of card piles is valid 
	 * @param piles an array of piles, element is number of cards in that pile
	 * @param numPiles number of piles 
	 * @return isValid a boolean variable to indicate whether the configuration is true
	 */
	private static boolean isValidConfiguration(int[] piles, int numPiles){
		Boolean isValid = true;
		if (piles.length != CARD_TOTAL){
			isValid = false;
		}
		int sum = 0;
		for (int i = 0 ; i<numPiles; i++){
			if (piles[i] <= 0){
				isValid = false;
				break;
			}
			sum += piles[i];
		}
		if(sum != CARD_TOTAL){
			isValid = false;
		}
		return isValid;  
	}

	/**
	 * Use scanner to read configString,
	 * check if every element is a number
	 * @return allNumber a boolean variable to indicate whether all inputs are numbers
	 */
	private static boolean checkStringNumber(String configString) {
		boolean allNumbers = true;
		Scanner line = new Scanner(configString);
		int i=0;
		while (line.hasNextInt() && line.hasNext()) {
			line.next();
		}
		if (line.hasNext() && !line.hasNextInt()) {
			allNumbers = false;
		}
		return allNumbers;
	}

	/**
	 * This method read user's input configString, split the string by space
	 * then convert each element to a number
	 * @param configString a string of configuration
	 * @return array an array converted from the input string
	 */
	private static int[] convertStringToArray(String configString) {
		int[] array = new int[CARD_TOTAL];
		Scanner line = new Scanner(configString);
		int i=0;
		while (line.hasNextInt()) {
			int number = line.nextInt();
			array[i] = number;
			i++;
		}
		return array;
	}

	/**
	 * This method generates random number of piles,
	 * then for each pile, generate random number of cards in each pile
	 * @return array a random array
	 */
	private int[] generateRandomArray() {
		int[] array = new int[CARD_TOTAL];
		Random r = new Random();
		int remainingCards = CARD_TOTAL;
		int i = 0;

		while (remainingCards > 0) {
			int cardInPile = r.nextInt(remainingCards)+1;
			array[i] = cardInPile;
			remainingCards = remainingCards - cardInPile;
			i++;
		}
		return array;
	}
}
