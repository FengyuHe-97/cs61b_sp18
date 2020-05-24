package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);


    private int size;
    private static class Position {
        private int x;
        private int y;
    }

/**
 * adds a hexagon of side length s to a given position in the world
 */
    public static void addHexagon(TETile[][] tiles, Position p, int size, TETile t){
        int startx=p.x;
        int starty=p.y;
        int row=0;
        for(int y=starty;y<starty+size;y+=1) {
            row=row+1;
            for(int x=startx-row+1;x<startx+size+row-1;x+=1){
                tiles[x][y+size*2-2*row+1]=t;
                tiles[x][y]=t;

            }
        }
    }

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.GRASS;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.TREE;
            case 3: return Tileset.SAND;
            case 4: return Tileset.MOUNTAIN;
            default: return Tileset.NOTHING;
        }
    }

    public static void fillWithEmpty(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }



    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] hexagonTiles = new TETile[WIDTH][HEIGHT];

        Position p = new Position();
        int size = 2;


        p.x = 20;
        p.y = 20;
        fillWithEmpty(hexagonTiles);
        addHexagon(hexagonTiles,  p,  size, randomTile());

        ter.renderFrame(hexagonTiles);
    }


}
