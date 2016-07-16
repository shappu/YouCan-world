/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdesktop.wonderland.modules.proximitytest.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import org.jdesktop.wonderland.client.BaseClientPlugin;
import org.jdesktop.wonderland.client.ClientPlugin;
import org.jdesktop.wonderland.client.jme.JmeClientMain;
import org.jdesktop.wonderland.client.login.ServerSessionManager;
import org.jdesktop.wonderland.common.annotation.Plugin;

/**
 *
 * @author swapnil
 */
@Plugin
public class proximityClientPlugin extends BaseClientPlugin{
    
    public static JMenuItem Desion = null;
    private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(proximityClientPlugin.class.getName());
    
    public void initialize(ServerSessionManager LoginInfo)
     {
       
       //s = LoginInfo.getPrimarySession();
       //myname = LoginInfo.getUsername();
       //logger.warning("****************The session is "+s+"*****************");
     Desion = new JCheckBoxMenuItem("Proximity Enabled");
     
     Desion.addActionListener(new ActionListener() {

             public void actionPerformed(ActionEvent e) {
                if (Desion.isSelected() == true) {
                    
                       
                             
                         
                        logger.warning("*****************Inside the actionperformed******************** "); 
                          //logger.warning("*****************"+myname+"******************** ");     
                   
                  
                  
                }
                
             }
         
     });
        super.initialize(LoginInfo);
     }
     
     // @Override
    public void activate() {
       JmeClientMain.getFrame().addToToolsMenu(Desion);
       //Des.setVisible(true);
        
    }
      
     
      
      
      
      
   
      
     // @Override
 protected void deactivate() {
      
          JmeClientMain.getFrame().removeFromToolsMenu(Desion);
      }

    public void cleanup() {
        super.cleanup();
    }
      
     
      
    
}
