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
import com.jme3.audio.AudioNode;
import com.jme3.audio.Listener;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.input.ChaseCamera;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Vector;
import mygame.CustomChaseCamera;
import mygame.KeyBindings;
import mygame.LeapMotionController;
import mygame.WrenchControl;
/**
 *
 * @author Matt
 */
public class ExcavatorDrivableAppState {
    private SimpleApplication app;
    private Camera cam;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    private BulletAppState bulletAppState;
    private GamePlayAppState gamePlayAppState;
    private StartScreenAppState startScreenAppState;
    private AppStateManager stateManager;
    private FlyByCamera flyCam;
    private Node nodePivot, nodeBody, nodeBelts, nodeArm1, nodeArm2, nodeArm3;
    private CustomChaseCamera chaseCam;
    private AudioNode excavatorEngineSoundPassive;
    private AudioNode excavatorEngineSoundRunning;
    private Listener listener;
    
     // LeapMotion
    private LeapMotionController lmc;
    private Controller controller;
    
    // Excavator
    private Vector3f walkDirection = new Vector3f(0,0,0);
    private Vector3f viewDirection = new Vector3f(0,1,0);
    private boolean rotateLeft = false, rotateRight = false, forward = false, backward = false;
    private float speed = 8;
    private ParticleEmitter dustEmitterLeft;
    private ParticleEmitter dustEmitterRight;
    private ParticleEmitter dustEmitterExhaust;
    
    // Camera
    private CameraNode camNode;
    
    // Game settings
    private Node playerStart;
}
