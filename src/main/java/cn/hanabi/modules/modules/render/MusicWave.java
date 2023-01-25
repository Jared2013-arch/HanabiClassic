package cn.hanabi.modules.modules.render;

import cn.hanabi.events.EventRender;
import cn.hanabi.events.EventRender2D;
import cn.hanabi.modules.Category;
import cn.hanabi.modules.Mod;
import cn.hanabi.utils.math.TimeHelper;
import cn.hanabi.utils.render.RenderUtil;
import com.darkmagician6.eventapi.EventTarget;

import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class MusicWave extends Mod {


    public MusicWave() {
        super("MusicWave", Category.RENDER);
    }

    TimeHelper timeHelper = new TimeHelper();

    @EventTarget
    public void onRender(EventRender2D e) {

        if (timeHelper.isDelayComplete(100)) {
            float heightRate = 1;
            if (waveformGraph.deque.size() > 1) {
                Iterator<Short> iter = waveformGraph.deque.iterator();
                Short p1 = iter.next();
                Short p2 = iter.next();
                int x1 = 0, x2 = 0;
                while (iter.hasNext()) {
                    RenderUtil.drawRect(x1, (int) (p1 * heightRate), x2, (int) (p2 * heightRate), 0x55FFFFFF);

                    p1 = p2;
                    p2 = iter.next();
                    x1 = x2;
                    x2 += 1;
                }
            }
            timeHelper.reset();

        }
    }

    WaveformGraph waveformGraph;

    @Override
    protected void onEnable() {
        super.onEnable();
        //load music
        waveformGraph = new WaveformGraph();
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("D:\\test.wav"));
            SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(audioInputStream.getFormat());
            sourceDataLine.open();
            sourceDataLine.start();
            byte[] buf = new byte[4];
            int len;
            while ((len = audioInputStream.read(buf)) != -1) {
                if (audioInputStream.getFormat().getChannels() == 2) {
                    if (audioInputStream.getFormat().getSampleRate() == 16) {
                        waveformGraph.put((short) ((buf[1] << 8) | buf[0]));
                        waveformGraph.put((short) ((buf[3] << 8) | buf[2]));
                    } else {
                        waveformGraph.put(buf[1]);//左声道
                        waveformGraph.put(buf[3]);//左声道
                        System.out.println(buf);
                    }
                } else {
                    if (audioInputStream.getFormat().getSampleRate() == 16) {
                        waveformGraph.put((short) ((buf[1] << 8) | buf[0]));
                        waveformGraph.put((short) ((buf[3] << 8) | buf[2]));
                    } else {
                        waveformGraph.put(buf[1]);
                        waveformGraph.put(buf[2]);
                        waveformGraph.put(buf[3]);
                        waveformGraph.put(buf[4]);
                    }
                }
            }


        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }
}

class WaveformGraph {

    public LinkedList<Short> deque = new LinkedList<Short>();
    private Timer timer;

    public WaveformGraph() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                synchronized (deque) {
                    float heightRate = 1;
                    if (deque.size() > 1) {
                        Iterator<Short> iter = deque.iterator();
                        Short p1 = iter.next();
                        Short p2 = iter.next();
                        int x1 = 0, x2 = 0;
                        while (iter.hasNext()) {
                            p1 = p2;
                            p2 = iter.next();
                            x1 = x2;
                            x2 += 1;
                        }
                    }
                }
            }
        }, 100, 100);
    }

    public void put(short v) {
        synchronized (deque) {
            deque.add(v);
            if (deque.size() > 500) {
                deque.removeFirst();
            }
        }
    }

    public void clear() {
        deque.clear();
    }
}