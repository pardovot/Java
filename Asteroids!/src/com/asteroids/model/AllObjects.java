package com.asteroids.model;

import java.awt.Graphics;
import java.util.LinkedList;

public class AllObjects {
	public LinkedList<GameObject> objects = new LinkedList<>();

	public void tick() {
		for (GameObject gameObject : objects) {
			gameObject.tick();
		}
	}

	public void render(Graphics g) {
		try {
			for (GameObject gameObject : objects) {
				gameObject.render(g);
			}
		} catch (Exception e) {
		}
	}

	public void addObject(GameObject object) {
		objects.add(object);
	}

	public void removeObject(GameObject object) {
		objects.remove(object);
	}

	public int getSize() {
		return objects.size();
	}

	public GameObject get(int i) {
		return objects.get(i);
	}

	public LinkedList<GameObject> link() {
		return objects;
	}

}
