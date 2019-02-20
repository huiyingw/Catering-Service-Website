package domain.util;

import java.util.Date;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class DateRange{

  public DateRange(){
    start = null;
    end = null;
  }

  public DateRange(Date start, Date end){
    this.start = start;
    this.end = end;
  }

  public Date getStart(){
    return start;
  }


  public ISODate getStartISO(){
    return new ISODate(start);
  }

  public Date getEnd(){
    return end;
  }

  public ISODate getEndISO(){
    return new ISODate(end);
  }

  public Duration getDuration(){
    return Duration.of(end.getTime() - start.getTime(), ChronoUnit.SECONDS);
  }

  public boolean contains(DateRange other){
    if(other.getStart().compareTo(start) >= 0 && other.getStart().compareTo(end) <= 0){
      return true;
    }
    else if (other.getEnd().compareTo(end) <= 0 &&  other.getEnd().compareTo(start) >= 0){
      return true;
    }
    return false;
  }

  private Date start;
  private Date end;
}
