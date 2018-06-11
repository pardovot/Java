package com.asteroids.model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.asteroids.controller.*;
import com.asteroids.view.LoadImages;

public class Player extends GameObject {

	// spaceship animations
	private BufferedImage normal_no_engine;
//	private Image normal_half_engine;
//	private Image normal_full_engine;
//	private Image no_engine_left;
//	private Image half_engine_left;
//	private Image full_engine_left;
//	private Image no_engine_right;
//	private Image half_engine_right;
//	private Image full_engine_right;

	// spaceship movement and rotation settings
	public float degree = 0;
	public float rotationSpeed;
	public float radian;

	public Player() {
		super(Game.WIDTH / 2 - 40, Game.HEIGHT / 2 - 39, ID.PLAYER, 20);
		normal_no_engine = LoadImages.getPlayer().getSubimage(39, 0, 40, 40);
//		try {
//			bigImage = ImageIO.read(new File("res/spaceship.png"));
//			normal_no_engine = bigImage.getSubimage(39, 0, 40, 40);
//			normal_half_engine = bigImage.getSubimage(40, 85, 36, 40);
//			normal_full_engine = bigImage.getSubimage(40, 36, 45, 45);
//			no_engine_left = bigImage.getSubimage(3, 36, 36, 36);
//			half_engine_left = bigImage.getSubimage(3, 85, 36, 40);
//			full_engine_left = bigImage.getSubimage(3, 36, 36, 46);
//			no_engine_right = bigImage.getSubimage(85, 0, 31, 36);
//			half_engine_right = bigImage.getSubimage(85, 85, 31, 56);
//			full_engine_right = bigImage.getSubimage(85, 40, 31, 46);
//
//		} catch (Exception e) {
//		}
	}

	public void tick() {
		// add velocity to x and y
		x += velX;
		y += velY;

		// adds pressed rotation speed to rotation
		degree += rotationSpeed;

		// converts image degrees to radian
		radian = (float) Math.toRadians(degree-90);

		// if degree > 360 -> back to 0
		simplifyRotation();

		// adds velocity while forward key is pressed
		if (KeyInput.forwardPressed) {

			// calculates cos(for x) and sin(for y) while converting degree to radians.
			velX += Math.cos(radian) * 0.01;
			velY += Math.sin(radian) * 0.01;

			// if not moving spaceship decelerates
		} else {
			velX *= 0.99f;
			velY *= 0.99f;
		}
		
		int maxSpeed = 4;
	    float speed = (float) Math.sqrt((velX * velX) + (velY * velY));
	    if (speed>maxSpeed) {
	    	velX *= maxSpeed/speed;
	    	velY *= maxSpeed/speed;
	    }

		// bounds the player to the screen.
		x = Game.Bounds(x, 0, Game.WIDTH - 48);
		y = Game.Bounds(y, 0, Game.HEIGHT - 78);
	}

	// renders the spaceship animation according to the situation
	public void render(Graphics g) {
		// if (velX == 0 && velY == 0) {
		// g.drawImage(normal_no_engine, (int) x, (int) y, null);
		// } else if (velX == 0 && velY < 0 && velY > -3) {
		// g.drawImage(normal_half_engine, (int) x, (int) y, null);
		// } else if (velX == 0 && velY < -2) {
		// g.drawImage(normal_full_engine, (int) x, (int) y, null);
		// } else if (velX < 0 && velY == 0) {
		// g.drawImage(no_engine_left, (int) x, (int) y, null);
		// } else if (velX > 0 && velY == 0) {
		// g.drawImage(no_engine_right, (int) x, (int) y, null);
		// } else if (velX < 0 && velX > -3 && velY > 0) {
		// g.drawImage(half_engine_right, (int) x, (int) y, null);
		// } else if (velX > 0 && velX < 3 && velY > 0) {
		// g.drawImage(half_engine_left, (int) x, (int) y, null);
		// } else if (velX > 2 && velY > 0) {
		// g.drawImage(full_engine_right, (int) x, (int) y, null);
		// } else if (velX < -2 && velY > 0) {
		// g.drawImage(full_engine_left, (int) x, (int) y, null);
		// }
		// g.drawImage(normal_no_engine, (int) x, (int) y, null);

		// gives the ability to rotate the current spaceship sprite and sets new degree
		AffineTransform at = AffineTransform.getTranslateInstance(x, y);
		at.rotate(Math.toRadians(degree), normal_no_engine.getWidth() / 2, normal_no_engine.getHeight() / 2);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(normal_no_engine, at, null);
	}

	// getters and setters
	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(float rotatation) {
		this.rotationSpeed = rotatation;
	}

	public float getRadian() {
		return radian;
	}

	public void simplifyRotation() {
		if (degree > 0) {
			if (degree > 360) {
				int divide = (int) degree / 360;
				divide *= 360;
				degree -= divide;
			}
		} else {
			if (degree < -360) {
				int divide = (int) degree / 360;
				divide *= 360;
				degree -= divide;
				degree *= -1;
			}
		}
	}

	public float getDegree() {
		return degree;
	}
}
