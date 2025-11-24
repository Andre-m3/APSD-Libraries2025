package apsd.classes.containers.collections.concretecollections.bases;

import apsd.classes.containers.sequences.Vector;
import apsd.classes.utilities.Box;
import apsd.classes.utilities.MutableNatural;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.Sequence;
import apsd.interfaces.traits.Predicate;

/** Object: Abstract chain base implementation on linked-list. */
abstract public class LLChainBase<Data> implements Chain<Data> { // Must implement Chain

  protected final MutableNatural size = new MutableNatural();
  protected final Box<LLNode<Data>> headref = new Box<>();
  protected final Box<LLNode<Data>> tailref = new Box<>();

  public LLChainBase() {}

  public LLChainBase(TraversableContainer<Data> con) {
    size.Assign(con.Size());
    final Box<Boolean> first = new Box<>(true);
    con.TraverseForward(dat -> {
      LLNode<Data> node = new LLNode<>(dat);
      if (first.Get()) {
        headref.Set(node);
        first.Set(false);
      } else {
        tailref.Get().SetNext(node);
      }
      tailref.Set(node);
      return false;
    });
  }

  protected LLChainBase(long size, LLNode<Data> head, LLNode<Data> tail) {
    this.size.Assign(size);
    headref.Set(head);
    tailref.Set(tail);
  }

  abstract protected LLChainBase<Data> NewChain(long size, LLNode<Data> head, LLNode<Data> tail);

  /* ************************************************************************ */
  /* Specific member functions from LLChainBase                               */
  /* ************************************************************************ */

  protected class ListFRefIterator implements ForwardIterator<Box<LLNode<Data>>> {
    
    protected Box<LLNode<Data>> curr = headref;

    public ListFRefIterator() {}

    public ListFRefIterator(ListFRefIterator iter) {
      curr = iter.curr;
    }

    @Override
    public boolean IsValid() { return !(curr.IsNull()); }

    @Override
    public void Reset() {
      curr = headref;
    }

    @Override
    public Box<LLNode<Data>> GetCurrent() {
      if (!IsValid()) throw new IllegalStateException("Iterator is not valid!");
      return curr;
    }

    @Override
    public void Next() {
      if (!IsValid()) throw new IllegalStateException("Iterator is not valid!");
      curr = curr.Get().GetNext();
    }

    @Override
    public Box<LLNode<Data>> DataNNext() {
      if(!IsValid()) throw new IllegalStateException("Iterator is not valid!");
      Box<LLNode<Data>> prevcurr = curr;
      curr = curr.Get().GetNext();
      return prevcurr;
    }
  }

  protected ForwardIterator<Box<LLNode<Data>>> FRefIterator() {
    return new ListFRefIterator();
  }

  protected class ListBRefIterator implements BackwardIterator<Box<LLNode<Data>>> {

    protected long curr = -1L;
    protected Vector<Box<LLNode<Data>>> arr = null;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ListBRefIterator() { Reset(); }

    public ListBRefIterator(ListBRefIterator iter) {
      curr = iter.curr;
      this.arr = (iter.arr != null) ? new Vector<>(iter.arr) : null; // arr = new Vector<>(iter.arr); Controllo necessario?
    }

    @Override
    public boolean IsValid() { return (curr >= 0); }

    @Override
    public void Reset() {
      curr = -1L;
      if (Size().IsZero()) arr = null;
      else {
        arr = new Vector<>(Size());
        for (Box<LLNode<Data>> ref = headref; !ref.IsNull(); ref = ref.Get().GetNext()) {
          arr.SetAt(ref, Natural.Of(++curr));
        }
      }
    }

    @Override
    public Box<LLNode<Data>> GetCurrent() {
      if (!IsValid()) throw new IllegalStateException("Iterator is not valid!");
      return arr.GetAt(Natural.Of(curr));
    }

    @Override
    public void Prev() {
      if (!IsValid()) throw new IllegalStateException("Iterator is not valid!");
      curr--;
    }

