package cn.hanabi.loader;

import aLph4anTi1eaK_cN.Annotation.Setup;
import cn.hanabi.loader.auth.Check;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class NiggaFunabi {

    public NiggaFunabi(){
        NiggaMoment();
    }

    static Check auth = new Check();

    @Setup
    public void NiggaMoment(){
        new Check();
        if (!auth.socketGet()){
            Println("你看你妈呢 sbsbsbsbsbsbsbsbsbsbsbsbsbbssbsbsbsbbsbsb!");
        } else {
            Println("哥们在这里给你说唱HAHAHHAHAHAHHAHAHHAHH");
            Println(("java.lang.System") + ("out.println")  + "[Power X] 正在Kill 看门狗!!!!!!!!!!!!!!!");
        }
    }


    protected void Println(String obj){
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
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
