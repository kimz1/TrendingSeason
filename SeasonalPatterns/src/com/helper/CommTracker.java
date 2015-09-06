package com.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.comm.Commodity;

public class CommTracker {

	/*
	 * Create a new instance of CommTracker. The run() method runs automatically
	 * when the instance is created the first time.
	 */

	private static final CommTracker TRACKER = new CommTracker();;
	private static CommContainer container = null;
	private static ScheduledExecutorService exec = null;

	static {
		container = CommContainer.getInstance();
		exec = Executors.newScheduledThreadPool(2);
	}
	
	private CommTracker() {}

	public static CommTracker getInstance() {
		return TRACKER;
	}

	public void run() {
		check();
		update();
	}

	public static void check() {

		exec.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {

				System.out.println("Inside check's Runnable...");
				XMLParser parser = new XMLParser(container);
				parser.worker();
			}
		}, 1, 10, TimeUnit.SECONDS);
	}

	public static void update() {

		exec.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {

				System.out.println("Inside update's Runnable...");
				SortedSet<Commodity> setToUpdate = updateLookup();
				if (setToUpdate != null && setToUpdate.size() != 0) {

					System.out.println("Set not null, updating commodities...");
					ExecutorService service = CommContainer.getNewThreadPool();
					if(service.isShutdown()) {
						System.out.println("Service is SHUTDOWN");
					}
					for (final Commodity comm : setToUpdate) {
						service.submit(new Runnable() {

							@Override
							public void run() {
								comm.updateCommodity();
							}
						});
					}
					try {
						service.awaitTermination(15, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					service.shutdown();
					if (service.isShutdown()) {
						System.out.println("Executor service down.");
					}
				} else {
					System.out.println("No updates necessary.");
				}
			}
		}, 2, 10, TimeUnit.SECONDS);
	}

	private static SortedSet<Commodity> updateLookup() {
		System.out.println("Inside updateLookup");
		TreeSet<Commodity> set = new TreeSet<>();

		for (Commodity comm : container.getSet()) {
			Calendar now = Calendar.getInstance();
			
			if (comm.getLastPossibleUpdate() > now.getTimeInMillis()
				&& comm.getNextUpdateTime() < now.getTimeInMillis()) {

				System.out.println("getLastUpdateTime() = " + new Date(comm.getLastUpdateTime()));
				System.out.println("getNextUpdateTime() = " + new Date(comm.getNextUpdateTime()));
				System.out.println("comm.hashcode() = " + comm.hashCode());
				set.add(comm);
			} else {
				System.out.println("No update match");
			}
		}
		return set;
	}

	@Override
	public String toString() {

		return "CommTracker";
	}
}