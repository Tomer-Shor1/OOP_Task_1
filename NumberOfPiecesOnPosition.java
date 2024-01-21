import java.util.Comparator;

public class NumberOfPiecesOnPosition implements Comparator<Position> {

    @Override
    public int compare(Position o1, Position o2) {

        //sort by smaller x or y
        if (o1.getPiecesOnPosition() == o2.getPiecesOnPosition()){
            if (o1.getRow() < o2.getRow()){
                return -1;
            }
            else if (o1.getRow() == o2.getRow()){
                if (o1.getColumn() < o2.getColumn()){
                    return -1;
                }
            }
            else return 1;
        }
        return o2.getPiecesOnPosition()-o1.getPiecesOnPosition();
    }
}