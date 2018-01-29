

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class KidMathWelcome{
	public static Scene scene;

	public static void start(Stage primaryStage){
		//initialise all nodes in splash screen
		GridPane paneLoad = new GridPane();
		paneLoad.setPadding(new Insets(20,20,20,20));
	//	ImageView imgSys = new ImageView(new Image("data/img/system.png"));
		ImageView imgSys = new ImageView(getClass().getResource("system.jpg").toExternalForm());
		imgSys.setFitHeight(150);
		imgSys.setFitWidth(450);
		paneLoad.add(imgSys, 0, 0);
		scene = new Scene(paneLoad);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Kid Math Learning System");
		primaryStage.show();
		
	}

}
