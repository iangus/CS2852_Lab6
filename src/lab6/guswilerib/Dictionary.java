/*
 * CS2852 - 041
 * Spring 2016
 * Lab 4
 * Name: Ian Guswiler
 * Created: 4/5/2016
 */

package lab6.guswilerib;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Scanner;

/**
 * Collection of word strings that is to be used as a dictionary
 *
 * @author Ian Guswiler
 * @version 4/11/16
 */
public class Dictionary {
    private final Collection<String> collection;

    /**
     * creates a new dictionary with an empty collection
     * @param collection collection to be used in the dictionary. If it is not empty it will be cleared.
     */
    public Dictionary(Collection<String> collection){
        if(collection == null){
            throw new NullPointerException("The collection passed to the dictionary constructor is null.");
        } else if(!collection.isEmpty()){
            collection.clear();
        }
        this.collection = collection;
    }

    /**
     * loads a file into the dictionary
     * @param fileName name of file to be loaded into the dictionary
     * @return time taken to add the words to the dictionary
     */
    public long load(String fileName){
        long nanoStart = 0;
        long nanoEnd = 0;

        File file = new File(fileName);
        try(Scanner fileScan = new Scanner(file)){
            nanoStart = System.nanoTime();
            while(fileScan.hasNext()){
                collection.add(fileScan.next());
            }
            nanoEnd = System.nanoTime();
        } catch (FileNotFoundException e){
            System.err.println("The file " + fileName + " could not be found.");
        }

        return nanoEnd - nanoStart;
    }

    /**
     * checks if the specified word is in the dictionary collection
     * @param target word to be searched for
     * @return returns a boolean representing if the word was found or not
     */
    public boolean contains(String target){
        return collection.contains(target);
    }

    /**
     * clears out the dictionary
     */
    public void clear(){
        collection.clear();
    }


}
