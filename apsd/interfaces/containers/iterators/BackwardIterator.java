package apsd.interfaces.containers.iterators;

import apsd.classes.utilities.Natural;
import apsd.interfaces.traits.Predicate;

/** Interface: Iteratore all'indietro. */
public interface BackwardIterator<Data> extends Iterator<Data> {

  // Prev
  default void Prev() { DataNPrev(); }

  default void Prev(final Natural num) {
    long count = num.ToLong();
    for (long i = 0; i < count && IsValid(); i++) {
      Prev();
    }
  }
  
  // DataNPrev
  abstract Data DataNPrev();

  // ForEachBackward
  default boolean ForEachBackward(final Predicate<Data> fun) {
    if (fun != null) {
      while (IsValid()) {
        if (fun.Apply(DataNPrev())) { return true; }
      }
    }
    return false;
  }

}
