import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

public class Board extends JLayeredPane{
	private Card[][] cards = new Card[4][13];
	private JLabel stack = new JLabel();
	private JLabel clubFoundation = new JLabel();
	private JLabel spadeFoundation = new JLabel();
	private JLabel diamondFoundation = new JLabel();
	private JLabel heartFoundation = new JLabel();
	public Board() {
		try {//Stack and Foundation images
			stack.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/frame.png"))));
			clubFoundation.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/frame_club.png"))));
			diamondFoundation.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/frame_diamond.png"))));
			spadeFoundation.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/frame_spade.png"))));
			heartFoundation.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/frame_heart.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		stack.setBounds(30,30,72,100);
		clubFoundation.setBounds(336,30,72,100);
		diamondFoundation.setBounds(438,30,72,100);
		spadeFoundation.setBounds(540,30,72,100);
		heartFoundation.setBounds(642,30,72,100);
		add(stack,0);
		add(clubFoundation,0);
		add(diamondFoundation,0);
		add(spadeFoundation,0);
		add(heartFoundation,0);
		for(int i = 0; i < 4; i++) {//Preparing cards
			for(int j = 0; j < 13; j++) {
				cards[i][j] = new Card(i==0 ? Suit.Club : i==1 ? Suit.Diamond : i==2 ? Suit.Spade : Suit.Heart,j+1);
				cards[i][j].setBounds(150+i*10+j*10,150+i*10+j*10,72,100);
				add(cards[i][j],1);
			}
		}
		Logic logic = new Logic(this,cards);
		cards[0][0].setPanel(this);
		cards[0][0].setLogic(logic);
		logic.placeCards();
		stack.addMouseListener(new MouseAdapter() {//Returns cards from waste to stack
			public void mouseClicked(MouseEvent event) {
				logic.wasteToStack();
			}
		});
	}
	public void gameOver() {
		JOptionPane.showMessageDialog(null,"You won!");
		System.exit(0);
	}
}
