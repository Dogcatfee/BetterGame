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
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.texture.Texture2D;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import mygame.BrickBuilder;
import mygame.WrenchControl;

/**
 *
 * @author Matt
 */
public class WorldAppState extends AbstractAppState {
    
    private SimpleApplication app;
    private Camera cam;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    private BulletAppState bulletAppState;
    private ViewPort viewPort;
    private GamePlayAppState gamePlayAppState;
    private AppStateManager stateManager;
    private StartScreenAppState startScreenAppState;
    private ExcavatorDrivableAppState excavatorDrivableAppState;
    private FilterPostProcessor filterPostProcessorLight;
    private FilterPostProcessor filterPostProcessorWater;
    private DirectionalLight sun;
    
    private RigidBodyControl scenePhy;
    private Node sceneNode;
    private LoadState gameStateLoading = LoadState.INITIALIZE;
    
    private enum LoadState{
        LOADING, LOADED, IDLE, INITIALIZE;
    }
    
    private int gameLevel = 1;
    private int gameMaxLevel = 2;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        this.app = (SimpleApplication) app;
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort = this.app.getViewPort();
        
        bulletAppState = stateManager.getState(BulletAppState.class);
        gamePlayAppState = stateManager.getState(GamePlayAppState.class);
        startScreenAppState = stateManager.getState(StartScreenAppState.class);
        
        filterPostProcessorLight = new FilterPostProcessor(assetManager);
        filterPostProcessorWater = new FilterPostProcessor(assetManager);
        
