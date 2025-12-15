package zapsdtest.personaltest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import apsd.classes.containers.collections.concretecollections.LLSortedChain;
import apsd.classes.containers.collections.concretecollections.VSortedChain;
import apsd.classes.containers.deqs.*;

public class PersonalDeqsTest {

    /****************************************************************************/
    /*  1. TEST DI LOGICA POP & DEQUEUE (Stack & Queue).
    /*  Verifica che Stack segua LIFO e Queue segua FIFO.                       
    /*  Test gia presenti di base ma qui vengono resi più "robusti".            */
    /****************************************************************************/

    @Test
    void testStack_LIFO_Logic() {
        System.out.println("[Personal] Testing Stack LIFO properties...");
        // Come vistoo a lezione, usando windows devo necessariamente inserire il parametro di tipo (WStack<Integer>)
        WStack<Integer> stack = new WStack<>();
        int limit = 50;

        // Push: 0..49
        for (int i = 0; i < limit; i++) {
            stack.Push(i);
        }

        // Pop: mi aspetto 49..0
        for (int i = limit - 1; i >= 0; i--) {
            assertEquals(i, stack.Top(), "Stack Top violated LIFO order");
            stack.Pop();
        }
        
        assertTrue(stack.IsEmpty(), "Stack should be empty after popping all elements");

        System.out.println("[Personal] LIFO Logic on Stack Passed!");
    }

    @Test
    void testQueue_FIFO_Logic() {
        System.out.println("[Personal] Testing Queue FIFO properties...");
        WQueue<Integer> queue = new WQueue<>();
        int limit = 50;

        // Enqueue 0..49
        for (int i = 0; i < limit; i++) {
            queue.Enqueue(i);
        }

        // Dequeue: mi aspetto 0..49
        for (int i = 0; i < limit; i++) {
            assertEquals(i, queue.Head(), "Queue Head violated FIFO order");
            queue.Dequeue();
        }

        assertTrue(queue.IsEmpty(), "Queue should be empty after dequeueing all elements");

        System.out.println("[Personal] FIFO Logic on Queue Passed!");
    }

    
    /****************************************************************************/
    /*  11. TEST STACK (Test generale)
    /*  Verifica la robustezza LIFO con una sequenza complessa di Push/Pop.     */
    /****************************************************************************/

    @Test
    void testStack_ComplexFlow() {
        System.out.println("[Personal] Testing Stack Mixed Operations...");
        WStack<String> stack = new WStack<>();
        
        // Operazione 1: Push A, B
        stack.Push("A");
        stack.Push("B"); 
        assertEquals("B", stack.Top()); // Mi aspetto che B sia in cima (Top)
        
        // Operazione 2: Pop (rimuove B)
        stack.Pop(); 
        assertEquals("A", stack.Top()); // Ora mi aspetto che A sia in cima (Top)
        
        // Operazione 3: Svuotamento
        stack.Pop(); // Viene rimosso anche A -> Ora lo Stack "dovrebbe" essere vuoto
        assertTrue(stack.IsEmpty());
        
        // Operazione 4: Pop su vuoto - ipotesi secondo MOGAVERO: "Lascia fare" (No-Op)
        // Verifico che NON lanci eccezione e che lo stack resti vuoto/coerente.
        assertDoesNotThrow(() -> stack.Pop(), "Pop on empty stack should be safe (no-op)");
        assertTrue(stack.IsEmpty(), "Stack should remain empty");

        System.out.println("[Personal] Stack Complex Flow Passed!");
    }

    
    /****************************************************************************/
    /*  12. TEST QUEUE (Operazioni Miste)
    /*  Verifica la robustezza FIFO con operazioni alternate.                   */
    /****************************************************************************/

    @Test
    void testQueue_ComplexFlow() {
        System.out.println("[Personal] Testing Queue Mixed Operations...");
        WQueue<Integer> queue = new WQueue<>();
        
        // Operazione 1: Enqueue 1, 2, 3
        queue.Enqueue(1);
        queue.Enqueue(2);
        queue.Enqueue(3);
        assertEquals(1, queue.Head());  // Mi aspetto che 1 sia in testa (Head)
        
        // Operazione 2: Dequeue (rimuove 1)
        queue.Dequeue();
        assertEquals(2, queue.Head());  // Ora mi aspetto che 2 sia in testa (Head)
        
        // Operazione 3: Enqueue (rimuove 4)
        queue.Enqueue(4);
        assertEquals(2, queue.Head());  // Head deve restare due, avedo inserito 4 in coda!
        
        // Operazione 4: Svuotamento (rimuovo tutti gli elementi)
        queue.Dequeue(); // Rimuovo 2
        queue.Dequeue(); // Rimuovo 3
        assertEquals(4, queue.Head());  // Ora mi aspetto 4 in testa (Head)
        queue.Dequeue(); // Rimuovo anche 4 -> Queue dovrebbe essere vuota
        
        assertTrue(queue.IsEmpty());
        assertNull(queue.Head(), "Head on empty queue should return null");
        
        // Operazione 5: Dequeue su vuoto
        // -> A lezione abbiamo detto che NON deve lanciare eccezioni, semplicemente non fa nulla. Verifico!
        assertDoesNotThrow(() -> queue.Dequeue(), "Dequeue on empty queue should be safe (no-op)");

        System.out.println("[Personal] Queue Complex Flow Passed!");
    }

}