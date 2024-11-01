package dtcc.itn262;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundEffects {
	private static Clip ohYeahClip;
	private static Clip explosionClip;

	// Preload sound clips
	public static void initializeSounds() {
		ohYeahClip = loadClip("src/main/resources/ohyeah.wav");
		explosionClip = loadClip("src/main/resources/hugeExplosion.wav");
	}

	private static Clip loadClip(String filePath) {    // Method to play the plane takeoff sound
		try {
			// Load the sound file
			File soundFile = new File(filePath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			// Get a sound clip resource
			Clip clip = AudioSystem.getClip();
			// Open the audio stream and start playing it
			clip.open(audioStream);
			return clip;
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
			System.err.println("Error loading sound: " + e.getMessage());
			return null;
		}
	}


	// Play preloaded fire sound
	public static void playFireSound() {
		if (ohYeahClip != null) {
			ohYeahClip.setFramePosition(0);  // Rewind to the start
			ohYeahClip.start();
		}
	}

	// Play preloaded hit sound
	public static void playHitSound() {
		if (explosionClip != null) {
			explosionClip.setFramePosition(0);  // Rewind to the start
			FloatControl gainControl = (FloatControl) explosionClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-15.0f);  // Reduce volume by 10 decibels (adjust as needed)
			explosionClip.start();
		}
	}
}