        initLight();
        initBloomFilter();
    }
    
    private long waitTime;
    private long currentTime;
    @Override
    public void update(float tpf){
        currentTime = System.currentTimeMillis()/1000;
        if(gameStateLoading == LoadState.INITIALIZE){
            if(gameLevel > gameMaxLevel){
                startScreenAppState.menuGameComplete();
                gamePlayAppState.endGame();
                return;
            }
            gameStateLoading = LoadState.LOADING;
            startScreenAppState.setLoadingScreen();
            waitTime = System.currentTimeMillis()/1000 +1;
        } else if(gameStateLoading == LoadState.LOADING){
            if(currentTime >= waitTime){
                if(gameLevel == 1){
                    levelUpWorldManager(gameLevel);
                }else if(gameLevel > 1){
                    levelUpWorldManager(gameLevel);
                }
                gameStateLoading = LoadState.LOADED;
            }
        }else if(gameStateLoading == LoadState.LOADED){
            startScreenAppState.setInGameGUI();
            gameStateLoading = LoadState.IDLE;
            
        }
    }
    
    public void levelUpWorldManager(int level){
        bulletAppState.getPhysicsSpace().removeAll(rootNode);
        if(sceneNode != null){
            rootNode.detachAllChildren();
        }
        if(excavatorDrivableAppState != null){
            stateManager.detach(excavatorDrivableAppState);
        }
        viewPort.removeProcessor(filterPostProcessorWater);
        viewPort.removeProcessor(filterPostProcessorLight);
        
        initShadow();
        
        sceneNode = worldServant(level);
        sceneNode.setName("WorldFullModel");
        rootNode.detachAllChildren();
        rootNode.attachChild(sceneNode);
        searchTraverseWorld();
        
        excavatorDrivableAppState = new ExcavatorDrivableAppState();
        stateManager.attach(excavatorDrivableAppState);
    }
    //RETURNS A NODE
    public Node worldServant(int level){
        switch(level){
            case 1:
                return (Node)assetManager.loadModel("Scenes/map1.j3o");
            case 2:
                return (Node)assetManager.loadModel("Scenes/mapCity.j3o");
            default:
                return (Node)assetManager.loadModel("Scenes/map1.j3o");
        }
    }
    
    public void levelUp(int level){
        gameLevel = level;
        gameStateLoading = LoadState.INITIALIZE;
    }
    private void searchTraverseWorld(){
        SceneGraphVisitor visitor = new SceneGraphVisitor(){
            public void visit(Spatial spatial){
                
                if(spatial.getName().equals("building-ogremesh")){
                    spatial.addControl(new RigidBodyControl(0f));
                    spatial.setName("World");
                    bulletAppState.getPhysicsSpace().add(spatial);
                }
                if(spatial.getName().equals("podA-ogremesh")){
                    spatial.setName("Spawn");
                    Node playerStart = new Node("playerStart");
                    playerStart.setLocalRotation(spatial.getWorldRotation());
                    playerStart.setLocalTranslation(spatial.getWorldTranslation());
                    gamePlayAppState.setPlayerStart(playerStart);
                    spatial.setCullHint(Spatial.CullHint.Always);
                }
                if(spatial.getName().equals("podB-ogremesh")){
                    spatial.setName("Goal");
                    spatial.addControl(new RigidBodyControl(0f));
                    spatial.getControl(RigidBodyControl.class).setKinematic(true);
                    bulletAppState.getPhysicsSpace().add(spatial);
                }
                if(spatial.getName().equals("block-ogremesh") || spatial.getName().equals("blockCircle-ogremesh") ||
                        spatial.getName().equals("blockCircleQuart-ogremesh")){
                    spatial.setName("World");
                    spatial.addControl(new RigidBodyControl(0f));
                    bulletAppState.getPhysicsSpace().add(spatial);
                    spatial.setCullHint(Spatial.CullHint.Always);
                }
                if(spatial.getName().equals("wrench-ogremesh")){
                    spatial.addControl(new WrenchControl(assetManager, bulletAppState, gamePlayAppState));
                    spatial.addControl(new RigidBodyControl(0f));
                    spatial.getControl(RigidBodyControl.class).setKinematic(true);
                    spatial.setName("Target");
                    gamePlayAppState.increaseMapMaxWrenches();
                    bulletAppState.getPhysicsSpace().add(spatial);
                    
                }
                
                if(spatial.getName().equals("water-ogremesh")){
                    spatial.setName("Water");
                    initWaterFromWorld(spatial);
                    spatial.setCullHint(Spatial.CullHint.Always);
                }
            
            }
        };
        
        //WHAT?
        rootNode.depthFirstTraversal(visitor);
    }
    private void initBloomFilter(){
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        bloom.setBloomIntensity(2);
        bloom.setExposurePower(1);
        bloom.setBlurScale(5f);
        fpp.addFilter(bloom);
        viewPort.addProcessor(fpp);
    }
    public void initLight(){
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(.3f));
        rootNode.addLight(al);
        
        sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f,-2.5f,-1f).normalizeLocal()));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
    
    }
    public void initShadow(){
        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, 1024,2);
        dlsf.setLight(sun);
        filterPostProcessorLight.addFilter(dlsf);
        viewPort.addProcessor(filterPostProcessorLight);
    }
    
    public void initWaterFromWorld(Spatial spatial){
        WaterFilter water = new WaterFilter(rootNode,sun.getDirection());
        Vector3f extent = ((BoundingBox) spatial.getWorldBound()).getExtent(new Vector3f());
        water.setWaterHeight(spatial.getWorldTranslation().getY() + extent.getY() + extent.getY());
        water.setMaxAmplitude(0.3f);
        water.setSpeed(0.5f);
        
        filterPostProcessorWater.addFilter(water);
        viewPort.addProcessor(filterPostProcessorWater);
    }
    
    @Override
    public void stateDetached(AppStateManager stateManager){
        bulletAppState.getPhysicsSpace().removeAll(rootNode);
        rootNode.detachAllChildren();
        stateManager.detach(gamePlayAppState);
        stateManager.detach(excavatorDrivableAppState);
        stateManager.detach(bulletAppState);
        filterPostProcessorLight.removeAllFilters();
        viewPort.removeProcessor(filterPostProcessorLight);
        rootNode.removeLight(sun);
        System.out.println("Detatching worldAppState");
    }    
    
}
