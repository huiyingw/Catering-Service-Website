package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import domain.api.Domain;
import domain.api.DomainException;
import domain.entityclasses.Event;

@Controller
public class ManagerController{

	@RequestMapping("/manager")
	public String route(Model model){
    return "manager";
	}

  // display info page for specific event!
  @RequestMapping("/manager/event")
  public String route(@RequestParam(value="eid", required=true) String eid, Model model){
    try{
      Domain domain = Domain.getInstance();
      Event event = domain.getEventById(Integer.parseInt(eid));
      if(event != null){
        model.addAttribute("name", event.getName());
        model.addAttribute("event_desc", event.toString());
      }
    }
    catch(DomainException e){
      System.out.println(e.getMessage());
      return "";
    }

    String [][] employeeArray = {{"Jon","123"},{"Frank","456"}};
    String [][] equipmentArray = {{"MacNChees","234"}, {"Tables","63464"}};
    model.addAttribute("employees",employeeArray);
    model.addAttribute("equipment",equipmentArray);
    return "eventInfo";
  }
}
