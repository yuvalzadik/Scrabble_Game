package scrabble_game;
import java.util.HashMap;

public class DictionaryManager {
    private final HashMap<String, Dictionary> Books = new HashMap<>();
    private static DictionaryManager singletone = null;

    /**
     * The challengequery function takes in a method and an array of strings.
     * The function checks if the last string in the array is present in any of the dictionaries specified by other strings.
     * If it is, then it returns true, else false.

     *
     * @param method Determine whether the query or challenge method is used
     * @param args Pass in an array of strings
     *
     * @return A boolean value
     *
     * @docauthor Trelent
     */
    private boolean challengequery(String method, String... args) {
        boolean word_exist = false;
        String searchword = args[args.length - 1];
        Dictionary dict;
        for (int i = 0; i < args.length - 1; i++) {
            if (!(Books.containsKey(args[i]))) {
                dict = new Dictionary("src/main/resources/Dictionaries/"+args[i]);
                Books.put(args[i], dict);
            } else {
                dict = Books.get(args[i]);
            }
            if (method.equals("query")) {
                if (dict.query(searchword))
                    word_exist = true;
            } else {
                if (dict.challenge(searchword))
                    word_exist = true;
            }
        }
        return word_exist;
    }

    /**
     * The query function is used to query the database for a specific entry.
     * The user must provide the name of the table they wish to query, and then
     * specify which column they want to search in, and what value that column should have.
     * For example: &quot;query users username johndoe&quot; will return all entries in the users table where
     * username = johndoe. If no such entry exists, an error message will be printed out instead.

     *
     * @param args Pass in a variable number of arguments
     *
     * @return A boolean value
     *
     * @docauthor Trelent
     */
    public boolean query(String... args) {
        return challengequery("query", args);
    }

    /**
     * The challenge function is used to challenge a player.
     * <p>
     *
     * @param args Pass in an array of strings
     *
     * @return A boolean value
     *
     * @docauthor Trelent
     */
    public boolean challenge(String... args) {
        return challengequery("challenge", args);
    }

    /**
     * The get function is a static function that returns the singleton instance of
     * DictionaryManager. If no instance exists, it creates one and then returns it.

     * <p>
     *
     * @return The singletone instance of the class
     *
     * @docauthor Trelent
     */
    public static DictionaryManager get() {
        if (singletone == null)
            singletone = new DictionaryManager();
        return singletone;
    }

    /**
     * The getSize function returns the size of the ArrayList Books.
     * <p>
     *
     *
     * @return The size of the arraylist
     *
     * @docauthor Trelent
     */
    public int getSize() {
        return Books.size();
    }
}





