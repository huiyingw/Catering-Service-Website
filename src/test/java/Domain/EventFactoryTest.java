import org.junit.*;
import java.util.Date;

import database.api.DebugDBInterface;
import database.jdbc.JDBCConnectionType;
import domain.api.eventfactory.*;
import domain.api.Domain;
import domain.entityclasses.Event;

public class EventFactoryTest{


  @Test
  public void testEventCreate() throws Exception{
    DebugDBInterface dbI = DebugDBInterface.getInstance();
    dbI.ensureConnection(JDBCConnectionType.SQLITE);
    dbI.NUKE();
    dbI.initializeDB();

    EventInfo info  = new EventInfo();
    info.eventName  = "POWER";
    info.eventStart = new Date(2017 - 1900, 10,25,0,0,0);
    info.eventEnd   = new Date(2017 - 1900, 10,29,0,0,0);
    info.eventDetails.location        = "Yo Momas House";
    info.eventDetails.numGuests       = 123;
    info.eventDetails.themeName       = "BBQ";
    info.eventDetails.customerMessage = "dont mess this up!";
    info.customerInfo.name        = "Frank Drebin, Police Squad";
    info.customerInfo.email       = "frank@police.squad.net";
    info.customerInfo.phoneNumber = "123 4531 8592";

    Event newEvent = EventFactory.createNewEvent(info);
    Event loadEvent = new Event();
    loadEvent.setId(newEvent.getId());
    dbI.load(loadEvent);

    assert(newEvent.getName().equals(loadEvent.getName()));

    Domain dI = Domain.getInstance();
    dI.removeEvent(loadEvent);
  }

}
