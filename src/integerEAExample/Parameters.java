package integerEAExample;

public class Parameters {

	//can solve for n=100 with around 200k-1000k iterations depending on settings
	static int n = 100;
	static int maxIterations = 1000000;
	
	static int populationSize = 100;
	static double mutationRate = 0.038; //generally optimal for these proportions program based on testing
	static int tournamentSize = 10;
	static int crossoverMethod = 1;
	
	static long seed = -1; //set random number to default value to utilise clock millisecond timer if desired

	
}
