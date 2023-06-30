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

    static public GameCommand getCommandEnumFromChar(char ch){
        for(Map.Entry<GameCommand, Character> entry: commandToChar.entrySet()) {
            if(entry.getValue() == ch) {
                return entry.getKey();
            }
        }
        return null;
    }

    static public String getJoinGameCommandString(String playerName) {
        return "-1," + commandToChar.get(GameCommand.JoinGame) + "," + playerName;
    }

    static public String getStartGameCommandString(int playerId) {
        return playerId + "," + commandToChar.get(GameCommand.StartGame);
    }

    static public String getTryPlaceWordCommandString(int playerId, String word, int row, int col, boolean vertical) {
        return playerId + "," + commandToChar.get(GameCommand.TryPlaceWord) + "," + word + "," + row + "," + col + "," + vertical;
    }

    static public String getChallengeCommandString(int playerId, String word, int row, int col, boolean vertical) {
        return playerId + "," + commandToChar.get(GameCommand.Challenge) + "," + word + "," + row + "," + col + "," + vertical;
    }

    static public String getGetBoardCommandString(int playerId) {
        return playerId + "," + commandToChar.get(GameCommand.GetBoard);
    }

    static public String getGetRandTileString(int playerId) {
        return playerId + "," + commandToChar.get(GameCommand.GetRandTile);
    }

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

    public static String getSkipTurnString(int playerId) {
        return playerId + "," + commandToChar.get(GameCommand.SkipTurn);
    }

    public static String getSwapTilesString(int playerId) {
        return playerId + "," + commandToChar.get(GameCommand.SwapTiles);
    }
}
