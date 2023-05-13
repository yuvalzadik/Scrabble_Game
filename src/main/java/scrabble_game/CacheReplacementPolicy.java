package scrabble_game;
public interface CacheReplacementPolicy{
	void add(String word);
	String remove();
}
