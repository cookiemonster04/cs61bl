package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import edu.princeton.cs.introcs.StdDraw;

public class Engine {
    /* Feel free to change the width and height. */
    public static final File CWD = new File(System.getProperty("user.dir"));
//    public static final File ENGINE_DIR = Utils.join(CWD,".byow");
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    Random r;
//    public static final File RANDOM = Utils.join(ENGINE_DIR, "random");
    public static final File RANDOM = Utils.join(CWD, "random.txt");
    boolean colon = false;
    StringBuilder curSeed;
    long seed;
    boolean toggleSight = true;
//    public static final File tiles = Utils.join(ENGINE_DIR, "tiles"); // folder
    public static final File tiles = Utils.join(CWD, "tiles.txt"); // folder
    TETile[][] grid = new TETile[WIDTH][HEIGHT];
    TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
    int aX, aY; // Avatar X and Y
    int coinsCollected = 0;
//    public static final File avatarCoords = Utils.join(ENGINE_DIR, "coords");
    public static final File avatarStuff = Utils.join(CWD, "coords.txt");
//    public static final File aTile = Utils.join(ENGINE_DIR, "tiles", "avatar_tile");
    InputSource inputSource = new KeyboardInputSource();
    TERenderer ter = new TERenderer();
    HashSet<Square> coins = new HashSet<>();
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    boolean invalidSeedInput = false;
    public int state = 0; // 0 = menu, 1 = input seed, 2 = game
    public boolean keyboard = false;
    public static final int MOVES_PER_COIN = 8;
    int moves = 0;
    public static final int INITIAL_COINS = 5;
    public int[][] dist = new int[WIDTH][HEIGHT];
    boolean showPaths = false;
    boolean collide = false;
    boolean exit = false;
    public void interactWithKeyboard() throws InterruptedException {
        // display title screen
        // handle first key press (N, L, Q)
        // Render world, take user keypress in 1 by 1, changing the world for each keypress
        // Show main menu, then create the new/load world accordingly
//        displayMainMenu();
//        NLWorld(inputSource);
        clearWorld();
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
                    ter.renderFrame(finalWorldFrame, coinsCollected, errorMessage, toggleSight, aX, aY);
                    TimeUnit.SECONDS.sleep(1);
                }
                else {
                    ter.renderFrame(finalWorldFrame, coinsCollected, "", toggleSight, aX, aY);
                }
            }
            if (StdDraw.hasNextKeyTyped()) {
                char c = inputSource.getNextKey();
                lastChar = c;
                try {
                    act(c);
                    invalidKeypress = false;
                } catch (Exception InputMismatchException) {
                    invalidKeypress = true;
                    errorMessage = InputMismatchException.getMessage();
                }
            }
            if (exit) {
                exit = false;
                return;
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
        update();
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
                exit = true;
            }
        }
        else if (state == 1) {
//            c = inputSource.getNextKey();
            if (equal(c, 'S')) {
                state++;
                seed = Long.parseLong(curSeed.toString());
                createWorld();
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
            }else {
                if (isMove(c)) {
                    move(c);
                    if (!collide) {
                        if (moves % MOVES_PER_COIN == 0) {
                            setRandomFloorTile(Tileset.COIN);
                        }
                        collectCoins();
                        calcDist();
                        moveCoins();
                        moves++;
                    }
                } else if (equal(c, 'P')) {
                    showPaths = !showPaths;
                } else if (equal(c, 'e')) {
                    toggleSight = !toggleSight;
                } else {
                    throw new InputMismatchException("Not WASD or colon or P or E");
                }
            }
        }
        else {
            throw new RuntimeException("Should not get here");
        }
        if (state == 2) update();
    }
    private void calcDist() {
        for (int i = 0; i < dist.length; i++) {
            Arrays.fill(dist[i], -1);
        }
        LinkedList<Square> q = new LinkedList<>();
        q.add(new Square(aX, aY));
        dist[aX][aY] = 0;
        while(q.size() > 0) {
            Square s = q.pop();
            if (s.x != 0 && !grid[s.x-1][s.y].equals(Tileset.WALL) && dist[s.x-1][s.y] == -1) {
                dist[s.x-1][s.y] = dist[s.x][s.y]+1;
                q.add(new Square(s.x-1, s.y));
            }
            if (s.x != WIDTH-1 && !grid[s.x+1][s.y].equals(Tileset.WALL) && dist[s.x+1][s.y] == -1) {
                dist[s.x+1][s.y] = dist[s.x][s.y]+1;
                q.add(new Square(s.x+1, s.y));
            }
            if (s.y != 0 && !grid[s.x][s.y-1].equals(Tileset.WALL) && dist[s.x][s.y-1] == -1) {
                dist[s.x][s.y-1] = dist[s.x][s.y]+1;
                q.add(new Square(s.x, s.y-1));
            }
            if (s.y != HEIGHT-1 && !grid[s.x][s.y+1].equals(Tileset.WALL) && dist[s.x][s.y+1] == -1) {
                dist[s.x][s.y+1] = dist[s.x][s.y]+1;
                q.add(new Square(s.x, s.y+1));
            }
        }
    }
    private void moveCoins() {
        HashSet<Square> newCoins = new HashSet<>();
        for (Square s : coins) {
            if (s.x != 0 && !grid[s.x-1][s.y].equals(Tileset.WALL) && dist[s.x-1][s.y] > dist[s.x][s.y]) {
                grid[s.x-1][s.y] = Tileset.COIN;
                grid[s.x][s.y] = Tileset.FLOOR;
                newCoins.add(new Square(s.x-1, s.y));
            }
            else if (s.x != WIDTH-1 && !grid[s.x+1][s.y].equals(Tileset.WALL) && dist[s.x+1][s.y] > dist[s.x][s.y]) {
                grid[s.x+1][s.y] = Tileset.COIN;
                grid[s.x][s.y] = Tileset.FLOOR;
                newCoins.add(new Square(s.x+1, s.y));
            }
            else if (s.y != 0 && !grid[s.x][s.y-1].equals(Tileset.WALL) && dist[s.x][s.y-1] > dist[s.x][s.y]) {
                grid[s.x][s.y-1] = Tileset.COIN;
                grid[s.x][s.y] = Tileset.FLOOR;
                newCoins.add(new Square(s.x, s.y-1));
            }
            else if (s.y != HEIGHT-1 && !grid[s.x][s.y+1].equals(Tileset.WALL) && dist[s.x][s.y+1] > dist[s.x][s.y]) {
                grid[s.x][s.y+1] = Tileset.COIN;
                grid[s.x][s.y] = Tileset.FLOOR;
                newCoins.add(new Square(s.x, s.y+1));
            }
            else {
                newCoins.add(new Square(s.x, s.y));
            }
        }
        coins = newCoins;
    }
    private final char up = 'W', left = 'A', right = 'D', down = 'S';
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
        collide = collide(nX, nY);
        if (collide) return;
        aX = nX; aY = nY;
    }
    public boolean collide(int x, int y) {
        return grid[x][y].equals(Tileset.WALL);
    }
    private void clearWorld() {
        colon = false;
        toggleSight = true;
        grid = new TETile[WIDTH][HEIGHT];
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        coinsCollected = 0;
        inputSource = new KeyboardInputSource();
        ter = new TERenderer();
        coins = new HashSet<>();
        invalidSeedInput = false;
        state = 0; // 0 = menu, 1 = input seed, 2 = game
        moves = 0;
        dist = new int[WIDTH][HEIGHT];
        showPaths = false;
        collide = false;
        exit = false;
        for (int i = 0; i < WIDTH; i++) {
            Arrays.fill(grid[i], Tileset.NOTHING);
        }
        if (keyboard) StdDraw.clear(StdDraw.BLACK);
    }
    private void collectCoins() {
        for (int i = Math.max(aX-1, 0); i <= Math.min(aX+1, WIDTH-1); i++) {
            for (int j = Math.max(aY-1, 0); j <= Math.min(aY+1, HEIGHT-1); j++) {
                if (grid[i][j].equals(Tileset.COIN)) {
                    coins.remove(new Square(i, j));
                    coinsCollected++;
                    grid[i][j] = Tileset.FLOOR;
                }
            }
        }
    }
    public void createWorld() {
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
            cur.connect(r, grid, roomToConnectTo);
//            ter.renderFrame(finalWorldFrame);
//            StdDraw.pause(250);
        }
        for (Room room : rooms) {
            room.draw(grid);
//            ter.renderFrame(finalWorldFrame);
//            StdDraw.pause(250);
        }
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (grid[i][j].equals(Tileset.FLOOR)) continue;
                boolean wall = false;
                for (int curX = Math.max(0, i-1); curX <= Math.min(WIDTH-1, i+1); curX++) {
                    for (int curY = Math.max(0, j-1); curY <= Math.min(HEIGHT-1, j+1); curY++) {
                        if (grid[curX][curY].equals(Tileset.FLOOR)) {
                            wall = true;
                            break;
                        }
                    }
                    if (wall) {
                        break;
                    }
                }
                grid[i][j] = wall ? Tileset.WALL : Tileset.NOTHING;
            }
        }
        setRandomFloorTile(Tileset.AVATAR);
        for (int i = 0; i < INITIAL_COINS; i++) {
            setRandomFloorTile(Tileset.COIN);
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
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                finalWorldFrame[i][j] = grid[i][j];
            }
        }
        if (showPaths) {
            calcDist();
            for (Square s : coins) {
                int cx = s.x, cy = s.y;
                while(true) {
                    if (cx != 0 && !grid[cx-1][cy].equals(Tileset.WALL) && dist[cx-1][cy] > dist[cx][cy]
                            && !finalWorldFrame[cx-1][cy].equals(Tileset.COIN)) {
                        finalWorldFrame[cx-1][cy] = Tileset.PATH;
                        cx = cx-1;
                        continue;
                    }
                    else if (cx != WIDTH-1 && !grid[cx+1][cy].equals(Tileset.WALL) && dist[cx+1][cy] > dist[cx][cy]
                            && !finalWorldFrame[cx+1][cy].equals(Tileset.COIN)) {
                        finalWorldFrame[cx+1][cy] = Tileset.PATH;
                        cx = cx+1;
                        continue;
                    }
                    else if (cy != 0 && !grid[cx][cy-1].equals(Tileset.WALL) && dist[cx][cy-1] > dist[cx][cy]
                            && !finalWorldFrame[cx][cy-1].equals(Tileset.COIN)) {
                        finalWorldFrame[cx][cy-1] = Tileset.PATH;
                        cy = cy-1;
                        continue;
                    }
                    else if (cy != HEIGHT-1 && !grid[cx][cy+1].equals(Tileset.WALL) && dist[cx][cy+1] > dist[cx][cy]
                            && !finalWorldFrame[cx][cy+1].equals(Tileset.COIN)) {
                        finalWorldFrame[cx][cy+1] = Tileset.PATH;
                        cy = cy+1;
                        continue;
                    }
                    break;
                }
            }
        }
        finalWorldFrame[aX][aY] = Tileset.AVATAR;
    }

    public boolean equal(char a, char b) {
        return Character.toUpperCase(a) == Character.toUpperCase(b);
    }
    public void saveWorld() {
        try {
            Utils.writeObject(RANDOM, r);
            PrintWriter pw = new PrintWriter(new FileWriter(avatarStuff));
            pw.println(aX);
            pw.println(aY);
            pw.println(moves);
            pw.println(coinsCollected);
            pw.println(toggleSight);
            pw.println(showPaths);
            for (Square c : coins) {
                pw.print(c.x + " " + c.y + " ");
            }
            pw.println();
            pw.close();
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    File curTile = Utils.join(CWD, String.format("Tile %d %d.txt", i, j));
                    Utils.writeObject(curTile, grid[i][j]);
                }
            }
        }
        catch(IOException ioe) {
            System.out.println("IOException occurred.");
            throw new RuntimeException();
//            System.exit(0);
        }
    }
    public void loadWorld() {
        try {
            r = Utils.readObject(RANDOM, Random.class);
            BufferedReader br = new BufferedReader(new FileReader(avatarStuff));
            aX = Integer.parseInt(br.readLine());
            aY = Integer.parseInt(br.readLine());
            moves = Integer.parseInt(br.readLine());
            coinsCollected = Integer.parseInt(br.readLine());
            toggleSight = Boolean.parseBoolean(br.readLine());
            showPaths = Boolean.parseBoolean(br.readLine());
            StringTokenizer st = new StringTokenizer(br.readLine());
            while(st.hasMoreTokens()) {
                coins.add(new Square(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
            }
            br.close();
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    File curTile = Utils.join(CWD, String.format("Tile %d %d.txt", i, j));
                    grid[i][j] = Utils.readObject(curTile, TETile.class);
                }
            }
        }
        catch (IOException ioe) {
            System.out.println("IOException occurred.");
//            System.exit(0);
            throw new RuntimeException();
        }
    }
    public int countFloorTiles() {
        int floorCount = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                floorCount += grid[i][j].equals(Tileset.FLOOR) ? 1 : 0;
            }
        }
        return floorCount-1;
    }
    public void setRandomFloorTile(TETile tile) {
        int floorCount = countFloorTiles();
        int tileIndex = RandomUtils.uniform(r, floorCount);
        floorCount = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                boolean isAvatar = aX == i && aY == j;
                if (isAvatar) continue;
                if (floorCount == tileIndex && grid[i][j].equals(Tileset.FLOOR)) {
                    if (tile.equals(Tileset.AVATAR)) {
                        aX = i; aY = j;
                        return;
                    } else if (tile.equals(Tileset.COIN)) {
                        coins.add(new Square(i, j));
                    }
                    grid[i][j] = tile;
                    return;
                }
                floorCount += grid[i][j].equals(Tileset.FLOOR) ? 1 : 0;
            }
        }
    }
    private class Square {
        int x, y;
        public Square(int x, int y) {
            this.x = x; this.y = y;
        }
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Square)) return false;
            if (this == o) return true;
            return this.x == ((Square) o).x && this.y == ((Square) o).y;
        }
        @Override
        public int hashCode() {
            return x*1000+y;
        }
    }
    /*
    public void saveWorld2() {
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
    public void loadWorld2() {
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
     */

}
