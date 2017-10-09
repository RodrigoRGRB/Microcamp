package mvc;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import jdk.nashorn.internal.ir.ReturnNode;

public class View implements Observer{

	
	TelegramBot bot = TelegramBotAdapter.build("376301833:AAGcwZBPyiPWSClsxFd4Zh9JQQnGxvLtMqM");

	//Object that receives messages
	GetUpdatesResponse updatesResponse;
	//Object that send responses
	SendResponse sendResponse;
	//Object that manage chat actions like "typing action"
	BaseResponse baseResponse;
			
	
	int queuesIndex=0;
	
	String nome = "";
	ArrayList lista;
	Boolean flag = false;
	
	ControllerSearch controllerSearch; //Strategy Pattern -- connection View -> Controller
	ControllerAnswer controllerAnswer;
	boolean searchBehaviour = false;
	
	private Model model;

	public View(Model model){
		this.model = model; 
	}
	
	
	public void setControllerSearch(ControllerSearch controllerSearch){ //Strategy Pattern
		this.controllerSearch = controllerSearch;
	}
	
	public void setControllerAnswer(ControllerAnswer controllerAnswer){ //Strategy Pattern
		this.controllerAnswer = controllerAnswer;
	}
	
	
	
	
	
	
	public void receiveUsersMessages() throws FileNotFoundException {
		
		
		
		 ArrayList<String> respostas = new ArrayList<String>();
		 
		
		 

		 int cont = 0;
		//infinity loop
		while (true){
		
			//taking the Queue of Messages
			updatesResponse =  bot.execute(new GetUpdates().limit(100).offset(queuesIndex));
			
			//Queue of messages
			List<Update> updates = updatesResponse.updates();

			//taking each message in the Queue
			for (Update update : updates) {
				
				System.out.println(cont);
				//updating queue's index
				queuesIndex = update.updateId()+1;
				
				if(flag == false){
					setControllerAnswer(new ControllerPerguntas(model, this));
					this.callPerguntas(update);
					System.out.println("chamei as perguntas");
					flag=true;
				}
				
				if(this.searchBehaviour==true){
					System.out.println(respostas);
					nome = update.message().text();
					System.out.println(nome);
					cont=0;
					setControllerSearch(new ControllerRespostas(model, this));
					this.callController(update,respostas);
					
				}
				else if(update.message().text().equals("s") || update.message().text().equals("n") || update.message().text().equals("S") || update.message().text().equals("N")){
					
					if(update.message().text().equals("s") || update.message().text().equals("S")){
						respostas.add((String) lista.get(cont));
					}
					
					cont++;
					
					if(cont == lista.size()){
						//enviar para o controller
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"Ok, uma ultima questão entre com seu nome: "));
						this.searchBehaviour=true;
						
					}else{
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"digite s ou n, você é/possui "+lista.get(cont)));
						
					}
				}else{
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),"digite s ou n, você é/possui "+lista.get(cont)));
				}
				
			}

		}
		
		
	}
	
	
	
	public void callController(Update update, Object respostas){
		System.out.println("chamou o controller");
		this.controllerSearch.search(update,respostas);
	}
	
	public void callPerguntas(Update update){
		this.controllerAnswer.searchPerguntas(update);
	}
	
	
	
	public void update(long chatId, String profissoes){
		sendResponse = bot.execute(new SendMessage(chatId, nome+" você pode ser "+profissoes));
		this.searchBehaviour = false;
	}
	
	public void update(Long chatId, ArrayList lista){
		this.lista = lista;
	}


	public void sendTypingMessage(Update update){
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}
	
	
	

}