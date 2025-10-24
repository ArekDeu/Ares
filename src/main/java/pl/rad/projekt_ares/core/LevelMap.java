package pl.rad.projekt_ares.core;

import java.util.LinkedHashMap;
import java.util.Map;

/** definicja mapy 2D: kafle + regiony do odblokowania */
public final class LevelMap {

    // typ kafla (możesz rozszerzyć później)
    public enum Tile { GRASS, WATER, MOUNTAIN }

    // rozmiar kafla w pikselach (neutralny „kontrakt” z rysowaniem)
    public static final int TILE = 32;

    // wymiary (kafle)
    public final int width;
    public final int height;

    // siatka z kaflami (wczytana cała od początku)
    public final Tile[][] tiles;

    /** regiony logiczne (R1..R4) → prostokąty w siatce */
    public static final class Region {
        public final String id;
        public final int x0, y0, w, h; // punkt startowy + szerokość + wysokość (w kaflach)
        public Region(String id, int x0, int y0, int w, int h) {
            this.id = id; this.x0 = x0; this.y0 = y0; this.w = w; this.h = h;
        }
    }

    public final Map<String, Region> regions = new LinkedHashMap<>();

    /** ładowanie mapy — generowanie demka 40×24 + definicje 8 regionów (R1..R8) */
    public static LevelMap generateDemo() {
        int W = 40, H = 24;           // rozmiar mapy (2× większa)
        Tile[][] t = new Tile[W][H];

        // wypełnianie kaflami — „ramka” górami, środek trawiasty, dwie „rzeki”
        for (int x = 0; x < W; x++) {
            for (int y = 0; y < H; y++) {
                if (x == 0 || y == 0 || x == W-1 || y == H-1) {
                    t[x][y] = Tile.MOUNTAIN;                 // krawędź
                } else if ((y == 7 || y == 16) && x > 2 && x < W-3) {
                    t[x][y] = Tile.WATER;                    // rzeki
                } else {
                    t[x][y] = Tile.GRASS;                    // trawa
                }
            }
        }

        LevelMap map = new LevelMap(W, H, t);

        // definicje regionów — podział wnętrza na 4 kolumny × 2 wiersze
        // kolumny: [9,10,9,10] szerokości; wiersze: [11,11] wysokości
        map.regions.put("R1", new Region("R1",  1,  1,  9, 11));
        map.regions.put("R2", new Region("R2", 10,  1, 10, 11));
        map.regions.put("R3", new Region("R3", 20,  1,  9, 11));
        map.regions.put("R4", new Region("R4", 29,  1, 10, 11));
        map.regions.put("R5", new Region("R5",  1, 12,  9, 11));
        map.regions.put("R6", new Region("R6", 10, 12, 10, 11));
        map.regions.put("R7", new Region("R7", 20, 12,  9, 11));
        map.regions.put("R8", new Region("R8", 29, 12, 10, 11));

        return map;
    }

    /** sprawdzanie kolizji — przejście tylko po trawie (GRASS) wewnątrz mapy */
    public boolean isPassable(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height && tiles[x][y] == Tile.GRASS;
    }

    private LevelMap(int width, int height, Tile[][] tiles) {
        this.width = width; this.height = height; this.tiles = tiles;
    }
}
