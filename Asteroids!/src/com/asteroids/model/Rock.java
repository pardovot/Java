package com.asteroids.model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.asteroids.controller.*;

public class Rock extends GameObject {

	private BufferedImage bigRocks;
	private BufferedImage smallRocks;
	private Image rock;
	private float bigRockFrame = 0;
	private float animSpeed = 0.04f;
	private int bigRockFrameCount = 16;
	private float smallRockFrame = 0;
	private int smallRockFrameCount = 16;
	private boolean once = true;
	private int asteroidSpeed = 2;

	public Rock(float x, float y) {
		super(x, y, ID.BIGROCK, 25);
		try {
			bigRocks = ImageIO.read(new File("res/rock.png"));
		} catch (Exception e) {
		}
	}

	public void tick() {
		while (once) {
			if (Math.random() > 0.5) {
				velX = (float) Math.random() * -asteroidSpeed + asteroidSpeed;
			} else {
				velX = (float) Math.random() * asteroidSpeed - asteroidSpeed;
			}
			if (Math.random() > 0.5) {
				velY = (float) Math.random() * asteroidSpeed - asteroidSpeed;
			} else {
				velY = (float) Math.random() * -asteroidSpeed + asteroidSpeed;
			}
			once = false;
		}
		x += velX;
		y += velY;

		// if object goes out of screen bounds, is appears on the other side
		if (x > Game.WIDTH) x = 0; if (x < 0) x = Game.WIDTH;
		if (y > Game.HEIGHT) y = 0; if (y < 0) y = Game.HEIGHT;

		// x = Game.Bounds(x, 0, Game.WIDTH - 48);
		// y = Game.Bounds(y, 0, Game.HEIGHT - 78);
	}

	public void render(Graphics g) {
		bigRockFrame += animSpeed;
		if (bigRockFrame > bigRockFrameCount) {
			bigRockFrame -= bigRockFrameCount;
		}
		try {
			rock = bigRocks.getSubimage((int) bigRockFrame * 64, 0, 64, 64);
		} catch (Exception e) {
		}
		g.drawImage(rock, (int) x, (int) y, null);
	}

	public void renderBigRock(Graphics g) {

	}

	public void renderSmallRock(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		smallRockFrame += animSpeed;
		if (smallRockFrame > smallRockFrameCount) {
			smallRockFrame -= smallRockFrameCount;
		}
		try {
			rock = smallRocks.getSubimage((int) smallRockFrame * 65, 0, 60, 60);
		} catch (Exception e) {
		}
		g2d.drawImage(rock, 200, 200, null);
	}

	public void setAnimSpeed(float animSpeed) {
		this.animSpeed = animSpeed;
	}

	public float getAnimSpeed() {
		return animSpeed;
	}
	
}
