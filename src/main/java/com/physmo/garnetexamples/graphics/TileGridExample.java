package com.physmo.garnetexamples.graphics;

import com.physmo.garnet.Garnet;
import com.physmo.garnet.GarnetApp;
import com.physmo.garnet.graphics.Graphics;
import com.physmo.garnet.graphics.Texture;
import com.physmo.garnet.graphics.TileSheet;
import com.physmo.garnet.graphics.Viewport;
import com.physmo.garnet.input.Mouse;
import com.physmo.garnet.tilegrid.TileGridData;
import com.physmo.garnet.tilegrid.TileGridDrawer;

// NOTE: On MacOS the following VM argument is required: -XstartOnFirstThread
public class TileGridExample extends GarnetApp {

    static int tileGridViewportId = 1;
    String imageFileName = "prototypeArt.png";
    TileSheet tileSheet;
    Texture texture;
    double scrollX = 0;
    double scrollY = 0;

    TileGridDrawer tileGridDrawer;
    TileGridData tileGridData;

    int wallTileID;
    int grassTileID;
    Viewport viewport;

    public TileGridExample(Garnet garnet, String name) {
        super(garnet, name);
    }

    public static void main(String[] args) {
        Garnet garnet = new Garnet(400, 400);
        GarnetApp app = new TileGridExample(garnet, "");

        garnet.setApp(app);

        garnet.init();
        garnet.run();
    }

    @Override
    public void init(Garnet garnet) {

        texture = Texture.loadTexture(imageFileName);
        tileSheet = new TileSheet(texture, 16, 16);

        Graphics graphics = garnet.getGraphics();

        graphics.addTexture(texture);

        wallTileID = tileSheet.getTileIndexFromCoords(0, 7);
        grassTileID = tileSheet.getTileIndexFromCoords(1, 7);

        int mapWidth = 15;
        int mapHeight = 15;

        tileGridData = new TileGridData(mapWidth, mapHeight);
        tileGridDrawer = new TileGridDrawer()
                .setData(tileGridData)
                .setTileSize(16, 16)
                .setTileSheet(tileSheet)
                .setViewportId(tileGridViewportId);

        viewport = graphics.getViewportManager().getViewport(tileGridViewportId);
        viewport.setWidth(400 - 20)
                .setHeight(400 - 20)
                .setWindowX(10)
                .setWindowY(10)
                .setClipActive(true)
                .setDrawDebugInfo(true)
                .setZoom(4);

        graphics.setActiveViewport(tileGridViewportId);

        // Fill the tile map with grass and walls
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                int tileId = grassTileID;//random.nextInt(5);
                if (x == 0 || y == 0 || x == mapWidth - 1 || y == mapHeight - 1) tileId = wallTileID;
                tileGridData.setTileId(x, y, tileId);
            }
        }


    }

    @Override
    public void tick(double delta) {

        // Scroll tile window with middle mouse button.
        if (garnet.getInput().getMouse().isButtonPressed(Mouse.BUTTON_MIDDLE)) {
            int[] mousePosition = garnet.getInput().getMouse().getPosition();
            scrollX = mousePosition[0];
            scrollY = mousePosition[1];
        }

        viewport.setX(scrollX);
        viewport.setY(scrollY);

        // Resize window with mouse.
        if (garnet.getInput().getMouse().isButtonPressed(Mouse.BUTTON_LEFT)) {
            int[] mousePosition = garnet.getInput().getMouse().getPosition();
            viewport.setWidth(mousePosition[0]);
            viewport.setHeight(mousePosition[1]);
        }

        // Move window with mouse (Right button)
        if (garnet.getInput().getMouse().isButtonPressed(Mouse.BUTTON_RIGHT)) {
            int[] mousePosition = garnet.getInput().getMouse().getPosition();
            viewport.setWindowX(mousePosition[0]);
            viewport.setWindowY(mousePosition[1]);
        }
    }

    @Override
    public void draw(Graphics g) {
        tileGridDrawer.draw(g, 0, 0);
        g.drawImage(tileSheet.getSubImage(0, 0), 64, 86);
    }

}
