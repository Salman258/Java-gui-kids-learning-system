

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class KidMathCreateChild {
	final static String filename = "data/Accounts.txt";
	private static File selectedFile;
	public static Scene profileScene;
	private static boolean validated;
	private static File destination;
	private static String profileImagePath;
	
	public static void start(Stage primaryStage){
		//initialise all nodes in edit child account page
		FileChooser fileChooser = new FileChooser();
		GridPane paneCreate = new GridPane();
		Label lblCreate = new Label("Create Child Account");
		Label lblCreateUser = new Label("Username : (6-20) Characters");
		Label lblCreatePass = new Label("Password : (6-12) Characters");
		Label lblCreateConfirmPass = new Label("Enter password again :");
		Label lblCreateEmail = new Label ("Email :");
		Label lblCreateConfirmEmail = new Label("Enter email again :");
		Label lblCreateImage = new Label("Profile picture : (optional)");
		Label lblCreateLevel = new Label("Level :");
		Label lblCreateError = new Label("");
		TextField txtCreateUser = new TextField();
		PasswordField txtCreatePass = new PasswordField();
		PasswordField txtCreateConfirmPass = new PasswordField();
		TextField txtCreateEmail = new TextField();
		TextField txtCreateConfirmEmail = new TextField();
		TextField txtCreateLevel = new TextField();
		Button btnCreateChooseImg = new Button("Choose image...");
		Button btnCreate = new Button("Create");
		Button btnBack = new Button("Back");
		
		paneCreate.setPadding(new Insets(20,20,20,20));
		paneCreate.setVgap(5);
		paneCreate.setHgap(5);
		paneCreate.add(lblCreate, 0, 0);
		paneCreate.add(lblCreateUser, 0, 1);
		paneCreate.add(lblCreatePass, 0, 2);
		paneCreate.add(lblCreateConfirmPass, 0 ,3);
		paneCreate.add(lblCreateEmail, 0, 4);
		paneCreate.add(lblCreateConfirmEmail, 0, 5);
		paneCreate.add(lblCreateImage, 0, 6);
		paneCreate.add(lblCreateLevel, 0, 7);
		paneCreate.add(txtCreateUser, 1, 1);
		paneCreate.add(txtCreatePass, 1, 2);
		paneCreate.add(txtCreateConfirmPass, 1, 3);
		paneCreate.add(txtCreateEmail, 1, 4);
		paneCreate.add(txtCreateConfirmEmail, 1, 5);
		paneCreate.add(btnCreateChooseImg, 1, 6);
		paneCreate.add(txtCreateLevel, 1, 7);
		paneCreate.add(btnCreate, 1, 8);
		paneCreate.add(btnBack, 0, 8);
		paneCreate.add(lblCreateError, 1, 9);
		GridPane.setHalignment(btnCreate, HPos.RIGHT);
		primaryStage.setScene(new Scene(paneCreate));
		primaryStage.setTitle("Create Child Account");
		primaryStage.show();
		
		//button to choose profile image
		btnCreateChooseImg.setOnAction(e->{
			fileChooser.setTitle("Select Profile Image");
			selectedFile = fileChooser.showOpenDialog(primaryStage);
		});
		
		//button to edit child account
		btnCreate.setOnAction(e->{
			Timeline timeline = new Timeline();
			validated = true;
			profileImagePath="";
			lblCreateError.setText("");
			lblCreateError.setStyle("-fx-text-fill: red;");
			lblCreateImage.setStyle("-fx-text-fill: black;");
			lblCreateEmail.setStyle("-fx-text-fill: black;");
			lblCreateConfirmEmail.setStyle("-fx-text-fill: black;");
			lblCreatePass.setStyle("-fx-text-fill: black;");
			lblCreateConfirmPass.setStyle("-fx-text-fill: black;");
			lblCreateUser.setStyle("-fx-text-fill: black;");
			lblCreateLevel.setStyle("-fx-text-fill: black;");
			
			//if statements are separated instead of nested to display all errors at once
			//validation of username
			if (!Validate.isValidName(txtCreateUser.getText())){
				lblCreateUser.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//look if new username already exist
			try {
				Scanner read = new Scanner(new File(filename));
				String tmp = "";
				while (read.hasNextLine()){
					tmp = read.nextLine();
					if (tmp.split("\t")[1].matches(txtCreateUser.getText())){
						validated = false;
						lblCreateUser.setStyle("-fx-text-fill: red;");
					}
				}
				read.close();
			} catch (Exception e3) { 
				validated = false; 
				lblCreateUser.setStyle("-fx-text-fill: red;");
				throw new MyRuntimeException("Cannot determine if username extsts.");
			}
			//validation of email
			if(!Validate.isValidEmail(txtCreateEmail.getText())){
				lblCreateEmail.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//check if email entered is consistent
			if(!txtCreateEmail.getText().matches(txtCreateConfirmEmail.getText())){
					lblCreateConfirmEmail.setStyle("-fx-text-fill: red;");
					validated = false;
			}
			//check if password entered is consistent
			if(!txtCreatePass.getText().matches(txtCreateConfirmPass.getText())){
				lblCreateConfirmPass.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//validation of password
			if(!Validate.isValidPassword(txtCreatePass.getText())){
				lblCreatePass.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//validation of level
			if (!Validate.isValidLevel(txtCreateLevel.getText())){
				lblCreateLevel.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//firstly screen text input fields
			if (validated == false){
				lblCreateError.setText("Invalid ");
			}else{
				//when all text inputs are validated
				//copy profile image if selected
				//copy default picture if not selected
				if (selectedFile == null){
					selectedFile = new File(System.getProperty("user.dir")+"/data/img/defaultuser.jpg");
				}
				destination = new File(System.getProperty("user.dir") + "/data/img/" + txtCreateUser.getText() + selectedFile.toString().substring(selectedFile.toString().lastIndexOf("."),selectedFile.toString().length()));
				profileImagePath = destination.toPath().toString();
				try {
					Files.copy(selectedFile.toPath(), destination.toPath());
				} catch (Exception e1) {
					validated = false;
					lblCreateImage.setStyle("-fx-text-fill: red;");
					throw new MyRuntimeException("Failed to fetch profile image");
				}
				//second screen for any errors
				if (validated == false){
					lblCreateError.setText("Invalid ");
				}else{
					try {
						//create child account if no errors
						CreateChild(txtCreateLevel.getText(),txtCreateUser.getText(), txtCreatePass.getText(), txtCreateEmail.getText(), profileImagePath);
						btnCreate.setText("Done");
						btnCreate.setDisable(true);
						//direct to parent profile after showing "Done" to user in 0.8 seconds
						timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.8),e2->{
							primaryStage.setScene(KidMathParentProfile.profileScene);
							primaryStage.setTitle(KidMathParentProfile.user + " Profile");
							KidMathParentProfile.initCBox();
						}));
						timeline.play();
					} catch (Exception e1) {
						btnCreate.setText("Failed. Try Again");
						throw new MyRuntimeException("Canoot create child account.");
					}
				}
			}
		});
		
		//go back to parent profile if button clicked
		btnBack.setOnAction(e->{
			primaryStage.setScene(KidMathParentProfile.profileScene);
			primaryStage.setTitle(KidMathParentProfile.user + " Profile");
		});

	}
	
	private static void CreateChild(String inlevel, String inuser, String inpass, String inemail, String inpicture) throws Exception{
		//create child account from accounts file
		//var inlevel, inuser, inpass, inemail, inpicture : fields of the child account to be edited 
		//writes into a temporary file first before delete old accounts file and rename itself
		//into new accounts file
		//also create child txt file
		File accFile = new File(filename);
		File tmpFile = new File("data/tmp.txt");
		Scanner read = new Scanner(accFile);
		String line ="";
		PrintWriter write = new PrintWriter(tmpFile);
		//write each line into tmp file
		while(read.hasNextLine()){
			line = read.nextLine();
			write.println(line);
		}		
		//replace old accounts file with new accounts file
		//append new account details at the end of the file
		//create children text file
		line = inlevel + "\t" + inuser + "\t" + inpass + "\t" + inemail + "\t" + inpicture;
		write.println(line);
		accFile.delete();
		tmpFile.renameTo(new File(filename));
		write.close();
		read.close();
		PrintWriter writeChild = new PrintWriter("data/"+ inuser+".txt");
		writeChild.close();
		
	}
}
