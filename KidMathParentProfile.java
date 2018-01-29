
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class KidMathParentProfile{
	final static String filename = "data/Accounts.txt";
	private static File selectedFile;
	public static Scene profileScene;
	private static boolean validated;
	private static File destination;
	private static String profileImagePath;
	protected static String user;
	protected static String email;
	protected static String picture;
	protected static String pass;
	private static ArrayList<String> childrenList;
	private static ComboBox<String> cbChild = new ComboBox<String>();
	private static Button btnSelect;
	private static Button btnDelete;


	public static void start(Stage primaryStage,String inuser,String inpass, String inemail,String inpicture){
		//var inuser, inpass : login credentials of parent account
		//var inemail, inpicture : details of parent account
		childrenList = new ArrayList<String>();
		user = inuser;
		pass = inpass;
		email = inemail;
		picture = inpicture;
		btnSelect = new Button("Select");
		btnDelete = new Button("Delete");
		
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
		Button btnCreateChild = new Button("Create Child Profile");
		
		ImageView imgProfile = null;
		try {
			imgProfile = new ImageView(new Image(new File(picture).toURI().toURL().toExternalForm()));
		} catch (Exception e) {
			throw new MyRuntimeException("Error Fetching profile image.");
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
		cbChild.setMinWidth(200);
		pane.add(paneProfile, 0, 0);
		pane.add(lblSelect, 0, 1);
		pane.add(cbChild, 0, 2);
		pane.add(btnSelect, 1, 2);
		pane.add(btnDelete, 2, 2);
		pane.add(btnCreateChild, 0, 3);
		GridPane.setColumnSpan(paneProfile, GridPane.REMAINING);
		
		//set the main UI and menu bar
		fileMenu.getItems().addAll(menuItemLogout, new SeparatorMenuItem(), menuItemExit);
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		menuBar.getMenus().add(fileMenu);
		root.setTop(menuBar);
		root.setCenter(pane);
		
		//populate the combo box with created child accounts
		initCBox();
		
		profileScene = new Scene(root);
		primaryStage.setTitle(user + " Profile");
		primaryStage.setScene(profileScene);
		primaryStage.sizeToScene();
		primaryStage.show();
		
		//handles event when user click Edit button to edit Parent profile
		btnEditProfile.setOnAction(e->{
			primaryStage.setTitle("Edit Parent Profile");
			newEditPage(primaryStage);
		});
		
		//handles event when user selects a child
		btnSelect.setOnAction(e->{
			for (String i: childrenList){
				if (cbChild.getValue().matches(i.split("\t")[1])){
					KidMathChildProfile.start(primaryStage,i.split("\t")[0],i.split("\t")[1],i.split("\t")[2],i.split("\t")[3],i.split("\t")[4]);
					break;
				}
			}
		});
		
		//handles event when user pressed the select button
		btnCreateChild.setOnAction(e->{
			KidMathCreateChild.start(primaryStage);
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
		
		//handles event when user deletes a child account
		btnDelete.setOnAction(e->{
			deleteChild();
		});
	}
	
	private static void newEditPage(Stage primaryStage){
		//initialise all nodes in edit parent account page
		//also populate empty textfields with existing account details
		FileChooser fileChooser = new FileChooser();
		GridPane paneEdit = new GridPane();
		Label lblEdit = new Label("Edit Parent Account");
		Label lblEditUser = new Label("Username : (6-20) Characters");
		Label lblEditPass = new Label("Password : (6-12) Characters");
		Label lblEditConfirmPass = new Label("Enter password again :");
		Label lblEditEmail = new Label ("Email :");
		Label lblEditConfirmEmail = new Label("Enter email again :");
		Label lblEditImage = new Label("Profile picture :");
		Label lblEditError = new Label("");
		TextField txtEditUser = new TextField(user);
		PasswordField txtEditPass = new PasswordField();
		txtEditPass.setText(pass);
		PasswordField txtEditConfirmPass = new PasswordField();
		txtEditConfirmPass.setText(pass);
		TextField txtEditEmail = new TextField(email);
		TextField txtEditConfirmEmail = new TextField(email);
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
		paneEdit.add(txtEditUser, 1, 1);
		paneEdit.add(txtEditPass, 1, 2);
		paneEdit.add(txtEditConfirmPass, 1, 3);
		paneEdit.add(txtEditEmail, 1, 4);
		paneEdit.add(txtEditConfirmEmail, 1, 5);
		paneEdit.add(btnEditChooseImg, 1, 6);
		paneEdit.add(btnEdit, 1, 7);
		paneEdit.add(btnBack, 0, 7);
		paneEdit.add(lblEditError, 1, 8);
		GridPane.setHalignment(btnEdit, HPos.RIGHT);
		primaryStage.setScene(new Scene(paneEdit));
		primaryStage.setTitle("Edit Parent Account");
		primaryStage.show();
		
		//button to choose profile image
		btnEditChooseImg.setOnAction(e->{
			fileChooser.setTitle("Select Profile Image");
			selectedFile = fileChooser.showOpenDialog(primaryStage);
		});
		
		//Button to edit parent account
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
			//firstly screen text input fields
			if (validated == false){
				lblEditError.setText("Invalid ");
			}else{
				//when all text inputs are validated
				//copy profile image if selected (replace if necessary)
				//refer to default picture if not selected
				if (selectedFile != null){
					File oldImgFile = new File(picture);
					if (oldImgFile.delete()==false){
						System.out.println("failed");
					}
					destination = new File(System.getProperty("user.dir") + "/data/img/" + txtEditUser.getText() + selectedFile.toString().substring(selectedFile.toString().lastIndexOf("."),selectedFile.toString().length()));
					profileImagePath = destination.toPath().toString();
					try {
						Files.copy(selectedFile.toPath(), destination.toPath(),StandardCopyOption.REPLACE_EXISTING);
					} catch (Exception e1) {
						validated = false;
						lblEditImage.setStyle("-fx-text-fill: red;");
						throw new MyRuntimeException("Failed to fetch profile image.");
					}
				}else{
					profileImagePath = new File(picture).toString();
				}
				//second screen for any errors
				if (validated == false){
					lblEditError.setText("Invalid ");
				}else{
					try {
						//edit parent account if no errors
						EditParent(txtEditUser.getText(), txtEditPass.getText(), txtEditEmail.getText(), profileImagePath);
						btnEdit.setText("Done");
						btnEdit.setDisable(true);
						//direct to parent profile after showing "Done" to user in 0.8 seconds
						timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.8),e2->{
							//parse new details of parent account
							start(primaryStage, user, pass, email, profileImagePath);
						}));
						timeline.play();
					} catch (Exception e1) {
						btnEdit.setText("Failed. Try Again");
						throw new MyRuntimeException("Cannot edit account.");
					}
				}
			}
		});
		
		//go back to parent profile
		btnBack.setOnAction(e->{
			start(primaryStage, user, pass, email, picture);
		});
	}
	
	public static void initCBox(){
		//initialise and populate the combo box with child accounts
		//only enable select and delete button when 
		//child account is present in the combo box
		btnDelete.setDisable(true);
		btnSelect.setDisable(true);
		String curString ="";
		childrenList.clear();
		cbChild.getItems().clear();
		try {
			//look for child accounts and append into a array
			//in order to store full details of the child accounts
			Scanner readChild = new Scanner(new File(filename));
			while (readChild.hasNextLine()){
				curString = readChild.nextLine();
				if (!curString.split("\t")[0].matches("P")){
					childrenList.add(curString);
				}
			}
			//add the username of the child accounts to the combo box
			for (String i: childrenList){
				cbChild.getItems().add(i.split("\t")[1]);
			}
			readChild.close();
		} catch (Exception e) {	
			throw new MyRuntimeException("Cannot initialise child accounts");
		}
		//enable select and delete button if child account array is not empty
		//and set default value of the combo box
		//mainly to avoid errors
		if (!childrenList.isEmpty()){
			btnDelete.setDisable(false);
			btnSelect.setDisable(false);
			cbChild.getSelectionModel().selectFirst();
		}
	}
	
	private static void EditParent(String inuser, String inpass, String inemail, String inpicture) throws Exception{
		//edit parent account from accounts file
		//var inuser, inpass, inemail, inpicture : fields of the parent account to be edited 
		//writes into a temporary file first before delete old accounts file and rename itself
		//into new accounts file
		File accFile = new File(filename);
		File tmpFile = new File("data/tmp.txt");
		Scanner read = new Scanner(accFile);
		String line ="";
		PrintWriter write = new PrintWriter(tmpFile);
		while(read.hasNextLine()){
			line = read.nextLine();
			//replace the old parent details with new ones
			//in both file and class variable
			if (line.split("\t")[0].matches("P") && line.split("\t")[1].matches(user)){
				line = "P\t" + inuser + "\t" + inpass + "\t" + inemail + "\t" + inpicture;
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
	
	private static void deleteChild(){
		//delete the selected child account in combo box
		//writes into a temporary file first before delete old accounts file and rename itself
		//into new accounts file
		String curString ="";
		File accFile = new File(filename);
		File tmpFile = new File("data/tmp.txt");
		try {
			Scanner readChild = new Scanner(accFile);
			PrintWriter write = new PrintWriter(tmpFile);
			while (readChild.hasNextLine()){
				curString = readChild.nextLine();
				//only append accounts into tmp file if it is not
				//the deleted child account
				if (!curString.split("\t")[1].matches(cbChild.getValue())){
					write.println(curString);
				}
				else{
					//delete child account txt file and its profile picture
					new File("data/" + curString.split("\t")[1] + ".txt").delete();
					new File(curString.split("\t")[4]).delete();
				}
			}
			accFile.delete();
			tmpFile.renameTo(new File(filename));
			write.close();
			readChild.close();
			initCBox();
		} catch (Exception e) {
			throw new MyRuntimeException("Cannot delete child account");
		}
	}
	
}
