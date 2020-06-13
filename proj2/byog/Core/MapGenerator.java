package byog.Core;

import byog.TileEngine.*;
import byog.lab6.MemoryGame;
import edu.princeton.cs.introcs.StdDraw;
import java.util.Random;
import byog.Core.LinkedListDeque;
import java.util.ArrayList;

public class MapGenerator {

    private static int WIDTH;
    private static int HEIGHT;

    private static int pointX;
    private static int pointY;

    private static long SEED;
    private static Random RANDOM = new Random(SEED);

    public static TERenderer ter = new TERenderer();
    public static TETile[][] world;

    /**
     * 其实没必要用LinkedListDeque,用java.util.ArrayList就可以但我想试一下
     */
    LinkedListDeque<Position> pendingTiles  = new LinkedListDeque<>();

    //private static final String[] TOWARDS = {"up", "down", "left", "right"};

    public static void getSEED(String[] args) {
        // TODO
        if (args.length < 3) {
            throw new RuntimeException("Please enter a correct seed");
        }

        int seed = Integer.parseInt(args[0]);
        SEED = seed;
    }

    /**
     * 初始化世界
     */
    public void initializeTheWorld(int width, int height){
        //ter.initialize(WIDTH, HEIGHT);
        WIDTH = width;
        HEIGHT = height;
        world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

    }

    public void demo() {
        Position p = new Position(30,30,"down", "hallway");
        generateRoom(p);

        while(!pendingTiles.isEmpty()){
            removePendingTiles(pendingTiles.removeFirst());
        }
    }

    public class Position {
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
    private class CoverArea {
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


    public void generateRoom(Position p){
        int roomWidth = RandomUtils.uniform(RANDOM, 4, 9);
        int roomHeight = RandomUtils.uniform(RANDOM, 4, 9);
        int startX, startY;
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
            world[p.x][p.y] = Tileset.WALL;
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

    public void generateHallway(Position p){
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

    public void drawVerticalHallway(Position p) {
        int length = RandomUtils.uniform(RANDOM, 4, 9);
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
            world[p.x][p.y] = Tileset.WALL;
        } else {
            // 若无冲突， 填充地板和两侧墙壁
            fillTiles(startX, startY, endX, endY, Tileset.FLOOR);

            fillTiles(startX - 1, startY, endX - 1, endY, Tileset.WALL);
            fillTiles(startX + 1, startY, endX + 1, endY, Tileset.WALL);
            //打洞
            CoverArea ca = new CoverArea(startX - 1, startY,endX + 1, endY);
            generatePendingTiles( ca,  p, RandomUtils.uniform(RANDOM, 2, 3));
        }
    }

    public void drawHorizontalHallway(Position p) {
        int length = RandomUtils.uniform(RANDOM, 4, 9);
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
            world[p.x][p.y] = Tileset.WALL;
        } else {
            // 若无冲突， 填充地板和两侧墙壁
            fillTiles(startX, startY, endX, endY, Tileset.FLOOR);

            fillTiles(startX, startY - 1, endX, endY - 1, Tileset.WALL);
            fillTiles(startX, startY + 1, endX, endY + 1, Tileset.WALL);

            //打洞
            CoverArea ca = new CoverArea(startX, startY - 1,endX, endY + 1);
            generatePendingTiles( ca,  p, RandomUtils.uniform(RANDOM, 2, 3));
        }
    }

//    public void generateLHallway(){
//        // todo
//    }
//
//
//        switch (p.region) {
//            case "room":
//                generatePendingTilesForRoom();
//                break;
//            case "hallway":
//                generatePendingTilesForHallway();
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid region: " + p.region);
//        }



    //取出一个待处理墙洞
    public void removePendingTiles(Position p){
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


    public void generatePendingTiles(CoverArea ca, Position p,int num){
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
     * 用拼图的逻辑判断该生成房间还是走廊
     * @param towards
     * @param p
     * @return
     */
    public String chooseRegionForHallway(String towards, Position p){
        String returnRegion;
        int pairA,pairB;
        switch(towards){
            case "up":
                pairA = 1;
                break;
            case "down":
                pairA = 4;
                break;
            case "left":
                pairA = 2;
                break;
            case "right":
                pairA = 3;
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + towards);
        }

        switch(p.towards){
            case "up":
                pairB = 1;
                break;
            case "down":
                pairB = 4;
                break;
            case "left":
                pairB = 2;
                break;
            case "right":
                pairB = 3;
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + p.towards);
        }
        if(pairA + pairB == 5) {
            returnRegion = "room";
        } else {
            returnRegion = "hallway";
        }
        return returnRegion;
    }

    public void randomGetHole(CoverArea ca,String towards, String region){
        int x,y;
        switch (towards) {
            case "up":
                if(ca.startX + 1 == ca.endX - 1 ){ //random（1，1）这种情况会报错，这里处理方法比较粗糙
                    x = ca.startX + 1;
                }else{
                    x = RandomUtils.uniform(RANDOM, ca.startX + 1, ca.endX - 1);
                }
                y = ca.endY;
                world[x][y] = Tileset.FLOOR;
                y += 1;
                break;
            case "down":
                if(ca.startX + 1 == ca.endX - 1 ){ //random（1，1）这种情况会报错，这里处理方法比较粗糙
                    x = ca.startX + 1;
                }else {
                    x = RandomUtils.uniform(RANDOM, ca.startX + 1, ca.endX - 1);
                }
                y = ca.startY;
                world[x][y] = Tileset.FLOOR;
                y -= 1;
                break;
            case "left":
                x = ca.startX;
                if(ca.startY + 1 == ca.endY - 1 ){ //random（1，1）这种情况会报错，这里处理方法比较粗糙
                    y = ca.startY + 1;
                }else {
                    y = RandomUtils.uniform(RANDOM, ca.startY + 1, ca.endY - 1);
                }
                world[x][y] = Tileset.FLOOR;
                x -= 1;
                break;
            case "right":
                x = ca.endX;
                if(ca.startY + 1 == ca.endY - 1 ){ //random（1，1）这种情况会报错，这里处理方法比较粗糙
                    y = ca.startY + 1;
                }else {
                    y = RandomUtils.uniform(RANDOM, ca.startY + 1, ca.endY - 1);
                }
                world[x][y] = Tileset.FLOOR;
                x +=1;
                break;
            default:
                throw new IllegalArgumentException("Invalid towards: " + towards);
        }

        Position p = new Position(x,y,towards, region);
        pendingTiles.addLast(p);
    }


    public boolean isConflict(int startX, int startY, int endX, int endY){
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

    public void fillTiles(int startX, int startY, int endX, int endY, TETile t ){
        for (int x = startX; x <= endX; x += 1) {
            for (int y = startY; y <=  endY; y += 1) {
                world[x][y] = t;
            }
        }
    }





}
