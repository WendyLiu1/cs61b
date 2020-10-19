import java.io.File;
public class Body {
	final double GRAVITATIONAL_CONSTANT = 6.67e-11;
	/**
	 * Its current x position
	 */
	double xxPos;
	/**
	 * Its current y position
	 */
	double yyPos;
	/**
	 * Its current velocity in the x direction
	 */ 
	double xxVel;
	/**
	 * Its current velocity in the y direction
	 */
	double yyVel;
	/**
	 * Its mass
	 */
	double mass;
	/**
	 * The name of the file that corresponds to the image
	 * that depicts the body
	 */
	String imgFileName;

	public Body(double xP, double yP, double xV, 
					double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
	
	public Body(Body b) {
		xxPos = b.xxPos;
		yyPos = b.yyPos;
		xxVel = b.xxVel;
		yyVel = b.yyVel;
		mass = b.mass;
		imgFileName = b.imgFileName;
	}

	/**
	 * calculates the distance between two bodies
	 */
	public double calcDistance(Body b) {
		double xDiff = this.xxPos - b.xxPos;
		double yDiff = this.yyPos - b.yyPos;
		return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
	}

	/**
	 * Calculates the force exerted on this body by the given body
	 */
	public double calcForceExertedBy(Body b) {
		return (GRAVITATIONAL_CONSTANT * this.mass * b.mass) / Math.pow(this.calcDistance(b), 2);
	}

	/**
	 * Calculates the force exerted in the X direction
	 */
	public double calcForceExertedByX(Body b) {
		return (this.calcForceExertedBy(b) * (b.xxPos - this.xxPos)) / this.calcDistance(b);
	}
	
	/**
	 * Calculates the force exerted in the Y direction
	 */
	public double calcForceExertedByY(Body b) {
		return (this.calcForceExertedBy(b) * (b.yyPos - this.yyPos)) / this.calcDistance(b);
	}

	/**
	 * Calculates the net X force exerted by all bodies in the input array
	 */
	public double calcNetForceExertedByX(Body[] bodies) {
		double netForce = 0;
		for (Body b : bodies) {
			if (!this.equals(b)) {
				netForce += this.calcForceExertedByX(b);
			}
		}
		return netForce;
	}

	/**
	 * Calculates the net Y force exerted by all bodies in the input array
	 */
	public double calcNetForceExertedByY(Body[] bodies) {
		double netForce = 0;
		for (Body b : bodies) {
			if (!this.equals(b)) {
				netForce += this.calcForceExertedByY(b);
			}
		}
		return netForce;
	}

	/**
	 * Update the body's position and velocity based on forces exerted on the body
	 */
	public void update(double time, double forceX, double forceY) {
		double xAcc = forceX / mass;
		double yAcc = forceY / mass;
		xxVel = xxVel + time * xAcc;
		yyVel = yyVel + time * yAcc;
		xxPos = xxPos + time * xxVel;
		yyPos = yyPos + time * yyVel;
	}

	public void draw() {
		String imgFolder = "images";
		String imgFile = imgFolder + File.separator + this.imgFileName;
		StdDraw.picture(this.xxPos, this.yyPos, imgFile);
	}
}
