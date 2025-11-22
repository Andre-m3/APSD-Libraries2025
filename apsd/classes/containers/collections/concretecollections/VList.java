package apsd.classes.containers.collections.concretecollections;


import apsd.classes.containers.collections.concretecollections.bases.VChainBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.List;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.DynVector;
import apsd.interfaces.containers.sequences.MutableSequence;

/** Object: Concrete list implementation on (dynamic circular) vector. */
public class VList<Data> extends VChainBase<Data> implements List<Data> { // Must extend VChainBase and implement List

  public VList() {
    super();
  }

  public VList(TraversableContainer<Data> con) {
    super(con);
  }

  protected VList(DynVector<Data> vec) {
    super(vec);
  }

  @Override
  protected VChainBase<Data> NewChain(DynVector<Data> vec) {
    return new VList<>(vec);
  }

  /* ************************************************************************ */
  /* Override specific member functions from MutableIterableContainer         */
  /* ************************************************************************ */

  @Override
  public MutableForwardIterator<Data> FIterator() {
    return vec.FIterator();
  }

  @Override
  public MutableBackwardIterator<Data> BIterator() {
    return vec.BIterator();
  }

  /* ************************************************************************ */
  /* Override specific member functions from MutableSequence                  */
  /* ************************************************************************ */

  @Override
  public MutableSequence<Data> SubSequence(Natural from, Natural to) {
    return (MutableSequence<Data>) super.SubSequence(from, to);
  }

  /* ************************************************************************ */
  /* Override specific member functions from InsertableAtSequence             */
  /* ************************************************************************ */

  @Override
  public void SetAt(Data val, Natural pos) {
    vec.SetAt(val, pos);
  }

  @Override
  public void InsertAt(Data val, Natural pos) {
    vec.InsertAt(val, pos);
  }

}
