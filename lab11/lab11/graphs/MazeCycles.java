package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int[] comeFrom = new int[maze.V()];//记得要初始化
    boolean findCycle = false;

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        int startX = 1;
        int startY = 1;
        int targetX = maze.N();
        int targetY = maze.N();

        int s = maze.xyTo1D(startX, startX);

        marked[s] = true;
        edgeTo[s] = s;
        announce();

        dfs(s);

    }

    private void dfs(int v){
        if (findCycle) {
            return;
        }

        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (findCycle) {
                return;
            }

            if (!marked[w]) {
                marked[w] = true;
                comeFrom[w] = v;
                announce();
                dfs(w);
            }
            if (marked[w] && comeFrom[v] != w && comeFrom[w] != v){
                findCycle = true;
                int temp = v;
                edgeTo[w] = v;
                while(temp != w) {
                    edgeTo[temp] = comeFrom[temp];
                    temp = comeFrom[temp];
                }
                announce();
                return;
            }

            }
        }



    // Helper methods go here
}