    @Override
    public Box<LLNode<Data>> DataNPrev() {
      if (!IsValid()) throw new IllegalStateException("Iterator is not valid!");
      // Restituisce l'elemento corrente e poi decrementa l'indice (post-decremento)
      // Nota -> Per andare "all'indietro" viene ricostruito un vettore da scorrere normalmente!
      //      -> Usiamo le Box (<LLNode<...>>) proprio per simulare l'utilizzo dei puntatori (siamo in java...!)
      return arr.GetAt(Natural.Of(curr--));
    }

  }

  protected BackwardIterator<Box<LLNode<Data>>> BRefIterator() {
    return new ListBRefIterator();
  }

  /* ************************************************************************ */
  /* Override specific member functions from Container                        */
  /* ************************************************************************ */

  @Override
  public Natural Size() { return size.ToNatural(); }

  /* ************************************************************************ */
  /* Override specific member functions from ClearableContainer               */
  /* ************************************************************************ */

  @Override
  public void Clear() {
    size.Zero();
    headref.Set(null);
    tailref.Set(null);
  }

  /* ************************************************************************ */
  /* Override specific member functions from RemovableContainer               */
  /* ************************************************************************ */

  @Override
  public boolean Remove(Data dat) {
    if (dat == null) return false;
    final Box<LLNode<Data>> prd = new Box<>();
    return FRefIterator().ForEachForward(cur -> {
      LLNode<Data> node = cur.Get();
      if (node.Get().equals(dat)) {
        cur.Set(node.GetNext().Get());
        if (tailref.Get() == node) { tailref.Set(prd.Get()); }
        size.Decrement();
        return true;
      }
      prd.Set(node);
      return false;
    });
  }

  /* ************************************************************************ */
  /* Override specific member functions from IterableContainer                */
  /* ************************************************************************ */

  protected class ListFIterator implements MutableForwardIterator<Data> {
    
    protected final ForwardIterator<Box<LLNode<Data>>> iter;

    public ListFIterator() {
      iter = FRefIterator();
    }

    public ListFIterator(ListFIterator iter) {
      this.iter = iter.iter; // ...= new ListFRefIterator((ListFRefIterator) iter.iter); Causa dell'errore?
    } 

    @Override
    public boolean IsValid() { return iter.IsValid(); }

    @Override
    public void Reset() { iter.Reset(); }

    @Override
    public Data GetCurrent() {
      return iter.GetCurrent().Get().Get();
    }

    @Override
    public void SetCurrent(Data dat) {
      if (dat == null) return;
      iter.GetCurrent().Get().Set(dat);
    }

    @Override
    public void Next() { iter.Next(); }

    @Override
    public Data DataNNext() {
      return iter.DataNNext().Get().Get();
    }
  
  }

  @Override
  public ForwardIterator<Data> FIterator() {
    return new ListFIterator();
  }

  protected class ListBIterator implements MutableBackwardIterator<Data> {
    
    protected final BackwardIterator<Box<LLNode<Data>>> iter;

    public ListBIterator() {
      iter = BRefIterator();
    }

    public ListBIterator(ListBIterator iter) {
      this.iter = iter.iter; // ...= new ListBRefIterator((ListBRefIterator) iter.iter); Causa dell'errore?
    }

    @Override
    public boolean IsValid() { return iter.IsValid(); }

    @Override
    public void Reset() { iter.Reset(); }

    @Override
    public Data GetCurrent() {
      return iter.GetCurrent().Get().Get();
    }

    @Override
    public void SetCurrent(Data dat) {
      // if (dat == null) return; Controllo superfluo, non viene "sporcato" il valore non facendolo!
      iter.GetCurrent().Get().Set(dat);
    }

    @Override
    public void Prev() { iter.Prev(); }

    @Override
    public Data DataNPrev() {
      return iter.DataNPrev().Get().Get();
    }

  }

  @Override
  public BackwardIterator<Data> BIterator() {
    return new ListBIterator();
  }

  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  public Data GetFirst() {
    if (headref.IsNull()) throw new IndexOutOfBoundsException("First element doesn't exist...");
    return headref.Get().Get();
  }

