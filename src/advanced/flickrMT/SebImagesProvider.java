/*******************************************************************************
 *   SebImagesProvider.java
 * 
 * ® Sébastien Parodi (capturevision), 2015.
 *   http://capturevision.wordpress.com
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
 ******************************************************************************/


package advanced.flickrMT;

import org.mt4j.components.visibleComponents.widgets.progressBar.*;

import java.util.*;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import processing.core.PApplet;
import processing.core.PImage;

/**
 *
 * @author SEB__ALIEN
 */
public class SebImagesProvider extends AbstractProgressThread
{
    private  PApplet                    _engine = null;
    private final ArrayList<String>     _imagesURL;
    private ArrayList<MTImage>    _images;

    
    
    public SebImagesProvider(PApplet engine)
    {
        super(10);
        _engine     = engine;
        _imagesURL  = new ArrayList<String>();
        //_images     = new ArrayList<MTImage>();
        
        initImagesList();
        
        //loadImages();
    }
    
    private void initImagesList()
    {
        _imagesURL.add("https://farm8.staticflickr.com/7328/11588412764_7b32e6fc2d_h.jpg");
        _imagesURL.add("https://farm6.staticflickr.com/5545/11085109845_37f584f226_h.jpg");
        _imagesURL.add("https://farm4.staticflickr.com/3808/10871092174_b67e1658e5_b.jpg");
        _imagesURL.add("https://farm8.staticflickr.com/7147/6756183891_9ba2e82617_b.jpg");
        _imagesURL.add("https://farm8.staticflickr.com/7506/15532505943_f4a3dcfc1e_h.jpg");
        _imagesURL.add("https://farm4.staticflickr.com/3725/8947008893_f73abc74f8_o.jpg");
        _imagesURL.add("https://farm4.staticflickr.com/3290/3010658431_b276586482_b.jpg");
    }

    private void loadImages()
    {
        for (int i = 0; i < _imagesURL.size(); i++)
        {
            // IprogressInfoProvider variables..
            _percentageFinished = i / _imagesURL.size();
            _current            = i;
                        
            PImage      image   = _engine.loadImage(_imagesURL.get(i));
            MTImage     card    = new MTImage(_engine, image);
            
            _images.add(card);

            
        }
        _isFinished = true;
                                           
    }
            
    
    protected ArrayList<String> getImagesURL()
    {
        return _imagesURL;
    }
    
    public ArrayList<MTImage> getImages()
    {
        if (_images == null)
        {
            _images = new ArrayList<MTImage>();
            loadImages();
        }

        
        return _images;
    }
    
    
    private float _percentageFinished;
    private float _current;
    private Boolean _isFinished = false;
    

//    @Override
//    public float getPercentageFinished()
//    {
//        return _percentageFinished;
//    }
//
//    @Override
//    public float getCurrent()
//    {
//        return _current;
//    }
//
//
//    @Override
//    public boolean isFinished()
//    {
//        return _isFinished;
//    }
//
//    @Override
//    public String getCurrentAction()
//    {
//        return "Loading Images";
//    }
//
//    @Override
//    public float getTarget()
//    {
//        return 7f;
//    }

    @Override
    public void run()
    {
        getImages();
    }

           
}
