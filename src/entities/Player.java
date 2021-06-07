package entities;

import models.TexturedModel;
import org.joml.Vector3f;
import renderEngine.MainGameLoop;

public class Player extends Entity implements Keys{

    private static final float RUN_SPEED = 5;
    private static final float TURN_SPEED = 15;
    private static final float GRAVITY = -0.42f;
    private static final float JUMP_HEIGHT = 14;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float verticalSpeed = 0;

    private boolean toMove = true;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(int key) {

        if (toMove) {
            checkInput(key);

            super.increaseRotation(0, currentTurnSpeed * MainGameLoop.getDelta(), 0);

            float distance = currentSpeed * MainGameLoop.getDelta();
            float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
            float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
            super.increasePosition(dx, 0, dz);
            toMove = false;
        }else{
            toMove = true;

        }
    }

    private void checkInput(int key) {

        if (key == KEY_W)
            this.currentSpeed = RUN_SPEED;
        else if (key == KEY_S)
            this.currentSpeed = -RUN_SPEED;
        else
            this.currentSpeed = 0;

        if(key == KEY_D)
            this.currentTurnSpeed = -TURN_SPEED;
        else if (key == KEY_A)
            this.currentTurnSpeed = TURN_SPEED;
        else
            this.currentTurnSpeed = 0;

        if(key == KEY_SPACEBAR)
            jump();

    }

    private void jump() {
        if(super.getPosition().y <= 0)
            verticalSpeed = JUMP_HEIGHT;
            super.increasePosition(0, verticalSpeed, 0);
    }

    public void gravity() {
        verticalSpeed = 0;
        if(super.getPosition().y > 0)
            super.increasePosition(0, GRAVITY * MainGameLoop.getDelta(), 0);
        if(super.getPosition().y < 0)
            super.getPosition().y = 0;
    }
}
