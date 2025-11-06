package apsd.interfaces.containers.base;

import apsd.classes.utilities.Natural;
import apsd.interfaces.traits.Reallocable;

/** Interface: ClearableContainer che è anche Reallocable. */
public interface ReallocableContainer extends ClearableContainer, Reallocable{

  double GROW_FACTOR = 2.0; // Must be strictly greater than 1.
  double SHRINK_FACTOR = 2.0; // Must be strictly greater than 1.
  
  // Capacity (da qui faremo distinzione con size)
  Natural Capacity();

  // Grow
  default void Grow() {
    Realloc(Natural.Of((long) (Capacity().ToLong() * GROW_FACTOR)));
  }   // **** Se dim è 0? dovrei verificare questa condizione? Controllare in futuro!
  default void Grow(Natural dim){
    Realloc(Natural.Of(Capacity().ToLong() + dim.ToLong()));
  }

  // Shrink
  default void Shrink(){
    Realloc(Natural.Of((long) (Capacity().ToLong() / SHRINK_FACTOR)));
  }

  /* ************************************************************************ */
  /* Override specific member functions from Container                        */
  /* ************************************************************************ */

  // ...

  /* ************************************************************************ */
  /* Override specific member functions from ClearableContainer               */
  /* ************************************************************************ */

  // ...

}
