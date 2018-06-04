import java.awt.Graphics;

public abstract class GameObject {
	protected float x, y;
	protected ID id;
	protected float velX, velY;
	protected float rotate;

	public GameObject(float x, float y, ID id) {
		setX(x);
		setY(y);
		setId(id);
	}
	
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
	
}
