package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.CircularVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;

/** Object: Concrete (static) circular vector implementation. */
public class CircularVector<Data> extends CircularVectorBase<Data> {

  public CircularVector() {
    super();
  }

  public CircularVector(Natural inisize) {
    super(inisize);
  }

  public CircularVector(TraversableContainer<Data> con) {
    super(con); // La classe base alloca solo lo spazio.
    if (con != this) {
      // La classe concreta ha la responsabilità di copiare i dati.
      final int[] i = {0};
      con.TraverseForward(dat -> { arr[i[0]++] = dat; return false; });
    }
  }

  protected CircularVector(Data[] arr) {
    this.arr = arr;
  }

  @Override
  protected CircularVector<Data> NewVector(Data[] arr) {
    return new CircularVector<>(arr);
  }

}
