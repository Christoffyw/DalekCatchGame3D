package me.ChristopherW.core.custom;

import org.joml.Vector2i;
import org.joml.Vector3f;

import com.jme3.bullet.PhysicsSpace;

import me.ChristopherW.core.entity.Entity;
import me.ChristopherW.core.entity.Model;

public class Doctor extends Piece {

    public Doctor(Vector2i position, Entity entity, Board board) {
        super(position, entity, board);
    }
}
