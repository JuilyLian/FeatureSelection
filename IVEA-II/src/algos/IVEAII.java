/*
 * Author : Christopher Henard (christopher.henard@uni.lu)
 * Date : 01/03/14
 * Copyright 2013 University of Luxembourg 鈥� Interdisciplinary Centre for Security Reliability and Trust (SnT)
 * All rights reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package algos;

import jmetal.core.*;
import jmetal.encodings.variable.Binary;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingDistanceComparator;
import jmetal.util.comparators.DominanceComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


/**
 *
 * @author xiaoli lian
 */
public class IVEAII extends Algorithm {

    private int print_time = 0;

    private long maxRunTimeMS;

    /**
     * Defines the number of tournaments for creating the mating pool
     */
    public static final int TOURNAMENTS_ROUNDS = 1;

    /**
     * Stores the value of the indicator between each pair of solutions into the
     * solution set
     */
    private List<List<Double>> indicatorValues_;

    /**
     *
     */
    private double maxIndicatorValue_;

    private int maxViolations;
    /**
     * Constructor. Create a new IBEA instance
     *
     * @param problem Problem to solve
     */
    public IVEAII(Problem problem, long maxRunTimeMS) {
        super(problem);
        this.maxRunTimeMS = maxRunTimeMS;
    } // Spea2

    /**
     * calculates the hypervolume of that portion of the objective space that is
     * dominated by individual a but not by individual b
     */
    double calcHypervolumeIndicator(Solution p_ind_a,
            Solution p_ind_b,
            int d,
            double maximumValues[],
            double minimumValues[]) {
        double a, b, r, max;
        double volume = 0;
        double rho = 2.0;

        r = rho * (maximumValues[d - 1] - minimumValues[d - 1]);
        max = minimumValues[d - 1] + r;

        a = p_ind_a.getObjective(d - 1);
        if (p_ind_b == null) {
            b = max;
        } else {
            b = p_ind_b.getObjective(d - 1);
        }

        if (d == 1) {
            if (a < b) {
                volume = (b - a) / r;
            } else {
                volume = 0;
            }
        } else {
            if (a < b) {
                volume = calcHypervolumeIndicator(p_ind_a, null, d - 1, maximumValues, minimumValues)
                        * (b - a) / r;
                volume += calcHypervolumeIndicator(p_ind_a, p_ind_b, d - 1, maximumValues, minimumValues)
                        * (max - b) / r;
            } else {
                volume = calcHypervolumeIndicator(p_ind_a, p_ind_b, d - 1, maximumValues, minimumValues)
                        * (max - b) / r;
            }
        }

        return (volume);
    }

    /**
     * This structure store the indicator values of each pair of elements
     */
    public void computeIndicatorValuesHD(SolutionSet solutionSet,
            double[] maximumValues,
            double[] minimumValues) {
        SolutionSet A, B;
        // Initialize the structures
        indicatorValues_ = new ArrayList<List<Double>>();
        maxIndicatorValue_ = -Double.MAX_VALUE;

        for (int j = 0; j < solutionSet.size(); j++) {
            A = new SolutionSet(1);
            A.add(solutionSet.get(j));

            List<Double> aux = new ArrayList<Double>();
            for (int i = 0; i < solutionSet.size(); i++) {
                B = new SolutionSet(1);
                B.add(solutionSet.get(i));

                int flag = (new DominanceComparator()).compare(A.get(0), B.get(0));

                double value = 0.0;
                if (flag == -1) {
                    value = -calcHypervolumeIndicator(A.get(0), B.get(0), problem_.getNumberOfObjectives(), maximumValues, minimumValues);
                } else {
                    value = calcHypervolumeIndicator(B.get(0), A.get(0), problem_.getNumberOfObjectives(), maximumValues, minimumValues);
                }
                 //double value = epsilon.epsilon(matrixA,matrixB,problem_.getNumberOfObjectives());

                //Update the max value of the indicator
                if (Math.abs(value) > maxIndicatorValue_) {
                    maxIndicatorValue_ = Math.abs(value);
                }
                aux.add(value);
            }
            indicatorValues_.add(aux);
        }
    } // computeIndicatorValues

    /**
     * Calculate the fitness for the individual at position pos
     */
    public void fitness(SolutionSet solutionSet, int pos) {
        double fitness = 0.0;
        double kappa = 0.05;

        for (int i = 0; i < solutionSet.size(); i++) {
            if (i != pos) {
                fitness += Math.exp((-1 * indicatorValues_.get(i).get(pos) / maxIndicatorValue_) / kappa);
            }
        }

    
        solutionSet.get(pos).setFitness(fitness);

    }