  @Override
  public Data GetLast() {
    if (tailref.IsNull()) throw new IndexOutOfBoundsException("Last element doesn't exist...");
    return tailref.Get().Get();
  }

  @Override
  public Sequence<Data> SubSequence(Natural from, Natural to) {

    long start = from.ToLong();
    long end = to.ToLong();

    if (start > end || end >= size.ToLong())
      return null;
    
    final Box<LLNode<Data>> lHead = new Box<>();
    final Box<LLNode<Data>> lTail = new Box<>();
    final Box<Long> idx = new Box<>(0L);

    TraverseForward(dat -> {
      if (idx.Get() > end) return true;
      LLNode<Data> node = new LLNode<>(dat);
      if (idx.Get() == start) lHead.Set(node);
      if (idx.Get() > start) lTail.Get().SetNext(node);
      lTail.Set(node);
      
      idx.Set(idx.Get() + 1);
      return false;
    });

    return NewChain(end - start + 1, lHead.Get(), lTail.Get());
  }


  /* ************************************************************************ */
  /* Override specific member functions from RemovableAtSequence              */
  /* ************************************************************************ */

  @Override
  public Data AtNRemove(Natural index) {
    long idx = ExcIfOutOfBound(index);
    if (idx == 0) return FirstNRemove();
    else if (idx == size.ToLong() - 1) return LastNRemove();
    else {
      ForwardIterator<Box<LLNode<Data>>> iter = FRefIterator();
      iter.Next(idx);
      Box<LLNode<Data>> current = iter.GetCurrent();
      LLNode<Data> node = current.Get();
      current.Set(node.GetNext().Get());
      size.Decrement();
      return node.Get();      
    }
  }

  @Override
  public void RemoveFirst() {
    // Caso speciale -> lista vuota
    if (headref.IsNull()) throw new IndexOutOfBoundsException("Empty"); // Dai test viene chiesto di lanciare eccezione...
    
    LLNode<Data> node = headref.Get();
    if (tailref.Get() == node) {
      headref.Set(null);
      tailref.Set(null);
    } else {
      headref.Set(headref.Get().GetNext().Get());
    }
    size.Decrement();
  }

  @Override
  public void RemoveLast() {
    // Caso speciale -> lista vuota
    if (headref.IsNull()) throw new IndexOutOfBoundsException("Empty"); // Dai test viene chiesto di lanciare eccezione...

    // Gestiamo il caso in cui size = 1, quindi headref punta al primo elemento
    if (size.ToLong() == 1) { // Caso "speciale" -> size = 1
        RemoveFirst();
        return;
    }

    // Caso comune -> size > 1
    BackwardIterator<Box<LLNode<Data>>> iter = BRefIterator();
    iter.GetCurrent().Set(null);
    iter.Prev();
    tailref.Set(iter.GetCurrent().Get());
    size.Decrement();
  }

  @Override
  public Data FirstNRemove() {
    LLNode<Data> node = headref.Get();
    if (node == null) return null;
    Data data = node.Get();
    RemoveFirst();
    return data;
  }


  /* ************************************************************************ */
  /* Override specific member functions from Collection                       */
  /* ************************************************************************ */

  @Override
  public boolean Filter (Predicate<Data> func) {
    MutableNatural oldsize = size;
    if (func != null) {
      ForwardIterator<Box<LLNode<Data>>> iter = FRefIterator();
      LLNode<Data> pred = null;
      while (iter.IsValid()) {
        Box<LLNode<Data>> curr = iter.GetCurrent();
        LLNode<Data> node = curr.Get();
        if (func.Apply(node.Get())) {
          iter.Next();
        } else {
          curr.Set(node.GetNext().Get());
          if (tailref.Get() == node) tailref.Set(pred);
          size.Decrement();
        }
        pred = curr.Get();
        }
      }
    return !size.equals(oldsize);
  }

}
