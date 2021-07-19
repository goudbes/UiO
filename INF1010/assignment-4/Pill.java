/**
*Interface for pills
*/
public interface Pill {

  /**
  *Sets number of pills
  *@param numberOfPills Number of pills
  */
  void setNumberOfPills(int numberOfPills);
  /**
  * Returns number of pills
  */
  int getNumberOfPills();
  /**
  *Sets the amount of active substance in 1 pill
  *@param activeSubstance Active substance (mg) in 1 pill
  */
  void setActiveSubstance(double activeSubstance);
  /**
  *Returns the amount of active substance (mg) in one pill
  */
  double getActiveSubstance();
}
