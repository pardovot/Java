package com.solar.system.model;

import static org.lwjgl.opengl.GL11.*;

import java.io.FileInputStream;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.solar.system.controller.SolarSystem;

public class Planet {

	public static boolean show = true;
	private String name;

	// Planet x, y, z(if translated, default = false).
	private float x;
	private float y;
	private float z;
	private float distance; // Planet distance from the sun.
	private float radius; // Planet radius.

	// The plane of planets according to the sun, where earth's plane being 0(also
	// called 'Ecliptic Plane').
	private float orbitalPlane;
	private float degree; // Planet axis degree.
	private float orbitSpeed = 0; // The speed the planet revolves around the sun/planet if moon.
	private float orbitVelocity;
	private float axisRotationSpeed = 0; // The speed the planet revolves around itself.
	private float axisRotationVelocity;
	private Texture texture;
	private boolean translate = false; // if x, y, z are set, translate needs to be true.
	private boolean pushMatrix;
	private boolean popMatrix;
	private boolean isMoon;
	private Texture saturnRingTexture;
	private Texture uranusRingTexture;

	public Planet(String name, float distance, float radius, float orbitalPlane, float degree, float orbitVelocity,
			float axisRotationVelocity, Texture texture, boolean pushMatrix, boolean popMatrix, boolean isMoon) {
		this.name = name;
		this.distance = distance;
		this.radius = radius;
		this.orbitalPlane = orbitalPlane;
		this.degree = degree;
		this.orbitVelocity = orbitVelocity;
		this.axisRotationVelocity = axisRotationVelocity;
		this.texture = texture;
		this.pushMatrix = pushMatrix;
		this.popMatrix = popMatrix;
		this.isMoon = isMoon;
		if (name.toLowerCase().equals("saturn") || (name.toLowerCase().equals("uranus"))) {
			try {
				saturnRingTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/saturnRing.jpg"));
				uranusRingTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/uranusRing.jpg"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		orbitSpeed += (Math.random() * 100000) + 1000;
		axisRotationSpeed += (Math.random() * 100000) + 1000;
	}

	public Planet(String name, float x, float y, float z, float distance, float radius, float orbitalPlane,
			float degree, float orbitVelocity, float axisRotationVelocity, Texture texture, boolean translate,
			boolean pushMatrix, boolean popMatrix, boolean isMoon) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.distance = distance;
		this.radius = radius;
		this.orbitalPlane = orbitalPlane;
		this.degree = degree;
		this.orbitVelocity = orbitVelocity;
		this.axisRotationVelocity = axisRotationVelocity;
		this.texture = texture;
		this.translate = translate;
		this.pushMatrix = pushMatrix;
		this.popMatrix = popMatrix;
		this.isMoon = isMoon;
		if (name.toLowerCase().equals("saturn") || (name.toLowerCase().equals("uranus"))) {
			try {
				saturnRingTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/saturnRing.jpg"));
				uranusRingTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/uranusRing.jpg"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 'Shuffles' the planets positions.
		orbitSpeed += (Math.random() * 100000) + 1000;
		axisRotationSpeed += (Math.random() * 100000) + 1000;
	}
	
	public void render() {
		glEnable(GL_TEXTURE_2D);
		if (pushMatrix) {
			glPushMatrix();
		}
		if (!isMoon) {
			glRotatef(90, 1, 0, 0);
		}

		glRotatef(orbitalPlane, 0, 1, 0);
		
		if (show) {
			glRotatef(orbitSpeed, 0, 0, orbitSpeed);
		}

		if (translate) {
			glTranslatef(x, y, z);
		}

		glTranslatef(distance, distance, 0);

		glRotatef(-degree, 1, 0, 0);
		
		glRotatef(axisRotationSpeed, 0, 0, axisRotationSpeed);
		
		if (texture != null) {
			texture.bind();
		}

		
		drawSphere(radius);
		
		if (name.toLowerCase().equals("uranus")) {
			uranusRingTexture.bind();
			glRotatef(1, 1, 0, 0);
			drawCircle(50, 30);
			glRotatef(90, 1, 0, 0);
		}

		if (name.toLowerCase().equals("saturn")) {
			saturnRingTexture.bind();
			glRotatef(1, 1, 0, 0);
			drawCircle(60, 39);
		}
		
		if (popMatrix) {
			glPopMatrix();
		}

		if (!SolarSystem.pause) {
			orbitSpeed += orbitVelocity;
		}
		axisRotationSpeed -= axisRotationVelocity;
	}

//	public void render() {
//		glEnable(GL_TEXTURE_2D);
//		if (pushMatrix) {
//			glPushMatrix();
//		}
//
//		if (!isMoon) {
//			glRotatef(90, 1, 0, 0);
//		} else {
////			glRotatef(0, 0, 0, 0);
////			glRotatef(-46.9f, 1, 0, 0);
////			glRotatef(degree, 1, 0, 0);
////			glRotatef(axisRotationSpeed, 0, 0, axisRotationSpeed);
//		}
//
//		glRotatef(orbitalPlane, 0, 1, 0);
//		
//		if (show) {
//			glRotatef(orbitSpeed, 0, 0, orbitSpeed);
//		}
//
//		// If x, y, z values are set, this will translate it to the correct position.
//		if (translate) {
//			glTranslatef(x, y, z);
//		}
//
//		glTranslatef(distance, distance, 0);
//
//		if (name.toLowerCase().equals("uranus")) {
//			glRotatef(degree, 1, 0, 0);
//			glRotatef(axisRotationSpeed, 0, 0, axisRotationSpeed);
//			uranusRingTexture.bind();
//			glRotatef(3, 1, 0, 0);
//			drawCircle(50, 30);
//		} else {
//			glRotatef(axisRotationSpeed, 0, 0, axisRotationSpeed);
//			glRotatef(degree, 1, 0, 0);
//		}
//
//		if (texture != null) {
//			texture.bind();
//		}
//
//		drawSphere(radius);
//
//		if (name.toLowerCase().equals("saturn")) {
//			saturnRingTexture.bind();
//			drawCircle(60, 39);
//		}
//		if (popMatrix) {
//			glPopMatrix();
//		}
//
//		if (!SolarSystem.pause) {
//			orbitSpeed += orbitVelocity;
//		}
//		axisRotationSpeed += axisRotationVelocity;
//		if(isMoon) {
//		}
//	}

	public void drawSphere(float radius) {
		final int[] glMode = { 5, 0, 2, 7, 1 };
		final float PI = (float) Math.PI;
		float x, y, z;
		int gradation = 60; // Amount of 'levels' for the sphere, higher = smoother.
		for (int j = 0; j < gradation; j++) {
			glBegin(glMode[SolarSystem.mode]);
			float alpha1 = (float) j / gradation * PI;
			float alpha2 = (float) (j + 1) / gradation * PI;
			for (int i = 0; i <= gradation; i++) {
				float beta = (float) i / gradation * 2.0f * PI; // Coordinate transformations.
				x = (float) (radius * Math.cos(beta) * Math.sin(alpha1)); // x = r*sin(θ)*cos(φ).
				y = (float) (radius * Math.sin(beta) * Math.sin(alpha1)); // y = r*sin(θ)*sin(φ).
				z = (float) (radius * Math.cos(alpha1)); // z = r*cos(θ).
				glTexCoord2f(beta / (2.0f * PI), alpha1 / PI); // renders the texture on the vertices.
				glVertex3f(x, y, z); // renders the vertices.
				x = (float) (radius * Math.cos(beta) * Math.sin(alpha2));
				y = (float) (radius * Math.sin(beta) * Math.sin(alpha2));
				z = (float) (radius * Math.cos(alpha2));
				glTexCoord2f(beta / (2.0f * PI), alpha2 / PI);
				glVertex3f(x, y, z);
			}
			glEnd();
		}
	}

	public void drawCircle(int size, int end) {
		final float PI = (float) Math.PI;
		if (size <= end) {
			return;
		}
		glBegin(GL_LINE_LOOP);
		{
			for (int i = 0; i <= 50; i++) {
				double angle = PI * i / 25;
				double x = Math.cos(angle);
				double y = Math.sin(angle);
				glVertex2d(x * size, y * size);
			}
			glEnd();
		}
		drawCircle(size - 2, end);
	}

	public float getOrbitVelocity() {
		return orbitVelocity;
	}

	public void setOrbitVelocity(float orbitVelocity) {
		this.orbitVelocity = orbitVelocity;
	}

	public float getAxisRotationVelocity() {
		return axisRotationVelocity;
	}

	public void setAxisRotationVelocity(float axisRotationVelocity) {
		this.axisRotationVelocity = axisRotationVelocity;
	}

	public String getName() {
		return name;
	}

}
