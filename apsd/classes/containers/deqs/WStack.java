package apsd.classes.containers.deqs;

import apsd.classes.containers.collections.concretecollections.VList;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.List;
import apsd.interfaces.containers.deqs.Stack;

/** Object: Wrapper stack implementation. */
public class WStack<Data> implements Stack<Data> { // Must implement Stack

  protected final List<Data> lst;

  public WStack() { lst = new VList<>(); }

  public WStack(List<Data> lst) { this.lst = lst; }

  public WStack(TraversableContainer<Data> con) { this.lst = new VList<>(con); }

  public WStack(List<Data> lst, TraversableContainer<Data> con) {
    this.lst = lst;
    con.TraverseForward(dat -> {  this.lst.InsertFirst(dat); return false; });
  }

  /* ************************************************************************ */
  /* Override specific member functions from Container                        */
  /* ************************************************************************ */

  @Override
  public Natural Size() { return lst.Size(); }

  /* ************************************************************************ */
  /* Override specific member functions from ClearableContainer               */
  /* ************************************************************************ */

  @Override
  public void Clear() { lst.Clear(); }

  /* ************************************************************************ */
  /* Override specific member functions from Stack                            */
  /* ************************************************************************ */

  @Override
  public Data Top() {
    if (lst.Size().IsZero()) { return null; } else { return lst.GetFirst(); }
  }

  @Override
  public void Push(Data dat) { lst.InsertFirst(dat); }

  @Override
  public void Pop() {
    if (lst.Size().IsNotZero()) { lst.RemoveFirst(); }
  }

  @Override
  public Data TopNPop() {
    if (lst.Size().IsZero()) { return null; } else { return lst.FirstNRemove(); }
  }

  @Override
  public void SwapTop(Data dat) {
    if (lst.Size().IsZero()) { Push(dat); } else { lst.SetFirst(dat); }
  }

  @Override
  public Data TopNSwap(Data dat) {
    if (lst.Size().IsZero()) { Push(dat); return null; } else { return lst.FirstNRemove(); }
  }

}