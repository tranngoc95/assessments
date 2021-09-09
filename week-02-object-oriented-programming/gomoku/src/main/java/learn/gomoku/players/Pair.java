package learn.gomoku.players;

public class Pair {

    private int count;
    private char[] symbol;

    public Pair(int count, char[] symbol) {
        this.count = count;
        this.symbol = symbol;
    }

    public int getCount() {
        return count;
    }

    public int getSpace() {
        int space = 0;
        for(int i=0; i<symbol.length; i++){
            if(symbol[i]!=0){
                break;
            }
            space++;
        }
        return space;
    }

    public char[] getSymbol() {
        return symbol;
    }
}
