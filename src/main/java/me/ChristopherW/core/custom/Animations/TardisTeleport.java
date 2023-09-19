package me.ChristopherW.core.custom.Animations;

import org.joml.Vector3f;

import me.ChristopherW.core.Animation;
import me.ChristopherW.core.entity.Entity;
import me.ChristopherW.core.utils.GlobalVariables;
import me.ChristopherW.test.Launcher;

public class TardisTeleport implements Animation {

    private Entity tardis;
    private Vector3f startPos;
    public Vector3f getStartPos() {
        return startPos;
    }

    public void setStartPos(Vector3f startPos) {
        this.startPos = startPos;
    }

    private Vector3f endPos;
    public Vector3f getEndPos() {
        return endPos;
    }

    public void setEndPos(Vector3f endPos) {
        this.endPos = endPos;
    }

    public float timeElapsed;
    private boolean isPlaying = false;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public Entity getTardis() {
        return tardis;
    }

    public void setTardis(Entity tardis) {
        this.tardis = tardis;
    }

    boolean teleport = false;
    boolean showDoctor = false;

    @Override
    public void tick(float time) {
        if(!isPlaying)
            return;
        if(timeElapsed >= 8) {
            isPlaying = false;
            tardis.setVisible(false);
            return;
        }
        else if(timeElapsed >= 6) {
            if(!showDoctor) {
                Launcher.getGame().board.getDoctor().setPosition(Launcher.getGame().board.getGridSpaceFromWorld(endPos));
                Launcher.getGame().board.getDoctor().getEntity().setVisible(true);
                showDoctor = true;
            }
            tardis.getPosition().lerp(new Vector3f(endPos).add(0,20,0), (timeElapsed-6f)/20f);
            //System.out.printf("%f\n", (timeElapsed-6f)/2f);
        } else if(timeElapsed >= 4) {
            if(!teleport) {
                tardis.setPosition(new Vector3f(endPos).add(0,20,0));
                teleport = true;
            }
            tardis.getPosition().lerp(new Vector3f(endPos), (timeElapsed-4f)/20f);
            //System.out.printf("%f\n", (timeElapsed-4f)/2f);
        } else if(timeElapsed >= 2) {
            Launcher.getGame().board.getDoctor().getEntity().setVisible(false);
            tardis.getPosition().lerp(new Vector3f(startPos).add(0,20,0), (timeElapsed-2f)/20f);
            //System.out.printf("%f\n", (timeElapsed-2f)/2f);
        } else {
            tardis.getPosition().lerp(new Vector3f(startPos), timeElapsed/20f);
            //System.out.printf("%f\n", timeElapsed/2f);
        }
        timeElapsed += time;
    }

    @Override
    public void play() {
        tardis.setVisible(true);
        this.isPlaying = true;
        this.timeElapsed = 0;
        this.teleport = false;
        this.showDoctor = false;
        tardis.setPosition(new Vector3f(startPos).add(0,20,0));
    }
    
}
