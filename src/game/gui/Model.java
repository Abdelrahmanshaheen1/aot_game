package game.gui;
import game.engine.Battle;

import java.util.*;

enum Mode{
	EASY, HARD
}

public class Model{

	Mode mode;
	public Battle battle;
	Model(){
		int spawn_distance = 200;
		mode = Mode.EASY;
		try {
			battle = new Battle(1, 0, spawn_distance, 3, 250);			
		} catch (Exception e){
			System.out.println("Battle creation failed? Perhaps you won");
		}
	}
	
	Model(boolean set_mode)
	{
		
		mode = Mode.HARD;
		int spawn_distance = 100;
		try {
			battle = new Battle(1, 0, spawn_distance, 5, 125);			
		} catch (Exception e){
			System.out.println("Battle creation failed? Perhaps you won");
		}
	}
	
	void pass_turn() {
		battle.passTurn();
	}
}