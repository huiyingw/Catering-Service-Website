package domain.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import database.query.StorableInterface;
import database.api.DefaultDBInterface;
import database.query.StorageDriverException;
import database.jdbc.JDBCException;
import domain.entityclasses.Event;

public class EventRangeQuery implements StorableInterface{

  public EventRangeQuery(DateRange queryRange){
    range = queryRange;
  }

  public ArrayList<Event> getEventList(){
    return eventsInRange;
  }

  public Event getEvent(long id){
    for(Event e : eventsInRange){
      if (e.getId() == id){
        return e;
      }
    }
    return null;
  }

  public boolean hasEvent(Event e){
    return eventsInRange.contains(e);
  }

  public boolean hasEvent(long id){
    boolean found = false;
    List ret = eventsInRange.stream().filter(x -> x.getId() == id).collect(Collectors.toList());
    return ret.size() > 0;
  }

  //StorableInterface --------------------------------
  public String getStorageDriver(){
    return "event_range_query";
  }

  public ArrayList<Object> getStorageValues(){
    ArrayList<Object> output = new ArrayList<>();
    output.add(range.getStartISO());
    output.add(range.getEndISO());
    return output;
  }

  public void constructFromStorageValues(ArrayList<Object> vals){
    // vals is a list of event id's that fall in the specified range
    eventsInRange.clear();

    for(Object id : vals){
      Event newEvent = new Event();
      newEvent.setId(((Number)id).longValue());

      try{
        DefaultDBInterface dbI = DefaultDBInterface.getInstance();
        dbI.load(newEvent);
        eventsInRange.add(newEvent);
      }
      catch(StorageDriverException|JDBCException e){
        continue;//cant load this one :(
      }
    }
  }

  public String getStorageLocation(){
    return "events";
  }
  ////////////////////////////////////////////////////

  private DateRange range;
  private ArrayList<Event> eventsInRange = new ArrayList<>();
}
