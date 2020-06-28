package byog.Core;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

public class Game {
    //TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        try {
            MapGenerator.getSizeOfWorld(WIDTH, HEIGHT);
            MapGenerator.drawFrame(); //渲染主菜单
            while (true) {
                /*是否有键盘输入*/
                if (!StdDraw.hasNextKeyTyped()) {
                    continue;
                }
                char temp = StdDraw.nextKeyTyped();

                switch (temp) {
                    case 'n': MapGenerator.drawText("Please enter a seed and press 'S'");
                        StringBuilder input = new StringBuilder(); //存放'N'之后输入的字符
                        /*获取n-s之间的数字作为种子*/
                        while (temp != 's') {
                            if (!StdDraw.hasNextKeyTyped()) {
                                continue;
                            }
                            temp = StdDraw.nextKeyTyped();
                            input.append(temp);
                        }
                        String s = input.substring(0, input.length() - 1);

                        //ter.initialize(WIDTH, HEIGHT);
                        MapGenerator.getSizeOfWorld(WIDTH, HEIGHT);
                        MapGenerator.getSEED(Long.parseLong(s));

                        MapGenerator.run();
                        break;
                    case 'q': System.exit(0);
                        break;
//                    case 'l': File f = new File("./game.txt");
//                        /*如果未经保存就读取游戏存档，则提示错误信息并终止游戏*/
//                        if ((!f.exists())) {
//                            MapGenerator.drawRedText("No previous game has been saved.");
//                            return;
//                        }
//                        StdDraw.enableDoubleBuffering();
//                        MapGenerator.run(MapGenerator.loadWorld()); //以读取的存档内容作为参数运行游戏
//                        break;
                    default:
                }
            }
        } catch (NumberFormatException e) {
            /*如果输入的种子含有字母或其他非法字符则提示错误信息并终止游戏*/
            MapGenerator.drawText("Sorry, you didn't enter the proper number", Color.red);
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        long seed;

        //MapGenerator map = new MapGenerator();
        String pattern3 = "([Nn])(\\d+)([Ss])";
        Pattern r3 = Pattern.compile(pattern3);
        Matcher m3 = r3.matcher(input);
        if (m3.find()) {
            seed = Long.parseLong(m3.group(2));
            MapGenerator.getSizeOfWorld(WIDTH, HEIGHT);
            MapGenerator.initializeTheWorld();
            MapGenerator.getSEED(seed);
            MapGenerator.generateWorld();

            return MapGenerator.world;

        }

        return null;


    }
}
