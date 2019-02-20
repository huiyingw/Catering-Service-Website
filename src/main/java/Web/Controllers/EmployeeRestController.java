package controllers;

import java.util.Date;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import domain.api.Domain;
import domain.api.DomainException;

@RestController
class EmployeeRestController {

  @RequestMapping("/employee/events")
  public String serveEvents(@RequestParam(value="start") String start_date, @RequestParam(value="end") String end_date){
    try{
      Domain dom = Domain.getInstance();
      return dom.convertEventsToJSON(dom.filterEvents((e) -> true));
    }
    catch(DomainException e){
      System.out.println("Call an Admin! Events could not be served to manager event calendar!");
      return "";
    }
  }

}
