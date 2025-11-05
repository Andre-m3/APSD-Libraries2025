package apsd.interfaces.containers.base;

// import apsd.classes.utilities.Natural;
// import apsd.classes.utilities.MutableNatural;
import apsd.classes.utilities.Box;
import apsd.interfaces.traits.Accumulator;
import apsd.interfaces.traits.Predicate;

/** Interface: MembershipContainer con supporto all'attraversamento. */
public interface TraversableContainer<Data> extends MembershipContainer<Data> {

  boolean TraverseForward(final Predicate<Data> pred);
  boolean TraverseBackward(final Predicate<Data> pred);

  default <Acc> Acc FoldForward(Accumulator<Data, Acc> fun, Acc ini) {
    final Box<Acc> acc = new Box<>(ini);
    if (fun != null)
      TraverseForward(dat -> { acc.Set(fun.Apply(dat, acc.Get())); return false; });
      // Con false dovremmo aver attraversato correttamente tutto il contenitore...
    return acc.Get();
  }

  default <Acc> Acc FoldBackward(Accumulator<Data, Acc> fun, Acc ini) {
    final Box<Acc> acc = new Box<>(ini);
    if (fun != null)
      TraverseBackward(dat -> { acc.Set(fun.Apply(dat, acc.Get())); return false; });
    return acc.Get();
  }

  /* ************************************************************************ */
  /* Override specific member functions from Container                        */
  /* ************************************************************************ */

  // ...

  /* ************************************************************************ */
  /* Override specific member functions from MembershipContainer              */
  /* ************************************************************************ */

  @Override
  default boolean Exists(final Data val) {
    // Usiamo TraverseForward per cercare l'elemento. Se lo troviamo, il predicato
    // restituisce false, TraverseForward si ferma e restituisce false.
    // Quindi, se TraverseForward restituisce false, l'elemento esiste.
    return !TraverseForward(dat -> !dat.equals(val));
  }

}
