package apsd.interfaces.containers.sequences;

import apsd.classes.utilities.Natural;

/** Interface: Sequence con supporto all'inserimento di un dato tramite posizione. */
public interface InsertableAtSequence<Data> extends Sequence<Data> {

  // InsertAt
  void InsertAt(final Data value, final Natural index);

  // InsertFirst
  default void InsertFirst(final Data value) { InsertAt(value, Natural.ZERO); }

  // InsertLast              //* ******* size()-nat.one? ******* *//
  default void InsertLast(final Data value) { InsertAt(value, Size()); }

}