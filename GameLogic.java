import java.util.ArrayList;
import java.util.List;


public class GameLogic implements PlayableLogic{

    public GameLogic() {
        reset();
    }

    final int BoardSize = 11;
    private final ConcretePiece [][] Board = new ConcretePiece[BoardSize][BoardSize];
    private boolean secondPlayerTurn = true;
    private Position KingsPosition;
    public ConcretePlayer firstPlayer = new ConcretePlayer(true);
    public ConcretePlayer secondPlayer = new ConcretePlayer(false);
    List<ConcretePiece> pieces = new ArrayList<>();
    List<Position> PositionsList = new ArrayList<>();
    Position lastPosition = null;

    @Override
    public boolean move(Position a, Position b)
    {
        int a_x = a.getRow(), a_y = a.getColumn();
        int b_x = b.getRow(), b_y = b.getColumn();

        if (isSecondPlayerTurn()){
            if (getPieceAtPosition(a).getOwner().isPlayerOne()){
                return false;
            }
        }

        if (!isSecondPlayerTurn()){
            if (!getPieceAtPosition(a).getOwner().isPlayerOne()){
                return false;
            }
        }


        // if there is no piece on the position - impossible
        //if there is a piece on destination position - impossible
        if(Board[a_x][a_y] == null || Board[b_x][b_y]!= null){
            return false;
        }

        // if it's the same position - impossible
        if(a.equals(b)) {return false;}

        //if pawn tries to move to a corner - impossible
        if(getPieceAtPosition(a) instanceof Pawn){
            if ((b_x == 0 && b_y == 0) || (b_x == 0 && b_y ==10) || (b_x == 10 && b_y == 0) || (b_x == 10 && b_y ==10)){
                return false;
            }
        }

        //checks if it moves horizontally or vertically
        if(!validDirection(a,b)) {return false;}


        // check if there is no piece on the path
            if(a.getRow() == b.getRow()){
                if (a.getColumn()<b.getColumn()) {
                    for (int i = a.getColumn() + 1; i < b.getColumn()-1; i++) {
                        if (Board[a.getRow()][i] != null) {return false;}
                    }
                }
                else {
                    for (int i = a.getColumn() - 1; i > b.getColumn(); i--) {
                        if (Board[a.getRow()][i] != null) {return false;}
                        }
                }
            }

            if(a.getColumn() == b.getColumn()){
                if (a.getRow() > b.getRow()) {
                    for (int i = a.getRow() - 1; i > b.getRow(); i--) {
                        if (Board[i][a.getColumn()] != null) {return false;
                        }
                    }
                }
                else {
                    for (int i = a.getRow() + 1; i < b.getRow(); i++) {
                        if (Board[i][a.getColumn()] != null) {return false;}
                    }
                }
            }

        //move the piece
        getPieceAtPosition(a).setPiecePosition(b);
        Board[b.getRow()][b.getColumn()] = Board[a.getRow()][a.getColumn()];
        Board[a.getRow()][a.getColumn()] = null;
        lastPosition = a;


        if (getPieceAtPosition(b) instanceof King){KingsPosition = b;}

        checkIfPieceTraveled(findPositionInList(PositionsList,b.getRow(),b.getColumn()));
        switchTurn();
        CheckIfKilled(b);
        getPieceAtPosition(b).addDistance(distanceBetweenTwoPositions(a,b));
        return true;
    }


    //check if a piece was on a position
    public void checkIfPieceTraveled(Position position){
        if (getPieceAtPosition(position) == null){return;}
        if (!position.getPieces().contains(getPieceAtPosition(position))){
            position.getPieces().add(getPieceAtPosition(position));
        }
    }

