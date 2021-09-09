package learn.gomoku.players;

import learn.gomoku.game.Board;
import learn.gomoku.game.Gomoku;
import learn.gomoku.game.Stone;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class BotPlayer implements Player{
    private final String name;

    public BotPlayer(){
        name = "Simple Bot";
    }

    private Board board = new Board(Gomoku.WIDTH);
    private final Random random = new Random();

    HashMap<Stone, int[]> botMoves = new HashMap<>();
//    HashMap<Stone, int[]> opponentMoves = new HashMap<>();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Stone generateMove(List<Stone> previousMoves) {
        boolean isBlack = true;
        if (previousMoves != null && !previousMoves.isEmpty()) {
            Stone lastMove = previousMoves.get(previousMoves.size() - 1);
            isBlack = !lastMove.isBlack();
            board.addStone(lastMove);
//            opponentMoves.put(lastMove, null);
            if(previousMoves.size()>1){
                botMoves.put(previousMoves.get(previousMoves.size() - 2), null);
                board.addStone(previousMoves.get(previousMoves.size() - 2));
            }
        }
        if(botMoves.isEmpty()){
            return new Stone(
                    random.nextInt(Gomoku.WIDTH),
                    random.nextInt(Gomoku.WIDTH), isBlack);
        }


        // Choose a stone to base on that to generate move
        Stone maxScoreStone = (Stone) botMoves.keySet().toArray()[0];
        int maxScore=0;
        for(Stone stone : botMoves.keySet()){
            botMoves.put(stone, new int[]{countHorizontal(stone), countVertical(stone), countDiagonalUp(stone), countDiagonalDown(stone)});
            int sum= IntStream.of(botMoves.get(stone)).sum();
            if(sum>maxScore){
                maxScore=sum;
                maxScoreStone=stone;
            }
        }

        // Choose move's direction
        int maxAt = 0;
        for (int i = 0; i < botMoves.get(maxScoreStone).length; i++) {
            maxAt = botMoves.get(maxScoreStone)[i] > botMoves.get(maxScoreStone)[maxAt] ? i : maxAt;
        }

        // Deciding a placement
        int[][] delta = new int[2][2];
        switch (maxAt){
            // Horizontal
            case 0:
                delta[0] = new int[]{1,0};
                delta[1] = new int[]{-1,0};
                break;
            // Vertical
            case 1:
                delta[0] = new int[]{0,1};
                delta[1] = new int[]{0,-1};
                break;
            // Diagonal Down
            case 2:
                delta[0] = new int[]{1,1};
                delta[1] = new int[]{-1,-1};
                break;
            // Diagonal Up
            case 3:
            default:
                delta[0] = new int[]{-1,1};
                delta[1] = new int[]{1,-1};
        }

        char symbol = isBlack ? 'X' : 'O';
        for (int[] ints : delta) {
            int r = maxScoreStone.getRow() + ints[0];
            int c = maxScoreStone.getColumn() + ints[1];
            while(r >= 0 && r < Gomoku.WIDTH && c >= 0 && c < Gomoku.WIDTH && board.getBoard()[r][c]==symbol){
                r+=ints[0];
                c+=ints[1];
            }

            if (r >= 0 && r < Gomoku.WIDTH && c >= 0 && c < Gomoku.WIDTH && board.getBoard()[r][c] == 0) {
                return new Stone(r, c, isBlack);
            }
        }

        return new Stone(
                random.nextInt(Gomoku.WIDTH),
                random.nextInt(Gomoku.WIDTH), isBlack);

    }

    private int countHorizontal(Stone stone) {
        Pair pair1 = count(stone, 1, 0);
        Pair pair2 = count(stone, -1, 0);

        int total = pair1.getCount() + pair2.getCount();

        if(total<5){
            return pair1.getSpace()+pair2.getSpace()+total>5 ? total : 0;
        }
        else {
            return 0;
        }
    }

    private int countVertical(Stone stone) {
        Pair pair1 = count(stone, 0, 1);
        Pair pair2 = count(stone, 0, -1);
        int total = pair1.getCount() + pair2.getCount();

        if(total<5){
            return pair1.getSpace()+pair2.getSpace()+total>5 ? total : 0;
        }
        else {
            return 0;
        }
    }

    private int countDiagonalDown(Stone stone) {
        Pair pair1 = count(stone, 1, 1);
        Pair pair2 = count(stone, -1, -1);
        int total = pair1.getCount() + pair2.getCount();
        if(total<5){
            return pair1.getSpace()+pair2.getSpace()+total>5 ? total : 0;
        }
        else {
            return 0;
        }
    }

    private int countDiagonalUp(Stone stone) {
        Pair pair1 = count(stone, -1, 1);
        Pair pair2 = count(stone, 1, -1);
        int total = pair1.getCount() + pair2.getCount();
        if(total<5){
            return pair1.getSpace()+pair2.getSpace()+total>5 ? total : 0;
        }
        else {
            return 0;
        }
    }

    private Pair count(Stone stone, int deltaRow, int deltaCol) {

        int result = 0;
        int r = stone.getRow() + deltaRow;
        int c = stone.getColumn() + deltaCol;
        char symbol = stone.isBlack() ? 'X' : 'O';

        while (r >= 0 && r < Gomoku.WIDTH && c >= 0 && c < Gomoku.WIDTH && board.getBoard()[r][c] == symbol) {
            result++;
            r += deltaRow;
            c += deltaCol;
        }

        if(4-result<=0){
            return new Pair(result, null);
        } else {
            char[] sym = new char[4-result];
            for(int i=0; i<4-result; i++){
                if(r >= 0 && r < Gomoku.WIDTH && c >= 0 && c < Gomoku.WIDTH){
                    sym[i] = board.getBoard()[r][c];
                    r += deltaRow;
                    c += deltaCol;
                }
                else {
                    break;
                }

            }
            return new Pair(result, sym);
        }


    }
}
