package com.asteroids.model;

import java.awt.Graphics;

import com.asteroids.controller.*;


public abstract class GameObject {
	protected float x, y;
	protected ID id;
	protected float velX, velY;
	protected int radius = 1;

	public GameObject(float x, float y, ID id, int radius) {
		setX(x);
		setY(y);
		setId(id);
		setRadius(radius);
	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getY() {
		return y;
	}

	public float getX() {
		return x;
	}

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

}
