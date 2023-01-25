package cn.hanabi.injection.mixins;

import cn.hanabi.Client;
import cn.hanabi.Hanabi;
import cn.hanabi.events.EventGui;
import cn.hanabi.events.EventKey;
import cn.hanabi.events.EventMouse;
import cn.hanabi.events.EventTick;
import cn.hanabi.injection.interfaces.IMinecraft;
import cn.hanabi.utils.client.IconUtils;
import cn.hanabi.utils.client.OSUtils;
import cn.hanabi.utils.game.PacketHelper;
import cn.hanabi.utils.math.AnimationUtil;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.utils.render.RenderUtil;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.nio.ByteBuffer;

@Mixin(Minecraft.class)
@SideOnly(Side.CLIENT)
public abstract class MixinMinecraft implements IMinecraft {
    @Shadow
    private int rightClickDelayTimer;
    @Shadow
    public GuiScreen currentScreen;
    @Shadow
    public int displayWidth;
    @Shadow
    public int displayHeight;
    long lastFrame;

    @Shadow
    @Mutable
    @Final
    private Session session;
    @Shadow
    private LanguageManager mcLanguageManager;
    @Shadow
    private Timer timer;
    @Shadow
    private int leftClickCounter;
    @Shadow
    private boolean fullscreen;

    @Shadow
    protected abstract void clickMouse();

    @Shadow
    protected abstract void rightClickMouse();

    public void runCrinkMouse() {
        this.clickMouse();
    }

    public void runRightMouse() {
        this.rightClickMouse();
    }

    /*
     * // 绕过Forge System.exit() 等一系列安全检测 private void injectSecurityManager() throws
     * IllegalAccessException, NoSuchFieldException, InvocationTargetException,
     * NoSuchMethodException { SecurityManager sm = System.getSecurityManager();
     *
     * if ((sm != null) && (sm.getClass().getClassLoader() != null)) {
     * AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
     * sm.getClass().getProtectionDomain().implies(SecurityConstants.ALL_PERMISSION)
     * ; return null; }); }
     *
     * Method reflectionDataMethod =
     * Class.class.getDeclaredMethod("reflectionData");
     * reflectionDataMethod.setAccessible(true); Object reflectionData =
     * reflectionDataMethod.invoke(System.class);
     *
     * Field[] res = null; if (reflectionData != null) { Field field =
     * reflectionData.getClass().getDeclaredField("declaredFields");
     * field.setAccessible(true); res = (Field[]) field.get(reflectionData); }
     *
     * // No cached value available; request value from VM if(res == null) { Method
     * method = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
     * method.setAccessible(true); res = (Field[]) method.invoke(System.class,
     * false); }
     *
     * for(Field re : res) { if(re.getName().equals("security")) {
     * re.setAccessible(true); re.set(null, sm); } } }
     */

    public void setClickCounter(int a) {
        this.leftClickCounter = a;
    }


    @Inject(method = "run", at = @At("HEAD"))
    private void init(CallbackInfo callbackInfo) {
        if (displayWidth < 1100)
            displayWidth = 1100;

        if (displayHeight < 630)
            displayHeight = 630;
    }

