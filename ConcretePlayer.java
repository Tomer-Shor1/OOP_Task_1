public class ConcretePlayer implements Player{

    private final boolean isPlayerOne;
    private  int wins = 0;
    private int piecesLeft;
    private boolean hasWon = false;

    public ConcretePlayer(boolean isPlayerOne){
        this.isPlayerOne = isPlayerOne;
        if (isPlayerOne){
            this.piecesLeft = 24;
        }
        else {this.piecesLeft = 12;}
    }
    @Override
    public boolean isPlayerOne() {
        return isPlayerOne;
    }
    @Override
    public int getWins() {
        return wins;
    }
    public int getPiecesLeft(){
        return piecesLeft;
    }
    public void playerWon(){
        this.hasWon = true;
        this.wins++;
    }
    public void decreasePieces(){
        piecesLeft--;
    }
    public boolean getHasWon(){
        return this.hasWon;
    }



}
