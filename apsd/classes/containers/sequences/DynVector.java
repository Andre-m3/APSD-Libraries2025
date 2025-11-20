package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.DynLinearVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;

/** Object: Concrete dynamic (linear) vector implementation. */
public class DynVector<Data> extends DynLinearVectorBase<Data> { // Must extend DynLinearVectorBase

  public DynVector() {
    super();
  }

  public DynVector(Natural inicapacity) {
    super(inicapacity);
  }

  public DynVector(TraversableContainer<Data> con) {
    super(con);
  }

    protected DynVector(Data[] arr) {
    super(arr);
  }

  @Override
  protected DynVector<Data> NewVector(Data[] arr) {
    return new DynVector<>(arr);
  }

}
