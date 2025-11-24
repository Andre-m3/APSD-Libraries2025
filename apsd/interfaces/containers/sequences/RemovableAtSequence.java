package apsd.interfaces.containers.sequences;

import apsd.classes.utilities.Natural;

/** Interface: Sequence con supporto alla rimozione di un dato tramite posizione. */
public interface RemovableAtSequence<Data> extends Sequence<Data> {

  // RemoveAt
  default void RemoveAt(final Natural index) { AtNRemove(index); }
  
  // AtNRemove -> getAt and RemoveAt
  Data AtNRemove(final Natural index);

  // RemoveFirst
  default void RemoveFirst() {
    RemoveAt(Natural.ZERO);
  } // If IsEmpty() -> RemoveAt will throw new Exception!

  // FirstNRemove
  default Data FirstNRemove() {
    if (IsEmpty()) { return null; }
    return AtNRemove(Natural.ZERO);
  } // If isEmpty() -> nothing to remove, but must return null pointer!

  // RemoveLast
  default void RemoveLast() {
    if (IsEmpty()) throw new IndexOutOfBoundsException("Sequence is empty");
    RemoveAt(Size().Decrement());
  } // If isEmpty() -> we must handle Size().Decrement(), so we throw new Exception!

  // LastNRemove
  default Data LastNRemove() {
    if (IsEmpty()) { return null; }
    return AtNRemove(Size().Decrement());
  } // If isEmpty() -> nothing to remove, but must return null pointer

}