    /**
     * @author xlian
     * Calculate the second dimension of fitness
     * @param solutionSet
     * @return
     */
    private double computeInfeasiblity(SolutionSet solutionSet){
		  double infeasibility = 0.0;
		  for(int i = 0; i < solutionSet.size(); i++){
			  infeasibility += solutionSet.get(i).getObjective(0);
		  }
		  return infeasibility;
	  }
    
    /**@author xlian
     * remove the solution with more violations than threshold
     * @param solutionSet
     * @param threshold
     * @return
     */
    public boolean removeByViolations(SolutionSet solutionSet,double threshold){
    	double kappa = 0.05;
  	  
  	  int worstIndex = solutionSet.size() - 1;
  	  
  	  //in the framework of SATIBEA, the objective(0) is the rule violations
  	  int worstViolations = (int) solutionSet.get(worstIndex).getObjective(0);
  	  if(worstViolations < threshold)
  		  return false;
  	  
  	  //store the solution indexs with the worst violations
  	  List<Integer> worstIndexList = new ArrayList<Integer>();

  	  for(int j = solutionSet.size() - 2; j >= 0; j--)
  	  {
  		  int curViolation = (int)solutionSet.get(j).getObjective(0);
  		  if(curViolation == worstViolations)
  		  {
  			  worstIndexList.add(j);
  		  }
  		  else if(curViolation < worstViolations)
  		  {
  			  break;
  		  }
  	  }
  	  
  	  //find the solutions with least crownding distance from worstIndexList
  	  double worstDistance = solutionSet.get(worstIndex).getCrowdingDistance();
  	  for(int nIndex = 1; nIndex < worstIndexList.size(); nIndex++)
  	  {
  		  int curIndex = worstIndexList.get(nIndex);
  		  double curDistance = solutionSet.get(curIndex).getCrowdingDistance();
  		  if(curDistance < worstDistance)
  		  {
  			  worstIndex = curIndex;
  		  }
  	  }
  	  
  	  //update the population
  	  for(int i = 0; i < worstIndex; i++){  		
          double fitness = solutionSet.get(i).getFitness();
          fitness -= Math.exp((- indicatorValues_.get(worstIndex).get(i)/maxIndicatorValue_) / kappa);
          solutionSet.get(i).setFitness(fitness);
  	    
  	  }
  	  
  	// remove worst from the indicatorValues list
	    indicatorValues_.remove(worstIndex); // Remove its own list
	    Iterator<List<Double>> it = indicatorValues_.iterator();
	    while (it.hasNext())
	        it.next().remove(worstIndex);

	    // remove the worst individual from the population
	    solutionSet.remove(worstIndex);  
	    return true;
    }//remove the worst violations
    
//    public boolean removeByViolations(SolutionSet solutionSet,double threshold){
//      double kappa = 0.05;
//  	  int worstIndex = 0;
//  	  
//  	  //in the framework of SATIBEA, the objective(0) is the rule violations
//  	  double worstViolations = solutionSet.get(worstIndex).getObjective(0);
////  	  if(worstViolations < threshold)
////  		  return false;
//  	  for(int i = 1; i < solutionSet.size(); i++)
//  	  {
//  		  double violation = solutionSet.get(i).getObjective(0);
//  		  if(violation > worstViolations)
//  		  {
//  			  worstIndex = i;
//  			  worstViolations = violation;
//  		  }
//  	  }
//  	  
//  	  //update the population
//  	  for (int i = 0; i < solutionSet.size(); i++) {
//          if (i != worstIndex) {
//              double fitness = solutionSet.get(i).getFitness();
//              fitness -= Math.exp((-indicatorValues_.get(worstIndex).get(i) / maxIndicatorValue_) / kappa);
//              solutionSet.get(i).setFitness(fitness);
//          }
//      }
//  	  
//  	// remove worst from the indicatorValues list
//	    indicatorValues_.remove(worstIndex); // Remove its own list
//	    Iterator<List<Double>> it = indicatorValues_.iterator();
//	    while (it.hasNext())
//	        it.next().remove(worstIndex);
//
//	    // remove the worst individual from the population
//	    solutionSet.remove(worstIndex);  
//	    return true;
//    }
    
