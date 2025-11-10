package apsd.interfaces.containers.sequences;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.ResizableContainer;

public interface DynVector<Data> extends Vector<Data>, InsertableAtSequence<Data>, RemovableAtSequence<Data>, ResizableContainer{

  /* ************************************************************************ */
  /* Override specific member functions from InsertableAtSequence             */
  /* ************************************************************************ */
  
  @Override
  default void InsertAt(final Data val, final Natural pos) {
    if (pos == Size()) { Expand(Natural.ONE); }
    else { ShiftRight(pos); } // Overloaded method of super (Vector)
    SetAt(val, pos);
  }
  

  /* ************************************************************************ */
  /* Override specific member functions from RemovableAtSequence              */
  /* ************************************************************************ */

  @Override
  default Data AtNRemove(final Natural pos) {
    Data val = GetAt(pos);
    ShiftLeft(pos); // Overloaded method of super (Vector)
    return val;
  }

  /* ************************************************************************ */
  /* Specific member functions of Vector                                       */
  /* ************************************************************************ */

  @Override
  default void ShiftRight(Natural pos, Natural num) {
    Expand(num);
    Vector.super.ShiftRight(pos, num);
  }

  @Override
  default void ShiftLeft(Natural pos, Natural num) {
    Vector.super.ShiftLeft(pos, num);
    Reduce(num);
  }

  @Override
  default DynVector<Data> SubVector(Natural from, Natural to) {
    return (DynVector<Data>) Vector.super.SubVector(from, to);
  }

  /* ************************************************************************ */
  /* Override specific member functions from Container                        */
  /* ************************************************************************ */

  @Override
  abstract Natural Size();

}
