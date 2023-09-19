package me.ChristopherW.core.custom;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jme3.bullet.PhysicsSpace;

import org.joml.Vector2i;
import org.joml.Vector3f;

import me.ChristopherW.core.ObjectLoader;
import me.ChristopherW.core.entity.Entity;
import me.ChristopherW.core.entity.Texture;
import me.ChristopherW.test.Game;

public class Board {
    private Piece[][] data;
    private Entity[][] spaces;
    public Entity darkSpace;
    public Entity lightSpace;
    private Vector2i highlightedTile;
    public Vector2i getHighlightedTile() {
        return highlightedTile;
    }

    public void setHighlightedTile(Vector2i highlightedTile) {
        if(highlightedTile == null) {
            Entity space = spaces[this.highlightedTile.y][this.highlightedTile.x];
            if(space.getName().contains("LightSpace"))
                space.getModel().setMaterial(this.lightSpace.getModel().getMaterial());
            if(space.getName().contains("DarkSpace"))
                space.getModel().setMaterial(this.darkSpace.getModel().getMaterial());
        }
        this.highlightedTile = highlightedTile;
    }

    private int size;
    int spacing = 2;
    int offsetX = 11;
    int offsetZ = 11;

    public Doctor getDoctor() {
        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                if(data[y][x] instanceof Doctor) {
                    return (Doctor) data[y][x];
                }
            }   
        }
        return null;
    }

    public Piece[][] getData() {
        return data;
    }

    public int getSize() {
        return size;
    }

    public Entity getSpaceEntity(int x, int y) {
        return spaces[y][x];
    }

    public Vector2i getGridSpaceFromWorld(Vector3f in) {
        int x = (int) ((in.x + offsetX) / spacing);
        int y = (int) ((in.z + offsetZ) / spacing);
        return new Vector2i(x, y);
    }

    public Vector3f getWorldSpaceCoord(int x, int y) {
        return new Vector3f((x * spacing) - offsetX, 0, (y * spacing) - offsetZ);
    }
    public Vector3f getWorldSpaceCoord(Vector2i in) {
        return getWorldSpaceCoord(in.x, in.y);
    }

    public Board(int size, Map<String, Entity> entities, ObjectLoader loader, PhysicsSpace physicsSpace) {
        data = new Piece[size][size]; 
        spaces = new Entity[size][size];
        this.size = size;
        Entity board = new Entity(loader.loadModel("assets/models/cube.obj", Game.defaultTexture), new Vector3f(0, -0.01f, 0), new Vector3f(0, 0, 0), new Vector3f(12, 0.5f, 12), physicsSpace);
        entities.put("Board", board);

        darkSpace = new Entity("DarkSpace", 
            loader.loadModel("assets/models/cube.obj", new Texture(loader.loadTextureColor(new Color(73,128,120)))),
            new Vector3f(0, 0.5f, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(1f, 0.01f, 1f),
            physicsSpace
        );
        lightSpace = new Entity("LightSpace", 
            loader.loadModel("assets/models/cube.obj", new Texture(loader.loadTextureColor(new Color(97,159,172)))),
            new Vector3f(0, 0.5f, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(1f, 0.01f, 1f),
            physicsSpace
        );

        int i = 0;
        for(int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if(i % 2 == 0) {
                    Entity space = new Entity(lightSpace);
                    spaces[y][x] = space;
                    space.setName(space.getName() + (i + 1));
                    space.setPosition(new Vector3f((x * spacing) - offsetX, space.getPosition().y, (y * spacing) - offsetZ));
                    entities.put(space.getName(), space);
                } else {
                    Entity space = new Entity(darkSpace);
                    spaces[y][x] = space;
                    space.setName(space.getName() + (i + 1));
                    space.setPosition(new Vector3f((x * spacing) - offsetX, space.getPosition().y, (y * spacing) - offsetZ));
                    entities.put(space.getName(), space);
                }
                i++;
            }
            i++;
        }
    }

    public void setData(Piece[][] entities) {
        this.data = entities;
    }
}