    /**
     * Calculate the fitness for the entire population.
     *
     */
    public void calculateFitness(SolutionSet solutionSet) {
        // Obtains the lower and upper bounds of the population
        double[] maximumValues = new double[problem_.getNumberOfObjectives()];
        double[] minimumValues = new double[problem_.getNumberOfObjectives()];

        // The first objective is as my fitness function
        for (int i = 1; i < problem_.getNumberOfObjectives(); i++) {
            maximumValues[i] = -Double.MAX_VALUE; // i.e., the minus maxium value
            minimumValues[i] = Double.MAX_VALUE; // i.e., the maximum value
        }

        for (int pos = 0; pos < solutionSet.size(); pos++) {
        	// The first objective is as my fitness function
            for (int obj = 0; obj < problem_.getNumberOfObjectives(); obj++) {
                double value = solutionSet.get(pos).getObjective(obj);
                if (value > maximumValues[obj]) {
                    maximumValues[obj] = value;
                }
                if (value < minimumValues[obj]) {
                    minimumValues[obj] = value;
                }
            }
        }

        computeIndicatorValuesHD(solutionSet, maximumValues, minimumValues);
        for (int pos = 0; pos < solutionSet.size(); pos++) {
            fitness(solutionSet, pos);
        }
    }

    /**
     * @author xlian
     * 
     * Update the fitness before removing an individual, to add the distance impact
     */
    public void removeWorst(SolutionSet solutionSet) {

        // Find the worst;
        double worst = solutionSet.get(0).getFitness();
        int worstIndex = 0;
        double kappa = 0.05;

     
        for (int i = 1; i < solutionSet.size(); i++) {
            if (solutionSet.get(i).getFitness() > worst) {
                worst = solutionSet.get(i).getFitness();
                worstIndex = i;
            }
        }

        //store the solution indexs with the worst violations
    	  List<Integer> worstIndexList = new ArrayList<Integer>();
    	//  worstIndexList.add(worstIndex);
    	  for(int j = 1; j < solutionSet.size(); j++)
    	  {
    		  int curViolation = (int)solutionSet.get(j).getFitness();
    		  if(curViolation == worst)
    		  {
    			  worstIndexList.add(j);
    		  }
    	  }
    	  
    	  //find the solutions with least crownding distance from worstIndexList
    	  double worstDistance = solutionSet.get(worstIndex).getCrowdingDistance();
    	  for(int nIndex = 1; nIndex < worstIndexList.size(); nIndex++)
    	  {
    		  int curIndex = worstIndexList.get(nIndex);
    		  double curDistance = solutionSet.get(curIndex).getCrowdingDistance();
    		  if(curDistance < worstDistance)
    		  {
    			  worstIndex = curIndex;
    		  }
    	  }
    	  
        // Update the population
        for (int i = 0; i < solutionSet.size(); i++) {
            if (i != worstIndex) {
                double fitness = solutionSet.get(i).getFitness();
                fitness -= Math.exp((-indicatorValues_.get(worstIndex).get(i) / maxIndicatorValue_) / kappa);
                solutionSet.get(i).setFitness(fitness);
            }
        }

        // remove worst from the indicatorValues list
        indicatorValues_.remove(worstIndex); // Remove its own list
        Iterator<List<Double>> it = indicatorValues_.iterator();
        while (it.hasNext()) {
            it.next().remove(worstIndex);
        }

        solutionSet.remove(worstIndex);
    } // removeWorst

