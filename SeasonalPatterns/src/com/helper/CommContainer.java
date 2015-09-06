/* A single container object is created, that stores all Commodity objects that
 * are created when the program is initialized. Every day is updated for new
 * objects to create.
 * 
 * An object represents a commodity with specified type, month (according to its specifications) and year.
 */

package com.helper;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.comm.Commodity;

public class CommContainer {

	private static SortedSet<Commodity> set = new TreeSet<>();
	private static final CommContainer CONT = new CommContainer();
	private static ExecutorService pool = null;
	private static final Object LOCK = new Object();

	static {
		pool = Executors.newFixedThreadPool(2);
	}
	
	private CommContainer() {}

	// Returns the actual container if there is one, otherwise returns a newly
	// created container.
	public static CommContainer getInstance() {
		return CommContainer.CONT;
	}

	// Adds a Commodity object to the container.
	public void add(Commodity comm) throws CommodityException {
		synchronized (LOCK) {
			if (comm == null) {
				throw new CommodityException("Commodity null");
			} else {
				CommContainer.set.add(comm);
			}
		}
	}

	// Returns the size of the container's Set object.
	public int getSize() {
		return CommContainer.set.size();
	}

	// Returns the ExecutorService object (Thread Pool).
	public static ExecutorService getNewThreadPool() {
		
		CommContainer.pool = Executors.newFixedThreadPool(2);
		return CommContainer.pool;
	}

	// Returns the containers Set object.
	public SortedSet<Commodity> getSet() {
		synchronized (LOCK) {
			return set;
		}
	}

	// Prints all objects in the container to the console.
	public void printAll() {
		if (set.isEmpty()) {
			System.out.println("Empty");
		} else {
			Iterator<Commodity> iter = set.iterator();
			while (iter.hasNext()) {
				Commodity comm = (Commodity) iter.next();
				System.out.println(comm.toString());
			}
		}
	}
}