package apsd.interfaces.containers.sequences;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.SortedIterableContainer;


/** Interface: Sequence & SortedIterableContainer. */
public interface SortedSequence<Data extends Comparable<? super Data>> extends Sequence<Data>, SortedIterableContainer<Data> { // Must extend Sequence and SortedIterableContainer

  /* ************************************************************************ */
  /* Override specific member functions from MembershipContainer              */
  /* ************************************************************************ */

  @Override
  default boolean Exists(final Data value) {
    // Un elemento esiste se la sua ricerca non restituisce null.
    return Search(value) != null;
  }

  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  default Natural Search(final Data val) {
    if (val == null) { return null; }

    long left = 0;
    long right = Size().ToLong() - 1;

    while (left <= right) {
      long mid = left + (right - left) / 2;
      Data midVal = GetAt(Natural.Of(mid));
      int cmp = midVal.compareTo(val);

      if (cmp < 0) {
        left = mid + 1; // Cerca nella metà destra
      } else if (cmp > 0) {
        right = mid - 1; // Cerca nella metà sinistra
      } else {
        return Natural.Of(mid); // Trovato!
      }
    }
    return null; // Non trovato
  }

  // @Override
  // Se la Sequence è Sorted a priori, allora sappiamo gestire tutto meglio!
  //  GetAt.... In posizione "..." dammi Data. (corretto oppure no?)
  //  Search... (metodo blu)
  //    -> Trovare un dato specifico in una sequenza ordinata è più facile!

}
