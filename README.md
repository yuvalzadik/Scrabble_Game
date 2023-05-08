# Scrabble Game

###### **Overview**
Scrabble is a word game in which two to four players earn points by constructing words with letter tiles and placing them on a 15×15 grid of squares.
Each letter has a different point value, there are 98 letter tiles (without the two empty tiles like in the original game), and only one letter tile can fit in a grid space.

###### **Motivation:**
Building a project that contains the following elements:
- Use of design and architecture templates.
- Communication and client server architecture.
- Using a data structures and streaming data using files.
- Implementation of different algorithms.
- Parallel programming using threads.
- Building a desktop application with a GUI.




**Scrabble Board**

###### **Scrabble Board Diagram:**

<img src="https://github.com/yuvalzadik/Scrabble_Game/blob/main/Images/board.jpeg" width="420" height="420" alt=""/>

The board will be made as a 15 x 15 array of Cell objects.  
It will show bonuses that the cells have. (e.g., starting center, double word, double letter, triple word, triple letter)


**Scrabble Tile**

Tiles are going to hold a character and corresponding points.
Characters are used to check if tiles played on board are legit words, and points will be added from all tiles played to form the new word in addition to any newer words completed with the initial wordplay.


**Scrabble Bag**

Contains 98 tiles:
1 point:   E ×12, A ×9, I ×9, O ×8, N ×6, R ×6, T ×6, L ×4, S ×4, U ×4  
2 points:  D ×4, G ×3  
3 points:  B ×2, C ×2, M ×2, P ×2  
4 points:  F ×2, H ×2, V ×2, W ×2, Y ×2  
5 points:  K ×1  
8 points:  J ×1, X ×1  
10 points: Q ×1, Z ×1

The bag will be responsible for containing tile items identical to the list above in terms of quantity and property.
During the game it allows players to randomly draw tiles, and the amount of remaining tiles will be updated each turn.


**Dictionary**


**Game rules and details:**

1. Each player draws a tile from the bag to determine the order of the players.
   The order will be determined according to the letters drawn from the smallest to the largest.
2. All the tiles must be returned to the bag, and then each player randomly draws 7 tiles.
3. First, the first player has to form a legal word that goes through the star slot on the board. Only he will get double points for it.
   After that, he will pull tiles from the bag to complete the tiles amounting to 7.
4. Gradually, each player on his turn puts together a legal word from the tiles in his possession, where each word must combine one of the tiles that exist on the board. After writing the word, the player completes tiles from the bag to 7.
   His score is accumulated according to all new words created by the board following the placing of the tiles and according to the different bonus slots as detailed above.
5. A player who cannot create a valid word forfeits his turn.
6. The game ends after N rounds.

   ###### **Legal word:**
            - Written from left to right or from top to bottom.
            - Appears in one of the books chosen for the game.
            - Combines one of the existing tiles on the game board.
            - Does not produce other illegal words on the board



###### **Algorithm**



###### Demonstration:
###### **[Demo](will be added later)**
 
