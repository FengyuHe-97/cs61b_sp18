package lab11.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private Maze maze;


    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);//https://blog.csdn.net/yongbutingxide/article/details/82669054
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        Queue<Integer> waitingQueue = new LinkedList<>(); // Create and initialize a Queue using a LinkedList

        waitingQueue.add(s);
        marked[s] = true;

        while (!waitingQueue.isEmpty()) {
            int v = waitingQueue.remove();
            announce();
            if (v == t) {
                return;
            }
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    waitingQueue.add(w);
                }
            }
        }
    }


    @Override
    public void solve() {
         bfs();
    }
}

