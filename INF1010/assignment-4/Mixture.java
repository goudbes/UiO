/**
*Interface for mixtures
*/
public interface Mixture {

  /**
  *Sets the volume of the bottle
  *@param volume Volume of the bottle
  */
  void setVolume(double volume);

  /**
  *Returns the volume of the bottle
  */
 double getVolume();

  /**
  *Sets the amount of active substance in 1 ml
  *@param activeSubstance Amount of active substance in 1 ml
  */
  void setActiveSubstance(double activeSubstance);

  /**
  *Returns the amount of active substance in 1 ml
  */
  double getActiveSubstance();
}
