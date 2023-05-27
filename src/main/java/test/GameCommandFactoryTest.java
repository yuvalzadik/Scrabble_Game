package test;

import model.GameCommandsFactory;

import java.util.ArrayList;

public class GameCommandFactoryTest {
    public static void main(String[] args) {

        boolean passall= true;
        String joinString = GameCommandsFactory.getJoinGameCommandString("YUVAL");
        if (!(joinString.equals("-1,I,YUVAL"))) {
            System.out.println("problem with getJoinGameCommand ");
            passall= false;
        }
        String startGameString = GameCommandsFactory.getStartGameCommandString(5);
        if (!(startGameString.equals("5,S"))) {
            System.out.println("problem with getStartGameCommand ");
            passall= false;
        }
        String tryPlaceWordQuery = GameCommandsFactory.getTryPlaceWordCommandString(5, "MOON", 3, 7, true);
        if (!(tryPlaceWordQuery.equals("5,Q,MOON,3,7,true"))) {
            System.out.println("problem with getTryPlaceWordCommand");
            passall= false;
        }
        String challengeWordQuery =  GameCommandsFactory.getChallengeCommandString(6, "LOL", 7, 7, false);
        if (!(challengeWordQuery.equals("6,C,LOL,7,7,false"))) {
            System.out.println("problem with getChallengeCommand ");
            passall= false;
        }
        String getBoardString = GameCommandsFactory.getGetBoardCommandString(8);
        if (!(getBoardString.equals("8,B"))) {
            System.out.println("problem with getGetBoardCommand");
            passall= false;
        }
        String getBagString = GameCommandsFactory.getGetRandTileString(5);
        if (!(getBagString.equals("5,T"))) {
            System.out.println("problem with getGetRandTile");
            passall= false;
        }
        String [] Dictionaries = new String[2];
        Dictionaries[0]= "yuv.txt";
        Dictionaries[1]= "m.txt";
        String getDictionariesString = GameCommandsFactory.getSetGameDictionariesString(5,  Dictionaries);
        if (!(getDictionariesString.equals("5,D,yuv.txt,m.txt,endDictionaries"))) {
            System.out.println("problem with getSetGameDictionaries");
            passall= false;
        }
        if (passall){
            System.out.println("success");
        }

    }
}
