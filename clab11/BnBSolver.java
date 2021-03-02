import java.util.ArrayList;
import java.util.List;

/**
 * BnBSolver for the Bears and Beds problem. Each Bear can only be compared to Bed objects and each Bed
 * can only be compared to Bear objects. There is a one-to-one mapping between Bears and Beds, i.e.
 * each Bear has a unique size and has exactly one corresponding Bed with the same size.
 * Given a list of Bears and a list of Beds, create lists of the same Bears and Beds where the ith Bear is the same
 * size as the ith Bed.
 */
public class BnBSolver {

    private List<Bear> bearSortList;
    private List<Bed> bedSortList;

    public BnBSolver(List<Bear> bears, List<Bed> beds) {
        this.bearSortList = new ArrayList<>(bears);
        this.bedSortList = new ArrayList<>(beds);
    }

    /**
     * Returns List of Bears such that the ith Bear is the same size as the ith Bed of solvedBeds().
     */
    public List<Bear> solvedBears() {
        return BnBSolver.quickSort(this.bearSortList, 0, this.bedSortList);
    }

    /**
     * Returns List of Beds such that the ith Bear is the same size as the ith Bear of solvedBears().
     */
    public List<Bed> solvedBeds() {
        return BnBSolver.quickSort(this.bedSortList, 0, this.bearSortList);
    }

    private static <P extends Comparable<Q>, Q extends Comparable<P>> List<P> quickSort(
            List<P> unsorted, int compareIdx, List<Q> compareList
    ) {
        if (unsorted == null) {
            throw new IllegalArgumentException();
        }
        if (unsorted.size() < 2 || compareIdx == compareList.size()) {
            return unsorted;
        }
        Q pivot = compareList.get(compareIdx);
        List<P> less = new ArrayList<>();
        List<P> equal = new ArrayList<>();
        List<P> greater = new ArrayList<>();

        BnBSolver.partition(unsorted, pivot, less, equal, greater);

        List<P> lessSorted = BnBSolver.quickSort(less, compareIdx + 1, compareList);
        List<P> greaterSorted = BnBSolver.quickSort(greater, compareIdx + 1, compareList);

        return BnBSolver.concatenate(lessSorted, equal, greaterSorted);
    }


    private static <P extends Comparable<Q>, Q extends Comparable<P>> void partition(
            List<P> unsorted, Q pivot,
            List<P> less, List<P> equal, List<P> greater
    ) {
        for (P item : unsorted) {
            if (item.compareTo(pivot) == 0) {
                equal.add(item);
            } else if (item.compareTo(pivot) < 0) {
                less.add(item);
            } else {
                greater.add(item);
            }
        }
    }

    private static <P> List<P> concatenate(List<P> l1, List<P> l2, List<P> l3) {
        List<P> newList = new ArrayList<>(l1);
        newList.addAll(l2);
        newList.addAll(l3);
        return newList;
    }
}
