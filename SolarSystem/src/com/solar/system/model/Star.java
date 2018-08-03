package com.solar.system.model;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.Texture;
import com.solar.system.controller.SolarSystem;

public class Star {

	public static boolean show = true;
	private float distance = (float) (Math.random() * 10000000 + 1000000);
	private float radius = (float) (Math.random() * 5000 + 10);
	private float orbitalPlane = (float) Math.random() * 180.03f;
	private float degree = (float) Math.random() * 180.03f;
	private float orbitSpeed = 0;
	private Texture texture = SolarSystem.planetTextures[(int) (Math.random() * 4) + 0];

	public Star() {
		orbitSpeed += (Math.random() * 100000) + 1000;
	}

	public void render() {
		glEnable(GL_TEXTURE_2D);
		glLightf(GL_LIGHT0, GL_POSITION, 1.0f);
		glMaterialf(GL_FRONT, GL_SHININESS, 128.0f);
		glPushMatrix();

		glRotatef(90, 1, 0, 0);

		glRotatef(orbitalPlane, 0, 1, 0);

		glRotatef(orbitSpeed, 0, 0, orbitSpeed);

		glTranslatef(distance, distance, 0);

		if (texture != null) {
			texture.bind();
		}

		glRotatef(degree, 1, 0, 0);
		drawSphere(radius);

		glPopMatrix();
	}

	public void drawSphere(float radius) {
		final float PI = (float) Math.PI;
		float x, y, z;
		int gradation = 4;
		for (int j = 0; j < gradation; j++) {
			glBegin(GL_TRIANGLE_STRIP);
			float alpha1 = (float) j / gradation * PI;
			float alpha2 = (float) (j + 1) / gradation * PI;
			for (int i = 0; i <= gradation; i++) {
				float beta = (float) i / gradation * 2.0f * PI;
				x = (float) (radius * Math.cos(beta) * Math.sin(alpha1));
				y = (float) (radius * Math.sin(beta) * Math.sin(alpha1));
				z = (float) (radius * Math.cos(alpha1));
				glTexCoord2f(beta / (2.0f * PI), alpha1 / PI);
				glVertex3f(x, y, z);
				x = (float) (radius * Math.cos(beta) * Math.sin(alpha2));
				y = (float) (radius * Math.sin(beta) * Math.sin(alpha2));
				z = (float) (radius * Math.cos(alpha2));
				glTexCoord2f(beta / (2.0f * PI), alpha2 / PI);
				glVertex3f(x, y, z);
			}
			glEnd();
		}
	}
}
