package com.solar.system.controller;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.solar.system.model.AllPlanets;
import com.solar.system.model.Planet;

import com.solar.system.render.Camera;

public class KeyInput {

	private float speed = 5f;
	private Camera camera;
	private AllPlanets allPlanets;
	private int interval = 200;
	private float cameraSpeedIncrease = 1.1f;
	private float cameraSpeedReduction = 0.9f;
	private float minSpeed = 0.001027754f;
	private float cameraViewVelocity = 2f;

	// Used to make intervals to avoid double presses.
	private long currentTime = System.currentTimeMillis();
	private long expectedTime = currentTime + interval;

	public KeyInput(Camera camera, AllPlanets allPlanets) {
		this.camera = camera;
		this.allPlanets = allPlanets;
	}

	public void keyPressed() {
		currentTime = System.currentTimeMillis();
		int dwheel = Mouse.getDWheel();

		if (dwheel > 0) {
			speed *= cameraSpeedIncrease;
		}

		if (dwheel < 0) {
			speed *= cameraSpeedReduction;
			if (speed <= minSpeed) {
				speed = minSpeed;
			}
		}

		// Increases stars orbital speed.
		if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
			if (currentTime >= expectedTime) {
				allPlanets.FF();
				expectedTime = currentTime + interval / 2;
			}
		}

		// Decreases stars orbital speed.
		if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
			if (currentTime >= expectedTime) {
				allPlanets.SM();
				expectedTime = currentTime + interval / 2;
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			System.exit(1);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			camera.rotateX(-cameraViewVelocity);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			camera.rotateX(cameraViewVelocity);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			camera.rotateY(-cameraViewVelocity);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			camera.rotateY(cameraViewVelocity);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			camera.move(speed, 1);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			camera.move(-speed, 1);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			camera.move(speed, 0);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			camera.move(-speed, 0);
		}

		// Pauses planets orbit around the sun, but not axis rotation.
		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			if (currentTime >= expectedTime) {
				SolarSystem.pause = !SolarSystem.pause;
				expectedTime = currentTime + interval;
			}
		}

		// Sets the planets in linear view.
		if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
			if (currentTime >= expectedTime) {
				Planet.show = !Planet.show;
				expectedTime = currentTime + interval;
			}
		}

		// Switches OpenGL model view(glBegin).
		if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
			if (currentTime >= expectedTime) {
				SolarSystem.mode++;
				if (SolarSystem.mode == 5) {
					SolarSystem.mode = 0;
				}
				expectedTime = currentTime + interval;
			}
		}
		
		// Resets camera settings.
		if(Keyboard.isKeyDown(Keyboard.KEY_R)) {
			if (currentTime >= expectedTime) {
				camera.defSettings();
				speed = 5f;
				expectedTime = currentTime + interval;
			}
		}
	}
}
