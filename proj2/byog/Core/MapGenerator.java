package byog.Core;

import byog.TileEngine.*;
import edu.princeton.cs.introcs.StdDraw;
import java.util.Random;
import byog.Core.LinkedListDeque;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.Font;

public class MapGenerator {

    private static int WIDTH;
    private static int HEIGHT;

    private static long SEED;
    private static Random RANDOM = new Random(SEED);

    public static TETile[][] world;

    public static TERenderer ter = new TERenderer();

    /*玩家player所在位置*/
    static int x = 0;
    static int y = 0;

    /**
     * 其实没必要用LinkedListDeque,用java.util.ArrayList就可以但我想试一下
     */
    static LinkedListDeque<Position> pendingTiles  = new LinkedListDeque<>();


    /**
     * 确定 SEED
     */
    public static void getSEED(long seed) {
        SEED = seed;
    }

    /**
     * 确定 WIDTH 和 HEIGHT
     */
    public static void getSizeOfWorld(int width, int height){
        WIDTH = width;
        HEIGHT = height;
    }

    /**
     * 初始化世界,给world赋值nothing
     */
    public static void initializeTheWorld(){
        world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public static void demo() {
        Position p = new Position(20, 20, "down", "hallway");
        generateRoom(p);

        while (!pendingTiles.isEmpty()) {
            removePendingTiles(pendingTiles.removeFirst());
        }

        world[0][10] = Tileset.LOCKED_DOOR;
    }

    public static void generateWorld() {
        Position p = new Position(0, 10, "right", "hallway");
        generateRoom(p);

        while (!pendingTiles.isEmpty()) {
            removePendingTiles(pendingTiles.removeFirst());
        }

        world[0][10] = Tileset.LOCKED_DOOR;
    }



    public void fromStringToSeed(String input){


    }


    private static class Position {
        public int x;
        public int y;
        public String towards;
        public String region;

        public Position(int xx, int yy, String twd, String reg){
            x = xx;
            y = yy;
            towards = twd;
            region = reg;
        }
    }

    /**
     * 包含区域左下和右上坐标的子类
     */
    private static class CoverArea {
        private int startX;
        private int startY;
        private int endX;
        private int endY;

        public  CoverArea(int x, int y, int x2, int y2) {
            startX = x;
            startY = y;
            endX = x2;
            endY = y2;
        }

    }


    public static void generateRoom(Position p){
        int roomWidth = RandomUtils.uniform(RANDOM, 4, 9);
        int roomHeight = RandomUtils.uniform(RANDOM, 4, 9);
        int startX, startY;
        int wallOpeningX = p.x;
        int wallOpeningY = p.y;

        //可以写成一个method，但是懒得改
        switch (p.towards) {
            case "up":
                p.y += 1;
                break;
            case "down":
                p.y -= 1;
                break;
            case "left":
                p.x -= 1;
                break;

            case "right":
                p.x += 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + p.towards);
        }
        switch (p.towards) {
            case "up":
                startX = p.x - RandomUtils.uniform(RANDOM, 1, roomWidth - 2);
                startY = p.y;
                break;
            case "down":
                startX = p.x - RandomUtils.uniform(RANDOM, 1, roomWidth - 2);
                startY = p.y - roomHeight + 1;
                break;
            case "left":
                startX = p.x - roomWidth + 1;
                startY = p.y - RandomUtils.uniform(RANDOM, 1, roomHeight - 2);
                break;
            case "right":
                startX = p.x;
                startY = p.y - RandomUtils.uniform(RANDOM, 1, roomHeight - 2);
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + p.towards);
        }



