						|-----------------------------------------------------|
						|	      Laboratorio 5: Minería de datos(nb)   	      |	
						|-----------------------------------------------------|
						
Objetivos:
	
	1.-Evaluar el modelo con evaluación no honesta, hold-out y 10-fold cross validation.

Pre-condiciones:
	
	1.-Los ficheros que se le pasan al programa deben tener la extensión .arff.
	2.-Al programa se le debe pasar 2 ficheros, uno train y otro dev, en ese orden y la ruta de donde quieres que guarde el fichero del informe.

Post-Condiciones:
	
	1.-Devuelve un fichero la siguiente información:
		a)Fíguras de mérito y matriz de confusión del algoritmo naive bayes utilizando el método de evaluación no honesto.
		b)Fíguras de mérito y matriz de confusión del algoritmo naive bayes utilizando el método de evaluación hold-out.
		c)Fíguras de mérito y matriz de confusión del algoritmo naive bayes utilizando el método de evaluación honesto(10 fold cross validation).
	2.- .model del clasificador.	


Como ejecutar el programa:

	1.-Abrir la consola correspondiente a tu Sistema Operativo.
	
	2.-Ejecutar el siguinte comando:  java -jar path_deljar [path_delarchivodeentrenamiento] [path_delarchivodev] [[path_delinforme]
	Parámetros:
		path_delarchivodeentrenamiento: path del archivo .arff correspondiente al conjunto de train.
		path_delarchivodetest: path del archivo .arff correspondiente al conjunto de dev.
		path_delinforme: path de donde quieres que guarde el infrome.
	
	Ejemplo: java -jar knn.jar c:/train.arff c:/dev.arff c:/informeknn.txt

Recursos utlizados:
	
	1.-Eclipse Luna
	2.-JDK 1.7
	3.-Weka 3.6.11

		
	  
