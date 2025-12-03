package zapsdtest.personaltest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import apsd.classes.utilities.Natural;
import apsd.classes.containers.sequences.*;

/**
 * Structural Tests: Verifiche approfondite su Size, Capacity e Shift.
 */
public class StructuralTests {

  // --------------------------------------------------------------------------
  // 1. TEST SIZE & CAPACITY (DynCircularVector)
  // Verifica la coerenza tra dimensione logica e capacità fisica.
  // --------------------------------------------------------------------------

  @Test
  void testDynVector_SizeCapacity_Consistency() {
    System.out.println("[Structural] Testing Size vs Capacity Logic...");
    
    // 1. Creazione: Capacity iniziale piccola
    DynCircularVector<Integer> vec = new DynCircularVector<>(Natural.Of(5));
    assertEquals(Natural.Of(0), vec.Size(), "Initial size should be 0");
    assertEquals(Natural.Of(5), vec.Capacity(), "Initial capacity should be 5");

    // 2. Expand: Aumentiamo la size (senza superare capacity)
    vec.Expand(Natural.Of(3)); // Size diventa 3 -> [null, null, null]
    vec.SetAt(10, Natural.Of(0));
    vec.SetAt(20, Natural.Of(1));
    vec.SetAt(30, Natural.Of(2));
    
    assertEquals(Natural.Of(3), vec.Size());
    assertEquals(Natural.Of(5), vec.Capacity());

    // 3. Expand oltre Capacity (Realloc automatica implicita in alcuni metodi, ma Expand è esplicito)
    // Nota: Expand di solito richiede spazio. Se la capacity è 5 e size 3, Expand(3) porta size a 6.
    // DynVector dovrebbe gestire la Realloc automaticamente.
    vec.Expand(Natural.Of(3)); 
    
    assertEquals(Natural.Of(6), vec.Size(), "Size should be 6");
    assertTrue(vec.Capacity().compareTo(Natural.Of(6)) >= 0, "Capacity should have grown to at least 6");

    // 4. Realloc "Tranciante" (Caso limite)
    // Se riduco la Capacity sotto la Size corrente, la Size deve essere troncata.
    vec.Realloc(Natural.Of(2));
    
    assertEquals(Natural.Of(2), vec.Capacity(), "Capacity forced to 2");
    assertEquals(Natural.Of(2), vec.Size(), "Size should be truncated to match Capacity");
    assertEquals(10, vec.GetAt(Natural.Of(0)), "Data 0 should be preserved");
    assertEquals(20, vec.GetAt(Natural.Of(1)), "Data 1 should be preserved");
    // L'accesso a 2 deve fallire
    assertThrows(IndexOutOfBoundsException.class, () -> vec.GetAt(Natural.Of(2)));

    System.out.println("Size/Capacity Consistency Passed!");
  }

  // --------------------------------------------------------------------------
  // 2. TEST SHIFT (CircularVector)
  // Verifica gli spostamenti, specialmente quelli ottimizzati (testa) vs standard.
  // --------------------------------------------------------------------------

  @Test
  void testCircularVector_Shift_Logic() {
    System.out.println("[Structural] Testing Circular Shift Logic...");
    
    // Setup: Vettore di capacità 5 pieno: [A, B, C, D, E]
    CircularVector<String> vec = new CircularVector<>(Natural.Of(5));
    vec.SetAt("A", Natural.Of(0));
    vec.SetAt("B", Natural.Of(1));
    vec.SetAt("C", Natural.Of(2));
    vec.SetAt("D", Natural.Of(3));
    vec.SetAt("E", Natural.Of(4));

    // A. SHIFT RIGHT IN TESTA (Ottimizzato: muove solo 'start')
    // Sposto di 1 a partire da 0.
    // [A, B, C, D, E] -> ShiftRight(0, 1) -> [null, A, B, C, D] (E viene perso/sovrascritto se statico)
    // Nota: Nei vettori statici, ShiftRight verso la fine fa cadere l'ultimo elemento.
    vec.ShiftRight(Natural.Of(0), Natural.Of(1));
    
    assertNull(vec.GetAt(Natural.Of(0)), "Pos 0 should be null (gap created)");
    assertEquals("A", vec.GetAt(Natural.Of(1)), "A shifted to 1");
    assertEquals("B", vec.GetAt(Natural.Of(2)), "B shifted to 2");
    assertEquals("D", vec.GetAt(Natural.Of(4)), "D shifted to 4");
    // E è caduto fuori dal buffer

    // Stato attuale: [null, A, B, C, D]
    
    // B. SHIFT LEFT NEL MEZZO (Standard: muove i dati)
    // Sposto a sinistra di 1 a partire da indice 2 (dove c'è 'B').
    // [null, A, B, C, D] -> ShiftLeft(2, 1) -> [null, A, C, D, null]
    // B viene sovrascritto da C. C da D. D diventa null.
    vec.ShiftLeft(Natural.Of(2), Natural.Of(1));
    
    assertEquals("A", vec.GetAt(Natural.Of(1)), "Pos 1 untouched");
    assertEquals("C", vec.GetAt(Natural.Of(2)), "C shifted left to 2");
    assertEquals("D", vec.GetAt(Natural.Of(3)), "D shifted left to 3");
    assertNull(vec.GetAt(Natural.Of(4)), "Pos 4 should be null (gap from shift)");

    System.out.println("Circular Shift Logic Passed!");
  }

  // --------------------------------------------------------------------------
  // 3. TEST SHIFT ESTREMI E VUOTI
  // Verifica comportamenti su vettori vuoti o shift totali.
  // --------------------------------------------------------------------------

  @Test
  void testShift_EdgeCases() {
    System.out.println("[Structural] Testing Shift Edge Cases...");
    
    CircularVector<Integer> vec = new CircularVector<>(Natural.Of(3));
    vec.SetAt(1, Natural.Of(0));
    vec.SetAt(2, Natural.Of(1));
    vec.SetAt(3, Natural.Of(2)); // [1, 2, 3]

    // Shift dell'intera dimensione (Svuota tutto)
    vec.ShiftRight(Natural.Of(0), Natural.Of(3));
    // Atteso: [null, null, null]
    assertNull(vec.GetAt(Natural.Of(0)));
    assertNull(vec.GetAt(Natural.Of(2)));

    // Shift su vettore "vuoto" (pieno di null)
    // Non deve lanciare eccezioni
    vec.ShiftLeft(Natural.Of(0), Natural.Of(1)); 
    
    // Shift con count 0 (Non deve fare nulla)
    vec.SetAt(99, Natural.Of(0));
    vec.ShiftRight(Natural.Of(0), Natural.Of(0));
    assertEquals(99, vec.GetAt(Natural.Of(0)), "Shift with 0 count should verify identity");

    System.out.println("Shift Edge Cases Passed!");
  }
}