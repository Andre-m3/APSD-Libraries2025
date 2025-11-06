package apsd.interfaces.containers.sequences;

// import apsd.classes.utilities.Natural;

import apsd.interfaces.containers.base.SortedIterableContainer;


/** Interface: Sequence & SortedIterableContainer. */
public interface SortedSequence<Data extends Comparable<? super Data>> extends Sequence<Data>, SortedIterableContainer<Data> { // Must extend Sequence and SortedIterableContainer

  /* ************************************************************************ */
  /* Override specific member functions from MembershipContainer              */
  /* ************************************************************************ */

  // @Override
  // Exists... (metodo blu)

  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  // @Override
  // Se la Sequence è Sorted a priori, allora sappiamo gestire tutto meglio!
  //  GetAt.... In posizione "..." dammi Data. (corretto oppure no?)
  //  Search... (metodo blu)
  //    -> Trovare un dato specifico in una sequenza ordinata è più facile!

}
