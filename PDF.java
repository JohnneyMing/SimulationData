import java.util.Random;

public class PDF { 
	public static Random rng = new Random();
	
	public int [] items;
	public double [] cdf;
	
	// P should sum to 1.0
	public PDF(int [] I, double [] P)
	{
		int Ilen = I.length;
		int Plen = P.length;
		
		assert(Ilen == Plen);
		
		items = I.clone();
		cdf = new double [Ilen];
		cdf[0] = P[0];
		for (int i=1; i<Plen; i++)
			cdf[i] = cdf[i-1] + P[i];
	}
	public int randomItem()
	{
		double roll = rng.nextDouble();
		int i = 0;
		while (i < cdf.length && roll > cdf[i]) {
			i++;
		}
		return items[i];
	}
	public double expectedItem()		// for testing purposes
	{
		final int N = 10000000;
		int sum = 0;
		for (int i=0; i<N; i++) {
			sum += randomItem();
		}
		return sum / (double)N;
	}
}
