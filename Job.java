public class Job {
	public int jobID;
	public double time;
	public Job(int id, double t)
	{
		jobID = id;
		time = t;
	}
	public String toString()
	{
		return String.format("(%d,%f)", jobID, time);
	}
}
