package dtcc.itn262;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
	int[] xyBird = new int[]{0, 200}; // x=0 and y=200
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
				if (keyCode == KeyEvent.VK_LEFT) { // left arrow key
					if (pc.x > 0)
						pc.x -= 10 + pc.boost;

					pc.plane.setBounds(pc.x, pc.y, 100, 100);

				} else if (keyCode == KeyEvent.VK_RIGHT) { // right arrow key
					if (pc.x < 350)
						pc.x += 10 + pc.boost;
					else
						pc.x = 0;
					pc.plane.setBounds(pc.x, pc.y, 100, 100);
				}  // Up arrow key
				else if (keyCode == KeyEvent.VK_UP) {
					if (pc.y > 0) pc.y -= 10 + pc.boost; // Adjust upward position
					pc.plane.setBounds(pc.x, pc.y, 100, 100);
				}
				// Down arrow key
				else if (keyCode == KeyEvent.VK_DOWN) {
					if (pc.y < 300) pc.y += 10 + pc.boost; // Adjust downward position
					pc.plane.setBounds(pc.x, pc.y, 100, 100);
				}
				if (keyCode == 32) { // space bar
					if (Math.abs(pc.x - xyBird[0]) < 50 && Math.abs(pc.y - xyBird[1]) < 50)
					{
						score++;
						lblScore.setText("Score: " + score);
						Random rnd = new Random();
						xyBird[0] = rnd.nextInt(1, 300);
						xyBird[1] = 0;
					}
					System.out.println("Space bar pressed");
				}


				if (keyCode == 27) { // escape key
					pc.exitGame();
				}
			}
		});

		int round = 0;
		for (int y = 0; y < 400; ++y) {
			lblBird.setBounds(this.xyBird[0], y, 50, 50);
			sleep(20);  // make function
			if (y > 398) {  // if the birds are at the bottom then tick off a rounbd and randomize new position at top
				Random rnd = new Random();
				y = 0; // the loop will go on forever until game is exited
				this.xyBird[0] = rnd.nextInt(1, 300); // randomly places birds
				round++;
				if (round > 3) {
					lblScore.setText("Game Over! Score: " + this.score);
				}
			}
		}
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

}
