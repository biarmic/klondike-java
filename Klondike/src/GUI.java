import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

public class GUI extends JFrame{
	Board board;
	public GUI() {
		super("Klondike");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(744,884);
		setResizable(false);
		setLocation((int)(screenSize.width-744)/2,(int)(screenSize.height-884)/2);
		setResizable(false);
		getContentPane().setBackground(new Color(21,140,23) );
		board = new Board();
		add(board);
		setVisible(true);
	}
	public static void main(String[] args) {
		GUI gui = new GUI();
	}
}
