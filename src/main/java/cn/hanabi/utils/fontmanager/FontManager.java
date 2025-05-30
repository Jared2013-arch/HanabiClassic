package cn.hanabi.utils.fontmanager;

import cn.hanabi.Hanabi;
import cn.hanabi.injection.interfaces.IMinecraft;
import cn.hanabi.utils.fontmanager.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
    public static String fontName = "";
    private final HashMap fonts = new HashMap();

    public UnicodeFontRenderer comfortaa10;
    public UnicodeFontRenderer comfortaa11;
    public UnicodeFontRenderer comfortaa12;
    public UnicodeFontRenderer comfortaa13;
    public UnicodeFontRenderer comfortaa15;
    public UnicodeFontRenderer comfortaa16;
    public UnicodeFontRenderer comfortaa17;
    public UnicodeFontRenderer comfortaa18;
    public UnicodeFontRenderer comfortaa20;
    public UnicodeFontRenderer comfortaa22;
    public UnicodeFontRenderer comfortaa25;
    public UnicodeFontRenderer comfortaa28;
    public UnicodeFontRenderer comfortaa30;
    public UnicodeFontRenderer comfortaa35;
    public UnicodeFontRenderer comfortaa40;
    public UnicodeFontRenderer comfortaa45;
    public UnicodeFontRenderer comfortaa50;
    public UnicodeFontRenderer comfortaa70;
    public UnicodeFontRenderer comfortaa150;

    public UnicodeFontRenderer raleway10;
    public UnicodeFontRenderer raleway11;
    public UnicodeFontRenderer raleway12;
    public UnicodeFontRenderer raleway13;
    public UnicodeFontRenderer raleway15;
    public UnicodeFontRenderer raleway16;
    public UnicodeFontRenderer raleway17;
    public UnicodeFontRenderer raleway18;
    public UnicodeFontRenderer raleway20;
    public UnicodeFontRenderer raleway25;
    public UnicodeFontRenderer raleway30;
    public UnicodeFontRenderer raleway35;
    public UnicodeFontRenderer raleway40;
    public UnicodeFontRenderer raleway45;
    public UnicodeFontRenderer raleway50;
    public UnicodeFontRenderer raleway70;

    public UnicodeFontRenderer usans10;
    public UnicodeFontRenderer usans11;
    public UnicodeFontRenderer usans12;
    public UnicodeFontRenderer usans13;
    public UnicodeFontRenderer usans14;
    public UnicodeFontRenderer usans15;
    public UnicodeFontRenderer usans16;
    public UnicodeFontRenderer usans17;
    public UnicodeFontRenderer usans18;
    public UnicodeFontRenderer usans19;
    public UnicodeFontRenderer usans20;
    public UnicodeFontRenderer usans21;
    public UnicodeFontRenderer usans22;
    public UnicodeFontRenderer usans23;
    public UnicodeFontRenderer usans24;
    public UnicodeFontRenderer usans25;
    public UnicodeFontRenderer usans28;
    public UnicodeFontRenderer usans30;
    public UnicodeFontRenderer usans35;
    public UnicodeFontRenderer usans40;
    public UnicodeFontRenderer usans45;
    public UnicodeFontRenderer usans50;
    public UnicodeFontRenderer usans70;
    public UnicodeFontRenderer usans150;

    public UnicodeFontRenderer icon10;
    public UnicodeFontRenderer icon11;
    public UnicodeFontRenderer icon12;
    public UnicodeFontRenderer icon13;
    public UnicodeFontRenderer icon14;
    public UnicodeFontRenderer icon15;
    public UnicodeFontRenderer icon16;
    public UnicodeFontRenderer icon17;
    public UnicodeFontRenderer icon18;
    public UnicodeFontRenderer icon19;
    public UnicodeFontRenderer icon20;
    public UnicodeFontRenderer icon21;
    public UnicodeFontRenderer icon22;
    public UnicodeFontRenderer icon23;
    public UnicodeFontRenderer icon24;
    public UnicodeFontRenderer icon25;
    public UnicodeFontRenderer icon30;
    public UnicodeFontRenderer icon35;
    public UnicodeFontRenderer icon40;
    public UnicodeFontRenderer icon45;
    public UnicodeFontRenderer icon50;
    public UnicodeFontRenderer icon70;
    public UnicodeFontRenderer icon100;
    public UnicodeFontRenderer icon130;


    public UnicodeFontRenderer sessionInfoIcon10;
    public UnicodeFontRenderer sessionInfoIcon11;
    public UnicodeFontRenderer sessionInfoIcon12;
    public UnicodeFontRenderer sessionInfoIcon13;
    public UnicodeFontRenderer sessionInfoIcon14;
    public UnicodeFontRenderer sessionInfoIcon15;
    public UnicodeFontRenderer sessionInfoIcon16;
    public UnicodeFontRenderer sessionInfoIcon17;
    public UnicodeFontRenderer sessionInfoIcon18;
    public UnicodeFontRenderer sessionInfoIcon19;
    public UnicodeFontRenderer sessionInfoIcon20;
    public UnicodeFontRenderer sessionInfoIcon21;
    public UnicodeFontRenderer sessionInfoIcon22;
    public UnicodeFontRenderer sessionInfoIcon23;
    public UnicodeFontRenderer sessionInfoIcon24;
    public UnicodeFontRenderer sessionInfoIcon25;
    public UnicodeFontRenderer sessionInfoIcon30;
    public UnicodeFontRenderer sessionInfoIcon35;



    public UnicodeFontRenderer micon15;
    public UnicodeFontRenderer micon30;

    public UnicodeFontRenderer wqy13;

    public UnicodeFontRenderer wqy16;
    public UnicodeFontRenderer wqy18;
    public UnicodeFontRenderer wqy25;

    public void initFonts() {

        comfortaa10 = this.getFont("comfortaa", 10.0F);
        comfortaa11 = this.getFont("comfortaa", 11.0F);
        comfortaa12 = this.getFont("comfortaa", 12.0F);
        comfortaa13 = this.getFont("comfortaa", 13.0F);
        comfortaa15 = this.getFont("comfortaa", 15.0F);
        comfortaa16 = this.getFont("comfortaa", 16.0F);
        comfortaa17 = this.getFont("comfortaa", 17.0F);
        comfortaa18 = this.getFont("comfortaa", 18.0F);
        comfortaa20 = this.getFont("comfortaa", 20.0F);
        comfortaa22 = this.getFont("comfortaa", 22.0F);
        comfortaa25 = this.getFont("comfortaa", 25.0F);
        comfortaa30 = this.getFont("comfortaa", 30.0F);
        comfortaa28 = this.getFont("comfortaa", 28.0F);
        comfortaa35 = this.getFont("comfortaa", 35.0F);
        comfortaa40 = this.getFont("comfortaa", 40.0F);
        comfortaa45 = this.getFont("comfortaa", 45.0F);
        comfortaa50 = this.getFont("comfortaa", 50.0F);
        comfortaa70 = this.getFont("comfortaa", 70.0F);
        comfortaa150 = this.getFont("comfortaa", 140.0F);

        raleway10 = this.getFont("raleway", 10.0F);
        raleway11 = this.getFont("raleway", 11.0F);
        raleway12 = this.getFont("raleway", 12.0F);
        raleway13 = this.getFont("raleway", 13.0F);
        raleway15 = this.getFont("raleway", 15.0F);
        raleway16 = this.getFont("raleway", 16.0F);
        raleway17 = this.getFont("raleway", 17.0F);
        raleway18 = this.getFont("raleway", 18.0F);
        raleway20 = this.getFont("raleway", 20.0F);
        raleway25 = this.getFont("raleway", 25.0F);
        raleway30 = this.getFont("raleway", 30.0F);
        raleway35 = this.getFont("raleway", 35.0F);
        raleway40 = this.getFont("raleway", 40.0F);
        raleway45 = this.getFont("raleway", 45.0F);
        raleway50 = this.getFont("raleway", 50.0F);
        raleway70 = this.getFont("raleway", 70.0F);


        usans10 = this.getFont("usans", 10.0F, true);
        usans11 = this.getFont("usans", 11.0F, true);
        usans12 = this.getFont("usans", 12.0F, true);
        usans13 = this.getFont("usans", 13.0F, true);
        usans14 = this.getFont("usans", 14.0F, true);
        usans15 = this.getFont("usans", 15.0F, true);
        usans16 = this.getFont("usans", 16.0F, true);
        usans17 = this.getFont("usans", 17.0F, true);
        usans18 = this.getFont("usans", 18.0F, false);
        usans19 = this.getFont("usans", 19.0F, true);
        usans20 = this.getFont("usans", 20.0F, true);
        usans21 = this.getFont("usans", 21.0F, true);
        usans22 = this.getFont("usans", 22.0F, true);
        usans23 = this.getFont("usans", 23.0F, true);
        usans24 = this.getFont("usans", 24.0F, true);
        usans25 = this.getFont("usans", 25.0F, true);
        usans28 = this.getFont("usans", 28.0F, true);
        usans30 = this.getFont("usans", 30.0F, true);
        usans35 = this.getFont("usans", 35.0F, true);
        usans40 = this.getFont("usans", 40.0F, true);
        usans45 = this.getFont("usans", 45.0F, true);
        usans50 = this.getFont("usans", 50.0F, true);
        usans70 = this.getFont("usans", 70.0F, true);
        usans150 = this.getFont("usans", 150.0F, true);




        this.icon10 = this.getFontWithCustomGlyph("icon", 10.0f, 59648, 59673);
        this.icon11 = this.getFontWithCustomGlyph("icon", 11.0f, 59648, 59673);
        this.icon12 = this.getFontWithCustomGlyph("icon", 12.0f, 59648, 59673);
        this.icon13 = this.getFontWithCustomGlyph("icon", 13.0f, 59648, 59673);
        this.icon14 = this.getFontWithCustomGlyph("icon", 14.0f, 59648, 59673);
        this.icon15 = this.getFontWithCustomGlyph("icon", 15.0f, 59648, 59673);
        this.icon16 = this.getFontWithCustomGlyph("icon", 16.0f, 59648, 59673);
        this.icon17 = this.getFontWithCustomGlyph("icon", 17.0f, 59648, 59673);
        this.icon18 = this.getFontWithCustomGlyph("icon", 18.0f, 59648, 59673);
        this.icon19 = this.getFontWithCustomGlyph("icon", 19.0f, 59648, 59673);
        this.icon20 = this.getFontWithCustomGlyph("icon", 20.0f, 59648, 59673);
        this.icon21 = this.getFontWithCustomGlyph("icon", 21.0f, 59648, 59673);
        this.icon22 = this.getFontWithCustomGlyph("icon", 22.0f, 59648, 59673);
        this.icon23 = this.getFontWithCustomGlyph("icon", 23.0f, 59648, 59673);
        this.icon24 = this.getFontWithCustomGlyph("icon", 24.0f, 59648, 59673);
        this.icon25 = this.getFontWithCustomGlyph("icon", 25.0f, 59648, 59673);
        this.icon30 = this.getFontWithCustomGlyph("icon", 30.0f, 59648, 59673);
        this.icon35 = this.getFontWithCustomGlyph("icon", 35.0f, 59648, 59673);
        this.icon40 = this.getFontWithCustomGlyph("icon", 40.0f, 59648, 59673);
        this.icon45 = this.getFontWithCustomGlyph("icon", 45.0f, 59648, 59673);
        this.icon50 = this.getFontWithCustomGlyph("icon", 50.0f, 59648, 59673);
        this.icon70 = this.getFontWithCustomGlyph("icon", 70.0f, 59648, 59673);
        this.icon100 = this.getFontWithCustomGlyph("icon", 100.0f, 59648, 59673);
        this.icon130 = this.getFontWithCustomGlyph("icon", 130.0f, 59648, 59673);

        sessionInfoIcon10 = this.getFont("sessioninfo",10.0f);
        sessionInfoIcon11 = this.getFont("sessioninfo",11.0f);
        sessionInfoIcon12 = this.getFont("sessioninfo",12.0f);
        sessionInfoIcon13 = this.getFont("sessioninfo",13.0f);
        sessionInfoIcon14 = this.getFont("sessioninfo",14.0f);
        sessionInfoIcon15 = this.getFont("sessioninfo",15.0f);
        sessionInfoIcon16 = this.getFont("sessioninfo",16.0f);
        sessionInfoIcon17 = this.getFont("sessioninfo",17.0f);
        sessionInfoIcon18 = this.getFont("sessioninfo",18.0f);
        sessionInfoIcon19 = this.getFont("sessioninfo",19.0f);
        sessionInfoIcon20 = this.getFont("sessioninfo",20.0f);
        sessionInfoIcon21 = this.getFont("sessioninfo",21.0f);
        sessionInfoIcon22 = this.getFont("sessioninfo",22.0f);
        sessionInfoIcon23 = this.getFont("sessioninfo",23.0f);
        sessionInfoIcon24 = this.getFont("sessioninfo",24.0f);
        sessionInfoIcon25 = this.getFont("sessioninfo",25.0f);
        sessionInfoIcon30 = this.getFont("sessioninfo",30.0f);
        sessionInfoIcon35 = this.getFont("sessioninfo",35.0f);


        micon15 = this.getFont("micon", 15f);
        micon30 = this.getFont("micon", 30f);


        wqy13 = this.getFontWithCJK("harmony", 13.0f);
        wqy16 = this.getFontWithCJK("harmony", 16.0f);
        wqy18 = this.getFontWithCJK("harmony", 18.0f);
        wqy25 = this.getFontWithCJK("harmony", 25.0f);
    }

    public UnicodeFontRenderer getFontWithCustomGlyph(String name, float size, int fontPageStart, int fontPageEnd) {

        fontName = name;

        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && ((HashMap) this.fonts.get(name)).containsKey(size)) {
                return (UnicodeFontRenderer) ((HashMap) this.fonts.get(name)).get(size);
            }
            InputStream inputStream = this.getClass()
                    .getResourceAsStream("/assets/minecraft/Client/fonts/" + name + ".ttf");
            Font font = null;
            font = Font.createFont(0, inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size), fontPageStart, fontPageEnd);
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(
                    ((IMinecraft) Minecraft.getMinecraft()).getLanguageManager().isCurrentLanguageBidirectional());
            HashMap map = new HashMap();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map) this.fonts.get(name));
            }
            map.put(size, unicodeFont);
            this.fonts.put(name, map);
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        return unicodeFont;
    }

    public UnicodeFontRenderer getFontWithCJK(String name, float size) {

        fontName = name;

        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && ((HashMap) this.fonts.get(name)).containsKey(size)) {
                return (UnicodeFontRenderer) ((HashMap) this.fonts.get(name)).get(size);
            }
            InputStream inputStream = this.getClass()
                    .getResourceAsStream("/assets/minecraft/Client/fonts/" + name + ".ttf");
            Font font = null;
            font = Font.createFont(0, inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size), true);
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(
                    ((IMinecraft) Minecraft.getMinecraft()).getLanguageManager().isCurrentLanguageBidirectional());
            HashMap map = new HashMap();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map) this.fonts.get(name));
            }
            map.put(size, unicodeFont);
            this.fonts.put(name, map);
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        return unicodeFont;
    }

    public UnicodeFontRenderer getFont(String name, float size, boolean bol) {

        fontName = name;

        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && ((HashMap) this.fonts.get(name)).containsKey(size)) {
                return (UnicodeFontRenderer) ((HashMap) this.fonts.get(name)).get(size);
            }
            InputStream inputStream = this.getClass()
                    .getResourceAsStream("/assets/minecraft/Client/fonts/" + name + ".otf");
            Font font = null;
            font = Font.createFont(0, inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(
                    ((IMinecraft) Minecraft.getMinecraft()).getLanguageManager().isCurrentLanguageBidirectional());
            HashMap map = new HashMap();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map) this.fonts.get(name));
            }
            map.put(size, unicodeFont);
            this.fonts.put(name, map);
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        return unicodeFont;
    }

    public UnicodeFontRenderer getFont(String name, float size) {

        fontName = name;

        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && ((HashMap) this.fonts.get(name)).containsKey(size)) {
                return (UnicodeFontRenderer) ((HashMap) this.fonts.get(name)).get(size);
            }
            InputStream inputStream = this.getClass()
                    .getResourceAsStream("/assets/minecraft/Client/fonts/" + name + ".ttf");
            Font font = null;
            font = Font.createFont(0, inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(
                    ((IMinecraft) Minecraft.getMinecraft()).getLanguageManager().isCurrentLanguageBidirectional());
            HashMap map = new HashMap();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map) this.fonts.get(name));
            }
            map.put(size, unicodeFont);
            this.fonts.put(name, map);
        } catch (Exception var7) {
            System.out.println("Font not found: " + name + " " + size);
        }
        return unicodeFont;
    }
}
