package model;
public interface CacheReplacementPolicy{
	void add(String word);
	String remove();
}
