public class Pawn extends ConcretePiece{
    private final String type = "♙";
    private int x;
    private int y;
    private String name;
    public Pawn(Player owner,int x,int y,String name) {
        super(owner, "♙",x,y,name);
    }

    public Player getOwner(Player owner){
        return super.getOwner();
    }

    public String getType(){return this.type;}
    public int[] getPosition(){
        int [] arr = new int[2];
        arr[0] = this.x;
        arr[1] = this.y;
        return arr;
    }
}
