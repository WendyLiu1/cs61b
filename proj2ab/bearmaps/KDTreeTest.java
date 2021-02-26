package bearmaps;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KDTreeTest {
    private static Random r = new Random();

    @Test
    public void NaivePointSetTest() {
        Point p1 = new Point(1.1, 2.2);
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        List<Point> points = List.of(p1, p2, p3);
        NaivePointSet np = new NaivePointSet(points);
        KdTree kd = new KdTree(points);

        Point kdNearest = np.nearest(3.0, 4.0);
        assertEquals(kdNearest, p2);

        Point npNearest = kd.nearest(3.0, 4.0);
        assertEquals(npNearest, p2);
    }

    @Test
    public void KDTreeSimpleTest() {
        Point p1 = new Point(2, 3);
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);

        List<Point> points = List.of(p1, p2, p3, p4, p5, p6, p7);
        KdTree kd = new KdTree(points);
        PointSet np = new NaivePointSet(points);

        Point kdNearest = kd.nearest(0, 7);
        assertEquals(kdNearest, p6);

        Point npNearest = np.nearest(0, 7);
        assertEquals(npNearest, p6);
    }

    @Test
    public void randomPointSetTest() {

        List<Point> points = this.generateRandomPoints(1000);
        PointSet np = new NaivePointSet(points);
        PointSet kd = new KdTree(points);

        List<Point> queryPoints = this.generateRandomPoints(1000);
        for (Point p : queryPoints){
            Point expected = np.nearest(p.getX(), p.getY());
            Point kdNearest = kd.nearest(p.getX(), p.getY());
            assertEquals(kdNearest, expected);
        }
    }

    /**
     * Generate N random points
     * @param N
     * @return
     */
    private List<Point> generateRandomPoints(int N) {
        List<Point> points = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            points.add(this.generateRandomPoint());
        }
        return points;
    }

    private Point generateRandomPoint() {
        double x = r.nextDouble();
        double y = r.nextDouble();
        return new Point(x, y);
    }
}
