package apsd.interfaces.containers.collections;

import apsd.interfaces.containers.base.IterableContainer;

public interface Set<Data> extends Collection<Data> {

  // Union
  default void Union(final Set<Data> set) {
    set.TraverseForward(dat -> { Insert(dat); return false; });
  }

  // Difference
  default void Difference(final Set<Data> other) {
    other.TraverseForward(dat -> { Remove(dat); return false; });
  }

  // Intersection
  default void Intersection(final Set<Data> set) { Filter(dat -> set.Exists(dat)); }

  /* ************************************************************************ */
  /* Override specific member functions from IterableContainer                */
  /* ************************************************************************ */

  @Override
  default boolean IsEqual(final IterableContainer<Data> other) {
    if (other == null || !(other instanceof Set) || !this.Size().equals(other.Size())) {
      return false;
    }
    if (this == other) return true;
    // Ritorna 'true' se NON viene trovato nessun elemento in 'this' che non esista in 'other'.
    return !this.TraverseForward(dat -> !other.Exists(dat));
  }

}
