package lab6;

/**
 * A simple storage object for an item in a grid. This is a generic class so
 * you can store whatever you want in it. There is also a parameter that keeps
 * track of whether you've visited this GamePiece or not. It's merely an easy
 * and extremely efficient way to manage an item in a grid.
 * @author hornick and taylor
 * @version 2016.04.18
 * 
 * @param <E> The type of the object the GamePiece is storing
 */
public class GamePiece<E> {

    /**
     * Storage for the object contained in this <tt>GamePiece</tt>
     */
    private final E element;

    /**
     * Placeholder for whether or not the <tt>GamePiece</tt> has been visited
     * during recursion
     */
    private boolean visited;

    /**
     * Two parameter constructor for creating a <tt>GamePiece</tt> with the
     * element that's stored in the grid, and whether or not it has been
     * visited.
     * 
     * @param element Holds the object placed in the grid.
     * @param visited Holds state: whether it's been visited or not
     */
    public GamePiece(E element, boolean visited) {
        this.element = element;
        this.visited = visited;
    }

    /**
     * Returns whether or not this <tt>GamePiece</tt> has a bread visited
     * currently placed on it.
     * @return <tt>true</tt> if the visited flag has been set, otherwise
     *         <tt>false</tt>
     */
    public boolean hasBeenVisited() {
        return visited;
    }

    /**
     * Marks gamepiece as having been visited
     */
    public void setVisitedFlag() {
        visited = true;
    }

    /**
     * Removes visited flag from gamepiece
     */
    public void clearVisitedFlag() {
        visited = false;
    }

    /**
     * Returns the element (object) stored in this <tt>GamePiece</tt>.
     * @return The element in this <tt>GamePiece</tt>.
     */
    public E getElement() {
        return element;
    }
}
