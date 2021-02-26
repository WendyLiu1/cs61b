package bearmaps;
import java.util.ArrayList;
import java.util.List;

public class NaivePointSet implements PointSet {

    private List<Point> pointSet;

    public NaivePointSet(List<Point> points) {
        this.pointSet = new ArrayList<>();
        for (Point p : points) {
            this.pointSet.add(new Point(p.getX(), p.getY()));
        }

    }

    @Override
    public Point nearest(double x, double y) {
        if (this.pointSet == null || this.pointSet.size() == 0) {
            return null;
        }
        Point currentPos = new Point(x, y);
        Point nearestPoint = this.pointSet.get(0);
        double minDis = Point.distance(currentPos, nearestPoint);

        for (Point p : this.pointSet) {
            double distant = Point.distance(currentPos, p);
            if (distant < minDis) {
                minDis = distant;
                nearestPoint = p;
            }
        }
        return nearestPoint;
    }
}
