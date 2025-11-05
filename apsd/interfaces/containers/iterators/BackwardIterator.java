package apsd.interfaces.containers.iterators;

import apsd.classes.utilities.Natural;
import apsd.interfaces.traits.Predicate;

/** Interface: Iteratore all'indietro. */
public interface BackwardIterator<Data> extends Iterator<Data> {

  // Prev
  void Prev();
  void Prev(final Natural num);
  
  // DataNPrev
  Data DataNPrev();

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
