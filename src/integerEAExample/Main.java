package integerEAExample;

import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		SolveProblem myEA = new SolveProblem();
		
		long startTime = System.nanoTime();
		myEA.runEA();
		
		long endTime = System.nanoTime();
		long durationInMs = TimeUnit.NANOSECONDS.toMillis(endTime-startTime);
		System.out.println("Time taken: "+ durationInMs + " ms"); 
	}

}
