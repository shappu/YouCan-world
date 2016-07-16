/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdesktop.wonderland.modules.proximitytest.client;

import com.illposed.osc.OSCPacket;
import com.illposed.osc.OSCPortOut;
import java.io.IOException;
import java.net.InetAddress;

/**
 *
 * @author swapnil
 */
public class ssd {
      OSCPortOut oscPort;

    
      /**
     *
     * @param host
     * @param port
     */
    public ssd(String host, int port) {
        try {
            this.oscPort = new OSCPortOut(InetAddress.getByName(host), port);
            System.out.println("Sending OSC messages to: " + host + ":" + port);
        }
        catch (Exception e) {
            System.out.println("Couldn't open OSC port");
            System.exit(-1);
        }
    }

    public void sendOSC(OSCPacket packet) {
        try {
            oscPort.send(packet);
        }
        catch (IOException e) {
            System.out.println("Exception: " + e.toString());
        }
    }

    
}
