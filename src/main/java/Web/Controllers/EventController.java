package controllers;

import java.util.*;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import domain.entityclasses.Event;
import database.api.DebugDBInterface;
import database.jdbc.JDBCConnectionType;
import domain.api.eventfactory.*;
import domain.api.Domain;
import domain.api.DomainException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.lang.Integer;



@Controller
public class EventController {

    @GetMapping("/offers")
    public String OffersForm( Model model) {
        return "offers";
    }

    
    @RequestMapping("offers")
    public String sendinput(@RequestParam Map<String, String> requestParams, Model model)throws ParseException{

        String name=requestParams.get("name");
        String email=requestParams.get("email");
        String phoneNumber=requestParams.get("phoneNumber");
        String eventName=requestParams.get("eventName");
        String location=requestParams.get("location");
        String start = requestParams.get("start");
        //Date newstart = ISO8601.parse(start);
        String end = requestParams.get("end");
        //Date newend = ISO8601.parse(end);
        String numGuests = requestParams.get("numGuests");
        String themeName = requestParams.get("themeName");
        String customerMessage = requestParams.get("customerMessage");
        

    	model.addAttribute("name",name);
    	model.addAttribute("email",email);
    	model.addAttribute("phoneNumber",phoneNumber);
    	model.addAttribute("event",eventName);
    	model.addAttribute("location",location);
    	model.addAttribute("start",start);
        model.addAttribute("end",end);
    	model.addAttribute("numGuests",numGuests);
    	model.addAttribute("cusine",themeName);
    	model.addAttribute("message",customerMessage);

       
        DateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newstart = ISO8601.parse(start);
        Date newend = ISO8601.parse(end);
        

        int newnumGuests = Integer.parseInt(numGuests);

        //create new event object;
        EventInfo info  = new EventInfo();
        info.eventName = eventName;
        info.eventStart = newstart;
        info.eventEnd = newend;
        info.eventDetails.location= location;
        info.eventDetails.numGuests= newnumGuests;
        info.eventDetails.themeName= themeName;
        info.eventDetails.customerMessage = customerMessage;
        info.customerInfo.name= name;
        info.customerInfo.email= email;
        info.customerInfo.phoneNumber = phoneNumber;
        //store into DB;
        try{
            Event newEvent = EventFactory.createNewEvent(info);
        }
        catch(DomainException e){
            System.out.println("The event is not created!");
        }
        //start schedular;
    	return "customerResult";
    }

}