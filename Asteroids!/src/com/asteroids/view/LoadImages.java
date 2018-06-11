package com.asteroids.view;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class LoadImages {

	private static BufferedImage bullet;
	private static BufferedImage explosion;
	private static BufferedImage bigRock;
	private static BufferedImage smallRock;
	private static BufferedImage player;
	private static BufferedImage background;

	public LoadImages() {
		try {
			background = ImageIO.read(new File("res/background.jpg"));
			bullet = ImageIO.read(new File("res/fire_blue.png"));
			explosion = ImageIO.read(new File("res/explosions/type_C.png"));
			bigRock = ImageIO.read(new File("res/rock.png"));
//			smallRock = 
			player = ImageIO.read(new File("res/spaceship.png"));
		} catch (Exception e) {
		}
	}
	
	public static BufferedImage getBullet() {
		return bullet;
	}
	
	public static BufferedImage getExplosion() {
		return explosion;
	}
	
	public static BufferedImage getBigRock() {
		return bigRock;
	}
	
	public static BufferedImage getSmallRock() {
		return smallRock;
	}
	
	public static BufferedImage getPlayer() {
		return player;
	}
	
	public static BufferedImage getbackground() {
		return background;
	}
}
