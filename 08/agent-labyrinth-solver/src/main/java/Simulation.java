import sim.engine.SimState;

import java.util.List;

/**
 *
 */
public class Simulation extends SimState {


    public Simulation(long seed) {
        super(seed);
    }

    public static void main(String[] args) {

        Maze m = Maze.createPerfectMaze(10, 30);

        System.out.println(m);

       // doLoop(Simulation.class, args);
    }

}

