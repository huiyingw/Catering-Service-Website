package database.jdbc;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class JDBCConnectionManager {

  private JDBCConnectionManager(){
    //nop
  }

  //construct this object and open database connection
  private JDBCConnectionManager(JDBCConnectionType conType) throws JDBCException{
    myConnType = conType;
    establishConnection(conType);
  }

  private static JDBCConnectionManager instance = null;
  public static JDBCConnectionManager getInstance(JDBCConnectionType conType) throws JDBCException{
    if(instance == null){
      instance = new JDBCConnectionManager(conType);
    }
    if(!instance.isConnected()){
      instance.establishConnection(conType);
    }

    return instance;
  }

  public static JDBCConnectionManager getInstance() throws JDBCException{
    return getInstance(JDBCConnectionType.MYSQL);
  }

  //open connection to database of conType based on config file data
  public void establishConnection(JDBCConnectionType conType) throws JDBCException{
    String connectionUrl = "";

    // load connection configeration
    switch(conType) {
      case MYSQL:
        connectionUrl = getMySQLConnectionString();
        break;
      case SQLITE:
        connectionUrl = getSQLiteConnectionString();
        break;
      default:
        throw new JDBCException("Unknown JDBC connection type!",JDBCExceptionType.ARGUMENT_ERROR);
    }

    //connect
    if(!connectToDB(connectionUrl)){
      throw new JDBCException("Could not connect to database with url: " + connectionUrl,
          JDBCExceptionType.CONNECTION_ERROR);
    }
  }

  //close database connection
  public void closeConnection() throws JDBCException{
    if(!disconnectFromDB()){
      throw new JDBCException("Could not release database connection",
          JDBCExceptionType.CONNECTION_ERROR);
    }
  }

  //connect to the database sepcified by dbUrl.
  private boolean connectToDB(String dbUrl){
    if(dbConnection == null || !isConnected()){
      try{
        dbConnection = DriverManager.getConnection(dbUrl,"program","seng330");
      }
      catch (SQLException e){
        System.out.println("ERROR: " + e);
        return false;
      }
    }
    else{
      return false;
    }

    return true;
  }

  //disconnect from db if connected
  private boolean disconnectFromDB(){
    if(dbConnection != null){
      try{
        dbConnection.close();
      }
      catch(SQLException e){
        return false;
      }
    }
    return true;
  }

  public ArrayList<HashMap<String, Object>> query(String queryString) throws JDBCException{
    if(dbConnection != null){
      try{
        Statement stm = dbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
        stm.execute(queryString);
        ResultSet resSet = stm.getResultSet();
        return ResultSetToArrayHash(resSet);
      }
      catch (SQLException e){
        throw new JDBCException("SQL Error: " + e.getMessage() + "\n on query: " + queryString ,
                                  JDBCExceptionType.QUERY_ERROR);
      }
    }
    else{
      throw new JDBCException("No Database connection", JDBCExceptionType.CONNECTION_ERROR);
    }
  }

  // returns an Array of query results. i.e. an Array of Arrays of Hashmaps.
  public ArrayList<ArrayList<HashMap<String, Object>>> multiQuery(String [] queries) throws JDBCException{
    ArrayList<ArrayList<HashMap<String, Object>>> res = new ArrayList<ArrayList<HashMap<String, Object>>>();

    for(String query : queries){
      res.add(query(query));
    }
    return res;
  }

  public JDBCConnectionType getConnectionType(){
    return myConnType;
  }

  // convert a java.sql.ResultSet to a ArrayList<HashMap<String, Object>>.
  // where there is a HashMap for every row. Said hashmap holds key, val pairs
  // column_name, column_value.
  private ArrayList<HashMap<String, Object>> ResultSetToArrayHash(ResultSet resSet) throws SQLException{
    ArrayList<HashMap<String, Object>> queryResults = new ArrayList<>();
    if(resSet != null){
      try{
        ResultSetMetaData columnInfo = resSet.getMetaData();
        while(resSet.next()){
          HashMap<String, Object> rowHashMap = new HashMap<>();

          for(int i =1; i < columnInfo.getColumnCount()+1; i++){
            rowHashMap.put(columnInfo.getColumnName(i),resSet.getObject(i));
          }
          queryResults.add(rowHashMap);
        }
      }
      finally{
        resSet.close();
      }
    }

    return queryResults;
  }

  public boolean isConnected(){
    try{
      return dbConnection == null ? false : dbConnection.isValid(5);
    }
    catch(SQLException e){
      return false;
    }
  }

  // get the JDBC connection string for mysql from config file
  private String getMySQLConnectionString(){
    return "jdbc:mysql://localhost:3306/data";
  }

  // get the JDBC connection string for sqlite from config file
  private String getSQLiteConnectionString(){
    return "jdbc:sqlite:/tmp/sqlite_tmp.db";
  }

  // JDBC connection handle
  private Connection dbConnection = null;
  private JDBCConnectionType myConnType = JDBCConnectionType.NONE;
}
