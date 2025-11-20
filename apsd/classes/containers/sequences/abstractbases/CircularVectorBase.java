package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;

/** Object: Abstract (static) circular vector base implementation. */
abstract public class CircularVectorBase<Data> extends VectorBase<Data> { // Must extend VectorBase

  protected long start = 0L;

  public CircularVectorBase() {
    super();
  }

  public CircularVectorBase(Natural inicapacity) {
    super(inicapacity);
  }

  public CircularVectorBase(TraversableContainer<Data> con) {
    super(con);
  }

  protected CircularVectorBase(Data[] arr) {
    this.arr = arr;
  }

  @Override
  protected void ArrayAlloc(Natural newsize) {
    super.ArrayAlloc(newsize);
    start = 0L;
  }

  /* ************************************************************************ */
  /* Override specific member functions from ReallocableContainer             */
  /* ************************************************************************ */

  @Override
  @SuppressWarnings("unchecked")
  public void Realloc(Natural newcapacity) {
    long ns = newcapacity.ToLong();
    if (ns >= Integer.MAX_VALUE) { throw new ArithmeticException("Overflow: size cannot exceed Integer.MAX_VALUE!"); }

    Data[] newArr = (Data[]) new Object[(int) ns];
    long limit = Math.min(Size().ToLong(), ns);

    for (long i = 0; i < limit; i++) {
      newArr[(int) i] = GetAt(Natural.Of(i));
    }
    arr = newArr;
    start = 0L; // Dopo la riallocazione, l'array è lineare, quindi start torna a 0.
  }

  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  public Data GetAt(final Natural index) {
    long logIndex = ExcIfOutOfBound(index);
    long capacity = arr.length; // Usiamo una variabile locale per leggibilità ed efficienza
    if (capacity == 0) { throw new IndexOutOfBoundsException("Vector is empty."); }
    long physIndex = ((start + logIndex) % capacity + capacity) % capacity;
    return arr[(int) physIndex];
  }

  /* ************************************************************************ */
  /* Override specific member functions from MutableSequence                  */
  /* ************************************************************************ */

  @Override
  public void SetAt(final Data value, final Natural index) {
    long logIndex = ExcIfOutOfBound(index);
    long capacity = arr.length;
    if (capacity == 0) { throw new IndexOutOfBoundsException("Vector is empty."); }
    long physIndex = ((start + logIndex) % capacity + capacity) % capacity;
    arr[(int) physIndex] = value;
  }

  /* ************************************************************************ */
  /* Specific member functions of Vector                                      */
  /* ************************************************************************ */

  @Override
  public void ShiftLeft(Natural pos, Natural num) {
    if (pos.IsZero()) {
      long capacity = arr.length;
      if (capacity > 0) { start = (start + num.ToLong()) % capacity; }
    } else
      super.ShiftLeft(pos, num);
  }

  @Override
  public void ShiftRight(Natural pos, Natural num) {
    if (pos.IsZero()) {
      long capacity = arr.length;
      if (capacity > 0) { start = ((start - num.ToLong()) % capacity + capacity) % capacity; }
    } else
      super.ShiftRight(pos, num);
  }

}
