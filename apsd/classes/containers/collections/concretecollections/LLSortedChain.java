package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.LLChainBase;
import apsd.classes.containers.collections.concretecollections.bases.LLNode;
import apsd.classes.utilities.Box;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.SortedChain;
import apsd.interfaces.containers.iterators.ForwardIterator;

/** Object: Concrete sorted chain implementation on linked-list. */
public class LLSortedChain<Data extends Comparable<? super Data>> extends LLChainBase<Data> implements SortedChain<Data> { // Must extend LLChainBase and implement SortedChain

  public LLSortedChain() {}

  public LLSortedChain(LLSortedChain<Data> chn) {
    super(chn);
  }

  public LLSortedChain(TraversableContainer<Data> con) {
    super(con);
  }

  protected LLSortedChain(long size, LLNode<Data> head, LLNode<Data> tail) {
    super(size, head, tail);
  }

  @Override
  protected LLChainBase<Data> NewChain(long size, LLNode<Data> head, LLNode<Data> tail) {
    return new LLSortedChain<>(size, head, tail);
  }

  /* ************************************************************************ */
  /* Specific member functions of LLSortedChain                               */
  /* ************************************************************************ */

  protected LLNode<Data> PredFind(Data val) {
    if (val == null) return null;

    Box<LLNode<Data>> current = headref;
    long len = size.ToLong();
    LLNode<Data> pred = null;

    while (len > 0) {
      long nlen = (len - 1) / 2;
      Box<LLNode<Data>> next = current;
      for (long idx = 0; idx < nlen; idx++, next = next.Get().GetNext()) {}
      if (next.Get().Get().compareTo(val) < 0) {
        pred = next.Get();
        current = next.Get().GetNext();
        len = len - nlen - 1;
      } else { len = nlen; }
    }
    return pred;
  }

  protected LLNode<Data> PredPredFind(Data val){
    if (val == null) return null;

    ForwardIterator<Box<LLNode<Data>>> iter = FRefIterator();
    long len = size.ToLong();
    LLNode<Data> predpred = null;

    while (len > 1) {
      long nlen = len / 2;
      ForwardIterator<Box<LLNode<Data>>> next = new ListFRefIterator((ListFRefIterator) iter);
      next.Next(nlen - 1);
      LLNode<Data> tmpnode = next.GetCurrent().Get();
      next.Next();
      if (next.GetCurrent().Get().Get().compareTo(val) < 0) {
        predpred = tmpnode;
        iter = next;
        len = len - nlen;
      } else { len = nlen; }
    }
    return predpred;
  }

  protected LLNode<Data> PredSuccFind(Data val) {
    if (val == null) return null;

    Box<LLNode<Data>> current = headref;
    long len = size.ToLong();
    LLNode<Data> predsucc = null;

    while (len > 0) {
      long nlen = len / 2;
      Box<LLNode<Data>> next = current;
      for (long idx = 0; idx < nlen; idx++, next = next.Get().GetNext()) {}
      if (next.Get() != null && next.Get().Get().compareTo(val) <= 0) {
        predsucc = next.Get();
        current = next.Get().GetNext();
        len = len - nlen - 1;
      } else { len = nlen; }
    }
    return predsucc;
  }

  /* ************************************************************************ */
  /* Override specific member functions from InsertableContainer              */
  /* ************************************************************************ */

  @Override
  public boolean Insert (Data val) {
    if (val == null) return false;
    LLNode<Data> pred = PredFind(val);
    Box<LLNode<Data>> current = (pred == null) ? headref : pred.GetNext();
    LLNode<Data> node = current.Get();
    LLNode<Data> newnode = new LLNode<>(val, node);
    current.Set(newnode);
    if (tailref.Get() == pred) { tailref.Set(newnode); }
    size.Increment();
    return true;
  }

  /* ************************************************************************ */
  /* Override specific member functions from RemovableContainer               */
  /* ************************************************************************ */

  @Override
  public boolean Remove (Data val) {
    if (val == null) return false;
    LLNode<Data> pred = PredFind(val);
    Box<LLNode<Data>> current = (pred == null) ? headref : pred.GetNext();
    LLNode<Data> node = current.Get();
    if (node != null &&node.Get().equals(val)) {
      current.Set(node.GetNext().Get());
      if (tailref.Get() == node) { tailref.Set(pred); }
      size.Decrement();
      return true;
    } else { return false; }
  }

  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  public Natural Search (Data val) {
    if (val == null) return null;
    Box<LLNode<Data>> current = headref;
    long len = size.ToLong();
    long pos = 0;
    while (len > 0) {
      long steplen = len / 2;
      Box<LLNode<Data>> next = current;
      for (long idx = 0; idx < steplen; idx++) { next = next.Get().GetNext(); }
      Data elem = next.Get().Get();
      if (elem.compareTo(val) == 0) { return Natural.Of(pos + steplen);}
      else if (elem.compareTo(val) < 0) {
        pos = pos + steplen + 1;
        current = next.Get().GetNext();
        len = len - steplen - 1;
      } else {
        len = steplen;
      }
    }
    return null;
  }

  /* ************************************************************************ */
  /* Override specific member functions from SortedSequence                   */
  /* ************************************************************************ */

  // ...

  /* ************************************************************************ */
  /* Override specific member functions from OrderedSet                       */
  /* ************************************************************************ */

  // ...

  /* ************************************************************************ */
  /* Override specific member functions from Chain                            */
  /* ************************************************************************ */

  // ...

}
