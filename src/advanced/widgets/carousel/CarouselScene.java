/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package advanced.widgets.carousel;

import gov.pnnl.components.visibleComponents.widgets.carouselMenu.MTCarouselMenu;
import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.math.Vector3D;
import processing.core.PImage;

public class CarouselScene extends AbstractScene
{

    /***********************************************
    * Gets the path to icons.
    *
    * @return the path to icons
    ***********************************************/
    private String getPathToIcons()
    {
        // return System.getProperty("user.dir")+File.separator+"examples"+File.separator+"advanced"+File.separator+"mtShell"+ File.separator +"data"+ File.separator+"images"+File.separator; 
        //Load from classpath
        return   "advanced" + AbstractMTApplication.separator    + 
                 "widgets"  + AbstractMTApplication.separator    + 
                 "carousel" + AbstractMTApplication.separator    + 
                 "data"     + AbstractMTApplication.separator;
                                 
    }
    
    public CarouselScene(MTApplication mtapp, String name)
    {
        super(mtapp, name);
                
        MTCarouselMenu menu = new MTCarouselMenu(mtapp, new Vector3D(mtapp.width / 2, mtapp.height / 2), 10);
        menu.translateGlobal(new Vector3D(0,0,200));
        getCanvas().addChild(menu);
        
        MTRectangle[] carouselItems = menu.carouselItems;
        int i = 0;
        for (MTRectangle carouselItem : carouselItems)
        {
            PImage image = mtapp.loadImage(getPathToIcons() + "texture" + i++ + ".png");
            
            carouselItem.setTexture(image);
        }
    }
}
