package domain.api.eventfactory;

import java.util.Date;

import domain.entityclasses.EventDetails;
import domain.entityclasses.CustomerInfo;

// event info structure. fill this out an pass in to EventFactory
// to create a new event object.
public class EventInfo{
  public String eventName;
  public Date eventStart;
  public Date eventEnd;

  public EventDetails eventDetails = new EventDetails();
  public CustomerInfo customerInfo = new CustomerInfo();
}
