


//import com.sun.prism.paint.Color;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
	public static int gen =0;
	
	
    public void start(Stage primaryStage) {
    	this.primaryStage = primaryStage;
       //creating the solar system
       solarSystem = new SolarSystem();
       mainPane = new BorderPane();
       HBox infoBox = createHBox();
       HBox sideBar = createSideBar();
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
				mouseX = event.getSceneX();
				mouseY = event.getSceneY();
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
		count += 250 ;
	
		if(count >= 3600 * 24 * 365) {
			gen++;
			count = 0;
			Shuttle best = SolarSystem.best;
			
			for(int i = 0; i < solarSystem.shuttles.length; i++) {
				if(solarSystem.shuttles[i] != null && (best == null || best.getPosition().distance(solarSystem.planets[10].getPosition()) >= solarSystem.shuttles[i].getPosition().distance(solarSystem.planets[10].getPosition())))
					best = solarSystem.shuttles[i];
			}
			System.out.println("Select " + best.init);
			System.out.println(solarSystem.bestDistance);
			solarSystem = new SolarSystem();
			for(int i = 0; i < solarSystem.shuttles.length; i++) {
				if(i==0) {
					solarSystem.shuttles[i] = new Shuttle(best.init, 1000);
				}
				else
				solarSystem.shuttles[i] = new Shuttle(best.init.sum(new Vector(Math.random() * 0.00002*solarSystem.bestDistance - 0.00001*solarSystem.bestDistance, Math.random()  * 0.00002*solarSystem.bestDistance -0.00001*solarSystem.bestDistance, Math.random()  * 0.00002*solarSystem.bestDistance - 0.00001*solarSystem.bestDistance)).normalize().multiply(1000000), 1000);
			}
			mainPane.getChildren().remove(0);
			mainPane.getChildren().add(solarSystem);
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
        launch(args);
        
    }
 
}
