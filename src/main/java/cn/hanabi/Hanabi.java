package cn.hanabi;

import cn.hanabi.command.CommandManager;
import cn.hanabi.events.EventLoop;
import cn.hanabi.events.EventPacket;
import cn.hanabi.modules.ModManager;
import cn.hanabi.utils.client.ClientUtil;
import cn.hanabi.utils.client.DebugUtil;
import cn.hanabi.utils.client.SoundFxPlayer;
import cn.hanabi.utils.fileSystem.FileManager;
import cn.hanabi.utils.fontmanager.FontManager;
import cn.hanabi.utils.game.NukerUtil;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.utils.waypoints.WaypointManager;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import me.yarukon.DiscordThread;
import me.yarukon.Yarukon;
import me.yarukon.hud.window.HudWindowManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S07PacketRespawn;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Hanabi {
    @NotNull
    public static final String CLIENT_NAME = "Hanabi";
    @NotNull
    public static final String CLIENT_VERSION = "Classic";
    @NotNull
    public static final String CLIENT_INITIALS;


    public static Hanabi INSTANCE;

    static {
        List<Character> chars = new ArrayList<>();
        for (char c : CLIENT_NAME.toCharArray())
            if (Character.toUpperCase(c) == c)
                chars.add(c);

        char[] c = new char[chars.size()];

        for (int i = 0; i < chars.size(); i++) {
            c[i] = chars.get(i);
        }

        CLIENT_INITIALS = new String(c);
    }

    public final boolean windows = System.getProperties().getProperty("os.name").toLowerCase().contains("windows");


    public ArrayList<DebugUtil> debugUtils = new ArrayList<>();

    public ModManager moduleManager;
    public CommandManager commandManager;
    public FileManager fileManager;
    public FontManager fontManager;
    public TrayIcon trayIcon;
    public WaypointManager waypointManager;

    public String location;

    public boolean hypixelBypass = false;
    public HudWindowManager hudWindowMgr;
    public boolean customScoreboard = false;
    public boolean hasOptifine = false;
    public Field ofFastRenderField;

    //Crasher
    public Queue<Packet<?>> packetQueue;
    public String rank;
    public String username = System.getProperty("user.name");
    TimeHelper ms = new TimeHelper();
    public long timing;


    public Hanabi() {
        INSTANCE = this;
        EventManager.register(this);
        location = Locale.getDefault().getCountry();

        fileManager = new FileManager();
        commandManager = new CommandManager();
        moduleManager = new ModManager();

        new Yarukon();


        EventManager.register(new NukerUtil());

        ClientUtil.notifications.clear();

        moduleManager.addModules();
        hudWindowMgr = new HudWindowManager();
        commandManager.addCommands();

        waypointManager = new WaypointManager();

        fileManager.load();


        if (windows) {
            if (SystemTray.isSupported()) {
                try {
                    this.trayIcon = new TrayIcon(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/assets/minecraft/Client/logo128.png"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.trayIcon.setImageAutoSize(true);
                this.trayIcon.setToolTip("Hanabi Client");
                try {
                    SystemTray.getSystemTray().add(this.trayIcon);
                } catch (AWTException var7) {
                    this.log("Unable to add tray icon.");
                }
                this.trayIcon.displayMessage("HanabiClient", "Thank you for using Hanabi", TrayIcon.MessageType.NONE);
                Wrapper.notificationsAllowed(true);
            }
        }

        new SoundFxPlayer().playSound(SoundFxPlayer.SoundType.Startup, 0);

        try {
            this.ofFastRenderField = GameSettings.class.getDeclaredField("ofFastRender");
            hasOptifine = true;
        } catch (Exception ignored) {
        }

        //Crasher
        packetQueue = new ConcurrentLinkedQueue<>();
        ms.reset();
        timing = 100L;


        //Discord
        new DiscordThread().start();
    }

    public void log(String message) {
        String prefix = "[" + CLIENT_NAME + "] ";
        Hanabi.INSTANCE.println(prefix + message);
    }

    public boolean fastRenderDisabled(GameSettings gameSettingsIn) {
        try {
            return !((boolean) this.ofFastRenderField.get(gameSettingsIn));
        } catch (Exception ignored) {
        }
        return true;
    }

    public void stopClient() {
        try {
            if (windows) {
                if (SystemTray.isSupported()) {
                    Hanabi.INSTANCE.trayIcon.displayMessage("HanabiClient - Notification", "See you soon.", TrayIcon.MessageType.ERROR);
                }
            }
            fileManager.save();
        } catch (Exception e) {
            System.err.println("Failed to save settings:");
            e.printStackTrace();
        }
    }


    @EventTarget
    public void onTick(EventLoop e) {
        if (packetQueue.isEmpty())
            return;

        if (ms.isDelayComplete(timing)) {
            Wrapper.sendPacketNoEvent(packetQueue.poll());
            ms.reset();
        }
    }

    @EventTarget
    public void onWorldChange(EventPacket e) {
        if (e.getPacket() instanceof S07PacketRespawn || e.getPacket() instanceof S01PacketJoinGame) {
            packetQueue.clear();
            ms.reset();
        }

    }

    public void println(String obj) {
        Class<?> systemClass = null;
        try {
            systemClass = Class.forName("java.lang.System");
            Field outField = null;
            try {
                outField = systemClass.getDeclaredField("out");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            Class<?> printStreamClass = Objects.requireNonNull(outField).getType();
            Method printlnMethod = printStreamClass.getDeclaredMethod("println", String.class);
            Object object = outField.get(null);
            printlnMethod.invoke(object, obj);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
