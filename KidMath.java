//Liew Yu Hung 032s5770
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

public class KidMath extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage){
		//start spash screen for 0.7 seconds before login screen
		KidMathWelcome.start(primaryStage);
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.7),e->{
			KidMathLogin.start(primaryStage);
		}));
		timeline.play();
		
		
	}

}
