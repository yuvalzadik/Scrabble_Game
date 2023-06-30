package model;

import java.util.HashMap;
import java.util.Map;

public class GameCommandsFactory {
    static public HashMap<GameCommand, Character> commandToChar = new HashMap<>() {{
        put(GameCommand.JoinGame, 'I');
        put(GameCommand.StartGame, 'S');
        put(GameCommand.TryPlaceWord, 'Q');
        put(GameCommand.Challenge, 'C');
        put(GameCommand.GetBoard, 'B');
        put(GameCommand.GetRandTile, 'T');
        put(GameCommand.SetGameDictionaries, 'D');
        put(GameCommand.SkipTurn, 'E');
        put(GameCommand.SwapTiles, 'P');
    }};

    /**
     * The getCommandEnumFromChar function takes a character as input and returns the corresponding GameCommand enum.
     *
     * @param ch char  Get the corresponding gamecommand enum from the commandtochar map
     *
     * @return The command enum associated with the given char
     */
    static public GameCommand getCommandEnumFromChar(char ch){
        for(Map.Entry<GameCommand, Character> entry: commandToChar.entrySet()) {
            if(entry.getValue() == ch) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * The getJoinGameCommandString function takes a player name as input and returns a string that can be sent to the server
     * to join the game. The format of this string is:
     * -2,J,playerName\n

     * @param playerName String  Identify the player who is joining the game
     *
     * @return A string that represents a join game command
     */
    static public String getJoinGameCommandString(String playerName) {
        return "-1," + commandToChar.get(GameCommand.JoinGame) + "," + playerName;
    }

    /**
     * The getStartGameCommandString function takes in a playerId and returns a string that is the command to start the game.
     *
     *
     * @param playerId int  Identify the player who is sending the command
     *
     * @return A string that contains the player id and a startgame command
     *
     * @docauthor Trelent
     */
    static public String getStartGameCommandString(int playerId) {
        return playerId + "," + commandToChar.get(GameCommand.StartGame);
    }

    /**
     * The getTryPlaceWordCommandString function takes in a playerId, word, row, col and vertical boolean.
     * It returns a string that is formatted as follows:
     * &quot;playerId&quot; + &quot;,&quot; + commandToChar.get(GameCommand.TryPlaceWord) + &quot;,&quot; + word + &quot;,&quot;
     *
     * @param playerId int  Identify the player who is trying to place a word
     * @param word String  Pass in the word that is being placed on the board
     * @param row int  Determine the row of the board where a word will be placed
     * @param col int  Determine the column of the word to be placed
     * @param vertical boolean  Determine whether the word is placed vertically or horizontally
        static public string gettryplacewordcommandstring(int playerid, string word, int row, int col) {
            return gettryplacewordcommandstring(playerid, word, row ,col ,true);
        }


     *
     * @return A string in the following format:
     *
     * @docauthor Trelent
     */
    static public String getTryPlaceWordCommandString(int playerId, String word, int row, int col, boolean vertical) {
        return playerId + "," + commandToChar.get(GameCommand.TryPlaceWord) + "," + word + "," + row + "," + col + "," + vertical;
    }

    /**
     * The getChallengeCommandString function takes in a playerId, word, row, col and vertical.
     * It returns a string that is formatted as follows:
     * &quot;playerId&quot; + &quot;,&quot; + commandToChar.get(GameCommand.Challenge) + &quot;,&quot; + word
     *      + &quot;,&quot;+ row+&quot;,&quot;+col+&quot;,&quot;vertical&quot;. This function is used to create the challenge command string for the server to read from clients.

     *
     * @param playerId int  Identify the player who is making the challenge
     * @param  word String Pass the word that is being challenged
     * @param row int Specify the row of the board where you want to place a word
     * @param col int  Determine the column that the word is placed in
     * @param vertical boolean  Determine if the word is placed vertically or horizontally
        static public string getplacewordcommandstring(int playerid, string word, int row, int col, boolean vertical) {
            return playerid + &quot;,&quot; + commandtochar
     *
     * @return A string in the format:
     *
     * @docauthor Trelent
     */
    static public String getChallengeCommandString(int playerId, String word, int row, int col, boolean vertical) {
        return playerId + "," + commandToChar.get(GameCommand.Challenge) + "," + word + "," + row + "," + col + "," + vertical;
    }

    /**
     * The getGetBoardCommandString function takes in a playerId and returns a string that is the command to get the board.
     *
     * @param playerId int  Identify the player that is sending the command
     *
     * @return A string of the form &quot;playerid,command&quot;
     *
     * @docauthor Trelent
     */
    static public String getGetBoardCommandString(int playerId) {
        return playerId + "," + commandToChar.get(GameCommand.GetBoard);
    }

    /**
     * The getGetRandTileString function takes in a playerId and returns a string that can be sent to the server.
     * The string is formatted as follows: &quot;playerId,GetRandTile&quot;
     *
     * @param playerId int  Identify the player who is making the request
     *
     * @return A string that is composed of the playerid and a character representing the getrandtile command
     *
     * @docauthor Trelent
     */
    static public String getGetRandTileString(int playerId) {
        return playerId + "," + commandToChar.get(GameCommand.GetRandTile);
    }

    /**
     * The getSetGameDictionariesString function takes in a playerId and an array of dictionaries,
     * and returns a string that can be sent to the server. The string is formatted as follows:
     * &quot;playerId,commandChar,dictionaryName0,...dictionaryNameN,&quot;endDictionaries&quot;

     *
     * @param playerId int  Identify the player who is sending the message
     * @param dictionaries String[]  Pass the dictionaries to be used in the game
     *
     * @return A string that contains the playerid and a list of dictionaries
     *
     * @docauthor Trelent
     */
    static public String getSetGameDictionariesString(int playerId, String[] dictionaries) {
        String setGameDictionariesString = playerId + "," + commandToChar.get(GameCommand.SetGameDictionaries) + ",";
        int i;
        for (i=0; i< dictionaries.length; i++){
            setGameDictionariesString += dictionaries[i];
            setGameDictionariesString += ",";
        }
        setGameDictionariesString += "endDictionaries";
        return setGameDictionariesString;
    }

    /**
     * The getSkipTurnString function returns a string that represents the command to skip a turn.
     *
     * @param playerId  int Identify the player who is skipping their turn
     *
     * @return A string that is a comma-separated list of the player id and the character representation of gamecommand
     *
     * @docauthor Trelent
     */
    public static String getSkipTurnString(int playerId) {
        return playerId + "," + commandToChar.get(GameCommand.SkipTurn);
    }

    /**
     * The getSwapTilesString function returns a string that is used to tell the server that the player wants to swap tiles.
     *
     * @param playerId int  Identify which player is making the move
     *
     * @return A string that contains the player id and a character representing the command
     */
    public static String getSwapTilesString(int playerId) {
        return playerId + "," + commandToChar.get(GameCommand.SwapTiles);
    }
}
