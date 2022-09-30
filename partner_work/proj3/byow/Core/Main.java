package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.*;
import edu.princeton.cs.introcs.StdDraw;

import java.io.IOException;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static final int HUD_HEIGHT = 2;
    public static void main(String[] args) throws InterruptedException {
        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        }
//        EngineCopy engine = new EngineCopy();
//        TERendererCopy ter = new TERendererCopy();
        Engine engine = new Engine();
        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT+HUD_HEIGHT);
        if (args.length == 2 && args[0].equals("-s")) {
            TETile[][] ret1 = engine.interactWithInputString("n2saaaawsd");
            ter.renderFrame(ret1, engine.coinsCollected, "", engine.toggleSight, engine.aX, engine.aY);
//            StdDraw.pause(5000);
            engine.interactWithInputString("n2saaaa:q");
            TETile[][] ret2 = engine.interactWithInputString("lwsd");
            ter.renderFrame(ret2, engine.coinsCollected, "", engine.toggleSight, engine.aX, engine.aY);
            boolean fail = false;
            for (int i = 0; i < ret1.length; i++) {
                for (int j = 0; j < ret1[0].length; j++) {
                    if (!ret1[i][j].equals(ret2[i][j])) {
                        fail = true;
                    }
                }
            }
            System.out.println(fail);
//            ter.renderFrame(ret, engine.coinsCollected, "", engine.toggleSight, engine.aX, engine.aY);
        } else if (args.length == 2 && args[0].equals("-p")) { System.out.println("Coming soon."); }
        // DO NOT CHANGE THESE LINES YET ;)
        else {
//            engine.seed = Long.parseLong(args[0]);
            engine.interactWithKeyboard();
        }
    }
}
