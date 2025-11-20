package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.sequences.DynVector;

/** Object: Abstract dynamic circular vector base implementation. */
abstract public class DynCircularVectorBase<Data> extends CircularVectorBase<Data> implements DynVector<Data> {

  protected long size = 0L;

  public DynCircularVectorBase() {}

  public DynCircularVectorBase(Natural inicapacity) {
    super(inicapacity);
  }

  public DynCircularVectorBase(TraversableContainer<Data> con) {
    super(con);
    this.size = con.Size().ToLong();
  }

  protected DynCircularVectorBase(Data[] arr) {
    super(arr);
    this.size = arr.length;
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
    size = Math.min(size, Capacity().ToLong());
  }

  /* ************************************************************************ */
  /* Override specific member functions from ResizableContainer               */
  /* ************************************************************************ */

  @Override
  public void Expand(Natural num) {
    DynVector.super.Grow(num);
    size += num.ToLong();
  }

  @Override
  public void Reduce(Natural num) {
    long val = num.ToLong();
    if (val > size) throw new ArithmeticException("Underflow: size cannot be negative!");
    size -= val;
    DynVector.super.Shrink();
  }

  /* ************************************************************************ */
  /* Specific member functions of Vector                                      */
  /* ************************************************************************ */

  @Override
  public void ShiftLeft(Natural pos, Natural num) {
    super.ShiftLeft(pos, num);
    if (pos.IsZero()) { Reduce(num); }
  }

  @Override
  public void ShiftRight(Natural pos, Natural num) {
    super.ShiftRight(pos, num);
    if (pos.IsZero()) { Expand(num); }
  }

}
