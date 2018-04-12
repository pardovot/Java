import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Window extends JFrame {

	private static final long serialVersionUID = 1507712322422586032L;

	public Window(Game game) {
		JFrame frame = new JFrame("Pong");
		frame.setSize(Game.WIDTH, Game.HEIGHT);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.add(game);
	}
}