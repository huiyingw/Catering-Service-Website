package domain.util;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import domain.util.DateRange;

import domain.entityclasses.Event;

//convert(s) domain objects to json objects
public class JSONification{

  public static String eventToJSON(Event event){
    String name       = event.getName();
    String id         = ((Long)event.getId()).toString();
    String startDate  = ISO8601.format(event.getDateRange().getStart());
    String endDate    = ISO8601.format(event.getDateRange().getEnd());

    return String.format("{%s: \"%s\",%s: \"%s\",%s: \"%s\",%s: \"%s\"}",
        TITLE, name, ID, id, START, startDate, END, endDate);
  }

  public static String eventToJSON(ArrayList<Event> events){
    String eventsJSON = "";

    for(Event e : events){
      eventsJSON = joinJSON(eventsJSON, eventToJSON(e));
    }

    return eventsJSON;
  }

  public static String joinJSON(String s1, String s2){
    if(s1.isEmpty()){
      return s2;
    }
    else if (s2.isEmpty()){
      return s1;
    }
    else{
      return s1 + ",\n" + s2;
    }
  }

  public static String encapsulateJSON(String tag, String list){
    return tag + ": [" + list + "]";
  }

  // full calidar JSON tags
  private static final String TITLE = "\"title\"";
  private static final String START = "\"start\"";
  private static final String END   = "\"end\"";
  private static final String ID    = "\"id\"";
  private static final DateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
}
