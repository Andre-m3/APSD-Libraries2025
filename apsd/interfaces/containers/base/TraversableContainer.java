package apsd.interfaces.containers.base;

import apsd.classes.utilities.Box;
import apsd.classes.utilities.MutableNatural;
import apsd.classes.utilities.Natural;
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

  @Override
  default Natural Size() {
    /* Su un TraversableContainer si può calcolare la size!
     * TraverseForward è "costantemente" efficiente.
     * Non ci sono casi che possono causare peggioramenti, come per Backward...
     */
    final MutableNatural size = new MutableNatural(0L);
    TraverseForward(dat -> { size.Increment(); return false; });
    return size.ToNatural();
  }

  /* ************************************************************************ */
  /* Override specific member functions from MembershipContainer              */
  /* ************************************************************************ */

  @Override
  default boolean Exists(final Data val) {
    // La TraverseForward scorre tutti gli elementi.
    // Se trova l'elemento val, il predicato restituisce 'true'
    // Di conseguenza la traverse si interrompe. 
    return TraverseForward(dat -> { if (dat == null) return (val == null); else return dat.equals(val); });
  }

}
