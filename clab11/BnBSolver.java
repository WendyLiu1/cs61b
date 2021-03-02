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
        return this.sortBearsBasedOnBed(this.bearSortList, 0);
    }

    private List<Bear> sortBearsBasedOnBed(List<Bear> unsortedBearList, int bedIdx) {
        if (unsortedBearList == null || unsortedBearList.size() < 2 || bedIdx == this.bedSortList.size()) {
            return unsortedBearList;
        }
        Bed pivot = this.bedSortList.get(bedIdx);
        List<Bear> less = new ArrayList<>();
        List<Bear> equal = new ArrayList<>();
        List<Bear> greater = new ArrayList<>();
        BnBSolver.partition(unsortedBearList, pivot, less, equal, greater);

        less = this.sortBearsBasedOnBed(less, bedIdx + 1);
        greater = this.sortBearsBasedOnBed(greater, bedIdx + 1);
        List<Bear> result = this.concatenate(less, equal, greater);
        return result;
    }

    /**
     * Returns List of Beds such that the ith Bear is the same size as the ith Bear of solvedBears().
     */
    public List<Bed> solvedBeds() {
        return this.sortBedBasedOnBear(this.bedSortList, 0);
    }

    private List<Bed> sortBedBasedOnBear(List<Bed> unsorted, int bearIdx) {
        if (unsorted == null || unsorted.size() < 2 || bearIdx == this.bearSortList.size()) {
            return unsorted;
        }
        Bear pivot = this.bearSortList.get(bearIdx);
        List<Bed> less = new ArrayList<>();
        List<Bed> equal = new ArrayList<>();
        List<Bed> greater = new ArrayList<>();
        this.partition(unsorted, pivot, less, equal, greater);

        less = this.sortBedBasedOnBear(less, bearIdx + 1);
        greater = this.sortBedBasedOnBear(greater, bearIdx + 1);

        List<Bed> result = this.concatenate(less, equal, greater);
        return result;
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

    private <P> List<P> concatenate(List<P> l1, List<P> l2, List<P> l3) {
        List<P> newList = new ArrayList<>(l1);
        newList.addAll(l2);
        newList.addAll(l3);
        return newList;
    }
}
