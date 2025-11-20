package apsd.classes.containers.sequences;

import apsd.classes.containers.sequences.abstractbases.LinearVectorBase;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;

/** Object: Concrete (static linear) vector implementation. */
public class Vector<Data> extends LinearVectorBase<Data> { // Must extend LinearVectorBase

  public Vector() {
    super();
  }

  public Vector(Natural inisize) {
    super(inisize);
  }

  public Vector(TraversableContainer<Data> con) {
    super(con);
  }

  protected Vector(Data[] arr) {
    super(arr);
  }

  @Override
  protected Vector<Data> NewVector(Data[] arr) {
    return new Vector<>(arr);
  }

}
