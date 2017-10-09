package mvc;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pengrad.telegrambot.model.Update;

public class Model implements Subject{
	
	private static final Update Update = null;


	private List<Observer> observers = new LinkedList<Observer>();
	
	
	private static Model uniqueInstance;
	
	private Model(){}
	
	public static Model getInstance(){
		if(uniqueInstance == null){
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}
	
	public HashMap<String, List<String>> gerarPerguntas(){
		
		Gson gson = new Gson();

		try (Reader reader = new FileReader("G:/_Programas/eclipse/eclipse/workspace/botTeste/generated.txt"))
		{
						
			//Codigo para funcionar a leitura do json 
		    Type listType =
		           new TypeToken<HashMap<String, HashMap<String, List<String>>>>(){}.getType();
		    HashMap<String, HashMap<String, List<String>>> c = gson.fromJson(reader, listType);
		    HashMap<String, List<String>> get = c.get("profissao");
		    Set<String> items =  get.keySet();
		    
		    return  get;
		}catch (IOException e) 
		{    
		}
		
		return null;
		
		
	}
	
	public void  retornarLista(Update update){
		HashMap<String, List<String>> get = (HashMap<String, List<String>>) gerarPerguntas();
		Set<String> items =  get.keySet();
		 
		ArrayList lista = new ArrayList();
		
		for(String item: items){
			 
			 for(String ic: get.get(item)){
				lista.add(ic);
			 }
		 }
		
		this.notifyObservers(update.message().chat().id(), lista);
		
	}
	
	private void notifyObservers(Long chatId, ArrayList lista) {
			for(Observer observer:observers){
				observer.update(chatId, lista);
			}
	}

	
	
	
	public void enviaResultado(Update update,ArrayList lista){
		System.out.println("cheguei no model");
		ArrayList respostas = lista;
		String[] resp = (String[]) respostas.toArray(new String[respostas.size()]);
        
        
        //Le o arquivo e compara as repostas
	    ArrayList valores = new ArrayList();
        int maior = 0;
        
        HashMap<String, List<String>> get = (HashMap<String, List<String>>) gerarPerguntas();
		Set<String> items =  get.keySet();
		
        //Comparar as repostas
        for(String item: items){
        	
        	int cont = 0;
        	int a = 0;
        	for(String ic: get.get(item)){
        		
        		for(int y=0; y < resp.length;y++)
            		
        			if(ic.toString().equals(resp[y])){
            			a++;
            			System.out.println(ic);
            			break;
            		}
	    		
	    	}
        	System.out.println(a);
        	if(a == 0){
        		
        	}
        	else if(a == maior){
        		valores.add(item);
        		maior = a;
        	}
        	else if(a > maior){
        		valores.clear();
        		valores.add(item);
        		maior = a;
        	}
        }
        
        System.out.println(valores.size());
        
        Object[] profissoes = valores.toArray();
        
       
        
        
		if(profissoes.length == 1){
			System.out.println("chamei a view");
        	this.notifyObservers(update.message().chat().id(), (String) profissoes[0]);
        	
        }else if(profissoes.length > 1){
        	for(int w = 0 ; w < profissoes.length; w++){
        		System.out.println("chamei a view");
        		this.notifyObservers(update.message().chat().id(), (String) profissoes[w]);
        		
        	}
        }
        
        
		
	}
	
	
	
	public void registerObserver(Observer observer){
		observers.add(observer);
	}
	
	public void notifyObservers(long chatId, String profissoes){
		for(Observer observer:observers){
			observer.update(chatId, profissoes);
		}
	}

	
	
	/*
	public void addStudent(Student student){
		this.students.add(student);
	}
	
	public void addTeacher(Teacher teacher){
		this.teachers.add(teacher);
	}
	
	public void searchStudent(Update update){
		String studentsData = null;
		for(Student student: students){
			if(student.getName().equals(update.message().text())){
				studentsData = student.getAcademicNumber();
			}
		}
		
		if(studentsData != null){
			this.notifyObservers(update.message().chat().id(), studentsData);
		} else {
			this.notifyObservers(update.message().chat().id(), "Student not found");
		}
		
	}
	
	public void searchTeacher(Update update){
		String teachersData = null;
		for(Teacher teacher:teachers){
			if(teacher.getName().equals(update.message().text())) teachersData = teacher.getField();
		}
		
		if(teachersData != null){
			this.notifyObservers(update.message().chat().id(), teachersData);
		} else {
			this.notifyObservers(update.message().chat().id(), "Teacher not found");
		}
		
	}*/

}
