package apsd.interfaces.containers.sequences;

import apsd.classes.utilities.Natural;

/** Interface: Sequence con supporto alla rimozione di un dato tramite posizione. */
public interface RemovableAtSequence<Data> extends Sequence<Data> {

  // RemoveAt
  // AtNRemove
  void RemoveAt(final Natural index);
  Data AtNRemove(final Natural index);

  // RemoveFirst
  // FirstNRemove
  default void RemoveFirst() {
    if (!IsEmpty()) {
      RemoveAt(Natural.ZERO);
    }
  }

    default Data FirstNRemove() {
    if (IsEmpty()) {
      return null;
    }
    return AtNRemove(Natural.ZERO);
  }

  // RemoveLast
  // LastNRemove
  default void RemoveLast() {
    if (!IsEmpty()) {
      RemoveAt(Size().Decrement());
    }
  }

  default Data LastNRemove() {
    if (IsEmpty()) {
      return null;
    }
    return AtNRemove(Size().Decrement());
  }

}
