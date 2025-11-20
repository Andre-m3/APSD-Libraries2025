package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.sequences.DynVector;

/** Object: Abstract dynamic linear vector base implementation. */
abstract public class DynLinearVectorBase<Data> extends LinearVectorBase<Data> implements DynVector<Data> { // Must extend LinearVectorBase and implement DynVector

  protected long size = 0L;

  public DynLinearVectorBase() {}

  public DynLinearVectorBase(Natural inisize) {
    super(inisize); // Alloca l'array con la capacità iniziale
  }

  public DynLinearVectorBase(TraversableContainer<Data> con) {
    super(con);
    this.size = con.Size().ToLong();
  }

  protected DynLinearVectorBase(Data[] arr) {
    super(arr);
    this.size = arr.length;
  }

  @Override
  protected void ArrayAlloc(Natural newcapacity) {
    if (newcapacity == null) { throw new NullPointerException("Natural 'newcapacity' cannot be null..."); }
    final long MIN_CAPACITY = 1L;

    super.ArrayAlloc(newcapacity.ToLong() < MIN_CAPACITY ? Natural.Of(MIN_CAPACITY) : newcapacity);
    // this.size = newcapacity.ToLong();
  }

  /* ************************************************************************ */
  /* Override specific member functions from Container                        */
  /* ************************************************************************ */

  @Override
  public Natural Size() {
    return Natural.Of(size);
  }

  /* ************************************************************************ */
  /* Override specific member functions from ClearableContainer               */
  /* ************************************************************************ */

  @Override
  public void Clear() {
    super.Clear();
    size = 0L;
  }

  /* ************************************************************************ */
  /* Override specific member functions from ReallocableContainer             */
  /* ************************************************************************ */

  @Override
  public void Realloc(Natural newcapacity) {
    super.Realloc(newcapacity);
    size = Math.min(size, arr.length);
  }

  /* ************************************************************************ */
  /* Override specific member functions from ResizableContainer               */
  /* ************************************************************************ */

  @Override
  public void Expand(Natural num) {
    // if (num == null) throw new NullPointerException("Natural 'num' cannot be null...");    /// *** non necessario? *** ///
    DynVector.super.Grow(num);
    size += num.ToLong();
  }

  @Override
  public void Reduce(Natural num) {
    if (num == null) throw new NullPointerException("Natural 'num' cannot be null...");
    long val = num.ToLong();
    if (val > size) throw new ArithmeticException("Underflow: size cannot be negative!");
    size -= val;
    DynVector.super.Shrink();
  }

}