    //checks if the move is horizontally or vertically
    public boolean validDirection(Position a, Position b){//

        return !(a.getRow() !=b.getRow() && a.getColumn() != b.getColumn());
    }
    //computes the distance between two positions
    public int distanceBetweenTwoPositions(Position a, Position b){
        //if the piece moves on Y axis
        if(a.getRow() == b.getRow()){
            return Math.abs(a.getColumn()- b.getColumn());
        }

        //if the piece moves on X axis
        if (a.getColumn() == b.getColumn()){
            return Math.abs(a.getRow() - b.getRow());
        }
        else return 0;
    }
    //removes a piece from the board
    public void removePiece(Position a){
        Board[a.getRow()][a.getColumn()] = null;
    }
    // checks if a piece on position p killed an enemy's piece
    public void CheckIfKilled(Position p){

        //king cant attack
        if (getPieceAtPosition(p) instanceof King) {return;}

        int p_row = p.getRow(), p_column = p.getColumn();

        boolean owner = getPieceAtPosition(p).getOwner().isPlayerOne();
        // check whose piece has been eaten
        ConcretePlayer piecesKilledOwner;
        if(getPieceAtPosition(p).getOwner().isPlayerOne()){
            piecesKilledOwner = secondPlayer;
        }
        else {piecesKilledOwner = firstPlayer;}


        //if the attackers piece is not on the bounds
        if(p_column<10 && p_column > 0 && p_row > 0 && p_row <10){
            Position up = new Position(p.getRow(), p.getColumn()-1);
            Position down = new Position(p.getRow(), p.getColumn()+1);
            Position right = new Position(p.getRow()+1, p.getColumn());
            Position left = new Position(p.getRow()-1, p.getColumn());


            if (getPieceAtPosition(up) != null && getPieceAtPosition(up).getOwner().isPlayerOne() != owner
            && !(getPieceAtPosition(up) instanceof King)){
                if(up.getColumn() == 0){
                    getPieceAtPosition(p).killed();
                    piecesKilledOwner.decreasePieces();
                    Board[up.getRow()][up.getColumn()] = null;
                    return;
                }
                Position upper = new Position(up.getRow(),up.getColumn()-1);
                if (getPieceAtPosition(upper) != null && getPieceAtPosition(upper).getOwner().isPlayerOne() == owner){
                    getPieceAtPosition(p).killed();
                    piecesKilledOwner.decreasePieces();
                    Board[up.getRow()][up.getColumn()] = null;
                }
            }

            if (getPieceAtPosition(down) != null  && getPieceAtPosition(down).getOwner().isPlayerOne() != owner
             && !(getPieceAtPosition(down) instanceof King)){
                if (down.getColumn() == 10){
                    getPieceAtPosition(p).killed();
                    piecesKilledOwner.decreasePieces();
                    Board[down.getRow()][down.getColumn()] = null;
                    return;
                }
                Position downer = new Position(down.getRow(),down.getColumn()+1);
                if (getPieceAtPosition(downer)!= null && getPieceAtPosition(downer).getOwner().isPlayerOne() == owner) {
                    getPieceAtPosition(p).killed();
                    piecesKilledOwner.decreasePieces();
                    Board[down.getRow()][down.getColumn()] = null;

                }
            }

            if (getPieceAtPosition(right) != null && getPieceAtPosition(right).getOwner().isPlayerOne() != owner
            && !(getPieceAtPosition(right) instanceof King)){
                if (right.getRow() == 10){
                    getPieceAtPosition(p).killed();
                    piecesKilledOwner.decreasePieces();
                    Board[right.getRow()][right.getColumn()] = null;
                    return;
                }
                Position righter = new Position(right.getRow()+1,right.getColumn());
                if (getPieceAtPosition(righter)!= null && getPieceAtPosition(righter).getOwner().isPlayerOne() == owner){
                    getPieceAtPosition(p).killed();
                    piecesKilledOwner.decreasePieces();
                    Board[right.getRow()][right.getColumn()] = null;
                    return;
                }
            }

            if (getPieceAtPosition(left) != null && getPieceAtPosition(left).getOwner().isPlayerOne() != owner
            && !(getPieceAtPosition(left) instanceof King)){
                if (left.getRow() == 0){
                    getPieceAtPosition(p).killed();
                    piecesKilledOwner.decreasePieces();
                    Board[left.getRow()][left.getColumn()] = null;
                    return;
                }
                Position lefter = new Position(left.getRow()-1,left.getColumn());
                if (getPieceAtPosition(lefter)!= null && getPieceAtPosition(lefter).getOwner().isPlayerOne() == owner){
                    getPieceAtPosition(p).killed();
                    piecesKilledOwner.decreasePieces();
                    Board[left.getRow()][left.getColumn()] = null;
                }
            }
        }

        //if the piece is on the righter or lefter side
        if (p_row == 10 || p_row == 0) {
            Position up = new Position(p.getRow(), p.getColumn() - 1);
            Position down = new Position(p.getRow(), p.getColumn() + 1);

            if (getPieceAtPosition(down) != null && down.getColumn() < 10 && getPieceAtPosition(down).getOwner().isPlayerOne() != owner
            && !(getPieceAtPosition(down) instanceof King )) {
                Position downer = new Position(down.getRow(), down.getColumn() + 1);
                if (getPieceAtPosition(downer) != null && getPieceAtPosition(downer).getOwner().isPlayerOne() == owner || (downer.getRow() == 0 ||downer.getRow() == 10) && downer.getColumn() == 10) {
                    getPieceAtPosition(p).killed();
                    piecesKilledOwner.decreasePieces();
                    Board[down.getRow()][down.getColumn()] = null;
                }
            }
            if (getPieceAtPosition(up) != null && up.getColumn() > 0 && getPieceAtPosition(up).getOwner().isPlayerOne() != owner &&
                    !(getPieceAtPosition(up) instanceof King)) {
                Position upper = new Position(up.getRow(), up.getColumn() - 1);
                if (getPieceAtPosition(upper) != null  && getPieceAtPosition(upper).getOwner().isPlayerOne() == owner || (upper.getRow() == 0 ||upper.getRow() == 10) && upper.getColumn() == 0) {
                    getPieceAtPosition(p).killed();
                    piecesKilledOwner.decreasePieces();
                    Board[up.getRow()][up.getColumn()] = null;
                }
            }
        }

             // if the piece is on the upper or lower bound
             if (p_column ==  0 || p_column == 10){
                 Position right = new Position(p.getRow()+1, p.getColumn());
                 Position left = new Position(p.getRow()-1, p.getColumn());

                 //if one of the neighbours is the king
                 if (getPieceAtPosition(right) != null && right.getRow()<10 && getPieceAtPosition(right).getOwner().isPlayerOne() != owner
                 && !(getPieceAtPosition(right) instanceof King)){
                     Position righter = new Position(right.getRow()+1,right.getColumn());
                     if (getPieceAtPosition(righter)!= null && getPieceAtPosition(righter).getOwner().isPlayerOne() == owner || (righter.getColumn() == 0 ||righter.getColumn() == 10) && righter.getRow() ==10){
                         getPieceAtPosition(p).killed();
                         piecesKilledOwner.decreasePieces();
                         Board[right.getRow()][right.getColumn()] = null;

                     }
                 }

                 if (getPieceAtPosition(left) != null && left.getRow() > 0 && getPieceAtPosition(left).getOwner().isPlayerOne() != owner
                 && !(getPieceAtPosition(left) instanceof King)){
                     Position lefter = new Position(left.getRow()-1,left.getColumn());
                     if (getPieceAtPosition(lefter)!= null && getPieceAtPosition(lefter).getOwner().isPlayerOne() == owner || (lefter.getColumn() == 0 ||lefter.getColumn() == 10) && lefter.getRow() == 0){
                         getPieceAtPosition(p).killed();
                         piecesKilledOwner.decreasePieces();
                         Board[left.getRow()][left.getColumn()] = null;
                     }
                 }
             }
    }
    @Override
    public ConcretePiece getPieceAtPosition(Position position) {
          return Board [position.getRow()][position.getColumn()];
    }
    @Override
    public Player getFirstPlayer() {
        return firstPlayer;
    }
    @Override
    public Player getSecondPlayer() {
        return secondPlayer;
    }

