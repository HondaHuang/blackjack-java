package blackjackgame;

public class Card {
	private int value;
	private int cardSuit;
	public String fileName;
	boolean aceCheck; // Checks to see if Ace value is 1 (false) or 11 (true)

	public Card() {
		value = 0;
		cardSuit = 0;
	}

	public Card(int suit, int val) {
		value = val;
		cardSuit = suit;
		fileName = Integer.toString(value) + suit;
	}

	public void setValue(int val) {
		value = val;
	}

	public void setSuit(int suit) {
		cardSuit = suit;
	}

	public int getValue() {
		return value;
	}

	public int getSuit() {
		return cardSuit;
	}

	public void aceChecked() {
		aceCheck = true;
	}

	public void aceUnchecked() {
		aceCheck = false;
	}
}
