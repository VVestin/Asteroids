package in.vvest.gamestates;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import in.vvest.game.GameStateManager;
import in.vvest.objects.Asteroid;
import in.vvest.objects.Laser;
import in.vvest.objects.Player;
import in.vvest.objects.Vec2;
import in.vvest.particles.ParticleSystem;

public class PlayState extends GameState {
	
	private List<Laser> lasers;
	private List<Asteroid> asteroids;
	private ParticleSystem particles;
	private Player p;
	private int highScore;
	private long lastFire;
	
	public PlayState(GameStateManager gsm) {
		super(gsm);
		try {
			Scanner s = new Scanner(new File("res/highscores.txt"));
			s.next();
			highScore = s.nextInt();
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		particles = new ParticleSystem();
		p = new Player(3);
		resetLevel();
	}

	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawRect(0, 0, 400, 400);
		p.draw(g);
		for (int i = 0; i < lasers.size(); i++) {
			lasers.get(i).draw(g);
		}
		
		for (int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(g);
		}
		g.setColor(Color.WHITE);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(String.format("%05d", p.getScore()), 320, 20);
		g.drawString("Hi-Score", (400 - fm.stringWidth("Hi-Score")) / 2, 20);
		String highScoreMessage = String.format("%05d", Math.max(highScore, p.getScore()));
		g.drawString(highScoreMessage, (400 - fm.stringWidth(highScoreMessage)) / 2, 40);
		particles.draw(g);
	}

	public void update() {
		p.update(keyState);
		if (keyState.containsKey("space") && keyState.get("space") && System.currentTimeMillis() - lastFire > 250) {
			lasers.add(new Laser(p.getPos(), p.getAngle()));
			lastFire = System.currentTimeMillis();
		}
		for (int i = lasers.size() - 1; i >= 0; i--) {
			lasers.get(i).update();
			if (lasers.get(i).isDead())
				lasers.remove(i);
		}
		for (int i = asteroids.size() - 1; i >= 0; i--) {
			asteroids.get(i).update(p, lasers, asteroids, i, particles);
		}
		if (asteroids.size() == 0)
			resetLevel();
		else if (p.getNumLives() < 0)
			gsm.setGameState(gsm.getCurrentGameState(), new GameOverState(gsm, p.getScore()));
		
		particles.update();
	}
	
	private void resetLevel() {
		p.setPos(new Vec2(200, 200));
		p.setVel(new Vec2(0, 0));
		p.addToScore(25);
		lasers = new ArrayList<Laser>();
		asteroids = new ArrayList<Asteroid>();
		for (int i = 0; i < 4; i++) {
			Vec2 asteroidPos = new Vec2(800 * Math.random() - 200, 800 * Math.random() - 100);
			if (asteroidPos.x > 0 && asteroidPos.x < 400)
				asteroidPos = new Vec2(200 * Math.random() + (asteroidPos.x > 200 ? 400 : -400), asteroidPos.y);
			if (asteroidPos.y > 0 && asteroidPos.y < 400)
				asteroidPos = new Vec2(asteroidPos.x, 100 * Math.random() + (asteroidPos.y > 200 ? 400 : -400));
			asteroids.add(new Asteroid(asteroidPos, Asteroid.Size.LARGE));
		}
	}
}
