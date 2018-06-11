package com.asteroids.model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.asteroids.controller.*;
import com.asteroids.view.LoadImages;

public class Bullet extends GameObject {

	private float frame = 0;
	private float animSpeed = 0.04f;
	private int frameCount = 16;
	private BufferedImage bullet;
	private float fixedDegree;

	public Bullet(float x, float y, Player player) {
		super(x, y, ID.BULLET, 32);
		velX = (float) Math.cos(player.getRadian()) * 2.4f;
		velY = (float) Math.sin(player.getRadian()) * 2.4f;
		fixedDegree = player.getDegree();
	}

	public void tick() {
		x += velX;
		y += velY;
	}

	public void render(Graphics g) {
		frame += animSpeed;
		if (frame > frameCount)
			frame -= frameCount;
		bullet = LoadImages.getBullet().getSubimage((int) frame * 32, 0, 32, 52);
		AffineTransform at = at(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bullet, at, null);
	}

	public AffineTransform at(Graphics g) {
		AffineTransform at = AffineTransform.getTranslateInstance(x, y);
		at.rotate(Math.toRadians(fixedDegree), bullet.getWidth() / 2, bullet.getHeight() / 2);
		return at;
	}

}
