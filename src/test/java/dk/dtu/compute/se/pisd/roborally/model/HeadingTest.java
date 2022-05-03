package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * We are testing the heading methods to be sure it works as intend.
 * @author Mohamad Anwar Meri
 */

class HeadingTest {

    @Test
    void next() {

      Heading heading = Heading.WEST;
      Heading heading1 = heading.next();

      Assertions.assertEquals(heading1, Heading.NORTH, "Player should be heading North");
    }

    @Test
    void prev() {
        Heading heading = Heading.WEST;
        Heading heading1 = heading.prev();

        Assertions.assertEquals(heading1, Heading.SOUTH, "Player should be heading South");
    }

    @Test
    void round() {
        Heading heading = Heading.WEST;
        Heading heading1 = heading.round();

        Assertions.assertEquals(heading1, Heading.EAST, "Player should heading East");
    }
}