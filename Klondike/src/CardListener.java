import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class CardListener implements MouseListener, MouseMotionListener{
	private static Board panel;
	private static Logic logic;
	private ArrayList<Card> tableau;
	private Card source;
	private static ArrayList<Card> moving = new ArrayList<Card>();
	private int x;
	private int y;
	private boolean doubleClicked = false;
	@Override
	public void mousePressed(MouseEvent event) {
		if(event.getSource() instanceof Card) {
			setSource(event);
			if(!source.isInStack()) {//Cards in the waste or tableaux can be moved by hand
				/*
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				*/
				if(event.getClickCount()>=2 && logic.isFoundationAvailable(source.getSuit(),source.getNumber())) {
					logic.moveToFoundation(source.getSuit(),source);
					moving.clear();
					logic.checkTopCards();
					doubleClicked = true;
				}else {
					updateCoordinates(event);
					updateLayers();
				}
			}
		}
	}
	@Override
	public void mouseDragged(MouseEvent event) {
		if(source instanceof Card)
			updateCoordinates(event);
	}
	@Override
	public void mouseReleased(MouseEvent event) {
		if(!doubleClicked && source instanceof Card) {
			if(!source.isInStack()) {
				updateCoordinates(event);
				boolean returning = false;
				if(isXOnTableau()) {
					int column = findColumn();
					if(column>=0 && column<=6) {
						tableau = logic.getTableau(column);
						int row = tableau.size();
						if(isYOnTableau(row)) {
							if(tableau.size()!=0) {
								Card top = tableau.get(0);
								if(!top.getSuit().isSameColor(source.getSuit()) && top.getNumber()-source.getNumber()==1) {
									placeCard(column);
								}else
									returning = true;
							}else {
								if(source.getNumber()==13) {
									placeCard(column);
								}else
									returning = true;
							}
						}else if(column>=3 && column<=6 && y>=-20 && y<=80) {
							Suit foundationSuit = column==3 ? Suit.Club : column==4 ? Suit.Diamond : column==5 ? Suit.Spade : Suit.Heart;
							if(foundationSuit==source.getSuit() && logic.isFoundationAvailable(foundationSuit,source.getNumber())) {
								logic.moveToFoundation(foundationSuit,source);
								moving.clear();
								logic.checkTopCards();
							}else
								returning = true;
						}else
							returning = true;
					}else
						returning = false;
				}else
					returning = true;
				if(returning)
					returnCards();
			}
		}
		if(doubleClicked)
			doubleClicked = false;
	}
	@Override
	public void mouseClicked(MouseEvent event) {
		if(source.isInStack() && !source.isInWaste())
			logic.revealFromStack();
	}
	private void setSource(MouseEvent event) {
		source = (Card) event.getSource();
		if(!source.isInStack() && !source.isInWaste() && !source.isInFoundation()) {
			ArrayList<Card> list = source.getTableau();
			for(int i = list.indexOf(source); i >= 0; i--) {
				if(list.get(i).isOpen())
					moving.add(list.get(i));
				else
					break;
			}
		}else if(!source.isInStack())
			moving.add(source);
	}
	public void setPanel(Board panel) {
		CardListener.panel = panel;
	}
	public void setLogic(Logic logic) {
		CardListener.logic = logic;
	}
	private void updateCoordinates(MouseEvent event) {
		x = (int)event.getPoint().getX()+source.getX()-36;
		y = (int)event.getPoint().getY()+source.getY()-50;
		for(int i = 0; i < moving.size(); i++)
			moving.get(i).setLocation(x,y+33*i);
	}
	private boolean isXOnTableau() {
		return (x+32)%102>30;
	}
	private boolean isYOnTableau(int row) {
		return y+50-(160+row*33)>0 && y+50-(160+row*33)<100;
	}
	private int findColumn() {
		return x/102;
	}
	private void placeCard(int column) {
		if(!source.isInStack() && !source.isInWaste() && !source.isInFoundation()) {
			ArrayList<Card> list = source.getTableau();
			int size = moving.size();
			for(int i = 0; i < size; i++) {
				Card a = moving.get(i);
				list.remove(a);
				tableau.add(0,a);
				a.setTableau(tableau,tableau.size(),column);
				a.updateLocation();
			}
			logic.checkTopCards();
		}else if(!source.isInStack() && !source.isInWaste()) {
			logic.foundationToTableau(source.getSuit());
			Card a = moving.get(0);
			tableau.add(0,a);
			a.setTableau(tableau,tableau.size(),column);
			a.foundationToTableau();
			a.updateLocation();
		}else if(!source.isInStack()){
			logic.wasteToTableau();
			Card a = moving.get(0);
			tableau.add(0,a);
			a.setTableau(tableau,tableau.size(),column);
			a.wasteToTableau();
			a.updateLocation();
		}
		moving.clear();
	}
	private void updateLayers() {
		for(int i = 0; i < moving.size(); i++)
			panel.setLayer(moving.get(i),24+i);
	}
	private void returnCards() {
		if(!source.isInStack() && source.isInWaste()) {
			logic.returnToWaste();
			source.updateLocation();
		}else if(!source.isInStack() && source.isInFoundation()) {
			logic.returnToFoundation(source.getSuit());
			source.updateLocation(source.getSuit());
		}else {
			for(Card a : moving)
				a.updateLocation();
		}
		moving.clear();
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
}
