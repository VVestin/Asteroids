package in.vvest.gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import in.vvest.game.GameStateManager;
import in.vvest.objects.Asteroid;
import in.vvest.objects.Laser;
import in.vvest.objects.Player;
import in.vvest.objects.Vec2;
import in.vvest.particles.ParticleSystem;

public class MainMenuState extends GameState {

	private Player p;
	private List<Laser> lasers;
	private List<Asteroid> asteroids;
	private ParticleSystem particles;
	private long lastFire;
	private long transitionTime;
	
	public MainMenuState(GameStateManager gsm) {
		super(gsm);
		p = new Player(0);
		p.setPos(new Vec2(50, 230));
		lasers = new ArrayList<Laser>();
		asteroids = new ArrayList<Asteroid>();
		asteroids.add(new Asteroid(new Vec2(345, 230), Asteroid.Size.LARGE));
		asteroids.get(0).setVel(new Vec2(0, 0));
		particles = new ParticleSystem();
		transitionTime = 0;
	}

	public void draw(Graphics g) {
		Font oldFont = g.getFont();
		g.setFont(oldFont.deriveFont(50f));
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.WHITE);
		int titleBarX = (400 - fm.stringWidth("Asteroids")) / 2;
		g.drawString("Asteroids", titleBarX, 65);
		g.setFont(oldFont.deriveFont(15f));
		g.drawString("Use [A] [D] to rotate", titleBarX + 15, 100);
		g.drawString("Use [W] to thrust", titleBarX + 15, 125);
		g.drawString("Use [SPACE] to fire lasers", titleBarX + 15, 150);
		fm = g.getFontMetrics();
		if (System.currentTimeMillis() / 650 % 2 != 0)
		g.drawString("<Destroy the asteroid to continue>", (400 - fm.stringWidth("<Destroy the asteroid to continue>")) / 2, 390);
		
		p.draw(g);
		for (int i = 0; i < lasers.size(); i++) {
			lasers.get(i).draw(g);
		}
		for (int i = asteroids.size() - 1; i >= 0; i--) {
			asteroids.get(i).draw(g);
		}
		particles.draw(g);
	}

	public void update() {
		p.update(keyState);
		particles.update();
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
		if (transitionTime != 0) {
			if (System.currentTimeMillis() >= transitionTime)
				gsm.setGameState(gsm.getCurrentGameState(), new CountDownState(gsm, 3, new PlayState(gsm)));
		} else if (asteroids.size() > 1) {
			transitionTime = System.currentTimeMillis() + 650;
			asteroids.clear();
		}
	}

}
