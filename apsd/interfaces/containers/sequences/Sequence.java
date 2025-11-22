package apsd.interfaces.containers.sequences;

// import apsd.classes.utilities.Box;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.iterators.ForwardIterator;

/** Interface: IterableContainer con supporto alla lettura e ricerca tramite posizione. */
public interface Sequence<Data> extends IterableContainer<Data> {

  // GetAt
  default Data GetAt(final Natural index) {
    ExcIfOutOfBound(index); // index OutOfBound check
    ForwardIterator<Data> iter = FIterator();
    iter.Next(index);
    return iter.GetCurrent();
  }

  // GetFirst
  default Data GetFirst() {
    if (IsEmpty()) throw new IndexOutOfBoundsException("Sequence is empty");
    return GetAt(Natural.ZERO);
  }

  // GetLast
  default Data GetLast() {
    if (IsEmpty()) throw new IndexOutOfBoundsException("Sequence is empty");
    return GetAt(Size().Decrement());
  }

  /* Search (senza validare caso null)
  default Natural Search(final Data value) {
    long idx = 0;
    ForwardIterator<Data> iter = FIterator();

    while (iter.IsValid()) {
      if (iter.GetCurrent().equals(value)) { return Natural.Of(idx); }
      iter.Next(); idx++;
    }
    return null; // Non trovato!
  }*/

  // Search (con gestione caso null)
  default Natural Search(final Data value) {
    long idx = 0;
    ForwardIterator<Data> iter = FIterator();

    while (iter.IsValid()) {
            Data currentData = iter.DataNNext(); // Prende il dato e avanza l'iteratore
      if (value == null) {
        if (currentData == null) { return Natural.Of(idx); }
      } else {
        if (value.equals(currentData)) { return Natural.Of(idx); }
      }
      idx++;
    }
    return null; // Non trovato!
  }

  // IsInBound
  default boolean IsInBound(final Natural index) {
    return index != null && index.compareTo(Size()) < 0;
  }

  // ExcIfOutOfBound
  default long ExcIfOutOfBound(Natural num) {
    if (num == null) throw new NullPointerException("Natural number cannot be null!");  // Additional exception check (Null Pointer)
    long idx = num.ToLong();
    if (idx >= Size().ToLong()) throw new IndexOutOfBoundsException("Index out of bounds: index must be strictly smaller than size!");
    return idx;
  }

  // SubSequence
  abstract Sequence<Data> SubSequence(final Natural from, final Natural to);

}
