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

  default boolean IsEqual(final IterableContainer<Data> other) {
    if (other == null) return false;
    if (this == other) return true;
    if (!this.Size().equals(other.Size())) return false;

    ForwardIterator<Data> thisIter = FIterator();
    ForwardIterator<Data> otherIter = other.FIterator();

    while (thisIter.IsValid() && otherIter.IsValid()) {
      Data val1 = thisIter.GetCurrent();
      Data val2 = otherIter.GetCurrent();
    
      // Gestione dei null per non avere problemi con equals()
      if (val1 == null) {
        if (val2 != null) return false;
      } else if (!val1.equals(val2)) {
        return false;
      }

      // Incremento gli iteratori
      thisIter.Next();
      otherIter.Next();
    }

    return !(thisIter.IsValid() || otherIter.IsValid()); // Entrambi devono essere terminati, altrimenti non sono uguali
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
