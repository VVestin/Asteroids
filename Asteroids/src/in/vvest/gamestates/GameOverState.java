package in.vvest.gamestates;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import in.vvest.game.GameStateManager;

public class GameOverState extends GameState {
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWYXZ";

	private String[] topPlayers;
	private int[] topScores;
	private int playerRank;
	private int playerScore;
	private String playerName;
	private int currentLetter;
	private long lastUp, lastDown, lastSpace;

	public GameOverState(GameStateManager gsm, int playerScore) {
		super(gsm);
		this.playerScore = playerScore;
		topPlayers = new String[5];
		topScores = new int[5];
		playerRank = -1;
		try {
			Scanner s = new Scanner(new File("res/highscores.txt"));
			int i;
			for (i = 0; i < topPlayers.length; i++) {
				topPlayers[i] = s.next();
				topScores[i] = s.nextInt();
				if (playerScore > topScores[i] && playerRank == -1)
					playerRank = i;
			}
			if (playerRank != -1) {
				for (i = topPlayers.length - 1; i > playerRank; i--) {
					topPlayers[i] = topPlayers[i - 1];
					topScores[i] = topScores[i - 1];
				}
				topScores[playerRank] = playerScore;
				playerName = "";
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.WHITE);
		g.drawString("Game Over", (400 - fm.stringWidth("Game Over")) / 2, 30);
		String playerScoreMessage = String.format("You Scored: %05d", playerScore);
		g.drawString(playerScoreMessage, (400 - fm.stringWidth(playerScoreMessage)) / 2, 80);
		for (int i = 0; i < topPlayers.length; i++) {
			String currentPlayerName = topPlayers[i];
			if (i == playerRank && playerName.length() < 3) {
				currentPlayerName = playerName + ALPHABET.substring(currentLetter, currentLetter + 1);
				while (currentPlayerName.length() < 3)
					currentPlayerName += ALPHABET.substring(0, 1);
			}
			String scoreMessage = String.format("%d.  %s  %05d", i + 1, currentPlayerName, topScores[i]);
			if (i == playerRank && playerName.length() < 3 && (System.currentTimeMillis() / 400) % 3 != 0) {
				g.drawLine((400 - fm.stringWidth("0.  ")) / 2 + fm.charWidth('A') * (playerName.length() - 1),
						122 + 30 * i, (400 - fm.stringWidth("0.  ")) / 2 + fm.charWidth('A') * playerName.length(),
						122 + 30 * i);
			}
			g.drawString(scoreMessage, (400 - fm.stringWidth(scoreMessage)) / 2, 120 + 30 * i);
		}
	}

	public void update() {
		if (playerRank != -1 && playerName.length() < 3) {
			if (keyState.containsKey("up") && keyState.get("up") && System.currentTimeMillis() - lastUp > 150) {
				currentLetter++;
				currentLetter %= ALPHABET.length();
				lastUp = System.currentTimeMillis();
			} else if (keyState.containsKey("down") && keyState.get("down") && System.currentTimeMillis() - lastDown > 150) {
				currentLetter--;
				if (currentLetter <= 0)
					currentLetter = ALPHABET.length() - 1;
				lastDown = System.currentTimeMillis();
			}
			if (keyState.containsKey("space") && keyState.get("space") && System.currentTimeMillis() - lastSpace > 300) {
				playerName += ALPHABET.substring(currentLetter, currentLetter + 1);
				currentLetter = 0;
				lastSpace = System.currentTimeMillis();
				if (playerName.length() == 3) {
					topPlayers[playerRank] = playerName;
					try {
						FileWriter out = new FileWriter("res/highscores.txt");
						for (int i = 0; i < topPlayers.length; i++) {
							out.write(topPlayers[i] + " " + topScores[i] + "\n");
						}
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
