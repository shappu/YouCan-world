/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jdesktop.wonderland.modules.proximitytest.client;

import org.jdesktop.wonderland.client.cell.Cell;
import org.jdesktop.wonderland.client.cell.MovableComponent;
import org.jdesktop.wonderland.common.cell.CellStatus;
import org.jdesktop.wonderland.common.cell.CellTransform;

/**
 *
 * @author swapnil
 */
public class movepdf extends MovableComponent{
    
    public movepdf(Cell cell){
        super(cell);
        super.setStatus(CellStatus.ACTIVE, true);
    }
    
    
}
