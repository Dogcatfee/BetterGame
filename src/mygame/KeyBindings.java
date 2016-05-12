/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.input.KeyInput;
import java.lang.reflect.Field;

/**
 *
 * @author Matt
 */
public class KeyBindings {
    
    public int CHARACTER_FORWARD = KeyInput.KEY_UP;
    public int CHARACTER_BACKWARD = KeyInput.KEY_DOWN;
    public int CHARACTER_LEFT = KeyInput.KEY_LEFT;
    public int CHARACTER_RIGHT = KeyInput.KEY_RIGHT;
    
    public int CHARACTER_JUMP = KeyInput.KEY_1;
    public int CHARACTER_SPRINT = KeyInput.KEY_2;
    
    
    public int EXIT_APPLICATION = KeyInput.KEY_ESCAPE;
    
    public static String getKeyName(int keyCode){
        Class keyClass = KeyInput.class;
        for(Field field : keyClass.getFields()){
            try{
                if(keyCode == field.getInt(null)){
                    return field.getName();
                }
                
            } catch (Exception ex){
                
            }
        }
        
        
        
        
        return null;
    }
}
