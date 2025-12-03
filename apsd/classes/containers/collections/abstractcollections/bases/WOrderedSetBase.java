package apsd.classes.containers.collections.abstractcollections.bases;

import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.OrderedSet;
import apsd.interfaces.containers.collections.SortedChain;

/** Object: Abstract wrapper set base implementation via chain. */
abstract public class WOrderedSetBase<Data extends Comparable<? super Data>, Chn extends SortedChain<Data>> extends WSetBase<Data, Chn> implements OrderedSet<Data> { // Must extend WSetBase and implement OrderedSet; Chn must extend SortedChain

  // WOrderedSetBase
  public WOrderedSetBase() { super(); }

  public WOrderedSetBase(Chn chn) { super(chn); }

  public WOrderedSetBase(TraversableContainer<Data> con) { super(con); }

  public WOrderedSetBase(Chn chn, TraversableContainer<Data> con) { super(chn, con); }

  /* ************************************************************************ */
  /* Override specific member functions from IterableContainer                */
  /* ************************************************************************ */

  /**  In questa Base indico quale "super" usare per ciascun metodo.
   * Altrimenti il richiamo sarebbe "ambiguo" e probabilmente errato!
   * Non ci sono implementazioni ulteriori
  */

  @Override
  public boolean IsEqual(IterableContainer<Data> con) { return chn.IsEqual(con); }

  /* ************************************************************************ */
  /* Override specific member functions from OrderedSet                       */
  /* ************************************************************************ */

  @Override
  public Data Min() { return chn.Min(); }

  @Override
  public Data Max() { return chn.Max(); }
  
  @Override
  public void RemoveMin() { chn.RemoveMin(); }

  @Override
  public void RemoveMax() { chn.RemoveMax(); }

  @Override
  public Data MinNRemove() { return chn.MinNRemove(); }

  @Override
  public Data MaxNRemove() { return chn.MaxNRemove(); }

  @Override
  public Data Predecessor(Data val) { return chn.Predecessor(val); }

  @Override
  public Data Successor(Data val) { return chn.Successor(val); }

  @Override
  public void RemovePredecessor(Data val) { chn.RemovePredecessor(val); }

  @Override
  public void RemoveSuccessor(Data val) { chn.RemoveSuccessor(val); }

  @Override
  public Data PredecessorNRemove(Data val) { return chn.PredecessorNRemove(val); }

  @Override
  public Data SuccessorNRemove(Data val) { return chn.SuccessorNRemove(val); }

}
