import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/* 
 *  Main game object class.
 *  all objects are inherited from the game object class.
 *  the class initialize X and Y positions, X and Y velocities, and object ID.
 */

public abstract class GameObject {
	protected float x, y;
	protected ID id;
	protected float velX, velY;
	protected Color color;
	
	public GameObject(float x, float y, ID id, Color color) {
		setX(x);
		setY(y);
		setId(id);
		setColor(color);
	}
	
    public abstract Rectangle getBounds();

	public abstract void tick();
	
	public abstract void render(Graphics g);
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public float getY() {
		return y;
	}

	public float getX() {
		return x;
	}

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public float getVelX() {
		return velX;
	}

	public void setVelX(float velX) {
		this.velX = velX;
	}

	public float getVelY() {
		return velY;
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
