package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        
        AppSettings settings = new AppSettings(true);
        
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode[] modes = device.getDisplayModes();
        
        int camIndex = 0;
        for(int j=0; j<(modes.length-1); j++){
            if(modes[j].getHeight() >= 576){
                camIndex = j;
                break;
            }
        }
        settings.setResolution(modes[camIndex].getWidth(), modes[camIndex].getHeight());
        settings.setFrequency(modes[camIndex].getRefreshRate());
        settings.setBitsPerPixel(modes[camIndex].getBitDepth());
        
        settings.setVSync(true);
        
        Main app = new Main();
        app.setSettings(settings);
        app.setShowSettings(true);
        
        app.start();
    }

    @Override
    public void simpleInitApp() {
        setDisplayFps(true);
        setDisplayStatView(true);
        
        StartScreenAppState startScreenAppState = 
                new StartScreenAppState();
        stateManager.attach(startScreenAppState);
        
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
