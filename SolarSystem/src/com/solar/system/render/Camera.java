package com.solar.system.render;

import static org.lwjgl.opengl.GL11.*;

public class Camera {
	private float x;
	private float y;
	private float z;
	private float rx;
	private float ry;
	private float rz;
	private float ru;
	private float rs;

	private float fov;
	private float aspect;
	private float near;
	private float far;

	public Camera(float fov, float aspect, float near, float far) {
		x = 575;
		y = 0;
		z = -1094;
		rx = 0;
		ry = 31;
		rz = 0;
		ru = -86;
		rs = 100000;

		this.fov = fov;
		this.aspect = aspect;
		this.near = near;
		this.far = far;
		initProjection();
	}

	private void initProjection() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, aspect, near, far);

		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
	}

	public void useView() {
		glRotatef(rx, 1, 0, 0);
		glRotatef(ry, 0, 1, 0);
		glRotatef(rz, 0, 0, 1);
		glTranslatef(x, y, z);
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getRX() {
		return rx;
	}

	public float getRY() {
		return ry;
	}

	public float getRZ() {
		return rz;
	}

	public void setRX(float rx) {
		this.rx = rx;
	}

	public void setRY(float ry) {
		this.ry = ry;
	}

	public void setRZ(float rz) {
		this.rz = rz;
	}

	public void move(float amt, float dir) {
		z += amt * Math.sin(Math.toRadians(ry + 90 * dir));
		x += amt * Math.cos(Math.toRadians(ry + 90 * dir));
	}

	public void rotateY(float amt) {
		ry += amt;
	}

	public void rotateX(float amt) {
		rx += amt;
	}

	public float getRu() {
		return ru;
	}

	public void setRu(float ru) {
		this.ru = ru;
	}

	public void up(float amt) {
		ru += amt;
		glRotatef(ru, ru, 0, 0);
	}

	public void side(float amt) {
		rs += amt;
		glRotatef(0, 0, rs, rs);
	}

	public float getRs() {
		return rs;
	}

	public void setRs(float rs) {
		this.rs = rs;
	}

	public static void gluPerspective(float fovy, float aspect, float near, float far) {
		float bottom = -near * (float) Math.tan(fovy / 2);
		float top = -bottom;
		float left = aspect * bottom;
		float right = -left;
		glFrustum(left, right, bottom, top, near, far);
	}

	public void moveScreen(float amt) {
		setRZ(getRZ() + amt);
	}

	public void defSettings() {
		x = 575;
		y = 0;
		z = -1094;
		rx = 0;
		ry = 31;
		rz = 0;
		ru = -86;
		rs = 100000;
		initProjection();
	}

	public void test(float amt) {
		z += amt * Math.sin(Math.toRadians(rx));
	}
}