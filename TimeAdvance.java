

import java.util.*;
public class TimeAdvance {
	public static int MAXTIME = 1000000;
	
	public static double time = 0;
	public static boolean serverBusy;
	public static int jobID = 1;
	public static PriorityQueue<Event> FEL = new PriorityQueue<Event>();
	public static LinkedList<Job> waitList= new LinkedList<Job>();
	
	public static Random rng = new Random(34);
	public static double MAX_SERVICE_TIME = 5;
	public static double MAX_INTERARRIVAL_TIME = 20;

	public static int maxQ = 0;
	public static int numWait = 0;
	public static double totalWaitTime = 0;
	public static double maxWaitTime = 0;
	public static double totalServiceTime = 0;
	public static double totalTimeInSystem = 0;
	public static double maxTimeInSystem = 0;
	
	private static double uniformRandomDouble(double max)
	{ return rng.nextDouble() * max; }
	
	public static double nextServiceTime()
	{ return uniformRandomDouble(MAX_SERVICE_TIME); }
	
	public static double nextInterarrivalTime()
	{ return uniformRandomDouble(MAX_INTERARRIVAL_TIME); }
	
	private static void DumpFEL()
	{
		PriorityQueue<Event> buffer = new PriorityQueue<Event>();
		
		System.out.printf("T=%f ", time);
		int cnt = FEL.size();
		for (int i=0; i<cnt; i++) {
			Event curr = FEL.remove();
			System.out.print(curr.toString() + " ");
			buffer.add(curr);
		}
		System.out.println();
		FEL = buffer;
	}
	
	public static void DumpWaitList()
	{
		System.out.printf("  Wait list: [");
		int cnt = waitList.size();
		for (int i=0; i<cnt; i++) {
			Job curr = waitList.get(i);
			System.out.print(curr.toString() + " ");
		}
		System.out.println("]");
	}
	
	public static void main(String[] args)
	{
		FEL.add(new Event(nextServiceTime(), Event.EventType.DEPARTURE));
		//FEL.add(new Event(nextInterarrivalTime(), Event.EventType.ARRIVAL));
		serverBusy = true;
//		DumpFEL();
//		DumpWaitList();

		while (time < MAXTIME) {
			Event event = FEL.remove();
			time = event.time;
			if (event.typeOfEvent == Event.EventType.ARRIVAL) {
				FEL.add(new Event(time + nextInterarrivalTime(), Event.EventType.ARRIVAL));
				jobID++;
				if (serverBusy) {
					waitList.add(new Job(jobID, time));
					numWait++;
					maxQ = Math.max(maxQ, waitList.size());
				} else {
					double waitTime = 0;
					double serviceTime = nextServiceTime();
					double timeInSystem = waitTime + serviceTime;
					maxTimeInSystem = Math.max(maxTimeInSystem, timeInSystem);
					totalTimeInSystem += timeInSystem;
					FEL.add(new Event(time + serviceTime, Event.EventType.DEPARTURE));
					serverBusy = true;
				}
			} else if (event.typeOfEvent == Event.EventType.DEPARTURE) {
				serverBusy = false;
				if (waitList.size() > 0) {
					Job job = waitList.removeFirst();
					double waitTime = time - job.time;
					double serviceTime = nextServiceTime();
					double timeInSystem = waitTime + serviceTime;
					maxWaitTime = Math.max(maxWaitTime, waitTime);
					totalWaitTime += waitTime;
					//maxTimeInSystem = Math.max(maxTimeInSystem, timeInSystem);
					totalTimeInSystem += timeInSystem;
					FEL.add(new Event(time + serviceTime, Event.EventType.DEPARTURE));
					serverBusy = true;					
				}
			}
//			DumpFEL();
//			DumpWaitList();
		}

		System.out.printf("Number of jobs: %d%n", jobID);
		System.out.printf("Max queue length: %d%n", maxQ);
		System.out.printf("Avg wait time: %f%n", totalWaitTime/jobID);
		System.out.printf("Avg wait time for those that wait: %f%n", totalWaitTime/numWait);
	//	System.out.printf("Max wait time: %f%n", maxWaitTime);
		System.out.printf("Percentage of jobs that wait: %f%n", numWait/(double)jobID);
		System.out.printf("Avg time in system: %f%n", totalTimeInSystem/jobID);
		System.out.printf("Max time in system: %f%n", maxTimeInSystem);
	}
}
