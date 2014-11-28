package pack.datamining.modules.filters;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;

/**
 * Clase MAE para la aplicación de SMOTE. 
 * @author Andoni
 *
 */
public class OverSSMOTE {
	private static SMOTE mSMOTEr= new SMOTE();
	
	public OverSSMOTE()
	{

	}
	
	public void applySMOTE(Instances pInstances,double pPercentage, int pKNN) throws Exception
	{
		//Establezco los vecinos más próximos
		mSMOTEr.setNearestNeighbors(pKNN);
		//Establezco el porcentaje de SMOTE
		mSMOTEr.setPercentage(pPercentage);
		//Indico el formato de la entrada
		mSMOTEr.setInputFormat(pInstances);
		
		//Ejecuto el filtro sobre los datos dados.
		Filter.useFilter(pInstances, mSMOTEr);
	}

	public static SMOTE getmSMOTEr() {
		return mSMOTEr;
	}
}
