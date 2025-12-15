package zapsdtest.personaltest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import apsd.classes.utilities.Natural;
import apsd.classes.containers.sequences.*;

public class PersonalVectorsTest {

    /****************************************************************************/
    /*  1. TEST SIZE & CAPACITY (DynCircularVector)
    /*  Verifica la coerenza tra dimensione logica e capacità fisica.           */
    /****************************************************************************/

    @Test
    void testDynVector_SizeCapacity_Consistency() {
        System.out.println("[Structural] Testing Size vs Capacity Logic...");
        
        // 1. SETUP: Capacity iniziale piccola
        DynCircularVector<Integer> vec = new DynCircularVector<>(Natural.Of(5));
        assertEquals(Natural.Of(0), vec.Size(), "Initial size should be 0");
        assertEquals(Natural.Of(5), vec.Capacity(), "Initial capacity should be 5");

        // 2. EXPAND: Aumentiamo la size (senza superare capacity)
        vec.Expand(Natural.Of(3)); // Size diventa 3 -> [null, null, null]
        vec.SetAt(10, Natural.Of(0));
        vec.SetAt(20, Natural.Of(1));
        vec.SetAt(30, Natural.Of(2));
        
        assertEquals(Natural.Of(3), vec.Size());
        assertEquals(Natural.Of(5), vec.Capacity());

        // 3. EXPAND (oltre Capacity) -> in alcuni metodi la Realloc è implicita, ma la Expand non lo è mai
        // -> Nota: Expand richiede spazio. Se la capacity è 5 e size 3, Expand(3) porta size a 6.
        vec.Expand(Natural.Of(3)); 
        
        assertEquals(Natural.Of(6), vec.Size(), "Size should be 6");
        assertTrue(vec.Capacity().compareTo(Natural.Of(6)) >= 0, "Capacity should have grown to at least 6");

        // 4. REALLOC (nel caso limite)
        // -> Se riduco la Capacity sotto la Size corrente, la Size deve essere troncata.
        vec.Realloc(Natural.Of(2));
        
        assertEquals(Natural.Of(2), vec.Capacity(), "Capacity forced to 2");
        assertEquals(Natural.Of(2), vec.Size(), "Size should be truncated to match Capacity");
        assertEquals(10, vec.GetAt(Natural.Of(0)), "Data 0 should be preserved");
        assertEquals(20, vec.GetAt(Natural.Of(1)), "Data 1 should be preserved");
        // -> L'accesso a 2 deve fallire
        assertThrows(IndexOutOfBoundsException.class, () -> vec.GetAt(Natural.Of(2)));

        System.out.println("[Structural] Size/Capacity Consistency Passed!");
    }


    /****************************************************************************/
    /*  2. TEST SHIFT (CircularVector)
    /*  Verifica gli spostamenti, specialmente quelli ottimizzati (testa) vs standard. */
    /****************************************************************************/

