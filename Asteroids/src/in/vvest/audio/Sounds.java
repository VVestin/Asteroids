package in.vvest.audio;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public enum Sounds {

	LASER("res/laser.wav"), PLAYER_EXPLOSION("res/player_explosion.wav"), ASTEROID_EXPLOSION("res/asteroid_explosion.wav");
	
	private Clip clip;
	
	private Sounds(String path) {
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(path)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		clip.setFramePosition(0);
		clip.start();
	}	
}
