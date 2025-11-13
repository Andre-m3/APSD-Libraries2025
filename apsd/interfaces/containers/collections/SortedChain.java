package apsd.interfaces.containers.collections;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.sequences.SortedSequence;

public interface SortedChain<Data extends Comparable<? super Data>> extends OrderedChain<Data>, SortedSequence<Data> { // Must extend OrderedChain and SortedSequence

  // SearchPredecessor
  default Natural SearchPredecessor(final Data val) {
    long left = 0;
    long right = Size().ToLong() - 1;
    long pred = -1;

    while (left <= right) {
      long mid = left + (right - left) / 2;
      int cmp = GetAt(Natural.Of(mid)).compareTo(val);

      if (cmp < 0) { // midVal è un potenziale predecessore
        pred = mid;
        left = mid + 1; // Cerco un predecessore ancora più grande a destra
      } else { // midVal è >= val, quindi il predecessore deve essere a sinistra
        right = mid - 1; // Cerco nella metà sinistra
      }
    }
    return (pred > -1) ? Natural.Of(pred) : null;
  }

  // SearchSuccessor
  default Natural SearchSuccessor(final Data val) {
    long left = 0;
    long right = Size().ToLong() - 1;
    long succ = -1;

    while (left <= right) {
      long mid = left + (right - left) / 2;
      int cmp = GetAt(Natural.Of(mid)).compareTo(val);

      if (cmp > 0) { // midVal è un potenziale successore
        succ = mid;
        right = mid - 1; // Cerco un successore ancora più piccolo a sinistra
      } else { // midVal è <= val, quindi il successore deve essere a destra
        left = mid + 1; // Cerco nella metà destra
      }
    }
    return (succ > -1) ? Natural.Of(succ) : null;
  }
  
  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  default Natural Search(final Data val) {
    return SortedSequence.super.Search(val);
    // c'era ambiguità tra la ricerca lineare di Chain e quella binaria -> specifico quale "super.Search(...)" utilizzare
  }

  /* ************************************************************************ */
  /* Override specific member functions from Set                              */
  /* ************************************************************************ */

  // Intersection -> fornita dal template
  default void Intersection(SortedChain<Data> chain) {
    Natural i = Natural.ZERO, j = Natural.ZERO;
    while (i.compareTo(Size()) < 0 && j.compareTo(chain.Size()) < 0) {
      int cmp = GetAt(i).compareTo(chain.GetAt(j));
      if (cmp < 0) { RemoveAt(i); }
      else {
        j = j.Increment();
        if (cmp == 0) { i = i.Increment(); }
      }
    }
    while (i.compareTo(Size()) < 0) {
      RemoveAt(i);
    }
  }

  /* ************************************************************************ */
  /* Override specific member functions from OrderedSet                       */
  /* ************************************************************************ */

  // Min
  @Override
  default Data Min() { return GetFirst(); } // Sorted -> il minimo è il primo elemento... O(1).

  // Max
  @Override
  default Data Max() { return GetLast(); } // Sorted -> il massimo è l'ultimo elemento... O(1).

  // Predecessor
  @Override
  default Data Predecessor(final Data val) {
    Natural predIndex = SearchPredecessor(val);
    return (predIndex != null) ? GetAt(predIndex) : null;
  }

  // Successor
  @Override
  default Data Successor(final Data val) {
    Natural succIndex = SearchSuccessor(val);
    return (succIndex != null) ? GetAt(succIndex) : null;
  }

  // RemoveMin
  @Override
  default void RemoveMin() {
    if (!IsEmpty()) { RemoveAt(Natural.ZERO); }
  }

  // RemoveMax
  @Override
  default void RemoveMax() {
    if (!IsEmpty()) { RemoveAt(Size().Decrement()); }
  }

  // MinNRemove
  @Override
  default Data MinNRemove() {
    if (IsEmpty()) { return null; }
    Data min = GetAt(Natural.ZERO);
    RemoveAt(Natural.ZERO);
    return min;
  }

  //MaxNRemove
  @Override
  default Data MaxNRemove() {
    if (IsEmpty()) { return null; }
    Data max = GetAt(Size().Decrement());
    RemoveAt(Size().Decrement());
    return max;
  }

  // RemovePredecessor
  @Override
  default void RemovePredecessor(final Data val) {
    Natural predIndex = SearchPredecessor(val);
    if (predIndex != null) { RemoveAt(predIndex); }
  }

  // RemoveSuccessor
  @Override
  default void RemoveSuccessor(final Data val) {
    Natural succIndex = SearchSuccessor(val);
    if (succIndex != null) { RemoveAt(succIndex); }
  }

  // PredecessorNRemove
  @Override
  default Data PredecessorNRemove(final Data val) {
    Natural predIndex = SearchPredecessor(val);
    if (predIndex == null) { return null; }
    Data pred = GetAt(predIndex);
    RemoveAt(predIndex);
    return pred;
  }

  // SuccessorNRemove
  @Override
  default Data SuccessorNRemove(final Data val) {
    Natural succIndex = SearchSuccessor(val);
    if (succIndex == null) { return null; }
    Data succ = GetAt(succIndex);
    RemoveAt(succIndex);
    return succ;
  }

}
