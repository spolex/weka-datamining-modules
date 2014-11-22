package pack.datamining.modules.filters;

import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;

public class Discretization 
{
	public static Instances getDiscretized (Instances inst, int intervalo, int pos) throws Exception
	{
	
		inst.setClassIndex(-1);
		Discretize filter;		
		filter = new Discretize();
	    filter.setInputFormat(inst);
	    filter.setOptions(weka.core.Utils.splitOptions("-B "+ intervalo +" -M -1.0 -R " + pos + "-"+ pos));

	
	    return 	    Filter.useFilter(inst, filter);

	}

}
