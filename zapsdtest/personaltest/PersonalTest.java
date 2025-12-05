package zapsdtest.personaltest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import apsd.classes.utilities.Natural;
import apsd.classes.containers.collections.concretecollections.*;
import apsd.classes.containers.collections.abstractcollections.*;
import apsd.classes.containers.deqs.*;
import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;

import java.util.Random;

/**
 * Personal Test Suite: Verifiche concettuali, stress test e invarianza.
 */
public class PersonalTest {

    // --------------------------------------------------------------------------
    // 1. TEST DI LOGICA DEQUE (Stack & Queue)
    // Verifichiamo rigorosamente l'ordine LIFO e FIFO con sequenze miste.
    // --------------------------------------------------------------------------

    @Test
    void testStack_LIFO_Logic() {
        System.out.println("[Personal] Testing Stack LIFO properties...");
        WStack<Integer> stack = new WStack<>();
        int limit = 100;

        // Push sequenziale 0..99
        for (int i = 0; i < limit; i++) {
        stack.Push(i);
        }

        // Pop sequenziale: mi aspetto 99..0
        for (int i = limit - 1; i >= 0; i--) {
        assertEquals(i, stack.Top(), "Stack Top violated LIFO order");
        stack.Pop();
        }
        
        assertTrue(stack.IsEmpty(), "Stack should be empty after popping all elements");
    }

    @Test
    void testQueue_FIFO_Logic() {
        System.out.println("[Personal] Testing Queue FIFO properties...");
        WQueue<Integer> queue = new WQueue<>();
        int limit = 100;

        // Enqueue sequenziale 0..99
        for (int i = 0; i < limit; i++) {
        queue.Enqueue(i);
        }

        // Dequeue sequenziale: mi aspetto 0..99
        for (int i = 0; i < limit; i++) {
        assertEquals(i, queue.Head(), "Queue Head violated FIFO order");
        queue.Dequeue();
        }

        assertTrue(queue.IsEmpty(), "Queue should be empty after dequeueing all elements");
    }

    // --------------------------------------------------------------------------
    // 2. STRESS TEST VETTORI DINAMICI (VList)
    // Verifica che le riallocazioni (Expand/Shrink) non corrompano i dati.
    // --------------------------------------------------------------------------

    @Test
    void testVList_Stress_Realloc() {
        System.out.println("[Personal] Stress Testing VList (Dynamic Vector Reallocation)...");
        VList<Integer> list = new VList<>();
        int stressLimit = 5000; // Abbastanza alto da forzare molte resize

        // Fase 1: Inserimento massivo (Testa Expand/Realloc)
        for (int i = 0; i < stressLimit; i++) {
        list.InsertLast(i);
        }
        assertEquals(Natural.Of(stressLimit), list.Size(), "Size mismatch after stress insert");

        // Fase 2: Verifica integrità dati
        for (int i = 0; i < stressLimit; i++) {
        assertEquals(i, list.GetAt(Natural.Of(i)), "Data corruption at index " + i);
        }

        // Fase 3: Rimozione massiva (Testa Reduce/Shrink)
        // Rimuoviamo sempre dalla testa per forzare lo shift peggiore O(N) ogni volta
        // Nota: Se questo test è troppo lento, la tua implementazione di Shift è pesante (ma corretta)
        for (int i = 0; i < stressLimit; i++) {
        list.RemoveFirst();
        }
        assertTrue(list.IsEmpty(), "List should be empty after stress removal");
    }

    // --------------------------------------------------------------------------
    // 3. TEST INVARIANTE DI ORDINAMENTO (LLSortedChain & VSortedChain)
    // Verifica che l'inserimento di numeri CASUALI produca sempre una lista ordinata.
    // --------------------------------------------------------------------------

    @Test
    void testLLSortedChain_RandomIntegrity() {
        System.out.println("[Personal] Testing LLSortedChain Ordering Integrity...");
        LLSortedChain<Integer> sortedList = new LLSortedChain<>();
        verifySortingIntegrity(sortedList);
    }

    @Test
    void testVSortedChain_RandomIntegrity() {
        System.out.println("[Personal] Testing VSortedChain Ordering Integrity...");
        VSortedChain<Integer> sortedList = new VSortedChain<>();
        verifySortingIntegrity(sortedList);
    }

    // Helper generico per testare qualsiasi SortedChain (polimorfismo!)
    private void verifySortingIntegrity(apsd.interfaces.containers.collections.SortedChain<Integer> chain) {
        Random rand = new Random(12345); // Seed fisso per riproducibilità
        int n = 200;

        // Inseriamo numeri random
        for (int i = 0; i < n; i++) {
        chain.Insert(rand.nextInt(1000));
        }

        assertEquals(Natural.Of(n), chain.Size());

        // Verifichiamo che siano ordinati scorrendo con l'iteratore
        ForwardIterator<Integer> iter = chain.FIterator();
        Integer prev = null;
        
        while (iter.IsValid()) {
        Integer curr = iter.GetCurrent();
        if (prev != null) {
            assertTrue(prev.compareTo(curr) <= 0, 
            "Sorting violation! Found " + prev + " before " + curr);
        }
        prev = curr;
        iter.Next();
        }
    }

