

//import com.sun.prism.paint.Color;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Runner extends Application{
    public static SolarSystem solarSystem;
    public static double mouseX = 0;
    public static double mouseY = 0;
    public static Stage primaryStage;
    public static BorderPane mainPane;
    public static double frameSeconds = 0.1;
    public static int count = 0;
    public static int counter = 0;
    public boolean isLanding = false;
    public static int gen =0;
    public static boolean simulation = false;
    public static boolean followTitan = false;
    public static boolean followSaturn = false;
    public static boolean followShuttle = false;
    public static boolean followEarth = false;
    double factor = 1.9;
    Vector oldPos;
    Vector oldTitan;
    Shuttle shuttle;
    Vector bestPos;
    Vector bestTitan;
    double err;
    double oldErr;
    List<Circle>shuttleCir = new ArrayList<Circle>();
    PerspectiveCamera camera;
    Lander lander;
    Timeline timeline;

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //creating the solar system
        solarSystem = new SolarSystem();
        mainPane = new BorderPane();
        // HBox infoBox = createHBox();
        // HBox sideBar = createSideBar();
        camera = new PerspectiveCamera();

        camera.setNearClip(0.000001);
        mainPane.getChildren().add(solarSystem.getGUI());
        //mainPane.setBottom(infoBox);
        //mainPane.setRight(sideBar);

        Scene scene = new Scene(mainPane, 1000, 850, true);

        scene.setCamera(camera);
        scene.setFill(Color.BLACK);

        Light.Point light = new Light.Point();
        light.setX(0);
        light.setY(0);
        light.setZ(0);
        light.setColor(Color.RED);
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        for(int i= 0; i<solarSystem.getGUI().getPlanetSpheres().length; i++)
            solarSystem.getGUI().getPlanetSpheres()[i].setEffect(lighting);
        solarSystem.getGUI().setRotationAxis(Rotate.X_AXIS);


        //game loop......
        /*Timeline*/ timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(
                Duration.millis(frameSeconds),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent ae) {
                        //update the frame

                        update();
                    }
                });
        timeline.getKeyFrames().add(kf);
        timeline.play();
        //.................



        //handlers for moving the map with the mouse
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                double Xdifference = mouseX-event.getX();
                double Ydifference = mouseY-event.getY();
                solarSystem.getGUI().setMovingFactor(solarSystem.getGUI().getMovingFactor().getX()-(Xdifference/25), solarSystem.getGUI().getMovingFactor().getY()-(Ydifference/25));
            }
        });
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            }
        });



        //handler for reaching different planets with keyboard strokes
        scene.setOnKeyPressed((new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                if(code == KeyCode.W) {
                    camera.translateZProperty().set(camera.getTranslateZ()+20);
                }
                else if(code == KeyCode.S) {
                    camera.translateZProperty().set(camera.getTranslateZ()-20);
                }
                else if(code == KeyCode.LEFT) {
                    camera.translateXProperty().set(camera.getTranslateX()-20);
                }
                else if(code == KeyCode.RIGHT) {
                    camera.translateXProperty().set(camera.getTranslateX()+20);
                }
                else if(code == KeyCode.UP) {
                    camera.translateYProperty().set(camera.getTranslateY()-20);
                }
                else if(code == KeyCode.DOWN) {
                    camera.translateYProperty().set(camera.getTranslateY()+20);
                }
                else if(code == KeyCode.T) {
                    camera.setTranslateX(0);
                    camera.setTranslateY(0);
                    camera.setTranslateZ(0);
                    if(!followTitan) {
                        followTitan = true;
                        followShuttle = false;
                        followSaturn = false;
                        followEarth = false;
                    }
                    else
                        followTitan = false;
                }
                else if(code == KeyCode.R) {
                    camera.setTranslateX(0);
                    camera.setTranslateY(0);
                    camera.setTranslateZ(0);
                    if(!followShuttle) {
                        followShuttle = true;
                        followSaturn = false;
                        followTitan = false;
                        followEarth = false;
                    }
                    else
                        followShuttle = false;
                }
                else if(code == KeyCode.U) {
                    camera.setTranslateX(0);
                    camera.setTranslateY(0);
                    camera.setTranslateZ(0);
                    if(!followSaturn) {
                        followSaturn = true;
                        followShuttle = true;
                        followTitan = false;
                        followEarth = false;
                    }
                    else
                        followSaturn = false;
                }
                else if(code == KeyCode.E) {
                    if(!followSaturn) {
                        followSaturn = false;
                        followShuttle = false;
                        followTitan = false;
                        followEarth = true;
                    }
                    else
                        followEarth = false;
                }
                else if(code == KeyCode.A) {
                    solarSystem.getGUI().setRotate(solarSystem.getGUI().getRotate()+2);
                }
                else if(code == KeyCode.D) {
                    solarSystem.getGUI().setRotate(solarSystem.getGUI().getRotate()-2);
                }
                else if(code == KeyCode.DIGIT0) {
                    solarSystem.getGUI().setRotate(0);
                }
                else if(code == KeyCode.DOWN) {
                    setFrameSeconds(getFrameSeconds()*0.0001);

                }

                else if(code == KeyCode.C) {

                    solarSystem.getGUI().setScale(4e5);
                    solarSystem.getGUI().setMovingFactor(500,525);
                    camera.setTranslateX(0);
                    camera.setTranslateY(0);
                    camera.setTranslateZ(0);
                }
                else if(code == KeyCode.F)
                    fullScreen();
            }
        }));


        //handler for scaling with the mouse roll (or trackpad or touchscreen)
        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            public void handle(ScrollEvent event) {
                if(!isLanding) {
                    if(event.getDeltaY()>0)
                        solarSystem.getGUI().setScale(solarSystem.getGUI().getScale()*0.9);
                    else
                        solarSystem.getGUI().setScale(solarSystem.getGUI().getScale()*1.1);
                }
            }
        });



        primaryStage.setTitle("Mission To Titan");
        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    public HBox createHBox() {
        HBox infoBox = new HBox();
        infoBox.setPrefSize(0, 60);
        infoBox.setStyle("-fx-background-color: #336699;");
        Label time = new Label("time: ");
        Label scale = new Label("scale: ");
        infoBox.getChildren().addAll(time, scale);

        return infoBox;

    }

    public HBox createSideBar() {
        HBox infoBox = new HBox();
        infoBox.setPrefSize(0, 60);
        infoBox.setStyle("-fx-background-color: #336699;");
        Label time = new Label("time: ");
        Label scale = new Label("scale: ");
        infoBox.getChildren().addAll(time, scale);

        return infoBox;

    }

    public void fullScreen() {
        primaryStage.setFullScreen(true);
    }


    //method that updates the frame
    public void update() {
        solarSystem.updateSolarSystem();
        fixCamera();
        if(solarSystem.getTitan().getPosition().distance(solarSystem.getShuttle().getPosition())<=1000000) {
            //if(solarSystem.getShuttle().isLanding()) {
            timeline.setRate(.001);
            if(counter==0) {
                isLanding = true;
                //solarSystem.TIME = solarSystem.TIME/4000; //->done in land method
                solarSystem.getGUI().setScale(4e5);
                lander = new Lander(solarSystem.getShuttle(), solarSystem.getTitan());
                mainPane.getChildren().remove(solarSystem.getGUI());
                mainPane.getChildren().add(lander);
                camera.setTranslateX(-500);
                camera.setTranslateY(-500);
                camera.setLayoutX(0);
                camera.setLayoutY(0);
                followTitan = false;
                followSaturn = false;
                followShuttle = false;
                followEarth = false;
                counter++;

            }
            lander.buildScene(solarSystem.getShuttle(), solarSystem.getTitan());
        }

        count += 250 ;

        if(simulation) {
            if(count >= 3600 * 24 * 365/6) {
                gen++;

                shuttle = solarSystem.getShuttle();
                bestPos = shuttle.getPosition();
                //192417.8004932324 -925027.0853926808 -558.466505544255
                bestTitan = solarSystem.getTitan().getPosition();
                solarSystem = new SolarSystem();
                err = shuttle.getPosition().distance(solarSystem.getTitan().getPosition());
                //1.313989273851E9
                if(oldErr>err) {
                    factor+=0.01;
                }

                Vector correction = bestTitan.subtract(bestPos);// 80619.14074576352

                double initX = shuttle.init.getX();
                double initY = shuttle.init.getY();
                double initZ = shuttle.init.getZ();
                double scaling = Math.pow(err, (0.32+(count/7e8))*factor);
                double addX = correction.getX()/scaling;
                double addY = correction.getY()/scaling;
                double addZ = correction.getZ()/scaling;
                double newX = initX + addX;
                double newY = initY + addY;
                double newZ = initZ + addZ;
                solarSystem.setShuttle(shuttle);
                System.out.println(err);
                System.out.println(solarSystem.shuttle.getPosition().distance(solarSystem.getTitan().getPosition()));

                shuttle = new Shuttle(new Vector(newX, newY, newZ), 1000);//1.313989273851E9  13.432187276681386 -10.572101352235507 -2.7413417329502545


                oldErr = err;

                mainPane.getChildren().clear();
                mainPane.getChildren().add(solarSystem.getGUI());


                System.out.println("Select: " + shuttle.init);
                System.out.println("Position: " + solarSystem.bestPos);
                System.out.println("Titan: " + solarSystem.bestTitan);
                System.out.println("Time: " + solarSystem.bestTime);
                System.out.println("Correction: " + correction);
                System.out.println("Error: " + err);
                //System.out.println(SolarSystem.shuttle.getPosition().subtract(SolarSystem.planets[10].getPosition()).length());
                count = 0;
            }
        }

        //878629.7264863193	     -2588414.9687429797
    }//4.1155308720947456E8 -1.4453512119138913E9 8755575.60203173
    //4.091557040724596E8   -1.4462529557723603E9 8726104.776757749

    private void fixCamera() {

        if(followTitan) {
            camera.setLayoutX(solarSystem.getGUI().getPlanetSpheres()[10].getLayoutX()-solarSystem.getGUI().getMovingFactor().getX());
            camera.setLayoutY(solarSystem.getGUI().getPlanetSpheres()[10].getLayoutY()-solarSystem.getGUI().getMovingFactor().getY());
        }
        else if(followSaturn){
            camera.setLayoutX(solarSystem.getGUI().getPlanetSpheres()[6].getLayoutX()-solarSystem.getGUI().getMovingFactor().getX());
            camera.setLayoutY(solarSystem.getGUI().getPlanetSpheres()[6].getLayoutY()-solarSystem.getGUI().getMovingFactor().getY());
        }
        else if(followShuttle){
            camera.setLayoutX(solarSystem.getGUI().getShuttleSphere().getLayoutX()-solarSystem.getGUI().getMovingFactor().getX());
            camera.setLayoutY(solarSystem.getGUI().getShuttleSphere().getLayoutY()-solarSystem.getGUI().getMovingFactor().getY());
        }
        else if(followEarth){
            camera.setLayoutX(solarSystem.getGUI().getPlanetSpheres()[3].getLayoutX()-solarSystem.getGUI().getMovingFactor().getX());
            camera.setLayoutY(solarSystem.getGUI().getPlanetSpheres()[3].getLayoutY()-solarSystem.getGUI().getMovingFactor().getY());
        }
    }


    public static double getHeight() {
        return primaryStage.getHeight();
    }
    public static double getWidth() {
        return primaryStage.getWidth();
    }
    public static void setFrameSeconds(double time) {
        frameSeconds = time;
    }
    public static double getFrameSeconds() {
        return frameSeconds;
    }


    public static void main(String[] args) {
        //call the start method

        if(args.length>=1 && args[0].equals("simulation"))
            simulation = true;

        launch(args);

    }

}
