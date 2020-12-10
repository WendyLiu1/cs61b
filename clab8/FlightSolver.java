import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * Solver for the Flight problem (#9) from CS 61B Spring 2018 Midterm 2.
 * Assumes valid input, i.e. all Flight start times are >= end times.
 * If a flight starts at the same time as a flight's end time, they are
 * considered to be in the air at the same time.
 */
public class FlightSolver {
    private ArrayList<Flight> flights;
    public FlightSolver(ArrayList<Flight> flights) {
        this.flights = flights;
    }

    public int solve() {
        int counter = 0;
        int result = 0;
        Comparator<Flight> startTimeComparator = (arg1, arg2) -> (
            arg1.startTime() - arg2.startTime());
        Comparator<Flight> endTimeComparator = (arg1, arg2) -> {
            int diff = arg1.endTime() - arg2.endTime();
            return diff;
        };
        PriorityQueue<Flight> minStartTimePQ = new PriorityQueue<>(this.flights.size(), startTimeComparator);
        PriorityQueue<Flight> minEndTimePQ = new PriorityQueue<>(this.flights.size(), endTimeComparator);

        for (Flight f : this.flights) {
            minStartTimePQ.add(f);
            minEndTimePQ.add(f);
        }

        while (!minStartTimePQ.isEmpty()) {
            Flight start = minStartTimePQ.peek();
            Flight end = minEndTimePQ.peek();
            if (start.startTime() <= end.endTime()) {
                start = minStartTimePQ.poll();
                counter += start.passengers();
            } else {
                end = minEndTimePQ.poll();
                if (counter > result) {
                    result = counter;
                }
                counter -= end.passengers();
            }
        }

        if (counter > result) {
            result = counter;
        }
        return result;
    }

}
