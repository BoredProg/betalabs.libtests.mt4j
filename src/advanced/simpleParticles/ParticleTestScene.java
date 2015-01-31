/**
 * *****************************************************************************
 * ParticleTestScene.java
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
package advanced.simpleParticles;

import org.mt4j.AbstractMTApplication;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.camera.MTCamera;
import processing.core.PImage;
import processing.core.PVector;

public class ParticleTestScene extends AbstractScene
{

    private String path = "advanced" + AbstractMTApplication.separator + "simpleParticles" + AbstractMTApplication.separator + "data" + AbstractMTApplication.separator;

    public ParticleTestScene(AbstractMTApplication mtApplication, String name)
    {
        super(mtApplication, name);

        final MTParticleSystem mtPs = new MTParticleSystem(getMTApplication(), 0, 0, mtApplication.width, mtApplication.height);
        mtPs.attachCamera(new MTCamera(getMTApplication()));
        mtPs.setPickable(false);
        getCanvas().addChild(mtPs);
        final PImage texture = getMTApplication().loadImage(path + "particle.png");

        getCanvas().addInputListener(new IMTInputEventListener()
        {
            public boolean processInputEvent(MTInputEvent inEvt)
            {
                if (inEvt instanceof AbstractCursorInputEvt)
                {
                    AbstractCursorInputEvt ce = (AbstractCursorInputEvt) inEvt;
                    mtPs.getParticleSystem().addParticle(new ImageParticle(getMTApplication(), new PVector(ce.getX(), ce.getY()), texture));
                }
                return false;
            }

        });
    }

    public void onEnter()
    {
        getMTApplication().frameRate(60);
    }

    public void onLeave()
    {
    }

}
