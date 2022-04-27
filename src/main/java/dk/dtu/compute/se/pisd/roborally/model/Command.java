/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Mohamad Anwar Meri,  s215713@dtu.dk
 * @author Jasmin Clemmensen
 * @version $Id: $Id
 */
public enum Command {

    // This is a very simplistic way of realizing different commands.

    FORWARD("Forward"),
    RIGHT("Turn Right"),
    LEFT("Turn Left"),
    FAST_FORWARD("Fast Forward"),

    /**Tilføjer manglende programmeringskort
     * @author Jasmin Clemmensen
     */
    THREE_FORWARD("Move three forward"),
    U_TURN("Turn 180 degrees"),
    BACKWARDS("Move one back"),

    // TODO Assignment V3
    /** @author Mohamad Anwar Meri
     *
     */
    OPTION_RIGHT_LEFT("Right or left", RIGHT, LEFT);

    final public String displayName;

    // TODO Assignment V3
    /**
     * @author Mohamad Anwar Meri
     */
    final private List<Command> options;

    Command(String displayName, Command... options) {
        this.displayName = displayName;
        //
        this.options = List.of(options);
    }

    /**
     * <p>isInteractive.</p>
     *
     * @return a boolean.
     */
    public boolean isInteractive() {
        return !options.isEmpty();
    }

    /**
     * <p>Getter for the field <code>options</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Command> getOptions() {
        return options;
    }
}
