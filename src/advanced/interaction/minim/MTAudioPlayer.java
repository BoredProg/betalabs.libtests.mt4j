/**
 * *****************************************************************************
 * MTAudioPlayer.java
 *
 * ® Sébastien Parodi (capturevision), 2015.
 * http://capturevision.wordpress.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *****************************************************************************
 */
package advanced.interaction.minim;

import gov.pnnl.components.visibleComponents.shapes.MTArchedRectangle;
import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTSlider;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;
import processing.core.PGraphics;

/**
 *
 * @author SEB__ALIEN
 */
public class MTAudioPlayer extends MTRectangle
{

    private static final int WIDTH = 400;
    private static final int HEIGHT = 200;

    private String audioPath = "advanced" + AbstractMTApplication.separator
            + "interaction" + AbstractMTApplication.separator
            + "minim" + AbstractMTApplication.separator
            + "data" + AbstractMTApplication.separator
            + "audio" + AbstractMTApplication.separator;

    private AudioTrack audioTrack;
    private MTEllipse playButton;
    private MTTextArea infoPanel;
    private MTSlider slider;
    private boolean sliderUpdating = false;
    

    /**
     * Constructor
     * @param mtapp : the PApplet derived MTApplication instance.
     */
    public MTAudioPlayer(MTApplication mtapp)
    {
        super(mtapp, WIDTH, HEIGHT);

        infoPanel = new MTTextArea(mtapp, 0, 0, WIDTH, HEIGHT * 5 / 8);
        infoPanel.setFont(FontManager.getInstance().createFont(mtapp, "arial.ttf", 24, MTColor.GREEN, true));
        infoPanel.setFillColor(MTColor.BLACK);
        infoPanel.setPickable(false);
        
        
        
        audioTrack = new AudioTrack(audioPath + "thule.mp3", mtapp);
        
        
        
        slider = new MTSlider(0, HEIGHT*5/8, WIDTH, HEIGHT/8, 0, audioTrack.getLength(), mtapp);
        
        slider.getKnob().addGestureListener(DragProcessor.class, new IGestureEventListener()
        {
            public boolean processGestureEvent(MTGestureEvent ge)
            {
                DragEvent de = (DragEvent) ge;
                switch (de.getId())
                {
                    case MTGestureEvent.GESTURE_STARTED:
                        sliderUpdating = true;
                        break;
                    case MTGestureEvent.GESTURE_ENDED:
                        sliderUpdating = false;
                        audioTrack.cue((int) slider.getValue());
                        break;
                    default:
                        break;
                }
                return false;
            }

        });
        
        
        int playBtnRadius = HEIGHT / 8;
        playButton = new MTEllipse(mtapp, new Vector3D(WIDTH / 2, HEIGHT * 7 / 8), playBtnRadius, playBtnRadius);
        playButton.setFillColor(MTColor.GREEN);
        audioTrack.pause();
        sliderUpdating = false;
        playButton.unregisterAllInputProcessors();
        playButton.registerInputProcessor(new TapProcessor(mtapp));
        playButton.addGestureListener(TapProcessor.class, new IGestureEventListener()
        {
            @Override
            public boolean processGestureEvent(MTGestureEvent ge)
            {
                TapEvent te = (TapEvent) ge;
                if (te.getId() == MTGestureEvent.GESTURE_ENDED)
                {
                    if (!audioTrack.isPlaying())
                    {
                        audioTrack.play();
                        playButton.setFillColor(MTColor.RED);
                    }
                    else
                    {
                        audioTrack.pause();
                        playButton.setFillColor(MTColor.GREEN);
                    }

                }

                return false;
            }

        });   
        
        
        this.addChild(infoPanel);
        this.addChild(slider);
        this.addChild(playButton);

    }
    
    /**
     * Convert time from milliseconds to a string time format mm:ss
     *
     * @param sec time in milliseconds in integers
     * @return the formatted string
     */    
    private String getFormattedTime(int sec)
    {
        int min = sec / 60;
        sec %= 60;
        String time = "" + sec;
        if (sec < 10)
        {
            time = "0" + time;
        }
        time = min + ":" + time;
        if (min < 10)
        {
            time = "0" + time;
        }
        return time;
    }   
    
    
    @Override
    public void drawComponent(PGraphics g)
    {
        if (!sliderUpdating)
        {
            slider.setValue(audioTrack.getPosition());
        }
        infoPanel.setText("Track : " + audioTrack.getTitle() + "\n" + "Artist : " + audioTrack.getArtist() + "\n" + getFormattedTime(audioTrack.getPosition() / 1000) + " / " + getFormattedTime(audioTrack.getLength() / 1000));
        super.drawComponent(g);
    }    
    
    /**
     * destroy the component visually but also dispose of the audio track
     */
    @Override
    public void destroyComponent()
    {
        super.destroyComponent();
        audioTrack.close();
    }
    
    

}
