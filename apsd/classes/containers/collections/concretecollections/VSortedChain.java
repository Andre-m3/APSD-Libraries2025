package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.VChainBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.SortedChain;
import apsd.interfaces.containers.sequences.DynVector;

/** Object: Concrete set implementation via (dynamic circular) vector. */
public class VSortedChain<Data extends Comparable<? super Data>> extends VChainBase<Data> implements SortedChain<Data> { // Must extend VChainBase and implements SortedChain

  public VSortedChain() {
    super();
  }

  public VSortedChain(VSortedChain<Data> chn) {
    super(chn);
  }

  public VSortedChain(TraversableContainer<Data> con) {
    con.TraverseForward(dat -> { Insert(dat); return false;});
  }

  protected VSortedChain(DynVector<Data> vec) {
    super(vec);
  }

  @Override
  protected VChainBase<Data> NewChain(DynVector<Data> vec) {
    return new VSortedChain<>(vec);
  }

  /* ************************************************************************ */
  /* Override specific member functions from InsertableContainer              */
  /* ************************************************************************ */

  @Override
  public boolean Insert(Data dat) {
    if (dat == null) { return false; }
    Natural prd = SearchPredecessor(dat);
    Natural pos = (prd == null) ? Natural.ZERO : prd.Increment();
    /*vec.ShiftRight(pos); /// ERRORE TEST
    vec.SetAt(dat, pos);*/
    vec.InsertAt(dat, pos);
    return true;
  }

  /* ************************************************************************ */
  /* Override specific member functions from Chain                            */
  /* ************************************************************************ */

  @Override
  public boolean InsertIfAbsent(Data val) {
    // Sfruttiamo la ricerca binaria di SortedChain per efficienza
    if (Search(val) != null) {
      return false;
    }
    return Insert(val);
  }

@Override
  public void RemoveOccurrences(Data val) {
    // Implementazione Reader/Writer (due indici)
    long size = vec.Size().ToLong();
    long rdr = 0;
    long wrt = 0;

    while (rdr < size) {
      Data curr = vec.GetAt(Natural.Of(rdr));
      int cmp = curr.compareTo(val);
      
      if (cmp == 0) {
          // Elemento da rimuovere
      } else {
          // Elemento da tenere
          if (rdr != wrt)
            vec.SetAt(curr, Natural.Of(wrt));
          wrt++;
        }
      rdr++;
    }

    // Se qualcosa è stato rimosso, va modificata (ridotta) la size!
    if (wrt < size)
      vec.Reduce(Natural.Of(size - wrt));
  }

}
