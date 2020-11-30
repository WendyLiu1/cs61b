package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Tests the plip class
 *  @authr JWL
 */

public class TestClorus {

    @Test
    public void testBasics() {
        Clorus c = new Clorus(6);
        assertEquals(6, c.energy(), 0.01);
        c.move();
        assertEquals(5.97, c.energy(), 0.01);
        c.move();
        assertEquals(5.94, c.energy(), 0.01);
        c.stay();
        assertEquals(5.93, c.energy(), 0.01);
        c.stay();
        assertEquals(5.92, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus c = new Clorus(2);
        Clorus offSpring1 = c.replicate();
        assertEquals(1, offSpring1.energy(), 0.01);
        assertEquals(1, c.energy(), 0.01);

        Clorus offSpring2 = c.replicate();
        assertEquals(0.5, offSpring2.energy(), 0.01);
        assertEquals(0.5, c.energy(), 0.01);

        Clorus offSpring3 = c.replicate();
        assertEquals(0.25, offSpring3.energy(), 0.01);
        assertEquals(0.25, c.energy(), 0.01);
    }

    @Test
    public void testChoose() {

        // No empty adjacent spaces; stay.
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);

        assertEquals(expected, actual);


        // existing plips
        c = new Clorus(1.2);
        Plip p = new Plip(1.1);
        HashMap<Direction, Occupant> topPlip = new HashMap<Direction, Occupant>();
        topPlip.put(Direction.TOP, p);
        topPlip.put(Direction.BOTTOM, new Empty());
        topPlip.put(Direction.LEFT, new Impassible());
        topPlip.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(topPlip);
        expected = new Action(Action.ActionType.ATTACK, Direction.TOP);

        assertEquals(expected, actual);
        c.attack(p);
        assertEquals(2.3, c.energy(), 0.01);


        // replicate towards right.
        c = new Clorus(1.2);
        HashMap<Direction, Occupant> rightEmpty = new HashMap<Direction, Occupant>();
        rightEmpty.put(Direction.TOP, new Impassible());
        rightEmpty.put(Direction.BOTTOM, new Impassible());
        rightEmpty.put(Direction.LEFT, new Impassible());
        rightEmpty.put(Direction.RIGHT, new Empty());

        actual = c.chooseAction(rightEmpty);
        expected = new Action(Action.ActionType.REPLICATE, Direction.RIGHT);

        assertEquals(expected, actual);


        // Move to random direction
        c = new Clorus(0.8);
        HashMap<Direction, Occupant> topEmpty = new HashMap<Direction, Occupant>();
        topEmpty.put(Direction.TOP, new Empty());
        topEmpty.put(Direction.BOTTOM, new Impassible());
        topEmpty.put(Direction.LEFT, new Impassible());
        topEmpty.put(Direction.RIGHT, new Impassible());

        actual = c.chooseAction(topEmpty);
        expected = new Action(Action.ActionType.MOVE, Direction.TOP);

        assertEquals(expected, actual);
    }
}
