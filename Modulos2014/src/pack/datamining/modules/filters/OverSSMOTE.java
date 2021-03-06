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
	private static OverSSMOTE mOverSSMOTEr= new OverSSMOTE();
	private SMOTE mSMOTEr;
	public OverSSMOTE()
	{
			this.mSMOTEr = new SMOTE();
	}
	
	public Instances applySMOTE(Instances pInstances,double pPercentage, int pKNN) throws Exception
	{
		//Establezco los vecinos más próximos
		this.getmSMOTEr().setNearestNeighbors(pKNN);
		//Establezco el porcentaje de SMOTE
		this.getmSMOTEr().setPercentage(pPercentage);
		//Indico el formato de la entrada
		this.getmSMOTEr().setInputFormat(pInstances);
		
		//Ejecuto el filtro sobre los datos dados.
		return Filter.useFilter(pInstances, mSMOTEr);
		
	}

	public static OverSSMOTE getOverSSMOTE()
	{
		return mOverSSMOTEr;
	}
	
	private  SMOTE getmSMOTEr() {
		return mSMOTEr;
	}
}
