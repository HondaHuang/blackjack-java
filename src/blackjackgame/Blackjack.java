package blackjackgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Blackjack extends JFrame {
	int cash;
	int wager;
	Font f = new Font("Impact", Font.BOLD, 20);
	ArrayList<Card> deck;
	Card[] usersCards = new Card[10];
	Card[] dealersCards = new Card[10];
	private JButton buttonHit, buttonStay, buttonDouble, nextRound;
	private JPanel userPane, dealerPane, playerPane;
	private JLabel labelMoney, labelBet, dealerPoints, playerPoints, cardLabel, cardBack;
	private ImageIcon img, back;
	final int WIDTH = 500;
	final int HEIGHT = 350;
	boolean noDouble; // Prevents doubling after the player has already hit
	boolean roundActive = true;

	public Blackjack() {
		getContentPane().setBackground(new Color(50, 205, 50));
		setTitle("Blackjack");
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null); // Centers application to middle of screen
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setVisible(true);
		buildGame();
		gameRestart();
	}

	public void showDealerCard(Card c) {
		img = new ImageIcon(getClass().getResource("Cards/" + c.fileName + ".png"));
		cardLabel = new JLabel(img);
		dealerPane.add(cardLabel);
		cardLabel.setVisible(true);
	}

	public void showPlayerCard(Card c) {
		img = new ImageIcon(getClass().getResource("Cards/" + c.fileName + ".png"));
		JLabel cardLabel = new JLabel(img);
		playerPane.add(cardLabel);
		cardLabel.setVisible(true);
	}

	public int getUserCards() {
		int numberOfCards = 0;
		while (usersCards[numberOfCards] != null)
			numberOfCards++;
		return numberOfCards;
	}

	public int getDealerCards() {
		int numberOfCards = 0;
		while (dealersCards[numberOfCards] != null)
			numberOfCards++;
		return numberOfCards;
	}

	public int getTotal() {
		int numberOfCards = 0;
		int sum = 0;
		int cardVal = 0;
		while (usersCards[numberOfCards] != null) {
			cardVal = usersCards[numberOfCards].getValue();
			if (cardVal == 1 && sum <= 10) { // Changes value of Ace from 1 to 11 when appropriate
				cardVal = 11;
				usersCards[numberOfCards].aceChecked(); // Ace changed to 11
			} else if (cardVal > 10) // Changes value of Jack, Queen, and King to 10
				cardVal = 10;
			sum += cardVal;
			if (sum > 21) {
				for (int i = 0; i <= numberOfCards; i++) { // Changes value of Ace from 11 to 1 if total exceeds 21
					if (usersCards[i].getValue() == 1 && usersCards[i].aceCheck == true) {
						/*
						 * Checks for Aces with a value of 11
						 */
						usersCards[i].aceUnchecked(); // Changes value of Ace back to 1
						sum -= 10; // Points are deducted by 10 to reflect the change
					}
				}
			}
			numberOfCards++;
		}
		return sum;
	}

	public int getDealerTotal() {
		int numberOfCards = 0;
		int sum = 0;
		int cardVal = 0;
		while (dealersCards[numberOfCards] != null) {
			cardVal = dealersCards[numberOfCards].getValue();
			if (cardVal == 1 && sum <= 10) {
				cardVal = 11;
				dealersCards[numberOfCards].aceChecked();
			} else if (cardVal > 10)
				cardVal = 10;
			sum += cardVal;
			if (sum > 21) {
				for (int i = 0; i <= numberOfCards; i++) {
					if (dealersCards[i].getValue() == 1 && dealersCards[i].aceCheck == true) {
						dealersCards[i].aceUnchecked();
						sum -= 10;
					}
				}
			}
			numberOfCards++;
		}
		return sum;
	}

	public int setWager(int bet) {
		try {
			bet = Integer.parseInt(JOptionPane.showInputDialog("Enter Your Bet" + " (Max: " + cash + "):"));
		} catch (NumberFormatException ex) {
			bet = 0;
		}
		if (bet <= 0 || bet > cash)
			bet = setWager(0);
		else
			labelBet.setText(Integer.toString(bet));
		return bet;
	}

	public void buildGame() { // Builds the interface
		dealerPane = new JPanel();
		getContentPane().add(dealerPane, BorderLayout.NORTH);
		dealerPane.setBackground(new Color(50, 205, 50));
		dealerPoints = new JLabel("Dealer Points: " + getDealerTotal());
		dealerPoints.setFont(f);
		playerPane = new JPanel();
		getContentPane().add(playerPane, BorderLayout.CENTER);
		playerPane.setBackground(new Color(50, 205, 50));
		playerPoints = new JLabel("Your Points: " + getTotal());
		playerPoints.setFont(f);
		playerPoints.setForeground(Color.WHITE);
		userPane = new JPanel(new GridLayout(4, 3));
		userPane.setBackground(Color.LIGHT_GRAY);
		buttonHit = new JButton("Hit");
		buttonStay = new JButton("Stay");
		buttonDouble = new JButton("Double");
		labelBet = new JLabel("0");
		labelMoney = new JLabel("0");
		userPane.add(new JLabel(" Total Cash: "));
		userPane.add(labelMoney);
		userPane.add(new JLabel(" Current Wager: "));
		userPane.add(labelBet);
		userPane.add(buttonHit);
		userPane.add(buttonStay);
		userPane.add(buttonDouble);
		buttonHit.addActionListener(new Hit());
		buttonStay.addActionListener(new Stay());
		buttonDouble.addActionListener(new DoubleHit());
		nextRound = new JButton("Next Round");
		userPane.add(nextRound);
		nextRound.addActionListener(new newRound());
		getContentPane().add(userPane, BorderLayout.SOUTH);
		cash = 1000;
		labelMoney.setText(Integer.toString(cash));
	}

	public void gameRestart() {
		noDouble = false;
		dealerPane.add(dealerPoints);
		playerPane.add(playerPoints);
		wager = setWager(0);
		usersCards = new Card[10];
		dealersCards = new Card[10];
		deck = new ArrayList<Card>(52);
		for (int i = 0; i < 52; i++)
			deck.add(i, new Card((i % 4) + 1, (i % 13) + 1)); // Creates deck of 52 cards
		usersCards[0] = pullRandomCard(); // Draws a random card
		showPlayerCard(usersCards[0]); // Displays respective card image
		usersCards[1] = pullRandomCard();
		showPlayerCard(usersCards[1]);
		dealersCards[0] = pullRandomCard();
		showDealerCard(dealersCards[0]);
		back = new ImageIcon(getClass().getResource("Cards/00.png"));
		cardBack = new JLabel(back);
		dealerPane.add(cardBack); // Displays Dealer's hidden card
		dealerPoints.setText("Dealer points:  " + getDealerTotal());
		playerPoints.setText("Your points:  " + getTotal());
		if (getTotal() == 21) { // Auto-win for initial blackjack
			dealerPane.remove(cardBack);
			dealersCards[getDealerCards()] = pullRandomCard();
			showDealerCard(dealersCards[getDealerCards() - 1]);
			dealerPoints.setText("Dealer points:  " + getDealerTotal());
			if (getDealerTotal() == 21) { // Checks to see if Dealer also has initial 21
				JOptionPane.showMessageDialog(null, "The Round Ended In A Push");
			} else {
				JOptionPane.showMessageDialog(null, "You Won The Round!");
				cash += wager;
				labelMoney.setText(Integer.toString(cash));
			}
			roundActive = false;
		}
	}

	public Card pullRandomCard() {
		Random rand = new Random();
		return deck.remove(rand.nextInt(deck.size()));
	}

	class newRound implements ActionListener { // Clears board for new round
		public void actionPerformed(ActionEvent ae) {
			if (roundActive == false) {
				roundActive = true;
				dealerPane.removeAll();
				playerPane.removeAll();
				gameRestart();
				revalidate();
				repaint();
			}
		}
	}

	class DoubleHit implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if (roundActive && noDouble == false && wager <= cash / 2) { // Prevents betting over the limit
				wager *= 2;
				usersCards[getUserCards()] = pullRandomCard();
				showPlayerCard(usersCards[getUserCards() - 1]);
				playerPoints.setText("Your points:  " + getTotal());
				if (getTotal() > 21) {
					dealerPane.remove(cardBack);
					dealersCards[getDealerCards()] = pullRandomCard();
					showDealerCard(dealersCards[getDealerCards() - 1]);
					dealerPoints.setText("Dealer points:  " + getDealerTotal());
					JOptionPane.showMessageDialog(null, "You Lost The Round");
					roundActive = false;
					cash -= wager;
					labelMoney.setText(Integer.toString(cash));
				} else {
					roundActive = false;
					while (getDealerTotal() < 17) {
						dealerPane.remove(cardBack);
						dealersCards[getDealerCards()] = pullRandomCard();
						showDealerCard(dealersCards[getDealerCards() - 1]);
						dealerPoints.setText("Dealer points:  " + getDealerTotal());
					}
					if (getDealerTotal() > 21) {
						JOptionPane.showMessageDialog(null, "You Won The Round!");
						cash += wager;
						labelMoney.setText(Integer.toString(cash));
					} else {
						if (21 - getTotal() < 21 - getDealerTotal()) {
							JOptionPane.showMessageDialog(null, "You Won The Round!");
							cash += wager;
							labelMoney.setText(Integer.toString(cash));
						} else if (getTotal() == getDealerTotal()) {
							JOptionPane.showMessageDialog(null, "The Round Ended In A Push");
						} else {
							JOptionPane.showMessageDialog(null, "You Lost The Round");
							cash -= wager;
							labelMoney.setText(Integer.toString(cash));
						}
					}
				}
				if (cash <= 0) {
					JOptionPane.showMessageDialog(null, "You Filed For Bankruptcy");
					System.exit(0);
				}
			}
		}
	}

	class Hit implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if (roundActive) {
				noDouble = true;
				usersCards[getUserCards()] = pullRandomCard();
				showPlayerCard(usersCards[getUserCards() - 1]);
				playerPoints.setText("Your points:  " + getTotal());
				if (getTotal() > 21) {
					dealerPane.remove(cardBack);
					dealersCards[getDealerCards()] = pullRandomCard();
					showDealerCard(dealersCards[getDealerCards() - 1]);
					dealerPoints.setText("Dealer points:  " + getDealerTotal());
					JOptionPane.showMessageDialog(null, "You Lost The Round");
					roundActive = false;
					cash -= wager;
					labelMoney.setText(Integer.toString(cash));
				}
				if (cash <= 0) {
					JOptionPane.showMessageDialog(null, "You Filed For Bankruptcy");
					System.exit(0);
				}
			}
		}
	}

	class Stay implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if (roundActive) {
				roundActive = false;
				while (getDealerTotal() < 17) {
					dealerPane.remove(cardBack);
					dealersCards[getDealerCards()] = pullRandomCard();
					showDealerCard(dealersCards[getDealerCards() - 1]);
					dealerPoints.setText("Dealer points:  " + getDealerTotal());
				}
				if (getDealerTotal() > 21) {
					JOptionPane.showMessageDialog(null, "You Won The Round!");
					cash += wager;
					labelMoney.setText(Integer.toString(cash));
				} else {
					if (21 - getTotal() < 21 - getDealerTotal()) {
						JOptionPane.showMessageDialog(null, "You Won The Round!");
						cash += wager;
						labelMoney.setText(Integer.toString(cash));
					} else if (getTotal() == getDealerTotal()) {
						JOptionPane.showMessageDialog(null, "The Round Ended In A Push");
					} else {
						JOptionPane.showMessageDialog(null, "You Lost The Round");
						cash -= wager;
						labelMoney.setText(Integer.toString(cash));
					}
				}
			}
			if (cash <= 0) {
				JOptionPane.showMessageDialog(null, "You Filed For Bankruptcy");
				System.exit(0);
			}
		}
	}

	public static void main(String[] args) {
		Blackjack app = new Blackjack();
		app.setVisible(true);
	}
}