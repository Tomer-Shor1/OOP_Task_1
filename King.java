public class King extends ConcretePiece {

    private final String type = "♔";
    private int x = 5;
    private int y = 5;



    public King(Player owner) {
        super(owner, "♔",5,5, "K7");
    }

    public Player getOwner(){
       return super.getOwner();
    }

    public String getType(){
        return this.type;
    }
    public Position getKingPosition(){
        Position p = new Position(x,y);
        return p;
    }
    public void setKingPosition(Position p){
        this.x = p.getRow();
        this.y = p.getColumn();
    }
}

