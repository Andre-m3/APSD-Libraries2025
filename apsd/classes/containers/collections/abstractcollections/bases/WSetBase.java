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

  @Override
  public void Intersection(Set<Data> set) { chn.Intersection(set); }

  /** TEST CUSTOM -> FACCIO UN CONTROLLO SULLA "difference"
   * Se viene chiesta una Difference su un determinato set,
   * ma this (quindi questo set) è proprio il set passato al metodo, alora devo fare una clear!
   * Se da un set, rimuovo esattamente tutti gli elementi (Set1 = Set2, quindi Set2-Set1 è vuoto)
   * Allora richiamo Clear per gestire bene anche il resto! Ho svuotato il Set...
   */
  @Override
  public void Difference(Set<Data> set) {
    if (set == this) chn.Clear();
    else chn.Difference(set);
  }

  // Union e Intersection non hanno questo problema, vista la logica differente.
  // Non serve fare un override di Union

}
