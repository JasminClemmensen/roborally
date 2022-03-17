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
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
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
         * @author Mohamad Anwar Meri
         */
        Player currentPlayer = board.getCurrentPlayer();
        if (space != null && currentPlayer != null) {
            if (space.getPlayer() == null) {
                currentPlayer.setSpace(space);
                //skift spillerens tur.
                int currentPlayerNumber = board.getPlayerNumber(currentPlayer);
                Player nextPlayer = board.getPlayer((currentPlayerNumber + 1) % board.getPlayersNumber());
                board.setCurrentPlayer(nextPlayer);

                /**
                 * Den opdater sig, ved at regne ud hvor mange brikker den har indtil videre og
                 * tilføjer man 1 og så får vi den nye antal brik.
                 * @author Mohamad Anwar Meri
                 */
                board.setCount(board.getCount() + 1);

            }

        }

    }

    // XXX: V2
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
     * I executePrograms() bliver vi ind på den do-while løkker i continuePrograms() hele tiden.
     */
    // XXX: V2
    public void executePrograms() { //Alle Programs skal execute
        board.setStepMode(false); //StepMode er false her, fordi vi vil gerne have at alle execute.
        continuePrograms();

    }

    /**
     * I executeStep() afbryder vi efter et step er gennemført og fortsætter vi ikke.
     */
    // XXX: V2
    public void executeStep() { //Det er bare execute en step ad gang for hver spiller, dvs. ny step med en ny spiller.
        board.setStepMode(true); //StepMode afgør at vi kører continuePrograms(); flere gange, dvs. igen og igen. Eller om vi kører den igennem en gang.
        continuePrograms();
    }

    /**
     * Den er private, dvs. den kan ikke have en eksternt kald.
     * Med continuePrograms executere vi programmeret så langt muligt.
     * Vi har en do og while som udfører executeNextStep();
     * I while betyder det at så længe vi er i aktiveret Phase så skal vi udfører executeNextStep();
     * Hvis while ikke længere hører i aktiveret Phase, så rører vi ud af executeNextStep();
     */
    // XXX: V2
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    // XXX: V2
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer(); //executeNextStep starter med at finde ud af hvad den aktuelle spiller.

        /**
         *  Her tjekker vi i hvilken Phase er det her spil en aktivieret Phase ellers skal vi ikke acceptere noget som helst hvis spillerne hører ikke i aktiveret fase.
         *  Den aktuelle spiller skal ikke være NULL fordi ellers kan vi ikke finde hans kort.
         */
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {

            int step = board.getStep(); // Her finder vi ud af i hvilken step er vi på.

            /**
             * Her tjekker vi igen om de step ligger indenfor de værdier vi har.
             * Vi har spiller-register fra 0 op til spiller registersnummer.
             */
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    // TODO Assignment V3
                    /**
                     * @author Mohamad Anwar Meri
                     */
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
     * Hvordan man kan implementer controllen hvis spilleren har valgt hvilken option spilleren vil executer, så spilleren kan fortsætte.
     * @author Mohamad Anwar Meri
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

    // TODO Assignment V2
  /*  /**
     * @author Mohamad Anwar Meri
     * @param player the heading where the player should move.
     */
    /*
    public void moveForward(@NotNull Player player) {
        Space currentspace = player.getSpace();
        if (currentspace != null && player.board == currentspace.board) {
            Space target = board.getNeighbour(currentspace, player.getHeading());
            if (target != null && target.getPlayer() == null) {
                player.setSpace(target);
            }
        }

    }
*/
    // TODO Assignment V2
    /**
     * @author Mohamad Anwar Meri
     * @param player the player should move two times forward.
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player);
        moveForward(player);

    }

    // TODO Assignment V2
    /**
     * @author Mohamad Anwar Meri
     * @param player the player should turn to the right
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
     * @author Mohamad Anwar Meri
     * @param player the player should turn to the left
     */
    public void turnLeft(@NotNull Player player) {
        Heading currentHeading = player.getHeading();
        if (currentHeading != null) {
            Heading newHeading1 = currentHeading.prev();
            player.setHeading(newHeading1);
        }
    }
    /**
    Metoder til de manglende programmeringskort
     @author Jasmin Clemmensen
     */

    /**
     * @param player spilleren skal rykke tre felter frem
     * @author Jasmin Clemmensen
     */
    public void threeForward(@NotNull Player player){
        moveForward(player);
        moveForward(player);
        moveForward(player);
    }

    /**
     * @param player spilleren skal vende 180 grader
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
     * @param player spilleren skal rykke et felt baglæns
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
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

    /**
     *
     * @param player hvis spilleren lander på et felt hvor der allerede står en robot, bliver robotten skubbet videre
     * metode så robotterne kan skubbe hinanden fremad.
     * @author Anna og Jasmin
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

    void moveToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading) throws ImpossibleMoveException {
        assert board.getNeighbour(player.getSpace(), heading) == space; // make sure the move to here is possible in principle
        Player other = space.getPlayer();
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

    class ImpossibleMoveException extends Exception {

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
