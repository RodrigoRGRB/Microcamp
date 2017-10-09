package mvc;

import java.util.ArrayList;

import com.pengrad.telegrambot.model.Update;

public class ControllerPerguntas implements ControllerAnswer{

	private Model model;
	private View view;
	
	public ControllerPerguntas(Model model, View view){
		this.model = model; //connection Controller -> Model
		this.view = view; //connection Controller -> View
	}

	public void searchPerguntas(Update update) {
		view.sendTypingMessage(update);
		model.retornarLista(update);
		
	}
}
