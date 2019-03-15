
public class Vector {
	private double X;
	private double Y;
	private double Z;
	
	
	public Vector(double PosX, double PosY, double PosZ) {
		this.setX(PosX);
		this.setY(PosY);
		this.setZ(PosZ);
	}
	
	//distance between this vector and vector v.
	public double distance(Vector v) {
		double distance = Math.sqrt(Math.pow(v.getX()-this.X, 2)+Math.pow(v.getY()-this.Y, 2)+Math.pow(v.getZ()-this.Z, 2));
		return distance;
	}
	public double getZ() {
		return Z;
	}
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
	

}
