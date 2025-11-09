package apsd.interfaces.containers.deqs;

import apsd.interfaces.containers.base.ClearableContainer;
import apsd.interfaces.containers.base.InsertableContainer;

public interface Stack<Data> extends ClearableContainer, InsertableContainer<Data> {
  
  // Top
  abstract Data Top();

  // Pop
  abstract void Pop();

  // TopNPop
  default Data TopNPop() {
    Data top = Top(); Pop();
    return top;
  } 

  // SwapTop
  default void SwapTop(final Data val) {
    Pop(); Push(val);
  }

  // TopNSwap
  default Data TopNSwap(final Data val) {
    Data oldTop = TopNPop(); Push(val);
    return oldTop;
  }

  // Push
  abstract void Push(final Data val);

  /* ************************************************************************ */
  /* Override specific member functions from ClearableContainer               */
  /* ************************************************************************ */

  @Override
  default void Clear() { while (!IsEmpty()) { Pop(); } }
  // We "Pop() the Top()" until the whole Stack is empty!

  /* ************************************************************************ */
  /* Override specific member functions from InsertableContainer              */
  /* ************************************************************************ */

  @Override
  default boolean Insert(final Data val) { Push(val); return true; } 
  // Push(val) -> val is now our new Stack head! (LIFO)

}
