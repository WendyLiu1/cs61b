import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Experiments {
    /**
     * Runs a computational experiment where you insert 5000 random items
     * into a BST. Make a plot of the average depth of BST vs the number of
     * items.
     * On the same axes, also plot the average depth of an optimal BST vs.
     * the number of item.
     */
    public static void experiment1() {
        int length = 5000;
        Random r = new Random();
        List<Integer> xValues = new ArrayList<>();
        List<Double> normalBstDepths = new ArrayList<>();
        List<Double> optimalBSTDepths = new ArrayList<>();
        BST<Integer> normalBST = new BST<>();

        for (int n = 1; n <= length; n++) {
            int newItem = r.nextInt(Integer.MAX_VALUE);
            normalBST.add(newItem);
            xValues.add(n);
            normalBstDepths.add(normalBST.getAverageDepth());
            optimalBSTDepths.add(ExperimentHelper.optimalAverageDepth(n));
        }

        Experiments.renderGraph(xValues, normalBstDepths, "Normal BST",
                optimalBSTDepths, "Optimal BST");
    }

    /**
     * Knott's experiment using asymmetric deletion (i.e. always picking successor).
     * 1. Initialize a tree by randomly inserting N items. Record the average depth observed as the ‘starting depth’.
     * 2. Randomly delete an item using asymmetric Hibbard deletion.
     * 3. Randomly insert a new item.
     * 4. Record the average depth of the tree.
     * 5. Repeat steps 2-4 a total of M times.
     * 6. Plot the data.
     */
    public static void experiment2() {
        int m = 5000;
        Random r = new Random();
        List<Integer> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();

        BST<Integer> randomTree = Experiments.generateRandomTree(50000, r);
        for (int i = 1; i <= m; i++) {
            int newItem = r.nextInt(Integer.MAX_VALUE);
            xValues.add(i);
            ExperimentHelper.asymmetricDeletionAndInsertion(randomTree, newItem);
            yValues.add(randomTree.getAverageDepth());
        }
        Experiments.renderGraph(xValues, yValues, "Asymmetric deletion",
                null, null);
    }

    /**
     * Knott's experiment using symmetric deletion (i.e. randomly picking between successor and predecessor).
     * 1. Initialize a tree by randomly inserting N items. Record the average depth observed as the ‘starting depth’.
     * 2. Randomly delete an item using asymmetric Hibbard deletion.
     * 3. Randomly insert a new item.
     * 4. Record the average depth of the tree.
     * 5. Repeat steps 2-4 a total of M times.
     * 6. Plot the data.
     */
    public static void experiment3() {
        int m = 5000;
        Random r = new Random();
        List<Integer> xValues = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();

        BST<Integer> randomTree = Experiments.generateRandomTree(50000, r);
        for (int i = 1; i <= m; i++) {
            int newItem = r.nextInt(Integer.MAX_VALUE);
            xValues.add(i);
            ExperimentHelper.symmetricDeletionAndInsertion(randomTree, newItem);
            yValues.add(randomTree.getAverageDepth());
        }
        Experiments.renderGraph(xValues, yValues, "Symmetric deletion",
                null, null);
    }

    /**
     * Generate a random BST with n number of items in it
     * @param n number of items in the tree
     * @param r random value generator
     * @return the random BST tree
     */
    private static BST<Integer> generateRandomTree(int n, Random r) {
        BST<Integer> randomTree = new BST<>();
        for (int i = 0; i < n; i++) {
            int nextItem = r.nextInt(Integer.MAX_VALUE);
            randomTree.add(nextItem);
        }
        return randomTree;
    }

    /**
     * Render the xy chart
     * @param xValues X values
     * @param yValues1 y values for series 1
     * @param series1Title title for series 1
     * @param yValues2 y values for series 2 if exist
     * @param series2Title title values for series 2 if exist
     */
    private static void renderGraph(List<Integer> xValues, List<Double> yValues1,
                                    String series1Title, List<Double> yValues2, String series2Title) {
        XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("Item Number").yAxisTitle("Average Depth").build();
        chart.addSeries(series1Title, xValues, yValues1);
        if (yValues2 != null) {
            chart.addSeries(series2Title, xValues, yValues2);
        }

        new SwingWrapper(chart).displayChart();
    }

    public static void main(String[] args) {
        //Experiments.experiment1();
        //Experiments.experiment2();
        Experiments.experiment3();
    }
}