        if(isConflict(startX, startY, startX + roomWidth - 1, startY + roomHeight - 1)){
            // 若有冲突，直接填充墙孔
            world[wallOpeningX][wallOpeningY] = Tileset.WALL;
        } else {
            CoverArea ca = new CoverArea(startX, startY,startX + roomWidth - 1, startY + roomHeight - 1);
            // 若无冲突， 填充地板和四个墙壁
            fillTiles(startX + 1, startY + 1, startX + roomWidth - 2,
                    startY + roomHeight - 2, Tileset.FLOOR );
            fillTiles(startX, startY, startX + roomWidth - 1, startY, Tileset.WALL);
            fillTiles(startX, startY + roomHeight - 1, startX + roomWidth - 1,
                    startY + roomHeight - 1, Tileset.WALL);
            fillTiles(startX, startY, startX, startY + roomHeight - 1, Tileset.WALL);
            fillTiles(startX + roomWidth -1, startY, startX + roomWidth - 1,
                    startY + roomHeight - 1, Tileset.WALL);
            world[p.x][p.y] = Tileset.FLOOR;

            generatePendingTiles( ca,  p, RandomUtils.uniform(RANDOM, 2, 3));

        }
    }

    public static void generateHallway(Position p){
        switch (p.towards) {
            case "up":
                drawVerticalHallway(p);
                break;
            case "down":
                drawVerticalHallway(p);
                break;
            case "left":
                drawHorizontalHallway(p);
                break;
            case "right":
                drawHorizontalHallway(p);
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + p.towards);
        }
    }

    public static void drawVerticalHallway(Position p) {
        int length = RandomUtils.uniform(RANDOM, 4, 9);

        int wallOpeningX = p.x;
        int wallOpeningY = p.y;

        switch (p.towards) {
            case "up":
                p.y += 1;
                break;
            case "down":
                p.y -= 1;
                break;
            case "left":
                p.x -= 1;
                break;
            case "right":
                p.x += 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + p.towards);
        }


        int startX = p.x;
        int startY = p.y;
        int endX, endY;
        switch (p.towards) {
            case "up":
                endX = startX;//特别注意，endX永远应该是大的数字，在down的时候容易出问题
                endY = startY + length - 1;
                break;
            case "down":
                // 这里原本的start变成end
                endX = startX;
                endY = startY;
                startY = endY - length + 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + p.towards);
        }


        if (isConflict(startX - 1, startY, endX + 1, endY)) {
            // 若有冲突，直接填充墙孔
            world[wallOpeningX][wallOpeningY] = Tileset.WALL;
        } else {
            // 若无冲突， 填充地板和四侧墙壁，再把入口改为地砖
            fillTiles(startX, startY, endX, endY, Tileset.FLOOR);

            fillTiles(startX - 1, startY, endX - 1, endY, Tileset.WALL);
            fillTiles(startX + 1, startY, endX + 1, endY, Tileset.WALL);
            //复杂了，不想改
            fillTiles(startX - 1, startY,startX + 1, startY, Tileset.WALL);
            fillTiles(startX - 1, endY,startX + 1, endY, Tileset.WALL);
            world[p.x][p.y] = Tileset.FLOOR;

            //打洞
            CoverArea ca = new CoverArea(startX - 1, startY,endX + 1, endY);
            generatePendingTiles( ca,  p, RandomUtils.uniform(RANDOM, 2, 3));
        }
    }

    public static void drawHorizontalHallway(Position p) {
        int length = RandomUtils.uniform(RANDOM, 4, 9);

        int wallOpeningX = p.x;
        int wallOpeningY = p.y;

        switch (p.towards) {
            case "up":
                p.y += 1;
                break;
            case "down":
                p.y -= 1;
                break;
            case "left":
                p.x -= 1;
                break;
            case "right":
                p.x += 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + p.towards);
        }

        int startX = p.x;
        int startY = p.y;
        int endX, endY;
        switch (p.towards) {
            case "left":
                endX = startX;
                startX = endX - length + 1;
                endY = startY;
                break;
            case "right":
                endX = startX + length - 1;
                endY = startY;
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + p.towards);
        }


        if (isConflict(startX, startY - 1, endX, endY + 1)) {
            // 若有冲突，直接填充墙孔
            world[wallOpeningX][wallOpeningY] = Tileset.WALL;
        } else {
            // 若无冲突， 填充地板和两侧墙壁
            fillTiles(startX, startY, endX, endY, Tileset.FLOOR);

            fillTiles(startX, startY - 1, endX, endY - 1, Tileset.WALL);
            fillTiles(startX, startY + 1, endX, endY + 1, Tileset.WALL);
            fillTiles(startX, startY - 1, startX, startY + 1, Tileset.WALL);
            fillTiles(endX, startY - 1, endX, startY + 1, Tileset.WALL);
            world[p.x][p.y] = Tileset.FLOOR;

            //打洞
            CoverArea ca = new CoverArea(startX, startY - 1,endX, endY + 1);
            generatePendingTiles( ca,  p, RandomUtils.uniform(RANDOM, 2, 3));
        }
    }

