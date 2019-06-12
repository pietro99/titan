import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;


public class Lander extends Group {
	Line leg1;
	Line leg2;
	Circle shuttleBody;
    Circle leftThruster;
    Circle rightThruster;
    Circle titan;
    Vector shuttleLayout;
    Vector shuttleOldLayout;
    Vector shuttleOldPosition;
    Vector shuttlePosition;
    private static final boolean DEBUG = true; 
    int counter =0;
    Shape rocketTemp;
    Shape rocket;

    public Lander(Shuttle shuttle, Planet titan) {
    	shuttlePosition = shuttle.getPosition();
    	double distance = shuttlePosition.distance(titan.getPosition());
    	
    	this.titan = new Circle(titan.getRadius());
    	this.titan.setCenterX(0);
    	this.titan.setCenterY(0);
    	shuttleLayout = new Vector(this.titan.getCenterX(), this.titan.getCenterY()-distance, 0);
    	shuttleBody = new Circle(22);
    	shuttleBody.setFill(Color.rgb(169, 181, 201));
    	leg1 = new Line(shuttleBody.getCenterX(), shuttleBody.getCenterY(),shuttle.getDirection(2).normalize().getX()-15, shuttleBody.getCenterY()+25);
    	leg2 = new Line(shuttleBody.getCenterX(), shuttleBody.getCenterY(),shuttle.getDirection(2).normalize().getX()+15, shuttleBody.getCenterY()+25);
    	leg1.setFill(Color.rgb(199, 205, 216));
    	leg2.setFill(Color.rgb(199, 205, 216));
    	rocketTemp = Shape.union(shuttleBody,leg2);
        rocket = Shape.union(rocketTemp,leg1);
        rocket.setLayoutX(shuttlePosition.getX());
        rocket.setLayoutY(shuttlePosition.getY());
        this.titan.setFill(Color.WHITE);
        this.titan.setLayoutX(0);
        this.titan.setLayoutY(titan.getRadius());
        
        rocket.fillProperty().set(Color.WHITE);
        getChildren().add(rocket);
        getChildren().add(this.titan);
        shuttleOldPosition = shuttlePosition;
        shuttleOldLayout = shuttleLayout;
    	this.titan.setFill(Color.rgb(214, 190, 119));
      
    }

    public void buildScene(Shuttle shuttle, Planet titan){
    	shuttlePosition = shuttle.getPosition();
    	Vector differencePosition = shuttleOldPosition.subtract(shuttlePosition);
    	Vector distance = shuttle.getPosition().subtract(titan.getPosition());
    	
    	shuttleLayout = shuttleOldLayout.sum(differencePosition);

    	rocket.setLayoutX(shuttleLayout.getX()/SolarSystemGUI.getScale()*100);
        rocket.setLayoutY(shuttleLayout.getY()/SolarSystemGUI.getScale()*100);
        //rocket.setRotate((180 / Math.PI) * shuttle.getAngle2D(distance.multiply(-1)));
    	if(counter%1000==0 && DEBUG) {
        	System.out.println("distance: "+distance);
        	System.out.println("difference: "+differencePosition);
        	System.out.println("position: "+shuttlePosition);
            System.out.println("Layout: "+rocket.getLayoutX()+" "+rocket.getLayoutY());
            System.out.println();

    	}
        shuttleOldLayout = shuttleLayout;	
        counter++;
        shuttleOldPosition = shuttlePosition;
 }
}
