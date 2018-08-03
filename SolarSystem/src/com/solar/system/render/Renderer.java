package com.solar.system.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.Scanner;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Renderer {
	private static Scanner scanner = new Scanner(System.in);

	public static void initDisplay() {
		String result = "";
		Display.setTitle("Solar System");
		while (true) {
			System.out.println("Type 'Yes' to start fullscreen, 'No' otherwise");
			result = scanner.nextLine();
			if (result.toLowerCase().equals("yes") || result.toLowerCase().equals("no")) {
				break;
			}
		}
		try {
			if (result.toLowerCase().equals("yes")) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				for (DisplayMode display : modes) {
					if (display.isFullscreenCapable()) {
						if (display.getWidth() == 1920) {
							Display.setDisplayMode(display);
							Display.setFullscreen(true);
						}
					}
				}
			} else {
				Display.setDisplayMode(new DisplayMode(1200, 1200 / 8 * 5));
			}
			Display.create();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void initGL() {
		glClearColor(0, 0, 0, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
		glLoadIdentity();
	}

	public static void cleanUp() {
		Display.destroy();
	}
}
