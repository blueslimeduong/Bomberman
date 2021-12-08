package uet.oop.bomberman;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Bomb.Bomb;
import uet.oop.bomberman.entities.Bomb.Explosion;
import uet.oop.bomberman.entities.Character.Bomber;
import uet.oop.bomberman.entities.Character.Character;
import uet.oop.bomberman.entities.Character.CharacterFactory;
import uet.oop.bomberman.entities.Character.Enemy.*;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Items.BombItem;
import uet.oop.bomberman.entities.Items.SpeedItem;
import uet.oop.bomberman.entities.Tiles.*;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.LevelLoader;

import java.util.ArrayList;
import java.util.LinkedList;

public class Map {
    private static Map gameMap = new Map();
    private Map() {}

    public int WIDTH;
    public int HEIGHT;
    public static int level = 1;
    public Entity[][] tiles;
    public ArrayList<Character> characters = new ArrayList<>();
    public LinkedList<Bomb> bombs = new LinkedList<>();
    public static Bomber bomber;
    private LevelLoader levelLoader = new LevelLoader();
    public static Map getMap() {
        return gameMap;
    }

    private void resetMap() {
        tiles = new Entity[HEIGHT][WIDTH];
        characters.clear();
        bombs.clear();
    }

    public void createMap() {
        if(!levelLoader.loadLevel(level)) {
            System.out.println("Can't load map level");
        }
        WIDTH = levelLoader.getWIDTH();
        HEIGHT = levelLoader.getHEIGHT();
        resetMap();
        char[][] mapInput = levelLoader.getMap();
        for (int j = 0; j < HEIGHT; j++) {
            for (int i = 0; i < WIDTH; i++) {
                char c = mapInput[j][i];
                CharacterFactory.getCharacter(i , j, c);
                TileFactory.getTile(i, j, c);
            }
        }
    }

    public void updateMap() {
        for (int j = 0; j < HEIGHT; j++) {
            for (int i = 0; i < WIDTH; i++) {
                tiles[j][i].update();
            }
        }

        characters.forEach(Character::update);

        removeDead();

        for(Bomb b : bombs) {
            b.update();
        }

    }

    public void renderMap(GraphicsContext gc) {
        for (int j = 0; j < HEIGHT; j++) {
            for (int i = 0; i < WIDTH; i++) {
                tiles[j][i].render(gc);
            }
        }

        bombs.forEach(b -> b.render(gc));
        characters.forEach(ch -> ch.render(gc));
    }

    public Entity getTileAt(int x, int y) {
        return tiles[y][x];
    }

    public Character getCharacterAtTile(int x, int y) {
        for (Character character : characters) {
            if(character.getXTile() == x && character.getYTile() == y) {
                return character;
            }
        }
        return null;
    }

    public Bomb getBombAt(int x, int y) {
        for (Bomb bomb : bombs) {
            if (bomb.getXTile() == x && bomb.getYTile() == y) {
                return bomb;
            }
        }
        return null;
    }

    public Explosion getExplosionAt(int x, int y) {
        for (Bomb bomb : bombs) {
            if (bomb.getExplosionAt(x, y) != null) {
                return bomb.getExplosionAt(x, y);
            }
        }
        return null;
    }

    public Entity getEntityAtTile(int x, int y) {
        Entity e;

        e = getExplosionAt(x, y);
        if (e != null) return e;

        e = getBombAt(x, y);
        if (e != null) return e;

        return getTileAt(x, y);
    }

    public void removeDead() {
        try {
            for(int i=0;i<characters.size();i++){
                if(characters.get(i) != null){
                    if (!(characters.get(i) instanceof Enemy)) continue;
                    Enemy e = (Enemy) characters.get(i);
                    if (e.timeAfter <= 0) {
                        characters.remove(e);
                        BombermanGame.SCORE += e.getScore();
                        System.out.println(BombermanGame.SCORE);
                        i--;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
