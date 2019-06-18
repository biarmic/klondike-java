import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Card extends JLabel{
	private static Board panel;
	private ArrayList<Card> tableau;
	private boolean isInStack = true;
	private boolean isInWaste = false;
	private boolean isInFoundation = false;
	private CardListener listener = new CardListener();
	private Suit suit;
	private int number;
	private boolean isOpen;
	private int row;
	private int column;
	public Card(Suit suit, int number) {
		this.suit = suit;
		this.number = number;
		setClose();
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}
	public boolean isInStack() {
		return isInStack;
	}
	public void setInStack(boolean isInStack) {
		this.isInStack = isInStack;
	}
	public boolean isInWaste() {
		return isInWaste;
	}
	public boolean isInFoundation() {
		return isInFoundation;
	}
	public Suit getSuit() {
		return suit;
	}
	public int getNumber() {
		return number;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public int getRow() {
		return row;
	}
	public int getColumn() {
		return column;
	}
	public void setOpen() {
		isOpen = true;
		setImage();
	}
	public void setClose() {
		isOpen = false;
		setImage();
	}
	private void setImage() {
		try {
			setIcon(new ImageIcon(ImageIO.read(getClass().getResource(isOpen ? "images/" + suit.getName() + "_" + number + ".png" : "images/card_back.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void updateLocation(int row, int column) {
		setLocation(30+column*102,160+row*33);
		panel.setLayer(this,row);
	}
	public void updateLocation(Suit suit) {
		if(suit==Suit.Club)
			setLocation(336,30);
		else if(suit==Suit.Diamond)
			setLocation(438,30);
		else if(suit==Suit.Spade)
			setLocation(540,30);
		else
			setLocation(642,30);
	}
	public void updateLocation() {
		if(!isInStack && !isInWaste)
			updateLocation(row,column);
		else if(!isInStack) {
			setLocation(132,30);
		}
	}
	public void setPanel(Board panel) {
		Card.panel = panel;
		listener.setPanel(panel);
	}
	public void setLogic(Logic logic) {
		listener.setLogic(logic);
	}
	public ArrayList<Card> getTableau(){
		return tableau;
	}
	public void setTableau(ArrayList<Card> tableau, int row, int column) {
		this.tableau = tableau;
		this.row = row;
		this.column = column;
	}
	public void revealFromStack() {
		isInStack = false;
		isInWaste = true;
		setOpen();
		setLocation(132,30);
	}
	public void wasteToStack() {
		isInStack = true;
		isInWaste = false;
		setClose();
		setLocation(30,30);
	}
	public void wasteToTableau() {
		isInStack = false;
		isInWaste = false;
	}
	public void foundationToTableau() {
		isInFoundation = false;
	}
	public void moveToFoundation() {
		isInWaste = false;
		isInFoundation = true;
	}
}
