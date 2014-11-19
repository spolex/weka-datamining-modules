package pack.datamining.modules.temp;

import javax.swing.JDialog;
import javax.swing.JLabel;

import pack.datamining.modules.frames.ProgressDialog;

public class prueba {

	public static void main(String[] args) {
		JDialog parent = new JDialog();
	    parent.setSize(500, 150);
	    JLabel jl = new JLabel();
	    
	    
		final ProgressDialog dlg = new ProgressDialog(parent, 100, "Título");
		Thread t = new Thread(new Runnable() {
			 public void run() {
		        dlg.show();
			 }
		});
		t.start();
		for (int i = 0; i <= 500; i++) {
			jl.setText("Contando : " + i);
			dlg.processElement(prueba.class.getSimpleName().toString());
			try 
			{
				Thread.sleep(25);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();

			}
		}
		try 
		{
			Thread.sleep(5);
			dlg.close();
			parent.dispose();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}		  
}

