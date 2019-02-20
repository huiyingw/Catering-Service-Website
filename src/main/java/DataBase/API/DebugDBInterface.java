package database.api;

import database.jdbc.JDBCConnectionManager;
import database.jdbc.JDBCConnectionType;
import database.jdbc.JDBCException;

// debug db interface used in test cases
public class DebugDBInterface extends DefaultDBInterface {

  protected DebugDBInterface() throws JDBCException{super();}

  // singleton instance
  private static DebugDBInterface ptrDebugDBInterface = null;
  //get singleton
  public static DebugDBInterface getInstance() throws JDBCException{
    if(ptrDebugDBInterface == null){
      ptrDebugDBInterface = new DebugDBInterface();
      ptrDebugDBInterface.conManager = ptrDebugDBInterface.connectToDB();
    }
    return ptrDebugDBInterface;
  }

  @Override
  protected JDBCConnectionManager connectToDB() throws JDBCException{
    return JDBCConnectionManager.getInstance(JDBCConnectionType.SQLITE);
  }

  // default setup for the database (create tables etc.)
  public void initializeDB() throws DBException{
    try{
      String [] queryStrs = {
          "create table  if not exists employees (id bigint primary key , asset_id bigint, schedule_id bigint, name varchar(256));",
          "create table  if not exists equipment (id bigint primary key , asset_id bigint, schedule_id bigint, name varchar(256));",
          "create table  if not exists events    (id bigint primary key , start_time datetime, end_time datetime, name varchar(256));",
          "create table  if not exists events_resources (id bigint primary key, resource_id bigint, asset_type bigint);",
          "create table  if not exists accounts  (id varchar(256) primary key , password varchar(256), user_type int, token varchar(256));"};
      conManager.multiQuery(mysqlsqliteCompatify(queryStrs));
    }
    catch (JDBCException e){
      throw new DBException("Could not initialize data base w/ error:\n" + e.getMessage());
    }
  }

  //nuke the db
  public void NUKE() throws JDBCException{
    conManager.query("DROP TABLE IF EXISTS employees;");
    conManager.query("DROP TABLE IF EXISTS equipment;");
    conManager.query("DROP TABLE IF EXISTS events;");
    conManager.query("DROP TABLE IF EXISTS events_resources;");
    conManager.query("DROP TABLE IF EXISTS accounts;");
  }

  private String[] mysqlsqliteCompatify(String [] strs) throws DBException{

    String autoIncrementKeyword = "auto_increment";
    String integerKeyword = "bigint";
    switch(conManager.getConnectionType()){
      case SQLITE:
        autoIncrementKeyword = "autoincrement";
        integerKeyword = "INTEGER";
        break;
      case MYSQL:
        autoIncrementKeyword = "auto_increment";
        integerKeyword = "bigint";
        break;
      default:
        throw new DBException("Could not convert database types error: No database connection!");
    }

    for(int i =0; i < strs.length; i++){
      strs[i] = strs[i].replaceAll("(?<=\\s)bigint(?=[,\\s])",integerKeyword);
      strs[i] = strs[i].replaceAll("auto_increment(?=[,\\s])", autoIncrementKeyword);
    }
    return strs;
  }
}
