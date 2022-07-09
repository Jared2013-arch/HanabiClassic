
import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.*;


public class Index {
    public static Map additionalMods;
    static String inject;

    public static void premain(String agentArgs, Instrumentation inst) {
        inject = agentArgs;
        Class modListHelperClass;
        try {
            modListHelperClass = Class.forName("net.minecraftforge.fml.relauncher.ModListHelper");
        } catch (ClassNotFoundException var9) {
            System.out.println("未发现Minecraft Forge Mod Loader, 听通知操作");
            return;
        }

        Field additionalModsField = null;

        try {
            additionalModsField = modListHelperClass.getDeclaredField("additionalMods");
        } catch (NoSuchFieldException var8) {
            System.out.println("未发现Mod");
            return;
        }

        try {
            additionalMods = (Map) additionalModsField.get(modListHelperClass);
        } catch (IllegalAccessException var7) {
            System.out.println("非法行为！");
            return;
        }

        System.out.println("准备注入！");
        File injectingMod = new File(inject);
        additionalMods.put("Injected", injectingMod);
        System.out.println("已注入！");

    }

}


