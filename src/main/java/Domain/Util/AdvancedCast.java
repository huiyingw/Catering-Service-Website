package domain.util;

import java.util.ArrayList;
import java.util.HashMap;

public class AdvancedCast {

  //convert arraylist of type B to one of type T.
  public static <B,T> ArrayList<T> cast(ArrayList<B> list){
    ArrayList<T> out = new ArrayList<>();
    for(B at : list){
      out.add((T)at);
    }
    return out;
  }

  public static <BK,BV,TK,TV> HashMap<TK,TV> cast(HashMap<BK,BV> hash){
    final HashMap<TK, TV> out = new HashMap<>();
    hash.forEach((k,v)->out.put((TK)k,(TV)v));
    return out;
  }

}
