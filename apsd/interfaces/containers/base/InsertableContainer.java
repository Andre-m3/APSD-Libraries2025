package apsd.interfaces.containers.base;

/** Interface: Container con supporto all'inserimento di un dato. */
public interface InsertableContainer<Data> extends Container {

  boolean Insert(final Data val);

  default boolean InsertAll(final TraversableContainer<Data> trav) {
    // Passiamo il metodo Insert come predicato.
    // TraverseForward si fermerà e restituirà false al primo inserimento fallito.
    // Se tutti gli inserimenti hanno successo, TraverseForward restituirà true.
    return trav.TraverseForward(this::Insert);
  }

  default boolean InsertSome(final TraversableContainer<Data> trav) {
    // FoldForward è ideale per aggregare un risultato.
    // Partendo da 'false', l'accumulatore diventa 'true' non appena un'operazione
    // di inserimento ha successo, e rimane tale.
    return trav.FoldForward((val, acc) -> acc || Insert(val), false);
  }

}
