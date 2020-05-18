package integerEAExample;

import java.util.Random;

public class SolveProblem {
	
	int n;
	int population[][];
	int maxIterations;
	
	int populationSize;
	double mutationRate;
	int tournamentSize;
	int crossoverMethod;

	int[] fitnessTarget;
	
	
	Random randomNum;
	
	public SolveProblem() {
		this.n = Parameters.n;
		this.maxIterations = Parameters.maxIterations;
		this.populationSize = Parameters.populationSize;
		this.mutationRate = Parameters.mutationRate;
		this.tournamentSize = Parameters.tournamentSize;
		this.crossoverMethod = Parameters.crossoverMethod;
		this.fitnessTarget = new int[this.n];
		
		for(int i = 0; i < this.n; i++) {
			fitnessTarget[i] = i+1; //set target array
		}
		
		//this.randomNum = new Random(Parameters.seed); //will use clock millisecond as seed
		this.randomNum = new Random(); 
		
	}
	
	public void runEA() {
		initalise();
		int bestFitness = 0;
		int bestID = 0;
		int counter = 0;
		
		//run program until set max iterations
		for(int x = 0; x < this.maxIterations; x++) {
			int firstParentIndex, secondParentIndex;
			
			//*TOURNAMENT PARENT SELECTION*
			firstParentIndex = tournamentSelect();
			secondParentIndex = tournamentSelect();
			
			//*PROPORTIONATE FITNESS SELECT*
			//firstParentIndex = proportionateSelect();
			//secondParentIndex = proportionateSelect();
			
			int[] child;
			
			switch(this.crossoverMethod) { //determined by choice in Parameters.java
			case 0:
				child = uniformCrossover(firstParentIndex, secondParentIndex);
				break;
			case 1:
				child = onepointCrossover(firstParentIndex, secondParentIndex);
				break;
			case 2:
				child = twopointCrossover(firstParentIndex, secondParentIndex);
				break;
			default:
				child = uniformCrossover(firstParentIndex, secondParentIndex);
				break;
			}
			
			child = mutate(child); //mutate new child
			
			replaceWorstParent(child, evaluate(child), firstParentIndex, secondParentIndex);
			
			bestID = findBest();
			bestFitness = evaluate(this.population[bestID]);
			
			System.out.println("Iteration: " + x +  " best fitness: " + bestFitness + ", best member: " + memberToString(this.population[bestID]));
			if(bestFitness == this.n) {
				counter = x + 1; //note what iteration we are on
				break; //break if optimal solution has been found
			}
		}
		
		if(bestFitness == this.n) { //if optimal solution was found
			System.out.println("Solution found on iteration: " + counter);
		}else {
			System.out.println("Solution not found, best fitness: " + bestFitness);
		}
		
	}
	
	private void initalise() {
		this.population = new int[this.populationSize][this.n];
		for(int i = 0; i < this.populationSize; i++) { 
			for(int j = 0; j < this.n; j++) {
				//fill all 9 genes with random num from 1-9
				this.population[i][j] = this.randomNum.nextInt(this.n) + 1;
			}
		}
	}
	
	private int[] mutate(int[] child) {
		for(int i = 0; i < child.length; i++) {
			if(this.randomNum.nextDouble() < this.mutationRate) {
				child[i] = this.randomNum.nextInt(this.n) + 1;
			}
		}
		return child;
	}
	
	private int tournamentSelect() {
		//select n random members of population - determined by tournamentSize
		int tempID = this.randomNum.nextInt(this.populationSize);
		int winnerID = tempID;
		int tempFitness = evaluate(this.population[tempID]);
		int bestFitness = tempFitness;
		
		for(int x = 0; x < this.tournamentSize - 1; x++) {
			tempID = this.randomNum.nextInt(this.populationSize); //
			tempFitness = evaluate(this.population[tempID]);
			if(tempFitness > bestFitness) {
				bestFitness = tempFitness;
				winnerID = tempID;
			}
		}
		return winnerID;
	}
	
