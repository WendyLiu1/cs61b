/**
 * Created by hug.
 */
public class ExperimentHelper {

    /** Returns the internal path length for an optimum binary search tree of
     *  size N. Examples:
     *  N = 1, OIPL: 0
     *  N = 2, OIPL: 1
     *  N = 3, OIPL: 2
     *  N = 4, OIPL: 4
     *  N = 5, OIPL: 6
     *  N = 6, OIPL: 8
     *  N = 7, OIPL: 10
     *  N = 8, OIPL: 13
     */
    public static int optimalIPL(int N) {
        int sum = 0;
        for (int i = 1; i <= N; i++) {
            sum += Math.floor(Math.log(i) / Math.log(2));
        }
        return sum;
    }

    /** Returns the average depth for nodes in an optimal BST of
     *  size N.
     *  Examples:
     *  N = 1, OAD: 0
     *  N = 5, OAD: 1.2
     *  N = 8, OAD: 1.625
     * @return
     */
    public static double optimalAverageDepth(int N) {
        return ((double) ExperimentHelper.optimalIPL(N)) / N;
    }

    /**
     * Delete an random item in the given bst using asymmetric deletion,
     * i.e. always take successor and then insert a new item to the tree
     * @param tree tree that operate with
     * @param newItem new item to be insert
     */
    public static void asymmetricDeletionAndInsertion(BST<Integer> tree, int newItem) {
        int item = tree.getRandomKey();
        tree.deleteTakingSuccessor(item);
        tree.add(newItem);
    }

    /**
     * Delete an random item in the given bst using symmetric deletion,
     * i.e. randomly picking between successor and predecessor
     * and then insert a new item to the tree
     * @param tree tree that operate with
     * @param newItem new item to be insert
     */
    public static void symmetricDeletionAndInsertion(BST<Integer> tree, int newItem) {
        int item = tree.getRandomKey();
        tree.deleteTakingRandom(item);
        tree.add(newItem);
    }
}
