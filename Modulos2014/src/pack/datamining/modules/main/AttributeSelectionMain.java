package pack.datamining.modules.main;

import pack.datamining.modules.filters.AttributeSelect;
import pack.datamining.modules.io.LoaderSaver;
import weka.core.Instances;

public class AttributeSelectionMain {
	/**
	 * 
	 * @param args
	 * IMPORTANTE, SE ASUME QUE LA CLASE ES EL ATRIBUTO 
	 * DE LA ULTIMA POSICIÓN
	 */
	public static void main(String[] args) 
	{
		Instances data = LoaderSaver.getMyLoader().loadArff(args[0]);
		Instances newData = null;
		data.setClassIndex(data.numAttributes()-1);
		try {
			newData = AttributeSelect.getAttributeSelection(data);
			LoaderSaver.getMyLoader().saveInstances(newData, args[0]+ "attSel.arff");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
