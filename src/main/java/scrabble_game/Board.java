package scrabble_game;

import model.BookScrabbleCommunication;
import model.GameManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board implements Serializable {
    private static Board b = null;
    public Tile[][] tiles_board;
    private int[][] bonus;


    /**
     * The Board function is a constructor that creates the board and initializes it with all of its bonuses:
     * 1 -normal ,2 - double letter score , 3 - triple letter score, 22 - double word score , 33- triple word score
     * It also sets the tiles_board to null, so that there are no tiles on the board at first.

     * <p>
     *
     * @docauthor Trelent
     */
    private Board() {
        this.tiles_board = new Tile[15][15];
        for (Tile[]a : this.tiles_board) {
            Arrays.fill(a, null);
        }
        this.bonus = new int[15][15];
        for (int[]b : this.bonus) {
            Arrays.fill(b, 1);
        }

        //33
        bonus[0][0]= 33 ;  bonus[0][7] = 33; bonus[0][14]= 33;
        bonus[7][0]= 33; bonus[7][14]= 33;
        bonus[14][0]= 33; bonus[14][7]= 33; bonus[14][14]= 33;
        //22
        for (int i=1; i<14;i++){
            if (i == 5){
                i = 10;
            }
            bonus[i][i] = 22; bonus[i][14-i] = 22;
        }
        // 2
        bonus[0][3]= 2; bonus[0][11]= 2;
        bonus[2][6]= 2; bonus[2][8]= 2;
        bonus[3][0]= 2; bonus[3][7]= 2; bonus[3][14]= 2;
        bonus[6][2]= 2; bonus[6][6]= 2; bonus[6][8]= 2; bonus[6][12]= 2;
        bonus[7][3]= 2; bonus[7][11]= 2;
        bonus[8][2]= 2; bonus[8][6]= 2; bonus[8][8]= 2; bonus[8][12]= 2;
        bonus[11][0]= 2; bonus[11][7]= 2; bonus[11][14]= 2;
        bonus[12][6]= 2; bonus[12][8]= 2;
        bonus[14][3]= 2; bonus[14][11]= 2;
        //3
        bonus[1][5]= 3; bonus[1][9]= 3;
        bonus[5][1]= 3; bonus[5][5]= 3; bonus[5][9]= 3; bonus[5][13]= 3;
        bonus[9][1]= 3; bonus[9][5]= 3; bonus[9][9]= 3; bonus[9][13]= 3;
        bonus[13][5]= 3; bonus[13][9]= 3;

    }
    /**
     * The getTiles function returns a copy of the tiles_board array.
     *
     *
     * @return A copy of the tiles_board array
     *
     */
    public Tile[][] getTiles() {
        return this.tiles_board.clone();

    }
    /**
     * The getBoard function is a static function that returns the board object.
     *
     *
     * @return A board object
     *
     */
    public static Board getBoard() {
        if (b == null) {
            b = new Board();
        }
        return b;
    }

    /**
     * The boardLegal function checks if the word is legal to be placed on the board.
     * It checks if it is not out of bounds, and that it does not overlap with other tiles.
     *
     * @param word Get the row and column of the word
     *
     * @return True if the word is legal
     *
     */
    public boolean boardLegal( Word word) {
        // check if it is the first word - one tile have to be on the star position
        // notfirstword will be true automatic. if it is the first word will be changed to false.
        //neartile will be false automatic. if it is the first word will be changed to true.
        boolean notfirstword = b.tiles_board[7][7] !=null;
        boolean near_tile = false ;
        // check if the word inside board boundaries
        int row = word.getRow();
        int col = word.getCol();
        for (int i = 0; i < word.getTiles().length; i++) {
            if (row < 0 || row > 14 || col < 0 || col > 14)
                return false;
            if (!notfirstword) {
                near_tile = true ;
                if (row == 7 && col == 7)
                    notfirstword = true;
            }
            // check if the word contains or near existing tile.( just if it is not the first word)
            else {
                System.out.println("Checking Row: " + row + ", Col: "+ col);
                // if we want to put tile on occupied spot we have to check it is the same one
                if (b.tiles_board[row][col]!= null){
                    Board.printBoard(this);
                    if (word.getTiles()[i]!= null) {
                        if (b.tiles_board[row][col] != word.getTiles()[i]) {
                            return false;
                        }
                        else {
                            near_tile = true;
                        }
                    }
                }
                //check if the new word is near existing tile
                else {
                    if (row != 0) {
                        if (b.tiles_board[row - 1][col] != null)
                            near_tile = true;
                    }
                    if (row != 14) {
                        if (b.tiles_board[row + 1][col] != null)
                            near_tile = true;
                    }
                    if (col != 0) {
                        if (b.tiles_board[row][col - 1] != null)
                            near_tile = true;
                    }
                    if (col != 14) {
                        if (b.tiles_board[row][col + 1] != null)
                            near_tile = true;
                    }
                }
            }
            if (word.isVertical())
                row += 1;
            else
                col += 1;
        }
        // if it is the first word of the game and one of the tiles not on the star position it's not legal
        return notfirstword && near_tile;
    }

    /**
     * The dictionaryLegal function takes a Word object as an argument and returns true if the word is legal according to the dictionary, false otherwise.
     *
     *
     * @param word Get the tiles from the word and then check if it is a valid word
     *
     * @return True if the word is legal, false otherwise
     *
     */
    public boolean dictionaryLegal(Word word){
        BookScrabbleCommunication BScommunication = BookScrabbleCommunication.get_instance();
        StringBuilder sb = new StringBuilder();
        StringBuilder wordString = new StringBuilder();
        for(Tile tile : word.getTiles()){
            System.out.println("Test Inside for -> " + tile.letter);
            wordString.append(tile.letter);
        }
        sb.append("Q,").append(BScommunication.getDictionaries()).append(",").append(wordString);
        String resBSH = BScommunication.runChallengeOrQuery(sb.toString());
        return Boolean.parseBoolean(resBSH);
    }

    /**
     * The check_boundaries_up function checks if the tile is at the top of the board.
     * If it is not, then it will check if there are any tiles above it.
     * If there are no tiles above, then we return that row number as a valid move for that column.

     *
     * @param row Represent the row number of the tile that is being moved
     * @param col Check the column of the tile that is being moved
     *
     * @return The row number of the first tile above the given tile
     *
     * @docauthor Trelent
     */
    private static  int  check_boundaries_up (int row, int col){
        while (row != 0) {
            if (b.tiles_board[row -1][col] != null)
                row-=1 ;
            else
                return row;
        }
        return row;
    }
    /**
     * The check_boundaries_down function checks if the tile can be placed in the down direction.
     *
     *
     * @param row Keep track of the row number
     * @param col Check the column of the tile that is being placed
     *
     * @return The row number of the tile that is below the current one
     *
     * @docauthor Trelent
     */
    private static  int  check_boundaries_down (int row, int col){
        while (row != 14) {
            if (b.tiles_board[row +1][col] != null)
                row+=1 ;
            else
                return row;
        }
        return row;

    }
    /**
     * The check_boundaries_left function checks if the left side of the board is empty.
     *
     *
     * @param row Determine the row that the tile is in
     * @param col Keep track of the column number
     *
     * @return The column number of the leftmost tile in a row
     *
     * @docauthor Trelent
     */
    private static  int  check_boundaries_left (int row, int col){
        while (col != 0) {
            if (b.tiles_board[row][col -1] != null)
                col-=1 ;
            else
                return col;
        }
        return col;
    }
    /**
     * The check_boundaries_right function checks the boundaries of the board to make sure that
     * there are no null tiles in between two non-null tiles. If there is a null tile, then it will return
     * the column number of that tile. Otherwise, it will return 14 (the last column).

     *
     * @param row Check the row of the tile that is being placed
     * @param col Specify the column of the tile that is being placed
     *
     * @return The column number of the last tile in the row
     *
     * @docauthor Trelent
     */
    private static  int  check_boundaries_right (int row, int col){
        while (col != 14) {
            if (b.tiles_board[row][col +1] != null)
                col+=1 ;
            else
                return col;
        }
        return col;

    }
    /**
     * The create_Tilearr_tocheck_word function creates an array of tiles to check the word.
     * <p>
     *
     * @param length Determine the length of the array
     * @param first_row Determine the row of the first tile in a word
     * @param  first_col Get the first column of the word to be checked
     * @param isvertical Determine if the word is vertical or horizontal
     * @param tile Create a tile array of the same length as the word being checked
     *
     * @return An array of tile objects
     *
     * @docauthor Trelent
     */
    private static Tile[] create_Tilearr_tocheck_word( int length, int first_row, int first_col, boolean isvertical, Tile tile){
        Tile[] checktiles = new Tile[length];
        for (int i = 0 ; i< length; i++){
            if (isvertical) {
                    if (b.getTiles()[first_row + i][first_col] != null)
                        checktiles[i] = b.getTiles()[first_row + i][first_col];
                    else
                        checktiles[i] = tile;
            }
            else {
                if (b.getTiles()[first_row][first_col + i] != null)
                    checktiles[i] = b.getTiles()[first_row][first_col + i];
                else
                    checktiles[i] = tile;
            }
        }
        return  checktiles;
    }
    /**
     * The getWords function returns an ArrayList of Word objects that are formed by the tiles in the
     * given word. The function always gets a legal word, and it checks if there is any other words
     * that can be created from this move. If so, it adds them to the list as well.

     *
     * @param word Get the row, col and tiles of the word
     *
     * @return An arraylist of word objects
     *
     * @docauthor Trelent
     */
    private ArrayList<Word> getWords(Word word){
        // the function always get legal word
        ArrayList<Word> words = new ArrayList<>();
        int row = word.getRow();
        int col = word.getCol();
        int word_length = word.getTiles().length;
        if (!word.isVertical()){
            Tile[] the_word = word.getTiles().clone();
            int count = 0;
            for (Tile t: the_word){
                if (t==null)
                    the_word[count]= b.getTiles()[row][col+count];
                count++;
            }
            // need to check once the row of the word if it's not vertical word.
            int right_col = check_boundaries_right(row, (col +word_length - 1));
            int left_col = check_boundaries_left(row,col);
            Tile[] lefttiles = create_Tilearr_tocheck_word(col -left_col,row,left_col,false,null);
            Tile[] righttiles = create_Tilearr_tocheck_word((right_col -col -word_length +1),row,col+word_length-1,false,null);
            int lefttilesL = lefttiles.length;
            int middleL = the_word.length;
            int righttilesL =righttiles.length;
            int checktilesL = lefttilesL + middleL + righttilesL;
            //COMBINE THE 3 ARRAYS
            Tile[] checktiles11= new Tile[checktilesL];
            System.arraycopy(lefttiles, 0, checktiles11, 0, lefttilesL);
            System.arraycopy(the_word, 0, checktiles11, lefttilesL, middleL);
            System.arraycopy(righttiles, 0, checktiles11, lefttilesL+middleL, righttilesL);
            Word check =  new Word(checktiles11, row,left_col,false );
            if(dictionaryLegal(word))
                words.add( check);
            else
                words.add(word);
            //check boundaries for each tile
            for (int i=0 ; i < word_length; i++){
                Tile[] checktiles;
                if (word.getTiles()[i]!= null){
                    int up_row = check_boundaries_up(row,col + i);
                    int down_row = check_boundaries_down(row, col +i);
                    if (up_row!= down_row) {
                        checktiles = create_Tilearr_tocheck_word((down_row - up_row + 1), up_row, col + i, true, word.getTiles()[i]);
                        check = new Word(checktiles, up_row, col + i, true);
                        if (dictionaryLegal(word))
                            words.add(check);
                    }
                }
            }
        }
        else {
            // need to check once the colum of the word if it's vertical word.
            Tile[] the_word = word.getTiles().clone();
            int count = 0;
            for (Tile t: the_word){
                if (t==null)
                    the_word[count]=b.getTiles()[row+count][col];
                count++;
            }
            int up_row = check_boundaries_up(row,col);
            int down_row = check_boundaries_down((row + word_length -1), col);
            //Tile[] checktiles = create_Tilearr_tocheck_word((down_row - up_row +1),up_row, col, true);
            Tile[] uptiles = create_Tilearr_tocheck_word((row - up_row),up_row,col,true,null);
            Tile[] downtiles = create_Tilearr_tocheck_word((down_row -row-word_length +1),row+word_length-1,col,true,null);
            int uptilesL = uptiles.length;
            int middleL = the_word.length;
            int downtilesL =downtiles.length;
            int checktilesL = uptilesL + middleL + downtilesL;
            //COMBINE THE 3 ARRAYS
            Tile[] checktiles1= new Tile[checktilesL];
            System.arraycopy(uptiles, 0, checktiles1, 0, uptilesL);
            System.arraycopy(the_word, 0, checktiles1, uptilesL, middleL);
            System.arraycopy(downtiles, 0, checktiles1, uptilesL+middleL, downtilesL);
            Word check =  new Word(checktiles1, up_row,col,true );
            if(dictionaryLegal(word))
                words.add( check);
            else
                words.add(word);
            //check boundaries for each tile
            for (int i=0 ; i < word_length; i++){
                Tile[] checktiles;
                if (word.getTiles()[i]!= null){
                    int right_col = check_boundaries_right(row +i , col);
                    int left_col = check_boundaries_left(row +i,col);
                    if(right_col!= left_col) {
                        checktiles = create_Tilearr_tocheck_word((right_col - left_col + 1), row + i, left_col, false, word.getTiles()[i]);
                        check = new Word(checktiles, row + i, left_col, false);
                        if (dictionaryLegal(word))
                            words.add(check);
                    }
                }
            }
        }
        return words;
    }
    /**
     * The getScore function calculates the score of a word.
     * <p>
     *
     * @param word Get the row and column of the word
     *
     * @return The score of the word
     *
     * @docauthor Trelent
     */
    private int getScore(Word word){
        int row = word.getRow();
        int col = word.getCol();
        int score = 0;
        int word_bonus = 0;
        for (Tile tile :word.getTiles()) {
            int location_bonus = b.bonus[row][col];
            int tile_value = tile.score;
            if (location_bonus<4) {
                tile_value = tile_value * location_bonus;
            }
            else
                word_bonus+= location_bonus%10;
            score += tile_value ;
            if (word.isVertical())
                row +=1;
            else
                col+=1;
        }
        if (word_bonus!= 0)
            score= score * word_bonus;

        return  score;
    }

    /**
     * The tryPlaceWord function takes a Word object as input and attempts to place it on the board.
     * If the word can be placed, then it is placed and its score is returned.
     * If the word cannot be placed, then 0 is returned.

     *
     * @param word Get the row and col of the word
     *
     * @return -1 if the word is not legal - dictionaryLegal. 0 if the word is not legal - boardLegal. if the word is legal returns the score.
     *
     */
    public int tryPlaceWord(Word word){
        if (!(boardLegal(word))) {
            return 0;
        }

        List<Word> allWords = getWords(word);

        for(Word w : allWords){
            if(!dictionaryLegal(w)) return -1;
        }

        boolean firstword = b.tiles_board[7][7] ==null;
        ArrayList<Word> Total_words = getWords(word);

        //insert word
        int row = word.getRow();
        int col = word.getCol();
        for (Tile tile :word.getTiles()) {
            if (b.tiles_board[row][col]== null)
                b.tiles_board[row][col] = tile;
            if (word.isVertical())
                row +=1;
            else
                col+=1;
        }
        // calculate score
        int Total_score = 0;
        for (Word w :Total_words) {
            Total_score +=getScore(w);
        }
        if (firstword)
            return (Total_score * 2);
        else
            return Total_score;
    }

    /**
     * The serialize function takes a Board object and returns a byte array.
     * The serialize function is used to convert the board into an array of bytes, which can be stored in the database.

     *
     * @param board Pass in the board object that is to be serialized
     *
     * @return A byte array
     *
     */
    static public byte[] serialize(Board board){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(board);
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
     * The deserialize function takes a byte array and returns the Board object that it represents.
     *
     * @param bytes Convert the byte array into an object
    static public board deserialize(byte[] bytes){
            bytearrayinputstream bis = new bytearrayinputstream(bytes);
            objectinput in = null;
            try {
                in = new objectinputstream(bis);
     *
     * @return A board object, but I need to get the byte array from that
     *
     */
    static public Board deserialize(byte[] bytes){
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return (Board) in.readObject();
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

    /**
     * The printBoard function prints the current state of the board to the console.
     *
     * @param board Access the tiles on the board
     *
     */
    public static void printBoard(Board board){
        Tile[][] currentTiles = board.getTiles();
        for(int row=0; row < 15; row++){
            for (int col=0; col < 15; col++){
                if(currentTiles[row][col] != null)
                    System.out.print(currentTiles[row][col].letter+ " ");
                else
                    System.out.print("- ");
            }
            System.out.println("");
        }


    }

}
