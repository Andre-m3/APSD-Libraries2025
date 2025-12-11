package zapsdtest.personaltest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import apsd.classes.utilities.Natural;
import apsd.classes.containers.collections.concretecollections.*;
import apsd.classes.containers.collections.abstractcollections.*;
import apsd.classes.containers.deqs.*;
import apsd.classes.containers.sequences.CircularVector;
import apsd.classes.containers.sequences.DynCircularVector;
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

    // --------------------------------------------------------------------------
    // 10. TEST VETTORE CIRCOLARE (Wrap-around Logic)
    // Verifica che rimuovendo dalla testa e aggiungendo in coda, la capacità 
    // NON aumenti (segno che il buffer circolare funziona).
    // --------------------------------------------------------------------------
    @Test
    void testDynCircularVector_BoundedBehavior() {
        System.out.println("[Personal] Testing DynCircularVector Space Reuse...");
        
        // 1. Setup: Vettore piccolo pieno
        int initialCap = 5;
        DynCircularVector<Integer> vec = new DynCircularVector<>(Natural.Of(initialCap));
        for (int i = 0; i < initialCap; i++) vec.InsertLast(i);
        
        assertEquals(Natural.Of(initialCap), vec.Size());
        
        // 2. STRESS LOOP: Simuliamo una coda che si muove ("rolling")
        // Rimuoviamo dalla testa e inseriamo in coda per 1000 volte.
        int iterations = 1000;
        
        for (int i = 0; i < iterations; i++) {
            // Rimuovo il primo
            vec.RemoveFirst();
            
            // Inserisco un nuovo valore in fondo
            vec.InsertLast(i + 100);
            
            // La size deve rimanere costante (initialCap - 1 + 1 = initialCap)
            // O oscillare tra 4 e 5.
        }
        
        // 3. VERIFICA DATI
        assertEquals(Natural.Of(initialCap), vec.Size(), "Size should be stable");
        
        // 4. VERIFICA CAPACITÀ (Il cuore del test)
        // Se il vettore non fosse circolare, dopo 1000 inserimenti la capacità sarebbe > 1000.
        // Essendo circolare, deve riutilizzare lo spazio. 
        // Accettiamo una crescita fisiologica minima (es. raddoppio una tantum), ma non lineare.
        long currentCap = vec.Capacity().ToLong();
        
        System.out.println("Final Capacity after " + iterations + " ops: " + currentCap);
        
        // Assert rigoroso: La capacità non deve essere esplosa. 
        // Mettiamo un limite largo (es. 20) per stare sicuri, ma tecnicamente dovrebbe essere molto bassa.
        assertTrue(currentCap < 20, 
            "Memory Leak detected! Circular Vector is acting like a Linear Vector. Capacity: " + currentCap);
            
        System.out.println("DynCircularVector Space Reuse Passed!");
    }

    // --------------------------------------------------------------------------
    // 11. TEST STACK (Operazioni Miste)
    // Verifica la robustezza LIFO con una sequenza complessa di Push/Pop.
    // --------------------------------------------------------------------------
    @Test
    void testStack_ComplexFlow() {
        System.out.println("[Personal] Testing Stack Mixed Operations...");
        WStack<String> stack = new WStack<>();
        
        // Op 1: Push A, B
        stack.Push("A");
        stack.Push("B"); 
        assertEquals("B", stack.Top());
        
        // Op 2: Pop (rimuove B)
        stack.Pop(); 
        assertEquals("A", stack.Top());
        
        // Op 3: Svuotamento
        stack.Pop(); // Via A
        assertTrue(stack.IsEmpty());
        
        // Op 4: Pop su vuoto - IPOTESI MOGAVERO: "Lascia fare" (No-Op)
        // Verifico che NON lanci eccezione e che lo stack resti vuoto/coerente.
        assertDoesNotThrow(() -> stack.Pop(), "Pop on empty stack should be safe (no-op)");
        assertTrue(stack.IsEmpty(), "Stack should remain empty");

        System.out.println("Stack Complex Flow Passed!");
    }

    // --------------------------------------------------------------------------
    // 12. TEST QUEUE (Operazioni Miste)
    // Verifica la robustezza FIFO con operazioni alternate.
    // --------------------------------------------------------------------------
    @Test
    void testQueue_ComplexFlow() {
        System.out.println("[Personal] Testing Queue Mixed Operations...");
        WQueue<Integer> queue = new WQueue<>();
        
        // Op 1: Enqueue 1, 2, 3
        queue.Enqueue(1);
        queue.Enqueue(2);
        queue.Enqueue(3); // [1, 2, 3]
        assertEquals(1, queue.Head());
        
        // Op 2: Dequeue 1
        queue.Dequeue(); // [2, 3]
        assertEquals(2, queue.Head());
        
        // Op 3: Enqueue 4
        queue.Enqueue(4); // [2, 3, 4]
        assertEquals(2, queue.Head()); // La testa non deve cambiare
        
        // Op 4: Svuotamento
        queue.Dequeue(); // Via 2
        queue.Dequeue(); // Via 3
        assertEquals(4, queue.Head());
        queue.Dequeue(); // Via 4
        
        assertTrue(queue.IsEmpty());
        assertNull(queue.Head(), "Head on empty queue should return null");
        
        // Op 5: Dequeue su vuoto (WQueue gestisce silenziosamente o lancia eccezione?)
        // Guardando il tuo codice WQueue: if (lst.Size().IsNotZero()) { lst.RemoveFirst(); }
        // Quindi NON deve lanciare eccezioni, semplicemente non fa nulla.
        assertDoesNotThrow(() -> queue.Dequeue(), "Dequeue on empty queue should be safe (no-op)");

        System.out.println("Queue Complex Flow Passed!");
    }

    // --------------------------------------------------------------------------
    // 13. TEST APPROFONDITO VETTORE CIRCOLARE (Deep Logic)
    // Verifica Wrap-around fisico, Linearizzazione su Resize e Stress aritmetico.
    // --------------------------------------------------------------------------

    @Test
    void testDynCircularVector_DeepMechanics() {
        System.out.println("[Personal] Testing DynCircularVector Deep Mechanics...");

        // SCENARIO 1: Wrap-around e Riutilizzo Spazio
        // Capacità 4. Riempiamo: [0, 1, 2, 3]
        DynCircularVector<Integer> vec = new DynCircularVector<>(Natural.Of(4));
        for (int i = 0; i < 4; i++) vec.InsertLast(i);
        
        // Rimuoviamo 2 dalla testa.
        // Logico: [2, 3]. Fisico (probabile): [null, null, 2, 3] (start=2, size=2)
        vec.RemoveFirst();
        vec.RemoveFirst();
        
        // Inseriamo 2 in coda.
        // Logico: [2, 3, 4, 5]. Fisico atteso: [4, 5, 2, 3] (start=2, size=4)
        // Se il wrap funziona, NON deve espandere.
        vec.InsertLast(4);
        vec.InsertLast(5);
        
        assertTrue(vec.Capacity().compareTo(Natural.Of(4)) >= 0, "Capacity should be sufficient");
        assertEquals(Natural.Of(4), vec.Size());
        assertEquals(2, vec.GetFirst(), "First should be 2");
        assertEquals(5, vec.GetLast(), "Last should be 5");

        // SCENARIO 2: Resize su Vettore Avvolto (Il caso più critico!)
        // Stato attuale fisico ipotetico: [4, 5, 2, 3] (pieno e avvolto)
        // Forziamo espansione inserendo il 6.
        // Il vettore deve espandersi, ma soprattutto deve LINEARIZZARE i dati o ricalcolare start.
        // Nuovo stato atteso logico: [2, 3, 4, 5, 6]
        vec.InsertLast(6);
        
        assertTrue(vec.Capacity().compareTo(Natural.Of(4)) > 0, "Capacity must grow");
        assertEquals(Natural.Of(5), vec.Size());
        
        // Verifichiamo integrità dati dopo il resize
        int[] expected = {2, 3, 4, 5, 6};
        for (int i = 0; i < 5; i++) {
            assertEquals(expected[i], vec.GetAt(Natural.Of(i)), 
                "Data corruption at index " + i + " after resize on wrapped vector");
        }

        // SCENARIO 3: Accesso Modulare (Stress Test)
        // Proviamo operazioni miste Head/Tail per muovere 'start' in posizioni strane
        vec.Clear(); // Reset
        // Capacity è cresciuta (es. 8 o 6).
        
        // Inseriamo e rimuoviamo per spostare 'start' nel mezzo
        for (int i = 0; i < 100; i++) {
            vec.InsertLast(i);      // Add
            vec.RemoveFirst();      // Remove immediato -> start avanza di 1
        }
        // Ora start è avanzato di 100 posizioni (modulo capacity). Vector è vuoto.
        assertTrue(vec.IsEmpty());
        
        // Riempiamo di nuovo a cavallo del limite
        vec.InsertLast(10);
        vec.InsertLast(20);
        vec.InsertFirst(5); // Inserimento in testa su buffer circolare
        
        // Atteso: [5, 10, 20]
        assertEquals(5, vec.GetAt(Natural.Of(0)));
        assertEquals(10, vec.GetAt(Natural.Of(1)));
        assertEquals(20, vec.GetAt(Natural.Of(2)));
        assertEquals(3, vec.Size().ToLong());

        System.out.println("DynCircularVector Deep Mechanics Passed!");
    }

    // --------------------------------------------------------------------------
    // 14. TEST VETTORE STATICO CIRCOLARE (CircularVector)
    // Verifica che gli shift su vettore statico non rompano la logica circolare.
    // --------------------------------------------------------------------------
    @Test
    void testCircularVector_StaticBehavior() {
        System.out.println("[Personal] Testing Static CircularVector...");
        
        // Capacità 3.
        CircularVector<String> cvec = new CircularVector<>(Natural.Of(3));
        cvec.SetAt("A", Natural.Of(0));
        cvec.SetAt("B", Natural.Of(1));
        cvec.SetAt("C", Natural.Of(2));
        
        // ShiftRight(1) su vettore pieno.
        // In un vettore lineare [A, B, C] -> [null, A, B]. C cade.
        // In un vettore circolare statico, la logica dipende dall'implementazione di ShiftRight.
        // Se ottimizzata, sposta solo 'start' all'indietro.
        // Stato iniziale: start=0. [A, B, C]
        // ShiftRight(1) -> start = -1 (mod 3) = 2.
        // Dato che è pieno, l'elemento che era all'indice fisico 2 (C) diventa logico 0?
        // NO! ShiftRight inserisce 'null' in testa e fa scorrere tutto. L'ultimo elemento DEVE perdersi.
        
        cvec.ShiftRight(Natural.Of(0), Natural.Of(1));
        
        // Atteso: [null, A, B]
        assertNull(cvec.GetAt(Natural.Of(0)), "Head should be null after shift");
        assertEquals("A", cvec.GetAt(Natural.Of(1)), "A shifted to 1");
        assertEquals("B", cvec.GetAt(Natural.Of(2)), "B shifted to 2");
        // C è perso.
        
        // Proviamo ShiftLeft(1) su [null, A, B]
        // Atteso: [A, B, null]
        cvec.ShiftLeft(Natural.Of(0), Natural.Of(1));
        assertEquals("A", cvec.GetAt(Natural.Of(0)), "A shifted back to 0");
        assertEquals("B", cvec.GetAt(Natural.Of(1)), "B shifted back to 1");
        assertNull(cvec.GetAt(Natural.Of(2)), "Tail should be null");
        
        System.out.println("Static CircularVector Passed!");
    }

    // --------------------------------------------------------------------------
    // 15. TEST MUTABILITÀ ITERATORE
    // Verifica che SetCurrent modifichi davvero la struttura sottostante.
    // --------------------------------------------------------------------------
    @Test
    void testIterator_Mutability() {
        System.out.println("[Personal] Testing Iterator SetCurrent...");
        
        LLList<Integer> list = new LLList<>();
        list.InsertLast(10);
        list.InsertLast(20);
        list.InsertLast(30); // [10, 20, 30]
        
        // Ottengo iteratore
        apsd.interfaces.containers.iterators.MutableForwardIterator<Integer> it = list.FIterator();
        
        // Avanzo al secondo elemento (20)
        it.Next(); 
        assertEquals(20, it.GetCurrent());
        
        // Modifico "al volo" tramite iteratore
        it.SetCurrent(99);
        
        // Verifico che la lista sia cambiata permanentemente
        assertEquals(99, list.GetAt(Natural.Of(1)), "List should be updated by iterator");
        
        // Verifico anche tramite GetLast per sicurezza
        assertEquals(30, list.GetLast());
        
        System.out.println("Iterator Mutability Passed!");
    }

    // --------------------------------------------------------------------------
    // 16. TEST INSIEMI DISGIUNTI (Operazioni Logiche)
    // Verifica il comportamento su insiemi che non hanno elementi in comune.
    // --------------------------------------------------------------------------
    @Test
    void testSets_DisjointOperations() {
        System.out.println("[Personal] Testing Disjoint Sets Operations...");
        
        WSet<Integer> setA = new WSet<>();
        setA.Insert(1); setA.Insert(2); // A = {1, 2}
        
        WSet<Integer> setB = new WSet<>();
        setB.Insert(3); setB.Insert(4); // B = {3, 4}
        
        // INTERSEZIONE (A inter B) -> Deve essere vuoto
        // Nota: Intersection modifica 'this' (setA)
        // Per testare senza distruggere, usiamo copie o testiamo sequenzialmente.
        
        // Test Difference Disgiunta (A - B) -> Deve restare A
        // {1, 2} - {3, 4} = {1, 2}
        WSet<Integer> diffTest = new WSet<>(); 
        diffTest.Insert(1); diffTest.Insert(2);
        diffTest.Difference(setB);
        assertEquals(Natural.Of(2), diffTest.Size());
        assertTrue(diffTest.Exists(1));
        assertTrue(diffTest.Exists(2));
        
        // Test Intersezione Disgiunta (A inter B) -> Vuoto
        WSet<Integer> interTest = new WSet<>();
        interTest.Insert(1); interTest.Insert(2);
        interTest.Intersection(setB);
        assertTrue(interTest.IsEmpty(), "Intersection of disjoint sets must be empty");
        
        System.out.println("Disjoint Sets Logic Passed!");
    }

}