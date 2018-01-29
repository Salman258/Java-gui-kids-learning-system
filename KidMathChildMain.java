//Liew Yu Hung 0325770

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class KidMathChildMain{
	final static String filename = "data/Accounts.txt";
	private static File selectedFile;
	public static Scene profileScene;
	private static boolean validated;
	private static File destination;
	private static String profileImagePath;
	private static String user;
	private static String email;
	private static String picture;
	private static String pass;
	private static String level;

	public static void start(Stage primaryStage,String inlevel, String inuser,String inpass, String inemail,String inpicture){
		//var inuser, inpass : login credentials of child account
		//var inlevel, inemail, inpicture : details of child account
		user = inuser;
		pass = inpass;
		email = inemail;
		picture = inpicture;
		level = inlevel;
		
		//initialise all nodes in Parent Profile page
		//Create menu bar
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");
		//File menu - Logout & Exit
		MenuItem menuItemLogout = new MenuItem("Logout");
		MenuItem menuItemExit = new MenuItem("Exit");
		
		BorderPane root = new BorderPane();
		GridPane pane = new GridPane();
		GridPane paneProfile = new GridPane();
		pane.setPadding(new Insets(20,20,20,20));
		pane.setVgap(10);
		pane.setHgap(10);
		paneProfile.setHgap(10);
		
		Label lblUser = new Label(user);
		Label lblEmail = new Label(email);
		Label lblSelect = new Label("Select :");
		Button btnEditProfile = new Button("Edit Profile");

		Button btnLecture = new Button("Lecture");
		Button btnQuiz = new Button("Quiz");
		
		ImageView imgProfile = null;
		try {
			imgProfile = new ImageView(new Image(new File(picture).toURI().toURL().toExternalForm()));
		} catch (Exception e) {
			throw new MyRuntimeException("Error fetching image");
		}
		//pane for user profile details
		lblUser.setStyle("-fx-font-size:35");
		imgProfile.setFitHeight(120);
		imgProfile.setFitWidth(100);
		paneProfile.add(imgProfile, 0, 0);
		paneProfile.add(lblUser, 1, 0);
		paneProfile.add(lblEmail, 1, 1);
		paneProfile.add(btnEditProfile, 1, 2);
		GridPane.setRowSpan(imgProfile, GridPane.REMAINING);
		
		//main pane if the UI
		pane.add(paneProfile, 0, 0);
		pane.add(lblSelect, 0, 1);
		pane.add(btnLecture, 0, 2);
		pane.add(btnQuiz, 1, 2);
		GridPane.setColumnSpan(paneProfile, GridPane.REMAINING);
		
		//set the main UI and menu bar
		fileMenu.getItems().addAll(menuItemLogout, new SeparatorMenuItem(), menuItemExit);
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		menuBar.getMenus().add(fileMenu);
		root.setTop(menuBar);
		root.setCenter(pane);
		profileScene = new Scene(root);
		primaryStage.setTitle(user + " Profile");
		primaryStage.setScene(profileScene);
		primaryStage.sizeToScene();
		primaryStage.show();
		
		//handles event when user click Edit button to edit child profile
		btnEditProfile.setOnAction(e->{
			newEditPage(primaryStage);
		});
		
		//handles event when user click logout in menu
		menuItemLogout.setOnAction(e->{
			primaryStage.setTitle("Login");
			primaryStage.setScene(KidMathLogin.scene);
		});
		
		//handles event when user click exit in menu
		menuItemExit.setOnAction(e->{
			System.exit(0);
		});
	}
	
	private static void newEditPage(Stage primaryStage){
		//initialise all nodes in edit child account page
		//also populate empty textfields with existing account details
		FileChooser fileChooser = new FileChooser();
		GridPane paneEdit = new GridPane();
		Label lblEdit = new Label("Edit Child Account");
		Label lblEditUser = new Label("Username : (6-20) Characters");
		Label lblEditPass = new Label("Password : (6-12) Characters");
		Label lblEditConfirmPass = new Label("Enter password again :");
		Label lblEditEmail = new Label ("Email :");
		Label lblEditConfirmEmail = new Label("Enter email again :");
		Label lblEditImage = new Label("Profile picture :");
		Label lblEditError = new Label("");
		Label lblEditLevel = new Label("Level: ");
		TextField txtEditUser = new TextField(user);
		PasswordField txtEditPass = new PasswordField();
		txtEditPass.setText(pass);
		PasswordField txtEditConfirmPass = new PasswordField();
		txtEditConfirmPass.setText(pass);
		TextField txtEditEmail = new TextField(email);
		TextField txtEditConfirmEmail = new TextField(email);
		TextField txtEditLevel = new TextField(level);
		Button btnEditChooseImg = new Button("Choose image...");
		Button btnEdit = new Button("Update");
		Button btnBack = new Button("Back");
		
		paneEdit.setPadding(new Insets(20,20,20,20));
		paneEdit.setVgap(5);
		paneEdit.setHgap(5);
		paneEdit.add(lblEdit, 0, 0);
		paneEdit.add(lblEditUser, 0, 1);
		paneEdit.add(lblEditPass, 0, 2);
		paneEdit.add(lblEditConfirmPass, 0 ,3);
		paneEdit.add(lblEditEmail, 0, 4);
		paneEdit.add(lblEditConfirmEmail, 0, 5);
		paneEdit.add(lblEditImage, 0, 6);
		paneEdit.add(lblEditLevel, 0, 7);
		paneEdit.add(txtEditUser, 1, 1);
		paneEdit.add(txtEditPass, 1, 2);
		paneEdit.add(txtEditConfirmPass, 1, 3);
		paneEdit.add(txtEditEmail, 1, 4);
		paneEdit.add(txtEditConfirmEmail, 1, 5);
		paneEdit.add(btnEditChooseImg, 1, 6);
		paneEdit.add(txtEditLevel, 1, 7);
		paneEdit.add(btnEdit, 1, 8);
		paneEdit.add(btnBack, 0, 8);
		paneEdit.add(lblEditError, 1, 9);
		GridPane.setHalignment(btnEdit, HPos.RIGHT);
		primaryStage.setScene(new Scene(paneEdit));
		primaryStage.setTitle("Edit Account");
		primaryStage.show();
		
		//button to choose profile image
		btnEditChooseImg.setOnAction(e->{
			fileChooser.setTitle("Select Profile Image");
			selectedFile = fileChooser.showOpenDialog(primaryStage);
		});
		
		//button to edit child account
		btnEdit.setOnAction(e->{
			Timeline timeline = new Timeline();
			validated = true;
			profileImagePath="";
			lblEditError.setText("");
			lblEditError.setStyle("-fx-text-fill: red;");
			lblEditImage.setStyle("-fx-text-fill: black;");
			lblEditEmail.setStyle("-fx-text-fill: black;");
			lblEditConfirmEmail.setStyle("-fx-text-fill: black;");
			lblEditPass.setStyle("-fx-text-fill: black;");
			lblEditConfirmPass.setStyle("-fx-text-fill: black;");
			lblEditUser.setStyle("-fx-text-fill: black;");
			lblEditLevel.setStyle("-fx-text-fill: black;");
			
			//if statements are separated instead of nested to display all errors at once
			//validation of username
			if (!Validate.isValidName(txtEditUser.getText())){
				lblEditUser.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//look if new username already exist
			if (!txtEditUser.getText().matches(user)){
				try {
					Scanner read = new Scanner(new File(filename));
					String tmp = "";
					while (read.hasNextLine()){
						tmp = read.nextLine();
						if (tmp.split("\t")[1].matches(txtEditUser.getText())){
							validated = false;
							lblEditUser.setStyle("-fx-text-fill: red;");
						}
					}
					read.close();
				} catch (Exception e3) { 
					validated = false; 
					lblEditUser.setStyle("-fx-text-fill: red;");
					throw new MyRuntimeException("Cannot determine if username exists.");
				}
			}
			//validation of email
			if(!Validate.isValidEmail(txtEditEmail.getText())){
				lblEditEmail.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//check if email entered is consistent
			if(!txtEditEmail.getText().matches(txtEditConfirmEmail.getText())){
					lblEditConfirmEmail.setStyle("-fx-text-fill: red;");
					validated = false;
			}
			//check if password entered is consistent
			if(!txtEditPass.getText().matches(txtEditConfirmPass.getText())){
				lblEditConfirmPass.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//validation of password
			if(!Validate.isValidPassword(txtEditPass.getText())){
				lblEditPass.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//validation of level
			if (!Validate.isValidLevel(txtEditLevel.getText())){
				lblEditLevel.setStyle("-fx-text-fill: red;");
				validated = false;
			}
			//firstly screen text input fields
			if (validated == false){
				lblEditError.setText("Invalid ");
			}else{
				//when all text inputs are validated
				//copy profile image if selected (replace if necessary)
				//refer to default picture if not selected
				if (selectedFile != null){
					File oldImgFile = new File(picture);
					oldImgFile.delete();
					destination = new File(System.getProperty("user.dir") + "/data/img/" + txtEditUser.getText() + selectedFile.toString().substring(selectedFile.toString().lastIndexOf("."),selectedFile.toString().length()));
					profileImagePath = destination.toPath().toString();
					try {
						Files.copy(selectedFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
					} catch (Exception e1) {
						validated = false;
						lblEditImage.setStyle("-fx-text-fill: red;");
						throw new MyRuntimeException("Failed to fetch profile image.");
					}
				}else{
					profileImagePath = new File(picture).toPath().toString();
				}
				//second screen for any errors
				if (validated == false){
					lblEditError.setText("Invalid ");
				}else{
					try {
						//edit child account if no errors
						EditChild(txtEditLevel.getText(), txtEditUser.getText(), txtEditPass.getText(), txtEditEmail.getText(), profileImagePath);
						btnEdit.setText("Done");
						btnEdit.setDisable(true);
						//direct to child profile after showing "Done" to user in 0.8 seconds
						timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.8),e2->{
							start(primaryStage, level, user, pass, email, profileImagePath);
						}));
						timeline.play();
					} catch (Exception e1) {
						btnEdit.setText("Failed. Try Again");
						throw new MyRuntimeException("Canot edit child account.");
					}
				}
			}
		});
		
		//go back to child profile if button clicked
		btnBack.setOnAction(e->{
			start(primaryStage, level, user, pass, email, picture);
		});
	}
	
	private static void EditChild(String inlevel, String inuser, String inpass, String inemail, String inpicture) throws Exception{
		//edit child account from accounts file
		//var inlevel, inuser, inpass, inemail, inpicture : fields of the child account to be edited 
		//writes into a temporary file first before delete old accounts file and rename itself
		//into new accounts file
		//also rename child txt file
		File accFile = new File(filename);
		File tmpFile = new File("data/tmp.txt");
		Scanner read = new Scanner(accFile);
		String line ="";
		PrintWriter write = new PrintWriter(tmpFile);
		while(read.hasNextLine()){
			line = read.nextLine(); 
			//replace the old parent details with new ones
			//in both file and class variable
			//rename children text file
			if (line.split("\t")[0].matches(inlevel) && line.split("\t")[1].matches(user)){
				line = inlevel + "\t" + inuser + "\t" + inpass + "\t" + inemail + "\t" + inpicture;
				new File("data/" + user +".txt").renameTo(new File("data/" + inuser + ".txt"));
				level = inlevel;
				user = inuser;
				pass = inpass;
				email = inemail;
				picture = inpicture;
			}
			//write each line into tmp file
			write.println(line);
		}
		accFile.delete();
		tmpFile.renameTo(new File(filename));
		write.close();
		read.close();
		
	}
}
