/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.AppStates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.bullet.BulletAppState;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.Font;
/**
 *
 * @author Matt
 */
public class GamePlayAppState extends AbstractAppState implements ScreenController{
    private SimpleApplication app;
    private Camera cam;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    private AppStateManager stateManager;
    private ViewPort guiViewPort;
    private AudioRenderer audioRenderer;
    private BulletAppState bulletAppState;
    private ViewPort viewPort;
    private Node guiNode;
    private BitmapFont guiFont;
    private WorldAppState worldAppState;
    private StartScreenAppState startScreenAppState;

    private Node playerStart;
    private int score = 0;
    private int mapMaxWrenches = 0;
    private int currentWrenches = 0;
    private int level = 1;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort = this.app.getViewPort();
        this.guiNode = this.app.getGuiNode();
        this.guiViewPort = this.app.getViewPort();
        this.audioRenderer = this.app.getAudioRenderer();
        this.stateManager = this.app.getStateManager();
        
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        
        startScreenAppState = stateManager.getState(StartScreenAppState.class);
        stateManager.attach(worldAppState);
        
        JmeCursor jc = (JmeCursor) assetManager.loadAsset("Interface/Nifty/resources/cursorHand.cur");
        inputManager.setCursorVisible(true);
        inputManager.setMouseCursor(jc);
        
    }
    @Override
    public void update(float tpf){
        startScreenAppState.updateGameGUIWrenches(getWrenchCatchCount(), getMapMaxWrenches());
        startScreenAppState.updateGameGUILevel(getGameLevel());
    }
    
    public void setPlayerStart(Node playerStart){
        this.playerStart = playerStart;
    }
    public Node getPlayerStart(){
        return playerStart;
    }
    public Vector3f getPlayerStartPos(){
        return playerStart.getLocalTranslation();
    }
    public Quaternion getPlayerStartRot(){
        return playerStart.getLocalRotation();
    }
    public void setMapMaxWrenches(int mapMaxWrenches){
        this.mapMaxWrenches = mapMaxWrenches;
    }
    public void increaseMapMaxWrenches(){
        mapMaxWrenches++;
    }
    public int getMapMaxWrenches(){
        return mapMaxWrenches;
    }
    public void catchWrench(){
        currentWrenches++;
        if(getWrenchCatchCount() == getMapMaxWrenches()){
            currentWrenches = 0;
            mapMaxWrenches = 0;
            level++;
            worldAppState.levelUp(level);
        }
    }
    public void endGame(){
        stateManager.detach(worldAppState);
    }
    public int getWrenchCatchCount(){
        return currentWrenches;
    }
    public int getGameLevel(){
        return level;
    }
    public void bind(Nifty nifty, Screen screen){
        
    }
    public void onStartScreen(){
        
    }
    public void onEndScreen(){
        
    }
    
    
}
