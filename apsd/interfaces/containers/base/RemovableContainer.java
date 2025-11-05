package apsd.interfaces.containers.base;

/** Interface: Container con supporto alla rimozione di un dato. */
public interface RemovableContainer<Data> extends Container {
  
  boolean Remove(final Data val);

  default boolean RemoveAll(final TraversableContainer<Data> trav) {
    // Come per InsertAll, passiamo il riferimento al metodo Remove.
    // TraverseForward si occuperà di iterare e controllare il risultato.
    return trav.TraverseForward(this::Remove);
    /** Questa sintassi serve a dire "Per ogni elemento di questo 'container', esegui il metodo Remove"
      * Se anche un solo elemento fallisce (non viene rimosso) viene restituito false
      * Se la traverse conclude senza problemi (abbiamo rimosso tutti gli elementi) restituisce true! 
      * È analoga alla lambda vista in lezione, però come preferenza personale ritengo questa più "facile" da leggere se breve.
      */
  }

  default boolean RemoveSome(final TraversableContainer<Data> trav) {
    // Usiamo FoldForward, che è perfetto per aggregare un risultato booleano.
    // L'accumulatore `acc` parte da `false` e diventa `true` se un'operazione di rimozione ha successo.
    return trav.FoldForward((val, acc) -> acc || Remove(val), false);
  }

}
