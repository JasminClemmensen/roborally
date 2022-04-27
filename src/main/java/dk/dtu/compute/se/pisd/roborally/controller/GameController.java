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

import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;

/**
 * This is the controller main class, that interacts when the players do some actions in the GUI,
 * to make a simple move to see something happening on the board.
 * And that we see happening on the board, it's due to the controlling part of the game.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Mohamad Anwar Meri, s215713@dtu.dk
 * @author Jasmin
 * @author Anna Elise Høfde.
 * @version $Id: $Id
 */
public class GameController {

    public Board board;
    public boolean win = false;

    /**
     * <p>Constructor for GameController.</p>
     *
     * @param board a {@link dk.dtu.compute.se.pisd.roborally.model.Board} object.
     */
    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     * Here we change the turn of the current player.
     *
     * @param space the space to which the current player should move
     * @author Mohamad Anwar Meri
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space) {
        // TODO Assignment V1: method should be implemented by the students:
        //   - the current player should be moved to the given space
        //     (if it is free()
        //   - and the current player should be set to the player
        //     following the current player
        //   - the counter of moves in the game should be increased by one
        //     if and when the player is moved (the counter and the status line
        //     message needs to be implemented at another place)
        /**
         * Here we change the turn of the current player.
         * @author Mohamad Anwar Meri
         */
        Player currentPlayer = board.getCurrentPlayer();
        if (space != null && currentPlayer != null) {
            if (space.getPlayer() == null) {
                currentPlayer.setSpace(space);
                int currentPlayerNumber = board.getPlayerNumber(currentPlayer);
                Player nextPlayer = board.getPlayer((currentPlayerNumber + 1) % board.getPlayersNumber());
                board.setCurrentPlayer(nextPlayer);

                /**
                 * It updates itself by calculating how many pieces it has so far
                 * and you add 1, then we get the new number of pieces.
                 * @param board.setCount (board.getCount() + 1);
                 * @author Mohamad Anwar Meri
                 */
                board.setCount(board.getCount() + 1);

            }

        }

    }

    // XXX: V2
    /**
     * <p>startProgrammingPhase.</p>
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX: V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX: V2
    /**
     * <p>finishProgrammingPhase.</p>
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    // XXX: V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }


    /**
     * All programs must execute,
     * and this metode make the specifik moves the players ask for
     *
     * @author Mohamad Anwar Meri
     */
    // XXX: V2
    public void executePrograms() { //All programs must execute
        board.setStepMode(false); //StepMode er false her, fordi vi vil gerne have at alle execute.
        continuePrograms();

        /**
         * StepMode is false here, because we want to execute all programs
         * @param StepMode(false)
         * We get into the do-while loops in continuePrograms () all the time.
         * @param executePrograms()
         * @author Mohamad Anwar Meri
         */

    }


    // XXX: V2
    /**
     * <p>executeStep.</p>
     */
    public void executeStep() { //Det er bare execute en step ad gang for hver spiller, dvs. ny step med en ny spiller.
        board.setStepMode(true); //StepMode afgør at vi kører continuePrograms(); flere gange, dvs. igen og igen. Eller om vi kører den igennem en gang.
        continuePrograms();
        /**
         * It's just execute one step at a time for each player,
         * which means new step with a new player.
         * We interrupt after a step is completed, and we do not continue.
         * @param executeStep()
         * Determines that we run continuePrograms(); several times, which means. repeatedly. Or if we run it through once
         * @param StepMode
         * @author Mohamad Anwar Meri
         */
    }

    /**
     * It is private, which means. It cannot have an external call
     * We execute programmed in this function.
     * We have a do and while which executes executeNextStep();
     */
    // XXX: V2
    // XXX: V2
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
        /**
         * this means that as long as we are in the Enabled Phase then we need to execute executeNextStep ();
         * If while no longer belongs in enabled Phase, then we move out of executeNextStep();
         * @param while
         * @author Mohamad Anwar Meri
         */
    }

    /**
     * This method starts by figuring out what the current player is.
     * Here we check in which Phase this game is an activated Phase otherwise,
     * we do not have to accept anything if the players do not belong in the activated phase.
     */
    // XXX: V2
    // XXX: V2
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer(); //executeNextStep starter med at finde ud af hvad den aktuelle spiller.

        /**
         *
         * The current player should not be Null, because otherwise we can not find his card.
         * Here we find out in which step the currentPlayers are on
         * @param step
         * @author Mohamad Anwar Meri
         */
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep(); // Her finder vi ud af i hvilken step er vi på.

            /**
             * Here we check again if the steps are within the values we have.
             * We have player register from 0 up to player register number "5".
             * @param Player.NO_REGISTERS
             * @author Mohamad Anwar Meri
             */
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    // TODO Assignment V3

                    if (command.isInteractive()) {
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    for(int i = 0; i < board.getPlayersNumber(); i++) {
                        Player player = board.getPlayer(i);
                        if (player != null && player.getSpace() != null && player.getSpace().getActions() != null) {
                            for (FieldAction action : player.getSpace().getActions()) {
                                action.doAction(this, player.getSpace());
                            }
                        }
                    }
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {

                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }

    }

    // TODO Assignment V3

    /**
     * <p>executeOption.</p>
     *
     * @param option a {@link dk.dtu.compute.se.pisd.roborally.model.Command} object.
     */
    public void executeOption(Command option) { //det er den option spilleren har valgt
        Player currentPlayer = board.getCurrentPlayer(); // finder ud af hvad er den aktuelle spiller
        if (currentPlayer != null && // her betyder det hvis den aktuelle spiller kun ikke er null skal den gøre noget.
                board.getPhase() == Phase.PLAYER_INTERACTION && //Finder ud af om spillerens Phasen er et aktivt måde hvor det giver mening og kalde på metoden "executeOption"
                option != null) { //tjekker om den option er ikke null
            board.setPhase(Phase.ACTIVATION); //Hvis vi ikke sætter den her så bliver spilleren i interaction Phasen og så kan man ikke spille videre.
            executeCommand(currentPlayer, option); //executer Command

            int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
            if (nextPlayerNumber < board.getPlayersNumber()) {
                board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            } else {

                int step = board.getStep() + 1;
                if (step < Player.NO_REGISTERS) { //Hvis den her step er ikke ind for det register vi har, så skal vi sørger vi det bliver den ny step.
                    makeProgramFieldsVisible(step);
                    board.setStep(step);//den ny step
                    board.setCurrentPlayer(board.getPlayer(0));// starter vi med spiller 1 igen
                } else {
                    startProgrammingPhase();// vi sætter programmeret ind for programmerets Phasen, hvis vi er færdig med at executer.
                }
            }

        } else{
            assert false;
        }

        if (!board.isStepMode()) {
            continuePrograms();
        }
        /**
         * How to implement the control if the player has chosen which option the player will execute,
         * so that the player can continue.
         * @param executeOption
         * Find out if the player's Phase is an active way where it makes sense
         * to call the method "executeOption"
         * @param phase.PLAYER_INTERACTION
         * If we do not put it, the player will be in the interaction phase,
         * and then you can not continue playing
         * @param Phase.ACTIVATION
         * @author Mohamad Anwar Meri
         */
    }





    // XXX: V2
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {// i den switch case statement siger vi til hver commando hvad de skal gøre.
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                case THREE_FORWARD:
                    this.threeForward(player);
                    break;
                case U_TURN:
                    this.uTurn(player);
                    break;
                case BACKWARDS:
                    this.backwards(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * In this method, when the player lands on a field
     * where there is already a robot,the robot will be pushed on.
     * Which means the robots can push each other forward.
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Anna Elise Høfde og Jasmin Clemmensen
     */
    public void moveForward(@NotNull Player player) {
        if (player.board == board) {
            Space space = player.getSpace();
            Heading heading = player.getHeading();

            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                try {
                    moveToSpace(player, target, heading);
                } catch (ImpossibleMoveException e) {
                    // we don't do anything here  for now; we just catch the
                    // exception so that we do no pass it on to the caller
                    // (which would be very bad style).
                }


            }
        }
    }

    // TODO Assignment V2
    /**
     * This method calls the moveForward function twice.
     * The player should move two times forward.
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Mohamad Anwar Meri
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);

    }

    // TODO Assignment V2
    /**
     * This method turns the player to the cardinal directions that the player choose from program's cards.
     * Here The player turns to the right
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Mohamad Anwar Meri
     */
    public void turnRight(@NotNull Player player) {
        Heading currentHeading = player.getHeading();
        if (currentHeading != null) {
            Heading newHeading = currentHeading.next();
            player.setHeading(newHeading);
        }
    }

    // TODO Assignment V2
    /**
     * This method turns the player to the cardinal directions that the player choose from program's cards.
     * Here The player turns to the left
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Mohamad Anwar Meri
     */
    public void turnLeft(@NotNull Player player) {
        Heading currentHeading = player.getHeading();
        if (currentHeading != null) {
            Heading newHeading1 = currentHeading.prev();
            player.setHeading(newHeading1);
        }
    }

    /**
     * This method calls the moveForward function thrice.
     * The player should move three times forward.
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Jasmin Clemmensen
     */
    public void threeForward(@NotNull Player player){
        moveForward(player);
        moveForward(player);
        moveForward(player);
    }

    /**
     * This method turns the player 180 degrees
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Jasmin Clemmensen
     */
    public void uTurn(@NotNull Player player) {
        Heading currentHeading = player.getHeading();
        if (currentHeading != null) {
            Heading newHeading2  = currentHeading.round();
            player.setHeading(newHeading2);
        }
    }

    /**
     * This method moves the player a field backwards
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @author Jasmin Clemmensen
     */
    public void backwards(@NotNull Player player) {
        Space currentspace = player.getSpace();
        if (currentspace != null && player.board == currentspace.board) {
            Space target = board.getback(currentspace, player.getHeading());
            if (target != null && target.getPlayer() == null) {
                player.setSpace(target);
        }
    }}

    /**
     * <p>moveCards.</p>
     *
     * @param source a {@link dk.dtu.compute.se.pisd.roborally.model.CommandCardField} object.
     * @param target a {@link dk.dtu.compute.se.pisd.roborally.model.CommandCardField} object.
     * @return a boolean.
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }


    /**
     * <p>notImplemented.</p>
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;

        /**
         * @param notImplemented method called when no corresponding controller operation is implemented yet. This
         * should eventually be removed.
         */
    }

    /**
     * This method is used to get a player stopped by wall.
     *
     * @param player a {@link dk.dtu.compute.se.pisd.roborally.model.Player} object.
     * @param space a {@link dk.dtu.compute.se.pisd.roborally.model.Space} object.
     * @param heading a {@link dk.dtu.compute.se.pisd.roborally.model.Heading} object.
     * @throws dk.dtu.compute.se.pisd.roborally.controller.GameController.ImpossibleMoveException
     * @author Mohamad Anwar Meri
     */
    public void moveToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading)
            throws ImpossibleMoveException {
        assert board.getNeighbour(player.getSpace(), heading) == space; // make sure the move to here is possible in principle
        Player other = space.getPlayer();
        if(!space.getWalls().isEmpty()){
            for (int i = 0; i < space.getWalls().size();i++){
                if(space.getWalls().get(i).next().next() == heading){
                    throw new ImpossibleMoveException(player, space, heading);
                }
            }

        }
        else if(!player.getSpace().getWalls().isEmpty()){
            for(int i = 0; i < player.getSpace().getWalls().size(); i++){
                if(player.getSpace().getWalls().get(i) == heading){
                    throw new ImpossibleMoveException(player, space, heading);
                }
            }
        }
        if (other != null){
            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                // XXX Note that there might be additional problems with
                //     infinite recursion here (in some special cases)!
                //     We will come back to that!
                moveToSpace(other, target, heading);

                // Note that we do NOT embed the above statement in a try catch block, since
                // the thrown exception is supposed to be passed on to the caller

                assert target.getPlayer() == null : target; // make sure target is free now
            } else {
                throw new ImpossibleMoveException(player, space, heading);
            }
        }
        player.setSpace(space);
    }


    public static class ImpossibleMoveException extends Exception {

        private Player player;
        private Space space;
        private Heading heading;

        public ImpossibleMoveException(Player player, Space space, Heading heading) {
            super("Move impossible");
            this.player = player;
            this.space = space;
            this.heading = heading;
        }
    }

    }
