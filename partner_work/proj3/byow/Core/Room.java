package byow.Core;

import byow.TileEngine.*;
import java.util.*;

public class Room {
    int leftX, rightX;
    int leftY, rightY;
    public Room(int lx, int ly, int rx, int ry) {
        leftX = lx; rightX = rx;
        leftY = ly; rightY = ry;
    }
    public boolean overlap(Room r) {
        return leftX <= r.rightX && rightX >= r.leftX && leftY <= r.rightY && rightY >= r.leftY;
    }
    public void connect(Random r, TETile[][] grid, Room room) {
        int myX = RandomUtils.uniform(r, leftX, rightX+1), myY = RandomUtils.uniform(r, leftY, rightY);
        int rX = RandomUtils.uniform(r, room.leftX, room.rightX+1), rY = RandomUtils.uniform(r, room.leftY, room.rightY);
        int yMotionFirst = RandomUtils.uniform(r, 2);
        if (yMotionFirst == 0) { // move X first
            int minX = Math.min(myX, rX), maxX = Math.max(myX, rX);
            int minY = Math.min(myY, rY), maxY = Math.max(myY, rY);
            for (int i = minX; i <= maxX; i++) {
                grid[i][myY] = Tileset.FLOOR;
            }
            for (int i = minY; i <= maxY; i++) {
                grid[rX][i] = Tileset.FLOOR;
            }
        }
        else { // move Y first
            int minX = Math.min(myX, rX), maxX = Math.max(myX, rX);
            int minY = Math.min(myY, rY), maxY = Math.max(myY, rY);
            for (int i = minY; i <= maxY; i++) {
                grid[myX][i] = Tileset.FLOOR;
            }
            for (int i = minX; i <= maxX; i++) {
                grid[i][rY] = Tileset.FLOOR;
            }
        }
    }
    public void draw(TETile[][] grid) {
        for (int i = leftX; i <= rightX; i++) {
            for (int j = leftY; j <= rightY; j++) {
                grid[i][j] = Tileset.FLOOR;
            }
        }
    }
}
