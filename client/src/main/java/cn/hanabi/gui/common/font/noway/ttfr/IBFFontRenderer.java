package cn.hanabi.gui.common.font.noway.ttfr;

public interface IBFFontRenderer
{
    HStringCache getStringCache();

    void setStringCache(HStringCache value);

    boolean isDropShadowEnabled();

    void setDropShadowEnabled(boolean value);

    boolean isEnabled();

    void setEnabled(boolean value);
}
