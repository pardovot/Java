package com.solar.system.controller;

import java.io.FileInputStream;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import com.solar.system.model.AllPlanets;
import com.solar.system.model.Planet;
import com.solar.system.model.Star;
import com.solar.system.render.Renderer;
import com.solar.system.render.Camera;

public class SolarSystem {

	private Camera camera;
	private KeyInput keyInput;
	public static int mode = 0;
	public static boolean pause = false;

	private Planet sun;
	private Planet mercury;
	private Planet venus;
	private Planet earth;
	private Planet earthMoon;
	private Planet mars;
	private Planet jupiter;
	private Planet saturn;
	private Planet uranus;
	private Planet neptune;
	private Planet pluto;

	private Texture sunTexture;
	private Texture mercuryTexture;
	private Texture venusTexture;
	private Texture earthTexture;
	private Texture earthMoonTexture;
	private Texture marsTexture;
	private Texture jupiterTexture;
	private Texture saturnTexture;
	private Texture uranusTexture;
	private Texture neptuneTexture;
	private Texture plutoTexture;
	private Texture yellowStarTexture;
	private Texture redStarTexture;
	private Texture blueStarTexture;

	private AllPlanets allPlanets;
	private Star[] stars = new Star[1000];
	public static Texture[] planetTextures = new Texture[4];

	public void start() {
		init();
		gameLoop();
		Renderer.cleanUp();
	}

	public void gameLoop() {
		while (!Display.isCloseRequested()) {
			keyInput.keyPressed();
			Renderer.initGL();
			camera.useView();
			allPlanets.render();
			drawStars();
			Display.sync(144);
			Display.update();
		}
		Renderer.cleanUp();
	}

	public void init() {
		initMisc();
		initTextures();
		initPlanets();
		initPlanetList();
		initStars();
		keyInput = new KeyInput(camera, allPlanets);
	}

	public void initMisc() {
		Renderer.initDisplay();
		camera = new Camera(70f, (float) Display.getWidth() / (float) Display.getHeight(), 0.3f, 99999999999999999f);
	}

	public void initPlanets() {
		sun = new Planet("Sun", 0, 200, 0, 0, 0, 0.015f, sunTexture, true, true, false);
		mercury = new Planet("Mercury", 300, 5, 7.01f, 0.1f, 0.42f, 0.03f, mercuryTexture, true, true, false);
		venus = new Planet("Venus", 500, 15, 3.39f, 177.4f, 0.32f, 0.019f, venusTexture, true, true, false);
		earth = new Planet("Earth", 800, 18, 0, 23.45f, 0.35f, 0.11f, earthTexture, true, false, false);
		earthMoon = new Planet("EarthMoon", 120, 2, 6.68f, 5.14f, 0.26f, 0.08f, earthMoonTexture, false, true, true);
		mars = new Planet("Mars", 1100, 10, 1.85f, 25.19f, 0.11f, 0.11f, marsTexture, true, true, false);
		jupiter = new Planet("Jupiter", 1400, 45, 1.31f, 3.12f, 0.11f, 0.53f, jupiterTexture, true, true, false);
		saturn = new Planet("Saturn", 1700, 35, 2.49f, 26.73f, 0.08f, 0.259f, saturnTexture, true, true, false);
		uranus = new Planet("Uranus", 2000, 27, 0.77f, 97.86f, 0.06f, 0.15f, uranusTexture, true, true, false);
		neptune = new Planet("Neptune", 2300, 22, 1.77f, 29.56f, 0.049f, 0.169f, neptuneTexture, true, true, false);
		pluto = new Planet("Pluto", 2600, 4, -17.16f, 119.6f, 0.04f, -0.015f, plutoTexture, true, true, false);
	}

	public void initTextures() {
		try {
			sunTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/sun.jpg"));
			mercuryTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/mercury.jpg"));
			venusTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/venus.jpg"));
			earthTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/earth.jpg"));
			earthMoonTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/earthMoon.jpg"));
			marsTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/mars.jpg"));
			jupiterTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/jupiter.jpg"));
			saturnTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/saturn.jpg"));
			uranusTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/uranus.jpg"));
			neptuneTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/neptune.jpg"));
			plutoTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/pluto.jpg"));
			redStarTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/star1.png"));
			blueStarTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/star2.png"));
			yellowStarTexture = TextureLoader.getTexture("jpg", new FileInputStream("res/star3.jpg"));
			planetTextures[0] = sunTexture;
			planetTextures[1] = redStarTexture;
			planetTextures[2] = blueStarTexture;
			planetTextures[3] = yellowStarTexture;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initStars() {
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Star();
		}
	}

	public void drawStars() {
		for (int i = 0; i < stars.length; i++) {
			if (stars[i] != null) {
				stars[i].render();
			}
		}
	}

	public void initPlanetList() {
		allPlanets = new AllPlanets();
		allPlanets.add(sun);
		allPlanets.add(mercury);
		allPlanets.add(venus);
		allPlanets.add(earth);
		allPlanets.add(earthMoon);
		allPlanets.add(mars);
		allPlanets.add(jupiter);
		allPlanets.add(saturn);
		allPlanets.add(uranus);
		allPlanets.add(neptune);
		allPlanets.add(pluto);
	}
}
