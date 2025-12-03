package apsd.classes.containers.collections.concretecollections;

import apsd.classes.containers.collections.concretecollections.bases.LLChainBase;
import apsd.classes.containers.collections.concretecollections.bases.LLNode;
import apsd.classes.utilities.Box;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.SortedChain;
import apsd.interfaces.containers.iterators.ForwardIterator;

/** Object: Concrete sorted chain implementation on linked-list. */
public class LLSortedChain<Data extends Comparable<? super Data>> extends LLChainBase<Data> implements SortedChain<Data> { // Must extend LLChainBase and implement SortedChain

  public LLSortedChain() {}

  public LLSortedChain(LLSortedChain<Data> chn) {
    super(chn);
  }

  public LLSortedChain(TraversableContainer<Data> con) {
    super(con);
  }

  protected LLSortedChain(long size, LLNode<Data> head, LLNode<Data> tail) {
    super(size, head, tail);
  }

  @Override
  protected LLChainBase<Data> NewChain(long size, LLNode<Data> head, LLNode<Data> tail) {
    return new LLSortedChain<>(size, head, tail);
  }

  /* ************************************************************************ */
  /* Specific member functions of LLSortedChain                               */
  /* ************************************************************************ */

  protected LLNode<Data> PredFind(Data val) {
    if (val == null) return null;

    Box<LLNode<Data>> current = headref;
    long len = size.ToLong();
    LLNode<Data> pred = null;

    while (len > 0) {
      long nlen = (len - 1) / 2;
      Box<LLNode<Data>> next = current;
      for (long idx = 0; idx < nlen; idx++, next = next.Get().GetNext()) {}
      if (next.Get().Get().compareTo(val) < 0) {
        pred = next.Get();
        current = next.Get().GetNext();
        len = len - nlen - 1;
      } else { len = nlen; }
    }
    return pred;
  }

  protected LLNode<Data> PredPredFind(Data val){
    if (val == null) return null;

    ForwardIterator<Box<LLNode<Data>>> iter = FRefIterator();
    long len = size.ToLong();
    LLNode<Data> predpred = null;

    while (len > 1) {
      long nlen = len / 2;
      ForwardIterator<Box<LLNode<Data>>> next = new ListFRefIterator((ListFRefIterator) iter);
      next.Next(nlen - 1);
      LLNode<Data> tmpnode = next.GetCurrent().Get();
      next.Next();
      if (next.GetCurrent().Get().Get().compareTo(val) < 0) {
        predpred = tmpnode;
        iter = next;
        len = len - nlen;
      } else { len = nlen; }
    }
    return predpred;
  }

  protected LLNode<Data> PredSuccFind(Data val) {
    if (val == null) return null;

    Box<LLNode<Data>> current = headref;
    long len = size.ToLong();
    LLNode<Data> predsucc = null;

    while (len > 0) {
      long nlen = len / 2;
      Box<LLNode<Data>> next = current;
      for (long idx = 0; idx < nlen; idx++, next = next.Get().GetNext()) {}
      if (next.Get() != null && next.Get().Get().compareTo(val) <= 0) {
        predsucc = next.Get();
        current = next.Get().GetNext();
        len = len - nlen - 1;
      } else { len = nlen; }
    }
    return predsucc;
  }

  /* ************************************************************************ */
  /* Override specific member functions from InsertableContainer              */
  /* ************************************************************************ */

  @Override
  public boolean Insert (Data val) {
    if (val == null) return false;
    LLNode<Data> pred = PredFind(val);
    Box<LLNode<Data>> current = (pred == null) ? headref : pred.GetNext();
    LLNode<Data> node = current.Get();
    LLNode<Data> newnode = new LLNode<>(val, node);
    current.Set(newnode);
    if (tailref.Get() == pred) { tailref.Set(newnode); }
    size.Increment();
    return true;
  }

