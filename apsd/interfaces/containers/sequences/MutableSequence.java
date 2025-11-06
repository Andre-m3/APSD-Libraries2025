package apsd.interfaces.containers.sequences;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.MutableIterableContainer;
// import apsd.interfaces.containers.iterators.MutableForwardIterator;

/** Interface: Sequence & MutableIterableContainer con supporto alla scrittura tramite posizione. */
public interface MutableSequence<Data> extends Sequence<Data>, MutableIterableContainer<Data> {

  // SetAt
  void SetAt(final Data value, final Natural index);
  
  // GetNSetAt
  default Data GetNSetAt(final Data value, final Natural index) {
    Data old = GetAt(index);
    SetAt(value, index);
    return old;
  }

  // SetFirst
  default void SetFirst(final Data value) { SetAt(value, Natural.ZERO); }

  // GetNSetFirst
  default Data GetNSetFirst(final Data value) {
    return GetNSetAt(value, Natural.ZERO);
  }

  // SetLast
  default void SetLast(final Data value) { SetAt(value, Size().Decrement()); }

  // GetNSetLast
  default Data GetNSetLast(final Data value) {
    return GetNSetAt(value, Size().Decrement());
  }

  // Swap
  default void Swap(final Natural index1, final Natural index2) {
    Data tmp = GetAt(index1);
    SetAt(GetAt(index2), index1);
    SetAt(tmp, index2);
  }
  
  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  MutableSequence<Data> SubSequence(final Natural from, final Natural to);

}
