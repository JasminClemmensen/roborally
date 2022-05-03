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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.Gear;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @version $Id: $Id
 */
public class SpaceView extends StackPane implements ViewObserver {

    /**
     * Constant <code>SPACE_HEIGHT=50</code>
     */
    final public static int SPACE_HEIGHT = 50; // 60; // 75;
    /**
     * Constant <code>SPACE_WIDTH=50</code>
     */
    final public static int SPACE_WIDTH = 50;  // 60; // 75;

    public final Space space;


    /**
     * <p>Constructor for SpaceView.</p>
     *
     * @param space a {@link dk.dtu.compute.se.pisd.roborally.model.Space} object.
     */
    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        //updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);


    }


    private void updatePlayer() {
        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0, 20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }
            arrow.setRotate((90 * player.getHeading().ordinal() % 360));
            this.getChildren().add(arrow);
        }
    }


    /**
     * ...
     * @author Mohamad Anwar Meri, s215713@dtu.dk
     */

    @Override
    /** {@inheritDoc} */
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();

            // XXXX Her tegner vi alle væge
            Pane pane = new Pane();
            for (Heading wall : space.getWalls()) {
                Polygon rectangle = switch (wall) {
                    case SOUTH -> new Polygon(0, SPACE_HEIGHT - 2, SPACE_WIDTH, SPACE_HEIGHT - 2,
                            SPACE_WIDTH, SPACE_HEIGHT, 0, SPACE_HEIGHT);
                    case WEST -> new Polygon(0,0, 2,0, 2, SPACE_HEIGHT, 0, SPACE_HEIGHT);
                    case NORTH -> new Polygon(0,0, SPACE_WIDTH, 0, SPACE_WIDTH, 2 ,0 , 2);
                    case EAST -> new Polygon(SPACE_WIDTH-2, 0, SPACE_WIDTH, 0, SPACE_WIDTH, SPACE_HEIGHT,
                            SPACE_WIDTH - 2,SPACE_HEIGHT);
                    default -> null;
                };

                if( rectangle != null) {
                    rectangle.setFill(Color.RED);
                    pane.getChildren().add(rectangle);
                }
            }

            this.getChildren().add(pane);

            for (FieldAction action : space.getActions()) {
                if (action instanceof ConveyorBelt) {
                    ConveyorBelt conveyorBelt = (ConveyorBelt) action;
                    Polygon arrow = new Polygon(0.0, 0.0,
                            20.0, 40.0,
                            40.0, 0.0);
                    arrow.setFill(Color.LIGHTBLUE);
                    arrow.setRotate((90 * conveyorBelt.getHeading().ordinal()) % 360);
                    this.getChildren().add(arrow);

                } else if(action instanceof Gear){
                    Gear gear = (Gear) action;
                    Circle circle = new Circle(10);
                    if(gear.isTurnsLeft()){
                        circle.setFill(Color.PURPLE);
                    } else {
                        circle.setFill(Color.DARKBLUE);
                    }
                    getChildren().add(circle);
                    updatePlayer();
                }
            }
            //Her tegnes spilleren
            updatePlayer();

        }
    }
}

