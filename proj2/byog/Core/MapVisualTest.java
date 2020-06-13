package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.Core.MapGenerator;

public class MapVisualTest {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 60;

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        MapGenerator map = new MapGenerator();

        map.initializeTheWorld(WIDTH, HEIGHT);

        map.demo();



        ter.renderFrame(map.world);
    }
}
