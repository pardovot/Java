package com.asteroids.model;

import java.awt.Graphics;
import java.awt.Image;

import com.asteroids.controller.*;
import com.asteroids.view.LoadImages;


public class Explosion extends GameObject {

	private AllObjects objects;
	private Image explosion;
	private float frame = 0;
	private float animSpeed = 0.4f;
	private int frameCount = 48;

	public Explosion(float x, float y, AllObjects objects) {
		super(x, y, ID.EXPLOSION, 1);
		this.objects = objects;
	}

	public void render(Graphics g) {
		explosion(g);
	}

	public void explosion(Graphics g) {
		frame += animSpeed;
		if (frame > frameCount) {
			frame -= frameCount;
		}
		explosion = LoadImages.getExplosion().getSubimage((int) frame * 256, 0, 256, 256);
		g.drawImage(explosion, (int) x, (int) y, 110, 110, null);
		if (frame >= 47f) {
			objects.removeObject(this);
		}
	}

	public void tick() {
		
	}

	public void setAnimSpeed(float animSpeed) {
		this.animSpeed = animSpeed;
	}
}
