package dtcc.itn262;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

public class PlaneController {
	private int lives = 3;
	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();
	private JLabel lblBirds;
	private JLabel plane;
	private JLabel lblScore;
	private JLabel[] lifeIcons = new JLabel[lives];
	private int x = 150;
	private int y = 475;
	private int score = 0;
	private int[] xyBird = new int[]{0, 0}; // x=0 and y=200
	private int boost = 0;  // hit b key to increase boost
	private Clip ohYeahClip; // Declare a Clip object to hold the sound file

	public PlaneController(int planeType) {
		SoundEffects.initializeSounds();
		initializeComponents(planeType);
		setUpFrame();
		setupKeyListeners();
		startBirdMovement();
	}

	private void initializeComponents(int planeType) {
		this.plane = new JLabel(planeType == 1 ? new ImageIcon("sprites/plane1.png") : new ImageIcon("sprites/plane2.png"));
		this.lblBirds = new JLabel(new ImageIcon("sprites/birds.png"));
		this.lblScore = new JLabel("Score: " + this.score);
		this.panel.setLayout(null); // removed layout manager it was redundant
		this.panel.add(plane);
		this.panel.add(lblBirds);
		this.panel.add(lblScore);
		plane.setBounds(this.x, this.y, 100, 100); // place plane and make it 100 by 100
		lblBirds.setBounds(this.xyBird[0], this.xyBird[1], 50, 50); // place birds and make it 50 by 50
		lblScore.setBounds(0, 0, 200, 20); // place score at top left
		displayLives();
		panel.setBackground(Color.cyan);
	}

	private void setUpFrame() { // https://docs.oracle.com/javase/8/docs/api/index.html?javax/swing/JFrame.html
		this.frame.setSize(450, 600);
		this.frame.setResizable(false);
		this.frame.setIconImage(new ImageIcon("sprites/plane1.png").getImage());
		this.frame.add(this.panel);
		this.frame.setVisible(true);
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitGame();
			}
		});
	}

	private void movePlaneRight() {
		if (x < 400) x += 10 + boost;
		else x = -50;
		plane.setBounds(x, y, 100, 100);
	}

	private void movePlaneLeft() {
		if (x > -50) x -= 10 + boost;
		else x = 390;
		plane.setBounds(x, y, 100, 100);
	}

	private void movePlaneUp() {
		if (y > 100) y -= 10 + boost;
		plane.setBounds(x, y, 100, 100);
	}

	private void movePlaneDown() {
		if (y < 490) y += 10 + boost;
		plane.setBounds(x, y, 100, 100);
	}

/*	private void checkCollision() {
		if (Math.abs(x - xyBird[0]) < 50) {
			score++;
			lblScore.setText("Score: " + score);
			SoundEffects.playHitSound();
			resetBirds();
		}
	}*/


	private void setupKeyListeners() {
		this.frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent key) {
				handleKeyPress(key.getKeyCode());
			}

		/*	@Override
			public void keyReleased(KeyEvent key) {
				handleKeyRelease(key.getKeyCode());
			}*/ // not working need to fix
		});
	}

/*	private void handleKeyRelease(int keyCode) {
		if (keyCode == KeyEvent.VK_B) {
			resetBoost();
		} // fix this later
	}*/
