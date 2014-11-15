package pack.datamining.modules.filters;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.InterquartileRange;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.RemoveWithValues;

public class Outliers 
{
	/**
	 * Se utilizan los datos pasados por parámetros.
	 * Se indican las intancias con outliers y/o extreme values
	 * y se eliminan las mismas. Tambien se eliminian los atributos
	 * indicadores de outliers y extreme values.
	 * 
	 * 
	 * @param pData: el conjunto de instancias recibidas por 
	 * parámetros que van a ser filtradas.
	 * @return el conjunto de instancias sin outliers ni extreme values.
	 * @throws Exception
	 * 
	 */
	public static Instances getFilterInstancesWithoutOutliers(Instances pData) throws Exception
	{
		InterquartileRange inter= new InterquartileRange();
		//Añadimos los atributos outlier y extreme value al final de cada instancia
		inter.setOptions(weka.core.Utils.splitOptions("-R first-last -O 3.0 -E 63.0"));
		inter.setInputFormat(pData);
		Instances forFilterDataAltered = Filter.useFilter(pData, inter);
		RemoveWithValues rm = new RemoveWithValues();
		Instances data1 = null;
		Instances data2 =  null;
		//Guardamos en numero total de atributos de los datos(Los outliers y extreme
		//values se almacenan en las ultimas posiciones)
		int attributeNumber = forFilterDataAltered.numAttributes();
		rm.setInputFormat(forFilterDataAltered);
		//Trabaja con indices como en el GUI, empieza en 1, no en 0
		rm.setOptions(weka.core.Utils.splitOptions("-S 0.0 -C "+(attributeNumber)+" -L 2"));
		data1 = Filter.useFilter(forFilterDataAltered, rm);
		rm.setOptions(weka.core.Utils.splitOptions("-S 0.0 -C "+(attributeNumber - 1)+" -L 2"));
		data2 = Filter.useFilter(data1, rm);
		Remove rmAtt = new Remove();
		rmAtt.setInputFormat(data2);
		rmAtt.setOptions(weka.core.Utils.splitOptions("-R "+(data2.numAttributes()-1)+","+data2.numAttributes()));
		Instances newData = Filter.useFilter(data2, rmAtt);
		return newData;
	}
}
