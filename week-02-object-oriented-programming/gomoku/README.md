# Assessment M02: Gomoku

### Requirements
- Be able to set up two players
- Be able to get input from human players
- Keep track and display stone placements
- Validate input before moving forward
- Have messages for each invalid input
- Display final result
- Option to play again
- Have at least one class beyond the App class

### State
Player player1

Player player2

Gomoku game

Stone stone

boolean playAgain

Result result

char[][] board
 
### Steps
- do while(playAgain)
  - Set Up
    - print out Welcome to Gomoku
    - prompt user to choose player 1 & enter name(if needed)
    - create Player 1
    - prompt user to choose player 2 & enter name(if needed)
    - create Player 2
    - create game with game=Gomoku(player1, player2)
    - print out (Randomizing) & who goes first
  - Game Play
    - do while(!game.isOver) loop
      - do while(!result.isSuccess)
        - if calling current.generatemove()==null
          - prompt current player to input stone placement of row and col
          - create stone with input position 
        - result=game.place(stone)  
        - printErrorMessage(result)
      - save stone position in board  
      - print out board;
  - End Game
    - print out game result
    - prompt players if they want to play again


### Methods
Player createPlayer(int choice)

### Class inputAssist
String readString(String prompt)

int readInt(String prompt)

### Class outputAssist
void printBoard(char[][] board);

void printErrorMessage(Result result)
{//switch match result with appropriate msg};