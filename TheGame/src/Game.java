import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = -6112428091888191314L;

	public static final int WIDTH = 800, HEIGHT = WIDTH / 12 * 9;
	private Thread thread;
	private boolean isRunning = false;
	private Handler handler;
	private Player player = new Player();
	private basicEnemy basicEnemy = new basicEnemy();
	private FollowingEnemy followingEnemy = new FollowingEnemy(player);
	private HUD hud = new HUD();
	public STATE gameState = STATE.Menu;
	private long currentTime;
	private long expectedTime;
	private int time;
	public boolean paused = false;
	private Menu menu;
	private KeyInput keyInput;
	private Shape circle;
	private Rectangle playerRect = new Rectangle((int) player.getX(), (int) player.getY(), 32, 32);

	// main game constructor.
	public Game() {
		Audio.loadSounds();
		Audio.getMusic("music").loop();
		handler = new Handler();
		keyInput = new KeyInput(handler, this, player, basicEnemy, followingEnemy);
		this.addKeyListener(keyInput);
		menu = new Menu(this, handler, player, basicEnemy, followingEnemy);
		this.addMouseListener(menu);
		new Window(WIDTH, HEIGHT, "My Game", this);

	}

	// main run method.
	public void run() {
		this.requestFocus();
		boolean isWinning = false;
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		time = 0; // time(in seconds) before new enemy appears.
		currentTime = System.currentTimeMillis();
		expectedTime = currentTime + time * 1000;
		int scoreToLevelUp = 1500; // first score to level, increases by 2250 every level.
		int scoreToLevelTemp = scoreToLevelUp;
		boolean resetPosition = false;
		boolean playLevelUp = true;
		while (isRunning) {
			if (gameState == STATE.Game && !paused) { // main running game loop.
				allObjectsLoop();
				deathTest(time);
				resetPosition = false;
				currentTime = System.currentTimeMillis();
				expectedTime = addEnemyEveryXTime(currentTime, expectedTime, time);
				scoreToLevelUp = levelUpTest(scoreToLevelUp, scoreToLevelTemp, resetPosition, isWinning, time,
						currentTime, expectedTime);
				isWinning = testWinning(isWinning);
				playLevelUp = true;
			}
			if (paused || gameState != STATE.Game) { // keeps time updated while game is not running
				currentTime = System.currentTimeMillis();
				expectedTime = currentTime + (time * 1000);
			}
			if (gameState == STATE.GameOver) { // play game over sound.
				Audio.getSound("gameOver").play();
			}
			if (playLevelUp) {
				if (gameState == STATE.LevelUp) { // play level up sound once.
					Audio.getSound("levelUp").play();
					playLevelUp = false;
				}
			}
			if (gameState == STATE.Winning) { // game over - winning.
				Audio.getSound("Winning").play();
				handler.object.clear();
				handler.removeAllObjects();
				handler.extraObjects.clear();
				HUD.setHealth(100);
				resetPlayerPosition(true);
				scoreToLevelUp = scoreToLevelTemp;
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
			resetPlayerPosition(resetPosition); // reset players position to center.
		}
		render();
		stop();
		System.exit(1);
	}

	private void tick() { // main updating method.
		if (!paused) {
			if (gameState != STATE.LevelUp) {
				handler.tick();
				if (gameState == STATE.Game) {
					playerRect = new Rectangle((int) player.getX(), (int) player.getY(), 32, 32);
					hud.tick();
				}
			}
		}
	}

	private void render() { // main graphics rendering method.
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		if (gameState == STATE.Game) {
			handler.render(g);
			hud.render(g);
		} else if (gameState == STATE.Menu || gameState == STATE.Help || gameState == STATE.GameOver
				|| gameState == STATE.LevelUp || gameState == STATE.Winning) {
			menu.render(g);
		}
		if (paused) {
			Font font = new Font("pause", 1, 70);
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("PAUSE", 280, 295);
		}
		g.dispose();
		bs.show();
	}

	public void start() { // starting thread and game loop.
		thread = new Thread(this);
		thread.start();
		isRunning = true;
	}

	public void stop() { // stops game.
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
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

	public void deathTest(int time) { // tests if player is dead.
		if (HUD.getHealth() <= 0) {
			gameState = STATE.GameOver;
			handler.object.clear();
			handler.removeAllObjects();
			HUD.setHealth(100);
			resetPlayerPosition(true);
		}
	}

	public int resetGame(int scoreToLevel) { // resets all values for a new game.
		handler.object.clear();
		handler.removeAllObjects();
		HUD.setHealth(100);
		resetPlayerPosition(true);
		return scoreToLevel = 200;
	}

	public int levelUpTest(int scoreToLevelUp, int scoreToLevelTemp, boolean resetPosition, boolean isWinning, int time,
			long currentTime, long expectedTime) { // if desired score is reached, level up.
		if (hud.getScore() >= scoreToLevelUp) {
			hud.LevelUp();
			scoreToLevelUp += (int) scoreToLevelTemp * 1.5;
			resetPosition = true;
			HUD.setHealth((float) (HUD.getHealth() + 10));
			testWinning(isWinning);
			handler.removeAllObjects();
			if (!isWinning)
				gameState = STATE.LevelUp; // going to level up state.
			if (this.time > 1) {
				this.time--;
			}
			currentTime = System.currentTimeMillis();
			expectedTime = currentTime + time * 1000;
		}
		return scoreToLevelUp;
	}

	public boolean testWinning(boolean isWinning) { // tests if game is over - player won.
		if (hud.getLevel() == 6) {
			gameState = STATE.Winning;
			isWinning = true;
		}
		return isWinning;
	}

	public long addEnemyEveryXTime(long currentTime, long expectedTime, int time) {
		// adds enemy to the game every 'X' seconds. Subtracts by 1 second every stage.
		if (expectedTime - currentTime <= 10) {
			basicEnemy basicEnemy = new basicEnemy();
			handler.addObject(basicEnemy);
			expectedTime += time * 1000;
			System.out.println("added");
		}
		return expectedTime;
	}

	public void allObjectsLoop() {
		// main objects loop, avoids enemies, collects life and score objects,
		// reduces hp to player on touch
		GameObject nextObject = null;
		GameObject closestObject = null;
		GameObject tempObject = null;
		float x, y, x1, y1;
		for (int i = 0; i < handler.extraObjects.size() - 1; i++) {
			// determine which life/score object is the closest
			nextObject = handler.extraObjects.get(i + 1);
			tempObject = handler.extraObjects.get(i);
			if (tempObject.getId() == ID.GoldenScore || tempObject.getId() == ID.AddLife) {
				if (player.getX() > nextObject.getX()) {
					x = player.getX();
					x1 = nextObject.getX();
				} else {
					x = nextObject.getX();
					x1 = player.getX();
				}
				if (player.getY() > nextObject.getY()) {
					y = player.getY();
					y1 = nextObject.getY();
				} else {
					y = nextObject.getY();
					y1 = player.getY();
				}
				if (x < x1 || y < y1) {
					closestObject = nextObject;
				} else {
					closestObject = tempObject;
				}
			}
		}
		if (handler.extraObjects.size() == 1) { // if there's only 1 object, locks on it.
			closestObject = tempObject;
		}
		boolean isIntersects = false;
		circle = new Ellipse2D.Double((int) player.getX() - 48, (int) player.getY() - 48, 130, 130);
		for (int i = 0; i < handler.object.size(); i++) {
			// main auto movement loop, iterates through each object, and if detect
			// collision, moves away.
			isIntersects = false;
			tempObject = handler.object.get(i);
			if (tempObject.getId() == ID.basicEnemy || tempObject.getId() == ID.FollowingEnemy) {
				if (playerRect.intersects(tempObject.getBounds())) {
					HUD.setHealth((float) (HUD.getHealth() - 0.001));
					isIntersects = true;
				}
				if (circle.intersects(tempObject.getBounds())) {
					isIntersects = true;
					float diffX = (tempObject.getX() - player.getX() - 8);
					float diffY = (tempObject.getY() - player.getY() - 8);
					float distance = (float) Math
							.sqrt((tempObject.getX() - player.getX()) * (tempObject.getX() - player.getX())
									+ ((tempObject.getY() - player.getY()) * (tempObject.getY() - player.getY())));
					player.setVelX((-1 / distance) * diffX * 10);
					player.setVelY((-1 / distance) * diffY * 10);
				}
			} else if (closestObject != null && !isIntersects) {
				float diffX = player.getX() - closestObject.getX();
				float diffY = player.getY() - closestObject.getY();
				float distance = (float) Math
						.sqrt((player.getX() - closestObject.getX()) * (player.getX() - closestObject.getX())
								+ ((player.getY() - closestObject.getY()) * (player.getY() - closestObject.getY())));

				player.setVelX((-1 / distance) * diffX * 30);
				player.setVelY((-1 / distance) * diffY * 30);

			} else if (player.getX() != WIDTH / 2 - 32 && player.getY() != HEIGHT / 2 - 32) {
				float diffX = player.getX() - 378;
				float diffY = player.getY() - 278;
				float distance = (float) Math.sqrt((player.getX() - 368) * (player.getX() - 368)
						+ ((player.getY() - 268) * (player.getY() - 268)));

				player.setVelX((-1 / distance) * diffX * 5);
				player.setVelY((-1 / distance) * diffY * 5);
			}
		}
		for (int i = 0; i < handler.extraObjects.size(); i++) {
			// detects collision between player and life/score objects.
			tempObject = handler.extraObjects.get(i);
			if (tempObject.getId() == ID.GoldenScore) {
				if (playerRect.intersects(tempObject.getBounds())) {
					handler.extraObjects.remove(i);
					hud.goldenScore();
					Audio.getSound("takeItem").play();
				}
			}
			if (tempObject.getId() == ID.AddLife) {
				if (playerRect.intersects(tempObject.getBounds())) {
					handler.extraObjects.remove(i);
					hud.addLife();
					Audio.getSound("takeItem").play();
				}
			}
		}
		// adds life/score object randomly.
		int randomNumber = (int) (Math.random() * 100000 + 1);
		if (randomNumber == 1000000) {
			GoldenScore goldenScore = new GoldenScore();
			handler.addExtraObject(goldenScore);
			Audio.getSound("addRandomItem").play();
		}
		randomNumber = (int) (Math.random() * 130000 + 1);
		if (randomNumber == 130000) {
			AddLife addLife = new AddLife();
			handler.addExtraObject(addLife);
			Audio.getSound("addRandomItem").play();
		}
	}

	public void resetPlayerPosition(boolean resetPosition) { // reset players position to center, removing all objects
																// besides the player.
		if (resetPosition) {
			player.setVelX(0);
			player.setX(WIDTH / 2 - 32);
			player.setVelY(0);
			player.setY(HEIGHT / 2 - 32);
			handler.removeAllObjects();
		}
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public void setExpectedTime(long expectedTime) {
		this.expectedTime = expectedTime;
	}

	public int getTime() {
		return time;
	}

}
