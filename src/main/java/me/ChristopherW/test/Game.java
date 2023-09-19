package me.ChristopherW.test;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.jme3.bullet.PhysicsSpace;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import me.ChristopherW.core.Animation;
import me.ChristopherW.core.Camera;
import me.ChristopherW.core.ILogic;
import me.ChristopherW.core.MouseInput;
import me.ChristopherW.core.ObjectLoader;
import me.ChristopherW.core.RenderManager;
import me.ChristopherW.core.WindowManager;
import me.ChristopherW.core.custom.Board;
import me.ChristopherW.core.custom.Dalek;
import me.ChristopherW.core.custom.Doctor;
import me.ChristopherW.core.custom.Piece;
import me.ChristopherW.core.custom.Animations.TardisTeleport;
import me.ChristopherW.core.entity.Entity;
import me.ChristopherW.core.entity.Material;
import me.ChristopherW.core.entity.Texture;
import me.ChristopherW.core.sound.SoundListener;
import me.ChristopherW.core.sound.SoundManager;
import me.ChristopherW.core.sound.SoundSource;
import me.ChristopherW.core.utils.GlobalVariables;
import me.ChristopherW.core.utils.Transformation;
import me.ChristopherW.core.utils.Utils;

public class Game implements ILogic {
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private final SoundManager soundManager;
    PhysicsSpace physicsSpace;

    public HashMap<String, SoundSource> audioSources = new HashMap<>();

    public Map<String, Entity> entities;
    public Map<String, Animation> animations;

    private Camera camera;
    public Board board;
    public static Texture defaultTexture;
    public static Texture highlightedTileTexture;

    private Entity dalekPrefab, doctorPrefab;
    private Vector3f mouseWorldPos = new Vector3f(0, 0, 0);

    public Game() throws Exception {
        // create new instances for these things
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();

        // setup sound system
        soundManager = new SoundManager();
        soundManager.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);

        // create new physics space with custom collision callbacks
        physicsSpace = new PhysicsSpace(PhysicsSpace.BroadphaseType.DBVT);
        physicsSpace.getSolverInfo().setSplitImpulseEnabled(true);
        physicsSpace.setGravity(new com.jme3.math.Vector3f(0, GlobalVariables.GRAVITY, 0));

        // create new camera
        camera = new Camera();

