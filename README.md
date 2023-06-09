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
  <br />
  <br />
  <br />

**Scrabble Board**

###### **Scrabble Board Diagram:**

<img src="https://github.com/yuvalzadik/Scrabble_Game/blob/main/Images/board.jpeg" width="420" height="420" alt=""/>

The board will be made as a 15 x 15 array of Cell objects.  
It will show bonuses that the cells have. (e.g., starting center, double word, double letter, triple word, triple letter)

<br />

**Scrabble Tile**

Tiles are going to hold a character and corresponding points.
Characters are used to check if tiles played on board are legit words, and points will be added from all tiles played to form the new word in addition to any newer words completed with the initial wordplay.

<br />

**Scrabble Bag**

Contains 98 tiles:
<br />
1 point:   E ×12, A ×9, I ×9, O ×8, N ×6, R ×6, T ×6, L ×4, S ×4, U ×4  
2 points:  D ×4, G ×3  
3 points:  B ×2, C ×2, M ×2, P ×2  
4 points:  F ×2, H ×2, V ×2, W ×2, Y ×2  
5 points:  K ×1  
8 points:  J ×1, X ×1  
10 points: Q ×1, Z ×1

The bag will be responsible for containing tile items identical to the list above in terms of quantity and property.
During the game it allows players to randomly draw tiles, and the amount of remaining tiles will be updated each turn.
<br />

**Dictionary**

Our dictionary will be represented by books, which are given as text files. 
To save on I/O operations and ensure fast data retrieval from the dictionary, we will use several filters:
1. Cache Manager: will keep in memory the answers to the most common queries.  
   The cash will use through LRU & LFU methods, and the search will be made in o(1) time.
2. Bloom filter - an efficient and cost-effective algorithm that tells with absolute certainty whether 
   a word is not exist in the dictionary, and in high probability it is there.
3. There is an option for the player to challenge the dictionary and check if it made a mistake
   and the word is actually not in it, using an I/O based search. 
   If the player was wrong he will lose points and if he was right he will get a bonus.

<br />

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

<br />
<br />

**Strategy:**

To ensure that the project is managed correctly and completed in the required time, we have taken the following actions:

1. Using Jira to organize in advance all the required tasks and division of work between the team.
2. We will divide the project into three main milestones and the tasks in Jira will be done according to them.
3. We will perform tests to check the correctness and efficiency of the code while writing it, so that we do not encounter unexpected problems at the end.

**GANTT CHART:**

<br />
<br />
<img src="https://github.com/yuvalzadik/Scrabble_Game/blob/b140711938481d1f67a84ba8d51ee4596c3b8646/Images/Roadmap.png" width="800" height="400" alt=""/>

<br />

###### **[Jira URL](https://scrabble-ptm2.atlassian.net/jira/software/projects/SCRAB/boards/1/roadmap)**
<br />

###### [Java Doc URL](https://yuvalzadik.github.io/Scrabble_Game/src/main/resources/com.example.scrabble_game/module-summary.html)

<br />

## Demo Video
[Link to Demo video](https://drive.google.com/file/d/1Yt7SvtBEaMmL3VvYvMRnusUgassQoNgT/view?usp=sharing)
 
