/**
 *
 */
public class MazeCell {
    public boolean state = false;

    private int column;
    private int row;

    public MazeCell(int posX, int posY) {
        this.column = posX;
        this.row = posY;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", column, row);
    }
}
