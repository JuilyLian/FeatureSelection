/*
 * Author : Christopher Henard (christopher.henard@uni.lu)
 * Date : 01/03/14
 * Copyright 2013 University of Luxembourg â€? Interdisciplinary Centre for Security Reliability and Trust (SnT)
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


import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import jmetal.operators.mutation.Mutation;


/**
 *
 * @author chris
 */
public class IVEAII_ReplaceMutation extends Mutation {

	
    private static Random r = new Random();
 //   private String fm;
    private int nFeat;
//    private List<List<Integer>> constraints;

    /**
     * Valid solution types to apply this operator
     */
    private static final List VALID_TYPES = Arrays.asList(BinarySolutionType.class,
            BinaryRealSolutionType.class,
            IntSolutionType.class, SATIBEA_BinarySolution.class);

    private Double mutationProbability_ = null;

//    private static final int SATtimeout = 1000;
//    private static final long iteratorTimeout = 150000;

    /**
     * Constructor Creates a new instance of the Bit Flip mutation operator
     */
    public IVEAII_ReplaceMutation(HashMap<String, Object> parameters, int nFeat) {
        super(parameters);
        if (parameters.get("probability") != null) {
            mutationProbability_ = (Double) parameters.get("probability");
        }

        this.nFeat = nFeat;
    }

    /**
     * Perform the mutation operation
     *
     * @param probability Mutation probability
     * @param solution The solution to mutate
     * @throws JMException
     */
    public void doMutation(double probability, Solution solution) throws JMException {

        Integer in = r.nextInt(50);

        if (in != 0) {

            try {
                if ((solution.getType().getClass() == BinarySolutionType.class)
                        || (solution.getType().getClass() == BinaryRealSolutionType.class) || solution.getType().getClass()==SATIBEA_BinarySolution.class) {
                    for (int i = 0; i < solution.getDecisionVariables().length; i++) {
                        for (Integer j : IVEAII_Problem.featureIndicesAllowedFlip) { //flip only not "fixed" features
                            if (PseudoRandom.randDouble() < probability) {
                                ((Binary) solution.getDecisionVariables()[i]).bits_.flip(j);
                            }
                        }
                    }

                    for (int i = 0; i < solution.getDecisionVariables().length; i++) {
                        ((Binary) solution.getDecisionVariables()[i]).decode();
                    }
                } // if
                else { // Integer representation
                    for (int i = 0; i < solution.getDecisionVariables().length; i++) {
                        if (PseudoRandom.randDouble() < probability) {
                            int value = PseudoRandom.randInt(
                                    (int) solution.getDecisionVariables()[i].getLowerBound(),
                                    (int) solution.getDecisionVariables()[i].getUpperBound());
                            solution.getDecisionVariables()[i].setValue(value);
                        } // if
                    }
                } // else
            } catch (ClassCastException e1) {
                Configuration.logger_.severe("BitFlipMutation.doMutation: "
                        + "ClassCastException error" + e1.getMessage());
                Class cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doMutation()");
            }

        } else {
        	
        	int violated = (int) solution.getObjective(0);
			 
			 if(violated > 0)
			 {
				 int seedSize = IVEAII_Problem.seeds.size();
		        	
				 Random random = new Random();
		
				 int seed_pos =  Math.abs(random.nextInt()) % seedSize;
				 
				 List<Integer> curSeed = IVEAII_Problem.seeds.get(seed_pos);
				 
				for(int n = 0; n < solution.getDecisionVariables().length; n++)
				 {
					 Binary bin = (Binary) solution.getDecisionVariables()[n];
					
					 for (Integer f : curSeed){
		                 boolean sign = f > 0;
		                 int index = f > 0? f :-f;
		                 index--;
		                 bin.setIth(index, sign);
		             }
				 }
			 }
        	
        }

    } // doMutation


    /**
     * Executes the operation
     *
     * @param object An object containing a solution to mutate
     * @return An object containing the mutated solution
     * @throws JMException
     */
    public Object execute(Object object) throws JMException {
        Solution solution = (Solution) object;

        if (!VALID_TYPES.contains(solution.getType().getClass())) {
            Configuration.logger_.severe("BitFlipMutation.execute: the solution "
                    + "is not of the right type. The type should be 'Binary', "
                    + "'BinaryReal' or 'Int', but " + solution.getType() + " is obtained");

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        } // if 

        doMutation(mutationProbability_, solution);
        return solution;
    } // execute

} // BitFlipMutation

