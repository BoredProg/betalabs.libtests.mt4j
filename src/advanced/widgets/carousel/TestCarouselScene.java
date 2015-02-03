/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advanced.widgets.carousel;

import org.mt4j.MTApplication;

/**
 * TestCarouselScene
 * @author Sebastien
 */
public class TestCarouselScene extends MTApplication
{
    
    public static void main(String[] args)
    {
        initialize();
    }

    public void startUp()
    {
        addScene(new CarouselScene(this, "Carousel Scene"));
    }
}
