==================================================================================================================
Asignatura: Miner�a de Datos
N�mero de Pr�ctica: 5.
Autores: Jose Ignacio S�nchez, Josu Rodr�guez, Jonathan Castro ,Alberto Fern�ndez, David Ram�rez, Andoni.
Fecha: 23 de noviembre de 2014.
==================================================================================================================
Recursos utilizados:
	- Windows XP, 8.1 con JDK 7 y eclipse Kepler y Luna.
	- Librer�a de Weka 3.7.10.
	- ClassifierBasedAttributeSelection1.0.5
	

* Es posible que d� errores a la hora de ejecutar si no se emplea la ruta absoluta
==================================================================================================================
Objetivos:

El objetivo principal de este m�dulo es  facilitar el trabajo de los clasificadores eliminando atributos
con informaci�n irrelevante o repetida.

Para tratar de realizar la mejor selecci�n de atributos posible este m�dulo utiliza la selecci�n de atributos
basado en un clasificador con dos clasificadores diferentes, Naive Bayes y RandomForest.

Tras crear las dos nuevos conjuntos de instancias (ambos con los atributos seleccionados por los modelos) se 
eval�an mediante el modelo RandomForest comparando la suma de las fmeasure para todas sus clases y guardando 
en un fichero el conjunto de instancias con mayor suma de fmeasure.

Nota: el proceso de selecci�n de atributos puede llevar bastante tiempo con archivos grandes. Se asume que la 
clase es el �ltimo atributo.
==================================================================================================================
Argumentos:
	- Fichero .arff de entrenamiento.
	- Fichero .arff de dev. (Optativo)
	- Fichero .arff de test. (Optativo)
	
	- Ejemplo: java -jar AttributeSelection-1.0.jar ScatteringNeutrons_Train.arff ScatteringNeutrons_Dev.arff ScatteringNeutrons_Test.arff
==================================================================================================================
Precondiciones:
	- La clase debe ser el �ltimo atributo.
	- La clase no puede ser num�rica.

Postcondiciones:
	- Se obtiene un fichero ARFF en la ruta del fichero original con el sufijo attSel.arff, este
	fichero es el fichero con la selecci�n de atributos.
		
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

		$sudo chmod 755 AttributeSelection-1.0.jar

Nota: se necesita la contrase�a de super usuario para realizar esta tarea.

3. Volvemos al directorio ra�z del workspace e introducimos el comando para ejecutar SCPA.jar:

		cd ..
		$java -jar -Xmx2048m bin/AttributeSelection-1.0.jar "path_train" <path dev> <path test>

Donde le asignaremos suficiente memoria para poder realizar el c�mputo sin problemas con el par�metro -Xmx
de la m�quina virtual de java.

Los par�metro <path dev> y <path test> son optativos. El par�metro path_train, que es obligatorio, es el path
donde se encuentra el archivo .arff con los datos que se utilizar� para el crear el filtro de la selecci�n de
atributos.

En la carpeta examples se encuentran varios archivos .arff con los que realizar el experimento, siendo uno de
ellos ionosphere.arff, adem�s de los resultados de los experimentos ya realizados. Para esto, una vez situados
en el directorio bin, como se ha descrito en los puntos anteriores, introducimos el siguiente comando:

		$java -jar -Xmx2048m bin/examples/AttributeSelection-1.0.jar ScatteringNeutrons_Train.arff ScatteringNeutrons_Dev.arff ScatteringNeutrons_Test.arff

==================================================================================================================
Ejemplo de ejecuci�n en Windows:

0. Asegurarse de tener Java instalado en el ordenador.

1. Pulsamos el bot�n de Windows + la tecla R y escribimos cmd. Tambi�n podemos ir al men� de inicio -> ejecutar
y escribir cmd. Se nos abrir� la terminal de Windows.

2. Nos situamos en el directorio donde se encuentra el ejecutable:

		XP: cd C:\
		    cd Documents and Settings\usuario\SAD_P2_SCPA\bin
		7, 8.1: cd C:\
		    C:\Users\Alberto\Desktop

3. Lanzamos el ejecutable AttributeSelection v1.0.jar:

		cd ..
		java -jar -Xmx2048m bin\AttributeSelection-1.0.jar ScatteringNeutrons_Train.arff ScatteringNeutrons_Dev.arff ScatteringNeutrons_Test.arff

Donde le asignaremos suficiente memoria para poder realizar el c�mputo sin problemas con el par�metro -Xmx
de la m�quina virtual de java.

Los par�metro <path dev> y <path test> son optativos. El par�metro path_train, que es obligatorio, es el path
donde se encuentra el archivo .arff con los datos que se utilizar� para el crear el filtro de la selecci�n de
atributos.