    @Test
    void testCircularVector_Shift_Logic() {
        System.out.println("[Structural] Testing Circular Shift Logic...");
        
        // SETUP: Vettore di capacità 5 pieno: [A, B, C, D, E]
        CircularVector<String> vec = new CircularVector<>(Natural.Of(5));
        vec.SetAt("A", Natural.Of(0));
        vec.SetAt("B", Natural.Of(1));
        vec.SetAt("C", Natural.Of(2));
        vec.SetAt("D", Natural.Of(3));
        vec.SetAt("E", Natural.Of(4));

        /******************************************************************/
        /* Caso A. SHIFT RIGHT IN TESTA (Ottimizzato: muove solo 'start') */

        // -> Sposto di 1 a partire da 0.
        // -> [A, B, C, D, E] -> ShiftRight(0, 1) -> [null, A, B, C, D]
        vec.ShiftRight(Natural.Of(0), Natural.Of(1));
        
        assertNull(vec.GetAt(Natural.Of(0)), "Pos 0 should be null (gap created)");
        assertEquals("A", vec.GetAt(Natural.Of(1)), "A shifted to 1");
        assertEquals("B", vec.GetAt(Natural.Of(2)), "B shifted to 2");
        assertEquals("D", vec.GetAt(Natural.Of(4)), "D shifted to 4");
        // -> [E] è caduto fuori dal buffer
        // -> Stato attuale: [null, A, B, C, D]
        
        /******************************************************************/
        /* Caso B. SHIFT LEFT NEL MEZZO (Standard: muove i dati)          */

        // -> Sposto a sinistra di 1 a partire da indice 2 (dove c'è 'B').
        // -> [null, A, B, C, D] -> ShiftLeft(2, 1) -> [null, A, C, D, null]
        vec.ShiftLeft(Natural.Of(2), Natural.Of(1));
        
        assertEquals("A", vec.GetAt(Natural.Of(1)), "Pos 1 untouched");
        assertEquals("C", vec.GetAt(Natural.Of(2)), "C shifted left to 2");
        assertEquals("D", vec.GetAt(Natural.Of(3)), "D shifted left to 3");
        assertNull(vec.GetAt(Natural.Of(4)), "Pos 4 should be null (gap from shift)");

        System.out.println("[Structural] Circular Shift Logic Passed!");
    }


    /****************************************************************************/
    /*  3. TEST SHIFT ESTREMI E VUOTI
    /*  Verifica comportamenti su vettori vuoti o shift totali.                 */
    /****************************************************************************/

    @Test
    void testShift_EdgeCases() {
        System.out.println("[Structural] Testing Shift Edge Cases...");
        
        // SETUP: Vettore di capacità 3 pieno: [1, 2, 3]
        CircularVector<Integer> vec = new CircularVector<>(Natural.Of(3));
        vec.SetAt(1, Natural.Of(0));
        vec.SetAt(2, Natural.Of(1));
        vec.SetAt(3, Natural.Of(2));

        // Shift dell'intera dimensione (Svuota tutto)
        vec.ShiftRight(Natural.Of(0), Natural.Of(3));
        // -> Atteso: [null, null, null]
        assertNull(vec.GetAt(Natural.Of(0)));
        assertNull(vec.GetAt(Natural.Of(2)));

        // Shift su vettore "vuoto" (pieno di null)
        // -> Non deve lanciare eccezioni
        vec.ShiftLeft(Natural.Of(0), Natural.Of(1)); 
        
        // Shift con count 0 (Non deve fare nulla)
        vec.SetAt(99, Natural.Of(0));
        vec.ShiftRight(Natural.Of(0), Natural.Of(0));
        assertEquals(99, vec.GetAt(Natural.Of(0)), "Shift with 0 count should verify identity");

        System.out.println("[Structural] Shift Edge Cases Passed!");
    }


    /****************************************************************************/
    /*  4. TEST REALLOC GROWTH (DynVector)
    /*  Verifica che aumentando la capacità i dati esistenti restino ordinati
    /*  e che i nuovi slot allocati siano corretti (null e accessibili).        */
    /****************************************************************************/

    @Test
    void testDynVector_ReallocGrowKeepsOrder() {
        System.out.println("[Structural] Testing DynVector ReallocGrowKeepsOrder...");
        
        // SETUP: Vettore di capacità 3 pieno: [1, 2, 3]
        DynCircularVector<Integer> vec = new DynCircularVector<>(Natural.Of(3));
        vec.Expand(Natural.Of(3));
        vec.SetAt(1, Natural.Of(0));
        vec.SetAt(2, Natural.Of(1));
        vec.SetAt(3, Natural.Of(2));

        // Effettuo una realloc a capacità maggiore
        vec.Realloc(Natural.Of(8));
        assertEquals(Natural.Of(3), vec.Size(), "[Structural][Fail] Size deve restare 3");
        assertEquals(Natural.Of(8), vec.Capacity(), "[Structural][Fail] Capacity deve valere 8");
        assertEquals(1, vec.GetAt(Natural.Of(0)));
        assertEquals(2, vec.GetAt(Natural.Of(1)));
        assertEquals(3, vec.GetAt(Natural.Of(2)));

        vec.Expand(Natural.Of(2)); // sblocco i nuovi slot
        assertNull(vec.GetAt(Natural.Of(3)), "[Structural][Fail] Slot 3 deve essere null");
        assertNull(vec.GetAt(Natural.Of(4)), "[Structural][Fail] Slot 4 deve essere null");
        vec.SetAt(77, Natural.Of(4));
        assertEquals(77, vec.GetAt(Natural.Of(4)));
        assertEquals(1, vec.GetAt(Natural.Of(0)));

        System.out.println("[Structural] DynVector ReallocGrowKeepsOrder Passed!");
    }


