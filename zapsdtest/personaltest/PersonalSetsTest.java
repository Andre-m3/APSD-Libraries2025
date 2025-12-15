package zapsdtest.personaltest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import apsd.classes.utilities.Natural;
import apsd.classes.containers.collections.abstractcollections.*;
import apsd.classes.containers.collections.concretecollections.*;

public class PersonalSetsTest {

    /****************************************************************************/
    /*  4. TEST PROPRIETÀ INSIEMI (WSet & WOrderedSet)
    /*  Verifica unicità dei dati e gestione di eventuali duplicati.            */
    /****************************************************************************/

    @Test
    void testSets_Uniqueness() {
        System.out.println("[Personal] Testing Set Uniqueness...");

        // Testo WSet (Wrapped)
        WSet<String> set = new WSet<>();
        set.Insert("A");
        set.Insert("B");
        set.Insert("A"); // Duplicato
        boolean inserted = set.Insert("A"); // Duplicato

        assertFalse(inserted, "Insert should return false for duplicates");
        assertEquals(Natural.Of(2), set.Size(), "Set size grew despite duplicates!");
        assertTrue(set.Exists("A"));
        assertTrue(set.Exists("B"));

        // Testo WOrderedSet (basato su SortedChain)
        WOrderedSet<Integer> oSet = new WOrderedSet<>();
        oSet.Insert(10);
        oSet.Insert(5);  // Dovrebbe andare prima del 10 (SortedChain!)
        oSet.Insert(20);
        oSet.Insert(10); // Duplicato (quindi verifico anche l'unicità!)

        assertEquals(Natural.Of(3), oSet.Size());   // 10 non deve essere duplicato!
        assertEquals(5, oSet.Min(), "Min should be 5");
        assertEquals(20, oSet.Max(), "Max should be 20");

        System.out.println("[Personal] Set Uniqueness Passed!");
    }


    /****************************************************************************/
    /*  6. TEST ALGEBRA DEGLI INSIEMI (Union & Difference)
    /*  Verifica comportamenti critici come Union(...) e Difference(...).       */
    /****************************************************************************/
    
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


    /****************************************************************************/
    /*  16. TEST INSIEMI DISGIUNTI (Intersection & Difference)
    /*  Verifica il comportamento su insiemi che non hanno elementi in comune.  */
    /****************************************************************************/

    @Test
    void testSets_DisjointOperations() {
        System.out.println("[Personal] Testing Disjoint Sets Operations...");
        
        // Creo due insiemi disgiunti A e B
        WSet<Integer> setA = new WSet<>();
        setA.Insert(1); setA.Insert(2); // A = {1, 2}
        
        WSet<Integer> setB = new WSet<>();
        setB.Insert(3); setB.Insert(4); // B = {3, 4}
        

        
        // Test DIFFERENCE Disgiunta (A - B) -> Deve restare A
        // {1, 2} - {3, 4} = {1, 2}
        WSet<Integer> diffTest = new WSet<>(); 
        diffTest.Insert(1); diffTest.Insert(2);
        diffTest.Difference(setB);
        assertEquals(Natural.Of(2), diffTest.Size());
        assertTrue(diffTest.Exists(1));
        assertTrue(diffTest.Exists(2));
        
        // Test INTERSECTION Disgiunta (A inter B) -> Vuoto
        // Nota: intersection (A inter B) -> Deve essere vuoto
        // Per testare senza distruggere qualcosa (this), potremmo usare copie o testare sequenzialmente.
        WSet<Integer> interTest = new WSet<>();
        interTest.Insert(1); interTest.Insert(2);
        interTest.Intersection(setB);
        assertTrue(interTest.IsEmpty(), "Intersection of disjoint sets must be empty");
        
        System.out.println("[Personal] Disjoint Sets Logic Passed!");
    }

}
