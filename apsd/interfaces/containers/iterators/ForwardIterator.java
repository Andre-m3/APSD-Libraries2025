package apsd.interfaces.containers.iterators;

import apsd.classes.utilities.Natural;
import apsd.interfaces.traits.Predicate;

/** Interface: Iteratore in avanti. */
public interface ForwardIterator<Data> extends Iterator<Data> {

  // Next
  default void Next() { DataNNext(); }

  default void Next(final Natural num) {
    long count = num.ToLong();
    for (long i = 0; i < count && IsValid(); ++i) {
      Next();
    }
  }

  // DataNNext
  abstract Data DataNNext();

  // ForEachForward
  default boolean ForEachForward(Predicate<Data> fun) {
    if (fun != null) {
      while (IsValid()) {
        if (fun.Apply(DataNNext())) { return true; }
      }
    }
    return false;
  }

}
