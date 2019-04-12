
public class Vector {
	public static final Vector ZERO = new Vector(0, 0, 0);

	private double X;
	private double Y;
	private double Z;
	
	public Vector(double PosX, double PosY, double PosZ) {
		this.setX(PosX);
		this.setY(PosY);
		this.setZ(PosZ);
	}

	public Vector(Vector v) {
		this.setX(v.getX());
		this.setY(v.getY());
		this.setZ(v.getZ());
	}
	
	//distance between this vector and vector v.
	public double distance(Vector v) {
		double distance = Math.sqrt(Math.pow(v.getX()-this.X, 2)+Math.pow(v.getY()-this.Y, 2)+Math.pow(v.getZ()-this.Z, 2));
		return distance;
	}
	
	//sun between this vector and vector v.
	public Vector sum(Vector v) {
		Vector sum = new Vector(X+v.getX(),Y+v.getY(),Z+v.getZ());
		return sum;
	}
	
	//difference between this vector and vector v.
	public Vector subtract(Vector v) {
		Vector subtract = new Vector(X-v.getX(),Y-v.getY(),Z-v.getZ());
		return subtract;
	}
	
	public double length() {
		return Math.sqrt(squareLength());
	}

	public double squareLength() {
		return X*X + Y*Y + Z*Z;
	}

	public Vector multiply(double factor) {
		return new Vector(X * factor, Y * factor, Z * factor);
	}

	public double dot(Vector v) {
		return X * v.getX() + Y * v.getY() + Z * v.getZ();
	}

	public Vector cross(Vector v) {
		//from cross product formula
		return new Vector(Y * v.getZ() - Z * v.getY(), Z * v.getX() - X * v.getZ(), X * v.getY() - Y * v.getX());
	}

	public Vector rotate(Vector axis, double rad) {
		//Euler angle	-> gimball lock, we need to calculate the three angles
		//Axis - angle	-> we already have the direction and the angle (from torque momentum)
		//Quaternion rotation	-> no gimball lock, but we need a new class for quaternions

		//Axis - angle (from formula)
		Vector v = axis.normalize();
		return	this.multiply(Math.cos(rad)).sum(v.cross(this).multiply(Math.sin(rad))).sum(v.multiply(v.dot(this) * (1 - Math.cos(rad))));
	}

	public Vector normalize() {
		return new Vector(multiply(1 / length()));
	}

	public double getZ() { return Z; }
	public void setZ(double z) {
		Z = z;
	}
	public double getY() {
		return Y;
	}
	public void setY(double y) {
		Y = y;
	}
	public double getX() {
		return X;
	}
	public void setX(double x) {
		X = x;
	}
	public String toString() {
		return X+" "+Y+" "+Z;
	}
	public void set(double x, double y, double z) {
		X = x;
		Y = y;
		Z = z;
	}
}
