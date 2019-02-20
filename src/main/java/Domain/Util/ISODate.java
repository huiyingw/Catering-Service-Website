package domain.util;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

// change Dates toString method such that it produces an ISO8601 date-time string (needed for storage)
public class ISODate extends Date{

  public ISODate(){
    super();
  }

  public ISODate(Date d){
    super(d.getTime());
  }

  public ISODate(String s) throws ParseException{
    super();

    Date date = ISO8601.parse(s);
    setTime(date.getTime());
  }

  public void fromDate(Date d){
    if(d != null){
      setTime(d.getTime());
    }
  }

  public Date toDate(){
    return new Date(getTime());
  }

  public String toString(){
    return ISO8601.format(this);
  }
  private static final DateFormat ISO8601 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
}
