package apsd.classes.containers.sequences.abstractbases;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;

/** Object: Abstract (static) linear vector base implementation. */
abstract public class LinearVectorBase<Data> extends VectorBase<Data> { // Must extend VectorBase

  public LinearVectorBase() {
    super();
  }

  public LinearVectorBase(Natural initialsize) {
    super(initialsize);
  }

  public LinearVectorBase(TraversableContainer<Data> con) {
    super(con); // Alloca l'array della dimensione giusta
    if (con != this) {
      // Per il contatore viene usato un array di un elemento per aggirare la restrizione "effectively final" delle lambda.
      final int[] i = {0};
      con.TraverseForward(dat -> { arr[i[0]++] = dat; return false; });
    }
  }

  protected LinearVectorBase(Data[] arr) {
    this.arr = arr;
  }

  /* ************************************************************************ */
  /* Override specific member functions from ReallocableContainer             */
  /* ************************************************************************ */

  @Override
  @SuppressWarnings({"unchecked", "ManualArrayCopy"})
  public void Realloc(Natural newsize) {
    long ns = newsize.ToLong();
    if (ns >= Integer.MAX_VALUE) { throw new ArithmeticException("Overflow: size cannot exceed Integer.MAX_VALUE!"); }

    Data[] newArr = (Data[]) new Object[(int) ns];
    int limit = (int) Math.min(arr.length, ns);

    // Usiamo System.arraycopy per una copia più efficiente e per rimuovere il warning dell'IDE.
    if (limit > 0) { System.arraycopy(arr, 0, newArr, 0, limit); }
    arr = newArr;
  }

  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  public Data GetAt(final Natural index) {
    return arr[(int) ExcIfOutOfBound(index)];
  }

  /* ************************************************************************ */
  /* Override specific member functions from MutableSequence                  */
  /* ************************************************************************ */

  @Override
  public void SetAt(final Data value, final Natural index) {
    arr[(int) ExcIfOutOfBound(index)] = value;
  }

}
