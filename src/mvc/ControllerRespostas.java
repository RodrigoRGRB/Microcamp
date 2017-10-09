package mvc;

import java.util.ArrayList;

import com.pengrad.telegrambot.model.Update;

public class ControllerRespostas implements ControllerSearch{
	
	
	private Model model;
	private View view;
	
	public ControllerRespostas(Model model, View view){
		this.model = model; //connection Controller -> Model
		this.view = view; //connection Controller -> View
	}
	
	public void search(Update update,Object respostas){
		view.sendTypingMessage(update);
		System.out.println("chamou a view");
		System.out.println("vai chamar o model");
		model.enviaResultado(update,(ArrayList) respostas);
		
		
	}


}
