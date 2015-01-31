/**
 * *****************************************************************************
 * FlickrSebScene.java
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
package advanced.flickrMT;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.components.visibleComponents.widgets.progressBar.MTProgressBar;
import org.mt4j.input.IMTEventListener;
import org.mt4j.input.MTEvent;
import org.mt4j.input.gestureAction.DefaultLassoAction;
import org.mt4j.input.gestureAction.DefaultPanAction;
import org.mt4j.input.gestureAction.DefaultZoomAction;
import org.mt4j.input.gestureAction.InertiaDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.lassoProcessor.LassoProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.camera.MTCamera;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.ToolsMath;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

import com.aetrion.flickr.photos.SearchParameters;

import java.util.*;
import org.mt4j.components.css.util.CSSKeywords;
import org.mt4j.components.visibleComponents.widgets.progressBar.IprogressInfoProvider;
import processing.core.PApplet;

public class FlickrSebScene extends AbstractScene
{

    private AbstractMTApplication app;
    private MTProgressBar progressBar;

    private MTComponent pictureLayer;
    private MTSvgButton flickrButton;
    private LassoProcessor lassoProcessor;

    private SebImagesProvider imageProvider;

    public FlickrSebScene(AbstractMTApplication engine, String name)
    {
        super(engine, name);
        this.app = engine;

        // SEB : init my list of images..
        imageProvider = new SebImagesProvider(engine);

        //Set a zoom limit
        final MTCamera camManager = new MTCamera(engine);
        this.setSceneCam(camManager);
        this.getSceneCam().setZoomMinDistance(80);

        // this.setClearColor(new MTColor(135, 206, 250, 255));
        this.setClearColor(new MTColor(70, 70, 72, 255));

        //Show touches
        this.registerGlobalInputProcessor(new CursorTracer(engine, this));

        //Add multitouch gestures to the canvas background
        lassoProcessor = new LassoProcessor(app, this.getCanvas(), this.getSceneCam());
        this.getCanvas().registerInputProcessor(lassoProcessor);
        this.getCanvas().addGestureListener(LassoProcessor.class, new DefaultLassoAction(app, this.getCanvas().getClusterManager(), this.getCanvas()));

        this.getCanvas().registerInputProcessor(new PanProcessorTwoFingers(app));
        this.getCanvas().addGestureListener(PanProcessorTwoFingers.class, new DefaultPanAction());

        this.getCanvas().registerInputProcessor(new ZoomProcessor(app));
        this.getCanvas().addGestureListener(ZoomProcessor.class, new DefaultZoomAction());

        pictureLayer = new MTComponent(app);

        MTComponent topLayer = new MTComponent(app, "top layer group", new MTCamera(app));

        //Load from classpath
        PImage keyboardImg = app.loadImage("advanced" + AbstractMTApplication.separator + "flickrMT" + AbstractMTApplication.separator + "data" + AbstractMTApplication.separator
                + "keyb128.png");

        final MTImageButton keyboardButton = new MTImageButton(app, keyboardImg);
        keyboardButton.setFillColor(new MTColor(255, 255, 255, 200));
        keyboardButton.setName("KeyboardButton");
        keyboardButton.setNoStroke(true);
        keyboardButton.translateGlobal(new Vector3D(-2, app.height - keyboardButton.getWidthXY(TransformSpace.GLOBAL) + 2, 0));
        topLayer.addChild(keyboardButton);

//		progressBar = new MTProgressBar(app, app.loadFont(MT4jSettings.getInstance().getDefaultFontPath()+ "Ziggurat.vlw"));
        progressBar = new MTProgressBar(app, app.createFont("arial", 18));

        progressBar.setDepthBufferDisabled(true);
        progressBar.setVisible(true);
        progressBar.setProgressInfoProvider(imageProvider);
        ArrayList<MTImage> images = imageProvider.getImages();

        topLayer.addChild(progressBar);

        flickrButton = new MTSvgButton(app,
                "advanced" + AbstractMTApplication.separator
                + "flickrMT" + AbstractMTApplication.separator
                + "data" + AbstractMTApplication.separator
                + "Flickr_Logo.svg");

        flickrButton.scale(0.4f, 0.4f, 1, new Vector3D(0, 0, 0), TransformSpace.LOCAL);
        flickrButton.translate(new Vector3D(0, 15, 0));
        flickrButton.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
        topLayer.addChild(flickrButton);

        keyboardButton.addGestureListener(TapProcessor.class, new IGestureEventListener()
        {
            @Override
            public boolean processGestureEvent(MTGestureEvent ge)
            {
                TapEvent te = (TapEvent) ge;
                switch (te.getTapID())
                {
                    case TapEvent.TAPPED:
                        //Flickr Keyboard
                        /*
                         MTKeyboard keyb = new MTKeyboard(app);
                         keyb.setFillColor(new MTColor(30, 30, 30, 210));
                         keyb.setStrokeColor(new MTColor(0, 0, 0, 255));

                         final MTTextArea t = new MTTextArea(app, FontManager.getInstance().createFont(app, "arial.ttf", 50, MTColor.BLACK));
                         t.setExpandDirection(ExpandDirection.UP);
                         t.setStrokeColor(new MTColor(0, 0, 0, 255));
                         t.setFillColor(new MTColor(205, 200, 177, 255));
                         t.unregisterAllInputProcessors();
                         t.setEnableCaret(true);
                         //	t.snapToKeyboard(keyb);
                         keyb.snapToKeyboard(t);
                         keyb.addTextInputListener(t);
                         */
                        //Flickr Button for the keyboard
