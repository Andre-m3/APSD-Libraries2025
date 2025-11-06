package apsd.interfaces.containers.base;

import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.traits.Predicate;

/** Interface: TraversableContainer con supporto all'iterazione. */
public interface IterableContainer<Data> extends TraversableContainer<Data> {

  // FIterator
  // BIterator
  ForwardIterator<Data> FIterator();
  BackwardIterator<Data> BIterator();

  // IsEqual
  default boolean IsEqual(final IterableContainer<Data> other) {
    if (other == null) { return false; }
    if (!this.Size().equals(other.Size())) { return false; } // Hanno dimensioni diverse, sono diversi!
    if (this == other) { return true; } // Se "il contenitore" è lo stesso, allora sono uguali.
    if (this.IsEmpty()) { return true; } // Se entrambi sono vuoti (secondo if), allora sono uguali.

    ForwardIterator<Data> otherIter = other.FIterator();
    // Usiamo TraverseForward su 'this' e l'iteratore di 'other' per il confronto.
    // Il predicato si ferma e restituisce 'false' alla prima discrepanza. 'true' se tutto termina senza interrompersi
    return this.TraverseForward(dat -> dat.equals(otherIter.DataNNext()));
  }

  /* ************************************************************************ */
  /* Override specific member functions from TraversableContainer             */
  /* ************************************************************************ */

  @Override
  default boolean TraverseForward(final Predicate<Data> pred) {
    return FIterator().ForEachForward(pred);
  }

  @Override
  default boolean TraverseBackward(final Predicate<Data> pred) {
    return BIterator().ForEachBackward(pred);
  }

}
