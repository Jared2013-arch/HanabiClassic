package cn.hanabi.utils;

import cn.hanabi.events.*;
import cn.hanabi.injection.interfaces.IKeyBinding;
import cn.hanabi.utils.rotation.RotationUtil;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.MathHelper;

import static cn.hanabi.Wrapper.mc;

public final class RotationComponent {

    public static boolean isRotationg;

    public static boolean active;
    public static Vector2f rotations;
    public static Vector2f lastRotations;
    public static Vector2f targetRotations;
    public static Vector2f lastServerRotations;
    private static boolean smoothed;
    private static double rotationSpeed;
    private static MovementFix correctMovement;
    public static float movementYaw;
    public static float velocityYaw;
    Object playerYaw = null;
    private int count = 0;

    /*
     * This method must be called on Pre Update Event to work correctly
     */
    public static void setRotations(final Vector2f rotations, final double rotationSpeed, final MovementFix correctMovement) {
        RotationComponent.targetRotations = rotations;
        RotationComponent.rotationSpeed = rotationSpeed * 18;
        RotationComponent.correctMovement = correctMovement;
        active = true;

        smooth();
    }

    public static void setRotations(final float[] rotations, double rotationSpeed, MovementFix correctMovement) {
        setRotations(new Vector2f(rotations[0], rotations[1]), rotationSpeed, correctMovement);
    }




    private static void correctDisabledRotations() {
        if (mc.thePlayer == null) {
            return;
        }

        final Vector2f rotations = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        final Vector2f fixedRotations = RotationUtil.resetRotation(RotationUtil.applySensitivityPatchA(rotations, lastRotations));

        mc.thePlayer.rotationYaw = fixedRotations.x;
        mc.thePlayer.rotationPitch = fixedRotations.y;
    }

    public static void smooth() {
        if (!smoothed) {
            final float lastYaw = lastRotations.x;
            final float lastPitch = lastRotations.y;
            final float targetYaw = targetRotations.x;
            final float targetPitch = targetRotations.y;

            rotations = RotationUtil.smooth(new Vector2f(lastYaw, lastPitch), new Vector2f(targetYaw, targetPitch),
                    rotationSpeed + Math.random());

            if (correctMovement == MovementFix.NORMAL || correctMovement == MovementFix.TRADITIONAL) {
                movementYaw = rotations.x;
            }

            velocityYaw = rotations.x;
        }

        smoothed = true;

        /*
         * Updating MouseOver
         */
        mc.entityRenderer.getMouseOver(1);
    }

    public static void stopRotation() {
        active = false;

        correctDisabledRotations();
    }

