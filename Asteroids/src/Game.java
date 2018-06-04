import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -8921419424614180143L;
	public static float degtorad = 0.017453f;
	public static final int WIDTH = 1152, HEIGHT = WIDTH / 8 * 5;
	public static int centerX = WIDTH / 2 - 32;
	public static int centerY = HEIGHT / 2 - 32;

	private Thread thread;
	private boolean isRunning;
	private Image img;
	private Player player = new Player();
	private AllObjects objects;
	private KeyInput keyInput;
	public static boolean boom = false;
	private double delta;
	private Image explosion;
	private long delay = 80;
	private long currentTime = System.currentTimeMillis();
	private long expectedTime = currentTime + delay;

	public Game() {
		try {
			img = ImageIO.read(new File("res/background.jpg"));
			explosion = ImageIO.read(new File("res/explosions/type_A.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		objects = new AllObjects();
		objects.addObject(player);
		for (int i = 0; i < 20; i++) {
			objects.addObject(new Rock((int) (Math.random() * (Game.WIDTH - 64) + 1),
					(int) (Math.random() * (Game.HEIGHT - 64) + 1)));
		}
		keyInput = new KeyInput(player, objects);
		this.addKeyListener(keyInput);
		new Window(WIDTH, HEIGHT, "My Game", this);
		// rock = new Rock();
	}

	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		long timePressed = 0;

		// main running game loop.
		while (isRunning) {
			timePressed = 0;
			currentTime = System.currentTimeMillis();
			while (KeyInput.shoot && currentTime >= expectedTime) {
				timePressed++;
				objects.addObject(new Bullet(player.getX(), player.getY() - 40, ID.BULLET, player));
				timePressed++;
				System.out.println(timePressed);
				expectedTime = currentTime + delay;
			}

			// if (KeyInput.shoot && currentTime >= expectedTime) {
			// long timePressed = 0;
			// objects.addObject(new Bullet(player.getX(), player.getY() - 40, ID.BULLET,
			// player));
			// timePressed++;
			// System.out.println(timePressed);
			// expectedTime = currentTime + delay;
			// }
			destroyBullets();
			for (int i = 0; i < objects.getSize(); i++) {
				if (objects.get(i).getId() == ID.BIGROCK) {
					if (isCollide(player, objects.get(i))) {
					}
				}
			}
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			if (isRunning)
				render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames);
				frames = 0;
			}
		}

		render();

		stop();
		System.exit(1);

	}

	private void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);

	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		objects.render(g);
		player.render(g);
		// makeExplosion(g, player.getX(), player.getY());
		// if (boom) {
		// currentTime = System.currentTimeMillis();
		// expectedTime = currentTime + 1000;
		// while (currentTime < expectedTime) {
		// makeExplosion(g, player.getX(), player.getY());
		// boom = false;
		// }
		// }
		// if (collision) {
		// makeExplosion(g, player.getX(), player.getY());
		//
		// collision = false;
		// }
		g.dispose();
		bs.show();

	}

	private void tick() {
		player.tick();
		objects.tick();
	}

	// starting thread and game loop.
	public void start() {
		thread = new Thread(this);
		thread.start();
		isRunning = true;
	}

	// minimum and maximum values for object.
	public static float Bounds(float value, float min, float max) {
		if (value >= max) {
			return value = max;
		}
		if (value <= min) {
			return value = min;
		} else {
			return value;
		}

	}

	public void makeExplosion(Graphics g, float x, float y) {
		Graphics2D g2d = (Graphics2D) g;
		// frame += animSpeed;
		// if (frame > frameCount) {
		// frame -= frameCount;
		// }
		// try {
		// currentImage = explosion.getSubimage((int) frame * 50, 0, 50, 50);
		// } catch (Exception e) {
		// }
		// System.out.println("Explosion");
		Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
		explosion = toolkit.getImage("res/explosions/test.gif");
		g2d.drawImage(explosion, (int) x - 36, (int) y - 36, 110, 110, null);
		// g2d.drawImage(currentImage, 300, 300, 50, 50, null);
		return;
	}

	public boolean isCollide(GameObject firstObject, GameObject secondObject) {
		float value = (secondObject.getX() - firstObject.getX() + 10) * (secondObject.getX() - firstObject.getX() + 10)
				+ (secondObject.getY() - firstObject.getY()) * (secondObject.getY() - firstObject.getY());
		float value2 = (20 + 25) * (20 + 25);
		// System.out.println(value + " " +value2);
		if (value < value2) {
			// System.out.println(value2 - value);
		}
		return (secondObject.getX() - firstObject.getX() + 10) * (secondObject.getX() - firstObject.getX() + 10)
				+ (secondObject.getY() - firstObject.getY() + 10)
						* (secondObject.getY() - firstObject.getY() + 10) < (20 + 25) * (20 + 25);
	}

	public void destroyBullets() {
		for (int i = 0; i < objects.getSize(); i++) {
			if (objects.get(i).getId() == ID.BULLET) {
				GameObject bullet = objects.get(i);
				if (bullet.getX() > Game.WIDTH || bullet.getX() < 0 || bullet.getY() > Game.HEIGHT
						|| bullet.getY() < 0) {
					objects.removeObject(bullet);
				}
			}
		}
	}
}
