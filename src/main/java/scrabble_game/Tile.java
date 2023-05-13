package scrabble_game;
import java.io.*;
import java.util.Objects;
public class Tile implements Serializable{
    public final char letter ;
    public final int score ;
    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile)o;
        return letter == tile.letter && score == tile.score;
    }

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
        /*char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        for (int i=0; i<26; i++){
            letters_and_value[i] = new Tile(alphabet[i] , letter_amount[i]);
        }*/
        }

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
        public void put(Tile t){
            int tile_location = t.letter - 'A';
            if (current_amount_bag<98 && letter_amount[tile_location]< max_letter_amount[tile_location]){
                letter_amount[tile_location]+=1;
                current_amount_bag+=1;
            }
        }
        public int size() {
            return current_amount_bag;
        }
        public int[] getQuantities() {
            return this.letter_amount.clone();
        }
        public static Bag getBag() {
            if (_instance ==null) {
                _instance = new Bag();
            }
            return  _instance;
        }


    }
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
