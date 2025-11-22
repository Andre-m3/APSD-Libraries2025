package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.iterators.MutableBackwardIterator;
import apsd.interfaces.containers.iterators.MutableForwardIterator;
import apsd.interfaces.containers.sequences.MutableSequence;
import apsd.interfaces.containers.sequences.Vector;

/** Object: Abstract vector base implementation. */
abstract public class VectorBase<Data> implements Vector<Data> {

  protected Data[] arr = null;

  @SuppressWarnings("OverridableMethodCallInConstructor")
  public VectorBase () {
    ArrayAlloc(Natural.ZERO);
  }

  @SuppressWarnings("OverridableMethodCallInConstructor")
  public VectorBase (Natural initialsize) {
    if (initialsize == null) { throw new NullPointerException("A Natural 'size' cannot be null..."); }
    ArrayAlloc(initialsize);
  }

  @SuppressWarnings("OverridableMethodCallInConstructor")
  public VectorBase (TraversableContainer<Data> con) {
    if (con == null) { throw new NullPointerException("A TraversableContainer 'con' cannot be null..."); }
    ArrayAlloc(con.Size());
  }

  // NewVector
  abstract protected Vector<Data> NewVector(Data[] arr);

  @SuppressWarnings("unchecked")
  protected void ArrayAlloc(Natural newsize) {
    long size = newsize.ToLong();
    if (size >= Integer.MAX_VALUE) { throw new ArithmeticException("Overflow: size cannot exceed Integer.MAX_VALUE!"); }
    arr = (Data[]) new Object[(int) size];
  }

  /* ************************************************************************ */
  /* Override specific member functions from ClearableContainer               */
  /* ************************************************************************ */

  /*@Override
  public void Clear() { ArrayAlloc(Capacity()); }*/

  @Override
  public void Clear() { Realloc(Natural.ZERO); }

  /* ************************************************************************ */
  /* Override specific member functions from ResizableContainer               */
  /* ************************************************************************ */

  @Override
  public Natural Capacity() { return Natural.Of(arr.length); }

  /* ************************************************************************ */
  /* Override specific member functions from IterableContainer                */
  /* ************************************************************************ */

  protected class VectorFIterator implements MutableForwardIterator<Data> {
    
    protected long curr = 0L;

    @Override
    public boolean IsValid() {
      return (curr < Size().ToLong());
    }

    @Override
    public void Next() {
      if (!IsValid()) throw new IllegalStateException("Iterator has been terminated");
      curr++;
    }

    @Override
    public void Reset() { curr = 0L; }

    @Override
    public Data GetCurrent() {
      if (!IsValid()) throw new IllegalStateException("Iterator has been terminated");
      return GetAt(Natural.Of(curr));
    }

    @Override
    public void SetCurrent(Data val) {
      if (!IsValid()) throw new IllegalStateException("Iterator has been terminated");
      SetAt(val, Natural.Of(curr));
    }

    @Override
    public Data DataNNext() {
      if (!IsValid()) { throw new IllegalStateException("Iterator has been terminated"); }
      return GetAt(Natural.Of(curr++));
    }
  }

  protected class VectorBIterator implements MutableBackwardIterator<Data> {

    protected long curr;
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public VectorBIterator() { Reset(); }

    @Override
    public boolean IsValid() { return (curr >= 0L && curr < Size().ToLong()); }

    @Override
    public void Prev() {
      if (!IsValid()) throw new IllegalStateException("Iterator has been terminated.");
      curr--;
    }

    @Override
    public void Reset() { curr = Size().ToLong() - 1L; }

    @Override
    public Data GetCurrent() {
      if (!IsValid()) throw new IllegalStateException("Iterator has been terminated.");
      return GetAt(Natural.Of(curr)); }

    @Override
    public void SetCurrent(Data val) {
      if (!IsValid()) throw new IllegalStateException("Iterator has been terminated.");
      SetAt(val, Natural.Of(curr));
    }

    @Override
    public Data DataNPrev() {
    if (!IsValid()) throw new IllegalStateException("Iterator has been terminated.");
    return GetAt(Natural.Of(curr--));
    }
  }

  @Override
  public MutableForwardIterator<Data> FIterator() { return new VectorFIterator(); }

  @Override
  public MutableBackwardIterator<Data> BIterator() { return new VectorBIterator(); }


  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  abstract public Data GetAt(final Natural index);

  @SuppressWarnings("unchecked")
  @Override
  public MutableSequence<Data> SubSequence(Natural from, Natural to) {
  
    long fromIndex = from.ToLong();   // Creo "fromIndex" e "toIndex"
    long toIndex = to.ToLong();       // Così da non richiamare svariate volte il metodo ToLong()
    if (fromIndex > toIndex || toIndex >= Size().ToLong()) {
      throw new IndexOutOfBoundsException("Invalid subsequence range: from=" + fromIndex + ", to=" + toIndex + ", size=" + Size().ToLong());
    }

    long subSize = toIndex - fromIndex + 1;   // SubSize ('toIndex' e 'fromIndex' inclusi)
    Data[] subArray = (Data[]) new Object[(int) subSize];
    for (int i = 0; i < subSize; i++) {
      subArray[i] = this.GetAt(Natural.Of(fromIndex + i));
    }

    return NewVector(subArray);
  }


  /* ************************************************************************ */
  /* Override specific member functions from MutableSequence                  */
  /* ************************************************************************ */

  @Override
  abstract public void SetAt(final Data val, final Natural index);

}
