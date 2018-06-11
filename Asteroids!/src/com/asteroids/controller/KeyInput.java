package com.asteroids.controller;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.asteroids.model.*;

public class KeyInput extends KeyAdapter {

	private Player player;
	public static boolean forwardPressed = false;
	public static boolean shoot = false;
	private float rotationSpeed = 1.2f;

	public KeyInput(Player player) {
		this.player = player;
	}

	public synchronized void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:
			player.setRotationSpeed(player.getRotationSpeed() - rotationSpeed);
			break;
		case KeyEvent.VK_RIGHT:
			player.setRotationSpeed(player.getRotationSpeed() + rotationSpeed);
			break;
		case KeyEvent.VK_UP:
			forwardPressed = true;
			break;
		case KeyEvent.VK_DOWN:
			break;
		case KeyEvent.VK_SPACE:
			shoot = true;
			break;
		case KeyEvent.VK_P:
			break;
		}
//		player.setVelY(Game.Bounds(player.getVelY(), -forwardSpeed, forwardSpeed));
//		player.setVelX(Game.Bounds(player.getVelX(), -forwardSpeed, forwardSpeed));
		player.setRotationSpeed(Game.Bounds(player.getRotationSpeed(), -rotationSpeed, rotationSpeed));

		if (key == KeyEvent.VK_ESCAPE)
			System.exit(1);
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:
			player.setRotationSpeed(player.getRotationSpeed() + rotationSpeed);
			break;
		case KeyEvent.VK_RIGHT:
			player.setRotationSpeed(player.getRotationSpeed() - rotationSpeed);
			break;
		case KeyEvent.VK_UP:
			forwardPressed = false;
			break;
		case KeyEvent.VK_DOWN:
			break;
		case KeyEvent.VK_SPACE:
			shoot = false;
			break;
		}
//		player.setVelY(Game.Bounds(player.getVelY(), -forwardSpeed, forwardSpeed));
//		player.setVelX(Game.Bounds(player.getVelX(), -forwardSpeed, forwardSpeed));
		player.setRotationSpeed(Game.Bounds(player.getRotationSpeed(), -rotationSpeed, rotationSpeed));
	}

}
