package pack.datamining.modules.filters;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;

public class Balance
{
	/**
	 * Recibe las instancias desbalanceadas(más de una clase que de las demas).
	 * Es necesario conocer que atributo es la clase.
	 * 
	 * @param pData
	 * @return Instances: devuelve las instancias balanceadas (cantidades similares de cada clase).
	 * @throws Exception
	 *  
	 */
	public static Instances getBalancedInstances(Instances pData) throws Exception{
		Resample resam = new Resample();
		resam.setOptions(weka.core.Utils.splitOptions("-B 1.0 -S 1 -Z 100.0"));
		resam.setInputFormat(pData);
		return Filter.useFilter(pData, resam);
	}
	
}
