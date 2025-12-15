package zapsdtest.personaltest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import apsd.classes.utilities.Natural;
import apsd.classes.containers.collections.concretecollections.*;
import apsd.interfaces.containers.iterators.*;

public class PersonalListsTest {

    /****************************************************************************/
    /*  2. STRESS TEST VETTORI DINAMICI (VList)
    /*  Verifica che le riallocazioni (Expand/Shrink) non corrompano i dati.    */
    /****************************************************************************/

    @Test
    void testVList_Stress_Realloc() {
        System.out.println("[Personal] Stress Testing VList (Dynamic Vector Reallocation)...");
        VList<Integer> list = new VList<>();
        int stressLimit = 1000; // Limite alto così da forzare molte resize

        // Fase 1: Inserimento "pesante" (Testa Expand/Realloc)
        for (int i = 0; i < stressLimit; i++) {
            list.InsertLast(i);
        }
        assertEquals(Natural.Of(stressLimit), list.Size(), "Size mismatch after stress insert");

        // Fase 2: Verifica integrità dati
        for (int i = 0; i < stressLimit; i++) {
            assertEquals(i, list.GetAt(Natural.Of(i)), "Data corruption at index " + i);
        }

        // Fase 3: Rimozione di tutti i dati dalla testa (shift peggiore) (Testa Reduce/Shrink)
        for (int i = 0; i < stressLimit; i++) {
            list.RemoveFirst();
        }
        assertTrue(list.IsEmpty(), "List should be empty after stress removal");

        System.out.println("[Personal] Stress Testing VList Passed!");
    }
    

    /****************************************************************************/
    /*  5. TEST GESTIONE NULL (Solo Liste non ordinate)
    /*  Verifica la corretta gestione dei null in Liste.                        */
    /****************************************************************************/

    @Test
    void testList_NullHandling() {
        System.out.println("[Personal] Testing Null Handling in Lists...");
        LLList<String> list = new LLList<>();

        // Scenario: [A, null, B, null, C]
        list.InsertLast("A");
        list.InsertLast(null); // Inserimento permesso
        list.InsertLast("B");
        list.InsertLast(null);
        list.InsertLast("C");

        assertEquals(Natural.Of(5), list.Size());

        // Verifica delle posizioni
        assertNull(list.GetAt(Natural.Of(1)), "Pos 1 should be null");
        assertNull(list.GetAt(Natural.Of(3)), "Pos 3 should be null");

        // Il metodo Remove(val) proibisce val == null.
        // Allora testo anche che Remove(null) ritorni false e NON modifichi la lista.
        boolean result = list.Remove(null);
        assertFalse(result, "Remove(null) should return false as per specs");
        assertEquals(Natural.Of(5), list.Size(), "Size should not change after Remove(null)");

        // Per rimuovere i null, uso RemoveAt. Testo allora che questo funzioni.
        list.RemoveAt(Natural.Of(1)); 

        // Ora la size deve essere 4 e 'B' deve essere shiftato indietro
        assertEquals(Natural.Of(4), list.Size());
        assertEquals("B", list.GetAt(Natural.Of(1)), "After RemoveAt(1), B should shift to pos 1");

        System.out.println("[Personal] Null Handling LLList Passed!");
    }

    
    /****************************************************************************/
    /*  7. TEST INDIPENDENZA COPIE (Deep Copy)
    /*  Verifica che i costruttori di copia creino strutture indipendenti.      */
    /****************************************************************************/

    @Test
    void testCopy_Independence() {
        System.out.println("[Personal] Testing Copy Constructor Independence...");
        
        // Creo una generica lista "originale"
        VList<Integer> original = new VList<>();
        original.InsertLast(1);
        original.InsertLast(2);
        
        // Creo copia (usando costruttore che prende TraversableContainer)
        VList<Integer> copy = new VList<>(original);
        
        // Modifico la copia (mi aspetto che la copia cambi, ma l'originale no!)
        copy.InsertLast(3);
        copy.RemoveFirst(); // Rimuove 1
        
        // Verifico che l'originale sia INTATTO
        assertEquals(Natural.Of(2), original.Size(), "Original size changed!");
        assertEquals(1, original.GetFirst(), "Original content changed!");
        
        // Verifico la copia
        assertEquals(Natural.Of(2), copy.Size(), "Copy size incorrect"); // Rimasto 2 e 3
        assertEquals(2, copy.GetFirst());
        
        System.out.println("[Personal] Copy Independence Passed!");
    }


    /****************************************************************************/
    /*  9. TEST ITERATORI AVANZATI (DataNNext / DataNPrev)
    /*  Verifica l'uso combinato di accesso e movimento negli iteratori.        */
    /****************************************************************************/
    
    @Test
    void testIterators_Advanced() {
        System.out.println("[Personal] Testing Advanced Iterator Operations...");
        
        // Scenario: LLList [A, B]
        LLList<String> list = new LLList<>();
        list.InsertLast("A");
        list.InsertLast("B");
        
        // Forward (verifico che l'iteratore parta dal primo elemento)
        ForwardIterator<String> fiter = list.FIterator();
        assertEquals("A", fiter.DataNNext(), "DataNNext should return current then advance");
        assertEquals("B", fiter.GetCurrent(), "Should be at B now");
        
        // Backward (verifico che l'iteratore parta dall'ultimo elemento)        
        BackwardIterator<String> biter = list.BIterator();
        assertTrue(biter.IsValid());
        assertEquals("B", biter.DataNPrev(), "DataNPrev should return Last then move back");
        assertEquals("A", biter.GetCurrent(), "Should be at A now");
        
        System.out.println("[Personal] Advanced Iterators Passed!");
    }


    /****************************************************************************/
    /*  15. TEST MUTABILITÀ ITERATORE
    /*  Verifica che SetCurrent modifichi davvero la struttura sottostante.     */
    /****************************************************************************/

    @Test
    void testIterator_Mutability() {
        System.out.println("[Personal] Testing Iterator SetCurrent...");
        
        // Scenario: LLList [10, 20, 30]
        LLList<Integer> list = new LLList<>();
        list.InsertLast(10);
        list.InsertLast(20);
        list.InsertLast(30);
        
        // Ottengo iteratore
        apsd.interfaces.containers.iterators.MutableForwardIterator<Integer> iter = list.FIterator();
        
        // Avanzo al secondo elemento (20)
        iter.Next(); 
        assertEquals(20, iter.GetCurrent());
        
        // Modifico "al volo" tramite iteratore
        iter.SetCurrent(99);
        
        // Verifico che la lista sia cambiata permanentemente
        assertEquals(99, list.GetAt(Natural.Of(1)), "List should be updated by iterator");
        
        // Verifico anche tramite GetLast (per sicurezza)
        assertEquals(30, list.GetLast());
        
        System.out.println("[Personal] Iterator Mutability Passed!");
    }

}