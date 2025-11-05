package apsd.interfaces.containers.sequences;

// import apsd.classes.utilities.Box;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
// import apsd.interfaces.containers.iterators.ForwardIterator;

/** Interface: IterableContainer con supporto alla lettura e ricerca tramite posizione. */
public interface Sequence<Data> extends IterableContainer<Data> { // Must extend IterableContainer

  // GetAt
  Data GetAt(final Natural index);

  // GetFirst
  // GetLast
  default Data GetFirst() {
    if (IsEmpty()) {
      return null;
    }
    return GetAt(Natural.ZERO);
  }
  
  default Data GetLast() {
    if (IsEmpty()) {
      return null;
    }
    return GetAt(Size().Decrement());
  }

  // Search
  Natural Search(final Data value);

  // IsInBound
  default boolean IsInBound(final Natural index) {
    return index != null && index.compareTo(Size()) < 0;
  }

  // ExcIfOutOfBound
  default long ExcIfOutOfBound(Natural num) {
    if (num == null) throw new NullPointerException("Natural number cannot be null!");
    long idx = num.ToLong();
    if (idx >= Size().ToLong()) throw new IndexOutOfBoundsException("Index out of bounds: " + idx + "; Size: " + Size() + "!");
    return idx;
  }

  // SubSequence
  Sequence<Data> SubSequence(final Natural from, final Natural to);

}
