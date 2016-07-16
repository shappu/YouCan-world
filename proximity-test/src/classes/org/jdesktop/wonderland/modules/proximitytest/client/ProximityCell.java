/**
 * Project Wonderland
 *
 * Copyright (c) 2004-2009, Sun Microsystems, Inc., All Rights Reserved
 *
 * Redistributions in source code form must reproduce the above
 * copyright and this condition.
 *
 * The contents of this file are subject to the GNU General Public
 * License, Version 2 (the "License"); you may not use this file
 * except in compliance with the License. A copy of the License is
 * available at http://www.opensource.org/licenses/gpl-license.php.
 *
 * Sun designates this particular file as subject to the "Classpath" 
 * exception as provided by Sun in the License file that accompanied 
 * this code.
 */
package org.jdesktop.wonderland.modules.proximitytest.client;

import com.illposed.osc.OSCMessage;
import com.jme.bounding.BoundingVolume;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import iwork.eheap2.Event;
import iwork.eheap2.EventHeap;
import iwork.eheap2.EventHeapException;
import iwork.eheap2.FieldValueTypes;
import java.io.IOException;
//import java.awt.Event;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.jdesktop.wonderland.client.ClientContext;
import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.cell.Cell.RendererType;
import org.jdesktop.wonderland.client.cell.CellCache;
import org.jdesktop.wonderland.client.cell.CellComponent;
import org.jdesktop.wonderland.client.cell.CellManager;
import org.jdesktop.wonderland.client.cell.CellRenderer;
import org.jdesktop.wonderland.client.cell.ChannelComponent;
import org.jdesktop.wonderland.client.cell.ChannelComponent.ComponentMessageReceiver;
import org.jdesktop.wonderland.client.cell.MovableComponent;
import org.jdesktop.wonderland.client.cell.ProximityComponent;
import org.jdesktop.wonderland.client.cell.ProximityListener;
import org.jdesktop.wonderland.client.cell.annotation.UsesCellComponent;
import org.jdesktop.wonderland.client.comms.WonderlandSession;
import org.jdesktop.wonderland.client.hud.CompassLayout;
import org.jdesktop.wonderland.client.hud.HUD;
import org.jdesktop.wonderland.client.hud.HUDComponent;
import org.jdesktop.wonderland.client.hud.HUDManagerFactory;
import org.jdesktop.wonderland.client.jme.ClientContextJME;
import org.jdesktop.wonderland.client.jme.ViewManager;
import org.jdesktop.wonderland.common.cell.CellID;
import org.jdesktop.wonderland.common.cell.CellStatus;
import org.jdesktop.wonderland.common.cell.CellTransform;
import org.jdesktop.wonderland.common.cell.messages.CellMessage;
import org.jdesktop.wonderland.common.cell.state.CellClientState;
import org.jdesktop.wonderland.modules.proximitytest.common.ProximityCellClientState;
import org.jdesktop.wonderland.modules.proximitytest.common.ProximityEnterExitMessage;
//import org.jdesktop.wonderland.server.cell.MovableComponentMO;

/**
 * Client-side cell for proximity test
 * @author Jonathan Kaplan <kaplanj@dev.java.net>
 */
public class ProximityCell extends Cell implements ComponentMessageReceiver {
    Quaternion q = new Quaternion();
    Vector3f up = new Vector3f(-46.91f, -3.45f, 71.28f);
    private Logger logger = Logger.getLogger(ProximityCell.class.getName());
    private HUDComponent hm = null;
    private static WonderlandSession ses;
    private ImageIcon i = new ImageIcon("C:/Users/swapnil/inWorldClue");
   
   

    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(
            "org/jdesktop/wonderland/modules/proximitytest/client/resources/Bundle");

    /* The type of shape: BOX or SPHERE */
    private ProximityCellRenderer cellRenderer = null;

    @UsesCellComponent
    private ProximityComponent prox;
    //String cellID = "41"; //change this to the cellID of the door 
    boolean doorMove = false;
    private  int flag = 0;
	
   // private String type;

    @UsesCellComponent
    private ChannelComponent channel;

    private List<BoundingVolume> clientBounds;
    private List<BoundingVolume> serverBounds;
    

    private String[] us = new String[100];

    private ClientProximityListener proxListener;
    
    ViewManager viewmanager = ViewManager.getViewManager();
    Vector3f pos;
    public String type = "starting";
    public ProximityCell(CellID cellID, CellCache cellCache) {
        super(cellID, cellCache);
        
    }

    
 
