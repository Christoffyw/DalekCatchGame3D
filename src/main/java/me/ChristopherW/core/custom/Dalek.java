package me.ChristopherW.core.custom;

import org.joml.Vector2i;

import me.ChristopherW.core.entity.Entity;

public class Dalek extends Piece {
    private boolean hasCrashed;

    public Dalek(Vector2i position, Entity entity, Board board) {
        super(position, entity, board);
    }

    public void advanceTowards(Doctor doc) {

    }

    public void crash() {

    }

    public boolean hasCrashed() {
        return hasCrashed;
    }

    public void setHasCrashed(boolean hasCrashed) {
        this.hasCrashed = hasCrashed;
    }
}
