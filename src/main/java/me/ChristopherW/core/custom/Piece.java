package me.ChristopherW.core.custom;

import org.joml.Vector2i;

import me.ChristopherW.core.entity.Entity;
import me.ChristopherW.test.Launcher;

public class Piece {

    private Vector2i position;
    private Entity entity;
    private Board board;

    public Piece(Vector2i position, Entity entity, Board board) {
        this.position = position;
        this.entity = entity;
        this.board = board;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Vector2i getPosition() {
        return position;
    }

    public void setPosition(Vector2i position) {
        board.getData()[this.position.y][this.position.x] = null;
        this.position = position;
        board.getData()[this.position.y][this.position.x] = this;
    }
}
