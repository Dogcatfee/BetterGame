/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.AppStates;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.Listener;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.Console;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.text.JTextComponent;
import mygame.KeyBindings;
//import mygame.KeyBindings;
//import mygame.SettingsInputHandler;
/**
 *
 * @author Matt
 */
public class StartScreenAppState extends AbstractAppState implements ScreenController{
    
    private SimpleApplication app;
    private Camera cam;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    private ViewPort guiViewPort;
    private AudioRenderer audioRenderer;
    private AppStateManager stateManager;
    private boolean flagSoundEnabled = true;
    private Listener listener;
    private KeyBindings keyBindings;
    
    private Node sceneNode;
    private Nifty nifty;
    
    private Screen screenCompletion;
    private Element imageSoundIconStart;
    private ImageRenderer imageRendererStart;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        this.app = (SimpleApplication) app;
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.guiViewPort = this.app.getGuiViewPort();
        this.audioRenderer = this.app.getAudioRenderer();
        this.app.getFlyByCamera().setEnabled(false);
        this.listener = this.app.getListener();
        keyBindings = new KeyBindings();
        
        initNifty();
        
        screenCompletion = nifty.getScreen("completion");
        imageSoundIconStart = screenCompletion.findElementByName("animationSource");
        imageRendererStart = imageSoundIconStart.getRenderer(ImageRenderer.class);
        
    }
    private boolean updateWinningScreen = false;
    private int counter = 1;
    private boolean countDirUp = true;
    private long updateTime = 0;
    @Override
    public void update(float tpf){
        //UPDATES THE INTERFACE
        if(updateWinningScreen){
            if(updateTime < System.currentTimeMillis()){
                if(countDirUp == true){
                    if(counter < 10){
                        counter++;
                    }else {
                        countDirUp = false;
                    }
                }
                if(countDirUp == false){
                    if(counter>1){
                        counter--;
                    }else{
                        countDirUp = true;
                    }
                }
                //UPDATES THE IMAGE INFO
                imageRendererStart.setImage(nifty.createImage("Interface/Nifty/resources/win/win"+counter+".png", true));
                updateTime = System.currentTimeMillis() + 50;
            }
        }
        
    }
    private NiftyJmeDisplay niftyDisplay;
    private void initNifty(){
        niftyDisplay = new NiftyJmeDisplay(assetManager,    
                inputManager,
                audioRenderer,
                guiViewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/Nifty/startScreen.xml", "start",this);
        
        //NIFTY TO DISPLAY
        guiViewPort.addProcessor(niftyDisplay);
                
        
    }
    public void menuStartGame(){
        GamePlayAppState gamePlayAppState = new GamePlayAppState();
        stateManager.attach(gamePlayAppState);
    }
    public void menuSettings(){
        
    }
    public void menuGameComplete(){
        
    }
    public void menuQuitGame(){
        app.stop();
    }
    private SettingsInputHandler sih;
    public void settingsKeyHandler(String eventId){
        
    }
    
    public void keyCallBack(KeyInputEvent evt, String eventId) throws Exception{

    }
    
    public void mapNiftyBindings(String eventId, KeyBindings keyBindings int keyCode){
    

}
    public void setStartScreen(){
        
    }
    public void setLoadingScreen(){
        
    }
    public void setLoadedScreen(){
        
    }
    public void setInGameGUI(){
        
    }
    public void updateGameGUIWrenches(int currentWrenches, int maxWrenches){
        
    }
    public void updateGameGUILevel(int level){
        
    }
    public void guiToggleSound(){
        
    }
    public void updateWinningScreen(){
        updateWinningScreen = false;
    }
    public KeyBindings getKeyBindings(){
        
    }
    public void changeKeyBindings(){
        
    }
    public void methodToBeCalledWhenEffectStarted(){
        
    }
    public void bind(Nifty nifty, Screen screen){
        
    }
    public void onStartScreen(){
        
    }
    public void onEndScreen(){
        
    }
    
}