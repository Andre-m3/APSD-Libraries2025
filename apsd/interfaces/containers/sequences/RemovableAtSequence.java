package apsd.interfaces.containers.sequences;

import apsd.classes.utilities.Natural;

/** Interface: Sequence con supporto alla rimozione di un dato tramite posizione. */
public interface RemovableAtSequence<Data> extends Sequence<Data> {

  // RemoveAt     //*******************//
  void RemoveAt(final Natural index);   // Must be implemented
  
  // AtNRemove -> getAt and RemoveAt
  Data AtNRemove(final Natural index);

  // RemoveFirst
  default void RemoveFirst() {
    if (!IsEmpty()) { RemoveAt(Natural.ZERO); }
  } // If isEmpty() -> nothing to remove!

  // FirstNRemove
  default Data FirstNRemove() {
    if (IsEmpty()) { return null; }
    return AtNRemove(Natural.ZERO);
  } // If isEmpty() -> nothing to remove, but must return null pointer!

  // RemoveLast
  default void RemoveLast() {
    if (!IsEmpty()) { RemoveAt(Size().Decrement()); }
  } // If isEmpty() -> nothing to remove!

  // LastNRemove
  default Data LastNRemove() {
    if (IsEmpty()) { return null; }
    return AtNRemove(Size().Decrement());
  } // If isEmpty() -> nothing to remove, but must return null pointer

}
