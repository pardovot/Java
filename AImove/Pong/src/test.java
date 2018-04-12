import java.awt.Rectangle;

public class test {
	private static int x = 20;
	private static int y = 30;
	
	public static void main(String[] args) {
		Rectangle r1 = new Rectangle((int)x, (int)y, 100, 20);
		Rectangle r2 = new Rectangle((int)x, (int)y, 100, 20);
		
		if(r1.intersects(r2))
		{
		    //what to happen when collision occurs goes here
		}
		
	}
	
	public void test(Rectangle r1, Rectangle r2) {
		if(r1.intersects(r2))
		{
		    //what to happen when collision occurs goes here
		}
	}

	
}
