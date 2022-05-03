package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.dal.GameInDB;
import dk.dtu.compute.se.pisd.roborally.dal.Repository;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class is testing GameController necessary
 * @author Mohamad Anwar Meri
 */

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null, "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    /**
     * Test for Assignment V1 (can be delete later once V1 was shown to the teacher)
     */
    @Test
    void testV1() {
        Board board = gameController.board;

        Player player = board.getCurrentPlayer();
        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        assertEquals(player, board.getSpace(0, 4).getPlayer(), "Player " + player.getName() + " should beSpace (0,4)!");
    }



    /**
     * Assignment V2
     */

    @Test
    void moveCurrentPlayerToSpace() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        assertEquals(player1, board.getSpace(0, 4).getPlayer(), "Player " + player1.getName() + " should beSpace (0,4)!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        assertEquals(player2, board.getCurrentPlayer(), "Current player should be " + player2.getName() + "!");
    }

    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveForward(current);

        assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }


    @Test
    void fastForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.fastForward(current);

        assertEquals(current, board.getSpace(0, 2).getPlayer(), " Player 1" + " should beSpace (0,2)!" );
        assertEquals(Heading.SOUTH, current.getHeading(), " Player 1 " + " should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    @Test
    void turnRight() {
        Board board = gameController.board;
        Player player = board.getPlayer(2);
        player.setHeading(Heading.NORTH);

        gameController.turnRight(player);
        assertEquals(player.getHeading(), Heading.EAST, " " + player.getName() + " should be heading " + player.getHeading() + "!!");
    }

    @Test
    void turnLeft() {
        Board board = gameController.board;
        Player player = board.getPlayer(3);
        player.setHeading(Heading.NORTH);

        gameController.turnLeft(player);
        assertEquals(player.getHeading(), Heading.WEST, " " + player.getName() + " should be heading " + player.getHeading() +  "!!");

    }


    @Test
    void moveThreeForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.threeForward(current);
        assertEquals(current, board.getSpace(0, 3).getPlayer(), " Player 1" + " should beSpace (0,3)!" );
        assertEquals(Heading.SOUTH, current.getHeading(), " Player 1 " + " should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }


    @Test
    void moveBackWards() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.backwards(current);
        assertEquals(current, board.getSpace(0, 0).getPlayer(), " Player 1" + " should beSpace (1,0)!" );
        assertEquals(Heading.SOUTH, current.getHeading(), " Player 1 " + " should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,0) should be empty!");
    }


    @Test
    void uTurn() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.setHeading(Heading.EAST);
        gameController.uTurn(current);
        Assertions.assertEquals(Heading.WEST, current.getHeading());
    }


    @Test
    void robot_will_push_another_robot() {
        Player player1 = gameController.board.getPlayer(1);
        player1.setSpace(gameController.board.getSpace(3, 0));
        player1.setHeading(Heading.NORTH);

        Player player2 = gameController.board.getPlayer(2);
        player2.setSpace(gameController.board.getSpace(3, 1));

        gameController.moveForward(player1);

        Assertions.assertEquals(3, player2.getSpace().x,"Player " + player2.getName() + " should be space at (3,1)");
        Assertions.assertEquals(1, player2.getSpace().y,"Player " + player2.getName() + " should be space at (3,1)");

        Assertions.assertEquals(3, player1.getSpace().x,"Player " + player1.getName() + " should be space at (3,7)");
        Assertions.assertEquals(7, player1.getSpace().y,"Player " + player1.getName() + " should be space at (3,7)");

    }
}
