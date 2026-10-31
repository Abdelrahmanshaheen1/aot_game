package game.gui;

import game.gui.Model;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import java.util.PriorityQueue;
import game.engine.lanes.Lane;
import game.engine.titans.*;
import game.engine.weapons.*;
import java.util.*;

import java.util.Map.Entry;
import game.engine.weapons.*;
public class View {

    Model model;
	Button pass_button;
	Stage stage = null;
	ArrayList<Button> weapon_buttons;
	View(Model model, Button pass_button)
	{
		this.model = model;
		this.pass_button = pass_button;
		this.createWeaponButtons();
	}

	private void createWeaponButtons() {
		weapon_buttons = new ArrayList<Button>();
	    HashMap<Integer, WeaponRegistry> weaponShop = model.battle.getWeaponFactory().getWeaponShop();

	    for (Entry<Integer, WeaponRegistry> entry : weaponShop.entrySet()) {
	        WeaponRegistry weaponRegistry = entry.getValue();

	        String weaponDetails = String.format("%s, Price: %d, Damage: %d",
	                                             weaponRegistry.getName(),
	                                             weaponRegistry.getPrice(), weaponRegistry.getDamage());

	        Button buy_weapon_button = new Button(weaponDetails);
	        buy_weapon_button.setOnAction(event -> {
	            try {
	            	Lane selectedLane = model.battle.getLanes().peek();
	            	int weaponID = entry.getKey();
	                model.battle.purchaseWeapon(weaponID, selectedLane);
	                refreshView();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });

	        weapon_buttons.add(buy_weapon_button);
	    }
	    return;
	}
	
	public String getTitanImageName(Titan titan) {
	    if (titan instanceof AbnormalTitan) {
	        return "AbnormalTitan.png";
	    } else if (titan instanceof ArmoredTitan) {
	        return "ArmoredTitan.png";
	    } else if (titan instanceof ColossalTitan) {
	        return "ColossalTitan.png";
	    } else if (titan instanceof PureTitan) {
	        return "PureTitan.png";
	    } else {
	        return "UnknownTitan.png"; // Default case if none match
	    }
	}
	
	public String getWeaponImageName(Weapon weapon) {
	    if (weapon instanceof PiercingCannon) {
	        return "PiercingCannon.png";
	    } else if (weapon instanceof SniperCannon) {
	        return "SniperCannon.png";
	    } else if (weapon instanceof VolleySpreadCannon) {
	        return "VolleySpreadCannon.png";
	    } else if (weapon instanceof WallTrap) {
	        return "WallTrap.png";
	    } else {
	        return "UnknownWeapon.png"; // Default case if none match
	    }
	}

	public void endGame()
	{
        VBox rootNode = new VBox(10); // 10 pixels spacing between elements
        rootNode.setStyle("-fx-padding: 10;");
        Label titleLabel = new Label("Attack on Titan: Utopia!");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label gameoverLabel = new Label("GAME OVER!");
        gameoverLabel.setStyle("-fx-font-size: 50px; -fx-font-weight: bold;");
        rootNode.setAlignment(Pos.CENTER);
        rootNode.getChildren().add(titleLabel);
        rootNode.getChildren().add(gameoverLabel);
        Scene scene = new Scene(rootNode, 800, 600);
        stage.setScene(scene);
		stage.show();
	}
	
    public void updateView(Stage stage) {
    	if(this.stage == null)
    	{
    		this.stage = stage;    		
    	}
    	if(model.battle.isGameOver())
    	{
    		endGame();
    		return;
    	}
        VBox rootNode = new VBox(10); // 10 pixels spacing between elements
        rootNode.setStyle("-fx-padding: 10;");

        // Game info
        Label titleLabel = new Label("Attack on Titan: Utopia!");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label scoreLabel = new Label("Score: " + model.battle.getScore());
        Label turnLabel = new Label("Turn: " + model.battle.getNumberOfTurns());
        Label phaseLabel = new Label("Phase: " + model.battle.getBattlePhase());
        Label resourcesLabel = new Label("Available Resources: " + model.battle.getResourcesGathered());
        Label laneLabel = new Label("Available Lanes: " + model.battle.getLanes().size());

        rootNode.setAlignment(Pos.CENTER);
        rootNode.getChildren().addAll(titleLabel, scoreLabel, turnLabel, phaseLabel, resourcesLabel, laneLabel);

        PriorityQueue<Lane> lanes = model.battle.getLanes();
        for (Lane lane : lanes) {
        	HBox laneBox = new HBox(5); // spacing between images
        	Color beige = Color.rgb(165, 42, 42); 

            laneBox.setBackground(new Background(new BackgroundFill(beige, null, null)));
            laneBox.setPadding(new Insets(5));
            laneBox.setMinHeight(50);

            int health = lane.getLaneWall().getCurrentHealth();
            Label laneInfo = new Label("Wall health = " + health);
            laneInfo.setStyle("-fx-text-fill: white;");
            laneBox.getChildren().add(laneInfo);
            
            // Display Titans in the Lane
            for (Titan titan : lane.getTitans()) {
                ImageView titanView = new ImageView(new Image(getClass().getResourceAsStream("resources/images/" + getTitanImageName(titan))));
                titanView.setFitWidth(50);
                titanView.setPreserveRatio(true);
                laneBox.getChildren().add(titanView);
            }

            // Display Weapons in the Lane
            for (Weapon weapon : lane.getWeapons()) {
                ImageView weaponView = new ImageView(new Image(getClass().getResourceAsStream("resources/images/" + getWeaponImageName(weapon))));
                weaponView.setFitWidth(50);
                weaponView.setPreserveRatio(true);
                laneBox.getChildren().add(weaponView);
            }

            rootNode.getChildren().add(laneBox);
        }
        

        for(Button button : weapon_buttons)
        {
        	rootNode.getChildren().add(button);
        }
        rootNode.getChildren().add(pass_button);
        
        Scene scene = new Scene(rootNode, 800, 600);
        stage.setScene(scene);
		stage.show();
    }
    
    public void refreshView() {
    	updateView(stage);
    }
}

