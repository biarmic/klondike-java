
public enum Suit {
	Club("club"), Spade("spade"), Diamond("diamond"), Heart("heart");
	private String name;
	private Suit(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public boolean isSameColor(Suit suit) {
		if(this==Club && (suit==Spade  || suit==Club))
			return true;
		if(this==Spade && (suit==Club || suit==Spade))
			return true;
		if(this==Diamond && (suit==Heart || suit==Diamond))
			return true;
		if(this==Heart && (suit==Diamond || suit == Heart))
			return true;
		return false;
	}
}
