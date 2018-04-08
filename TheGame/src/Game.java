import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
	private GameOver gameOver = new GameOver(hud);
	private long currentTime;
	private long expectedTime;
	private int time;
	public boolean paused = false;
	private Menu menu = new Menu(this, handler, player, basicEnemy);

	// main game constructor.
	public Game() {
		Audio.loadSounds();
		Audio.getMusic("music").loop();
		handler = new Handler();
		this.addKeyListener(new KeyInput(handler, this, player, basicEnemy, followingEnemy));
		Menu menu = new Menu(this, handler, player, basicEnemy);
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
		time = 5;
		currentTime = System.currentTimeMillis();
		expectedTime = currentTime + time * 1000;
		int scoreToLevelUp = 1500;
		int scoreToLevelTemp = scoreToLevelUp;
		boolean resetPosition = false;
		boolean playLevelUp = true;
		while (isRunning) {
			if (gameState == STATE.Game && !paused) { // main running game loop.
				deathTest(time);
				resetPosition = false;
				addRandomObjects();
				currentTime = System.currentTimeMillis();
				expectedTime = addEnemyEveryXTime(currentTime, expectedTime, time);
				scoreToLevelUp = levelUpTest(scoreToLevelUp, scoreToLevelTemp, resetPosition, isWinning, time, currentTime, expectedTime);
				isWinning = testWinning(isWinning);
				objectsLoop();
				playLevelUp = true;
			}
			if (paused) { // change time values.
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
		} else if (gameState == STATE.GameOver) {
			gameOver.render(g);
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

	public void addRandomObjects() { // adds random score/health object.
		int randomNumber = (int) (Math.random() * 20000 + 1);
		if (randomNumber == 20000) {
			GoldenScore goldenScore = new GoldenScore();
			handler.addObject(goldenScore);
			Audio.getSound("addRandomItem").play();
		}
		randomNumber = (int) (Math.random() * 50000 + 1);
		if (randomNumber == 50000) {
			AddLife addLife = new AddLife();
			handler.addObject(addLife);
			Audio.getSound("addRandomItem").play();
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

	public int resetGame(int scoreToLevel) { // resets all values for a new game.
		handler.object.clear();
		handler.removeAllObjects();
		HUD.setHealth(100);
		resetPlayerPosition(true);
		return scoreToLevel = 200;
	}

	public int levelUpTest(int scoreToLevelUp, int scoreToLevelTemp, boolean resetPosition, boolean isWinning, int time, long currentTime,
			long expectedTime) { // if desired score is reached, level up.
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

	public long addEnemyEveryXTime(long currentTime, long expectedTime, int time) { // adds enemy to the game every 'X' seconds. Subtracts by 1 every stage.
		if (expectedTime - currentTime <= 10) {
			basicEnemy basicEnemy = new basicEnemy();
			handler.addObject(basicEnemy);
			expectedTime += time * 1000;
		}
		return expectedTime;
	}

	public void objectsLoop() { // main objects loop testing for collision between player and other objects. Also adding trails effect to enemies.
		try {
			for (int i = 0; i < handler.object.size(); i++) {
				handler.object.get(i).getX();
				int x = (int) (handler.object.get(i).getX() - player.getX());
				int y = (int) (handler.object.get(i).getY() - player.getY());
				if (handler.object.get(i).getId() == ID.basicEnemy) {
					if (x < 32 && x > -16 && y < 32 && y > -16) {
						HUD.setHealth((float) (HUD.getHealth() - 0.001));
					}
					BasicTrail basicTrail = new BasicTrail(handler.object.get(i).getX(), handler.object.get(i).getY(),
							Color.red, handler);
					handler.addObject(basicTrail);
				}
				try {
					if (handler.object.get(i).getId() == ID.FollowingEnemy) {
						if (x < 32 && x > -16 && y < 32 && y > -16) {
							HUD.setHealth((float) (HUD.getHealth() - 0.001));
						}
						BasicTrail basicTrail = new BasicTrail(handler.object.get(i).getX(),
								handler.object.get(i).getY(), Color.blue, handler);
						handler.addObject(basicTrail);
					}
				} catch (NullPointerException e) {
				}
				try {
					if (handler.object.get(i).getId() == ID.GoldenScore) {
						if (x < 32 && x > -16 && y < 32 && y > -16) {
							handler.object.remove(i);
							hud.goldenScore();
							Audio.getSound("takeItem").play();
						}
					}
				} catch (NullPointerException e) {
				}
				try {
					if (x < 32 && x > -16 && y < 32 && y > -16) {
						if (handler.object.get(i).getId() == ID.AddLife) {
							handler.object.remove(i);
							if (HUD.getHealth() + 30 > 100) {
								HUD.setHealth(100);
							} else {
								hud.addLife();
							}
							Audio.getSound("takeItem").play();
						}
					}
				} catch (NullPointerException e) {
				}
			}
		} catch (NullPointerException e) {
		}
	}

	public void resetPlayerPosition(boolean resetPosition) { // reset players position to center, removing all objects besides the player.
		if (resetPosition) {
			player.setVelX(0);
			player.setX(WIDTH / 2 - 32);
			player.setVelY(0);
			player.setY(HEIGHT / 2 - 32);
			handler.removeAllObjects();
		}
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
