
public class Event implements Comparable<Event>
{
	public enum EventType { ARRIVAL, DEPARTURE, EXIT };
	public EventType typeOfEvent;
	public double time;
	public Event(double time, EventType type)
	{
		typeOfEvent = type;
		this.time = time;
	}
// What would the compareTo look like if you wanted to give preference to earlier jobs, but 
// prefer DEPARTUREs in the event of a tie?
	@Override
	public int compareTo(Event other)
	{
		int result;
		if (time < other.time) {
			result = -1;
		} else if (time > other.time) {
			result = +1;
		} else {
			result = 0;
		}
		return result;
	}
	public String toString()
	{ return String.format("(%s,%f)", typeOfEvent, time); }
}
