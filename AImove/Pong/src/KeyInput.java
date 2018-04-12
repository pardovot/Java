import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 *  Main keyboard functions.
 */

public class KeyInput extends KeyAdapter {
	private Handler handler;

	public KeyInput(Handler handler) {
		this.handler = handler;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		for (int i = 0; i < handler.object.size(); i++) { // goes over all the objects and settings up the player speed.
			try {
				GameObject tempObject = handler.object.get(i);
				if (tempObject.getId() == ID.Player) {
					if (key == KeyEvent.VK_UP)
						tempObject.setVelY(tempObject.getVelY() - 5);
					if (key == KeyEvent.VK_LEFT)
						tempObject.setVelX(tempObject.getVelX() - 5);
					if (key == KeyEvent.VK_DOWN)
						tempObject.setVelY(tempObject.getVelY() + 5);
					if (key == KeyEvent.VK_RIGHT)
						tempObject.setVelX(tempObject.getVelX() + 5);
					tempObject.setVelY(Game.Bounds(tempObject.getVelY(), -5, 5));
					tempObject.setVelX(Game.Bounds(tempObject.getVelX(), -5, 5));
				}
			} catch (NullPointerException ex) {
			}
		}
		if (key == KeyEvent.VK_ESCAPE) // exits game if esc is pressed.
			System.exit(1);
		if(key == KeyEvent.VK_E) {
			handler.addObject(new basicEnemy(Game.count));
			Game.count++;
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		for (int i = 0; i < handler.object.size(); i++) { // opposite directions from the 'keyPressed' function, to make
															// the player stop on release.
			try {
				GameObject tempObject = handler.object.get(i);
				if (tempObject.getId() == ID.Player) {
					if (key == KeyEvent.VK_UP)
						tempObject.setVelY(tempObject.getVelY() + 5);
					if (key == KeyEvent.VK_LEFT)
						tempObject.setVelX(tempObject.getVelX() + 5);
					if (key == KeyEvent.VK_DOWN)
						tempObject.setVelY(tempObject.getVelY() - 5);
					if (key == KeyEvent.VK_RIGHT)
						tempObject.setVelX(tempObject.getVelX() - 5);
					tempObject.setVelY(Game.Bounds(tempObject.getVelY(), -5, 5));
					tempObject.setVelX(Game.Bounds(tempObject.getVelX(), -5, 5));
				}
			} catch (NullPointerException ex) {
			}
		}
	}
}
