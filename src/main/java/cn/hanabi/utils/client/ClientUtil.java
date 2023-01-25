package cn.hanabi.utils.client;

import cn.hanabi.gui.classic.notifications.Notification;
import cn.hanabi.gui.classic.notifications.Notification.Type;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.BlockPos;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public enum ClientUtil {
    INSTANCE;
    public static CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();
    public static double addY;

    public static void clear() {
        notifications.clear();
    }

    public static void sendClientMessage(String message, Type type) {
        if (notifications.size() > 8) notifications.remove(0);

        notifications.add(new Notification(message, type));
    }

    public static void sendNotification(Notification notification) {
        if (notifications.size() > 8) notifications.remove(0);
        notifications.add(notification);
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float) c.getRed();
        float g = 0.003921569f * (float) c.getGreen();
        float b = 0.003921569f * (float) c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static boolean isBlockBetween(BlockPos start, BlockPos end) {
        Minecraft mc = Minecraft.getMinecraft();
        int startX = start.getX();
        int startY = start.getY();
        int startZ = start.getZ();
        int endX = end.getX();
        int endY = end.getY();
        int endZ = end.getZ();
        double diffX = endX - startX;
        double diffY = endY - startY;
        double diffZ = endZ - startZ;
        double x = startX;
        double y = startY;
        double z = startZ;
        double STEP = 0.1D;
        int STEPS = (int) Math.max(Math.abs(diffX), Math.max(Math.abs(diffY), Math.abs(diffZ))) * 4;

        for (int i = 0; i < STEPS - 1; ++i) {
            x += diffX / (double) STEPS;
            y += diffY / (double) STEPS;
            z += diffZ / (double) STEPS;
            if (x != (double) endX || y != (double) endY || z != (double) endZ) {
                BlockPos pos = new BlockPos(x, y, z);
                Block block = mc.theWorld.getBlockState(pos).getBlock();
                if (block.getMaterial() != Material.air && block.getMaterial() != Material.water && !(block instanceof BlockVine) && !(block instanceof BlockLadder)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String removeColorCode(String displayString) {
        return displayString.replaceAll("\247.", "");
    }

    public void drawNotifications() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        double startY = res.getScaledHeight() - 25;
        double startY2 = res.getScaledHeight() - 45;
        final double lastY = startY;
        final double lastY2 = startY2;


        notifications.removeIf(Notification::shouldDelete);
    }

}