//    public void generateLHallway(){

//        }

    //取出一个待处理墙洞
    public static void removePendingTiles(Position p){
        switch (p.region) {
            case "room":
                generateRoom(p);
                break;
            case "hallway":
                generateHallway(p);
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + p.towards);
        }
    }


    /**
     * 选择地方生成墙洞并放进pending List
     * @param ca
     * @param p
     * @param num
     */
    public static void generatePendingTiles(CoverArea ca, Position p,int num){
        //排除一个不可能的朝向
        ArrayList<String> legalTowards = new ArrayList<String>();
        String region;
        legalTowards.add("up");
        legalTowards.add("down");
        legalTowards.add("left");
        legalTowards.add("right");
        switch (p.towards) {
            case "up":
                legalTowards.remove("down");
                break;
            case "down":
                legalTowards.remove("up");
                break;
            case "left":
                legalTowards.remove("right");
                break;
            case "right":
                legalTowards.remove("left");
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + p.towards);
        }

        for(int i =1; i <= num; i += 1){
            String towards = legalTowards.get(RANDOM.nextInt(legalTowards.size()));

            switch (p.region) {
                case "room":
                    region = "hallway";
                    break;
                case "hallway":
                    region = chooseRegionForHallway(towards, p);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid towards: " + p.towards);
            }
        // 打墙洞
            randomGetHole(ca, towards, region);
            legalTowards.remove(towards);
        }
    }

    /**
     * 为走廊上的墙洞判断该生成房间还是走廊
     * @param towards
     * @param p
     * @return
     */
    public static String chooseRegionForHallway(String towards, Position p){
        String returnRegion;
        if(towards == p.towards) {
            returnRegion = "room";
        } else {
            returnRegion = "hallway";
        }
        return returnRegion;
    }

    /**
     * 随机在墙上打洞
     * @param ca
     * @param towards
     * @param region
     */
    public static void randomGetHole(CoverArea ca,String towards, String region){
        int x,y;
        switch (towards) {
            case "up":
                if(ca.startX + 1 == ca.endX - 1 ){ //random（1，1）这种情况会报错，这里处理方法比较粗糙
                    x = ca.startX + 1;
                }else{
                    x = RandomUtils.uniform(RANDOM, ca.startX + 1, ca.endX - 1);
                }
                y = ca.endY;
                break;
            case "down":
                if(ca.startX + 1 == ca.endX - 1 ){ //random（1，1）这种情况会报错，这里处理方法比较粗糙
                    x = ca.startX + 1;
                }else {
                    x = RandomUtils.uniform(RANDOM, ca.startX + 1, ca.endX - 1);
                }
                y = ca.startY;
                break;
            case "left":
                x = ca.startX;
                if(ca.startY + 1 == ca.endY - 1 ){ //random（1，1）这种情况会报错，这里处理方法比较粗糙
                    y = ca.startY + 1;
                }else {
                    y = RandomUtils.uniform(RANDOM, ca.startY + 1, ca.endY - 1);
                }
                break;
            case "right":
                x = ca.endX;
                if(ca.startY + 1 == ca.endY - 1 ){ //random（1，1）这种情况会报错，这里处理方法比较粗糙
                    y = ca.startY + 1;
                }else {
                    y = RandomUtils.uniform(RANDOM, ca.startY + 1, ca.endY - 1);
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + towards);
        }

        world[x][y] = Tileset.FLOOR;
        Position p = new Position(x,y,towards, region);
        pendingTiles.addLast(p);
    }


    /**
     * 判断是否冲突
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return
     */
    public static boolean isConflict(int startX, int startY, int endX, int endY){
        if(startX < 0 || startY < 0 || endX > WIDTH - 1 || endY > HEIGHT - 1 ){
            return true;
        }
        for (int x = startX; x <= endX; x += 1) {
            for (int y = startY; y <=  endY; y += 1) {
                if (world[x][y] != Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void fillTiles(int startX, int startY, int endX, int endY, TETile t ){
        for (int x = startX; x <= endX; x += 1) {
            for (int y = startY; y <=  endY; y += 1) {
                world[x][y] = t;
            }
        }
    }

    /*游戏开始前的主菜单*/
    static  void drawFrame() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering();

        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        StdDraw.clear();
        StdDraw.clear(Color.black);

        //Draw the GUI

        Font bigFont = new Font("Monaco", Font.BOLD, 60);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, HEIGHT - 10, "CS61B: THE GAME");


        //Draw the actual text
        Font samallFont = new Font("Monaco", Font.ROMAN_BASELINE, 30);
        StdDraw.setFont(samallFont);
        StdDraw.text(midWidth, midHeight - 5, "New Game (N)");
        StdDraw.text(midWidth, midHeight - 8, "Load Game (L)");
        StdDraw.text(midWidth, midHeight - 11, "Quit (Q)");
        StdDraw.show();
    }

    /*在屏幕中间显示文字（白色）*/
    static void drawText(String s) {
        StdDraw.clear(Color.black);
        Font smallFont = new Font("Monaco", Font.BOLD, 35);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }

    /**
     * 在屏幕中间显示文字（自选颜色）
     */
    static void drawText(String s, Color color) {
        StdDraw.clear(Color.black);
        Font smallFont = new Font("Monaco", Font.BOLD, 35);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(color);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }


    /*用键盘作为输入初次游戏时对玩家按键的响应*/
    private static void move() {
        //setDoorLocation();
        setPlayerLocation();
        while (true) {
            //todo:tileDescription();
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            switch (key) {
                case 'w':
                    if (!(world[x][y + 1] == Tileset.WALL)) {
                        if (world[x][y + 1] == Tileset.LOCKED_DOOR) {
                            drawText("YOU WIN !!!",Color.yellow);
                            return;
                        }
                        world[x][y] = Tileset.FLOOR;
                        y += 1;
                        world[x][y] = Tileset.PLAYER;
                        ter.renderFrame(world);
                    }
                    break;
                case 's':
                    if (!(world[x][y - 1] == Tileset.WALL)) {
                        if (world[x][y - 1] == Tileset.LOCKED_DOOR) {
                            drawText("YOU WIN !!!",Color.yellow);
                            return;
                        }
                        world[x][y] = Tileset.FLOOR;
                        y -= 1;
                        world[x][y] = Tileset.PLAYER;
                        ter.renderFrame(world);
                    }
                    break;
                case 'a':
                    if (!(world[x - 1][y] == Tileset.WALL)) {
                        if (world[x - 1][y] == Tileset.LOCKED_DOOR) {
                            drawText("YOU WIN !!!",Color.yellow);
                            return;
                        }
                        world[x][y] = Tileset.FLOOR;
                        x -= 1;
                        world[x][y] = Tileset.PLAYER;
                        ter.renderFrame(world);
                    }
                    break;
                case 'd':
                    if (!(world[x + 1][y] == Tileset.WALL)) {
                        if (world[x + 1][y] == Tileset.LOCKED_DOOR) {
                            drawText("YOU WIN !!!",Color.yellow);
                            return;
                        }
                        world[x][y] = Tileset.FLOOR;
                        x += 1;
                        world[x][y] = Tileset.PLAYER;
                        ter.renderFrame(world);
                    }
                    break;
/*                case 'q': saveWorld(new Info(Game.SUBSEED, x, y));
                    System.exit(0);
                    break;*/
                default:
            }
        }
    }

    public static void setPlayerLocation(){
        while (true) {
            x = RandomUtils.uniform(RANDOM, 0, WIDTH - 1);
            y = RandomUtils.uniform(RANDOM, 0, HEIGHT - 1);
            if(world[x][y] == Tileset.FLOOR){
                world[x][y] = Tileset.PLAYER;
                break;
            }
        }
        ter.renderFrame(world);
    }

    public static void run(){
        prepare();
        generateWorld();

        ter.renderFrame(world);

        move();
    }

    /**
     * 用键盘输入时初次运行游戏对世界的初始化，seed来自键盘输入
     */
    private static void prepare() {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        ter.initialize(WIDTH, HEIGHT);
        initializeTheWorld();
        RANDOM = new Random(SEED);
    }



}


