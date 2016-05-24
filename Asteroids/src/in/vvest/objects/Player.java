package in.vvest.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Map;

import in.vvest.audio.Sounds;

public class Player {
	private static final double TURN_AMT = 0.05;
	private static final double MAX_SPEED = 5;
	private static final double ACCELERATION = .1;
	
	private Vec2 pos;
	private Vec2 vel;
	private Polygon shape;
	private double angle;
	private boolean thrusting;
	private int score;
	private int numLives;
	private long lastDeath;
	private long lastFire;
	
	public Player(int numLives) {
		this.numLives = numLives;
		pos = new Vec2(200, 200);
		vel = new Vec2(0, 0);
		shape = new Polygon();
		shape.addPoint(10, 0);
		shape.addPoint(1, 3);
		shape.addPoint(-8, 6);
		shape.addPoint(-11, 7);
		shape.addPoint(-8, 6);
		shape.addPoint(-8, 0);
		shape.addPoint(-8, -6);
		shape.addPoint(-11, -7);
		shape.addPoint(-8, -6);
		shape.addPoint(1, -3);
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		if (!(isInvincible() && (System.currentTimeMillis() / 250) % 3 == 0) && System.currentTimeMillis() - lastDeath > 2000) {
			g2d.translate(pos.x, pos.y);
			g2d.rotate(angle);
			g2d.setColor(Color.WHITE);
			g2d.setColor(Color.BLACK);
			g2d.fill(shape);
			g2d.setColor(Color.WHITE);
			g2d.draw(shape);
			if (thrusting) {
				g2d.setColor(Color.ORANGE);
				g2d.drawLine(-8, 3, -14, 0);
				g2d.drawLine(-8, -3, -14, 0);
			}
			g2d.setTransform(old);
			g2d.setColor(Color.WHITE);
		}
		for (int i = 0; i < numLives; i++) {
			g2d.translate(20 + 20 * i, 15);
			g2d.rotate(-Math.PI / 2);
			g2d.setColor(Color.BLACK);
			g2d.fill(shape);
			g2d.setColor(Color.WHITE);
			g2d.draw(shape);
			g2d.setTransform(old);		
		}
	}
	
	public void update(Map<String, Boolean> keyState, List<Laser> lasers) {
		if (keyState.containsKey("a") && keyState.get("a"))
			angle -= TURN_AMT;
		if (keyState.containsKey("d") && keyState.get("d"))
			angle += TURN_AMT;
		
		if (keyState.containsKey("w") && keyState.get("w")) {
			if (vel.lengthSquared() <= Math.pow(MAX_SPEED, 2))
				vel = vel.add(new Vec2(angle).scale(ACCELERATION));
			thrusting = true;
		} else {
			thrusting = false;
		}
		
		pos = pos.add(vel);
		vel = vel.scale(0.99);
		
		if (pos.x < 0)
			pos = new Vec2(400, pos.y);
		else if (pos.x > 400)
			pos = new Vec2(0, pos.y);
		
		if (pos.y < 0)
			pos = new Vec2(pos.x, 400);
		else if (pos.y > 400)
			pos = new Vec2(pos.x, 0);
		if (keyState.containsKey("space") && keyState.get("space") && System.currentTimeMillis() - lastFire > 250) {
			lastFire = System.currentTimeMillis();
			lasers.add(new Laser(pos, angle));
			Sounds.LASER.play();
		}
	}
	
	public void explode() {
		Sounds.PLAYER_EXPLOSION.play();
		pos = new Vec2(200, 200);
		angle = 0;
		vel = new Vec2(0, 0);
		thrusting = false;
		numLives--;
		lastDeath = System.currentTimeMillis();
	}
	
	public Vec2 getPos() {
		return pos;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public Polygon getShape() {
		return shape;
	}
	
	public int getNumLives() {
		return numLives;
	}
	
	public int getScore() {
		return score;
	}

	public boolean isInvincible() {
		return System.currentTimeMillis() - lastDeath < 4500;
	}
	
	public void addToScore(int points) {
		score += points;
	}
	
	public void setPos(Vec2 pos) {
		this.pos = pos;
	}
	
	public void setVel(Vec2 vel) {
		this.vel = vel;
	}
}
