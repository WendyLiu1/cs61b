package creatures;

import huglife.*;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * An implementation of a motile pacifist photosynthesizer.
 *
 * @author Josh Hug
 */
public class Plip extends Creature {

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
    protected static final String CREATURE_NAME = "plip";

    /**
     * Upper limit of the energy level
     */
    private static final double MAX_ENERGY = 2.0;

    /**
     * Energy consumption when move
     */
    private static final double ENERGY_CONSUMPTION_MOVE = 0.15;

    /**
     * Energy recovered when stay
     */
    private static final double ENERGY_RECOVERY_STAY = 0.2;

    /**
     * fraction of energy to retain when replicating.
     */
    private static final double ENERGY_REPLICATE_RETAIN = 0.5;

    /**
     * fraction of energy to bestow upon offspring.
     */
    private static final double ENERGY_REPLICATE_GIVEN = 0.5;

    /**
     * probability of taking a move when ample space available.
     */
    private static final double MOVE_PROBABILITY = 0.5;
    /**
     * creates plip with energy equal to E.
     */
    public Plip(double e) {
        super(Plip.CREATURE_NAME);
        this.energy = Plip.determineEnergy(e);
        this.r = 99;
        this.g = Plip.determineGreen(this.energy);
        this.b = 76;
    }

    /**
     * creates a plip with energy equal to 1.
     */
    public Plip() {
        this(1);
    }

    /**
     * Should return a color with red = 99, blue = 76, and green that varies
     * linearly based on the energy of the Plip. If the plip has zero energy,
     * it should have a green value of 63. If it has max energy, it should
     * have a green value of 255. The green value should vary with energy
     * linearly in between these two extremes. It's not absolutely vital
     * that you get this exactly correct.
     */
    public Color color() {
        this.g = Plip.determineGreen(this.energy);
        return color(this.r, this.g, this.b);
    }

    /**
     * Do nothing with C, Plips are pacifists.
     */
    @Override
    public void attack(Creature c) {
        // do nothing.
    }

    /**
     * Plips should lose 0.15 units of energy when moving. If you want to
     * to avoid the magic number warning, you'll need to make a
     * private static final variable. This is not required for this lab.
     */
    @Override
    public void move() {
        double newEnergy = this.energy - Plip.ENERGY_CONSUMPTION_MOVE;
        this.energy = Plip.determineEnergy(newEnergy);
    }


    /**
     * Plips gain 0.2 energy when staying due to photosynthesis.
     */
    @Override
    public void stay() {
        double newEnergy = this.energy + Plip.ENERGY_RECOVERY_STAY;
        this.energy = Plip.determineEnergy(newEnergy);
    }

    /**
     * Plips and their offspring each get 50% of the energy, with none
     * lost to the process. Now that's efficiency! Returns a baby
     * Plip.
     */
    @Override
    public Plip replicate() {
        double givenEnergy = this.energy * Plip.ENERGY_REPLICATE_RETAIN;
        this.energy = Plip.determineEnergy(this.energy * Plip.ENERGY_REPLICATE_RETAIN);
        return new Plip(givenEnergy);
    }

    /**
     * Plips take exactly the following actions based on NEIGHBORS:
     * 1. If no empty adjacent spaces, STAY.
     * 2. Otherwise, if energy >= 1, REPLICATE towards an empty direction
     * chosen at random.
     * 3. Otherwise, if any Cloruses, MOVE with 50% probability,
     * towards an empty direction chosen at random.
     * 4. Otherwise, if nothing else, STAY
     * <p>
     * Returns an object of type Action. See Action.java for the
     * scoop on how Actions work. See SampleCreature.chooseAction()
     * for an example to follow.
     */
    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        Deque<Direction> emptyNeighbors = new ArrayDeque<>();

        boolean anyClorus = false;
        Action action;

        for (Map.Entry<Direction, Occupant> entry: neighbors.entrySet()) {
            String occupantName = entry.getValue().name();
            if (occupantName.equals("empty")) {
                emptyNeighbors.add(entry.getKey());
            } else if (occupantName.equals(Clorus.CREATURE_NAME)) {
                anyClorus = true;
            }
        }

        if (emptyNeighbors.isEmpty()) {
            action = new Action(Action.ActionType.STAY);
        } else if (this.energy >= 1.0) {
            action = new Action(Action.ActionType.REPLICATE,
                    HugLifeUtils.randomEntry(emptyNeighbors));
        } else if (anyClorus) {
            if (Math.random() < Plip.MOVE_PROBABILITY) {
                action = new Action(Action.ActionType.MOVE,
                        HugLifeUtils.randomEntry(emptyNeighbors));
            } else {
                action = new Action(Action.ActionType.STAY);
            }
        } else {
            action = new Action(Action.ActionType.STAY);
        }

        return action;
    }

    /** If the plip has zero energy,
     * it should have a green value of 63. If it has max energy, it should
     * have a green value of 255.
     * @param currentEnergy current energy level
     * @return green color
     */
    private static int determineGreen(double currentEnergy) {
        int green = 63;
        if (currentEnergy == Plip.MAX_ENERGY) {
            green = 255;
        } else if (currentEnergy != 0) {
            green += (int) (96 * currentEnergy);
        }
        return green;
    }

    /**
     * determine the energy of the creature, ensure it will not
     * exceed upperBound and not below 0
     * @param energyLevel input energy
     * @return energy level of this creature
     */
    private static double determineEnergy(double energyLevel) {
        if (energyLevel > Plip.MAX_ENERGY) {
            energyLevel = 2.0;
        } else if (energyLevel < 0) {
            energyLevel = 0;
        }
        return energyLevel;
    }
}