    // --------------------------------------------------------------------------
    // 4. TEST PROPRIETÀ INSIEMI (WSet & WOrderedSet)
    // Verifica unicità e gestione duplicati.
    // --------------------------------------------------------------------------

    @Test
    void testSets_Uniqueness() {
        System.out.println("[Personal] Testing Set Uniqueness...");

        // Test WSet (VList based)
        WSet<String> set = new WSet<>();
        set.Insert("A");
        set.Insert("B");
        set.Insert("A"); // Duplicato
        boolean inserted = set.Insert("A"); // Duplicato

        assertFalse(inserted, "Insert should return false for duplicates");
        assertEquals(Natural.Of(2), set.Size(), "Set size grew despite duplicates!");
        assertTrue(set.Exists("A"));
        assertTrue(set.Exists("B"));

        // Test WOrderedSet (SortedChain based)
        WOrderedSet<Integer> oSet = new WOrderedSet<>();
        oSet.Insert(10);
        oSet.Insert(5);  // Dovrebbe andare prima del 10
        oSet.Insert(20);
        oSet.Insert(10); // Duplicato

        assertEquals(Natural.Of(3), oSet.Size());
        assertEquals(5, oSet.Min(), "Min should be 5");
        assertEquals(20, oSet.Max(), "Max should be 20");
    }

    // --------------------------------------------------------------------------
    // 5. TEST GESTIONE NULL (Solo Liste non ordinate)
    // Verifica che le liste accettino null (se la tua implementazione lo permette).
    // --------------------------------------------------------------------------

    @Test
    void testList_NullHandling() {
        System.out.println("[Personal] Testing Null Handling in Lists...");
        LLList<String> list = new LLList<>();

        // Scenario: [A, null, B, null, C]
        list.InsertLast("A");
        list.InsertLast(null); // Inserimento permesso (visto che abbiamo tolto i check in Insert)
        list.InsertLast("B");
        list.InsertLast(null);
        list.InsertLast("C");

        assertEquals(Natural.Of(5), list.Size());

        // Verifica posizioni
        assertNull(list.GetAt(Natural.Of(1)), "Pos 1 should be null");
        assertNull(list.GetAt(Natural.Of(3)), "Pos 3 should be null");

        // --- MODIFICA RISPETTO A PRIMA ---
        // Il metodo Remove(val) del docente proibisce val == null.
        // Quindi testiamo che Remove(null) ritorni false e NON modifichi la lista.
        boolean result = list.Remove(null);
        assertFalse(result, "Remove(null) should return false as per specs");
        assertEquals(Natural.Of(5), list.Size(), "Size should not change after Remove(null)");

        // Se vogliamo rimuovere un null, dobbiamo usare RemoveAt (per indice), che è permesso.
        // Rimuoviamo il null all'indice 1.
        list.RemoveAt(Natural.Of(1)); 

        // Ora la size deve essere 4 e 'B' deve essere scivolato indietro
        assertEquals(Natural.Of(4), list.Size());
        assertEquals("B", list.GetAt(Natural.Of(1)), "After RemoveAt(1), B should shift to pos 1");

        System.out.println("[Personal] Null Handling LLList Passed!");
    }

    // --------------------------------------------------------------------------
    // 6. TEST ALGEBRA DEGLI INSIEMI (Self-Operations)
    // Verifica comportamenti critici come Union(this) e Difference(this).
    // --------------------------------------------------------------------------
    
    @Test
    void testSet_Algebra_EdgeCases() {
        System.out.println("[Personal] Testing Set Algebra Edge Cases...");
        
        // Test su WSet
        WSet<String> set = new WSet<>();
        set.Insert("A");
        set.Insert("B");
        
        // Unione con se stesso: NON deve cambiare nulla (no duplicati)
        set.Union(set);
        assertEquals(Natural.Of(2), set.Size(), "Union with itself should not increase size");
        
        // Differenza con se stesso: DEVE svuotare l'insieme
        set.Difference(set);
        assertTrue(set.IsEmpty(), "Difference with itself should clear the set");
        
        // Test su WOrderedSet (per verificare che la delega funzioni)
        WOrderedSet<Integer> oSet = new WOrderedSet<>();
        oSet.Insert(10);
        oSet.Insert(20);
        oSet.Difference(oSet);
        assertTrue(oSet.IsEmpty(), "OrderedSet Difference with itself failed");
        
        System.out.println("[Personal] Set Algebra Logic Passed!");
    }

