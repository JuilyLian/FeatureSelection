package algos;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.comparators.DominanceComparator;

public class FMIBEA_Ranking {

	/**
	   * The <code>SolutionSet</code> to rank
	   */
	  private SolutionSet   solutionSet_ ;
	  
	  /**
	   * An array containing all the fronts found during the search
	   */
	  private SolutionSet[] ranking_  ;
	  
	  /**
	   * stores a <code>Comparator</code> for dominance checking
	   */
	  private static final Comparator dominance_ = new DominanceComparator();
	  
	  /**
	   * stores a <code>Comparator</code> for Overal Constraint Violation Comparator
	   * checking
	   */
	//  private static final Comparator constraint_ = new OverallConstraintViolationComparator();
	    private static final Comparator constraint_ = new ViolatedConstraintComparator();
	  /** 
	   * Constructor.
	   * @param solutionSet The <code>SolutionSet</code> to be ranked.
	   */       
	  public FMIBEA_Ranking(SolutionSet solutionSet) {        
	    solutionSet_ = solutionSet ;

	    // dominateMe[i] contains the number of solutions dominating i        
	    int [] dominateMe = new int[solutionSet_.size()];

	    // iDominate[k] contains the list of solutions dominated by k
	    List<Integer> [] iDominate = new List[solutionSet_.size()];

	    // front[i] contains the list of individuals belonging to the front i
	    List<Integer> [] front = new List[solutionSet_.size()+1];
	        
	    // flagDominate is an auxiliar encodings.variable
	    int flagDominate;    

	    // Initialize the fronts 
	    for (int i = 0; i < front.length; i++)
	      front[i] = new LinkedList<Integer>();
	        
	    /*
	    //-> Fast non dominated sorting algorithm
	    for (int p = 0; p < solutionSet_.size(); p++) {
	    // Initialice the list of individuals that i dominate and the number
	    // of individuals that dominate me
	      iDominate[p] = new LinkedList<Integer>();
	      dominateMe[p] = 0;            
	      // For all q individuals , calculate if p dominates q or vice versa
	      for (int q = 0; q < solutionSet_.size(); q++) {
	        flagDominate =constraint_.compare(solutionSet.get(p),solutionSet.get(q));
	        if (flagDominate == 0) {
	          flagDominate =dominance_.compare(solutionSet.get(p),solutionSet.get(q));                                
	        }
	        
	        if (flagDominate == -1) {
	          iDominate[p].add(new Integer(q));
	        } else if (flagDominate == 1) {
	                    dominateMe[p]++;   
	        }
	      }
	            
	      // If nobody dominates p, p belongs to the first front
	      if (dominateMe[p] == 0) {
	        front[0].add(new Integer(p));
	        solutionSet.get(p).setRank(0);
	      }            
	    }
	    */
	    
	    //-> Fast non dominated sorting algorithm
	    // Contribution of Guillaume Jacquenot
	    for (int p = 0; p < solutionSet_.size(); p++) {
	    // Initialize the list of individuals that i dominate and the number
	    // of individuals that dominate me
	      iDominate[p] = new LinkedList<Integer>();
	      dominateMe[p] = 0;
	    }
	    for (int p = 0; p < (solutionSet_.size()-1); p++) {
	      // For all q individuals , calculate if p dominates q or vice versa
	      for (int q = p+1; q < solutionSet_.size(); q++) {
	        flagDominate =constraint_.compare(solutionSet.get(p),solutionSet.get(q));
	        if (flagDominate == 0) {
	          flagDominate =dominance_.compare(solutionSet.get(p),solutionSet.get(q));
	        }
	        if (flagDominate == -1)
	        {
	          iDominate[p].add(q);
	          dominateMe[q]++;
	        }
	        else if (flagDominate == 1)
	        {
	          iDominate[q].add(p);
	          dominateMe[p]++;
	        }
	      }
	      // If nobody dominates p, p belongs to the first front
	    }
	    for (int p = 0; p < solutionSet_.size(); p++) {
	      if (dominateMe[p] == 0) {
	        front[0].add(p);
	        solutionSet.get(p).setRank(0);
	      }
	    }    
	    
	    //Obtain the rest of fronts
	    int i = 0;
	    Iterator<Integer> it1, it2 ; // Iterators
	    while (front[i].size()!= 0) {
	      i++;
	      it1 = front[i-1].iterator();
	      while (it1.hasNext()) {
	        it2 = iDominate[it1.next()].iterator();
	        while (it2.hasNext()) {
	          int index = it2.next();
	          dominateMe[index]--;
	          if (dominateMe[index]==0) {
	            front[i].add(index);
	            solutionSet_.get(index).setRank(i);
	          }
	        }
	      }
	    }
	    //<-
	        
	    ranking_ = new SolutionSet[i];
	    //0,1,2,....,i-1 are front, then i fronts
	    for (int j = 0; j < i; j++) {
	      ranking_[j] = new SolutionSet(front[j].size());
	      it1 = front[j].iterator();
	      while (it1.hasNext()) {
	                ranking_[j].add(solutionSet.get(it1.next()));
	      }
	    }
	    
	  } // Ranking

	  /**
	   * Returns a <code>SolutionSet</code> containing the solutions of a given rank. 
	   * @param rank The rank
	   * @return Object representing the <code>SolutionSet</code>.
	   */
	  public SolutionSet getSubfront(int rank, int populationSize) {
		  SolutionSet subFront = ranking_[rank];
		  SolutionSet returnSet = null;
		  if(subFront.size() > populationSize)
		  {
			  returnSet = deleteReplicate(subFront,populationSize);
		  }
	    //return ranking_[rank];
		  return returnSet == null? subFront: returnSet;
	  } // getSubFront

	  private SolutionSet deleteReplicate(SolutionSet original, int populationSize)
	  {
		  SolutionSet returnSet = original;
		  int setSize = returnSet.size();
		  
		  for(int i = 0; i< returnSet.size()&& setSize > populationSize; i++)
		  {
			  Solution solution_i = returnSet.get(i);
			  for(int j = i + 1; j < returnSet.size(); j++)
			  {
				  Solution solution_j = returnSet.get(j);
				  if(solution_j.equals(solution_j))
				  {
					  returnSet.remove(j);
					  setSize--;
				  }
			  }
		  }
		  return returnSet;
	  }
	  /** 
	  * Returns the total number of subFronts founds.
	  */
	  public int getNumberOfSubfronts() {
	    return ranking_.length;
	  } // getNumberOfSubfronts
}
