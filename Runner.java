import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
 
public class Runner extends Application{
	public static SolarSystem solarSystem;
	public static double mouseX = 0;
	public static double mouseY = 0;
	
	
    public void start(Stage primaryStage) {
    	
       //creating the solar system
       solarSystem = new SolarSystem();
       Scene scene = new Scene(solarSystem, 1000, 850, true);
       
       
       //game loop......
       Timeline timeline = new Timeline();
       timeline.setCycleCount(Timeline.INDEFINITE);
       KeyFrame kf = new KeyFrame(
               Duration.millis(0.1),
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
				else if(code == KeyCode.C) {
					solarSystem.setScale(4E6);
					solarSystem.setMovingFactor(500, 525);
				}
				else if(code == KeyCode.E) {
					solarSystem.setScale(27005.45381686715);
					solarSystem.setMovingFactor(6004.33999999998, 498.2600000000017);
				}
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
       primaryStage.show();
    }
    
    
    
    
    //method that updates the frame
    public void update() {
    	solarSystem.updateSolarSystem();
    }
    
    
    
    
 public static void main(String[] args) {
	 	//call the start method
        launch(args);
        
    }
 
}
