package apsd.classes.containers.collections.concretecollections.bases;

import apsd.classes.containers.sequences.DynCircularVector;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.DynVector;
import apsd.interfaces.containers.sequences.Sequence;
import apsd.interfaces.traits.Predicate;

/** Object: Abstract list base implementation on (dynamic circular) vector. */
abstract public class VChainBase<Data> implements Chain<Data> { // Must implement Chain

  protected final DynVector<Data> vec;

  public VChainBase() {
    vec = new DynCircularVector<>();
  }

  protected VChainBase(DynVector<Data> vec) {
    this.vec = vec;
  }

  public VChainBase(TraversableContainer<Data> con) {
    vec = new DynCircularVector<>(con);
  }

  abstract protected VChainBase<Data> NewChain(DynVector<Data> vec);

  /* ************************************************************************ */
  /* Override specific member functions from Container                        */
  /* ************************************************************************ */

  @Override
  public Natural Size() { return vec.Size(); }

  /* ************************************************************************ */
  /* Override specific member functions from ClearableContainer               */
  /* ************************************************************************ */

  @Override
  public void Clear() { vec.Clear(); }

  /* ************************************************************************ */
  /* Override specific member functions from RemovableContainer               */
  /* ************************************************************************ */

  @Override
  public boolean Remove(Data dat) { 
    Natural idx = vec.Search(dat);
    if (idx != null) {
      vec.RemoveAt(idx);
      return true;
    }
    return false;
  }

  /* ************************************************************************ */
  /* Override specific member functions from IterableContainer                */
  /* ************************************************************************ */

  @Override
  public ForwardIterator<Data> FIterator() {
    MutableForwardIterator<Data> iter = vec.FIterator();
    return iter;
  }

  @Override
  public BackwardIterator<Data> BIterator() {
    MutableBackwardIterator<Data> iter = vec.BIterator();
    return iter;
  }

  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  public Sequence<Data> SubSequence(Natural from, Natural to) {
    // vec.SubVector restituisce un DynVector.
    // Noi dobbiamo restituire una Sequence che sia coerente con questa classe (quindi una Chain).
    // Usiamo il metodo astratto NewChain per "wrappare" il subvector in una nuova Chain.
    return NewChain(vec.SubVector(from, to));
  }

  @Override
  public Data GetAt(Natural index) {
    return vec.GetAt(index);
  }

  /* ************************************************************************ */
  /* Override specific member functions from RemovableAtSequence              */
  /* ************************************************************************ */

  @Override
  public Data AtNRemove(Natural index) {
    return vec.AtNRemove(index);
  }

  /* ************************************************************************ */
  /* Override specific member functions from Collection                       */
  /* ************************************************************************ */

  @Override
  public boolean Filter(Predicate<Data> pred) {
    if (pred == null) return false;

    // Implementazione ottimizzata O(N) per vettori (approccio rdr/wrt visto a lezione).
    // Evito il richiamo di Remove(obj) che causerebbe n shift di elementi. (O(N^2))
    long size = vec.Size().ToLong();
    long wrt = 0; // Indice di scrittura

    for (long rdr = 0; rdr < size; rdr++) {
      Natural r = Natural.Of(rdr);
      Data val = vec.GetAt(r);
      
      if (pred.Apply(val)) { // Se il predicato è vero, teniamo l'elemento
        if (rdr != wrt) {
          // Se siamo avanti col puntatore di lettura, spostiamo l'elemento indietro
          vec.SetAt(val, Natural.Of(wrt));
        }
        wrt++;
      }
    }

    // Se abbiamo scartato degli elementi (write < size), riduciamo la dimensione del vettore
    if (wrt < size) {
      vec.Reduce(Natural.Of(size - wrt));
      return true;
    }

    return false;
  }
}
