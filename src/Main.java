import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		for (int i = 0; i < 1000000; i++) {
			new JFrame().setVisible(true);
		}
	}
}
