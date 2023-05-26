package test;

import scrabble_game.Board;
import scrabble_game.Tile;
import scrabble_game.Tile.Bag;
import scrabble_game.Word;

public class Milestone1Test {
	
	public static void testBag() {
		Bag b= Bag.getBag();
		Bag b1= Bag.getBag();
		if(b1!=b)
			System.out.println("your Bag in not a Singleton");
		
		int[] q0 = b.getQuantities();
		q0[0]+=1;
		int[] q1 = b.getQuantities();
		if(q0[0]!=q1[0] + 1)
			System.out.println("getQuantities did not return a clone");
		
		for(int k=0;k<9;k++) {
			int[] qs = b.getQuantities(); 
			Tile t = b.getRand();
			int i=t.letter-'A';
			int[] qs1 = b.getQuantities();		
			if(qs1[i]!=qs[i]-1)
				System.out.println("problem with getRand");
			
			b.put(t);
			b.put(t);
			b.put(t);
			
			if(b.getQuantities()[i]!=qs[i])
				System.out.println("problem with put");
		}
		
		if(b.getTile('a')!=null || b.getTile('$')!=null || b.getTile('A')==null)
			System.out.println("your getTile is wrong ");
		
	}
	
	
	
	private static Tile[] get(String s) {
		Tile[] ts=new Tile[s.length()];
		int i=0;
		for(char c: s.toCharArray()) {
			ts[i]=Bag.getBag().getTile(c);
			i++;
		}
		return ts;
	}

	public static void testBoard() {
		Board b = Board.getBoard();
		if(b!=Board.getBoard())
			System.out.println("board should be a Singleton ");
		

		Bag bag = Bag.getBag();
		Tile[] ts=new Tile[10];
		for(int i=0;i<ts.length;i++) 
			ts[i]=bag.getRand();
		
		Word w0=new Word(ts,0,6,true);
		Word w1=new Word(ts,7,6,false);
		Word w2=new Word(ts,6,7,true);
		Word w3=new Word(ts,-1,7,true);
		Word w4=new Word(ts,7,-1,false);
		Word w5=new Word(ts,0,7,true);
		Word w6=new Word(ts,7,0,false);
		if(b.boardLegal(w0) || b.boardLegal(w1) || b.boardLegal(w2) || b.boardLegal(w3) || b.boardLegal(w4) || !b.boardLegal(w5) || !b.boardLegal(w6))
			System.out.println("your boardLegal function is wrong");


		for(Tile t : ts)
			bag.put(t);
		/*System.out.println(Arrays.toString(get("HORN")));
		System.out.println(Arrays.toString(get("FA_M")));
		Word farm=new Word(get("FA_M"), 5, 7, true);
		System.out.println(Arrays.toString(farm.getTiles()).);*/


		Word horn=new Word(get("HORN"), 7, 5, false);
		if(b.tryPlaceWord(horn)!=14)
			System.out.println("problem in placeWord for 1st word");
		System.out.println("second word");
		Word farm=new Word(get("FA_M"), 5, 7, true);
		if(b.tryPlaceWord(farm)!=9)
			System.out.println("problem in placeWord for 2ed word ");

		Word paste=new Word(get("PASTE"), 9, 5, false);
		if(b.tryPlaceWord(paste)!=25)
			System.out.println("problem in placeWord for 3ed word ");

		Word mob=new Word(get("_OB"), 8, 7, false);
		if(b.tryPlaceWord(mob)!=18)
			System.out.println("problem in placeWord for 4th word ");

		Word bit=new Word(get("BIT"), 10, 4, false);
		if(b.tryPlaceWord(bit)!=22)
			System.out.println("problem in placeWord for 5th word ");
		

	}

	public static void main(String[] args) {
		testBag();
		testBoard();
		System.out.println("done");

	}

}