    @EventTarget
    public void onUpdateEvent(EventUpdate e) {
        if (!RotationComponent.active || RotationComponent.rotations == null || RotationComponent.lastRotations == null || RotationComponent.targetRotations == null || RotationComponent.lastServerRotations == null) {
            RotationComponent.rotations = (RotationComponent.lastRotations = (RotationComponent.targetRotations = (RotationComponent.lastServerRotations = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch))));
        }
        if (RotationComponent.active) {
            smooth();
        }
        if (RotationComponent.correctMovement == MovementFix.BACKWARDS_SPRINT && RotationComponent.active && Math.abs(RotationComponent.rotations.x - Math.toDegrees(direction())) > 45.0) {
            ((IKeyBinding) mc.gameSettings.keyBindSprint).setPress(false);
            mc.thePlayer.setSprinting(false);
        }
    }

    @EventTarget
    public void onEventMoveInput(EventMoveInput event) {
        if (active && correctMovement == MovementFix.NORMAL && rotations != null) {
            /*
             * Calculating movement fix
             */
            final float yaw = rotations.x;
            fixMovement(event, yaw);
        }
    }

    /**
     * Gets the players' movement yaw
     */

    public double direction() {
        float rotationYaw = movementYaw;

        if (mc.thePlayer.moveForward < 0) {
            rotationYaw += 180;
        }

        float forward = 1;

        if (mc.thePlayer.moveForward < 0) {
            forward = -0.5F;
        } else if (mc.thePlayer.moveForward > 0) {
            forward = 0.5F;
        }

        if (mc.thePlayer.moveStrafing > 0) {
            rotationYaw -= 70 * forward;
        }

        if (mc.thePlayer.moveStrafing < 0) {
            rotationYaw += 70 * forward;
        }

        return Math.toRadians(rotationYaw);
    }

    public double direction(float rotationYaw, final double moveForward, final double moveStrafing) {
        if (moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (moveForward < 0F) forward = -0.5F;
        else if (moveForward > 0F) forward = 0.5F;

        if (moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

    public void fixMovement(EventMoveInput event, final float yaw) {
        final float forward = event.getForward();
        final float strafe = event.getStrafe();

        final double angle = MathHelper.wrapAngleTo180_double(Math.toDegrees(direction(mc.thePlayer.rotationYaw, forward, strafe)));

        if (forward == 0 && strafe == 0) {
            return;
        }

        float closestForward = 0, closestStrafe = 0, closestDifference = Float.MAX_VALUE;

        for (float predictedForward = -1F; predictedForward <= 1F; predictedForward += 1F) {
            for (float predictedStrafe = -1F; predictedStrafe <= 1F; predictedStrafe += 1F) {
                if (predictedStrafe == 0 && predictedForward == 0) continue;

                final double predictedAngle = MathHelper.wrapAngleTo180_double(Math.toDegrees(direction(yaw, predictedForward, predictedStrafe)));
                final double difference = Math.abs(angle - predictedAngle);

                if (difference < closestDifference) {
                    closestDifference = (float) difference;
                    closestForward = predictedForward;
                    closestStrafe = predictedStrafe;
                }
            }
        }

        event.setForward(closestForward);
        event.setStrafe(closestStrafe);
    }


    @EventTarget
    public void onEventLook(EventLook event) {
        if (active && rotations != null) {
            event.setRotation(rotations);
        }
    }

    @EventTarget
    public void onPlayerMoveUpdateEvent(EventStrafe a) {
        if (RotationComponent.active && (RotationComponent.correctMovement == MovementFix.NORMAL || RotationComponent.correctMovement == MovementFix.TRADITIONAL) && RotationComponent.rotations != null) {
            a.setYaw(RotationComponent.rotations.x);
        }
    }

    @EventTarget
    public void onJumpFixEvent(EventJump event) {
        if (RotationComponent.active && (RotationComponent.correctMovement == MovementFix.NORMAL || RotationComponent.correctMovement == MovementFix.TRADITIONAL || RotationComponent.correctMovement == MovementFix.BACKWARDS_SPRINT) && RotationComponent.rotations != null) {
            event.setYaw(RotationComponent.rotations.x);
        }
    }

    private float getRandomYaw(float requestedYaw, int rand) {
        return requestedYaw + (360 * rand);
    }

    @EventTarget
    public void onPacketSendEvent(EventPacket event) {
        if (event.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            count += 1;
            if (count > 15) {
                count = 1;
            }
        }
    }

    @EventTarget
    public void onMotionEvent(EventPreMotion event) {
            if (active && rotations != null) {
                final float yaw = rotations.x;
                final float pitch = rotations.y;

                event.setYaw(yaw);
                event.setPitch(pitch);

                mc.thePlayer.renderYawOffset = yaw;
                mc.thePlayer.rotationYawHead = yaw;
                mc.thePlayer.renderArmPitch = pitch;

                lastServerRotations = new Vector2f(yaw, pitch);

                if (Math.abs((rotations.x - mc.thePlayer.rotationYaw) % 360) < 1 && Math.abs((rotations.y - mc.thePlayer.rotationPitch)) < 1) {
                    active = false;

                    correctDisabledRotations();
                }

                lastRotations = rotations;

            } else {
                lastRotations = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            }

            targetRotations = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
            smoothed = false;
    }

    public enum MovementFix {
        OFF("Off"),
        NORMAL("Normal"),
        TRADITIONAL("Traditional"),
        BACKWARDS_SPRINT("Backwards Sprint");

        final String name;

        MovementFix(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}