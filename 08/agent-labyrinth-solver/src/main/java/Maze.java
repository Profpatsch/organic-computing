import java.util.*;

public class Maze implements Iterable<List<MazeCell>> {

    /** height **/
    private int rows;
    /** width **/
    private int columns;

    /** Do you dare enter? **/
    private MazeCell entry;
    /** You’re never going to find this. **/
    private MazeCell exit;

    /** Stores all MazeCells **/
    List<List<MazeCell>> maze;

    private Maze(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        perfectMaze();
    }

    /**
     * Makes this Maze a perfect maze.
     */
    private void perfectMaze() {
        emptyMaze();
    }

    /**
     * Empties the maze, creating outer walls with two holes (entry and exit).
     */
    private void emptyMaze() {
        maze = new ArrayList<List<MazeCell>>();
        for (int i = 0; i < rows; i++) {
            List<MazeCell> row = new ArrayList<MazeCell>();
            for (int j = 0; j < columns; j++) {
                row.add(new MazeCell(j, i));
            }
            maze.add(row);
        }
        // walls
        for (List<MazeCell> row : maze) {
            for (MazeCell cell : row) {
                if (isEdgeWall(cell))
                    cell.state = true;
            }
        }

        entry = createHoleInEdgeWall();
        exit = createHoleInEdgeWall();
    }

    /**
     * Can be used to create an entry or an exit. Or multiple if you so desire.
     * @return The removed wall
     */
    private MazeCell createHoleInEdgeWall() {
        List<MazeCell> walls = getWalls();
        do {
            int rand = new Random().nextInt(walls.size());
            MazeCell candidate = walls.get(rand);
            if (isEdgeWall(candidate) && (!isEdgeCorner(candidate))) {
                candidate.state = false;
                return candidate;
            }
        } while (true);
    }

    /**
     * Returns a perfect maze.
     * @param rows height
     * @param columns width
     */
    public static Maze createPerfectMaze(int rows, int columns) {
        Maze m = new Maze(rows, columns);
        m.perfectMaze();
        return m;
    }

    /**
     * Is on the outer bounds of this small realm of madness.
     * @param cell
     * @return YES OR NO.
     */
    private boolean isEdgeWall(MazeCell cell) {
        int column = cell.getColumn();
        int row = cell.getRow();

        if (column == 0 || row == 0)
            return true;
        if (column == columns-1 || row == rows-1)
            return true;
        return false;
    }

    /**
     * You are cornered. You can’t be more in the corner than this.
     * @param cell
     * @return
     */
    private boolean isEdgeCorner(MazeCell cell) {
        // TODO: implement
        return false;
    }

    /**
     * Give me all walls in the maze.
     * @return list of walls
     */
    private List<MazeCell> getWalls() {
        ArrayList<MazeCell> set = new ArrayList<MazeCell>();
        for (List<MazeCell> row : maze) {
            for (MazeCell cell : row) {
                if (cell.state)
                    set.add(cell);
            }
        }
        return set;
    }

    @Override
    public Iterator iterator() {
        return maze.listIterator();
    }

    @Override
    public String toString() {
        String s = String.format("(%s rows, %s columns; entry: %s, exit: %s):\n", rows, columns, entry, exit);

        for (List<MazeCell> row : maze) {
            for (MazeCell mazeCell : row) {
                String x = mazeCell.state ? "x " : "o ";
                s = s.concat(x);
            }
            s = s.concat("\n");
        }
        return s;
    }
}