    // --------------------------------------------------------------------------
    // 7. TEST INDIPENDENZA COPIE (Deep Copy)
    // Verifica che i costruttori di copia creino strutture indipendenti.
    // --------------------------------------------------------------------------
    @Test
    void testCopy_Independence() {
        System.out.println("[Personal] Testing Copy Constructor Independence...");
        
        // 1. Creo lista originale
        VList<Integer> original = new VList<>();
        original.InsertLast(1);
        original.InsertLast(2);
        
        // 2. Creo copia (usando costruttore che prende TraversableContainer)
        VList<Integer> copy = new VList<>(original);
        
        // 3. Modifico la copia
        copy.InsertLast(3);
        copy.RemoveFirst(); // Rimuove 1
        
        // 4. Verifico che l'originale sia INTATTO
        assertEquals(Natural.Of(2), original.Size(), "Original size changed!");
        assertEquals(1, original.GetFirst(), "Original content changed!");
        
        // 5. Verifico la copia
        assertEquals(Natural.Of(2), copy.Size()); // Rimasto 2 e 3
        assertEquals(2, copy.GetFirst());
        
        System.out.println("[Personal] Copy Independence Passed!");
    }

    // --------------------------------------------------------------------------
    // 8. TEST LIMITI ORDINAMENTO (Pred/Succ Edge Cases)
    // Verifica il comportamento ai bordi di LLSortedChain.
    // --------------------------------------------------------------------------
    @Test
    void testSortedChain_Boundaries() {
        System.out.println("[Personal] Testing SortedChain Boundaries...");
        
        LLSortedChain<Integer> chain = new LLSortedChain<>();
        chain.Insert(10);
        chain.Insert(20);
        chain.Insert(30); // [10, 20, 30]
        
        // A. Predecessore del Minimo -> null
        assertNull(chain.Predecessor(10), "Predecessor of Min should be null");
        
        // B. Successore del Massimo -> null
        assertNull(chain.Successor(30), "Successor of Max should be null");
        
        // C. Predecessore di un valore esterno a sinistra (5) -> null
        assertNull(chain.Predecessor(5), "Predecessor of value < Min should be null");
        
        // D. Successore di un valore esterno a destra (40) -> null
        assertNull(chain.Successor(40), "Successor of value > Max should be null");
        
        // E. Valori intermedi non presenti
        // Cerco pred(25). Dovrebbe essere 20.
        assertEquals(20, chain.Predecessor(25), "Predecessor of 25 should be 20");
        // Cerco succ(25). Dovrebbe essere 30.
        assertEquals(30, chain.Successor(25), "Successor of 25 should be 30");
        
        System.out.println("[Personal] SortedChain Boundaries Passed!");
    }

    // --------------------------------------------------------------------------
    // 9. TEST ITERATORI AVANZATI (DataNNext / DataNPrev)
    // Verifica l'uso combinato di accesso e movimento.
    // --------------------------------------------------------------------------

    @Test
    void testIterators_Advanced() {
        System.out.println("[Personal] Testing Advanced Iterator Operations...");
        
        LLList<String> list = new LLList<>();
        list.InsertLast("A");
        list.InsertLast("B");
        
        // Forward
        ForwardIterator<String> it = list.FIterator();
        assertEquals("A", it.DataNNext(), "DataNNext should return current then advance");
        assertEquals("B", it.GetCurrent(), "Should be at B now");
        
        // Backward
        // Nota: BIterator parte dalla fine? Dipende dalla tua implementazione in LLChainBase.
        // Di solito BIterator parte dall'ultimo elemento.
        // Verifica: Reset() in LLChainBase posiziona curr = -1? No, Reset() mette curr = Size-1 (se array based).
        // Verifichiamo la tua logica ListBRefIterator: Reset() -> curr = -1, poi riempie array. 
        // Aspetta, il Reset() del BRefIterator metteva curr = -1 e riempiva.
        // Ma alla fine del Reset, curr deve puntare all'ultimo elemento valido!
        // Se curr rimane -1 dopo il loop di riempimento, l'iteratore parte "finito".
        
        // Controlliamo il tuo codice ListBRefIterator.Reset():
        // for (...) { arr.SetAt(..., ++curr); }
        // Sì! curr viene incrementato. Se size=2, curr diventa 0 poi 1. 
        // Quindi alla fine punta all'ultimo elemento. OK!
        
        BackwardIterator<String> bit = list.BIterator();
        assertTrue(bit.IsValid());
        assertEquals("B", bit.DataNPrev(), "DataNPrev should return Last then move back");
        assertEquals("A", bit.GetCurrent(), "Should be at A now");
        
        System.out.println("[Personal] Advanced Iterators Passed!");
    }

}