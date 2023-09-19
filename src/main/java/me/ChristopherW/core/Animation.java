package me.ChristopherW.core;

public interface Animation {
    public boolean isPlaying = false;
    public void tick(float elapsed);
    public void play();
}
