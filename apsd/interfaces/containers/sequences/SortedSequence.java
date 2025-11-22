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
    long left = 0;
    long right = Size().ToLong() - 1;

    while (left <= right) {
      long mid = left + (right - left) / 2;
      Data midVal = GetAt(Natural.Of(mid));
      int cmp;

      if (midVal == null) {
        // Se l'elemento in mezzo è null, e viene cercato null, abbiamo trovato una corrispondenza potenziale.
        // Ma siccome il compareTo non fornisce risultati corretti quando confrontato un null (es.: null.compareTo(10))
        // Allora delego alla ricerca lineare (più lenta! Però avverrà raramente) se troviamo un null.
        /** DA RICONTROLLARE: Noi siamo sicuri di non avere mai Sorted Sequences con null? E quindi di non inciampare mai in questo caso? */
        return Sequence.super.Search(val);
      }
      cmp = midVal.compareTo(val);

      if (cmp < 0) left = mid + 1;        // Cerca nella metà destra
      else if (cmp > 0) right = mid - 1;  // Cerca nella metà sinistra
      else return Natural.Of(mid);        // Trovato
    }

    return null; // Elemento non presente
  }
}
