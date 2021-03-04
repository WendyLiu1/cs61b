package bearmaps.proj2ab;

import java.util.List;

public class KdTree implements PointSet {

    private KdTreeNode root;
    private static final int orientationIdx = 0;

    public KdTree(List<Point> points) {
        if (points == null || points.size() < 1) {
            throw new IllegalArgumentException("input points is invalid");
        }

        for (Point point : points) {
            this.root = this.insert(point, this.root, KdTree.orientationIdx);
        }
    }

    /**
     * insert point into kd tree
     * @param point point to insert
     * @param current current position in the KD tree
     * @param idx index used to retrieve comparator
     * @return new root after insertion
     */
    private KdTreeNode insert(Point point, KdTreeNode current, int idx) {
        if (current == null) {
            return new KdTreeNode(new Point(point.getX(), point.getY()));
        }

        // change 0 to 1 and vice versa, also can do i = 1 - i;
        if (KdTree.comparePoints(current.point, point, idx) > 0) {
            idx ^= 1;
            current.left = this.insert(point, current.left, idx);
        } else if (KdTree.comparePoints(current.point, point, idx) < 0) {
            idx ^= 1;
            current.right = this.insert(point, current.right, idx);
        }
        return current;
    }

    /**
     * Compare dimension based on orientation
     * @param p1 point 1
     * @param p2 point 2
     * @param idx orientation index
     * @return negative value if p1 < p2, positive if p1 > p2, 0 otherwise
     */
    private static int comparePoints(Point p1, Point p2, int idx) {
        double p1Pos = p1.getX();
        double p2Pos = p2.getX();
        if (idx == 1) {
            p1Pos = p1.getY();
            p2Pos = p2.getY();
        }
        return Double.compare(p1Pos, p2Pos);
    }

    @Override
    public Point nearest(double x, double y) {
        KdTreeNode best = this.root;
        best = this.nearestInKDTree(this.root, new Point(x, y), best, KdTree.orientationIdx);
        return best.point;
    }

    /**
     * Get the nearest node from kdTree
     * @param current current kd tree node to examine
     * @param goal goal point
     * @param best current best distance node so far
     * @param idx comparator index
     * @return nearest point from the kd tree
     */
    private KdTreeNode nearestInKDTree(KdTreeNode current, Point goal, KdTreeNode best, int idx) {
        if (current == null) {
            return best;
        }

        if (Point.distance(current.point, goal) < Point.distance(best.point, goal)) {
            best = current;
        }

        KdTreeNode goodSide = current.right;
        KdTreeNode badSide = current.left;
        if (KdTree.comparePoints(goal, current.point, idx) < 0) {
            goodSide = current.left;
            badSide = current.right;
        }
        int newIdx = idx ^ 1;
        best = this.nearestInKDTree(goodSide, goal, best, newIdx);

        double bestPossibleDis = 0;
        if (idx == 0) {
            bestPossibleDis = Math.pow(goal.getX() - current.point.getX(), 2);
        } else {
            bestPossibleDis = Math.pow(goal.getY() - current.point.getY(), 2);
        }
        if (bestPossibleDis < Point.distance(best.point, goal)) {
            best = this.nearestInKDTree(badSide, goal, best, newIdx);
        }

        return best;
    }

    private class KdTreeNode {
        private KdTreeNode left;
        private KdTreeNode right;
        private Point point;

        private KdTreeNode(Point p) {
            this.point = p;
        }
    }
}
