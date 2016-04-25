package lab6;
//package edu.msoe.cs2852;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

/**
 * This is a partial implementation of a SortedArrayList class.
 * The class extends ArrayList (which provides most of the
 * functionality) and rewrites the implementation of a select
 * set of methods to ensure that elements within the sorted
 * array are organized in ascending order.  Only a few of the
 * methods have been optimized for a sorted array.  The methods
 * that have been optimized are explicitly overridden in
 * this subclass.<br />
 * <br />
 * The following methods have been overridden as unsupported since
 * they do not make sense in the context of a sorted container:<br />
 *     public E set(int arg0, E arg1);<br />
 *  public void add(int arg0, E arg1);
 *
 * @author Spring 2014 CS2852 staff
 * @version 2014.04.22-2.40
 *
 * @param <E> The type of data stored in the list.
 */
@SuppressWarnings("serial")
public class SortedArrayList<E extends Comparable<? super E>> extends ArrayList<E> implements List<E>, RandomAccess {

    /**
     * Returns <tt>true</tt> if this list contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this list contains
     * at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     * Makes use of a binarySearch to achieve O(log n) execution time.
     *
     * @param target element whose presence in this list is to be tested.
     * @return <tt>true</tt> if this list contains the specified element.
     * @throws NullPointerException if the specified element is null and this
     *         list does not support null elements (optional).
     */
    @Override
    public boolean contains(Object target) {
        boolean found = false;
        try {
            found = 0<= Collections.binarySearch(this, (E) target);
        } catch(ClassCastException e) {
            // Did not find since target was wrong type
        }
        return found;
    }

    /**
     * Adds the specified element to the list while maintaining
     * sorted order.<br />
     * <br />
     * Uses binarySearch to find the location to insert the element
     * and then calls add(int, E) to do the inserting.
     *
     * @param value element to be added to this list.
     * @return <tt>true</tt> (as per the general contract of the
     *            <tt>Collection.add</tt> method).
     *
     * @throws ClassCastException if the class of the specified element
     *           prevents it from being added to this list.
     * @throws IllegalArgumentException if some aspect of this element
     *            prevents it from being added to this list.
     */
    @Override
    public boolean add(E value) {
        // Find location for the value to be inserted
        int index = Collections.binarySearch(this, value);
        // If no matching value is in the collection, index
        //  will be the negative of the location where it
        //  should be inserted.  For example, if the value
        //  should be inserted into the first location, index
        //  will be -1.  The following modifies index so
        //  that it can be used to specify the location where
        //  value should be inserted.
        if (index<0) {
            ++index;
            index *= -1;
        }
        super.add(index, value);
        return true;
    }

    /**
     * Adds all of the elements in the specified collection to this list
     * and ensures that they are in sorted order.
     * @param collection collection containing elements to be added to this list
     * @return true if this list changed as a result of the call
     */
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        List<E> tmp = new ArrayList<>(collection.size() + this.size());
        tmp.addAll(this);
        tmp.addAll(collection);
        Collections.sort(tmp);
        this.clear();
        super.addAll(tmp);
        return !collection.isEmpty();
    }

    /**
     * @throws UnsupportedOperationException so that a client cannot
     *        cannot corrupt the sorted order of the list.
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        throw new UnsupportedOperationException("Not supported since container maintains sorted order");
    }

    /**
     * @throws UnsupportedOperationException so that a client cannot
     *        cannot corrupt the sorted order of the list.
     */
    @Override
    public E set(int index, E value) {
        throw new UnsupportedOperationException("Not supported since container maintains sorted order");
    }

    /**
     * @throws UnsupportedOperationException so that a client cannot
     *        cannot corrupt the sorted order of the list.
     */
    @Override
    public void add(int index, E value) {
        throw new UnsupportedOperationException("Not supported since container maintains sorted order");
    }

    /**
     * Returns the index in this list of the first occurrence of the specified
     * element, or -1 if this list does not contain this element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>,
     * or -1 if there is no such index.<br />
     * <br />
     * Makes use of binarySearch to find the index.  If the item is not
     * found, the index is set to -1 to match the interface specification.
     *
     * @param target element to search for.
     * @return the index in this list of the first occurrence of the specified
     *            element, or -1 if this list does not contain this element.
     */
    @Override
    public int indexOf(Object target) {
        int index = -1;
        try {
            index = Collections.binarySearch(this, (E)target);
        } catch(ClassCastException e) {
            // Just ignore this (which will return -1).
        }
        return index<0 ? -1 : index;
    }
}

