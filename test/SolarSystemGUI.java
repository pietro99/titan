import javafx.scene.Group;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class SolarSystemGUI extends Group {
    private  Sphere[] planetSpheres = new Sphere[11];
    private SolarSystem solarSystem;
    private  Sphere shuttleSphere = new Sphere(1);

    private static double scale = 4e5;//scaling factor
    private static Vector movingFactor = new Vector(500,525,0);

    public SolarSystemGUI(SolarSystem solarSystem){
        this.solarSystem = solarSystem;
        initiateGUIPosition();

    }
    public void updateGUI() {
        for(int i=0; i<solarSystem.getPlanets().length; i++) {
            planetSpheres[i].setLayoutX((solarSystem.getPlanet(i).getPosition().getX()/scale)+movingFactor.getX());
            planetSpheres[i].setLayoutY((solarSystem.getPlanet(i).getPosition().getY()/scale)+movingFactor.getY());
        }
        if(solarSystem.getShuttle() != null) {
            shuttleSphere.setLayoutX((solarSystem.getShuttle().getPosition().getX() / scale) + movingFactor.getX());
            shuttleSphere.setLayoutY((solarSystem.getShuttle().getPosition().getY() / scale) + movingFactor.getY());
        }
    }

    private void initiateGUIPosition() {
        //Spheres for the GUI
        PhongMaterial earthmt = new PhongMaterial();
        Image imgEarth = new Image("earth.jpg");
        earthmt.setDiffuseMap(imgEarth);
        PhongMaterial jupitermt = new PhongMaterial();
        Image imgJupiter = new Image("jupiter.jpg");
        jupitermt.setDiffuseMap(imgJupiter);
        PhongMaterial venusmt = new PhongMaterial();
        Image imgVenus = new Image("venus.jpg");
        venusmt.setDiffuseMap(imgJupiter);
        PhongMaterial uranusmt = new PhongMaterial();
        Image imgUranus = new Image("uranus.jpg");
        uranusmt.setDiffuseMap(imgUranus);
        PhongMaterial sunmt = new PhongMaterial();
        Image imgSun = new Image("sun.jpg");
        sunmt.setSelfIlluminationMap(imgSun);
        sunmt.setDiffuseMap(imgSun);
        PhongMaterial marsmt = new PhongMaterial();
        Image imgMars = new Image("mars.jpg");
        marsmt.setDiffuseMap(imgMars);
        PhongMaterial mercurymt = new PhongMaterial();
        Image imgMercury = new Image("mercury.jpg");
        mercurymt.setDiffuseMap(imgMercury);
        PhongMaterial saturnmt = new PhongMaterial();
        Image imgSaturn = new Image("saturn.jpg");
        saturnmt.setDiffuseMap(imgSaturn);
        PhongMaterial neptunemt = new PhongMaterial();
        Image imgNeptune = new Image("neptune.jpg");
        neptunemt.setDiffuseMap(imgNeptune);
        PhongMaterial moonmt = new PhongMaterial();
        Image imgMoon = new Image("moon.jpg");
        moonmt.setDiffuseMap(imgMoon);
        PhongMaterial titanmt = new PhongMaterial();
        Image imgTitan = new Image("titan.jpg");
        titanmt.setDiffuseMap(imgTitan);



        Light.Point light = new Light.Point();
        light.setX(0);
        light.setY(0);
        light.setZ(0);;
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        planetSpheres[0] = new Sphere(solarSystem.getSun().getRadius() /10000);
        planetSpheres[1] = new Sphere(solarSystem.getMercury().getRadius() /1000);
        planetSpheres[2] = new Sphere(solarSystem.getVenus().getRadius() /1000);
        planetSpheres[3] = new Sphere(solarSystem.getEarth().getRadius() /1000);
        planetSpheres[4] = new Sphere(solarSystem.getMars().getRadius() /1000);
        planetSpheres[5] = new Sphere(solarSystem.getJupiter().getRadius() /1000);
        planetSpheres[6] = new Sphere(solarSystem.getSaturn().getRadius() /100000);
        planetSpheres[7] = new Sphere(solarSystem.getUranus().getRadius() /1000);
        planetSpheres[8] = new Sphere(solarSystem.getNeptune().getRadius() /1000);
        planetSpheres[9] = new Sphere(solarSystem.getMoon().getRadius() /1000);
        planetSpheres[10] = new Sphere(solarSystem.getTitan().getRadius() /1000);

        //settings the textures of the planets
        planetSpheres[0].setMaterial(sunmt);
        planetSpheres[1].setMaterial(mercurymt);
        planetSpheres[2].setMaterial(venusmt);
        planetSpheres[3].setMaterial(earthmt);
        planetSpheres[4].setMaterial(marsmt);
        planetSpheres[5].setMaterial(jupitermt);
        planetSpheres[6].setMaterial(saturnmt);
        planetSpheres[7].setMaterial(uranusmt);
        planetSpheres[8].setMaterial(neptunemt);
        planetSpheres[9].setMaterial(moonmt);
        planetSpheres[10].setMaterial(titanmt);


        shuttleSphere = new Sphere(2);
//		shuttleSphere.fillProperty().set(Color.RED);


        // add the Spheres to the SolarSystem group
        for(int i=0; i<planetSpheres.length; i++)
            getChildren().add(planetSpheres[i]);

        getChildren().add(shuttleSphere);
    }


    //getters and setters

    public Sphere getShuttleSphere() {
        return shuttleSphere;
    }
    public Sphere[] getPlanetSpheres() {
        return planetSpheres;
    }
    public void setScale(double scale) {
        this.scale = scale;
    }
    public static double getScale() {
        return scale;
    }
    public void setMovingFactor(double x, double y) {
        movingFactor = new Vector(x,y,0);
    }
    public static Vector getMovingFactor() {
        return movingFactor;
    }
}
