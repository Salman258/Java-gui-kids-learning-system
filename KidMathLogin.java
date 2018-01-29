

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class KidMathLogin{
	public static Scene scene;
	private static boolean validated;
	private static boolean loggedIn;
	final static String filename = "Accounts.txt";
	private static String profileImagePath;
	private static File selectedFile;
	private static File destination;
	private static GridPane paneRegister = new GridPane();
	private static GridPane paneLogin = new GridPane();
	
	public static void start(Stage primaryStage){
		//check if the Accounts.txt file already exist to determine
		//whether system is fresh installed
		try{
			File accountsFile = new File(filename);
			if (accountsFile.exists()){
				//direct to login page 
				loginPage(primaryStage);
			}else{
				//direct to register page
				newRegisterPage(primaryStage);
			}
		}
		catch(Exception e) {
			//handle exception
			throw new MyRuntimeException("Failed to initialise Accounts.txt");
		}
		
	}

	private static void newRegisterPage(Stage primaryStage){
		//initialise nodes in Register Parent Account 
		FileChooser fileChooser = new FileChooser();
		Label lblRegister = new Label("Register New Parent Account");
		Label lblRegisterUser = new Label("Username : (6-20) Characters");
		Label lblRegisterPass = new Label("Password : (6-12) Characters");
		Label lblRegisterConfirmPass = new Label("Enter password again :");
		Label lblRegisterEmail = new Label ("Email :");
		Label lblRegisterConfirmEmail = new Label("Enter email again :");
		Label lblRegisterImage = new Label("Profile picture : (optional)");
		Label lblRegisterError = new Label("");
		TextField txtRegisterUser = new TextField();
		PasswordField txtRegisterPass = new PasswordField();
		PasswordField txtRegisterConfirmPass = new PasswordField();
		TextField txtRegisterEmail = new TextField();
		TextField txtRegisterConfirmEmail = new TextField();
		Button btnRegisterChooseImg = new Button("Choose image...");
		Button btnRegister = new Button("Register");
		paneRegister.setPadding(new Insets(20,20,20,20));
		paneRegister.setVgap(5);
		paneRegister.setHgap(5);
		paneRegister.add(lblRegister, 0, 0);
		paneRegister.add(lblRegisterUser, 0, 1);
		paneRegister.add(lblRegisterPass, 0, 2);
		paneRegister.add(lblRegisterConfirmPass, 0 ,3);
		paneRegister.add(lblRegisterEmail, 0, 4);
		paneRegister.add(lblRegisterConfirmEmail, 0, 5);
		paneRegister.add(lblRegisterImage, 0, 6);
		paneRegister.add(txtRegisterUser, 1, 1);
		paneRegister.add(txtRegisterPass, 1, 2);
		paneRegister.add(txtRegisterConfirmPass, 1, 3);
		paneRegister.add(txtRegisterEmail, 1, 4);
		paneRegister.add(txtRegisterConfirmEmail, 1, 5);
		paneRegister.add(btnRegisterChooseImg, 1, 6);
		paneRegister.add(btnRegister, 0, 7);
		paneRegister.add(lblRegisterError, 0, 8);
		GridPane.setHalignment(btnRegister, HPos.RIGHT);
		scene = new Scene(paneRegister);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Register New Parent Account");
		primaryStage.show();
		
		//Button to register the fields entered for the parent account
		btnRegister.setOnAction(e->{
			Timeline timeline = new Timeline();
			validated = true;
			profileImagePath="";
			lblRegisterError.setText("");
			lblRegisterError.setStyle("-fx-text-fill: red;");
			lblRegisterImage.setStyle("-fx-text-fill: black;");
			lblRegisterEmail.setStyle("-fx-text-fill: black;");
			lblRegisterConfirmEmail.setStyle("-fx-text-fill: black;");
			lblRegisterPass.setStyle("-fx-text-fill: black;");
			lblRegisterConfirmPass.setStyle("-fx-text-fill: black;");
			lblRegisterUser.setStyle("-fx-text-fill: black;");
			
			//if statements are separated instead of nested to display all errors at once
			//validation of username
			if (!Validate.isValidName(txtRegisterUser.getText())){
				lblRegisterUser.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//validation of email
			if(!Validate.isValidEmail(txtRegisterEmail.getText())){
				lblRegisterEmail.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//check if email entered is consistent
			if(!txtRegisterEmail.getText().matches(txtRegisterConfirmEmail.getText())
					||txtRegisterConfirmEmail.getText().replaceAll(" ","").matches("")){
					lblRegisterConfirmEmail.setStyle("-fx-text-fill: red;");
					validated = false;
			}
			//check if password entered is consistent
			if(!txtRegisterPass.getText().matches(txtRegisterConfirmPass.getText())
					||txtRegisterConfirmPass.getText().replaceAll(" ","").matches("")){
				lblRegisterConfirmPass.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//validate password
			if(!Validate.isValidPassword(txtRegisterPass.getText())){
				lblRegisterPass.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			
			//firstly screen text input fields
			if (validated == false){
				lblRegisterError.setText("Invalid ");
			}else{
				//when all text inputs are validated
				//copy profile image if selected
				//copy default picture if not selected
				if (selectedFile == null){
					selectedFile = new File(System.getProperty("user.dir")+"/data/img/defaultuser.jpg");
				}
				destination = new File(System.getProperty("user.dir") + "/data/img/" + txtRegisterUser.getText() + selectedFile.toString().substring(selectedFile.toString().lastIndexOf("."),selectedFile.toString().length()));
				profileImagePath = destination.toPath().toString();
				try {
					Files.copy(selectedFile.toPath(), destination.toPath());
				} catch (Exception e1) {
					validated = false;
					lblRegisterImage.setStyle("-fx-text-fill: red;");
					throw new MyRuntimeException("Failed to fetch profile image");
				}
				//second screen for any errors
				if (validated == false){
					lblRegisterError.setText("Invalid ");
				}else{
					//register parent account if no errors
					registerParent(txtRegisterUser.getText(), txtRegisterPass.getText(), txtRegisterEmail.getText(), profileImagePath);
					btnRegister.setText("Done");
					btnRegister.setDisable(true);
					//direct to login page after showing "Done" to user in 0.8 seconds
					timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.8),e2->{
						loginPage(primaryStage);
					}));
					timeline.play();
				}
			}
		});
		
		//button to choose profile image
		btnRegisterChooseImg.setOnAction(e->{
			fileChooser.setTitle("Select Profile Image");
			selectedFile = fileChooser.showOpenDialog(primaryStage);
		});
	}
	
	private static void loginPage(Stage primaryStage){
		//initialies nodes for login page
		Label lblUser = new Label("Username :");
		Label lblPass = new Label("Password :");
		Label lblError = new Label("");
		TextField txtUser = new TextField();
		PasswordField txtPass = new PasswordField();
		Button btnLogin = new Button("Login");
		Button btnForgotPass = new Button("Forgot password?");
		ImageView imgSys = new ImageView(new Image("data/img/system.png"));
		imgSys.setFitHeight(80);
		imgSys.setFitWidth(200);
		paneLogin.setPadding(new Insets(20,20,20,20));
		paneLogin.setVgap(10);
		paneLogin.setHgap(5);
		paneLogin.add(imgSys, 0, 0);
		paneLogin.add(lblUser, 0, 1);
		paneLogin.add(lblPass, 0, 2);
		paneLogin.add(txtUser, 1, 1);
		paneLogin.add(txtPass, 1, 2);
		paneLogin.add(lblError, 1, 3);
		paneLogin.add(btnLogin, 1, 4);
		paneLogin.add(btnForgotPass, 0, 4);
		GridPane.setHalignment(imgSys,HPos.CENTER);
		GridPane.setColumnSpan(imgSys, GridPane.REMAINING);
		GridPane.setHalignment(btnLogin, HPos.RIGHT);
		scene = new Scene(paneLogin);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Login");
		primaryStage.show();
		
		//button handler when user clicks "Login"
		btnLogin.setOnAction(e->{
			String curString="";
			lblError.setStyle("-fx-text-fill:red;");
			lblError.setText("");
			loggedIn = false;
			//check if the username and password fields are filled 
			if (txtUser.getText().replaceAll(" ","").matches("")||txtPass.getText().replaceAll(" ","").matches("")){
				lblError.setText("Please fill in the fields");
			}else{
				//verify username and password from text file
				try (Scanner read = new Scanner(new File(filename));){
					while (read.hasNextLine()){
						curString = read.nextLine();
						if (curString.split("\t")[1].matches(txtUser.getText())&&curString.split("\t")[2].matches(txtPass.getText())){
							loggedIn = true;
							break;
						}
					}
				} catch (Exception e1) {
					throw new MyRuntimeException("Failed to verify user credentials.");
				}
				//if user is successfully logged in
				//decides of user is parent or child
				//then direct to respective page
				if (loggedIn == true){
					txtUser.setText("");
					txtPass.setText("");
					if (curString.split("\t")[0].matches("P"))
						KidMathParentProfile.start(primaryStage,curString.split("\t")[1],curString.split("\t")[2],curString.split("\t")[3],curString.split("\t")[4]);
					else
						KidMathChildMain.start(primaryStage,curString.split("\t")[0], curString.split("\t")[1],curString.split("\t")[2], curString.split("\t")[3],curString.split("\t")[4]);
				}else{
					lblError.setText("Invalid Login");
				}
			}
		});
		
		//button to handle forgot password
		btnForgotPass.setOnAction(e->{
			
		});
	}
	
	private static void registerParent(String user, String pass, String email, String path){
		//method to create and write a file for parent account
		//will always be only one parent account and checked first 
		//hence this is always the first creation of Accounts.txt
		
		//var user : username of parent
		//var pass : password of parent
		//var email : email of parent 
		//var path : profile image path of parent
		
		try(PrintWriter write = new PrintWriter(new File(filename));){
			write.println("P\t" + user + "\t" + pass + "\t" + email + "\t" + path);
		}catch(Exception e1){ 
			throw new MyRuntimeException("Cannot create Parent Account");
		}
	}
	
}