  /* ************************************************************************ */
  /* Override specific member functions from RemovableContainer               */
  /* ************************************************************************ */

  @Override
  public boolean Remove (Data val) {
    if (val == null) return false;
    LLNode<Data> pred = PredFind(val);
    Box<LLNode<Data>> current = (pred == null) ? headref : pred.GetNext();
    LLNode<Data> node = current.Get();
    if (node != null &&node.Get().equals(val)) {
      current.Set(node.GetNext().Get());
      if (tailref.Get() == node) { tailref.Set(pred); }
      size.Decrement();
      return true;
    } else { return false; }
  }

  /* ************************************************************************ */
  /* Override specific member functions from Sequence                         */
  /* ************************************************************************ */

  @Override
  public Natural Search (Data val) {
    if (val == null) return null;
    Box<LLNode<Data>> current = headref;
    long len = size.ToLong();
    long pos = 0;
    while (len > 0) {
      long steplen = len / 2;
      Box<LLNode<Data>> next = current;
      for (long idx = 0; idx < steplen; idx++) { next = next.Get().GetNext(); }
      Data elem = next.Get().Get();
      if (elem.compareTo(val) == 0) { return Natural.Of(pos + steplen);}
      else if (elem.compareTo(val) < 0) {
        pos = pos + steplen + 1;
        current = next.Get().GetNext();
        len = len - steplen - 1;
      } else {
        len = steplen;
      }
    }
    return null;
  }

  /* ************************************************************************ */
  /* Override specific member functions from SortedSequence                   */
  /* ************************************************************************ */

  @Override
  public Natural SearchPredecessor(Data val) {
    if (headref.IsNull()) return null;
    if (headref.Get().Get().compareTo(val) >= 0) return null; // Head >= val, nessun predecessore

    long idx = 0;
    ForwardIterator<Data> iter = FIterator();
    long predIdx = -1;

    while (iter.IsValid()) {
      if (iter.GetCurrent().compareTo(val) < 0) { predIdx = idx; } // Trovato un candidato valido
      else {
        // Appena viene trovato un elemento >= val, ci fermiamo.
        // Essendo ordinata, non verranno trovati altri predecessori migliori.
        break; 
      }
      iter.Next();
      idx++;
    }

    return (predIdx != -1) ? Natural.Of(predIdx) : null;
  }

  @Override
  public Natural SearchSuccessor(Data val) {
    if (headref.IsNull()) return null;

    long idx = 0;
    ForwardIterator<Data> iter = FIterator();

    while (iter.IsValid()) {
      if (iter.GetCurrent().compareTo(val) > 0) {
        return Natural.Of(idx); // Trovato il primo maggiorante
      }
      iter.Next();
      idx++;
    }

    return null; // Nessun maggiorante trovato
  }

  /* ************************************************************************ */
  /* Override specific member functions from OrderedSet                       */
  /* ************************************************************************ */

  // Diagrams.pdf: "Override of The six methods for Predecessor and Successor"

  @Override
  public Data Predecessor(Data val) {
      LLNode<Data> pred = PredFind(val);
      return (pred != null) ? pred.Get() : null;
  }

  @Override
  public Data Successor(Data val) {
      LLNode<Data> ps = PredSuccFind(val);
      if (ps == null) {
          return (!headref.IsNull()) ? headref.Get().Get() : null;
      } else {
          return (!ps.GetNext().IsNull()) ? ps.GetNext().Get().Get() : null;
      }
  }

