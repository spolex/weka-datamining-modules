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
		Instances train, dev, test;
		
		train = null;
		dev = null;
		test = null;
		
		Instances[] out;
		
		if(args.length == 1)
		{
			train = LoaderSaver.getMyLoader().loadArff(args[0]);
			
		}
		
		if(args.length == 2)
		{
			train = LoaderSaver.getMyLoader().loadArff(args[0]);
			dev = LoaderSaver.getMyLoader().loadArff(args[1]);
		}
		
		if(args.length == 3)
		{
			train = LoaderSaver.getMyLoader().loadArff(args[0]);
			dev = LoaderSaver.getMyLoader().loadArff(args[1]);
			test =  LoaderSaver.getMyLoader().loadArff(args[2]);
		}
		
		train.setClassIndex(train.numAttributes()-1);
		try 
		{
			out = AttributeSelect.getFilteredData(train, dev, test);
			if (train != null)
				LoaderSaver.getMyLoader().saveInstances(out[0], args[0]+ "attSel.arff");
			if (dev != null)
				LoaderSaver.getMyLoader().saveInstances(out[1], args[1]+ "attSel.arff");
			if (test != null)
				LoaderSaver.getMyLoader().saveInstances(out[2], args[2]+ "attSel.arff");
		} 	
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
