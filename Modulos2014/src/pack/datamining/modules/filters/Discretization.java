package pack.datamining.modules.filters;

import weka.core.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;

public class Discretization 
{
	
	/**
	 * 
	 * @param inst Datos a discretizar
	 * @param intervalo Numero maximo de intervalos a crear
	 * @param pos Atributo a discretizar
	 * @return Datos discretizados
	 * @throws Exception
	 */
	public static Instances getDiscretized (Instances inst, int intervalo, int pos) throws Exception
	{
	
		inst.setClassIndex(-1);
		Discretize filter;		
		filter = new Discretize();
	    filter.setInputFormat(inst);
	    filter.setOptions(weka.core.Utils.splitOptions("-F -B "+ intervalo +" -M -1.0 -R " + pos + "-"+ pos));

	
	    return 	    Filter.useFilter(inst, filter);

	}

}
