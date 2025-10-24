package pl.rad.projekt_ares.core;

import java.util.HashSet;
import java.util.Set;

/** maska odkryć: zestaw odkrytych komórek + pomoc przy regionach */
public final class FogOfWar {
    private final int w, h;
    private final boolean[][] discovered;

    public FogOfWar(int w, int h) {
        this.w = w; this.h = h;
        this.discovered = new boolean[w][h];
    }

    /** odsłanianie prostokąta (np. regionu) */
    public void revealRect(int x0, int y0, int width, int height) {
        for (int x = x0; x < x0 + width; x++) {
            for (int y = y0; y < y0 + height; y++) {
                if (x >= 0 && y >= 0 && x < w && y < h) discovered[x][y] = true;
            }
        }
    }

    /** sprawdzanie widoczności kafla */
    public boolean isVisible(int x, int y) {
        return x >= 0 && y >= 0 && x < w && y < h && discovered[x][y];
    }

    /** zasilanie maski na podstawie listy regionów z GameState */
    public void applyUnlockedRegions(LevelMap map, Set<String> unlockedRegions) {
        for (String id : unlockedRegions) {
            LevelMap.Region r = map.regions.get(id);
            if (r != null) revealRect(r.x0, r.y0, r.w, r.h);
        }
    }

    /** serializacja uproszczona (opcjonalnie) */
    public Set<String> snapshotVisiblePoints() {
        Set<String> pts = new HashSet<>();
        for (int x = 0; x < w; x++) for (int y = 0; y < h; y++) if (discovered[x][y]) pts.add(x + "," + y);
        return pts;
    }

    /** odsłanianie wokół punktu — „latarka” o promieniu r (w kaflach) */
    public void revealCircle(int cx, int cy, int r) {
        int r2 = r * r;
        int x0 = Math.max(0, cx - r), x1 = Math.min(w - 1, cx + r);
        int y0 = Math.max(0, cy - r), y1 = Math.min(h - 1, cy + r);
        for (int x = x0; x <= x1; x++) {
            for (int y = y0; y <= y1; y++) {
                int dx = x - cx, dy = y - cy;
                if (dx*dx + dy*dy <= r2) discovered[x][y] = true;
            }
        }
    }

}
