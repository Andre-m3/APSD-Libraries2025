package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.DynCircularVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;

/** Object: Concrete dynamic circular vector implementation. */
public class DynCircularVector<Data> extends DynCircularVectorBase<Data> {

  public DynCircularVector() {
    super();
  }

  public DynCircularVector(Natural inisize) {
    super(inisize);
  }

  public DynCircularVector(TraversableContainer<Data> con) {
    super(con);
    if (con != this) {
      final int[] i = {0};
      con.TraverseForward(dat -> { arr[i[0]++] = dat; return false; });
    }
  }

  protected DynCircularVector(Data[] arr) {
    super(arr);
  }

  @Override
  protected DynCircularVector<Data> NewVector(Data[] arr) {
    return new DynCircularVector<>(arr);
  }

}
