//Takes in a code as a string and checks if it's right. If it is, it moves an object
package org.jdesktop.wonderland.modules.proximitytest.client;

//module Stuff
import com.jme.math.Vector3f;
import java.text.DecimalFormat;
import java.util.logging.Logger;

//Math
import java.lang.Math;
import com.jme.math.Quaternion;
import com.jme.math.Matrix3f;


//logger
import java.util.logging.Logger;
import java.util.logging.*;
import java.util.logging.Level;

import com.jme.scene.CameraNode;
import org.jdesktop.mtgame.WorldManager;


import org.jdesktop.wonderland.client.jme.ViewManager;

import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.cell.CellManager;
import org.jdesktop.wonderland.client.cell.MovableComponent;
import org.jdesktop.wonderland.client.jme.ClientContextJME;
import org.jdesktop.wonderland.common.cell.CellID;
import org.jdesktop.wonderland.common.cell.CellTransform;

import org.jdesktop.mtgame.AWTInputComponent;
import org.jdesktop.mtgame.NewFrameCondition;
import org.jdesktop.mtgame.ProcessorArmingCollection;
import org.jdesktop.mtgame.WorldManager;
import org.jdesktop.wonderland.client.ClientContext;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.cell.MovableAvatarComponent;
import org.jdesktop.wonderland.client.cell.MovableComponent;
import org.jdesktop.wonderland.common.cell.CellTransform;

import com.jme.renderer.Camera;

import com.illposed.osc.OSCMessage;
import org.jdesktop.wonderland.client.cell.CellCache;

/**
 *
 * @author Mohamad Tawakol tawakool@gmail.com Dalhousie University
 */
public class codeCheck{

	Logger logger;
	String code = "";
	String answer = "\u138c\u0403\u0470\u1228"; //Change this to the final answer
	boolean correct = false;
	String cellID = "4"; //change this to the cellID of the door 
	

        
	public codeCheck(String code) { //create the instance of the class with the code entered, it will check it and move it if it's correct
                
                logger = Logger.getLogger("codeCheck");
		this.code = code;
                logger.severe("Code = " + code);
		correct = check(code);
		
		if (correct){
                    logger.severe("Code correct");
         
                } else {
                    logger.severe("Wrong code");
                }
	}

	public boolean check(String code){
		return code.equals(answer);
	}
}