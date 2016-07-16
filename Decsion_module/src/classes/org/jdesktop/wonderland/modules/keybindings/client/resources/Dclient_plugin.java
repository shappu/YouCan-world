/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdesktop.wonderland.modules.keybindings.client.resources;


import edu.gatech.twinspace.core.virtual.client.AbstractEventHeapClientPlugin;
import iwork.eheap2.Event;
import iwork.eheap2.EventCallback;
import iwork.eheap2.EventHeap;
import iwork.eheap2.EventHeapException;
import iwork.eheap2.FieldValueTypes;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import org.jdesktop.wonderland.client.BaseClientPlugin;
import org.jdesktop.wonderland.client.ClientPlugin;
import org.jdesktop.wonderland.client.comms.WonderlandSession;
import org.jdesktop.wonderland.client.hud.CompassLayout;
import org.jdesktop.wonderland.client.hud.HUD;
import org.jdesktop.wonderland.client.hud.HUDComponent;
import org.jdesktop.wonderland.client.hud.HUDManagerFactory;
import org.jdesktop.wonderland.client.hud.HUDMessage;
import org.jdesktop.wonderland.client.jme.JmeClientMain;
import org.jdesktop.wonderland.client.login.ServerSessionManager;
import org.jdesktop.wonderland.client.login.SessionLifecycleListener;
import org.jdesktop.wonderland.common.annotation.Plugin;

/**
 *
 * @author swapnil
 */

