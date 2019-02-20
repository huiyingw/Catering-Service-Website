package domain.util;

//I think this interface explains its self.
public interface Identifiable {
  public abstract long    getId();
  public abstract void    setId(long id);
  public abstract String  getName();
  public abstract void    setName(String name);
}
