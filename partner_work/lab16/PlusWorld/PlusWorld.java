package PlusWorld;
import org.junit.Test;
import static org.junit.Assert.*;

import byowTools.TileEngine.TERenderer;
import byowTools.TileEngine.TETile;
import byowTools.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of plus shaped regions.
 */
public class PlusWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        int size = 3;
        world = plus(size, 0, 0, world);

        // draws the world to the screen
        ter.renderFrame(world);
    }

    private static TETile[][] plus(int size, int x, int y, TETile[][] world) {
        for (int i = 0; i < size * 3; i++) {
            for (int j = 0; j < size * 3; j++) {
                if ((size<=i && i<size*2) || (size <= j && j < size*2)) {
                    world[i][j] = Tileset.FLOWER;
                } else {
                    world[i][j] = Tileset.NOTHING;
                }
            }
        }
        return world;
    }


}