    @Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER))
    private void startGame(CallbackInfo ci) {
        Client.makeSense();
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/SplashProgress;clearVanillaResources(Lnet/minecraft/client/renderer/texture/TextureManager;Lnet/minecraft/util/ResourceLocation;)V", shift = At.Shift.BEFORE))
    private void login(CallbackInfo ci) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
    }

    @Inject(method = "createDisplay", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void createDisplay(CallbackInfo callbackInfo) {
        Client.doLogin();
    }

    private long lasteFrame = getTime();

    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    private void runGameLoop(CallbackInfo ci) {
        PacketHelper.onUpdate();
        final long currentTime = getTime();
        final int deltaTime = (int) (currentTime - lasteFrame);
        lasteFrame = currentTime;
        RenderUtil.delta = deltaTime;

        Client.onGameLoop();
    }


    @Inject(
            method = "clickMouse",
            at = @At("HEAD")
    )
    private void clickMouse(CallbackInfo callbackInfo) {
        EventManager.call(new EventMouse(EventMouse.Button.Left));
    }

    @Inject(
            method = "rightClickMouse",
            at = @At("HEAD")
    )
    private void rightClickMouse(CallbackInfo callbackInfo) {
        EventManager.call(new EventMouse(EventMouse.Button.Right));
    }

    @Inject(
            method = "middleClickMouse",
            at = @At("HEAD")
    )
    private void middleClickMouse(CallbackInfo callbackInfo) {
        EventManager.call(new EventMouse(EventMouse.Button.Middle));
    }


    @Inject(method = "runTick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        EventManager.call(new EventTick());
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;dispatchKeypresses()V", shift = At.Shift.AFTER))
    private void onKey(CallbackInfo ci) {
        if (Keyboard.getEventKeyState() && currentScreen == null)
            EventManager.call(new EventKey(
                    Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()));
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    private void onShutdown(CallbackInfo ci) {
        Hanabi.INSTANCE.stopClient();
    }

    @Inject(method = "displayGuiScreen", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;currentScreen:Lnet/minecraft/client/gui/GuiScreen;", shift = At.Shift.AFTER))
    private void displayGuiScreen(CallbackInfo callbackInfo) {
        EventManager.call(new EventGui(currentScreen));
    }

    @Inject(method = "setWindowIcon", at = @At("HEAD"), cancellable = true)
    private void setWindowIcon(CallbackInfo callbackInfo) {
        if (OSUtils.getOperatingSystemType() != OSUtils.OSType.MacOS) {
            final ByteBuffer[] favicon = IconUtils.getFavicon();
            if (favicon != null) {
                Display.setIcon(favicon);
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "toggleFullscreen", at = @At("RETURN"))
    public void toggleFullscreen(CallbackInfo info) {
        if (!this.fullscreen) {
            Display.setResizable(false);
            Display.setResizable(true);
        }
    }

    short stage = -1;
    float pos = 0;
    TimeHelper timerhelper = new TimeHelper();

    private void drawSplashScreen(int stage, int totalStage, String arg) {
        if (stage < 0)
            return;
        float posF = stage / (float) totalStage * 120;
        while (pos < posF) {
            if (timerhelper.isDelayComplete(20f)) {
                pos = AnimationUtil.moveUD(pos, posF, 0.2f, 0.1f);
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                int i = sr.getScaleFactor();
                Framebuffer framebuffer = new Framebuffer(sr.getScaledWidth() * i, sr.getScaledHeight() * i, true);
                framebuffer.bindFramebuffer(false);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0D, sr.getScaledWidth(), sr.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0F, 0.0F, -2000.0F);
                GlStateManager.disableLighting();
                GlStateManager.disableFog();
                GlStateManager.resetColor();
                Gui.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(255, 255, 255).getRGB());
                float i1 = sr.getScaledWidth() / 2f;
                float i2 = sr.getScaledHeight() / 2f;
                RenderUtil.drawRoundedRect(i1 - 60, i2 + 60, i1 + 60, i2 + 64, 1, new Color(222, 222, 222).getRGB());
                RenderUtil.drawRoundedRect(i1 - 60, i2 + 60, i1 - 60 + pos, i2 + 64, 1, new Color(61, 155, 239).getRGB());
                RenderUtil.drawImage(new ResourceLocation("Client/new/loading/logo.png"), sr.getScaledWidth() / 2f - 128 / 4f, sr.getScaledHeight() / 2f - 128 / 2, 128 / 2f, 128 / 2f);
                GlStateManager.disableLighting();
                GlStateManager.disableFog();
                framebuffer.unbindFramebuffer();
                framebuffer.framebufferRender(sr.getScaledWidth() * i, sr.getScaledHeight() * i);
                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(516, 0.1F);
                Minecraft.getMinecraft().updateDisplay();
                timerhelper.reset();
            }
        }
    }

    /**
     * @author SuperSkidder
     */
    @Overwrite
    private void sendClickBlockToController(boolean leftClick) {
        if (!leftClick) {
            this.leftClickCounter = 0;
        }

        if (this.leftClickCounter <= 0) {
            if (leftClick && Minecraft.getMinecraft().objectMouseOver != null && Minecraft
                    .getMinecraft().objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos blockpos = Minecraft.getMinecraft().objectMouseOver.getBlockPos();

                if (Minecraft.getMinecraft().theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air
                        && Minecraft.getMinecraft().playerController.onPlayerDamageBlock(blockpos,
                        Minecraft.getMinecraft().objectMouseOver.sideHit)) {
                    Minecraft.getMinecraft().effectRenderer.addBlockHitEffects(blockpos,
                            Minecraft.getMinecraft().objectMouseOver.sideHit);
                    Minecraft.getMinecraft().thePlayer.swingItem();
                }
            } else {
                Minecraft.getMinecraft().playerController.resetBlockRemoving();
            }
        }
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    public Timer getTimer() {
        return timer;
    }

    @Override
    public LanguageManager getLanguageManager() {
        return mcLanguageManager;
    }

    @Override
    public void setRightClickDelayTimer(int i) {
        rightClickDelayTimer = i;
    }
}