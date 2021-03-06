package com.iLearnSA.controller;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import application.iLearnDBConfig;
import application.iLearn_User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
public class SignUpViewController 
{
	//chwongr4703@ung.edu
	//ahfazzaman@gmail.com
	//Instantiate private main class
	//add here
	
	//Setup all FXML content here that will be used in the controller
	@FXML private TextField create_username, create_password, create_email;
	@FXML private Button signUp;
	@FXML private ComboBox<String> securityQuestion1;
	@FXML private ComboBox<String> securityQuestion2;
	@FXML private ComboBox<String> securityQuestion3;
	@FXML private TextField qAnswer1, qAnswer2, qAnswer3;
	
	//The main one we will be looking at would look something like this
	public void ClickSignUp_Button(ActionEvent event) throws SQLException, IOException {
		String userName, passWord, firstName, lastName, email, question1, question2, question3, answer1, answer2, answer3;
		userName = create_username.getText();
		passWord = create_password.getText();
		//firstName = firstName_textField.getText();
		//lastName = lastName_textField.getText();
		email = create_email.getText();
		question1 = securityQuestion1.getValue();
		question2 = securityQuestion1.getValue();
		question3 = securityQuestion1.getValue();
		answer1 = qAnswer1.getText();
		answer2 = qAnswer2.getText();
		answer3 = qAnswer3.getText();
		
		if(userName.isEmpty() || passWord.isEmpty() || email.isEmpty()) {
			alert("Input fileds empty", "Please fill usname, password and email fields");
		}
		else {
			//After all the validations go through, It will sign them up
			//And send them back to the login page where they can log in
			
			//We will then create a User object
			iLearn_User user = new iLearn_User();
			
			//We then use the model to set the values for the User object
			user.setUserName(userName);
			user.setPassWord(passWord);
			user.setEmail(email);
			
			//We then can set up a DB connection using Chris's connection object
			Connection connection = iLearnDBConfig.getConnection();
			//Create a statement
			Statement st = connection.createStatement();
			
			// check if username has been user by other people 
			String checkUserName = "SELECT COUNT(1) AS COUNT FROM users WHERE userName = '" + userName + "'";
			ResultSet rs = st.executeQuery(checkUserName);
			rs.first();
			
			// if username is occupied by someone else, the result of statement is 1, otherwise, 0
			int countColumn = rs.getInt("COUNT");
			boolean exist = countColumn >= 1 ? true : false;
			if(exist == false) {
				//Then run the query we set up above
				String query = "INSERT INTO users (userName, password, email) VALUES (" + "'" + userName + "', '" + passWord + "', '" + email + "')";//"userName = ?, passWord = ?, firstName = ?, lastName = ?, email = ?";
				st.executeUpdate(query);
				
				// tell the user if the account is created 
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("User creation");
				alert.setContentText("Account is created!");
				alert.showAndWait();

				Parent view = FXMLLoader.load(getClass().getResource("../view/LoginView.fxml"));
				Scene scene = new Scene(view);
				Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
				window.setScene(scene);
				window.show();
			}
			else {
				alert("Occupied username", "Please create another username");
			}
		}
	}
	
	// initialization of alert msg
	public void alert(String alertTile, String alertText) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(alertTile);
		alert.setContentText(alertText);
		alert.showAndWait();
	}
	
	// go back to login view
	public void goBackBtnClicked(ActionEvent event) throws IOException {
		Parent view = FXMLLoader.load(getClass().getResource("../view/LoginView.fxml"));
		Scene scene = new Scene(view);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
}
