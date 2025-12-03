package apsd.classes.containers.collections.abstractcollections.bases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.traits.Predicate;

/** Object: Abstract wrapper set base implementation via chain. */
@SuppressWarnings("OverridableMethodCallInConstructor")
abstract public class WSetBase<Data, Chn extends Chain<Data>> implements Set<Data> { // Must implement Set; Chn must extend Chain

  // protected Chn chn;
  protected Chn chn;

  // WSetBase
  public WSetBase() { ChainAlloc(); }

  public WSetBase(Chn chn) { this.chn = chn; }

  public WSetBase(TraversableContainer<Data> con) {
    ChainAlloc();
    con.TraverseForward(val -> { chn.InsertIfAbsent(val); return false; });
    // Come spiegato a lezione, privilegiamo sempre la TraverseForward
  }

  public WSetBase(Chn chn, TraversableContainer<Data> con) {
    this.chn = chn;
    con.TraverseForward(val -> { chn.InsertIfAbsent(val); return false; });
    // Come spiegato a lezione, privilegiamo sempre la TraverseForward
  }

  // ChainAlloc
  abstract protected void ChainAlloc();

  /* ************************************************************************ */
  /* Override specific member functions from Container                        */
  /* ************************************************************************ */

  @Override
  public Natural Size() { return chn.Size(); }

  /* ************************************************************************ */
  /* Override specific member functions from ClearableContainer               */
  /* ************************************************************************ */

  @Override
  public void Clear() { chn.Clear(); }

  /* ************************************************************************ */
  /* Override specific member functions from InsertableContainer              */
  /* ************************************************************************ */

  @Override
  public boolean Insert(Data val) { return chn.InsertIfAbsent(val); }

  /* ************************************************************************ */
  /* Override specific member functions from RemovableContainer               */
  /* ************************************************************************ */

  @Override
  public boolean Remove(Data val) { return chn.Remove(val); }

  /* ************************************************************************ */
  /* Override specific member functions from IterableContainer                */
  /* ************************************************************************ */

  @Override
  public ForwardIterator<Data> FIterator() { return chn.FIterator(); }

  @Override
  public BackwardIterator<Data> BIterator() { return chn.BIterator(); }

  /* ************************************************************************ */
  /* Override specific member functions from Collection                       */
  /* ************************************************************************ */

  @Override
  public boolean Filter(Predicate<Data> pred) { return chn.Filter(pred); }

  /* ************************************************************************ */
  /* Override specific member functions from Set                              */
  /* ************************************************************************ */

  // NOTA ATTENZIONE: In teoria non serve o sbaglio? Nel Diagrams è un errore?
  // @Override
  // public void Intersection(Set<Data> set) { chn.Intersection(set); }

}
