package advanced.simpleParticles;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.camera.MTCamera;

import processing.core.PImage;
import processing.core.PVector;

public class SimpleParticlesExample extends MTApplication {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		initialize();
	}

	@Override
	public void startUp() {
		if (MT4jSettings.getInstance().isOpenGlMode()){
			addScene(new ParticleTestScene(this, "Particles test"));			
		}else{
			System.err.println("Particle Scene can only be run in opengl mode!");
		}
	}
	
	

}
