package com.asteroids.view;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.asteroids.model.AllObjects;
import com.asteroids.model.Player;

public class Render extends Canvas {

	private static final long serialVersionUID = 8052570689971279227L;
	private Player player;
	private AllObjects objects;

	public Render(Player player, AllObjects objects) {
		this.player = player;
		this.objects = objects;
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(LoadImages.getbackground(), 0, 0, getWidth(), getHeight(), this);
		objects.render(g);
		player.render(g);
		g.dispose();
		bs.show();
	}
}
