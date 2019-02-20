package domain.api.repo;

import java.util.function.Function;
import java.util.ArrayList;

import domain.entityclasses.Schedule;
import domain.entityclasses.Schedulable;
import domain.entityclasses.Event;
import domain.util.DateRange;
import domain.util.AdvancedCast;

public class EventRepo implements RepoInterfaceTimed<Event>{
  public EventRepo(){

  }

  // get object by id
  @Override
  public Event getById(long id){
    return (Event)eventSchedule.getById(id);
  }

  // returns object(s) with name
  @Override
  public ArrayList<Event> getByName(String name){
    return AdvancedCast.<Schedulable,Event>cast(eventSchedule.getByName(name));
  }

  // return a list of objects that where accepted by filer function.
  @Override
  public ArrayList<Event> getByFilter(Function<Event,Boolean> filter){
    ArrayList<Event> result = new ArrayList<>();
    for(Object e : eventSchedule){
      Event event = (Event)e;
      if(filter.apply(event)){
        result.add(event);
      }
    }
    return result;
  }

  //return list of objects within the given date range.
  @Override
  public ArrayList<Event> getByDate(DateRange dr){
    ArrayList eventsSched = eventSchedule.getInRange(dr);
    ArrayList<Event> events = new ArrayList<>();
    for(Object o : eventsSched){
      events.add((Event)o);
    }
    return events;
  }

  // add object obj to repo.
  @Override
  public void add(Event obj){
    eventSchedule.add(obj);
  }

  // remove object matching obj from repo
  @Override
  public void remove(Event obj){
    eventSchedule.remove(obj);
  }

  // remove object from repo with matching id
  @Override
  public void remove(long id){
    eventSchedule.remove(id);
  }

  // get the count of objects stored in this repo
  @Override
  public int size(){
    return eventSchedule.size();
  }

  // flush this repo to disk i.e. store all contents in the DB.
  @Override
  public void flushToDisk(){
    //most likely will never get done.
  }

  private Schedule eventSchedule = new Schedule();
}
