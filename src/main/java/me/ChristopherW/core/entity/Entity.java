package me.ChristopherW.core.entity;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Quaternion;
import me.ChristopherW.core.utils.Utils;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Entity {
    private String name;
    private PhysicsRigidBody rigidBody;
    private Model model;
    private Vector3f position, rotation, scale;
    private boolean isVisible;
    private boolean enabled;
    private PhysicsSpace physicsSpace;

    public Entity(Entity source) {
        this.name = source.name;
        this.model = new Model(source.model, source.model.getMaterial().getTexture());
        this.position = new Vector3f(source.position);
        this.rotation = new Vector3f(source.rotation);
        this.scale = new Vector3f(source.scale);
        this.isVisible = source.isVisible;
        this.enabled = source.enabled;
        this.rigidBody = source.rigidBody;
        this.physicsSpace = source.physicsSpace;
    }

    public Entity(Model model, Vector3f position, Vector3f rotation, Vector3f scale, PhysicsSpace space) {
        this.name = "New Object";
        this.model = model;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.isVisible = true;
        this.enabled = true;
        this.rigidBody = null;
        this.physicsSpace = space;
    }

    public Entity(String name, Model model, Vector3f zero, Vector3f zero2, Vector3f vector3f, PhysicsSpace space) {
        this.name = name;
        this.model = model;
        this.position = zero;
        this.rotation = zero2;
        this.scale = vector3f;
        this.isVisible = true;
        this.enabled = true;
        this.rigidBody = null;
        this.physicsSpace = space;
    }

    public boolean isEnabled() {
        return enabled;
    }


    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(enabled) {
            physicsSpace.addCollisionObject(this.rigidBody);
            this.setVisible(true);
        } else {
            physicsSpace.removeCollisionObject(this.rigidBody);
            this.setVisible(false);
        }
    }

    public void localTranslate(float x, float y, float z) {
        if(z != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * z;
        }
        if(x != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * x;
        }
        position.y += y;
    }
    public Vector3f toLocale(float x, float y, float z, Vector3f rot) {
        Vector3f vec = new Vector3f();
        if(z != 0) {
            vec.x = (float) Math.sin(Math.toRadians(rot.y)) * -1.0f * z;
            vec.z = (float) Math.cos(Math.toRadians(rot.y)) * z;
        }
        if(x != 0) {
            vec.x = (float) Math.sin(Math.toRadians(rot.y - 90)) * -1.0f * x;
            vec.z = (float) Math.cos(Math.toRadians(rot.y - 90)) * x;
        }
        vec.y = y;
        return vec;
    }
    public Vector3f toLocale(float x, float y, float z) {
        Vector3f vec = new Vector3f();
        if(z != 0) {
            vec.x = (float) Math.sin(Math.toRadians(this.rotation.y)) * -1.0f * z;
            vec.z = (float) Math.cos(Math.toRadians(this.rotation.y)) * z;
        }
        if(x != 0) {
            vec.x = (float) Math.sin(Math.toRadians(this.rotation.y - 90)) * -1.0f * x;
            vec.z = (float) Math.cos(Math.toRadians(this.rotation.y - 90)) * x;
        }
        vec.y = y;
        return vec;
    }
    public void localTranslate(float x, float y, float z, Vector3f rot) {
        if(z != 0) {
            position.x += (float) Math.sin(Math.toRadians(rot.y)) * -1.0f * z;
            position.z += (float) Math.cos(Math.toRadians(rot.y)) * z;
        }
        if(x != 0) {
            position.x += (float) Math.sin(Math.toRadians(rot.y - 90)) * -1.0f * x;
            position.z += (float) Math.cos(Math.toRadians(rot.y - 90)) * x;
        }
        position.y += y;
    }
    public void translate(float x, float y, float z) {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
        if(this.rigidBody != null)
            this.rigidBody.setPhysicsLocation(Utils.convert(position));
    }
    public void translate(Vector3f translation) {
        translate(translation.x, translation.y, translation.z);
    }
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        if(this.rigidBody != null)
            this.rigidBody.setPhysicsLocation(Utils.convert(position));
    }
    public void setPosition(Vector3f position) {
        setPosition(position.x, position.y, position.z);
    }

    public void rotate(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }
    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
        if(this.rigidBody != null) {
            Quaternion quat = new Quaternion();
            quat.fromAngles((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
            this.rigidBody.setPhysicsRotation(quat);
        }


    }
    public void setRotation(Vector3f rotation) {
            setRotation(rotation.x, rotation.y, rotation.z);
}
    public void setScale(float scale) {
        setScale(scale, scale, scale);
    }
    public void setScale(Vector3f scale) {
        setScale(scale.x, scale.y, scale.z);
    }
    public void setScale(float x, float y, float z) {
        this.scale.x = x;
        this.scale.y = y;
        this.scale.z = z;
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public PhysicsRigidBody getRigidBody() {
        return rigidBody;
    }

    public void setRigidBody(PhysicsRigidBody rigidBody) {
        this.rigidBody = rigidBody;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public PhysicsSpace getPhysicsSpace() {
        return physicsSpace;
    }

    public void setPhysicsSpace(PhysicsSpace physicsSpace) {
        this.physicsSpace = physicsSpace;
    }
}
