package web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import database.api.DefaultDBInterface;
import database.query.StorableInterface;
import database.query.StorageDriverBase;
import database.query.StorageDriverException;
import database.jdbc.JDBCException;
import java.util.Random;
import java.lang.Integer;


@Controller
public class LoginController {



	@MessageMapping("/hello")
    @SendTo("/topic/greetings")
	public Greeting checkExist(Account message)throws Exception{
		DefaultDBInterface dbI = DefaultDBInterface.getInstance();
	  	Account temp = new Account();
	    temp.setuserID(message.getuserID());


		try{
			dbI.load(temp);
		}
		catch (StorageDriverException e){
			return new Greeting (","+"ID not found");
		}
		
		if (temp.getPassword().equals(message.getPassword())){
			Random rand = new Random();
			int  n = rand.nextInt(Integer.MAX_VALUE) + 1;
			int  m = rand.nextInt(Integer.MAX_VALUE) + 1;
			int  t = rand.nextInt(Integer.MAX_VALUE) + 1;
			String type = "";
			if (temp.getuserType()==1){
				type ="m";
			}else if(temp.getuserType()==2){
				type ="e";
			}else{
				type ="w";
			}
			String token= String.format("%d%d%d",n,m,t);
			temp.setToken(token);
			dbI.store(temp);
			return new Greeting(token+","+type);

		}
		else if (!temp.getPassword().equals(message.getPassword())) {
			return new Greeting(","+"Password Wrong");
		}
		else{
			return new Greeting(","+"User ID Not found");
		}
	}

	
	
}