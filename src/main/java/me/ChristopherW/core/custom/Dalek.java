package me.ChristopherW.core.custom;

import org.joml.Vector2i;

import me.ChristopherW.core.entity.Entity;
import me.ChristopherW.test.Launcher;

public class Dalek extends Piece {
    private boolean hasCrashed;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Dalek(Vector2i position, Entity entity, Board board) {
        super(position, entity, board);
    }

    public void advanceTowardsDoctor() {
        if(hasCrashed)
            return;

        Doctor doctor = Launcher.getGame().board.getDoctor();
        if(doctor == null)
            return;
        int newX = 0, newY = 0;
        if(doctor.getPosition().x != getPosition().x)
            newX = (doctor.getPosition().x - getPosition().x)/Math.abs(doctor.getPosition().x-getPosition().x);
        if(doctor.getPosition().y != getPosition().y)
            newY = (doctor.getPosition().y - getPosition().y)/Math.abs(doctor.getPosition().y-getPosition().y);

        Vector2i newPos = new Vector2i(this.getPosition()).add(newX, newY);
        if(Launcher.getGame().board.getData()[newPos.y][newPos.x] instanceof Dalek) {
            Launcher.getGame().entities.remove("Dalek" + ((Dalek) Launcher.getGame().board.getData()[newPos.y][newPos.x]).getId());
            ((Dalek) Launcher.getGame().board.getData()[newPos.y][newPos.x]).setHasCrashed(true);
            Launcher.getGame().entities.remove("Dalek" + getId());
            Launcher.getGame().board.getData()[getPosition().y][getPosition().x] = null;
            Entity crashSite = new Entity(Launcher.getGame().dalekCrashedPrefab);
            crashSite.setPosition(Launcher.getGame().board.getWorldSpaceCoord(newPos.x, newPos.y));
            int randomRotation = (int)(Math.random()*(360));
            crashSite.setRotation(0, randomRotation, 0);
            Launcher.getGame().entities.put("DalekCrashed" + getId(), crashSite);

            return;
        }
        setPosition(newPos);
    }

    public boolean hasCrashed() {
        return hasCrashed;
    }

    public void setHasCrashed(boolean hasCrashed) {
        this.hasCrashed = hasCrashed;
        this.getEntity().setVisible(false);
    }
}
