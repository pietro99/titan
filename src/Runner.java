import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Runner extends Application{
    public static SolarSystem solarSystem;
    public static double mouseX = 0;
    public static double mouseY = 0;
    public static Stage primaryStage;
    public static double frameSeconds = 0.1;
    VBox sideBar;


    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        //creating the solar system
        solarSystem = new SolarSystem();
        BorderPane mainPane = new BorderPane();
        HBox infoBox = createHBox();
        sideBar = createSideBar();
        mainPane.getChildren().add(solarSystem);
        mainPane.setBottom(infoBox);
        mainPane.setRight(sideBar);

        Scene scene = new Scene(mainPane, 1000, 850, true);


        //game loop......
        Timeline timeline = new Timeline();
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
                solarSystem.setMovingFactor(solarSystem.getMovingFactor().getX()-(Xdifference/25), solarSystem.getMovingFactor().getY()-(Ydifference/25));
            }
        });
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                update();
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();


                for (int i = 0; i < SolarSystem.getPlanetCircles().length; i++){

                    Circle tempCircle = new Circle(SolarSystem.getPlanetCircles()[i].getLayoutX(), SolarSystem.getPlanetCircles()[i].getLayoutY(), SolarSystem.getPlanetCircles()[i].getRadius()+10);

                    if(tempCircle.contains(mouseX,mouseY)){
                       // System.out.println("IT works" );
                        // System.out.println(SolarSystem.getPlanetCircles()[i].toString());
                        Planet clickedPlanet = SolarSystem.getPlanets()[i];

                        sideBar = updateSidebar(clickedPlanet);

                    }
                }

            }

        });



        //handler for reaching different planets with keyboard strokes
        scene.setOnKeyPressed((new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                if(code == KeyCode.S) {
                    solarSystem.setScale(72992.01452560296);
                    solarSystem.setMovingFactor(-4365.039999999958 , 20464.560000000056);
                }
                else if(code == KeyCode.DOWN) {
                    setFrameSeconds(getFrameSeconds()*0.0001);
                }
                else if(code == KeyCode.C) {

                    solarSystem.setScale(4e6);
                    solarSystem.setMovingFactor(500,525);
                }
                else if(code == KeyCode.F)
                    fullScreen();
            }
        }));


        //handler for scaling with the mouse roll (or trackpad or touchscreen)
        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            public void handle(ScrollEvent event) {
                if(event.getDeltaY()>0)
                    solarSystem.setScale(solarSystem.getScale()*0.9);
                else
                    solarSystem.setScale(solarSystem.getScale()*1.1);
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



    public VBox createSideBar() {
        VBox infoBox = new VBox();
        infoBox.setPrefSize(200, 60);
        infoBox.setStyle("-fx-background-color: #336699;");

        ObservableList<String> planetsOption =
                FXCollections.observableArrayList(
                        SolarSystem.getPlanet(0).getName(),
                        SolarSystem.getPlanet(1).getName(),
                        SolarSystem.getPlanet(2).getName(),
                        SolarSystem.getPlanet(3).getName(),
                        SolarSystem.getPlanet(4).getName(),
                        SolarSystem.getPlanet(5).getName(),
                        SolarSystem.getPlanet(6).getName(),
                        SolarSystem.getPlanet(7).getName(),
                        SolarSystem.getPlanet(8).getName(),
                        SolarSystem.getPlanet(9).getName(),
                        SolarSystem.getPlanet(10).getName()

                );
        final ComboBox comboBox = new ComboBox(planetsOption);
       // selectedItem = comboBox.getValue().toString();

        infoBox.getChildren().add(comboBox);
        return infoBox;

    }


//String selectedItem;

    public VBox updateSidebar(Planet planet) {
        VBox infoBox = sideBar;

        infoBox.getChildren().clear();


        ObservableList<String> planetsOption =
                FXCollections.observableArrayList(
                        SolarSystem.getPlanet(0).getName(),
                        SolarSystem.getPlanet(1).getName(),
                        SolarSystem.getPlanet(2).getName(),
                        SolarSystem.getPlanet(3).getName(),
                        SolarSystem.getPlanet(4).getName(),
                        SolarSystem.getPlanet(5).getName(),
                        SolarSystem.getPlanet(6).getName(),
                        SolarSystem.getPlanet(7).getName(),
                        SolarSystem.getPlanet(8).getName(),
                        SolarSystem.getPlanet(9).getName(),
                        SolarSystem.getPlanet(10).getName()

                );
        final ComboBox comboBox = new ComboBox(planetsOption);
       /* if(comboBox.getValue() != null ) {
            String output = comboBox.getValue().toString();
            System.out.println("output: " + output);
        }
        else{
            System.out.println("nope");
        } */
        //selectedItem = comboBox.getValue().toString();
        comboBox.setValue(planet.getName());
        Label size = new Label("Size = " + planet.getRadius());
        Label mass = new Label("Mass = " + planet.getMass());
        Label position = new Label("Position is: " + planet.getPosition());
        Label acceleration = new Label("Accelaration = " + planet.getAcceleration());
        Label velocity = new Label("Velocity =" + planet.getVelocity());

        infoBox.getChildren().addAll(comboBox,size, mass, position, acceleration, velocity);

        return infoBox;
    }


    public void fullScreen() {
        primaryStage.setFullScreen(true);
    }


    //method that updates the frame
    public void update() {
        solarSystem.updateSolarSystem();
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
        launch(args);

    }

}