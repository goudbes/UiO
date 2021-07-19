public interface TilUtlaan {
  /**
   * Lend an object.
   * @param borrower The name of the object's owner.
   */
  public void lend(String borrower);

  /**
   * Returns an object.
   */
  public void returnBack();
}
