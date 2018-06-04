import java.awt.Graphics;
import java.util.LinkedList;

public class AllObjects {
	private LinkedList<GameObject> objects = new LinkedList<>();

	public void tick() {
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			object.tick();
		}
	}

	public void render(Graphics g) {
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			object.render(g);
		}
	}

	public void addObject(GameObject object) {
		objects.add(object);
	}

	public void removeObject(GameObject object) {
		objects.remove(object);
	}
	
	public int getSize() {
		return objects.size();
	}
	
	public GameObject get(int i) {
		return objects.get(i);
	}

}
