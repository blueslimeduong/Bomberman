package uet.oop.bomberman.entities.Tiles;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Character.Character;
import uet.oop.bomberman.entities.Entity;

public class Brick extends Entity {
    public Brick(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        gameMap.tiles[yUnit][xUnit] = this;
    }

    @Override
    public void update() {

    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Character && ((Character)e).isWallPass()) {
            return true;
        }
        if (e instanceof Character) {
            ((Character) e).kill();
        }
        return false;
    }
}
