import java.util.*;

public class ConcretePiece implements Piece{

    private final Player owner;
    private final String type;
    private String name;
    private int kills = 0;
    private int x;
    private int y;
    private List<int []> Moves = new ArrayList<>();
    private int distance ;


    private boolean hasRemoved;
    public ConcretePiece(Player owner, String type,int x,int y,String name){
        this.owner = owner;
        this.type = type;
        this.hasRemoved = false;
        this.x = x;
        this.y = y;
        int [] initial_position = new int[2];
        initial_position[0] = x;
        initial_position[1] = y;
        Moves.add(initial_position);
        this.name = name;
        this.distance = 0;
    }
    @Override
    public Player getOwner() {
    return owner;
    }

    @Override
    public String getType() {
        return type;
    }

    public void Remove(){
        this.hasRemoved = true;
    }

    public void killed(){
        kills++;
    }
    public int getKills(){
        return kills;
    }
    public int[] getPiecePosition(){
        int [] arr = new int[2];
        arr[0] = x;
        arr[1] = y;
        return arr;
    }
    public void setPiecePosition(Position p){
        this.x = p.getRow();
        this.y = p.getColumn();
        int [] arr = new int[2];
        arr[0] = p.getRow();
        arr[1] = p.getColumn();
        Moves.add(arr);
    }
    public List<int []> getMoves(){

        return Moves;
    }
    public String getName(){
        return this.name;
    }
    public void addDistance(int distance){
        this.distance += distance;
    }
    public int getDistance(){
        return distance;
    }

}


class DistanceOfPieceComparator implements Comparator<ConcretePiece> {

    private boolean PlayerWon;

    public DistanceOfPieceComparator(boolean playerWon){}


    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {
        if (o1.getDistance() == o2.getDistance()){
            if (o1.getOwner().isPlayerOne() == PlayerWon){
                return -1;
            }
        }

        return o2.getDistance()- o1.getDistance();
    }
}

class NumberOfKillsComparator implements Comparator<ConcretePiece> {

    private boolean playerOneWon;

    public NumberOfKillsComparator(boolean playerOneWon){}


    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2)
    {
        if (o1.getKills() - o2.getKills() == 0){
            if (playerOneWon){
                return 1;
            }
            else return -1;
        }
        return o1.getKills() - o2.getKills();
    }
}

class NumberOfMovesComparator implements Comparator<ConcretePiece> {

    private boolean playerOneWon;

    public NumberOfMovesComparator(boolean playerOneWon){
    }

    @Override
    public int compare(ConcretePiece o1, ConcretePiece o2) {

        if (o1.getMoves().size() - o2.getMoves().size() == 0 ){
            if (playerOneWon){
                return 1;
            }
            else return -1;
        }
        return o1.getMoves().size() - o2.getMoves().size();
    }
}