    /****************************************************************************/
    /*  5. TEST REALLOC ZERO (DynVector)
    /*  Verifica del caso limite di Realloc a 0: il vettore deve resettarsi
    /*  completamente e lanciare eccezioni su accesso.                          */
    /****************************************************************************/

    @Test
    void testDynVector_ReallocZeroResets() {
        System.out.println("[Structural] Testing DynVector ReallocZeroResets...");

        // SETUP: Vettore di capacità 4 pieno: [10, 20, 30, 40]
        DynCircularVector<Integer> vec = new DynCircularVector<>(Natural.Of(4));
        vec.Expand(Natural.Of(4));
        vec.SetAt(10, Natural.Of(0));
        vec.SetAt(20, Natural.Of(1));
        vec.SetAt(30, Natural.Of(2));
        vec.SetAt(40, Natural.Of(3));

        // Effettuo una realloc a capacità 0 (reset)
        vec.Realloc(Natural.Of(0));
        assertEquals(Natural.Of(0), vec.Size(), "[Structural][Fail] Size deve reset");
        assertEquals(Natural.Of(0), vec.Capacity(), "[Structural][Fail] Capacity deve reset");
        assertThrows(IndexOutOfBoundsException.class, () -> vec.GetAt(Natural.Of(0)),
            "[Structural][Fail] Accesso dopo reset deve fallire");

        // Test ripristino di Capacity
        vec.Realloc(Natural.Of(3));
        vec.Expand(Natural.Of(3));
        vec.SetAt(111, Natural.Of(0));
        vec.SetAt(222, Natural.Of(1));
        vec.SetAt(333, Natural.Of(2));
        assertEquals(111, vec.GetAt(Natural.Of(0)));
        assertEquals(222, vec.GetAt(Natural.Of(1)));
        assertEquals(333, vec.GetAt(Natural.Of(2)));

        System.out.println("[Structural] DynVector ReallocZeroResets Passed!");
    }


    /****************************************************************************/
    /*  6. TEST SIMULAZIONE SHIFT WRAP (CircularVector)
    /*  Simula una manipolazione complessa: Shift a destra per creare spazio,
    /*  inserimento manuale nei gap, e Shift a sinistra per ricompattare.       */
    /****************************************************************************/

