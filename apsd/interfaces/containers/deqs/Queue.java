package apsd.interfaces.containers.deqs;

import apsd.interfaces.containers.base.ClearableContainer;
import apsd.interfaces.containers.base.InsertableContainer;

public interface Queue<Data> extends ClearableContainer, InsertableContainer<Data> {
  
  // Head
  abstract Data Head();

  // Dequeue
  abstract void Dequeue();

  // HeadNDequeue
  default Data HeadNDequeue() {
    Data head = Head(); Dequeue();
    return head;
  }

  // Enqueue
  abstract void Enqueue(final Data val);

  /* ************************************************************************ */
  /* Override specific member functions from ClearableContainer               */
  /* ************************************************************************ */

  @Override
  default void Clear() { while (!IsEmpty()) { Dequeue(); } }
  // We "Dequeue() the Head()" until the whole Queue is empty!

  /* ************************************************************************ */
  /* Override specific member functions from InsertableContainer              */
  /* ************************************************************************ */

  @Override
  default boolean Insert(final Data val) { Enqueue(val); return true; }
  // Enqueue(val) -> val is now our queue tail! (FIFO)

}
