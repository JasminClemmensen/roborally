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
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.dal.Connector;
import dk.dtu.compute.se.pisd.roborally.dal.GameInDB;
import dk.dtu.compute.se.pisd.roborally.dal.Repository;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * This controller-class is very important in RoboRally application,
 * because we create all important and required things to run and start the game.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Mohamad Anwar Meri,  s215713@dtu.dk
 * @author Anna Elise Høfde
 * @version $Id: $Id
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private List<String> BOARD_OPTIONS = Arrays.asList("defaultboard", "small");

    final private RoboRally roboRally;
    private Board board;

    private GameController gameController;



    /**
     * <p>Constructor for AppController.</p>
     *
     * @param roboRally a {@link dk.dtu.compute.se.pisd.roborally.RoboRally} object.
     */
    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }



    /**
     * This method starts a new game, with players-number from 2 players to 6 players.
     */
    public void newGame() {

        if (gameController != null) {
            // The UI should not allow this, but in case this happens anyway.
            // give the user the option to save the game or abort this operation!
            if (!stopGame()) {
                return;
            }
        }

        /**
         * @param dialog1 her laves dialogen så brugeren kan vælge imellem forskellige spilleplader.
         * @author Anna Elise Høfde
         */

        ChoiceDialog<String> dialog1 = new ChoiceDialog<>(BOARD_OPTIONS.get(0), BOARD_OPTIONS);
        dialog1.setTitle("Board");
        dialog1.setHeaderText("Choose board");
        Optional<String> result1 = dialog1.showAndWait();

        Board board = null;

        if (result1.isPresent()) {
            board = LoadBoard.loadBoard(result1.get());
        }

        /**
         * @param dialog her kan brugeren vælge hvor mange spillere de vil spille.
         */

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
            //Board board = new Board(8,8);
           // Board board = LoadBoard.LoadBoard1();
            gameController = new GameController(board);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i));
            }

            // XXX: V2
            // board.setCurrentPlayer(board.getPlayer(0));
            gameController.startProgrammingPhase();
            RepositoryAccess.getRepository().createGameInDB(board);
            roboRally.createBoardView(gameController);
        }
    }


    /**
     * In this method we save the game in database.
     */
    public void saveGame() {
        // XXX needs to be implemented eventually
        Repository repository = new Repository(new Connector());
        if (this.gameController.board.getGameId() != null) {
            repository.updateGameInDB(this.gameController.board);
        }
    }

    /**
     * In this method we load the game from the database, which was saved in saveGame().
     */
    public void loadGame() {
        // XXX needs to be implememted eventually
        // for now, we just create a new game
        /*if (gameController == null) {
           newGame();
         */

        Repository repository = new Repository(new Connector());
        List<GameInDB> game = repository.getGames();
        ChoiceDialog<GameInDB> choice = new ChoiceDialog<GameInDB>(game.get(0), game);
        choice.setContentText("Please select and load saved games\uD83D\uDE0A");
        choice.showAndWait();

        if (choice.getSelectedItem() != null) {
            this.board = repository.loadGameFromDB(choice.getSelectedItem().id);
            this.gameController = new GameController(this.board);

            if (this.board.getPhase() == Phase.INITIALISATION) {
                this.gameController.startProgrammingPhase();
            }
            roboRally.createBoardView(gameController);
        }

    }



    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            saveGame();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;

        }

    /**
     * This method will exit the RoboRally application, if the user press exit and ok button.
     * If user press exit and cancel, the user will return the application without exiting.
     * After the user exits the application, the game will be saved automatic.
     */
    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    /**
     * <p>isGameRunning.</p>
     *
     * @return a boolean.
     */
    public boolean isGameRunning() {
        return gameController != null;
    }


    /** {@inheritDoc} */
    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

}
