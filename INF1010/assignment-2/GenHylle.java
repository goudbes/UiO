public interface GenHylle<T> {
  public boolean insert(T t, int place);
  public boolean isEmpty(int place);
  public T take(int place);
  public int size();
}
