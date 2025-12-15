package zapsdtest.personaltest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import apsd.classes.containers.collections.concretecollections.*;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.iterators.ForwardIterator;

public class PersonalSortedTest {

    /****************************************************************************/
    /*  3. TEST INVARIANTE DI ORDINAMENTO (LLSortedChain & VSortedChain)
    /*  Verifica che l'inserimento di numeri CASUALI produca sempre una lista ordinata.    */
    /****************************************************************************/

    @Test
    void testLLSortedChain_RandomIntegrity() {
        // Testo la SortedChain basata su Linked List
        System.out.println("[Personal] Testing LLSortedChain Ordering Integrity...");
        LLSortedChain<Integer> sortedList = new LLSortedChain<>();
        verifySortingIntegrity(sortedList);
    }

    @Test
    void testVSortedChain_RandomIntegrity() {
        // Testo la SortedChain basata su Vector
        System.out.println("[Personal] Testing VSortedChain Ordering Integrity...");
        VSortedChain<Integer> sortedList = new VSortedChain<>();
        verifySortingIntegrity(sortedList);
    }

    // Mertodo Helper (ovviamente privato) per testare qualsiasi SortedChain (polimorfismo!)
    private void verifySortingIntegrity(apsd.interfaces.containers.collections.SortedChain<Integer> chain) {
        Random rand = new Random(12345); // nuovo Rand, ma con Seed fisso (per riproducibilità)!
        int n = 200;

        // Inserimento dei numeri random
        for (int i = 0; i < n; i++) {
        chain.Insert(rand.nextInt(1000));
        }

        assertEquals(Natural.Of(n), chain.Size());

        // Utilizzo iteratore per verificare il corretto ordinamento
        ForwardIterator<Integer> iter = chain.FIterator();
        Integer prev = null;
        
        while (iter.IsValid()) {
        Integer curr = iter.GetCurrent();
        if (prev != null) {
            // Se prev > curr, l'ordine è violato!
            assertTrue(prev.compareTo(curr) <= 0, 
                "Sorting violation! Found " + prev + " before " + curr);
        }
        prev = curr;
        iter.Next();
        }

        // Questo "messaggio" andrebbe messo nei due test specifici, ma per un problema in vscode lo lascio qui!
        System.out.println("[Personal] SortedChain Ordering Integrity Passed!");
    }


    /****************************************************************************/
    /*  8. TEST LIMITI ORDINAMENTO (Pred/Succ Edge Cases)
    /*  Verifica il comportamento ai "margini" di LLSortedChain.                */
    /****************************************************************************/

    @Test
    void testSortedChain_Boundaries() {
        System.out.println("[Personal] Testing SortedChain Boundaries...");
        
        // Scenario: SortedChain [10, 20, 30]
        LLSortedChain<Integer> chain = new LLSortedChain<>();
        chain.Insert(10);
        chain.Insert(20);
        chain.Insert(30);
        
        // Caso A. Predecessore del Minimo -> null
        assertNull(chain.Predecessor(10), "Predecessor of Min should be null");
        
        // Caso B. Successore del Massimo -> null
        assertNull(chain.Successor(30), "Successor of Max should be null");
        
        // Caso C. Predecessore di un valore esterno a sinistra (5) -> null
        assertNull(chain.Predecessor(5), "Predecessor of value < Min should be null");
        
        // Caso D. Successore di un valore esterno a destra (40) -> null
        assertNull(chain.Successor(40), "Successor of value > Max should be null");
        
        // Caso E. Valori intermedi non presenti [pred(25)->20 | succ(25)->30]
        assertEquals(20, chain.Predecessor(25), "Predecessor of 25 should be 20");
        assertEquals(30, chain.Successor(25), "Successor of 25 should be 30");
        
        System.out.println("[Personal] SortedChain Boundaries Passed!");
    }


    /****************************************************************************/
    /*  17. TEST RICERCA BINARIA (VSortedChain)
    /*  Verifica la correttezza della ricerca O(logN) coprendo tutti i casi limite.  */
    /****************************************************************************/

    @Test
    void testBinarySearch_Logic() {
        System.out.println("[Personal] Testing Binary Search Logic (VSortedChain)...");
        
        VSortedChain<Integer> vsc = new VSortedChain<>();

        // CASO 1: Vettore Vuoto
        assertFalse(vsc.Exists(10), "Search in empty vector should fail");

        // CASO 2: Singolo Elemento
        vsc.Insert(10); // [10]
        assertTrue(vsc.Exists(10), "Should find single element");
        assertFalse(vsc.Exists(5), "Should not find smaller element");
        assertFalse(vsc.Exists(15), "Should not find larger element");

        // CASO 3: Dimensione DISPARI (es. 5 elementi) -> [10, 20, 30, 40, 50]
        // Verifica il calcolo del "mid" perfetto
        vsc.Insert(20); vsc.Insert(30); vsc.Insert(40); vsc.Insert(50);
        
        // Test sui bordi e nel mezzo
        assertTrue(vsc.Exists(10), "Should find Min (Odd size)");
        assertTrue(vsc.Exists(50), "Should find Max (Odd size)");
        assertTrue(vsc.Exists(30), "Should find Mid (Odd size)");
        
        // Test assenze intermedie e esterne
        assertFalse(vsc.Exists(9), "Absent < Min");
        assertFalse(vsc.Exists(51), "Absent > Max");
        assertFalse(vsc.Exists(25), "Absent between elements");

        // CASO 4: Dimensione PARI (es. 6 elementi) -> [10, 20, 30, 35, 40, 50]
        // Aggiungiamo un elemento per rompere la simmetria del mid
        vsc.Insert(35); 
        
        assertTrue(vsc.Exists(35), "Should find new element (Even size)");
        assertTrue(vsc.Exists(30), "Should find element left of mid");
        assertTrue(vsc.Exists(40), "Should find element right of mid");

        // CASO 5: Stress Test Ordinamento e Ricerca
        // Se la ricerca binaria o l'inserimento fossero rotti, l'ordine salterebbe.
        vsc.Clear();
        int[] inputs = {5, 1, 9, 3, 7}; // Disordinati
        for (int x : inputs) vsc.Insert(x);
        // Atteso interno: [1, 3, 5, 7, 9]
        
        int[] expected = {1, 3, 5, 7, 9};
        for (int x : expected) {
            assertTrue(vsc.Exists(x), "Binary Search failed to find " + x);
        }
        assertFalse(vsc.Exists(2), "Binary Search found non-existent 2");
        assertFalse(vsc.Exists(4), "Binary Search found non-existent 4");
        assertFalse(vsc.Exists(6), "Binary Search found non-existent 6");
        assertFalse(vsc.Exists(8), "Binary Search found non-existent 8");

        System.out.println("[Personal] Binary Search Logic Passed!");
    }

}