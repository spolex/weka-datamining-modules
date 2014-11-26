==================================================================================================================
Asignatura: Mineria de datos.
N�mero de Pr�ctica: 5.
Autores: Jose Ignacio S�nchez.
Fecha: 26 de noviembre.
==================================================================================================================
Recursos utilizados:
	- Linux Mint 16, 17 con JDK 7 y eclipse Kepler.
	- Librer�a de Weka 3.7.11.
	- Librer�a de LibSVM 3.17.

* Es posible que d� errores a la hora de ejecutar el ejecutable .jar en Windows 7.
==================================================================================================================
Objetivos:

El objetivo principal del sistema es optimizar los par�metros de un modelo basado en las m�quinas de soporte
vectorial (SVM), utilizando un barrido de par�metros ad-hoc observando como figura de m�rito comparativa la
F-Measure.Como funcionalidad adicional cabe realizar el barrido gridsearch para los mismos par�metros y el 
mismo clasificador.

Tras entrenar el modelo (c-SVC), se realiza la predicci�n del conjunto de test de ser pedida.

Adem�s, es posible obtener lo resultados del modelo baseline, consitente en un clasificador OneR y obtener
los mismos resultados que con el modelo principal a traves de un barrido ad-hoc y un barrido implementado
con la librer�a CVPARAMETERS.Con ambos se obtiene la mejor B, con diferentes resultados.

Funcionalidades adicionales implementadas (m�s informaci�n en la Documentaci�n):
	- M�todo no-honesto.
	- Pseudo-GridSearch.
	- Preproceso de los datos configurable.

Nota: el balanceo de instancias est� desactivado por defecto para el algoritmo svm (filtro resample), no as� para
el baseline.
==================================================================================================================
Argumentos:
	- Fichero .arff de entrenamiento.
	- (Opcional) Fichero .arff de test.
	- (Opcional) Opciones a considerar, no es obligatorio escribir todas (se pueden escribir seguidas de
	  la siguiente manera: DBROG, sin gui�n.El orden no es indiferente):
		D: Activar m�todo deshonesto, desactivando 5FCV.
		B: Activa el baseline, usando OneR.
		R: Activar el filtro de resample para svm y lo desactiva para OneR.
		O: Desactivar el filtro de valores extremos y outliers.
		G: Se activa la optimizaci�n de par�metros con GridSearch.
	- Ejemplo: java -jar ScanParamsAlgorithm.jar ScatteringNeutrons_Train.arff ScatteringNeutrons_Test.arff DBRO
==================================================================================================================
Precondiciones:
	- La clase en los ficheros .arff debe encontrarse en �ltima posici�n.
	- Las opciones a considerar tienen que estar en may�sculas.

Postcondiciones:
	- Se obtiene un fichero de texto con los mejores resultados del modelo
	  obtenidos en el barrido ad-hoc, guard�ndose en la misma ruta que los ficheros 
	  de datos introducidos y se muestra por consola el mejor de los resultados.
	  El fichero de las predicciones de la clase para cada instancia del 
	  conjunto de test, tambi�n se obtiene como salida en el mismo path.
		- Si s�lo se ha usado un fichero de entrenamiento, el nombre del fichero resultante con los
	  	  resultados ser�: nombreDelFicheroEntrenamiento_Estimado[SVM|OneR]Fecha-Hora.txt.
		- Si se ha pasado a su vez, un fichero de entrenamiento, el nombre del fichero con los resultados
		  ser�: nombreDelFicheroTest_Estimado[SVM|OneR]Fecha-Hora.txt.
==================================================================================================================
Ejemplo de ejecuci�n en GNU/Linux:

Ejecuci�n del programa mediante la l�nea de comandos:

1. Nos situamos en el directorio del workspace descomprimido, lugar donde est� situado el ejecutable en el
subdirectorio bin, SCPA.jar, supondremos que el workspace en est� en nuestro directorio personal:

		$cd /home/usuario/SAD_P2_SCPA/bin

2. Le damos los permisos necesarios de ejecuci�n, en caso de que no los tenga ya, para asegurarnos
podemos introducir el siguiente comando:

		$ls -la

En caso de que no tenga los permisos suficientes para poder ejecutarlo se los asignamos:

		$sudo chmod 755 SCPA.jar

Nota: se necesita la contrase�a de super usuario para realizar esta tarea.

3. Volvemos al directorio ra�z del workspace e introducimos el comando para ejecutar SCPA.jar:

		cd ..
		$java -jar -Xmx2048m bin/SCPA.jar "path_train" "path_test" <argumentos>

Donde le asignaremos suficiente memoria para poder realizar el c�mputo sin problemas con el par�metro -Xmx
de la m�quina virtual de java.

El par�metro "path_test" y <argumentos> son �ptativos. El par�metro path_train, que es obligatorio, es el path
donde se encuentra el archivo .arff con los datos que se utilizar� para el experimento.

En la carpeta examples se encuentran varios archivos .arff con los que realizar el experimento, siendo uno de
ellos ionosphere.arff, adem�s de los resultados de los experimentos ya realizados. Para �sto, una vez situados
en el directorio bin, como se ha descrito en los puntos anteriores, introducimos el siguiente comando:

		$java -jar -Xmx2048m bin/SCPA.jar examples/ionosphere.arff B

Los resultados con los que cotejar se encuentran en el mismo directorio.
==================================================================================================================
Ejemplo de ejecuci�n en Windows:

0. Asegurarse de tener Java instalado en el ordenador.

