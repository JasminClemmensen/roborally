package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * ....
 *
 * @author Lucia
 */

public class Gear extends FieldAction {

    private boolean turnsLeft;

    public Gear(boolean turnsLeft){
        this.turnsLeft = turnsLeft;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if(player != null){
            if(turnsLeft){
                player.setHeading(player.getHeading().prev());
            } else{
                player.setHeading(player.getHeading().next());
            }
            return true;
        }
        return false;
    }

    public boolean isTurnsLeft() {
        return turnsLeft;
    }
}


