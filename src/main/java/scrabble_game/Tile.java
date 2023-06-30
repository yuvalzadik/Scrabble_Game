package scrabble_game;
import java.io.*;
import java.util.Objects;
public class Tile implements Serializable{
    public final char letter ;
    public final int score ;
    /**
     * The Tile function is a constructor that creates a Tile object with the given letter and score.
     * <p>
     *
     * @param letter Set the letter of a tile
     * @param  score Assign the score of each letter
     *
     * @docauthor Trelent
     */
    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }
    /**
     * The equals function checks if the letter and score of two tiles are equal.
     * <p>
     *
     * @param o Object  Compare the current object with another object
     *
     * @return True if the letter and score of two tiles are equal
     *
     * @docauthor Trelent
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile)o;
        return letter == tile.letter && score == tile.score;
    }

    /**
     * The hashCode function is used to generate a unique hash code for each object.
     * This is useful when storing objects in data structures such as HashMaps, where
     * the hashCode of an object can be used to determine its location in the map.

     * <p>
     *
     * @return The hashcode of the letter and score
     *
     * @docauthor Trelent
     */
    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    public static class Bag implements Serializable{
        private int[] letter_amount;
        public final int[] max_letter_amount;
        private Tile[] letters_and_value;
        private int current_amount_bag =98;

        private static Bag _instance = null;
        /**
         * The Bag function is a constructor that creates an instance of the Bag class.
         * It initializes the letter_amount array to contain 9 A's, 2 B's, 2 C's, 4 D's and so on.
         * It also initializes max_letter_amount to be equal to letter_amount at this point in time.
         * The letters and values are initialized as well with each Tile object containing a character and its corresponding value.

         * <p>
         *
         * @docauthor Trelent
         */
        private Bag() {
        letter_amount =new int[] {9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};
        max_letter_amount =  letter_amount.clone() ;
        letters_and_value = new Tile[26];
        letters_and_value = new Tile[]{new Tile('A', 1), new Tile('B', 3), new Tile('C', 3), new Tile('D', 2),
                new Tile('E', 1), new Tile('F', 4), new Tile('G', 2),
                new Tile('H', 4), new Tile('I', 1), new Tile('J', 8), new Tile('K', 5), new Tile('L', 1),
                new Tile('M', 3), new Tile('N', 1), new Tile('O', 1), new Tile('P', 3), new Tile('Q', 10),
                new Tile('R', 1), new Tile('S', 1), new Tile('T', 1), new Tile('U', 1), new Tile('V', 4),
                new Tile('W', 4), new Tile('X', 8), new Tile('Y', 4), new Tile('Z', 10)};

        }

        /**
         * The getRand function is a function that returns a random tile from the bag.
         * <p>
         *
         *
         * @return A random tile from the bag
         *
         * @docauthor Trelent
         */
        public Tile getRand() {
            if (current_amount_bag ==0)
                return null;
            else {
                int min = 0 ;
                int max = 25 ;
                // randomly get number between 0 -25
                int random_number= (int)Math.floor(Math.random()*(max-min+1)+min);
                // if the letter amount in this number is 0 we will scan all the letter_amount array until we find a spot
                // that is not empty. we will find one for sure since amount bag in this stage can not be 0 .
                while (letter_amount[random_number] == 0 )
                    random_number = ((random_number +1) % letter_amount.length );
                letter_amount[random_number] -= 1;
                current_amount_bag-=1;
                return letters_and_value[random_number];
            }
        }
        /**
         * The getTile function is used to get a tile from the bag.
         * <p>
         *
         * @param letter Specify the letter that we want to get from the bag
         *
         * @return A tile from the bag
         *
         * @docauthor Trelent
         */
        public Tile getTile(char letter){
            //if (letter.matches("[A-Z]{1}"))
            if (Character.isUpperCase(letter)) {
                int location = letter - 'A';
                if (letter_amount[location] != 0){
                    letter_amount[location]--;
                    current_amount_bag-=1;
                    return letters_and_value[location];
                }
            }
            // if the input is not valid or there is not more letters on the bag of the required one.
            return  null;
        }
        /**
         * The put function adds a tile to the bag.
         * <p>
         *
         * @param t Tile  Determine which tile to put back into the bag
         *
         * @docauthor Trelent
         */
        public void put(Tile t){
            int tile_location = t.letter - 'A';
            if (current_amount_bag<98 && letter_amount[tile_location]< max_letter_amount[tile_location]){
                letter_amount[tile_location]+=1;
                current_amount_bag+=1;
            }
        }
        /**
         * The size function returns the current amount of items in the bag.
         * <p>
         *
         *
         * @return The current amount of items in the bag
         *
         * @docauthor Trelent
         */
        public int size() {
            return current_amount_bag;
        }
        /**
         * The getQuantities function returns a copy of the letter_amount array.
         * <p>
         *
         *
         * @return A copy of the letter_amount array
         *
         * @docauthor Trelent
         */
        public int[] getQuantities() {
            return this.letter_amount.clone();
        }
        /**
         * The getBag function is a static function that returns the singleton instance of the Bag class.
         * <p>
         *
         *
         * @return The instance of the bag class
         *
         * @docauthor Trelent
         */
        public static Bag getBag() {
            if (_instance ==null) {
                _instance = new Bag();
            }
            return  _instance;
        }


    }
    /**
     * The serialize function takes a Tile object and returns a byte array.
     * This is done by creating an ObjectOutputStream, which writes the tile to the ByteArrayOutputStream.
     * The flush function is called on out, which flushes any buffered output bytes from this output stream and forces any buffered output bytes to be written out.
     * The close function is called on bos, which closes the stream, releasing any system resources associated with it.

     *
     * @param tile Pass the tile object to the function
     *
     * @return A byte array
     *
     * @docauthor Trelent
     */
    static public byte[] serialize(Tile tile){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(tile);
            out.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    /**
     * The deserialize function takes a byte array and returns the Tile object that it represents.
     * <p>
     *
     * @param bytes Convert the byte array to a bytearrayinputstream object
     *
     * @return An object of type tile
     *
     * @docauthor Trelent
     */
    static public Tile deserialize(byte[] bytes){
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return (Tile) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }


}