    //@Override
   public boolean isGameFinished() {

        //if second player lost all of his pieces
        if (firstPlayer.getPiecesLeft() == 0){
            secondPlayer.playerWon();
            gameStatistics(false);
            return true;
        }
        //if second player lost pieces
        if (secondPlayer.getPiecesLeft() == 0){
            firstPlayer.playerWon();
            gameStatistics(true);
            return true;
        }

        //if the king is on the bounds
       if ((KingsPosition.getRow() == 0 && KingsPosition.getColumn() == 0) || (KingsPosition.getRow() == 10 && KingsPosition.getColumn() == 0) ||
               (KingsPosition.getRow() == 0 && KingsPosition.getColumn() == 10) || (KingsPosition.getRow() == 10 && KingsPosition.getColumn() == 10)){
            firstPlayer.playerWon();
            gameStatistics(true);
            return true;
       }

       //if the king is captured
       if (isKingCaptured()) {
           secondPlayer.playerWon();
           gameStatistics(false);
           return true;
       }
       return false;
   }

    @Override
    public boolean isSecondPlayerTurn() {
        return secondPlayerTurn;
    }
    @Override
    public void reset() {
        Position kingsPosition = new Position(5, 5);
        KingsPosition = kingsPosition;
        secondPlayerTurn = true;
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                Board[i][j] = null;
                Position p = new Position(i,j);
                PositionsList.add(p);
            }
        }
        //initialize the board
        Board[5][3] = new Pawn(firstPlayer, 5, 3, "D1");
        Board[5][4] = new Pawn(firstPlayer, 5, 4, "D3");
        Board[3][5] = new Pawn(firstPlayer, 3, 5, "D5");
        Board[4][5] = new Pawn(firstPlayer, 4, 5, "D6");
        Board[6][5] = new Pawn(firstPlayer, 6, 5, "D8");
        Board[7][5] = new Pawn(firstPlayer, 7, 5, "D9");
        Board[5][6] = new Pawn(firstPlayer, 5, 6, "D11");
        Board[5][7] = new Pawn(firstPlayer, 5, 7, "D13");
        Board[5][5] = new King(firstPlayer);
        Board[4][4] = new Pawn(firstPlayer, 4, 4, "D2");
        Board[4][6] = new Pawn(firstPlayer, 4, 6, "D10");
        Board[6][4] = new Pawn(firstPlayer, 6, 4, "D4");
        Board[6][6] = new Pawn(firstPlayer, 6, 6, "D12");
        Board[1][5] = new Pawn(secondPlayer, 1, 5, "A12");
        Board[9][5] = new Pawn(secondPlayer, 9, 5, "A13");
        Board[5][1] = new Pawn(secondPlayer, 5, 1, "A6");
        Board[5][9] = new Pawn(secondPlayer, 5, 9, "A19");
        Board[0][3] = new Pawn(secondPlayer, 0, 3, "A7");
        Board[0][4] = new Pawn(secondPlayer, 0, 4, "A9");
        Board[0][5] = new Pawn(secondPlayer, 0, 5, "A11");
        Board[0][6] = new Pawn(secondPlayer, 0, 6, "A15");
        Board[0][7] = new Pawn(secondPlayer, 0, 7, "A17");
        Board[1][5] = new Pawn(secondPlayer, 1, 5, "A12");
        Board[3][10] = new Pawn(secondPlayer, 3, 10, "A20");
        Board[4][10] = new Pawn(secondPlayer, 4, 10, "A21");
        Board[5][10] = new Pawn(secondPlayer, 5, 10, "A22");
        Board[6][10] = new Pawn(secondPlayer, 6, 10, "A23");
        Board[7][10] = new Pawn(secondPlayer, 7, 10, "A24");
        Board[5][9] = new Pawn(secondPlayer, 5, 9, "A19");
        Board[3][0] = new Pawn(secondPlayer, 3, 0, "A1");
        Board[4][0] = new Pawn(secondPlayer, 4, 0, "A2");
        Board[5][0] = new Pawn(secondPlayer, 5, 0, "A3");
        Board[6][0] = new Pawn(secondPlayer, 6, 0, "A4");
        Board[7][0] = new Pawn(secondPlayer, 7, 0, "A5");
        Board[5][1] = new Pawn(secondPlayer, 5, 1, "A6");
        Board[10][3] = new Pawn(secondPlayer, 10, 3, "A8");
        Board[10][4] = new Pawn(secondPlayer, 10, 4, "A10");
        Board[10][5] = new Pawn(secondPlayer, 10, 5, "A14");
        Board[10][6] = new Pawn(secondPlayer, 10, 6, "A16");
        Board[10][7] = new Pawn(secondPlayer, 10, 7, "A18");
        Board[9][5] = new Pawn(secondPlayer, 9, 5, "A13");

        //make a list for all the pieces on the board
        for (int i = 0 ;  i<11 ; i++){
            for (int j = 0 ; j< 11 ; j++){
                if (Board[i][j] != null){
                    pieces.add(Board[i][j]);
                    findPositionInList(PositionsList,i,j).getPieces().add(Board[i][j]);
                    findPositionInList(PositionsList,i,j).setHasPieceInStart();
                }
            }
        }
    }

    //switch turns
    public void switchTurn(){
        secondPlayerTurn = !secondPlayerTurn;
    }

    //returns a position with given x and y
    public Position findPositionInList(List<Position> list,int x, int y){
        for (Position position : list) {
            if (position.getRow() == x && position.getColumn() == y) {
                return position;
            }
        }
        return null;
    }

    @Override
    public void undoLastMove() {
    switchTurn();}
    //returns board size
    @Override
    public int getBoardSize() {
        return BoardSize;}

    //checks if the king is captured
    public boolean isKingCaptured(){
        Position KingPosition = KingsPosition;
        int x = KingPosition.getRow();
        int y = KingPosition.getColumn();

        if (x>0 && x<10 && y>0 && y<10){
            Position up = new Position(x,y-1);
            Position down = new Position(x,y+1);
            Position right = new Position(x+1,y);
            Position left = new Position(x-1,y);

            if (getPieceAtPosition(up) != null && getPieceAtPosition(down) != null && getPieceAtPosition(right) != null && getPieceAtPosition(left) != null ){
                if(getPieceAtPosition(up).getOwner() != firstPlayer && getPieceAtPosition(down).getOwner() != firstPlayer &&
                        getPieceAtPosition(left).getOwner() != firstPlayer && getPieceAtPosition(right).getOwner() != firstPlayer ){
                    return true;
                }
            }
        }

        //side cases
        if (x == 0){
            Position up = new Position(x,y-1);
            Position down = new Position(x,y+1);
            Position right = new Position(x+1,y);

            if (getPieceAtPosition(up) != null && getPieceAtPosition(down) != null && getPieceAtPosition(right) != null  ){
                if(getPieceAtPosition(up).getOwner() != firstPlayer && getPieceAtPosition(down).getOwner() != firstPlayer &&
                         getPieceAtPosition(right).getOwner() != firstPlayer ){
                    return true;
                }
            }
        }

        if (x == 10){
            Position up = new Position(x,y-1);
            Position down = new Position(x,y+1);
            Position left = new Position(x-1,y);
            if (getPieceAtPosition(up) != null && getPieceAtPosition(down) != null && getPieceAtPosition(left) != null  ){
                if(getPieceAtPosition(up).getOwner() != firstPlayer && getPieceAtPosition(down).getOwner() != firstPlayer &&
                        getPieceAtPosition(left).getOwner() != firstPlayer ){
                    return true;
                }
            }
        }

        if (y == 0){
            Position down = new Position(x,y+1);
            Position right = new Position(x+1,y);
            Position left = new Position(x-1,y);
            if (getPieceAtPosition(right) != null && getPieceAtPosition(down) != null && getPieceAtPosition(left) != null  ){
                if(getPieceAtPosition(right).getOwner() != firstPlayer && getPieceAtPosition(down).getOwner() != firstPlayer &&
                        getPieceAtPosition(left).getOwner() != firstPlayer ){
                    return true;
                }
            }
        }

        if (y == 10){
            Position up = new Position(x,y-1);
            Position right = new Position(x+1,y);
            Position left = new Position(x-1,y);
            if (getPieceAtPosition(right) != null && getPieceAtPosition(left) != null && getPieceAtPosition(up) != null  ){
                if(getPieceAtPosition(right).getOwner() != firstPlayer && getPieceAtPosition(up).getOwner() != firstPlayer &&
                        getPieceAtPosition(left).getOwner() != firstPlayer ){
                    return true;
                }
            }
        }
        return false;
    }

    //get and print game statistics
    public void gameStatistics(boolean playerWon){

        //sort by number of moves
        List <ConcretePiece> listOfAllPiecesMoves = new ArrayList<>();
        listOfAllPiecesMoves = pieces;
        listOfAllPiecesMoves.sort(new NumberOfMovesComparator(playerWon));
        List <ConcretePiece> SortedListOfAllPiecesMoves = listOfAllPiecesMoves;

        //sort by number of kills
        List <ConcretePiece> listOfAllPieceskills = new ArrayList<>();
        listOfAllPieceskills = pieces;
        listOfAllPieceskills.sort(new NumberOfKillsComparator(playerWon));
        List <ConcretePiece> SortedListOfAllPiecesKills = listOfAllPieceskills;

        //sort by distance
        List <ConcretePiece> listOfAllPiecesDistance = new ArrayList<>();
        listOfAllPiecesDistance = pieces;
        listOfAllPiecesDistance.sort(new DistanceOfPieceComparator(playerWon));
        List <ConcretePiece> SortedListOfAllPiecesDistance = listOfAllPiecesDistance;

        //sort positions
        List<Position> listOfAllPositions = new ArrayList<>();
        listOfAllPositions = PositionsList;
        listOfAllPositions.sort(new NumberOfPiecesOnPosition());



        //#Prints

        //print moves
        for (ConcretePiece sortedListOfAllPiecesMove : SortedListOfAllPiecesMoves) {
            if (sortedListOfAllPiecesMove.getOwner().isPlayerOne() == playerWon && sortedListOfAllPiecesMove.getMoves().size() >= 2) {
                System.out.print(sortedListOfAllPiecesMove.getName() + ":" + "[");
                for (int j = 0; j < sortedListOfAllPiecesMove.getMoves().size(); j++) {
                    if (j == sortedListOfAllPiecesMove.getMoves().size() - 1) {
                        System.out.print("(" + sortedListOfAllPiecesMove.getMoves().get(j)[0] + "," + sortedListOfAllPiecesMove.getMoves().get(j)[1] + ")");
                    } else {
                        System.out.print("(" + sortedListOfAllPiecesMove.getMoves().get(j)[0] + "," + sortedListOfAllPiecesMove.getMoves().get(j)[1] + "),");
                    }
                }
                System.out.println("]");
            }
        }

        for (ConcretePiece sortedListOfAllPiecesMove : SortedListOfAllPiecesMoves) {
            if (sortedListOfAllPiecesMove.getOwner().isPlayerOne() != playerWon && sortedListOfAllPiecesMove.getMoves().size() >= 2) {
                System.out.print(sortedListOfAllPiecesMove.getName() + ":" + "[");
                for (int j = 0; j < sortedListOfAllPiecesMove.getMoves().size(); j++) {
                    if (j == sortedListOfAllPiecesMove.getMoves().size() - 1) {
                        System.out.print("(" + sortedListOfAllPiecesMove.getMoves().get(j)[0] + "," + sortedListOfAllPiecesMove.getMoves().get(j)[1] + ")");
                    } else {
                        System.out.print("(" + sortedListOfAllPiecesMove.getMoves().get(j)[0] + "," + sortedListOfAllPiecesMove.getMoves().get(j)[1] + "),");
                    }
                }
                System.out.println("]");
            }
        }

        System.out.println("***************************************************************************");

        //print kills
        for (int i = SortedListOfAllPiecesKills.size()-1 ; i >= 0; i--){
            if (SortedListOfAllPiecesKills.get(i).getKills() > 0){
                System.out.println(SortedListOfAllPiecesKills.get(i).getName() + ":" + SortedListOfAllPiecesKills.get(i).getKills() + " kills" );
            }
        }

        System.out.println("***************************************************************************");

        //print distance
        for (int i = 0; i<SortedListOfAllPiecesDistance.size();i++){
            if (SortedListOfAllPiecesDistance.get(i).getDistance() > 0){
                System.out.println(SortedListOfAllPiecesMoves.get(i).getName() + ": " + SortedListOfAllPiecesDistance.get(i).getDistance() + " squares");
            }
        }

        System.out.println("***************************************************************************");

        for (Position listOfAllPosition : listOfAllPositions) {
            if (listOfAllPosition.getPiecesOnPosition()  > 1) {
                System.out.println("(" + listOfAllPosition.getRow() + "," + listOfAllPosition.getColumn() + ") " + listOfAllPosition.getPiecesOnPosition() + " pieces");
            }
        }

        System.out.println("***************************************************************************");
    }
}



