import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

	private Player player;
	private AllObjects objects;
	public static boolean forwardPressed = false;
	public static boolean shoot = false;
	private long delay = 20;
	private long currentTime;
	private long expectedTime;

	public KeyInput(Player player, AllObjects objects) {
		this.player = player;
		this.objects = objects;
	}

	public synchronized void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:
			player.setRotationSpeed(player.getRotationSpeed() - 3);
			break;
		case KeyEvent.VK_RIGHT:
			player.setRotationSpeed(player.getRotationSpeed() + 3);
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
			Game.boom = true;
			break;
		}
		player.setVelY(Game.Bounds(player.getVelY(), -5, 5));
		player.setVelX(Game.Bounds(player.getVelX(), -5, 5));
		player.setRotationSpeed(Game.Bounds(player.getRotationSpeed(), -3, 3));

		if (key == KeyEvent.VK_ESCAPE)
			System.exit(1);
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_LEFT:
			player.setRotationSpeed(player.getRotationSpeed() + 3);
			break;
		case KeyEvent.VK_RIGHT:
			player.setRotationSpeed(player.getRotationSpeed() - 3);
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
		player.setVelY(Game.Bounds(player.getVelY(), -5, 5));
		player.setVelX(Game.Bounds(player.getVelX(), -5, 5));
		player.setRotationSpeed(Game.Bounds(player.getRotationSpeed(), -5, 5));
	}
	
	public void tick() {
		currentTime = System.currentTimeMillis();
		expectedTime = currentTime + 20;
	}

}