  @Override
  public Data PredecessorNRemove(Data val) {
      LLNode<Data> pp = PredPredFind(val);
      
      // Caso pp = null.
      // Se PredFind(val) non è null, allora PredFind(val) deve essere la testa.
      LLNode<Data> pred = PredFind(val);
      if (pp == null) {
          if (pred != null && pred == headref.Get()) {
              // Rimuovi testa
              Data res = pred.Get();
              headref.Set(pred.GetNext().Get());
              if (headref.IsNull()) tailref.Set(null);
              size.Decrement();
              return res;
          }
          return null; // Non c'è predecessore
      }
      
      // Caso pp esiste. Il nodo da rimuovere è pp.next
      LLNode<Data> toRemove = pp.GetNext().Get();
      if (toRemove == null) return null;  // check di sicurezza, caso che non dovrebbe mai avvenire

      Data res = toRemove.Get();
      pp.SetNext(toRemove.GetNext().Get());
      if (pp.GetNext().IsNull()) tailref.Set(pp);
      size.Decrement();
      return res;
  }

  @Override
  public Data SuccessorNRemove(Data val) {
      
      LLNode<Data> ps = PredSuccFind(val);
      Box<LLNode<Data>> link; 
      
      if (ps == null) { link = headref; }
      else { link = ps.GetNext(); }

      LLNode<Data> succ = link.Get();
      if (succ != null) { // Trovato successore
          Data res = succ.Get();
          link.Set(succ.GetNext().Get());
          if (link.IsNull()) { // coda rimossa
             tailref.Set(ps); // Se ps è null, tailref diventa null
          }
          size.Decrement();
          return res;
      }
      return null;
  }

  @Override
  public void RemovePredecessor(Data val) { PredecessorNRemove(val); }

  @Override
  public void RemoveSuccessor(Data val) { SuccessorNRemove(val); }

  /** CORREZIONI POST-TEST -> ERRORE "LLOrderedSetITest" FALLISCCE SU MIN() E MAX()
   * Dai test emerge un'incongruenza con Min() e Max().
   * I test voglio null, non exception (come giusto che sia a parer mio).
   * Però in una SoortedChain il min e il max sono rispettivamente il primo ed ultimo elemento! Quindi richiamiamo GetFirst e GetLast
   * Qui nasce un errore. Se la lista è vuota, GetFirst dirà "non esiste il primo elemento, lancio ExceptionIndexOutOfBound", come giusto che sia...
   * Siccome però i test richiedono il null come valore di ritorno, faccio un Override "semplice" per aggirare l'eccezione
   * */

  @Override
  public Data Min() {
    if (IsEmpty()) return null;   // Ecco, così se la lista è vuota, ritorno nulll!
    return GetFirst();
  }

  @Override
  public Data Max() {
    if (IsEmpty()) return null;   // Discorso analogo per il massimo, come detto nel commento precedente...
    return GetLast();
  }
  

  /* ************************************************************************ */
  /* Override specific member functions from Chain                            */
  /* ************************************************************************ */

  @Override
  public boolean InsertIfAbsent(Data val) {
    if (Search(val) != null) return false;
    return Insert(val);
  }

  @Override
  public void RemoveOccurrences(Data val) {
    if (val == null) return;

    LLNode<Data> prev = null;
    LLNode<Data> curr = headref.Get();

    // 1. FASE DI RICERCA: Avanzo finché curr < val
    while (curr != null && curr.Get().compareTo(val) < 0) {
      prev = curr;
      curr = curr.GetNext().Get();
    }

    // 2. FASE DI RIMOZIONE: Rimuovo finché curr == val
    while (curr != null && curr.Get().compareTo(val) == 0) {
      // Salvo il riferimento al prossimo nodo
      LLNode<Data> nextNode = curr.GetNext().Get();
      
      // Scollego 'curr' collegando 'prev' direttamente a 'nextNode' (un nodo è stato rimosso!)
      if (prev == null) { headref.Set(nextNode); } // Caso in cui rimuovo la testa
      else { prev.SetNext(nextNode); } // Caso in cui rimuovo un nodo interno o coda
      
      // Gestione Coda: Se nextNode è null, è stato rimosso l'ultimo elemento...
      //                Allora la nuova coda diventa 'prev'!
      if (nextNode == null) { tailref.Set(prev); }
      size.Decrement();
      curr = nextNode;
    }
  } 

}
