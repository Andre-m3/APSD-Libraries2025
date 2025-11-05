package apsd.interfaces.containers.base;

import apsd.classes.utilities.Natural;

/** Interface: Container, la base di tutti i contenitori. */
public interface Container {

  Natural Size();

  default boolean IsEmpty() { return Size().IsZero(); }

}