    /**
     * Called when the cell is initially created and any time there is a major
     * configuration change. The cell will already be attached to it's parent
     * before the initial call of this method
     * 
     * @param clientState
     */
    @Override
    public void setClientState(CellClientState clientState) {
        super.setClientState(clientState);
        
        clientBounds = ((ProximityCellClientState) clientState).getClientBounds();
        serverBounds = ((ProximityCellClientState) clientState).getServerBounds();
        
        updateListeners();
        
        if (cellRenderer != null) {
            cellRenderer.updateRenderer(clientBounds, serverBounds);
        }
    }

    @Override
    protected CellRenderer createCellRenderer(RendererType rendererType) {
        if (rendererType == RendererType.RENDERER_JME) {
            cellRenderer = new ProximityCellRenderer(this);
            cellRenderer.updateRenderer(clientBounds, serverBounds);
            return cellRenderer;
        }
        return super.createCellRenderer(rendererType);
    }

    @Override
    protected void setStatus(CellStatus status, boolean increasing) {
        super.setStatus(status, increasing);
        switch (status) {
            case ACTIVE:
                if (increasing) {
                    updateListeners();

                    channel.addMessageReceiver(ProximityEnterExitMessage.class, this);
                } else {
                    channel.removeMessageReceiver(ProximityEnterExitMessage.class);
                }
                break;
            case DISK:
                // TODO cleanup
                break;
        }

    }

    private void updateListeners() {
        if (proxListener != null) {
            prox.removeProximityListener(proxListener);
            proxListener = null;
        }

        if (prox != null && clientBounds != null && clientBounds.size() > 0) {
            proxListener = new ClientProximityListener();
            prox.addProximityListener(proxListener,
                                      clientBounds.toArray(new BoundingVolume[0]));
        }
    }
    
    /*public void setType(String t){
        
        type = t;
    }*/

