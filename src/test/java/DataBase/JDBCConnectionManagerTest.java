import org.junit.*;
import database.jdbc.JDBCConnectionManager;
import database.jdbc.JDBCConnectionType;
import database.jdbc.JDBCException;
import database.jdbc.JDBCExceptionType;
import java.util.ArrayList;
import java.util.HashMap;


// test JDBCConnectionManager with sqlite db
// I limit tests to sqlite db so that other devs need not install MySQL server on there machines.
// however the production server does operate on MySQL.
public class JDBCConnectionManagerTest {

  public JDBCConnectionManagerTest() throws JDBCException{
    conManager = JDBCConnectionManager.getInstance(JDBCConnectionType.SQLITE);
  }

  // connect, create database + table + insert in to table + query table
  @Test
  public void queryDatabaseSQLite(){
    try{

      //set up a simple data base
      conManager.query("CREATE TABLE IF NOT EXISTS testTable (text VARCHAR(20));");
      conManager.query("INSERT INTO testTable VALUES (\"HELLO WORLD\");");
      ArrayList<HashMap<String, Object>> rSet = conManager.query("SELECT * FROM testTable;");

      //check that our sql querys worked
      assert(rSet != null);
      assert(rSet.size() > 0);
      assert(rSet.get(0).get("text").equals("HELLO WORLD"));

      conManager.query("DROP TABLE testTable;");
    }
    catch(JDBCException e){
      System.out.println(e.getMessage());
      assert(false);
    }

  }

  JDBCConnectionManager conManager = null;
}
