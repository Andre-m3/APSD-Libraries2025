package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.LLChainBase;
import apsd.classes.containers.collections.concretecollections.bases.LLNode;
import apsd.classes.utilities.Box;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.List;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.MutableSequence;

/** Object: Concrete list implementation on linked-list. */
public class LLList<Data> extends LLChainBase<Data> implements List<Data> { // Must extend LLChainBase and implement List

  public LLList() {}

  public LLList(TraversableContainer<Data> con) {
    super(con);
  }

  protected LLList(long size, LLNode<Data> head, LLNode<Data> tail) {
    super(size, head, tail);
  }

  @Override
  protected LLChainBase<Data> NewChain(long size, LLNode<Data> head, LLNode<Data> tail) {
    return new LLList<>(size, head, tail);
  }

  /* ************************************************************************ */
  /* Override specific member functions from MutableIterableContainer         */
  /* ************************************************************************ */

  @Override
  public MutableForwardIterator<Data> FIterator() { return new ListFIterator(); }

  @Override
  public MutableBackwardIterator<Data> BIterator() { return new ListBIterator(); }

  /* ************************************************************************ */
  /* Override specific member functions from MutableSequence                  */
  /* ************************************************************************ */

  @Override
  public void SetAt(Data val, Natural pos) {
    // if (val == null) return; ATTENZIONE: I test in teoria non prevedono silent failure (?)
    List.super.SetAt(val, pos);
  }

  @Override
  public void SetFirst(Data val) {
    // if (val == null) return; ATTENZIONE: I test in teoria non prevedono silent failure (?)
    if (headref.IsNull()) throw new IllegalStateException("First element doesn't exist...");
    headref.Get().Set(val);
  }

  @Override
  public void SetLast(Data val) {
    // if (val == null) return; ATTENZIONE: I test in teoria non prevedono silent failure (?)
    if (tailref.IsNull()) throw new IllegalStateException("Last element doesn't exist...");
    tailref.Get().Set(val);
  }

  @Override
  public MutableSequence<Data> SubSequence(Natural from, Natural to) {
    return (MutableSequence<Data>) super.SubSequence(from, to);
  }

  /* ************************************************************************ */
  /* Override specific member functions from InsertableAtSequence             */
  /* ************************************************************************ */

  @Override
  public void InsertAt(Data val, Natural pos) {
    // if (val == null) return; ATTENZIONE: I test in teoria non prevedono silent failure (?)
    if (pos == null) throw new NullPointerException("Position cannot be null!");
    long idx = pos.ToLong();
    if (idx > Size().ToLong()) throw new IndexOutOfBoundsException("Position out of bound!");
    if (idx == Size().ToLong()) { InsertLast(val); }
    else {
      ForwardIterator<Box<LLNode<Data>>> iter = FRefIterator();
      iter.Next(idx);
      Box<LLNode<Data>> current = iter.GetCurrent();
      current.Set(new LLNode<>(val, current.Get()));
      size.Increment();
    }
  }

  @Override
  public void InsertFirst(Data val) {
    // if (val == null) return; ATTENZIONE: I test in teoria non prevedono silent failure (?)
    LLNode<Data> nnode = new LLNode<>(val, headref.Get());
    headref.Set(nnode);
    if (tailref.IsNull()) { tailref.Set(nnode); }
    size.Increment();
  }

  @Override
  public void InsertLast(Data val) {
    // if (val == null) return; ATTENZIONE: I test in teoria non prevedono silent failure (?)
    
    LLNode<Data> nnode = new LLNode<>(val, null);
    if (tailref.IsNull()) { headref.Set(nnode); }
    else { tailref.Get().SetNext(nnode); }

    tailref.Set(nnode);
    size.Increment();
  }

}
