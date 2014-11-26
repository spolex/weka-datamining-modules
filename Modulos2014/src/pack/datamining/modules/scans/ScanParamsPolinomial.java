package pack.datamining.modules.scans;

import weka.classifiers.functions.LibSVM;
import weka.core.Instances;

public class ScanParamsPolinomial extends ScanParamsRBFSvmAlgoritm {

	public ScanParamsPolinomial(Instances pTrainData, Instances pDevData) {
		super(pTrainData, pDevData);
	}
	
	@Override
	protected void configureModel(int pCmax, int pGMax) {		
	}
	
	@Override
	protected LibSVM extractEvaluatedModel(double cost, double gamma) {
		// TODO Auto-generated method stub
		return super.extractEvaluatedModel(cost, gamma);
	}
	
	@Override
	public LibSVM scanParams(int pCmax, int pGMax) {
		return super.scanParams(pCmax, pGMax);
	}
	
}