    @Test
    void testCircularVector_ShiftWrapAround() {
        System.out.println("[Structural] Testing CircularVector ShiftWrapAround...");

        // SETUP: Vettore di capacità 5 pieno: [1, 2, 3, 4, 5]
        CircularVector<Integer> vec = new CircularVector<>(Natural.Of(5));
        for (int i = 0; i < 5; i++) {
        vec.SetAt(i + 1, Natural.Of(i));
        }

        // ShiftRight(0,2) per creare spazio in testa
        vec.ShiftRight(Natural.Of(0), Natural.Of(2));
        assertNull(vec.GetAt(Natural.Of(0)));
        assertNull(vec.GetAt(Natural.Of(1)));
        assertEquals(1, vec.GetAt(Natural.Of(2)));
        assertEquals(2, vec.GetAt(Natural.Of(3)));
        assertEquals(3, vec.GetAt(Natural.Of(4)));

        // Inserimento manuale nei gap creati
        vec.SetAt(4, Natural.Of(0));
        vec.SetAt(5, Natural.Of(1));
        assertEquals(4, vec.GetAt(Natural.Of(0)));
        assertEquals(5, vec.GetAt(Natural.Of(1)));

        // ShiftLeft(0,2) per ricompattare
        vec.ShiftLeft(Natural.Of(0), Natural.Of(2));
        assertEquals(1, vec.GetAt(Natural.Of(0)));
        assertEquals(2, vec.GetAt(Natural.Of(1)));
        assertEquals(3, vec.GetAt(Natural.Of(2)));
        assertNull(vec.GetAt(Natural.Of(3)));
        assertNull(vec.GetAt(Natural.Of(4)));

        // Verifica finale: inserisco nuovi valori
        vec.SetAt(6, Natural.Of(3));
        vec.SetAt(7, Natural.Of(4));
        assertEquals(6, vec.GetAt(Natural.Of(3)));
        assertEquals(7, vec.GetAt(Natural.Of(4)));

        System.out.println("[Structural] CircularVector ShiftWrapAround Passed!");
    }


    /****************************************************************************/
    /*  10. TEST VETTORE CIRCOLARE (Wrap-around Logic)
    /*  Verifica che rimuovendo dalla testa e aggiungendo in coda,
    /*  la capacità NON aumenti (segno che il "buffer" circolare funziona).     */
    /****************************************************************************/

    @Test
    void testDynCircularVector_BoundedBehavior() {
        System.out.println("[Personal] Testing DynCircularVector Space Reuse...");
        
        // 1. SETUP: Vettore piccolo pieno!
        // Parto da 5. Se la logica è lineare, crescerà subito.
        int initialCap = 5;
        DynCircularVector<Integer> vec = new DynCircularVector<>(Natural.Of(initialCap));
        for (int i = 0; i < initialCap; i++) vec.InsertLast(i);
        
        assertEquals(Natural.Of(initialCap), vec.Size());
        
        // 2. ROLLING LOOP: Rimuovo dalla testa e inserisco in coda.
        // 30 iterazioni sono sufficienti: se il vettore fosse lineare (raddoppio),
        // passerebbe da 5 -> 10 -> 20 -> 40. Quindi supererebbe ampiamente il limite di 10 (arbitrario).
        int iterations = 30;
        
        for (int i = 0; i < iterations; i++) {
            // Rimuovo il primo (libero spazio logico in testa)
            vec.RemoveFirst();
            // Inserisco in coda (dovrebbe usare lo spazio appena liberato)
            vec.InsertLast(i + 100);
        }
        
        // 3. VERIFICA DEI DATI
        // La size deve essere rimasta piena (5)
        assertEquals(Natural.Of(initialCap), vec.Size(), "Size should be stable at " + initialCap);
        
        // 4. VERIFICA DELLA CAPACITY
        long currentCap = vec.Capacity().ToLong();
        System.out.println("Final Capacity after " + iterations + " ops: " + currentCap);
        
        // Assert: La capacità deve essere rimasta contenuta.
        // Se è < 10, significa che al massimo è raddoppiata una volta (5->10),
        // ma non è esplosa a 20 o 40 come farebbe un vettore lineare!
        assertTrue(currentCap <= 10, 
            "Circular Vector is acting like a Linear Vector! Capacity grew too much: " + currentCap);
            
        System.out.println("[Personal]DynCircularVector Space Reuse Passed!");
    }

    
    /****************************************************************************/
    /*  13. TEST APPROFONDITO VETTORE CIRCOLARE (Deep Logic)
    /*  Verifica Wrap-around fisico, Linearizzazione su Resize e Stress aritmetico. */
    /****************************************************************************/

