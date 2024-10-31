package dtcc.itn262;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class PlaneController {
	final int SPEED = 10;
	JFrame frame = new JFrame();
	JPanel panel = new JPanel();
	ImageIcon plane1 = new ImageIcon("sprites/plane1.png");
	ImageIcon plane2 = new ImageIcon("sprites/plane2.png");
	ImageIcon birds = new ImageIcon("sprites/birds.png");
	JLabel plane;
	int x = 150;
	int y = 205;
	int score = 0;
	int[] xyBird = new int[]{0,0}; // x=0 and y=200
	int boost = 0;  // hit key to increase boost and stuff maybe items?

	public PlaneController(int i) {
		if (i == 1) this.plane = new JLabel(this.plane1);
		else this.plane = new JLabel(this.plane2);
		final JLabel lblBird = new JLabel(this.birds);
		// final JLabel lblScore=new JLabel("Score: "+this.score); <-- this?
		final JLabel lblScore = new JLabel();
		lblScore.setText(Integer.toString(this.score));
		this.frame.setSize(450, 400);
		this.panel.add(this.plane);
		this.panel.add(lblBird);
		this.panel.add(lblScore);
		this.panel.setLayout((LayoutManager) null);
		this.frame.add(this.panel);
		this.panel.setBackground(Color.cyan);
		this.plane.setBounds(this.x, this.y, 100, 100); // place plane and make it 100 by 100
		lblBird.setBounds(this.xyBird[0], this.xyBird[1], 50, 50); // place birds and make it 50 by 50
		lblScore.setBounds(0, 0, 200, 20); // place score at top left
		this.frame.setVisible(true);
		this.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0); //close window
			}
		});
		//add in out keypress listeners for left and right directions

		this.frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent key) {
				int keyCode = key.getKeyCode();
				PlaneController pc = PlaneController.this;

				if (keyCode == KeyEvent.VK_LEFT) {
					if (pc.x > 0) pc.x -= 10 + pc.boost;
					pc.plane.setBounds(pc.x, pc.y, 100, 100);
				}
				else if (keyCode == KeyEvent.VK_RIGHT) {
					if (pc.x < 350) pc.x += 10 + pc.boost;
					else pc.x = 0;
					pc.plane.setBounds(pc.x, pc.y, 100, 100);
				}
				else if (keyCode == KeyEvent.VK_UP) {
					if (pc.y > 0) pc.y -= 10 + pc.boost;
					pc.plane.setBounds(pc.x, pc.y, 100, 100);
				}
				else if (keyCode == KeyEvent.VK_DOWN) {
					if (pc.y < 300) pc.y += 10 + pc.boost;
					pc.plane.setBounds(pc.x, pc.y, 100, 100);
				}
				if (keyCode == KeyEvent.VK_SPACE) {
					if (Math.abs(pc.x - xyBird[0]) < 50 && Math.abs(pc.y - xyBird[1]) < 50) {
						score++;
						lblScore.setText("Score: " + score);
						Random rnd = new Random();
						xyBird[0] = rnd.nextInt(300);
						xyBird[1] = 0;
						lblBird.setBounds(xyBird[0], xyBird[1], 50, 50);
					}
					System.out.println("Space bar pressed");
				}
				if (keyCode == KeyEvent.VK_ESCAPE) {
					pc.exitGame();
				}
			}
		});

		final int[] round = {0};
		// Timer for moving the bird and checking game over
		Timer timer = new Timer(60, e -> {
			if (round[0] > 3) {
				lblScore.setText("Game Over! Score: " + score);
				((Timer) e.getSource()).stop(); // Stop the timer
			} else {
				xyBird[1] += 2; // Move bird down by 2 pixels each tick
				lblBird.setBounds(xyBird[0], xyBird[1], 50, 50);

				if (xyBird[1] > 400) { // Reset bird position when it reaches bottom
					xyBird[1] = 0;
					xyBird[0] = new Random().nextInt(300);
					round[0]++;
				}
			}
		});
		timer.start(); // Start the bird movement timer
	}


	private void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void exitGame() {
		System.exit(0);
	}
	private void playOhYeah() {    // Method to play the plane takeoff sound
		try {
			// Load the sound file
			File soundFile = new File("src/main/resources/ohyeah.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

			// Get a sound clip resource
			Clip clip = AudioSystem.getClip();

			// Open the audio stream and start playing it
			clip.open(audioStream);
			clip.start();

			Thread.sleep(clip.getMicrosecondLength() / 1000);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
