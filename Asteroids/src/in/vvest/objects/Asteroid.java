package in.vvest.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import in.vvest.particles.ParticleSystem;

public class Asteroid {
	private static final double MIN_ANGULAR_VELOCITY = Math.PI / 400;
	private static final double MAX_ANGULAR_VELOCITY = Math.PI / 200;

	private Vec2 pos;
	private Vec2 vel;
	private Size size;
	private Polygon shape;
	private double angle;
	private double angularVel;

	public Asteroid(Vec2 pos, Size size) {
		this.pos = pos;
		this.size = size;
		shape = size.generateShape();
		vel = new Vec2(2 * Math.random() - 1, 2 * Math.random() - 1).normalize().scale(size.speed);
		angle = 2;
		angularVel = Math.random() * (MAX_ANGULAR_VELOCITY - MIN_ANGULAR_VELOCITY) + MIN_ANGULAR_VELOCITY;
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform old = g2d.getTransform();
		g2d.translate(pos.x, pos.y);
		g2d.rotate(angle);
		g2d.setColor(Color.GRAY);
		g2d.draw(shape);
		g2d.setTransform(old);
	}

	public void update(Player p, List<Laser> lasers, List<Asteroid> asteroids, int index, ParticleSystem particles) {
		pos = pos.add(vel);
		angle += angularVel;

		if (pos.x < -size.maxRadius)
			pos = new Vec2(400 + size.maxRadius, pos.y);
		else if (pos.x > 400 + size.maxRadius)
			pos = new Vec2(-size.maxRadius, pos.y);

		if (pos.y < -size.maxRadius)
			pos = new Vec2(pos.x, 400 + size.maxRadius);
		else if (pos.y > 400 + size.maxRadius)
			pos = new Vec2(pos.x, -size.maxRadius);

		for (int i = lasers.size() - 1; i >= 0; i--) {
			Vec2 laserPos = lasers.get(i).getPos().subtract(pos).rotate(-angle);
			if (shape.contains(laserPos.asPoint())) {
				p.addToScore(size.pointValue);
				lasers.remove(i);
				asteroids.remove(index);
				asteroids.addAll(explode(particles));
				break;
			}
		}
		if (!p.isInvincible()) {
			Polygon playerShip = p.getShape();
			for (int i = 0; i < playerShip.npoints; i++) {
				Vec2 vertice = new Vec2(playerShip.xpoints[i], playerShip.ypoints[i]).rotate(-p.getAngle()).add(p.getPos()).subtract(pos).rotate(-angle);
				if (shape.contains(vertice.asPoint())) {
					for (int j = 0; j < 40; j++)
						particles.addParticleEffect((int) p.getPos().x, (int) p.getPos().y, 1, 1, 750, 2, Color.WHITE);
					p.explode();
					asteroids.remove(index);
					asteroids.addAll(explode(null));
					break;
				}

			}
		}
	}

	public void setVel(Vec2 vel) {
		this.vel = vel;
	}
	
	private List<Asteroid> explode(ParticleSystem particles) {
		for (int i = 0; i < 3 * size.pointValue && particles != null; i++)
			particles.addParticleEffect((int) pos.x, (int) pos.y, 1, 1, 300, 3, Color.GRAY);
		List<Asteroid> children = new ArrayList<Asteroid>();
		if (size != Size.SMALL) {
			Size childrenSize = size == Size.LARGE ? Size.MEDIUM : Size.SMALL;
			for (int j = 0; j < 2 + (int) (Math.random() * 3); j++) {
				double randAngle = Math.random() * Math.PI * 2;
				children.add(new Asteroid(pos.add(
						new Vec2(size.minRadius * Math.cos(randAngle), size.minRadius * Math.sin(randAngle)).scale(.5)),
						childrenSize));
			}
		}
		return children;
	}

	public static enum Size {
		LARGE(50, 30, 10, .6, 10), MEDIUM(20, 10, 7, .8, 5), SMALL(10, 5, 6, 1.2, 1);

		private int maxRadius, minRadius, numPoints, pointValue;
		private double speed;

		private Size(int maxRadius, int minRadius, int numPoints, double speed, int pointValue) {
			this.maxRadius = maxRadius;
			this.minRadius = minRadius;
			this.numPoints = numPoints;
			this.pointValue = pointValue;
			this.speed = speed;
		}

		public Polygon generateShape() {
			Polygon p = new Polygon();
			for (int i = 0; i < numPoints; i++) {
				double angle = ((double) i / numPoints) * 2 * Math.PI;
				double length = (Math.random() * (maxRadius - minRadius)) + minRadius;
				p.addPoint((int) (length * Math.cos(angle)), (int) (length * Math.sin(angle)));
			}
			return p;
		}
	}

}