1. Pulsamos el bot�n de Windows + la tecla R y escribimos cmd. Tambi�n podemos ir al men� de inicio -> ejecutar
y escribir cmd. Se nos abrir� la terminal de Windows.

2. Nos situamos en el directorio del workspace descomprimido, lugar donde est� situado el ejecutable en el
subdirectorio bin, SCPA.jar, supondremos que el workspace en est� en nuestro directorio personal:

		XP: cd C:\
		    cd Documents and Settings\usuario\SAD_P2_SCPA\bin
		7, 8.1: cd C:\
		    cd Users\usuario\SAD_P2_SCPA\bin

3. Volvemos al directorio ra�z del workspace e introducimos el comando para ejecutar SCPA.jar:

		cd ..
		java -jar -Xmx2048m bin\SCPA.jar "path_train" "path_test" <argumentos>

Donde le asignaremos suficiente memoria para poder realizar el c�mputo sin problemas con el par�metro -Xmx
de la m�quina virtual de java.

El par�metro "path_test" y <argumentos> son �ptativos. El par�metro path_train, que es obligatorio, es el path
donde se encuentra el archivo .arff con los datos que se utilizar� para el experimento.

En la carpeta examples se encuentran varios archivos .arff con los que realizar el experimento, siendo uno de
ellos ionosphere.arff, adem�s de los resultados de los experimentos ya realizados. Para �sto, una vez situados
en el directorio bin, como se ha descrito en los puntos anteriores, introducimos el siguiente comando:

		$java -jar -Xmx2048m bin\SCPA.jar examples\ionosphere.arff B

Los resultados con los que cotejar se encuentran en el mismo directorio.
==================================================================================================================
Ejemplo de resultados:
utilizando
java -jar ./bin/SCPA.jar ./examples/ionosphere.arff GB

Starting C and Gamma optimization with svm model
Finished C and Gamma optimization with svm model
Tiempo de ejecuci�n : 0.0

Correctly Classified Instances         196               94.686  %
Incorrectly Classified Instances        11                5.314  %
Kappa statistic                          0.8561
Mean absolute error                      0.0531
Root mean squared error                  0.2305
Relative absolute error                 13.8955 %
Root relative squared error             52.8092 %
Coverage of cases (0.95 level)          94.686  %
Mean rel. region size (0.95 level)      50      %
Total Number of Instances              207     
Recall:	 0.9468599033816425
Precision:	 0.9465655667973141
=== Confusion Matrix ===

   a   b   <-- classified as
  45   8 |   a = b
   3 151 |   b = g

weightedROC : 0.9147880421465328
Fmeasure:	0.9459689961217264
 Mejor C: 1.0
 Mejor gamma: 1.0
La predicci�n del conjunto de datos estimadas por el modelo class weka.classifiers.functions.LibSVM
 Le ha llevado un tiempo de ejecuci�n: 0.015
----------------Baseline-----------------
Starting B optimization with OneR and ad hoc method
Finished B optimization with OneR and ad hoc method
Fin del barrido de par�metros

Correctly Classified Instances         173               83.5749 %
Incorrectly Classified Instances        34               16.4251 %
Kappa statistic                          0.6681
Mean absolute error                      0.1643
Root mean squared error                  0.4053
Relative absolute error                 32.9091 %
Root relative squared error             81.1253 %
Coverage of cases (0.95 level)          83.5749 %
Mean rel. region size (0.95 level)      50      %
Total Number of Instances              207     
Recall:	 0.8357487922705314
Precision:	 0.8475681067694
=== Confusion Matrix ===

   a   b   <-- classified as
  72  27 |   a = b
   7 101 |   b = g

weitheredROC : 0.8312289562289562
 con F-measure:0.833480993268363
La mejor B :	11
La predicci�n del conjunto de datos estimadas por el modelo class weka.classifiers.rules.OneR
 Le ha llevado un tiempo de ejecuci�n: 0.0
----------CVBaseline---------------------
Mejor -B:	5

Correctly Classified Instances         162               78.2609 %
Incorrectly Classified Instances        45               21.7391 %
Kappa statistic                          0.5627
Mean absolute error                      0.2174
Root mean squared error                  0.4663
Relative absolute error                 43.5561 %
Root relative squared error             93.3304 %
Coverage of cases (0.95 level)          78.2609 %
Mean rel. region size (0.95 level)      50      %
Total Number of Instances              207     
Recall:	 0.782608695652174
Precision:	 0.7839464882943145
=== Confusion Matrix ===

  a  b   <-- classified as
 72 27 |  a = b
 18 90 |  b = g

weitheredROC : 0.7803030303030302
 con F-measure:0.7817805383022775
----------------GridSearch-----------------
------Starting parameter optimization with CVParameterSelection------
This might take a while...
----------GridSearchSVM---------------------
Mejor C:	8
Mejor gamma:	0.125

Correctly Classified Instances         193               93.2367 %
Incorrectly Classified Instances        14                6.7633 %
Kappa statistic                          0.8225
Mean absolute error                      0.0676
Root mean squared error                  0.2601
Relative absolute error                 17.6851 %
Root relative squared error             59.5768 %
Coverage of cases (0.95 level)          93.2367 %
Mean rel. region size (0.95 level)      50      %
Total Number of Instances              207     
Recall:	 0.9323671497584541
Precision:	 0.9323671497584541
=== Confusion Matrix ===

   a   b   <-- classified as
  46   7 |   a = b
   7 147 |   b = g

weightedROC : 0.9112349914236707
 con F-measure:0.9323671497584541
