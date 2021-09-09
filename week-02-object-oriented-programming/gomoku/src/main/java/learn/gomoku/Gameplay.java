package learn.gomoku;

import learn.gomoku.game.Board;
import learn.gomoku.game.Gomoku;
import learn.gomoku.game.Result;
import learn.gomoku.game.Stone;
import learn.gomoku.players.BotPlayer;
import learn.gomoku.players.HumanPlayer;
import learn.gomoku.players.Player;
import learn.gomoku.players.RandomPlayer;

public class Gameplay {
    private final InputAssist assist = new InputAssist();
    private Board board;
    private Gomoku game;
    private Result result;

    public Gameplay() {
        String playAgain;
        do{
            //Set Up
            setup();

            //Game Play
            do{
                eachPlayerTurn();
            }while(!game.isOver());

            //End Game
            System.out.println(result.getMessage());
            playAgain = assist.readString("\nPlay Again? [y/n]:");
        }while (playAgain.equalsIgnoreCase("y"));
    }

    private void setup(){
        System.out.println("Welcome to Gomoku\n=================\n");
        Player player1=createPlayer(1);
        Player player2=createPlayer(2);

        game = new Gomoku(player1, player2);
        board = new Board(Gomoku.WIDTH);
        System.out.println("(Randomizing)\n");
        System.out.printf("%s goes first.\n", game.getCurrent().getName());
    }

    private void eachPlayerTurn(){
        Stone stone;
        do{
            System.out.printf("\n%s's Turn\n", game.getCurrent().getName());
            stone=game.getCurrent().generateMove(game.getStones());
            if(stone==null){
                int row = assist.readInt("Enter a row:");
                int col = assist.readInt("Enter a column:");
                stone=new Stone(row-1, col-1, game.isBlacksTurn());
            } else{
                System.out.println("Enter a row: "+(stone.getRow()+1));
                System.out.println("Enter a column: "+(stone.getColumn()+1));
            }
            result=game.place(stone);
            if(!result.isSuccess()){
                System.out.println(result.getMessage());
            }
        }while(!result.isSuccess());
        board.addStone(stone);
        board.printBoard();
    }

    private Player createPlayer(int playerNum){
        InputAssist assist = new InputAssist();
        int choice = assist.readInt("Player "+playerNum+" is:\n" +
                "1. Human\n" +
                "2. Random Player\n" +
                "3. Bot Player\n" +
                "Select [1-3]:",1, 3);
        switch (choice){
            case 1:
                String input = assist.readString("Player "+playerNum+", enter your name: ");
                return new HumanPlayer(input);
            case 2:
                Player random = new RandomPlayer();
                System.out.println("Random player's name is " + random.getName() + "\n");
                return random;
            case 3:
                Player bot = new BotPlayer();
                System.out.println("Random player's name is " + bot.getName() + "\n");
                return bot;
            default:
                return null;
        }
    }
}
