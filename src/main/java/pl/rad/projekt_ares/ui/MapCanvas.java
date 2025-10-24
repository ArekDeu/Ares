package pl.rad.projekt_ares.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import pl.rad.projekt_ares.core.FogOfWar;
import pl.rad.projekt_ares.core.LevelMap;

/** render mapy: kafle + półprzezroczysta mgła na nieodkrytych polach */
public final class MapCanvas extends Canvas {
    private final LevelMap map;
    private final FogOfWar fog;

    // pozycja gracza (kafle)
    private int px = -1, py = -1;

    public void setPlayerPosition(int x, int y) {
        this.px = x; this.py = y;
    }

    public MapCanvas(LevelMap map, FogOfWar fog) {
        this.map = map;
        this.fog = fog;
        // ustawienie rozmiaru płótna na rozmiar mapy
        setWidth(map.width * LevelMap.TILE);
        setHeight(map.height * LevelMap.TILE);
    }

    /**
     * render mapy — rysowanie kafli, siatki pomocniczej i półprzezroczystej mgły
     */
    public void redraw() {
        GraphicsContext g = getGraphicsContext2D();

        // tło
        g.setFill(Color.web("#1e1e1e"));
        g.fillRect(0, 0, getWidth(), getHeight());

        // rysowanie kafli (odkryte lekko jaśniejsze)
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                boolean vis = fog.isVisible(x, y);
                Color c;
                switch (map.tiles[x][y]) {
                    case GRASS -> c = vis ? Color.web("#46b02c") : Color.web("#3a9d23");
                    case WATER -> c = vis ? Color.web("#2b7fe0") : Color.web("#1f6ac6");
                    case MOUNTAIN -> c = vis ? Color.web("#7a756e") : Color.web("#6b6761");
                    default -> c = Color.GRAY;
                }
                g.setFill(c);
                g.fillRect(x * LevelMap.TILE, y * LevelMap.TILE, LevelMap.TILE, LevelMap.TILE);
            }
        }

        // siatka pomocnicza
        g.setStroke(Color.color(0,0,0,0.25));
        for (int x = 0; x <= map.width; x++)
            g.strokeLine(x*LevelMap.TILE, 0, x*LevelMap.TILE, map.height*LevelMap.TILE);
        for (int y = 0; y <= map.height; y++)
            g.strokeLine(0, y*LevelMap.TILE, map.width*LevelMap.TILE, y*LevelMap.TILE);

        // mgła (nieodkryte)
        g.setFill(Color.color(0,0,0, 0.60));
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                if (!fog.isVisible(x, y)) {
                    g.fillRect(x * LevelMap.TILE, y * LevelMap.TILE, LevelMap.TILE, LevelMap.TILE);
                }
            }
        }
        // gracz (na wierzchu)
        if (px >= 0 && py >= 0) {
            double cx = px * LevelMap.TILE + LevelMap.TILE / 2.0;
            double cy = py * LevelMap.TILE + LevelMap.TILE / 2.0;
            double r = LevelMap.TILE * 0.35;
            g.setFill(Color.WHITE);
            g.fillOval(cx - r, cy - r, 2*r, 2*r); // „kafelek gracza” — jasne kółko
            g.setStroke(Color.BLACK);
            g.strokeOval(cx - r, cy - r, 2*r, 2*r);
        }
    }
}
