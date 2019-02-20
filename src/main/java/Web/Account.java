package web;

import java.util.Date;
import java.util.ArrayList;
import domain.util.DateRange;
import database.query.StorableInterface;
import database.query.driver.AccountStorageDriver;



public class Account implements StorableInterface{

    private String password;
    private String userID;
    private int userType;
    private String Token;


    public Account() {
    }

    public Account(String userID, String password) {
        this.userID=userID;
        this.password = password;

    }

    public String getToken(){
        return Token;
    }

    public String getPassword() {
        return password;
    }

    public String getuserID() {
        return userID;
    }

    public void setToken(String Token){
        this.Token=Token;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setuserID(String userID){
        this.userID=userID;
    }

    public void setuserType(int type){
      userType = type;
    }

    public int getuserType(){
      return userType;
    }

    @Override
    public String getStorageDriver(){
        return "account";
    }

    @Override
     public ArrayList<Object> getStorageValues(){
        ArrayList<Object> temp = new ArrayList<>();
        temp.add((Object)userID);
        temp.add((Object)password);
        temp.add((Object)userType);
        temp.add((Object)Token);
        return temp;
    }

    @Override
    public void constructFromStorageValues(ArrayList<Object> vals){
      setPassword((String)vals.get(0));
      setuserType((Integer)vals.get(1));
      setToken((String)vals.get(2));
    }

    @Override
    public String getStorageLocation(){
        return "accounts";
    }
}
