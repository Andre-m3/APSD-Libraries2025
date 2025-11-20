package apsd.interfaces.containers.collections;

import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.sequences.InsertableAtSequence;
import apsd.interfaces.containers.sequences.MutableSequence;

public interface List<Data> extends Chain<Data>, InsertableAtSequence<Data>, MutableSequence<Data> { // Must extend MutableSequence, InsertableAtSequence, and Chain
  
  default List<Data> SubList(final Natural from, final Natural to) {
    return (List<Data>) SubSequence(from, to);
  }

  /* ************************************************************************ */
  /* Override specific member functions from ExtensibleContainer              */
  /* ************************************************************************ */

  @Override
  default boolean Insert(final Data val) {
    // Come discusso a lezione, preferiamo l'inserimento in Testa! (tempo costante)
    // L'inserimento in coda potrebbe causare tempi più lunghi per casistiche speciali
    InsertFirst(val);
    return true;
  }

}
