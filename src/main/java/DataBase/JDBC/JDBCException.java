package database.jdbc;

import java.lang.Exception;

public class JDBCException extends Exception {
  public JDBCException (String str, JDBCExceptionType exType){
    super(str);
  }

  public JDBCExceptionType getType(){
    return exceptionType;
  }

  private JDBCExceptionType exceptionType = JDBCExceptionType.UNKNOWN;
}
