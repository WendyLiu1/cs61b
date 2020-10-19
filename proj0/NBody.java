import java.io.File;
public class NBody {
	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);

		String filename = args[2];
		Body[] bodies = readBodies(filename);
		double radius = readRadius(filename);

		String imgFolder = "images";
		String backgroundImg = imgFolder + File.separator + "starfield.jpg";
		StdDraw.enableDoubleBuffering();
		StdDraw.setXscale(-radius, radius);
		StdDraw.setYscale(-radius, radius);
		StdDraw.picture(0, 0, backgroundImg); 

		double time = 0;
		int numPlanets = bodies.length;
		double[] xForces = new double[numPlanets];
		double[] yForces = new double[numPlanets];
		while (time <= T) {
			for (int i = 0; i < numPlanets; i++) {
				Body b = bodies[i];
				xForces[i] = b.calcNetForceExertedByX(bodies);
				yForces[i] = b.calcNetForceExertedByY(bodies);
			}
			for (int i = 0; i < numPlanets; i++) {
				Body b = bodies[i];
				b.update(dt, xForces[i], yForces[i]); 
			}
			StdDraw.clear();

			StdDraw.picture(0, 0, backgroundImg); 

			for (Body b : bodies) {
				b.draw();
			}

			StdDraw.show();
			StdDraw.pause(10);
			time += dt;
		}
		StdOut.printf("%d\n", bodies.length);
		StdOut.printf("%.2e\n", radius);
		for (Body body : bodies) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
					body.xxPos, body.yyPos, body.xxVel,
					body.yyVel, body.mass, body.imgFileName);
		}
	}
	public static double readRadius(String fileName) {
		In in = new In(fileName);
		int numPlanets = in.readInt();
		return in.readDouble();
	}

	public static Body[] readBodies(String fileName) {
		In in = new In(fileName);
		int numPlanets = in.readInt();
		double radius = in.readDouble();
		Body[] bodiesInUniverse = new Body[numPlanets];
		int idx = 0;
		/* Keep looking until the file is empty. */
		while (idx < numPlanets) {
			/* Each line has x,y coordinates, then x- and
			 * y-components of initial velocity, then mass,
			 * then name of the image file. */
			double xPos = in.readDouble();
			double yPos = in.readDouble();
			double xVel = in.readDouble();
			double yVel = in.readDouble();
			double mass = in.readDouble();
			String img = in.readString();
			bodiesInUniverse[idx++] = new Body(xPos, yPos,
					xVel, yVel, mass, img);
		}
		return bodiesInUniverse;
	}
}
