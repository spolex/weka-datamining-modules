package pack.datamining.modules.scans;

import weka.core.Instances;

public class ScanParamsPolinomial extends ScanParamsRBFSvmAlgoritm {

	public ScanParamsPolinomial(Instances pTrainData, Instances pDevData) {
		super(pTrainData, pDevData);
	}
	
	@Override
	protected void configureModel(int pCmax, int pGMax) {
		
		
	}
}
