package apsd.interfaces.containers.sequences;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.ReallocableContainer;

public interface Vector<Data> extends ReallocableContainer, MutableSequence<Data> {

  // ShiftLeft
  default void ShiftLeft(Natural pos, Natural num) {
    long idx = ExcIfOutOfBound(pos);
    long size = Size().ToLong();
    long len = num.ToLong();
    len = (len <= size - idx) ? len : size - idx;
      if (len > 0) {
      long iniwrt = idx;
      long wrt = iniwrt;
      for (long rdr = wrt + len; rdr < size; rdr++, wrt++) {
        Natural natrdr = Natural.Of(rdr);
        SetAt(GetAt(natrdr), Natural.Of(wrt));
        SetAt(null, natrdr);
      }
      for (; wrt - iniwrt < len; wrt++) {
        SetAt(null, Natural.Of(wrt));
      }
    }
  }

  // ShiftFirstLeft
  default void ShiftFirstLeft() { ShiftLeft(Natural.ZERO, Natural.ONE); }

  // ShiftLastLeft
  default void ShiftLastLeft() {
    if (!IsEmpty()) { ShiftLeft(Size().Decrement(), Natural.ONE); }
  }

  // ShiftRight
  default void ShiftRight(Natural pos, Natural num) {
    long idx = ExcIfOutOfBound(pos);
    long size = Size().ToLong();
    long len = num.ToLong();
    len = (len <= size - idx) ? len : size - idx;
    if (len > 0) {
      long iniwrt = idx + len;
      long wrt = size - 1;
      for (long rdr = wrt - len; rdr >= idx; rdr--, wrt--) {
        Natural natrdr = Natural.Of(rdr);
        SetAt(GetAt(natrdr), Natural.Of(wrt));
        SetAt(null, natrdr);
      }
      for (; wrt >= iniwrt; wrt--) {
        SetAt(null, Natural.Of(wrt));
      }
    }
  }

  // ShiftFirstRight
  default void ShiftFirstRight() { ShiftRight(Natural.ZERO, Natural.ONE); }
  
  // ShiftLastRight
  default void ShiftLastRight() {
    if (!IsEmpty()) { ShiftRight(Size().Decrement(), Natural.ONE); }
  }
  
  // SubVector -> Sfruttiamo la SubSequence (stesso concetto)
  default Vector<Data> SubVector(Natural from, Natural to) {
    return (Vector<Data>) SubSequence(from, to);
  }

  /* ************************************************************************ */
  /* Override specific member functions from Container                        */
  /* ************************************************************************ */

  @Override
  default Natural Size() { return ReallocableContainer.super.Size(); }

}