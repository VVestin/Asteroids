package in.vvest.particles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class ParticleSystem {

    private ArrayList<Particle> particles;

    public ParticleSystem() {
    	particles = new ArrayList<Particle>();
    }

    public void addParticleEffect(int x, int y, int w, int h, int time, float speed, Color c) {
    	particles.add(new Particle((int) (time * Math.random()), c, (float) (x + Math.random() * w), (float) (y + Math.random() * h),
			(float) (speed * (Math.random() - .5f)), (float) (speed * (Math.random() - .5f))));
    }

    public void draw(Graphics g) {
		for (int i = 0; i < particles.size(); i++) {
		    particles.get(i).draw(g);
		}
    }

    public void update() {
		Iterator<Particle> i = particles.iterator();
		while (i.hasNext()) {
		    Particle p = i.next();
		    if (p.isDead())
			i.remove();
		    else
			p.update();
		}
    }
}
