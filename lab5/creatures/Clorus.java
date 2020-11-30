package creatures;

import huglife.*;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * An implementation of a Clorus.
 *
 * @author JWL
 */
public class Clorus extends Creature {

    /**
     * red color.
     */
    private int r;
    /**
     * green color.
     */
    private int g;
    /**
     * blue color.
     */
    private int b;

    /**
     * Name of the creature
     */
    protected static final String CREATURE_NAME = "clorus";

    /**
     * Energy consumption when move
     */
    private static final double ENERGY_CONSUMPTION_MOVE = 0.03;

    /**
     * Energy consumption when stay
     */
    private static final double ENERGY_CONSUMPTION_STAY = 0.01;

    /**
     * fraction of energy to retain when replicating.
     */
    private static final double ENERGY_REPLICATE_RETAIN = 0.5;

    /**
     * fraction of energy to bestow upon offspring.
     */
    private static final double ENERGY_REPLICATE_GIVEN = 0.5;

    /**
     * creates clorus with energy equal to E.
     */
    public Clorus(double e) {
        super(Clorus.CREATURE_NAME);
        this.energy = Clorus.determineEnergy(e);
        this.r = 34;
        this.g = 0;
        this.b = 231;
    }

    /**
     * creates a clorus with energy equal to 1.
     */
    public Clorus() {
        this(1);
    }

    /**
     * return the color of this creature
     * @return return the color of this creature
     */
    public Color color() {
        return color(this.r, this.g, this.b);
    }

    /**
     * Gain the creature's energy after attack
     */
    @Override
    public void attack(Creature c) {
        this.energy += c.energy();
    }

    /**
     * Clorus should lose 0.03 units of energy when moving
     */
    @Override
    public void move() {
        double newEnergy = this.energy - Clorus.ENERGY_CONSUMPTION_MOVE;
        this.energy = Clorus.determineEnergy(newEnergy);
    }

    /**
     * Clorus should lose 0.01 units of energy when stay
     */
    @Override
    public void stay() {
        double newEnergy = this.energy - Clorus.ENERGY_CONSUMPTION_STAY;
        this.energy = Clorus.determineEnergy(newEnergy);
    }

    /**
     * Clorus and their offspring each get 50% of the energy, with none
     * lost to the process. Now that's efficiency! Returns a baby
     * Clorus.
     */
    @Override
    public Clorus replicate() {
        double givenEnergy = this.energy * Clorus.ENERGY_REPLICATE_GIVEN;
        this.energy = this.energy * Clorus.ENERGY_REPLICATE_RETAIN;
        return new Clorus(givenEnergy);
    }

    /**
     * Clorus take exactly the following actions based on NEIGHBORS:
     * 1. If no empty adjacent spaces, STAY.
     * 2. Otherwise, if Plips are seen, Clorus will Attack one of them.
     * 3. Otherwise, if the Clorus has energy greater than or equal to one, it
     * will Replicate to a random empty square.
     * 4. Otherwise, the Clorus will move to a random empty square.
     * <p>
     * Returns an object of type Action.
     */
    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();
        Deque<Direction> plipNeighbors = new ArrayDeque<>();

        Action action;

        for (Map.Entry<Direction, Occupant> entry: neighbors.entrySet()) {
            String occupantName = entry.getValue().name();
            Direction direction = entry.getKey();
            if (occupantName.equals("empty")) {
                emptyNeighbors.add(direction);
            } else if (occupantName.equals(Plip.CREATURE_NAME)) {
                plipNeighbors.add(direction);
            }
        }

        if (emptyNeighbors.isEmpty()) {
            action = new Action(Action.ActionType.STAY);
        } else if (!plipNeighbors.isEmpty()) {
            action = new Action(Action.ActionType.ATTACK,
                    HugLifeUtils.randomEntry(plipNeighbors));
        } else if (this.energy >= 1.0) {
            action = new Action(Action.ActionType.REPLICATE,
                    HugLifeUtils.randomEntry(emptyNeighbors));
        } else {
            action = new Action(Action.ActionType.MOVE,
                    HugLifeUtils.randomEntry(emptyNeighbors));
        }

        return action;
    }

    /**
     * determine the energy of the creature, ensure it will not go below 0
     * @param energyLevel input energy
     * @return energy level of this creature
     */
    private static double determineEnergy(double energyLevel) {
        return energyLevel;
    }
}

