import java.util.ArrayList;
import java.util.List;

public class Position {


        private int row;
        private int col;
        List<ConcretePiece> pieces = new ArrayList<>();

        public Position(int row, int col){
            this.row = row;
            this.col = col;
        }

        public int getRow(){
            return row;
        }

        public int getColumn(){
            return col;
        }

        public void setRow(int row){
            this.row = row;
        }

        public void setColumn(int column){
            this.col = column;
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
}