/*private void fireBullet() {
	// Create bullet as a small JLabel
	JLabel bullet = new JLabel();
	bullet.setOpaque(true);
	bullet.setBackground(Color.RED); // Set color for visibility
	bullet.setBounds(x + 45, y - 10, 5, 10); // Initial position near the plane

	// Add bullet to the panel
	panel.add(bullet);
	panel.setComponentZOrder(bullet, 0); // Set bullet to front
	panel.repaint();

	// Move bullet upwards using a Timer
	Timer bulletTimer = new Timer(20, e -> {
		bullet.setLocation(bullet.getX(), bullet.getY() - 5); // Move bullet up by 5 pixels
		if (bullet.getY() < 0) { // Stop when bullet goes off-screen
			((Timer) e.getSource()).stop();
			panel.remove(bullet); // Remove bullet from panel
			panel.repaint();
		}
	});
	bulletTimer.start();
}*/

	private void fireBullet() {  // https://github.com/dariomnz/Galaga/tree/main/src/Game
		// Create bullet as a small JLabel
		JLabel bullet = new JLabel();
		bullet.setOpaque(true);
		bullet.setBackground(Color.RED); // Set color for visibility
		bullet.setBounds(x + 45, y - 10, 5, 10); // Initial position near the plane

		// Add bullet to the panel
		panel.add(bullet);
		panel.setComponentZOrder(bullet, 0); // Set bullet to front
		panel.repaint();

		// Move bullet upwards using a Timer and check for collision with enemy
		Timer bulletTimer = new Timer(20, e -> {
			bullet.setLocation(bullet.getX(), bullet.getY() - 5); // Move bullet up by 5 pixels

			// Check for collision with the enemy (e.g., lblBirds)
			if (bullet.getBounds().intersects(lblBirds.getBounds())) {
				// Collision detected
				score++;
				lblScore.setText("Score: " + score);
				SoundEffects.playHitSound(); // Play hit sound on collision
				resetBirds(); // Reset or respawn the enemy
				((Timer) e.getSource()).stop(); // Stop bullet movement on collision
				panel.remove(bullet); // Remove bullet from panel
				panel.repaint();
			} else if (bullet.getY() < 0) { // Stop when a bullet goes off-screen
				((Timer) e.getSource()).stop();
				panel.remove(bullet);
				panel.repaint();
			}
		});
		bulletTimer.start();
	}

	private void handleKeyPress(int keyCode) {
		switch (keyCode) {
			case KeyEvent.VK_LEFT:
				movePlaneLeft();
				break;
			case KeyEvent.VK_RIGHT:
				movePlaneRight();
				break;
			case KeyEvent.VK_UP:
				movePlaneUp();
				break;
			case KeyEvent.VK_DOWN:
				movePlaneDown();
				break;
			case KeyEvent.VK_SPACE:
				SoundEffects.playFireSound();
				fireBullet();
				//
				// checkCollision();
				break;
			case KeyEvent.VK_ESCAPE:
				exitGame();
				break;
/*			case KeyEvent.VK_B:
				boostSpeed();
				break;*/ // fix this later
			default:
				// No action needed for other keys
				break;
		}
	}

/*
	private void boostSpeed() {
		boost += 50;
	}

	private void resetBoost() {
		boost -= 50;
	}
*/

	private void displayLives() {
		for (int j = 0; j < lives; j++) {
			lifeIcons[j] = new JLabel(new ImageIcon("sprites/plane1.png")); // Use a small plane icon
			panel.add(lifeIcons[j]);
			lifeIcons[j].setBounds(10 + (j * 30), 40, 20, 20); // Position icons with spacing
		}
	}


	// Timer for moving the bird and checking game over
	private void startBirdMovement() {
		Timer timer = new Timer(60, e -> {
			if (lives <= 0) {
				lblScore.setText("Game Over! Score: " + score);
				((Timer) e.getSource()).stop(); // Stop the timer
			} else {
				moveBirdsDown();
			}
		});
		timer.start(); // Start the bird movement timer
	}

	private void moveBirdsDown() {
		xyBird[1] += 2; // Move bird down by 2 pixels each tick
		lblBirds.setBounds(xyBird[0], xyBird[1], 50, 50);
		if (xyBird[1] > 550) { // Reset bird position when it reaches bottom
			resetBirds();
			lives--; // Decrement lives when bird reaches bottom
			updateLivesDisplay();
		}
	}

	private void resetBirds() {
		Random rnd = new Random();
		xyBird[0] = rnd.nextInt(300);
		xyBird[1] = 0;
		lblBirds.setBounds(xyBird[0], xyBird[1], 50, 50);
	}

	private void updateLivesDisplay() {
		if (lives >= 0 && lives < lifeIcons.length) {
			// Remove or hide the life icon
			panel.remove(lifeIcons[lives]);
			panel.repaint(); // Refresh the panel to show changes
		}
	}

	private void exitGame() {
		System.exit(0);
	}
}