        // set setup the sound listener to be at the world origin and load the audio sounds
        soundManager.setListener(new SoundListener(new Vector3f(0, 0, 0)));
        loadSounds();   
    }

    void loadSounds() {
        try {
            // load the sound file to a buffer, then create a new audio source at the world origin with the buffer attached
            // store that sound source to a map of sounds
            // repeat this for each sound file
            /*SoundBuffer golfHit1Buffer = new SoundBuffer("assets/sounds/golfHit1.ogg");
            soundManager.addSoundBuffer(golfHit1Buffer);
            SoundSource golfHit1Source = new SoundSource(false, false);
            golfHit1Source.setPosition(new Vector3f(0,0,0));
            golfHit1Source.setBuffer(golfHit1Buffer.getBufferId());
            audioSources.put("golfHit1", golfHit1Source);
            soundManager.addSoundSource("golfHit1", golfHit1Source);*/

            //golfHit1Source.setGain( 0.4f);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void init() throws Exception {

        renderer.init();

        // load all the textures
        defaultTexture = new Texture(loader.loadTexture("assets/textures/DefaultTexture.png"));
        highlightedTileTexture = new Texture(loader.loadTextureColor(Color.YELLOW));

        // initialize entities map
        entities = new HashMap<>();
        animations = new HashMap<>();

        dalekPrefab = new Entity("Dalek", loader.loadModel("assets/models/dalek.obj", new Texture(loader.loadTexture("assets/textures/dalek.png"))), new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(0.4f,0.4f,0.4f), physicsSpace);
        dalekPrefab.getModel().getMaterial().setSpecular(1.0f);
        dalekPrefab.getModel().getMaterial().setReflectability(5f);
        doctorPrefab = new Entity("Doctor", loader.loadModel("assets/models/doctor.obj", new Texture(loader.loadTexture("assets/textures/doctor.png"))), new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(0.7f,0.7f,0.7f), physicsSpace);

        board = new Board(12, entities, loader, physicsSpace);
        Entity tardis = new Entity("Tardis", loader.loadModel("assets/models/tardis.obj", new Texture(loader.loadTexture("assets/textures/tardis.png"))), new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1f,1f,1f), physicsSpace);
        tardis.setVisible(false);
        entities.put("Tardis", tardis);
        TardisTeleport teleportAnim = new TardisTeleport();
        teleportAnim.setTardis(tardis);
        animations.put("Teleport", (Animation) teleportAnim);

        board.setData(new Piece[12][12]);
        for(int i = 0; i < 3; i++) {
            int x = (int)(Math.random()*(12));
            int y = (int)(Math.random()*(12));
            board.getData()[y][x] = new Dalek(new Vector2i(x, y), new Entity(dalekPrefab), board);
        }
        
        int x = (int)(Math.random()*(12));
        int y = (int)(Math.random()*(12));
        board.getData()[y][x] = new Doctor(new Vector2i(x, y), new Entity(doctorPrefab), board);
    }

    Vector2f startPos;
    public void mouseDown(long window, int button, int action, int mods, MouseInput input) {
        if(button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS) {
            startPos = new Vector2f(input.getDisplVec());
        }
        if(button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_RELEASE) {
            if(startPos.distance(input.getDisplVec()) < 2f) {
                if(board.getDoctor() == null)
                    return;
                if(board.getHighlightedTile() == null)
                    return;
                if(((TardisTeleport) animations.get("Teleport")).isPlaying())
                    return;
                float distance = (float) board.getHighlightedTile().distance(board.getDoctor().getPosition());
                //System.out.println(distance + " to " + board.getHighlightedTile());
                if(distance < 1.5f) {
                    board.getDoctor().setPosition(board.getHighlightedTile());
                } else {
                    int x = (int)(Math.random()*(12));
                    int y = (int)(Math.random()*(12));
                    TardisTeleport tardisTeleport = (TardisTeleport) animations.get("Teleport");
                    tardisTeleport.setStartPos(board.getWorldSpaceCoord(board.getDoctor().getPosition()).add(0, 0.5f, 0));
                    tardisTeleport.setEndPos(board.getWorldSpaceCoord(x, y).add(0, 0.5f, 0));
                    tardisTeleport.play();
                }
            }
        }
    }

    public float rotationX = 0; // CAMERA ROTATION
    public float rotationY = 0;

    public void keyDown(long window, int key, int scancode, int action, int mods) {
        
        if(key == GLFW.GLFW_KEY_SPACE && action == GLFW.GLFW_PRESS) {
            
        }
    }

    float rotX = 0;
    float rotY = 0;
    float zoom = 1;

    @Override
    public void input(MouseInput input, double deltaTime, int frame) {

        if(input.isLeftButtonPress()) {
            rotX += input.getDisplVec().y * GlobalVariables.MOUSE_SENSITIVITY_X;
            rotY += input.getDisplVec().x * GlobalVariables.MOUSE_SENSITIVITY_X;
            rotY = Utils.clamp(rotY, -90, 90);
        }

        if(window.isKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            rotX -= 1f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            rotX += 1f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            rotY += 1f;
        }
        if(window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            rotY -= 1f;
        }

        // rotate the camera based on the mouse input
        // for each entity in the world
        // sync the visual rotation and positions with the physics rotations and positions
        for(Entity entity : entities.values()) {
            if(entity.getRigidBody() != null) {
                entity.setPosition(Utils.convert(entity.getRigidBody().getPhysicsLocation(null)));
            }
        }

        // update the physics world
        physicsSpace.update((float) deltaTime, 2, false, true, false);
    }

    public void onScroll(double dy) {
        zoom += dy/2;
    }

    float defaultRadius = 20f;
    float theta = 0.0f;
    @Override
    public void update(float interval, MouseInput mouseInput) {
        zoom = Utils.clamp(zoom, 0.25f, 2f);
        rotY = Utils.clamp(rotY, 0, 90);
        float radius = defaultRadius * zoom;

        // orbit the camera around the active ball
        Vector3f orbitVec = new Vector3f();
        orbitVec.x = (float) (Math.abs(radius * Math.cos(Math.toRadians(rotY))) * Math.cos(Math.toRadians(rotX)));
        orbitVec.y = (float) (radius * Math.sin(Math.toRadians(rotY))) + 1;
        orbitVec.z = (float) (Math.abs(radius * Math.cos(Math.toRadians(rotY))) * Math.sin(Math.toRadians(rotX)));

        camera.setPosition(orbitVec);
        camera.setRotation(rotY, rotX-90, camera.getRotation().z);

        if(board.getHighlightedTile() != null && board.getDoctor() != null) {
            if(board.getDoctor().getPosition().equals(board.getHighlightedTile()))
                board.setHighlightedTile(null);
        }

        int i = 0;
        for(int y = 0; y < board.getSize(); y++) {
            for(int x = 0; x < board.getSize(); x++) {
                Vector3f position = board.getWorldSpaceCoord(x, y);
                Entity space = board.getSpaceEntity(x, y);
                if(position.distance(mouseWorldPos) < 1.1f) {
                    if(board.getDoctor() != null) {
                        if(!board.getDoctor().getPosition().equals(new Vector2i(x, y))) {
                            board.setHighlightedTile(new Vector2i(x, y));
                            space.getModel().setMaterial(new Material(highlightedTileTexture));
                        }
                    }
                }
                else {
                    if(space.getName().contains("LightSpace"))
                        space.getModel().setMaterial(board.lightSpace.getModel().getMaterial());
                    if(space.getName().contains("DarkSpace"))
                        space.getModel().setMaterial(board.darkSpace.getModel().getMaterial());
                }
                Piece p = board.getData()[y][x];
                if(p != null) {
                    Entity e = p.getEntity();
                    if(e.getName() == "Doctor") {
                        e.setPosition(new Vector3f(position.x, 0.5f, position.z));
                        e.setPosition(new Vector3f(position.x, 0.5f, position.z));
                        Vector2i highlightedTile = board.getHighlightedTile();
                        if(highlightedTile != null) {
                            Vector3f rotVec = new Vector3f(board.getWorldSpaceCoord(highlightedTile)).sub(e.getPosition());
                            float yaw = (float) Math.toDegrees(Math.atan2(rotVec.z, rotVec.x));
                            e.setRotation(0, -yaw + 90, 0);
                        }
                        entities.put("Doctor", e);
                    } else {
                        i++;
                        e.setPosition(new Vector3f(position.x, 0.75f, position.z));
                        Entity doctor = entities.get("Doctor");
                        if(doctor != null) {
                            Vector3f rotVec = new Vector3f(doctor.getPosition()).sub(e.getPosition());
                            float yaw = (float) Math.toDegrees(Math.atan2(rotVec.z, rotVec.x));
                            e.setRotation(0, -yaw + 90, 0);
                        }
                        entities.put("Dalek" + i, e);
                    }
                }
            }
        }

        for (Animation animation : animations.values()) {
            animation.tick(interval);
        }

        // for each visible entity in the world, process its data before rendered
        for(Entity entity : entities.values()) {
            if(entity.isVisible())
                renderer.processEntity(entity);
        }
    } 

    @Override
    public void render() throws Exception {
        // if the window was resized, update the OpenGL viewport to match
        if(window.isResize()) {
            GL11.glViewport(0,0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        // set the clear color to the sky color
        GL11.glClearColor(GlobalVariables.BG_COLOR.x, GlobalVariables.BG_COLOR.y, GlobalVariables.BG_COLOR.z, GlobalVariables.BG_COLOR.w);
        
        // render to the OpenGL viewport from the perspective of the camera
        renderer.render(camera);

        double[] x = new double[1];
        double[] y = new double[1];
        GLFW.glfwGetCursorPos(window.getWindow(), x, y);

        Matrix4f projMat = window.getProjectionMatrix();
        Matrix4f viewMat = Transformation.createViewMatrix(camera);
        Matrix4f combinedMat = projMat.mul(viewMat);
        combinedMat = combinedMat.invert();

        Vector4f vec = new Vector4f();
        vec.x = (2.0f*((float)(x[0])/(window.getWidth())))-1.0f;
        vec.y = 1.0f-(2.0f*((float)(y[0])/(window.getHeight())));

        FloatBuffer depthBuffer = BufferUtils.createFloatBuffer(1920*1080);
        int framebufferStatus = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
        if (framebufferStatus != GL30.GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("Framebuffer is not complete: " + framebufferStatus);
        }
        int xPos = (int)x[0];
        int yPos = window.getHeight() - (int)y[0];
        GL11.glReadPixels(xPos, yPos, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, depthBuffer);
        float depth = depthBuffer.get();
    
        vec.z = 2.0f * depth - 1.0f;
        vec.w = 1.0f;
        
        Vector4f pos = vec.mul(combinedMat);
        pos.w = 1.0f/pos.w;

        pos.x *= pos.w;
        pos.y *= pos.w;
        pos.z *= pos.w;

        mouseWorldPos = new Vector3f(pos.x, pos.y, pos.z);

        // update the render of the ImGui frame
        window.imGuiGlfw.newFrame();
        ImGui.newFrame();
        window.guiManager.render();
        ImGui.render();
        window.imGuiGl3.renderDrawData(ImGui.getDrawData());
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
        GLFW.glfwPollEvents();
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
