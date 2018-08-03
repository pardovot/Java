package com.asteroids.controller;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import com.asteroids.model.*;
import com.asteroids.view.*;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -8921419424614180143L;
	public static final int WIDTH = 1152, HEIGHT = WIDTH / 8 * 5;

	private Thread thread;
	private boolean isRunning;
	LoadImages loadImages = new LoadImages();
	private Player player = new Player();
	private AllObjects objects;
	private KeyInput keyInput;
	private long delay = 80;
	private long currentTime = System.currentTimeMillis();
	private long expectedTime = currentTime + delay;
	public static BufferedImage test;
	private int fps = 0;

	public Game() {
		new Window(WIDTH, HEIGHT, "Asteroids!", this);
		objects = new AllObjects();
		objects.addObject(player);
		for (int i = 0; i < 30; i++) {
			objects.addObject(new Rock((int) (Math.random() * (Game.WIDTH - 64) + 1),
					(int) (Math.random() * (Game.HEIGHT - 64) + 1)));
		}
		keyInput = new KeyInput(player);
		this.addKeyListener(keyInput);
	}

	public void run() {

		int ups = 144;
		int fps = 144;

		long initialTime = System.nanoTime();
		final double timeU = 1000000000 / ups;
		final double timeF = 1000000000 / fps;
		double deltaU = 0, deltaF = 0;
		int frames = 0, ticks = 0;
		long timer = System.currentTimeMillis();

		while (isRunning) {
			destroyBullets();
			destroyAsteroids();
			// used to set delay between every bullet(milliseconds)
			currentTime = System.currentTimeMillis();
			if (KeyInput.shoot && currentTime >= expectedTime) {

				// calculates the accurate position of the x,y on the "circumference" of the
				// player
				float matchedX = player.getX() + 1 + (float) ((player.getRadius() + 32) * Math.cos(player.getRadian()));
				float matchedY = player.getY() - 7 + (float) ((player.getRadius() + 32) * Math.sin(player.getRadian()));
				objects.addObject(new Bullet(matchedX, matchedY, player));
				expectedTime = currentTime + delay;
			}
			long currentTime = System.nanoTime();
			deltaU += (currentTime - initialTime) / timeU;
			deltaF += (currentTime - initialTime) / timeF;
			initialTime = currentTime;

			if (deltaU >= 1) {
				tick();
				deltaU--;
				ticks++;
			}

			if (deltaF >= 1) {
				render();
				deltaF--;
				frames++;
			}

			if (System.currentTimeMillis() - timer > 1000) {
				this.fps = frames;
				System.out.println("UPS: " + ticks + ", FPS: " + frames);
				frames = 0;
				ticks = 0;
				timer += 1000;
			}
		}

		stop();
		System.exit(1);

	}

	private void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);

	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(LoadImages.getbackground(), 0, 0, getWidth(), getHeight(), this);
		objects.render(g);
		player.render(g);
		g.setColor(Color.red);
		g.drawString("FPS: " + fps, 5, 10);
		g.dispose();
		bs.show();

	}

	private void tick() {
		player.tick();
		objects.tick();
	}

	// starting thread and game loop.
	public void start() {
		thread = new Thread(this);
		thread.start();
		isRunning = true;
	}

	// minimum and maximum possible position for object.
	public static float Bounds(float value, float min, float max) {
		if (value >= max) {
			return value = max;
		}
		if (value <= min) {
			return value = min;
		} else {
			return value;
		}

	}

	// detects collision between two objects
	public boolean collision(GameObject a, GameObject b) {
		return (b.getX() - a.getX() + 10) * (b.getX() - a.getX() + 10)
				+ (b.getY() - a.getY() + 10) * (b.getY() - a.getY() + 10) < (a.getRadius() + b.getRadius())
						* (a.getRadius() + b.getRadius());
	}

	// destroys bullets once they go out of the screen
	public void destroyBullets() {
		for (int i = 0; i < objects.getSize(); i++) {
			if (objects.get(i).getId() == ID.BULLET) {
				GameObject bullet = objects.get(i);
				if (bullet.getX() > Game.WIDTH || bullet.getX() < 0 || bullet.getY() > Game.HEIGHT
						|| bullet.getY() < 0) {
					objects.removeObject(bullet);
				}
			}
		}
	}

	// whenever a collision between an asteroid and a bullet occurs, the asteroid is
	// destroyed
	public void destroyAsteroids() {
		GameObject bullet = null;
		GameObject bigRock = null;
		for (int i = 0; i < objects.getSize(); i++) {
			if (objects.get(i).getId() == ID.BULLET) {
				bullet = (Bullet) objects.get(i);
				for (int q = 0; q < objects.getSize(); q++) {
					if (objects.get(q).getId() == ID.BIGROCK) {
						bigRock = objects.get(q);
						if (bullet != null && bigRock != null) {
							if (collision(bigRock, bullet)) {
								objects.addObject(new Explosion(bigRock.getX(), bigRock.getY(), objects));
								objects.removeObject(bigRock);
								objects.removeObject(bullet);
							}
						}
					}
				}
			}
		}
	}
}
