package apsd.classes.containers.deqs;

import apsd.classes.containers.collections.concretecollections.VList;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.List;
import apsd.interfaces.containers.deqs.Queue;

/** Object: Wrapper queue implementation. */
public class WQueue<Data> implements Queue<Data> { // Must implement Queue

  protected final List<Data> lst;

  public WQueue() { lst = new VList<>(); }

  public WQueue(List<Data> lst) { this.lst = lst; }

  public WQueue(TraversableContainer<Data> con) { this.lst = new VList<>(con); }

  public WQueue(List<Data> lst, TraversableContainer<Data> con) {
    this.lst = lst;
    con.TraverseForward(dat -> { this.lst.InsertLast(dat); return false; });
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
  /* Override specific member functions from Queue                            */
  /* ************************************************************************ */

  @Override
  public void Enqueue (Data dat) { lst.InsertLast(dat); }

  @Override
  public void Dequeue () {
    if (lst.Size().IsNotZero()) { lst.RemoveFirst(); }
  }

  @Override
  public Data Head() {
    if (lst.Size().IsZero()) { return null; } else { return lst.GetFirst(); }
  }

  @Override
  public Data HeadNDequeue() {
    if (lst.Size().IsZero()) { return null; } else { return lst.FirstNRemove(); }
  }

}