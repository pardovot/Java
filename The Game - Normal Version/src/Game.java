import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = -1012067852768884049L;
	public static final int WIDTH = 800;
	public static final int HEIGHT = WIDTH / 12 * 9;
	private Thread thread;
	private boolean isRunning;
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
	public static boolean mute = false;
	private BufferedImage musicButton;
	private BufferedImage muteButton;
	private boolean playOnce = false;

	// main game constructor.
	public Game() {
		Audio.loadSounds();
		Audio.getMusic("music").loop();
		try {
			musicButton = ImageIO.read(new File("resources/musicButton.png")); // load player left sprite.
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			muteButton = ImageIO.read(new File("resources/muteButton.png")); // load player right
																		// sprite.
		} catch (IOException e) {
			e.printStackTrace();
		}

		handler = new Handler();
		keyInput = new KeyInput(handler, this, player, basicEnemy, followingEnemy);
		this.addKeyListener(keyInput);
		menu = new Menu(this, handler, player);
		this.addMouseListener(menu);
		new Window(WIDTH, HEIGHT, "My Game", this);
	}

	// main run method.
	public void run() {
		requestFocus();
		boolean isWinning = false;
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		time = 5; // time(in seconds) before new enemy appears.
		currentTime = System.currentTimeMillis();
		expectedTime = currentTime + (long) (time * 1000);
		int scoreToLevelUp = 1500; // first score to level, increases by 2250 every level.
		int scoreToLevelTemp = scoreToLevelUp;
		boolean resetPosition = false;
		boolean playLevelUp = true;
		while (isRunning) { // main running game loop.
			if (mute) {
				Audio.getMusic("music").pause();
				playOnce = true;
			} else if (!mute && playOnce) {
				Audio.getMusic("music").resume();
				playOnce = false;
			}
			if (gameState == STATE.Game && !paused) {
				deathTest(time);
				resetPosition = false;
				addRandomObjects();
				currentTime = System.currentTimeMillis();
				expectedTime = addEnemyEveryXTime(currentTime, expectedTime, time);
				scoreToLevelUp = levelUpTest(scoreToLevelUp, scoreToLevelTemp, resetPosition, isWinning, time,
						currentTime, expectedTime);
				isWinning = testWinning(isWinning);
				try {
					objectsLoop();
				} catch (Exception e) {
				}
				playLevelUp = true;
			}
			if (paused || gameState != STATE.Game) { // keeps time updated while game is not running
				currentTime = System.currentTimeMillis();
				expectedTime = currentTime + (long) (time * 1000);
			}
			if (gameState == STATE.GameOver && !mute) { // play game over sound.
				Audio.getSound("gameOver").play();
			}
			if (playLevelUp && gameState == STATE.LevelUp && !mute) { // play level up sound once.
				Audio.getSound("levelUp").play();
				playLevelUp = false;
			}
			if (gameState == STATE.Winning && !mute) { // game over - winning.
				Audio.getSound("Winning").play();
				handler.object.clear();
				handler.removeAllObjects();
				HUD.setHealth(100);
				resetPlayerPosition(true);
				scoreToLevelUp = scoreToLevelTemp;
			}
			long now = System.nanoTime();
			delta += (double) (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
			}
			if (isRunning) {
				render();
			}
			frames++;
			if (System.currentTimeMillis() - timer > 1000L) {
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
		if (!paused && gameState != STATE.LevelUp) {
			try {
				handler.tick();
			} catch (Exception e) {
			}
			if (gameState == STATE.Game) {
				hud.tick();
			}
		}
	}

	private void render() { // main graphics rendering method.
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		if (!mute) {
			g.drawImage(musicButton, 745, 10, null);
		} else {
			g.drawImage(muteButton, 745, 10, null);
		}
		if (gameState == STATE.Game) {
			try {
				handler.render(g);
			} catch (Exception e) {
			}
			hud.render(g);
		} else if (gameState == STATE.Menu || gameState == STATE.Help || gameState == STATE.GameOver
				|| gameState == STATE.LevelUp || gameState == STATE.Winning) {
			menu.render(g);
		}
		if (paused && gameState == STATE.Game) {
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
		} catch (Exception e) {
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

	public void addRandomObjects() { // adds life/score object randomly.
		int randomNumber = (int) (Math.random() * 20000 + 1);
		if (randomNumber == 20000) {
			GoldenScore goldenScore = new GoldenScore();
			handler.addObject(goldenScore);
			if (!mute) {
				Audio.getSound("addRandomItem").play();
			}
		}
		randomNumber = (int) (Math.random() * 50000 + 1);
		if (randomNumber == 50000) {
			AddLife addLife = new AddLife();
			handler.addObject(addLife);
			if (!mute) {
				Audio.getSound("addRandomItem").play();
			}
		}
	}

	public void deathTest(int time) { // tests if player is dead.
		if (HUD.getHealth() == 0) {
			gameState = STATE.GameOver;
			handler.object.clear();
			handler.removeAllObjects();
			HUD.setHealth(100);
			resetPlayerPosition(true);
		}
	}

	public int levelUpTest(int scoreToLevelUp, int scoreToLevelTemp, boolean resetPosition, boolean isWinning, int time,
			long currentTime, long expectedTime) { // if desired score is reached, level up.
		if (hud.getScore() >= (float) scoreToLevelUp) {
			hud.LevelUp();
			scoreToLevelUp = (int) ((double) scoreToLevelUp + (double) scoreToLevelTemp * 1.5D);
			resetPosition = true;
			HUD.setHealth((float) (HUD.getHealth() + 10D));
			testWinning(isWinning);
			handler.removeAllObjects();
			if (!isWinning) {
				gameState = STATE.LevelUp;
			}
			if (this.time > 1) {
				this.time--;
			}
			currentTime = System.currentTimeMillis();
			expectedTime = currentTime + (long) (time * 1000);
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
		}
		return expectedTime;
	}

	public void objectsLoop() { // main objects loop testing for collision between player and other objects.
		// Also adding trails effect to enemies.
		for (int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			if (tempObject.getId() == ID.basicEnemy || tempObject.getId() == ID.FollowingEnemy) {
				if (player.getBounds().intersects(tempObject.getBounds())) {
					HUD.setHealth((float) (HUD.getHealth() - 0.001));
				}
				BasicTrail basicTrail = new BasicTrail(tempObject.getX(), tempObject.getY(), tempObject.getColor(),
						handler);
				handler.addObject(basicTrail);
			}
			if (tempObject.getId() == ID.GoldenScore) {
				if (player.getBounds().intersects(tempObject.getBounds())) {
					handler.object.remove(i);
					hud.goldenScore();
					if (!mute) {
						Audio.getSound("takeItem").play();
					}
				}
			}

			if (tempObject.getId() == ID.AddLife) {
				if (player.getBounds().intersects(tempObject.getBounds())) {
					handler.object.remove(i);
					hud.addLife();
					if (!mute) {
						Audio.getSound("takeItem").play();
					}
				}
			}
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

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
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
