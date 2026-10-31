package game.gui;

import game.engine.Battle;

import java.io.IOException;






import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import game.gui.Model;
import game.gui.View;
import javafx.event.EventHandler;

public class Controller {

	private Stage stage;
	private Scene scene;
	private Parent root;
	private Model model;
	private View view;
	private Battle battle;
@FXML
private Button exit_button;

@FXML
private AnchorPane scenePane;

@FXML
Button pass_button = new Button("Pass");
//private static String MainStyle = "button.css";

	
//	public void switchToplayscene(ActionEvent event) throws IOException{
//		Parent root = FXMLLoader.load(getClass().getResource("playscene.fxml"));
//		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//		scene = new Scene(root);
//		scene.getStylesheets().add(MainStyle);
//		stage.setScene(scene);
//		stage.show();
//		
//	}

	public void exit(ActionEvent event)
	{
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Exit");
		alert.setHeaderText("Are you sure you want to Exit ?");
		alert.setContentText("Your progress will not be saved.");
		if(alert.showAndWait().get() == ButtonType.OK){
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			stage.close();
		}
	}
	
	
//	
//	public void switchTodiffscene(ActionEvent event) throws IOException{
//		Parent root = FXMLLoader.load(getClass().getResource("diffscene.fxml"));
//		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//		scene = new Scene(root);
//		stage.setScene(scene);
//		stage.show();
//	}
//
//
//
//
//	public void switchToinsscene(ActionEvent event) throws IOException{
//		Parent root = FXMLLoader.load(getClass().getResource("insscene.fxml"));
//		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//		scene = new Scene(root);
//		stage.setScene(scene);
//		stage.show();
//	}

//	public void switchToinsscene2(ActionEvent event) throws IOException{
//		Parent root = FXMLLoader.load(getClass().getResource("insscene2.fxml"));
//		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//		scene = new Scene(root);
//		stage.setScene(scene);
//		stage.show();
//	}
//	


	public void switchToEasyGameScene(ActionEvent event) throws IOException{
		pass_button.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	model.pass_turn();
		    	view.refreshView();
		    }
		});
		model = new Model();
		view = new View(model, pass_button);
		battle = model.battle;
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		view.updateView(stage);
	}
	
	
	
	
	public void switchToHardGameScene(ActionEvent event) throws IOException{
		pass_button.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	model.pass_turn();
		    	view.refreshView();
		    }
		});
		model = new Model(true);
		view = new View(model, pass_button);
		battle = model.battle;
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		view.updateView(stage);
	}
	
	


}