    public void messageReceived(CellMessage message) {
        try {
            int counter = 0;
             EventHeap eh = new EventHeap("reilly-student1.research.cs.dal.ca",4535);
            String nm = viewmanager.getPrimaryViewCell().getName();
           logger.warning("The name is ***********"+nm+"*******");
             String ip = "127.0.0.1";
            ProximityEnterExitMessage m = (ProximityEnterExitMessage) message;
            String u = m.getname();
            int id = 0;
            logger.warning("****"+u+"********");
            
            CellID c = m.getCellID();
            String cellName = m.getCellName();
            logger.warning("The name of the cell is"+cellName+"*******");
            
            String name = m.getname();
            Boolean b = proximityClientPlugin.Desion.isSelected();
            logger.warning("The value of desion is"+b+"****** ");
            logger.warning("The user"+u+ "enters the cell");
            //proximityClientPlugin p = new proximityClientPlugin();
            pos = viewmanager.getPrimaryViewCell().getWorldTransform().getTranslation(pos);
             Event [] ev = new Event[1];
            ProximityCellProperties pcp = new ProximityCellProperties();
                Event e = new Event("User Enteredconsultancy");
            e.addField ("name", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
            e.addField ("typeR", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
            
            Event f = new Event("User Enteredcommon");
            f.addField ("name", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
            f.addField ("type", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
             //f.addField ("type", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
             
             Event kl = new Event("movepdf");
            kl.addField ("cellid1", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
            kl.addField ("cellid2", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
            kl.addField ("cellid3", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
            kl.addField ("cellid4", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
            kl.addField ("name", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
            
            
           
            float x,y,z;
            x= pos.x;
            y= pos.y;
            z= pos.z;
            logger.warning("**********"+x+"**********"+y+"*************"+z);
            if("consultancy".equalsIgnoreCase(cellName)){
                type = "consultancy";
                
                logger.warning("The type is consultancy");                
               
                if(b == true){
                    
                    
                 iwork.eheap2.Event fir = new iwork.eheap2.Event("User Enteredconsultancy");
                 fir.addField("name",u);
                 fir.addField("typeR",type);
                 ev[0] = fir;
                 eh.putEvent(ev[0]);
                  logger.warning("****************Event is posted***************************");
              /*      
                 ssd s = new ssd(ip, 5103);
                logger.warning("The object of OSC has been created");
                Object args[] = new Object[1];
                args[0] = new Integer(1);
                args[0] = 0;
                OSCMessage osm = new OSCMessage("/osctest/alive", args);
              
                    s.sendOSC(osm);
                    logger.warning("The OSCmessage has been send");
                    
                  */  
                }
            }
            else if("commonalities".equalsIgnoreCase(cellName)) {

         
                type = "commonalities";
                
                logger.warning("The type is normal");
                if(b == true){
                  
                    
                 iwork.eheap2.Event fir = new iwork.eheap2.Event("User Enteredcommon");
                 fir.addField("name",u);
                 fir.addField("type",type);
                 ev[0] = fir;
                 eh.putEvent(ev[0]);
                  logger.warning("****************Event is posted***************************");   
                    
             /*       
                 try {
                      ssd s = new ssd(ip, 5103);
                
                logger.warning("The object of OSC has been created");
                Object args[] = new Object[1];
                args[0] = new Integer(1);
                args[0] = 1;
                OSCMessage osm = new OSCMessage("/osctest/alive", args);
              
                    s.sendOSC(osm);
                    logger.warning("The OSCmessage has been send");
                     JFrame f = new JFrame("Code Entry");
                    codeEntry ctrlPanel2 = new codeEntry("Code Entry", f, this);
                    
                    f.getContentPane().add(ctrlPanel2);
                        //logger.severe("Control panel 2 added: " + ctrlPanel2);
                    f.pack();
                    f.setVisible(true);
                    
                    logger.warning("After the window");
                    
                    Vector3f moved = new Vector3f(802.17f,-14.20f, 579.49f); //change this to the position you want the door to be in exactly
        
             Cell cell = ClientContextJME.getViewManager().getPrimaryViewCell().getCellCache().getCell(new CellID(2));
     
                        CellTransform transform1 = cell.getWorldTransform();
                        transform1.getRotation(q);
                        CellTransform transform = new CellTransform(q, moved, .04f);       
      
                        
                        logger.warning("The first");
                        //logger.severe("transform" + transform);
                       // logger.severe("Current position: " +  cell.getWorldTransform());
                        if (cell == null) {
                            logger.severe("cell is null");
                        } else {
                            try{
                            MovableComponent mcc = cell.getComponent(MovableComponent.class); //move
                            mcc.localMoveRequest(transform);
                            }catch(Exception ep){
                                logger.severe("ERROR AT GET MCC: " + ep.getMessage());
                            }
        }
                        
                    f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    logger.warning("The panel is clossed");
                } catch (Exception ex) {
                    //Logger.getLogger(GlDebugClientPlugin.class.getName()).log(Level.WA);

                }
               */       
                }
          
            }
            else if("reading".equalsIgnoreCase(cellName)){
                
                
               
                type = "reading";
                logger.warning("The type is reading");
                
                flag++;
                logger.warning("********************"+flag);
                
          if(flag == 4){
              
              String temp1 = "1";
                 String temp2 = "21", temp3 = "2", temp4 = "6";
                 
              
              Event mp = new Event("movepdf");
                 mp.addField("cellid1",temp1);
                 mp.addField("cellid2",temp2);
                 mp.addField("cellid3",temp3);
                 mp.addField("cellid4",temp4);
                 mp.addField("name","Steve");
                 eh.putEvent(mp);
                 logger.warning("The movepdf event is posted"); 
              
          }  
          
          else if(flag == 10)
          {
              
               if(b == true){
                   
                   
                if("Steve".equalsIgnoreCase(u)){       
      
                 logger.warning("Inside the case 1");
                 Vector3f moved = new Vector3f(-43.91f, 3.55f, 81.83f); //change this to the position you want the door to be in exactly
                 Vector3f moved3 = new Vector3f(-47.91f, 3.55f, 84.84f);
                 Vector3f moved4 = new Vector3f(-48.97f, 3.55f, 79.67f);
                  CellID c1 = new CellID(1);
                  CellID c2 = new CellID(33);
                  CellID c3 = new CellID(36);
                 String temp1 = "1";
                 String temp2 = "21";
                 Event mp = new Event("movepdf");
                 mp.addField("cellid1",temp1);
                 mp.addField("cellid2",temp2);
                 mp.addField("name","Steve");
                 eh.putEvent(mp);
                 logger.warning("The movepdf event is posted");
                
                 /*
                  try {
                      //String sid = ""
                            ses = ClientContext.getSessionManager("reilly-student1.research.cs.dal.ca:8080").getPrimarySession();
                       } catch (IOException ex) {
                           Logger.getLogger(ProximityCell.class.getName()).log(Level.SEVERE, null, ex);
                       }
                  
                 //Cell cell1 = ClientContextJME.getViewManager().getPrimaryViewCell().getCellCache().getCell(new CellID(1));
                 //Cell cell3 = ClientContextJME.getViewManager().getPrimaryViewCell().getCellCache().getCell(new CellID(36));
                 //Cell cell4 = ClientContextJME.getViewManager().getPrimaryViewCell().getCellCache().getCell(new CellID(33));
                 //WonderlandSession psession = ServerSessionManager
                
                 Cell cell1 = ClientContext.getCellCache(ses).getCell(c1);
                 Cell cell3 = ClientContext.getCellCache(ses).getCell(c2);
                 Cell cell4 = ClientContext.getCellCache(ses).getCell(c3);
                        CellTransform transform1 = cell1.getWorldTransform();
                        CellTransform transform3 = cell3.getWorldTransform();
                        CellTransform transform4 = cell4.getWorldTransform();
                        //transform1.getRotation(q);
                        CellTransform transforma = new CellTransform(q, moved, 1.0f);
                        CellTransform transformb = new CellTransform(q, moved3, 1.0f);
                        CellTransform transformc = new CellTransform(q, moved4, 1.0f);
      
                        
                        logger.warning("The first");
                       
                        MovableComponent mcc1 = cell1.getComponent(MovableComponent.class);
                        if(mcc1== null){
                        //cell1.addComponent(mcc1);
                            logger.warning("movablecomponent is null");
                        }
                        logger.warning("*****************"+cell1.getStatus().toString());
                        CellStatus cs = null;
                        //mcc1.setClientState(CellComponent.class.setStatus(CellStatus.ACTIVE,true));
                        
                    
                        mcc1.localMoveRequest(transforma);
                        logger.warning("The cell1 is moved");
                        
                        MovableComponent mcc3 = cell3.getComponent(MovableComponent.class);
                         if(mcc3== null){
                        //cell3.addComponent(mcc3);
                             logger.warning("movablecomponent is null");
                         }
                         logger.warning("*****************"+cell3.getStatus().toString());
                        mcc3.localMoveRequest(transformb);
                        logger.warning("The cell2 is moved");
                        
                         MovableComponent mcc4 = cell4.getComponent(MovableComponent.class);;
                         if(mcc4== null){
                        //cell4.addComponent(mcc4);
                             logger.warning("movablecomponent is null");
                         }
                         logger.warning("*****************"+cell4.getStatus().toString());
                        mcc4.localMoveRequest(transformc);
                        logger.warning("The cell3 is moved");
                        //logger.severe("transform" + transform);
                       // logger.severe("Current position: " +  cell.getWorldTransform());
                       /* if (cell1 == null) {
                            logger.severe("cell is null");
                        } else {
                            try{
                            MovableComponent mcc1 = cell1.getComponent(MovableComponent.class); //move
                            
                            if(mcc1 == null){
                                
                                mcc1 = new MovableComponent(cell1);
                                cell1.addComponent(mcc1);
                            }
                            mcc1.localMoveRequest(transforma);
                            
                            }catch(Exception ep){
                                logger.severe("ERROR AT GET MCC: " + ep.getMessage());
                            }  
                   
                        }
                        if (cell3 == null) {
                            logger.severe("cell is null");
                        } else {
                            try{
                            MovableComponent mcc3 = cell3.getComponent(MovableComponent.class); //move
                            if(mcc3 == null){
                                
                                mcc3 = new MovableComponent(cell1);
                                cell1.addComponent(mcc3);
                            }
                            mcc3.localMoveRequest(transformb);
                            }catch(Exception ep){
                                logger.severe("ERROR AT GET MCC: " + ep.getMessage());
                            }  
                   
                        }
                        
                        if (cell4 == null) {
                            logger.severe("cell is null");
                        } else {
                            try{
                            MovableComponent mcc4 = cell4.getComponent(MovableComponent.class); //move
                            if(mcc4 == null){
                                
                                mcc4 = new MovableComponent(cell1);
                                cell1.addComponent(mcc4);
                            }
                            mcc4.localMoveRequest(transformc);
                            }catch(Exception ep){
                                logger.severe("ERROR AT GET MCC: " + ep.getMessage());
                            }  
                   
                        }
                        
                        logger.severe("The cell is moved");*/             
                }
                else if("Aziz".equalsIgnoreCase(u)){
        
                           logger.warning("Inside the case 2");
                           Vector3f moved2 = new Vector3f(-43.91f, 3.55f, 81.83f);
                           Vector3f moved5 = new Vector3f(-47.91f, 3.55f, 84.84f);
                           Vector3f moved6 = new Vector3f(-48.97f, 3.55f, 79.67f);
                           
                           String te1 = "20";
                           String te2 = "15";
                 Event mp1 = new Event("movepdf");
                 mp1.addField("cellid1",te1);
                 mp1.addField("cellid2",te2);
                 mp1.addField("name","Aziz");
                 eh.putEvent(mp1);
                 logger.warning("The movepdf event is posted");
                           
                          /* Cell cell2 = ClientContextJME.getViewManager().getPrimaryViewCell().getCellCache().getCell(new CellID(39));
             
             Cell cell5 = ClientContextJME.getViewManager().getPrimaryViewCell().getCellCache().getCell(new CellID(31));
             Cell cell6 = ClientContextJME.getViewManager().getPrimaryViewCell().getCellCache().getCell(new CellID(34));
             
                CellTransform transform2 = cell2.getWorldTransform();
                CellTransform transform5 = cell5.getWorldTransform();
                CellTransform transform6 = cell6.getWorldTransform();
                        transform2.getRotation(q);
                        transform5.getRotation(q);
                        transform6.getRotation(q);
                        CellTransform transformd = new CellTransform(q, moved2, 1.0f);       
                        CellTransform transforme = new CellTransform(q, moved5, 1.0f);
                        CellTransform transformf = new CellTransform(q, moved6, 1.0f);
                        
                         MovableComponent mcc1 = new MovableComponent(cell2);
                         if(mcc1== null){
                        cell2.addComponent(mcc1);
                         }
                        mcc1.localMoveRequest(transformd);
                        logger.warning("The cell1 is moved");
                        
                         MovableComponent mcc3 = new MovableComponent(cell5);
                         if(mcc3== null){
                        cell5.addComponent(mcc3);
                         }
                        mcc3.localMoveRequest(transforme);
                        logger.warning("The cell2 is moved");
                        
                         MovableComponent mcc4 = new MovableComponent(cell6);
                         if(mcc4== null){
                        cell6.addComponent(mcc4);
                         }
                        mcc4.localMoveRequest(transformf);
                        logger.warning("The cell3 is moved");  */          
                        
                      
                }
                else if("Martha".equalsIgnoreCase(u)){
      
                           logger.warning("Inside the case 3");
                           Vector3f moved7 = new Vector3f(-43.91f, 3.55f, 81.83f);
                           Vector3f moved8 = new Vector3f(-47.91f, 3.55f, 84.84f);
                           Vector3f moved9 = new Vector3f(-48.97f, 3.55f, 79.67f);
                           
                          String tr1 = "34";
                          String tr2 = "36";
                 Event mp2 = new Event("movepdf");
                 mp2.addField("cellid1",tr1);
                  mp2.addField("cellid2",tr2);
                  mp2.addField("name","Martha");
                 eh.putEvent(mp2);
                 logger.warning("The movepdf event is posted"); 
                           
                        /*   Cell cell7 = ClientContextJME.getViewManager().getPrimaryViewCell().getCellCache().getCell(new CellID(1));
             
             Cell cell8 = ClientContextJME.getViewManager().getPrimaryViewCell().getCellCache().getCell(new CellID(36));
             Cell cell9 = ClientContextJME.getViewManager().getPrimaryViewCell().getCellCache().getCell(new CellID(34));
             
                CellTransform transform7 = cell7.getWorldTransform();
                CellTransform transform8 = cell8.getWorldTransform();
                CellTransform transform9 = cell9.getWorldTransform();
                        transform7.getRotation(q);
                        transform8.getRotation(q);
                        transform9.getRotation(q);
                        CellTransform transformg = new CellTransform(q, moved7, 1.0f);
                        CellTransform transformh = new CellTransform(q, moved7, 1.0f);
                        CellTransform transformi = new CellTransform(q, moved7, 1.0f);
      
                        
                        MovableComponent mcc1 = new MovableComponent(cell7);
                        if(mcc1== null){
                        cell7.addComponent(mcc1);
                        }
                        mcc1.localMoveRequest(transformg);
                        logger.warning("The cell1 is moved");
                        
                         MovableComponent mcc3 = new MovableComponent(cell8);
                         if(mcc3== null){
                        cell8.addComponent(mcc3);
                         }
                        mcc3.localMoveRequest(transformh);
                        logger.warning("The cell2 is moved");
                        
                         MovableComponent mcc4 = new MovableComponent(cell9);
                         if(mcc4== null){
                        cell9.addComponent(mcc4);
                         }
                        mcc4.localMoveRequest(transformi);
                        logger.warning("The cell3 is moved");*/
               /* ssd s = new ssd(ip, 5103);
                
                logger.warning("The object of OSC has been created");
                Object args[] = new Object[1];
                args[0] = new Integer(1);
                args[0] = 2;
                OSCMessage osm = new OSCMessage("/osctest/alive", args);
              
                    s.sendOSC(osm);
                    logger.warning("The OSCmessage has been send");
                     Cell cell = ClientContextJME.getViewManager().getPrimaryViewCell().getCellCache().getCell(new CellID(2));
                       logger.warning(cell.getName());
                       //logger.warning(cell.getID);
                        CellTransform transform1 = cell.getWorldTransform();
                        Quaternion q = new Quaternion();
                        transform1.getRotation(q);
                        CellTransform transform = new CellTransform(q, up, 0.04f);
                        logger.severe("Current position: " +  cell.getWorldTransform());
                        
                        if (cell == null) {
                            logger.severe("cell is null");
                        } else {
                            MovableComponent mcc = cell.getComponent(MovableComponent.class); //move
                            mcc.localMoveRequest(transform);
                        }
                    hm = createHUDComponent();*/
               //}
                }
                
                else if("Mathew".equalsIgnoreCase(u)) {
                    
                    logger.warning("Inside the case 36");
                           Vector3f moved7 = new Vector3f(-43.91f, 3.55f, 81.83f);
                           Vector3f moved8 = new Vector3f(-47.91f, 3.55f, 84.84f);
                           Vector3f moved9 = new Vector3f(-48.97f, 3.55f, 79.67f);
                           
                          String ti1 = "2";
                          String ti2 = "6";
                 Event mp3 = new Event("movepdf");
                 mp3.addField("cellid1",ti1);
                 mp3.addField("cellid2",ti2);
                 mp3.addField("name","Mathew");
                 eh.putEvent(mp3);
                 logger.warning("The movepdf event is posted"); 
                    
                }
            }
           
           
            /*int i = 0;
            us[i] = u;
            i++;
            logger.warning("*************** User"+u+"Entered****************");
            try {
                eh.putEvent(ev[0]);
                logger.warning("****************Event is posted***************************");
            } catch (EventHeapException ex) {
                Logger.getLogger(ProximityCell.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //CellTransform currentAvatarTransform =  ClientContextJME.getCellCache(this.getSessionManager().getPrimarySession()).getViewCell().getWorldTransform();
            cellRenderer.setSolid(true, m.getIndex(), !m.isEnter());
        } catch (EventHeapException ex) {
            Logger.getLogger(ProximityCell.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        catch (EventHeapException ex) {
            Logger.getLogger(ProximityCell.class.getName()).log(Level.SEVERE, null, ex);
        }
   */
          
            }
        }
        }
        catch (EventHeapException ex) {
            Logger.getLogger(ProximityCell.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
     
    }
        

        private class ClientProximityListener implements ProximityListener {

        public void viewEnterExit(boolean entered, Cell cell, CellID viewCellID,
                                  BoundingVolume proximityVolume, int proximityIndex)
        {
            String enter = entered?"Entered":"Exited";
            logger.log(Level.WARNING, "[ClientProximityListener] " + enter +
                       " cell " + cell.getCellID() + " volume " + proximityVolume);

            cellRenderer.setSolid(false, proximityIndex, !entered);
        }
    }
        
      
    
    private HUDComponent createHUDComponent()  {
         // HUDComponent hd = null;
        logger.warning("CREATING guestures HUD");
        
         // BufferedImage bi = ImageIO.read("");
        
                        
                          
        
        HUD mainHUD = HUDManagerFactory.getHUDManager().getHUD("main");
        
        
         hm = mainHUD.createImageComponent(i);
         hm.setName("Guestures");
         hm.setDecoratable(true);
         hm.setSize(70,70);
         hm.setVisible(true);
         
       
        hm.setPreferredLocation(CompassLayout.Layout.NORTH);
       
         //JPanel panelForHUD = new JPanel();
      logger.warning("The HUD is inserted");
        mainHUD.addComponent(hm);
        
        logger.log(Level.INFO, "*****************Inside the createHud******************** ");
        return hm;
       }
    

}
