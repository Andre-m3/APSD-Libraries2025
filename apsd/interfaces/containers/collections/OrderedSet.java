package apsd.interfaces.containers.collections;

public interface OrderedSet<Data extends Comparable<? super Data>> extends Set<Data> {

  // Min
  default Data Min() {
    if (IsEmpty()) { return null; }
    // FoldForward parte dal primo elemento e lo confronta con tutti gli altri.
    // L'accumulatore mantiene il minimo corrente.
    return FoldForward((dat, min) -> (min == null || dat.compareTo(min) < 0) ? dat : min, FIterator().GetCurrent());
  }

  // RemoveMin
  default void RemoveMin() { if (!IsEmpty()) { Remove(Min()); } }

  // MinNRemove
  default Data MinNRemove() {
    Data min = Min();
    if (min != null) { Remove(min); }
    return min;
  }

  // Max
  default Data Max() {
    if (IsEmpty()) { return null; }
    return FoldForward((dat, max) -> (max == null || dat.compareTo(max) > 0) ? dat : max, FIterator().GetCurrent());
  }

  // RemoveMax
  default void RemoveMax() { if (!IsEmpty()) { Remove(Max()); } }

  // MaxNRemove
  default Data MaxNRemove() {
    Data max = Max();
    if (max != null) { Remove(max); }
    return max;
  }

  // Predecessor
  default Data Predecessor(final Data val) {
    // FoldForward per trovare il più grande tra i minoranti di 'val'.
    // L'accumulatore 'pred' tiene traccia del miglior candidato trovato.
    return FoldForward((dat, pred) -> {
      if (dat.compareTo(val) < 0) { // 'dat' è un potenziale predecessore
        if (pred == null || dat.compareTo(pred) > 0) { return dat; }  // 'dat' è un predecessore migliore
      }
      return pred; // Altrimenti conservo il predecessore corrente
    }, null); // Se non ci sono Predecessor, restituisco null (valore base) -> Non è stato trovato
  }

  // RemovePredecessor
  default void RemovePredecessor(final Data val) {
    Data pred = Predecessor(val);
    if (pred != null) { Remove(pred); }
  }

  // PredecessorNRemove
  default Data PredecessorNRemove(final Data val) {
    Data pred = Predecessor(val);
    if (pred != null) { Remove(pred); }
    return pred;
  }

  // Successor
  default Data Successor(final Data val) {
    // Discorso analogo a Predecessor
    return FoldForward((dat, succ) -> {
      if (dat.compareTo(val) > 0) { // 'dat' è un potenziale successore
        if (succ == null || dat.compareTo(succ) < 0) { return dat; } // 'dat' è un successore migliore 
      }
      return succ;
    }, null); // Se non ci sono Successor, restituisco null (valore base) -> Non è stato trovato
  }

  // RemoveSuccessor
  default void RemoveSuccessor(final Data val) {
    Data succ = Successor(val);
    if (succ != null) { Remove(succ); }
  }

  // SuccessorNRemove
  default Data SuccessorNRemove(final Data val) {
    Data succ = Successor(val);
    if (succ != null) { Remove(succ); }
    return succ;
  }

}
