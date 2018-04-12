import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
	public static final int WIDTH = 800, HEIGHT = WIDTH / 12 * 9;
	private static final long serialVersionUID = -8921419424614180143L;
	private Thread thread;
	private long currentTime;
	private long expectedTime;
	private int time;
	private boolean isRunning = false;
	private Handler handler = new Handler();
	private Player player = new Player();
	public static int count = 1;
	private basicEnemy basicEnemy = new basicEnemy(count);
	private Shape circle = new Ellipse2D.Double((int) player.getX() - 48, (int) player.getY() - 48, 130, 130);
	private Rectangle rectangle = new Rectangle((int) basicEnemy.getX(), (int) basicEnemy.getY(), 16, 16);

	public Game() {
		handler.object.add(player);
		handler.object.add(basicEnemy);
		this.addKeyListener(new KeyInput(handler));
		Window window = new Window(this);
		count++;
	}

	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		time = 5;
		currentTime = System.currentTimeMillis();
		expectedTime = currentTime + time * 1000;
		while (isRunning) {
			currentTime = System.currentTimeMillis();
			expectedTime = addEnemyEveryXTime(currentTime, expectedTime, time);
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

	public void tick() {
		handler.tick();
		AImove();
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
		isRunning = true;
	}

	public void stop() { // stops game.
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(1);
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		handler.render(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.draw(circle);
		g2d.draw(rectangle);
		g.dispose();
		bs.show();
	}

	public static float Bounds(float value, float min, float max) { // minimum and maximum values for variable.
		if (value >= max) {
			return value = max;
		}
		if (value <= min) {
			return value = min;
		} else {
			return value;
		}

	}

	public long addEnemyEveryXTime(long currentTime, long expectedTime, int time) { // adds enemy to the game every 'X'
		// seconds. Subtracts by 1 every
		// stage.
		if (expectedTime - currentTime <= 10) {
			basicEnemy basicEnemy = new basicEnemy(count + 1);
			count++;
			handler.addObject(basicEnemy);
			expectedTime += time * 1000;
		}
		return expectedTime;
	}

	public void collision() {
		for (int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			if (tempObject.getId() == ID.basicEnemy || tempObject.getId() == ID.FollowingEnemy) {
				if (player.getBounds().intersects(tempObject.getBounds())) {
				}
			}
		}
	}

	public void AImove() {
		int num = 0;
		for (int i = 0; i < handler.object.size(); i++) {
			circle = new Ellipse2D.Double((int) player.getX() - 48, (int) player.getY() - 48, 130, 130);
			GameObject tempObject = handler.object.get(i);
			if (tempObject.getId() == ID.basicEnemy) {
				rectangle = new Rectangle((int) tempObject.getX(), (int) tempObject.getY(), 16, 16);
				num = tempObject.getNum();
			}
			if (rectangle != null && circle != null) {
				if (circle.intersects(rectangle) && tempObject.getNum() == num) {
					float diffX = (rectangle.x - player.getX() - 8);
					float diffY = (rectangle.y - player.getY() - 8);
					float distance = (float) Math.sqrt((rectangle.x - player.getX()) * (rectangle.x - player.getX())
							+ ((rectangle.y - player.getY()) * (rectangle.y - player.getY())));

					float newX = ((-1 / distance) * diffX * 10);
					float newY = ((-1 / distance) * diffY * 10);
					player.setVelX(newX);
					player.setVelY(newY);

				} else if (player.getX() != WIDTH / 2 - 32 && player.getY() != HEIGHT / 2 - 32) {
					float diffX = player.getX() - 368 + 10;
					float diffY = player.getY() - 268 + 10;
					float distance = (float) Math.sqrt((player.getX() - 368) * (player.getX() - 368)
							+ ((player.getY() - 268) * (player.getY() - 268)));

					player.setVelX((-1 / distance) * diffX * 5);
					player.setVelY((-1 / distance) * diffY * 5);
				}
			}
		}
	}

}