    @Test
    void testDynCircularVector_DeepMechanics() {
        System.out.println("[Personal] Testing DynCircularVector Deep Mechanics...");

        /*************************************************************/
        /* SCENARIO 1: Wrap-around e Riutilizzo Spazio gia esistente */

        // Capacità 4. Riempiamo: [0, 1, 2, 3]
        DynCircularVector<Integer> vec = new DynCircularVector<>(Natural.Of(4));
        for (int i = 0; i < 4; i++) vec.InsertLast(i);
        
        // Logico: [2, 3]. Fisico: [null, null, 2, 3] (start==2 | size==2)
        vec.RemoveFirst();
        vec.RemoveFirst();
        
        // Logico: [2, 3, 4, 5]. Fisico atteso: [4, 5, 2, 3] (start==2 | size==4)
        // Se il wrap funziona, NON deve espandere.
        vec.InsertLast(4);
        vec.InsertLast(5);
        
        assertTrue(vec.Capacity().compareTo(Natural.Of(4)) >= 0, "Capacity should be sufficient");
        assertEquals(Natural.Of(4), vec.Size());
        assertEquals(2, vec.GetFirst(), "First should be 2");
        assertEquals(5, vec.GetLast(), "Last should be 5");

        /*************************************************************/
        /* SCENARIO 2: Resize su Wrapped Vector (caso più critico)   */

        // Stato attuale fisico (ipotetico): [4, 5, 2, 3] (start==2 | size==4)
        // -> Forzo l'espansione inserendo il 6. (mi aspetto size==5)
        // Il vettore deve espandersi, e linearizzare i dati o ricalcolare lo start!
        // Nuovo stato atteso logico: [2, 3, 4, 5, 6] 
        vec.InsertLast(6);
        
        assertTrue(vec.Capacity().compareTo(Natural.Of(4)) > 0, "Capacity must grow");
        assertEquals(Natural.Of(5), vec.Size());
        
        // Verifico l'integrità dei dati dopo il resize
        int[] expected = {2, 3, 4, 5, 6};
        for (int i = 0; i < 5; i++) {
            assertEquals(expected[i], vec.GetAt(Natural.Of(i)), 
                "Data corruption at index " + i + " after resize on wrapped vector");
        }

        /*************************************************************/
        /* SCENARIO 3: Accesso Modulare (Stress Test)                */

        // Provo operazioni miste Head/Tail per muovere 'start' in posizioni strane
        vec.Clear();
        
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

        System.out.println("[Personal] DynCircularVector Deep Mechanics Passed!");
    }

    
    /****************************************************************************/
    /*  14. TEST VETTORE STATICO CIRCOLARE (CircularVector)
    /*  Verifica che gli shift su vettore statico non rompano la logica circolare. */
    /****************************************************************************/

    @Test
    void testCircularVector_StaticBehavior() {
        System.out.println("[Personal] Testing Static CircularVector...");
        
        // Capacità 3.
        CircularVector<String> cvec = new CircularVector<>(Natural.Of(3));
        cvec.SetAt("A", Natural.Of(0));
        cvec.SetAt("B", Natural.Of(1));
        cvec.SetAt("C", Natural.Of(2));
        
        // Con lo shift a destra l'ultimo elemento si perde? E con lo shift a sinistra?
        // Testo entrambi gli scenari (prima destra poi sinistra).
        
        cvec.ShiftRight(Natural.Of(0), Natural.Of(1));
        
        // Atteso: [null, A, B]
        assertNull(cvec.GetAt(Natural.Of(0)), "Head should be null after shift");
        assertEquals("A", cvec.GetAt(Natural.Of(1)), "A shifted to 1");
        assertEquals("B", cvec.GetAt(Natural.Of(2)), "B shifted to 2");
        // C è perso.
        
        // Ora provo ShiftLeft(1) su [null, A, B]
        // Atteso: [A, B, null]
        cvec.ShiftLeft(Natural.Of(0), Natural.Of(1));
        assertEquals("A", cvec.GetAt(Natural.Of(0)), "A shifted back to 0");
        assertEquals("B", cvec.GetAt(Natural.Of(1)), "B shifted back to 1");
        assertNull(cvec.GetAt(Natural.Of(2)), "Tail should be null");
        
        System.out.println("[Personal] Static CircularVector Passed!");
    }

}