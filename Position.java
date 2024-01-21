import java.util.ArrayList;
import java.util.List;

public class Position {


        private int row;
        private int col;
        List<ConcretePiece> pieces = new ArrayList<>();
        //int hasPieceInStart = 0; - optional if starting pieces does not count as stepped piece

        public Position(int row, int col){
            this.row = row;
            this.col = col;
           // this.hasPieceInStart = 0;
        }

        public int getRow(){
            return row;
        }

        public int getColumn(){
            return col;
        }

        @Override
        public String toString(){
            return " ( " + row + " , " + col + " ) ";
        }

        public boolean equals( Position b){
            return this.getRow() == b.getRow() && this.getColumn() == b.getColumn();
        }
        public int getPiecesOnPosition(){
            return this.pieces.size();
        }
        public List<ConcretePiece> getPieces(){
            return this.pieces;
        }
//        public void setHasPieceInStart(){
//            hasPieceInStart = 1;
//        }
//        public int getHasPieceOnStart(){
//            return hasPieceInStart;
//        }
}


