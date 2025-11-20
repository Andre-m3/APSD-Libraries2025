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
  default void RemoveOccurrences(Data val) {
    Natural i = Natural.ZERO;
    while (i.compareTo(Size()) < 0) {
      Data curr = GetAt(i);
      
      // Match Sicuro. Se curr è null, viene rimosso solo se anche val è null (improbabile/impossibile?).
      boolean match = (curr == null) ? (val == null) : curr.equals(val);
      
      if (match) RemoveAt(i); // 'i' non incrementa perché l'elemento successivo è scivolato nella posizione corrente
      else i = i.Increment(); // Passiamo al prossimo solo se non abbiamo rimosso
    }
  }
  
  // SubChain
  default Chain<Data> SubChain(final Natural from, final Natural to) {    /////***** SUB CHAIN *****/////
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