//                        MTSvgButton flickrButton = new MTSvgButton(app, 
//                                                                   "advanced" + AbstractMTApplication.separator + 
//                                                                   "flickrMT" + AbstractMTApplication.separator + 
//                                                                   "data" + AbstractMTApplication.separator     + 
//                                                                   "Flickr_Logo.svg");
//                        
//                        flickrButton.scale(0.4f, 0.4f, 1, new Vector3D(0, 0, 0), TransformSpace.LOCAL);
//                        flickrButton.translate(new Vector3D(0, 15, 0));
//                        flickrButton.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);

                        //Add actionlistener to flickr button
                        progressBar.setVisible(true);
                        progressBar.setProgressInfoProvider(imageProvider);
                        
                        progressBar.setProgressInfoProvider(imageProvider);
                        flickrButton.addGestureListener(TapProcessor.class, new IGestureEventListener()
                        {

                            /**
                             * Event Gesture processor anonymous class.
                             *
                             * @param ge a MTGestureEvent
                             * @return
                             */
                            @Override
                            public boolean processGestureEvent(MTGestureEvent ge)
                            {
                                TapEvent te = (TapEvent) ge;
                                switch (te.getTapID())
                                {
                                    case TapEvent.TAPPED:

                                        //for (MTImage card : imageProvider.getImagesURL())
                                        progressBar.setVisible(true);
                                        progressBar.setProgressInfoProvider(imageProvider);

                                        for (MTImage card : imageProvider.getImages())
                                        {

                                            card.setUseDirectGL(true);
                                            card.setDisplayCloseButton(true);
                                            card.setPositionGlobal(new Vector3D(ToolsMath.getRandom(10, MT4jSettings.getInstance().getWindowWidth() - 100), ToolsMath.getRandom(10, MT4jSettings.getInstance().getWindowHeight() - 50), 0));
                                            card.scale(0.6f, 0.6f, 0.6f, card.getCenterPointLocal(), TransformSpace.LOCAL);
                                            card.addGestureListener(DragProcessor.class, new InertiaDragAction());
                                            lassoProcessor.addLassoable(card); //make fotos lasso-able
                                            pictureLayer.addChild(card);
                                        }

                                        break;
                                    default:
                                        break;
                                }

                                return false;
                            }

                        });

                        // keyb.addChild(flickrButton);
//			        getCanvas().addChild(0, keyb);
                        getCanvas().addChild(flickrButton);
                        //keyb.setPositionGlobal(new Vector3D(app.width / 2f, app.height / 2f, 0));
                        break;
                    default:
                        break;
                }
                return true;
            }

        });

        this.getCanvas().addChild(pictureLayer);
        this.getCanvas().addChild(topLayer);
    }

    public void onEnter()
    {
        getMTApplication().registerKeyEvent(this);
    }

    public void onLeave()
    {
        getMTApplication().unregisterKeyEvent(this);
    }

    /**
     *
     * @param e
     */
    public void keyEvent(KeyEvent e)
    {
        int evtID = e.getID();
        if (evtID != KeyEvent.KEY_PRESSED)
        {
            return;
        }
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_BACK_SPACE:
                app.popScene();
                break;
            case KeyEvent.VK_F1:
                this.setClearColor(new MTColor(100, 99, 99, 255));
                break;
            case KeyEvent.VK_F2:
                this.setClearColor(new MTColor(120, 119, 119, 255));
                break;
            case KeyEvent.VK_F3:
                this.setClearColor(new MTColor(130, 129, 129, 255));
                break;
            case KeyEvent.VK_F4:
                this.setClearColor(new MTColor(160, 159, 159, 255));
                break;
            case KeyEvent.VK_F5:
                this.setClearColor(new MTColor(180, 179, 179, 255));
                break;
            case KeyEvent.VK_F6:
                this.setClearColor(new MTColor(100, 100, 102, 255));
                break;
            case KeyEvent.VK_F7:
                this.setClearColor(new MTColor(70, 70, 72, 255));
                break;
            case KeyEvent.VK_F:
                System.out.println("FPS: " + app.frameRate);
                break;
            default:
                break;
        }
    }

}
