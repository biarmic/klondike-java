import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Logic {
	private Board board;
	private Card[][] cards = new Card[4][13];
	private Stack<Card> stack = new Stack<Card>();
	private Stack<Card> waste = new Stack<Card>();
	private ArrayList<ArrayList<Card>> tableau = new ArrayList<ArrayList<Card>>();
	private Stack<Card> clubFoundation = new Stack<Card>();
	private Stack<Card> spadeFoundation = new Stack<Card>();
	private Stack<Card> diamondFoundation = new Stack<Card>();
	private Stack<Card> heartFoundation = new Stack<Card>();
	public Logic(Board board, Card[][] cards) {
		this.board = board;
		this.cards = cards;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 13; j++)
				stack.add(cards[i][j]);
		}
		shuffleStack();
		for(int i = 0; i < 7; i++) {
			ArrayList<Card> list = new ArrayList<Card>();
			tableau.add(list);
			for(int j = 0; j < i+1; j++) {//Choosing random cards from stack and placing on tableaux
				if(j==0)
					stack.peek().setOpen();
				list.add(stack.pop());
				list.get(j).setInStack(false);
				list.get(j).setTableau(list,i+1-j,i);
			}
		}
	}
	public ArrayList<Card> getTableau(int index) {
		return tableau.get(index);
	}
	public void shuffleStack() {
		Collections.shuffle(stack);
	}
	public void placeCards() {
		for(int i = 0; i < 7; i++) {
			ArrayList<Card> list = tableau.get(i);
			for(int j = 0; j < list.size(); j++) {
				Card a = list.get(j);
				a.setLocation(30+i*102,160+(list.size()-j)*33);
				board.setLayer(a,(list.size()-j));
			}
		}
		for(int i = 0; i < stack.size(); i++)
			board.setLayer(stack.peek(),1+stack.size()-i);
		for(Card a : stack)
			a.setLocation(30,30);
	}
	public void checkTopCards() {
		boolean gameOver = true;
		for(ArrayList<Card> list : tableau) {
			if(list.size()!=0 && !list.get(0).isOpen())
				list.get(0).setOpen();
			if(list.size()!=0)
				gameOver = false;
		}
		if(gameOver)
			board.gameOver();
	}
	public void revealFromStack() {
		waste.add(stack.pop());
		waste.peek().revealFromStack();
		board.setLayer(waste.peek(),waste.size());
	}
	public void wasteToStack() {
		int size = waste.size();
		for(int i = 0; i < size; i++) {
			stack.add(waste.pop());
			stack.peek().wasteToStack();
			board.setLayer(stack.peek(),stack.size());
		}
	}
	public void returnToWaste() {
		board.setLayer(waste.peek(),waste.size());
	}
	public void returnToFoundation(Suit suit) {
		if(suit==Suit.Club)
			board.setLayer(clubFoundation.peek(),clubFoundation.size());
		else if(suit==Suit.Diamond)
			board.setLayer(diamondFoundation.peek(),diamondFoundation.size());
		else if(suit==Suit.Spade)
			board.setLayer(spadeFoundation.peek(),spadeFoundation.size());
		else
			board.setLayer(heartFoundation.peek(),heartFoundation.size());
	}
	public void wasteToTableau() {
		waste.pop();
	}
	public void foundationToTableau(Suit suit) {
		if(suit==Suit.Club)
			clubFoundation.pop();
		else if(suit==Suit.Diamond)
			diamondFoundation.pop();
		else if(suit==Suit.Spade)
			spadeFoundation.pop();
		else
			heartFoundation.pop();
	}
	public boolean isFoundationAvailable(Suit suit, int number) {
		if(suit==Suit.Club)
			return (number==1 && clubFoundation.size()==0) || (clubFoundation.size()!=0 && number-clubFoundation.peek().getNumber()==1);
		else if(suit==Suit.Diamond)
			return (number==1 && diamondFoundation.size()==0) || (diamondFoundation.size()!=0 && number-diamondFoundation.peek().getNumber()==1);
		else if(suit==Suit.Spade)
			return (number==1 && spadeFoundation.size()==0) || (spadeFoundation.size()!=0 && number-spadeFoundation.peek().getNumber()==1);
		else
			return (number==1 && heartFoundation.size()==0) || (heartFoundation.size()!=0 && number-heartFoundation.peek().getNumber()==1);
	}
	public void moveToFoundation(Suit suit, Card card) {
		if(!card.isInWaste())
			card.getTableau().remove(card);
		else
			waste.pop();
		card.moveToFoundation();
		if(suit==Suit.Club) {
			clubFoundation.add(card);
			card.setLocation(336,30);
			board.setLayer(card,clubFoundation.size());
		}else if(suit==Suit.Diamond) {
			diamondFoundation.add(card);
			card.setLocation(438,30);
			board.setLayer(card,diamondFoundation.size());
		}else if(suit==Suit.Spade) {
			spadeFoundation.add(card);
			card.setLocation(540,30);
			board.setLayer(card,spadeFoundation.size());
		}else {
			heartFoundation.add(card);
			card.setLocation(642,30);
			board.setLayer(card,heartFoundation.size());
		}
	}
}
