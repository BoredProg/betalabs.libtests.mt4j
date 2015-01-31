/**
 * *****************************************************************************
 * photoviewer.java
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
package advanced.photoapp;

import java.util.ArrayList;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.shapes.PolygonDef;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.math.Vector3D;
import advanced.physics.physicsShapes.PhysicsRectangle;
import advanced.physics.util.PhysicsHelper;
import advanced.physics.util.UpdatePhysicsAction;
import org.mt4j.AbstractMTApplication;
import processing.core.PImage;

public class PhotoScene extends AbstractScene
{

    private float timeStep = 1.0f / 30.0f;
    private int constraintIterations = 10;
    private float scale = 20;
    private World world;
    

    private String imagePath = "advanced" + AbstractMTApplication.separator + "photoapp" + AbstractMTApplication.separator + "data" + AbstractMTApplication.separator;

    
    public PhotoScene(MTApplication mtapp)
    {
        super(mtapp, "PhotoScene");

        ArrayList<String> photoNames = new ArrayList<String>();
        photoNames.add("https://farm8.staticflickr.com/7328/11588412764_7b32e6fc2d_h.jpg");
        photoNames.add("https://farm6.staticflickr.com/5545/11085109845_37f584f226_h.jpg");
        photoNames.add("https://farm4.staticflickr.com/3808/10871092174_b67e1658e5_b.jpg");
        photoNames.add("https://farm8.staticflickr.com/7147/6756183891_9ba2e82617_b.jpg");
        photoNames.add("https://farm8.staticflickr.com/7506/15532505943_f4a3dcfc1e_h.jpg");
        photoNames.add("https://farm4.staticflickr.com/3725/8947008893_f73abc74f8_o.jpg");
        photoNames.add("https://farm4.staticflickr.com/3290/3010658431_b276586482_b.jpg");
        
        
        Vec2 gravity = new Vec2(0, 0);
        AABB worldAABB = new AABB();
        worldAABB.upperBound.set(100, 100);
        worldAABB.lowerBound.set(-100, -100);
        boolean doSleep = true;

         world = new World(worldAABB, gravity, doSleep);

        this.registerPreDrawAction(new UpdatePhysicsAction(world, timeStep, constraintIterations, scale));

        MTComponent physicsContainer = new MTComponent(mtapp);
        physicsContainer.scale(scale, scale, 1, Vector3D.ZERO_VECTOR);
        this.getCanvas().addChild(physicsContainer);

        for (String fileName : photoNames)
        {
            PImage photo = mtapp.loadImage(fileName);
            PhysicsPhoto physRect = new PhysicsPhoto(new Vector3D((float) Math.random() * mtapp.width, (float) Math.random() * mtapp.height), (int) (photo.width * 0.2), (int) (photo.height * 0.2), mtapp, world, 0.2f, 0.9f, 0.4f, scale);
            physRect.setTexture(photo);
            PhysicsHelper.addDragJoint(world, physRect, physRect.getBody().isDynamic(), scale);
            physicsContainer.addChild(physRect);
        }
    }

    private class PhysicsPhoto extends PhysicsRectangle
    {

        public PhysicsPhoto(Vector3D centerPoint, float width, float height,
                            MTApplication mtapp, World world, float density, float friction, float restitution, float worldScale)
        {
            super(centerPoint, width, height, mtapp, world, density, friction, restitution, worldScale);
        }

        @Override
        protected void polyDefB4CreationCallback(PolygonDef polyDef)
        {
            super.polyDefB4CreationCallback(polyDef);
            polyDef.filter.categoryBits = 0x0000;
            polyDef.filter.maskBits = 0x0000;
        }

        @Override
        protected void bodyDefB4CreationCallback(BodyDef def)
        {
            super.bodyDefB4CreationCallback(def);
            def.angularDamping = 0.5f;
            def.linearDamping = 0.5f;
        }

    }

}
