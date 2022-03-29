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

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import org.jetbrains.annotations.NotNull;
import javafx.scene.shape.Line;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 50; // 60; // 75;
    final public static int SPACE_WIDTH = 50;  // 60; // 75;

    public final Space space;


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
    }


    @Override
    /**
     * @author Mohamad Anwar Meri
     * Adding walls to the game.
     */
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();

            // XXXX Her tegner vi alle væge

            Pane pane = new Pane();
            Rectangle rectangle = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
            rectangle.setFill(Color.TRANSPARENT);
            pane.getChildren().add(rectangle);
            for (Heading heading : space.getWalls()) {
                if (heading == Heading.SOUTH) {
                    Line line = new Line(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                    line.setStroke(Color.RED);
                    line.setStrokeWidth(5);
                    line.setStrokeLineCap(StrokeLineCap.ROUND);
                    pane.getChildren().add(line);
                }
                this.getChildren().add(pane);

                // XXX mangle væg på andre sider
            }
            Pane pane1 = new Pane();
            Rectangle rectangle1 = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
            rectangle1.setFill(Color.TRANSPARENT);
            pane1.getChildren().add(rectangle1);
            for (Heading heading1 : space.getWalls()) {
                if (heading1 == Heading.NORTH) {
                    Line line1 = new Line(2, 2, SPACE_WIDTH - 2, 2);
                    line1.setStroke(Color.RED);
                    line1.setStrokeWidth(5);
                    line1.setStrokeLineCap(StrokeLineCap.ROUND);
                    pane.getChildren().add(line1);
                }
                this.getChildren().add(pane1);
            }
            Pane pane2 = new Pane();
            Rectangle rectangle2 = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
            rectangle2.setFill(Color.TRANSPARENT);
            pane2.getChildren().add(rectangle2);
            for (Heading heading2 : space.getWalls()) {
                if (heading2 == Heading.EAST) {
                    Line line2 = new Line(SPACE_WIDTH-2,2,SPACE_WIDTH-2,SPACE_HEIGHT-2);
                    line2.setStroke(Color.RED);
                    line2.setStrokeWidth(5);
                    line2.setStrokeLineCap(StrokeLineCap.ROUND);
                    pane.getChildren().add(line2);
                }
                this.getChildren().add(pane2);
            }
            Pane pane3 = new Pane();
            Rectangle rectangle3 = new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
            rectangle3.setFill(Color.TRANSPARENT);
            pane3.getChildren().add(rectangle3);
            for (Heading heading3 : space.getWalls()) {
                if (heading3 == Heading.WEST) {
                    Line line3 = new Line(2,2,2,SPACE_HEIGHT-2);
                    line3.setStroke(Color.RED);
                    line3.setStrokeWidth(5);
                    line3.setStrokeLineCap(StrokeLineCap.ROUND);
                    pane.getChildren().add(line3);
                }
                this.getChildren().add(pane3);
            }

            // Her tegnes spilleren
            Player player = space.getPlayer();
            if (player != null) {
                Polygon arrow = new Polygon(0.0, 0.0,
                        10.0, 20.0, 20.0, 0.0); //Størrelse af selve spiller dvs Polygon.
                try {
                    arrow.setFill(Color.valueOf(player.getColor()));
                } catch (Exception e) {
                    arrow.setFill(Color.MEDIUMPURPLE);
                }

                arrow.setRotate((90 * player.getHeading().ordinal() % 360));
                this.getChildren().add(arrow);
            }


        }
    }
}















    /*Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
    GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.RED);
                    gc.setLineWidth(5);
                    gc.setLineCap(StrokeLineCap.ROUND);
                     for (Heading wall : space.getWalls()) {
                switch (wall) {
                    case SOUTH:
                        line.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                        break;
                    case WEST:
                        gc.strokeLine(2, 2, 2, SPACE_HEIGHT - 2);
                        break;
                    case NORTH:
                        gc.strokeLine(2, 2, SPACE_WIDTH - 2, 2);
                        break;
                    case EAST:
                        gc.strokeLine(SPACE_WIDTH - 2, 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                        break;

                }
            }

             for (FieldAction action : space.getActions()) {
                if (action instanceof ConveyorBelt) {
                    ConveyorBelt conveyorBelt = (ConveyorBelt) action;
                    Polygon arrow = new Polygon(0.0, 0.0,
                            30.0, 60.0,
                            60.0, 0.0);
                    arrow.setFill(Color.LIGHTGRAY);
                    arrow.setRotate((90 * conveyorBelt.getHeading().ordinal()) % 360);
                    this.getChildren().add(arrow);
                }
            }
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
     */