@Plugin
public class Dclient_plugin  extends AbstractEventHeapClientPlugin implements ClientPlugin {
     private static final String host = "reilly-student1.research.cs.dal.ca";
     private static final int port = 4535;
     private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Dclient_plugin.class.getName());
     private JMenuItem Decisionsupport = null;
     private HUDComponent hudComponent = null;
     private HUDComponent hm = null;
     ArrayList<Event> templates ;
      MyJPanel p = new MyJPanel();
     
     public static String[] rs = new String[100];
    protected WonderlandSession psession;
    //getData gd = new getData(); 

    public Dclient_plugin () { super (host, port); }
     
     @Override
     public void initialize(ServerSessionManager LoginInfo)
     {
         
        /* LoginInfo.addLifecycleListener(new SessionLifecycleListener ()
         {

             public void sessionCreated(WonderlandSession session) {
                 throw new UnsupportedOperationException("Not supported yet.");
             }

             public void primarySession(WonderlandSession session) {
                 
                 try{
                  EventHeap eh = new EventHeap("reilly-student1.research.cs.dal.ca",4535);
                  //setEventHeap("reilly-student1.research.cs.dal.ca",8080);
                  Event e;
                  e = new Event("Patient Recos");
                  e.addField ("Recommendation", String[].class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
                  e.addField ("Patientname", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
                  Event [] ev = new Event[1];
                  ev[0] = e;
                 //templates.add(e);
                     eh.registerForEvents(ev,Dclient_plugin.this);
                 } catch (EventHeapException ex) {
                     Logger.getLogger(Dclient_plugin.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
             
         });*/
        
         Decisionsupport = new JCheckBoxMenuItem("Recommendation");
         Decisionsupport.addActionListener(new ActionListener(){

             public void actionPerformed(ActionEvent e) {
                  if (Decisionsupport.isSelected() == true) {
                   if (hudComponent == null) {  
                       logger.warning("*****************Inside the actionperformed******************** ");
                             hudComponent = createHUDComponent();
                          /*try {
                             buildTemplates();
                          } catch (EventHeapException ex) {
                              Logger.getLogger(Dclient_plugin.class.getName()).log(Level.SEVERE, null, ex);
                          }*/
                         
                              
                   }
                   hudComponent.setVisible(true);
                    //hm.setVisible(true);
                    //topMapEntity.setCameraEnabled(true);
                }
                else {
                    hudComponent.setVisible(false);
                   // topMapEntity.setCameraEnabled(false);
                }
             }
            
         });
         super.initialize(LoginInfo);
     }
     
      @Override
    public void activate() {
        // Add the HUD Debugger menu item
        JmeClientMain.getFrame().addToWindowMenu(Decisionsupport);
    }
      
      private HUDComponent createHUDComponent()  {
         // HUDComponent hd = null;
        logger.warning("CREATING Recommendation HUD");
        
        
        
                        
                          
        // Create the HUD Panel that displays the map.
        HUD mainHUD = HUDManagerFactory.getHUDManager().getHUD("main");
        
        
         hm = mainHUD.createComponent(p);
         hm.setName("Ask The Doctor");
         hm.setDecoratable(true);
         
       /* for(int gj=0; gj< gu.recos.length; gj++)
            
        {
        hm.setMessage(gu.recos[gj]);
        }*/
       // hm.setSize(200,200);
        hm.setPreferredLocation(CompassLayout.Layout.NORTHEAST);
       // hm.setVisible(true);
       /* for (int j=0; j<getuser.recos.length;j++)
        {
        mainHUD.createMessage(getuser.recos[j]);
                }*/
         JPanel panelForHUD = new JPanel();
       // hudComponent = mainHUD.createComponent();
       // hd = mainHUD.createComponent(panelForHUD);
       // hd.setName("Recommendation");
       // hd.setSize(200,200);
        //hd.setDecoratable(true);
        
        
        //hd.setPreferredLocation(CompassLayout.Layout.NORTHEAST);
        mainHUD.addComponent(hm);
        
        logger.warning("*****************Inside the createHud******************** ");
        return hm;
    }
      
      public void Display(String s)
      {
          p.Display(s);
      }
      
      
     public void buildTemplates() throws EventHeapException
      {
          templates = new ArrayList<Event>();
         // EventHeap eh = new EventHeap("reilly-student1.research.cs.dal.ca",4535);
          //setEventHeap("reilly-student1.research.cs.dal.ca",4535);
           Event e = new Event("Patient Recos");
        //e.addField ("RDF Class", "LoginEvent");
          e.addField ("Recommendation", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
           e.addField ("Patientname", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
          Event [] ev = new Event[2];
          ev[0] = e;
          Event f = new Event("User Enteredconsultancy");
        f.addField ("name", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
        f.addField ("typeR", String.class, FieldValueTypes.FORMAL, FieldValueTypes.FORMAL);
        ev[1] = f;
          templates.add(e);
          System.err.println("*************Events are added*******************");
          logger.warning("********************Added event to template*******************");
          //eh.registerForEvents(ev,this);
          
      }
      
      @Override
 protected void deactivate() {
 JmeClientMain.getFrame().removeFromWindowMenu(Decisionsupport);     
      }
      
      @Override
 public void cleanup() {
 if (hudComponent != null) {
            HUD mainHUD = HUDManagerFactory.getHUDManager().getHUD("main");
            mainHUD.removeComponent(hudComponent);
            mainHUD.removeComponent(hm);
 super.cleanup();
 }

}
   
   
   @Override
    public boolean returnEvent (Event[] events) {
         int i=0;
            //String n = new String();
           // hudComponent = createHUDComponent();
             //hudComponent.setVisible(true);
           
        try {
            logger.warning("**************Received Events*************");
            String type = events[0].getEventType();
            if(type.equalsIgnoreCase("User Enteredconsultancy")){
                logger.warning("**********************Events is received***********************");
            }
            String name = events[0].getPostValueString("Patientname");
           String n = events[0].getPostValueString("Recommendation");
            Display(n);
            logger.warning("Display in Dclient");
            }
        catch (EventHeapException ex) {
           System.err.println(ex.getMessage());
        }
            
            
            return true;
    }

   /* @Override
    public Event[] templates() {
       return (Event[]) templates.toArray (new Event [0]);
    }*/

    @Override
    public Event[] templates() {
        return (Event[]) templates.toArray (new Event [0]);
    }

    

   

    

   

    
} 


