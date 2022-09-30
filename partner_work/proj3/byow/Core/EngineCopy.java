package byow.Core;

import byow.InputDemo.*;
import byow.TileEngine.*;
import edu.princeton.cs.introcs.StdDraw;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class EngineCopy {
    /* Feel free to change the width and height. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File ENGINE_DIR = Utils.join(CWD,".byow");
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    Random r;
    public static final File RANDOM = Utils.join(ENGINE_DIR, "random");
    boolean colon = false;
    StringBuilder curSeed;
    long seed;
    boolean toggleSight = true;
    public static final File tiles = Utils.join(ENGINE_DIR, "tiles"); // folder
    TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
    int aX, aY; // Avatar X and Y
    public static final File avatarCoords = Utils.join(ENGINE_DIR, "coords");
    TETile avatarTile; // What was on the tile before avatar stood on it (so we can replace tile after avatar leaves tile)
    public static final File aTile = Utils.join(ENGINE_DIR, "tiles", "avatar_tile");
    InputSource inputSource = new KeyboardInputSource();
    TERendererCopy ter = new TERendererCopy();
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    boolean invalidSeedInput = false;
    public int state = 0; // 0 = menu, 1 = input seed, 2 = game
    public boolean keyboard = false;
    public void interactWithKeyboard() throws InterruptedException {
        // display title screen
        // handle first key press (N, L, Q)
        // Render world, take user keypress in 1 by 1, changing the world for each keypress
        // Show main menu, then create the new/load world accordingly
//        displayMainMenu();
//        NLWorld(inputSource);
        keyboard = true;
        char lastChar = ' ';
        boolean invalidKeypress = false;
        String errorMessage = "";
        while (true) {
            if (state == 0) {
                displayMainMenu();
            }
            else if (state == 1) {
                if (invalidSeedInput) {
                    notif(WIDTH/2, (HEIGHT)/2,
                            "Not a digit or S: \'" + lastChar
                                    + "\'.\n Press any key to continue.");
                    inputSource.getNextKey(); //wait for next key
                    invalidSeedInput = false;
                }
                else {
                    notif(WIDTH/2, (HEIGHT+Main.HUD_HEIGHT)/2,
                            "Please Enter A World Seed (numbers) Followed by an \'S\' When Done." +
                                    "\nYour Current Seed is:\n" + curSeed.toString() + "");
                }
            }
            else {
                if (invalidKeypress) {
                    ter.renderFrame(finalWorldFrame, errorMessage, toggleSight, aX, aY);
                    TimeUnit.SECONDS.sleep(1);
                }
                else {
                    ter.renderFrame(finalWorldFrame, "", toggleSight, aX, aY);
                }
            }
            if (StdDraw.hasNextKeyTyped()) {
                char c = inputSource.getNextKey();
                lastChar = c;
                try {
                    act(c);
                } catch (Exception InputMismatchException) {
                    invalidKeypress = true;
                    errorMessage = InputMismatchException.getMessage();
                }
            }

        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        clearWorld();
        for (int i = 0; i < input.length(); i++) {
            act(input.charAt(i));
        }
        return finalWorldFrame;
    }
//    private void NLWorld(InputSource inputSource) {
//        char c = inputSource.getNextKey();
//
//    }
    private void notif(int x, int y, String s) {
        StdDraw.clear(StdDraw.BLACK);
        String[] parsed = s.split("\n");
        for (int i = 0; i < parsed.length; i++) {
            StdDraw.text(x, y - 3*i, parsed[i]);
        }
        StdDraw.show();
    }
    private void displayMainMenu() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH/2, (HEIGHT+Main.HUD_HEIGHT)/2 + 10, "CS61BL: The Game");
        StdDraw.text(WIDTH/2, (HEIGHT+Main.HUD_HEIGHT)/2 + 3, "New Game (N)");
        StdDraw.text(WIDTH/2, (HEIGHT+Main.HUD_HEIGHT)/2, "Load Game (L)");
        StdDraw.text(WIDTH/2, (HEIGHT+Main.HUD_HEIGHT)/2 - 3, "Quit (Q)");
        StdDraw.show();
    }
    private void act(char c) throws RuntimeException {
        if (state == 0) {
            if (equal(c, 'N')) {
                state++;
                curSeed = new StringBuilder();
            }
            else if (equal(c, 'L')) {
                loadWorld();
                state = 2;
            }
            else if (equal(c, 'Q')) {
                System.exit(0);
            }
        }
        else if (state == 1) {
//            c = inputSource.getNextKey();
            if (equal(c, 'S')) {
                state++;
                seed = Long.parseLong(curSeed.toString());
                createWorld();
                return;
            } else if (c - '0' >= 0 && c - '0' <= 9) {
                curSeed.append(c);
            } else {
                invalidSeedInput = true;
            }
        }
        else if (state == 2) {
            if (colon) {
                if (equal(c, 'q')) {
                    saveWorld();
                    clearWorld();
                    state = 0;
                    return;
                } else {
                    colon = false;
                }
            }
            else {
                if (equal(c, 'q')) {
                    throw new InputMismatchException("Need to press : before Q to quit");
                }
            }
            if (equal(c, ':')) {
                colon = true;
                return;
            } else if (equal(c, 'e')) {
                toggleSight = !toggleSight;
                return;
            } else {
                if (isMove(c)) {
                    move(c);
                } else {
                    throw new InputMismatchException("Not WASD or colon or E");
                }
            }
        }
        else {
            throw new RuntimeException("Should not get here");
        }
    }
    private static final char up = 'W', left = 'A', right = 'D', down = 'S';
    public boolean isMove(char c) {
        return equal(c, up) || equal(c, left) || equal(c, right) || equal(c, down);
    }
    public void move(char dir) {
        int dx, dy;
        if (equal(dir, up)) {
            dx = 0; dy = 1;
        }
        else if (equal(dir, down)) {
            dx = 0; dy = -1;
        }
        else if (equal(dir, left)) {
            dx = -1; dy = 0;
        }
        else if (equal(dir, right)) {
            dx = 1; dy = 0;
        }
        else {
            throw new InputMismatchException("Keypress is not WASD");
        }
        int nX = aX+dx, nY = aY+dy;
        boolean collide = collide(nX, nY);
        if (collide) return;
        finalWorldFrame[aX][aY] = avatarTile;
        aX = nX; aY = nY;
        avatarTile = finalWorldFrame[aX][aY];
        finalWorldFrame[aX][aY] = Tileset.AVATAR;
    }
    public boolean collide(int x, int y) {
        return finalWorldFrame[x][y].equals(Tileset.WALL);
    }
    private void clearWorld() {
        for (int i = 0; i < WIDTH; i++) {
            Arrays.fill(finalWorldFrame[i], Tileset.NOTHING);
        }
        if (keyboard) StdDraw.clear(StdDraw.BLACK);
    }
    public void createWorld() {
        clearWorld();
        r = new Random(seed);
        // number of tries we will do to generate a valid room
        int numBigRoomTries = RandomUtils.uniform(r, 4);
        int numSmallRoomTries = RandomUtils.uniform(r, 5, 20);
        ArrayList<Room> rooms = generateRooms(r, numBigRoomTries, 5, 12, 4, 7);
        rooms.addAll(generateRooms(r, numSmallRoomTries, 2, 5, 2, 4));
        // Connect rooms
        /**
         0 1 2 3 4 5 6 7 8 9
         0
         0-1
         (0-1)-2
         **/
        for (int i = 1; i < rooms.size(); i++) {
            Room cur = rooms.get(i);
            Room roomToConnectTo = rooms.get(RandomUtils.uniform(r, 0, i));
            cur.connect(r, finalWorldFrame, roomToConnectTo);
//            ter.renderFrame(finalWorldFrame);
//            StdDraw.pause(250);
        }
        for (Room room : rooms) {
            room.draw(finalWorldFrame);
//            ter.renderFrame(finalWorldFrame);
//            StdDraw.pause(250);
        }
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (finalWorldFrame[i][j] == Tileset.FLOOR) continue;
                boolean wall = false;
                for (int curX = Math.max(0, i-1); curX <= Math.min(WIDTH-1, i+1); curX++) {
                    for (int curY = Math.max(0, j-1); curY <= Math.min(HEIGHT-1, j+1); curY++) {
                        if (finalWorldFrame[curX][curY] == Tileset.FLOOR) {
                            wall = true;
                            break;
                        }
                    }
                    if (wall) {
                        break;
                    }
                }
                finalWorldFrame[i][j] = wall ? Tileset.WALL : Tileset.NOTHING;
            }
        }
        int floorCount = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                floorCount += finalWorldFrame[i][j] == Tileset.FLOOR ? 1 : 0;
            }
        }
        int avatarFloorTileIndex = RandomUtils.uniform(r, floorCount);
        floorCount = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (floorCount == avatarFloorTileIndex && finalWorldFrame[i][j] == Tileset.FLOOR) {
                    avatarTile = finalWorldFrame[i][j];
                    finalWorldFrame[i][j] = Tileset.AVATAR;
                    aX = i; aY = j;
                    return;
                }
                floorCount += finalWorldFrame[i][j] == Tileset.FLOOR ? 1 : 0;
            }
        }
    }
    // min size inclusive, max size exclusive.
    public ArrayList<Room> generateRooms(Random r, int numTries, int xMinSize, int xMaxSize, int yMinSize, int yMaxSize) {
        ArrayList<Room> generatedRooms = new ArrayList<>();
        for (int i = 0; i < numTries; i++) {
            int xdiff = RandomUtils.uniform(r, xMinSize, xMaxSize), ydiff = RandomUtils.uniform(r, yMinSize, yMaxSize);
            int leftx = RandomUtils.uniform(r, 1, WIDTH-1-xdiff), lefty = RandomUtils.uniform(r, 1, HEIGHT-1-ydiff);
            int rightx = leftx+xdiff, righty = lefty+ydiff;
            Room potentialNewRoom = new Room(leftx, lefty, rightx, righty);
            boolean fail = false;
            for (Room room : generatedRooms) {
                if (room.overlap(potentialNewRoom)) {
                    fail = true;
                    break;
                }
            }
            if (!fail) {
                generatedRooms.add(potentialNewRoom);
            }
        }
        return generatedRooms;
    }

    public void update() {
        int mX = (int) StdDraw.mouseX();
        int mY = (int) StdDraw.mouseY();
        String tileType = finalWorldFrame[mX][mY].description();
        return;
    }
    public static boolean equal(char a, char b) {
        return Character.toUpperCase(a) == Character.toUpperCase(b);
    }
    public void saveWorld() {
        try {
            if (!ENGINE_DIR.exists()) {
                ENGINE_DIR.mkdir();
            }
            if (!tiles.exists()) {
                tiles.mkdir();
            }
            Utils.writeObject(RANDOM, r);
            PrintWriter pw = new PrintWriter(new FileWriter(avatarCoords));
            pw.println(aX);
            pw.println(aY);
            pw.close();
            Utils.writeObject(aTile, avatarTile);
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    File curTile = Utils.join(tiles, String.format("Tile %d %d", i, j));
                    Utils.writeObject(curTile, finalWorldFrame[i][j]);
                }
            }
        }
        catch(IOException ioe) {
            System.out.println("IOException occurred.");
            System.exit(0);
        }
    }
    public void loadWorld() {
        try {
            r = Utils.readObject(RANDOM, Random.class);
            BufferedReader br = new BufferedReader(new FileReader(avatarCoords));
            aX = Integer.parseInt(br.readLine());
            aY = Integer.parseInt(br.readLine());
            br.close();
            avatarTile = Utils.readObject(aTile, TETile.class);
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    File curTile = Utils.join(tiles, String.format("Tile %d %d", i, j));
                    finalWorldFrame[i][j] = Utils.readObject(curTile, TETile.class);
                }
            }
        }
        catch (IOException ioe) {
            System.out.println("IOException occurred.");
            System.exit(0);
        }
    }
}
