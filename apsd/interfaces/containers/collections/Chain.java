package apsd.interfaces.containers.collections;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.sequences.RemovableAtSequence;

public interface Chain<Data> extends RemovableAtSequence<Data>, Set<Data> { // Must extend RemovableAtSequence

  // InsertIfAbsent
  default boolean InsertIfAbsent(final Data val) {
    if (!Exists(val)) { return Insert(val); } // true
    return false;
  }

  // RemoveOccurrences
  default void RemoveOccurrences(final Data val) {
    // Richiamo il metodo Filter ereditato da Collection.
    // Filter rimuove gli elementi per i quali il predicato restituisce 'false'.
    // Vengono rimossi tutti gli elementi per cui `dat.equals(val)` è vero.
    Filter(dat -> !dat.equals(val));
  }
  
  // SubChain
  default Chain<Data> SubChain(final Natural from, final Natural to) {
    return (Chain<Data>) SubSequence(from, to);
  }
  
  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  default Natural Search(final Data val) {
    long size = Size().ToLong();
    for (long i = 0; i < size; i++) {
      Natural currentIndex = Natural.Of(i);
      if (GetAt(currentIndex).equals(val)) { return currentIndex; }
    }
    return null;
  }
  

}
