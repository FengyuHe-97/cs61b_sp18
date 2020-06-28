package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.Core.MapGenerator;

public class MapVisualTest {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 60;
    public static final int HEIGHT = 30;

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        MapGenerator.getSizeOfWorld(WIDTH, HEIGHT);
        MapGenerator.initializeTheWorld();

        MapGenerator.demo();



        ter.renderFrame(MapGenerator.world);
    }
}
