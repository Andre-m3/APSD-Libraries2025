package apsd.interfaces.containers.collections;

public interface OrderedChain<Data extends Comparable<? super Data>> extends OrderedSet<Data>, Chain<Data> {

} // Must extend Chain and OrderedSet