    private boolean isEqual(Solution A, Solution B)
    {
       String var_A = A.getDecisionVariables()[0].toString();
    	String var_B = B.getDecisionVariables()[0].toString();
    	if(var_A.equals(var_B))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    /**
     * calculate the crowding distance for the entire population
     * @param solutionSet
     */
    private void calculateCrowdingDistance(SolutionSet solutionSet)
    {
    	Distance distance  = new Distance();
    	distance.crowdingDistanceAssignment(solutionSet, problem_.getNumberOfObjectives());
    }
    
    
    /**
     * Runs of the IBEA algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated
     * solutions as a result of the algorithm execution
     * @throws JMException
     */
    public SolutionSet execute() throws JMException, ClassNotFoundException {

        long elapsed = 0, start = System.currentTimeMillis();

        int populationSize, archiveSize, maxEvaluations, evaluations;
        Operator crossoverOperator, mutationOperator, selectionOperator;
        SolutionSet solutionSet, archive, offSpringSolutionSet, validSolutionSet; //validSolutionSet collects the solution satisfying the feature model totally

        //Read the params
        populationSize = ((Integer) getInputParameter("populationSize")).intValue();
        archiveSize = ((Integer) getInputParameter("archiveSize")).intValue();
        maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();

        //Read the operators
        crossoverOperator = operators_.get("crossover");
        mutationOperator = operators_.get("mutation");
        selectionOperator = operators_.get("selection");

//        int validSize = 2 * populationSize;
        
        //Initialize the variables
        solutionSet = new SolutionSet(populationSize);
        archive = new SolutionSet(archiveSize);
        validSolutionSet = new SolutionSet(populationSize);
        evaluations = 0;

        //-> Create the initial solutionSet
        Solution newSolution;
       
        for (int i = 0; i < populationSize; i++) {
            newSolution = new Solution(problem_);
            problem_.evaluate(newSolution);
            problem_.evaluateConstraints(newSolution);
            evaluations++;
            solutionSet.add(newSolution);
        }
        Solution seed = solutionSet.get(0);
        validSolutionSet.add(seed);

        System.out.println("initial solution!");
    	while(elapsed < this.maxRunTimeMS ) {
	        SolutionSet union = ((SolutionSet) solutionSet).union(archive);
	        calculateCrowdingDistance(union);
	        calculateFitness(union);
	        archive = union;
	        
	        int unionSize = archive.size();
	        archive.sort(new ViolatedConstraintComparator());
	        double maxViolation = archive.get(unionSize-1).getObjective(0);
	        
	        if(maxViolation >= 1)
	        {
	        	int allViolation = problem_.getNumberOfConstraints();
	        	
	  	        double infeasiblity = computeInfeasiblity(archive)/ unionSize;
	  	        
	  	        double IFR = (maxViolation == 0)? 1: maxViolation/allViolation;
	  	        
	        	double threshold;
	        	if(maxEvaluations > evaluations)
	        	{
	        		threshold = ((maxEvaluations - evaluations)*infeasiblity)/maxEvaluations *IFR;
	        	}
	        	else
	        	{
	        		threshold = infeasiblity *IFR;
	        	}
	  		       
		        //delete by violations before delete by fitness        
	        	boolean isRemove = true;
	        	
	    		while(archive.size() > populationSize && isRemove){
		  	    	isRemove = removeByViolations(archive, threshold);
		  	    }
	        	
	        }
	   	 
	        System.out.println();
	        while (archive.size() > populationSize) {
	            removeWorst(archive);
	        }
	      
	        // Create a new offspringPopulation
	        offSpringSolutionSet = new SolutionSet(populationSize);
	        Solution[] parents = new Solution[2];
	        while (offSpringSolutionSet.size() < populationSize) {
	            int j = 0;
	            do {
	                j++;
	                parents[0] = (Solution) selectionOperator.execute(archive);
	            } while (j < IVEAII.TOURNAMENTS_ROUNDS); // do-while
	            int k = 0;
	            do {
	                k++;
	                parents[1] = (Solution) selectionOperator.execute(archive);
	            } while (k < IVEAII.TOURNAMENTS_ROUNDS); // do-while
	
	          
	            //make the crossover
	            Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
	            mutationOperator.execute(offSpring[0]);
	            problem_.evaluate(offSpring[0]);
	            problem_.evaluateConstraints(offSpring[0]);
	            offSpringSolutionSet.add(offSpring[0]);
	            evaluations++;
	            
	            
	            //�?�?�以�?�?
	            if(offSpring[0].getObjective(0) == 0)
	            {
            	 boolean flag = false;
                 for(int m = 0; m < validSolutionSet.size(); m++)
        		{
        			Solution curFeasible = validSolutionSet.get(m);
        		
        			if(isEqual(offSpring[0],curFeasible))
        			{
        				flag = true;
        				break;
    			    }
        		}
        		if(!flag) // && validSolutionSet.size() < validSize
        		{
        			if(validSolutionSet.size() < populationSize)
        			{
        				validSolutionSet.add(offSpring[0]);
        			}
        			else
        			{
        				//replace the dominated solutions
        				for(int l = 0; l < validSolutionSet.size(); l++)
        				{
        					Solution curValidSolution = validSolutionSet.get(l);
        					Comparator dominance_ = new DominanceComparator();
        					int result = dominance_.compare(offSpring[0],curValidSolution);
        					if(result == 1){
						       break;
							}
        					else if(result == -1)
        					{
        						validSolutionSet.replace(l, offSpring[0]);
        						break;
        					}
        				}
        			}
        		}
	            }
	        } // while
	        // End Create a offSpring solutionSet
	        solutionSet = offSpringSolutionSet;
	        
	        elapsed = System.currentTimeMillis() - start;

        } // while

        
        System.out.println("Elapsed: " + elapsed);
        System.out.println("RunTimeMS: " + this.maxRunTimeMS);
        System.out.println("Evaluations: " + evaluations);

        return validSolutionSet;
    } // execute
 } // IBEA
