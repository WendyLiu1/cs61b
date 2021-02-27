package bearmaps.hw4;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import bearmaps.proj2ab.ArrayHeapMinPQ;
import bearmaps.proj2ab.ExtrinsicMinPQ;
import edu.princeton.cs.algs4.Stopwatch;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    private double solutionWeight;
    private LinkedList<Vertex> solutionList;
    private double timeSpent;
    private SolverOutcome result;
    private int dequeueOperation;

    /**
     * Constructor which finds the solution,
     * computing everything necessary for all other methods
     * to return their results in constant time.
     * @param input
     * @param start
     * @param end
     * @param timeout timeout limit in seconds
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        this.timeSpent = sw.elapsedTime();
        this.solutionList = new LinkedList<>();
        this.solutionWeight = 0.0;
        this.dequeueOperation = 0;

        ExtrinsicMinPQ<Vertex> pq = new ArrayHeapMinPQ<>();
        // Current shortest distance to source
        Map<Vertex, Double> distTo = new HashMap<>();
        // Previous node in temporary shortest path
        Map<Vertex, Vertex> edgeTo = new HashMap<>();
        HashSet<Vertex> visited = new HashSet<>();

        pq.add(start, input.estimatedDistanceToGoal(start, end));
        distTo.put(start, 0.0);

        while (pq.size() > 0) {
            if (pq.getSmallest().equals(end)) {
                this.result = SolverOutcome.SOLVED;
                Vertex pos = end;
                while (!pos.equals(start)) {
                    this.solutionList.addFirst(pos);
                    pos = edgeTo.get(pos);
                }
                this.solutionList.addFirst(start);
                this.solutionWeight = distTo.get(end);
                break;
            }

            if (sw.elapsedTime() > timeout) {
                this.result = SolverOutcome.TIMEOUT;
                break;
            }

            Vertex current = pq.removeSmallest();
            this.dequeueOperation++;
            visited.add(current);

            double curDistToSource = distTo.get(current);
            for (WeightedEdge<Vertex> e : input.neighbors(current)) {
                Vertex to = e.to();

                // This is necessary to make sure we dont re-add
                // visited node to pq
                if (visited.contains(to)) {
                    continue;
                }

                double weight = e.weight();
                double newDist = curDistToSource + weight;
                if (pq.contains(to)) {
                    double endDistToSource = distTo.get(to);
                    if (newDist < endDistToSource) {
                        distTo.put(to, newDist);
                        edgeTo.put(to, current);
                        pq.changePriority(to, newDist + input.estimatedDistanceToGoal(to, end));
                    }
                } else {
                    distTo.put(to, newDist);
                    edgeTo.put(to, current);
                    pq.add(to, newDist + input.estimatedDistanceToGoal(to, end));
                }
            }
        }

        this.timeSpent = sw.elapsedTime();
        if (this.result == null) {
            this.result = SolverOutcome.UNSOLVABLE;
        }
    }

    /**
     * Returns one of SolverOutcome.SOLVED, SolverOutcome.TIMEOUT, or SolverOutcome.UNSOLVABLE.
     * @return SOLVED if the AStarSolver was able to complete all work in the time given.
     * UNSOLVABLE if the priority queue became empty.
     * TIMEOUT if the solver ran out of time. You should check to see if you have run
     * out of time every time you dequeue.
     */
    public SolverOutcome outcome() {
        return this.result;
    }

    /**
     * A list of vertices corresponding to a solution.
     * Should be empty if result was TIMEOUT or UNSOLVABLE
     * @return
     */
    public List<Vertex> solution() {
        return this.solutionList;
    }

    /**
     * The total weight of the given solution,
     * taken into account edge weight. Should be 0
     * if result was TIMEOUT or UNSOLVABLE
     * @return
     */
    public double solutionWeight() {
        return this.solutionWeight;
    }

    /**
     * The total number of priority queue dequeue operations.
     * @return
     */
    public int numStatesExplored() {
        return this.dequeueOperation;
    }

    /**
     * The total time spent in seconds by the constructor.
     * @return
     */
    public double explorationTime() {
        return this.timeSpent;
    }

}