	private int proportionateSelect() {
		//selects parent based on random chance with higher fitness members more likely to succeed
		int totalFitness = 0, partialSum = 0, parentID = 0, chosenParent = 0;
		double cutOff = this.randomNum.nextDouble() * totalFitness;
		
		for(int i = 0; i < this.populationSize; i++) {
			totalFitness =+ evaluate(this.population[i]); 
		}
		
		while(cutOff > partialSum) {
			partialSum += evaluate(this.population[parentID]);
			parentID++;
		}
		
		chosenParent = parentID;
		
		return chosenParent;
	}
	
	private int[] onepointCrossover(int aFirstParentIndex, int aSecondParentIndex) {
		int[] child = new int[population[aFirstParentIndex].length];
		//pick one random index as a crosspoint
		int crossPoint = this.randomNum.nextInt(this.n);
		//copy parentOne into child up until crosspoint
		for(int i = 0; i < crossPoint; i++) {
			child[i] = population[aFirstParentIndex][i];
		}
		
		//copy parentTwo into child from crosspoint until end
		for(int i = crossPoint; i < this.n; i++) {
			child[i] = population[aSecondParentIndex][i];
		}
		return child;
	}
	
	
	private int[] twopointCrossover(int aFirstParentIndex, int aSecondParentIndex) {
		int[] child = new int[population[aFirstParentIndex].length];
		
		//choose two random cross over points
		int crossPoint1 = this.randomNum.nextInt(this.n);
		int crossPoint2 = this.randomNum.nextInt(this.n);
		
		if(crossPoint1 > crossPoint2) {
			int temp = crossPoint1;
			crossPoint1 = crossPoint2;
			crossPoint2 = temp;
		}
		
		//copy parentOne up to p1
		for(int i = 0; i < crossPoint1; i++) {
			child[i] = population[aFirstParentIndex][i];
		}
		
		//copy parentTwo from p1-p2
		for(int i = crossPoint1; i < crossPoint2; i++) {
			child[i] = population[aSecondParentIndex][i];
		}
		
		//copy parentOne from p2-end
		for(int i = crossPoint2; i < this.n; i++) {
			child[i] = population[aFirstParentIndex][i];
		}
		
		return child;
	}
	
	private int[] uniformCrossover(int aFirstParentIndex, int aSecondParentIndex) {
		int[] child = new int[population[aFirstParentIndex].length];
		for (int index = 0; index < population[aFirstParentIndex].length; index++) {
			if (randomNum.nextBoolean())
				child[index] = population[aFirstParentIndex][index];
			else
				child[index] = population[aSecondParentIndex][index];
		}
		return child;
	}
	
	private int evaluate(int[] populationMember) {
		int result = 0;
		
		for(int i = 0; i < this.n; i++) {
			if(populationMember[i] == this.fitnessTarget[i]) {
				//fitness increments for every matching index 
				result++;
			}
		}
		return result;
	}
	
	
	private void replaceWorstParent(int[] child, int childFitness, int parentOneID, int parentTwoID) {
		int worstFitness = evaluate(this.population[parentTwoID]);
		int worstID = parentTwoID;
		if(evaluate(this.population[parentOneID]) < worstFitness) {
			worstFitness = evaluate(this.population[parentOneID]);
			worstID = parentOneID;
		}
		
		//replace worst parent with child if child is fitter
		if(evaluate(child) > worstFitness) {
			for(int i = 0; i < this.n; i++) {
				this.population[worstID][i] = child[i];
			}
		}
			
	}
	
	public int findBest() {
		//start with assumption best is first element in pop
		int bestFitness = evaluate(this.population[0]);
		int bestID = 0;
		
		for(int i = 0; i < this.populationSize; i++) {
			int tempFitness = evaluate(this.population[i]);
			if(tempFitness > bestFitness){
				bestFitness = tempFitness;
				bestID = i;
			}
		}
		return bestID;
	}
	
	public String memberToString(int[] popMember) {
		String output = "";
		
		for(int x = 0; x < this.n; x++) {
			output += popMember[x];
			if(x < this.n-1) {
				output +=",";
			}
		}
		return output;
	}
	
	
	
	
}
