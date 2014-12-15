==================================================================================================================
Asignatura: Minería de Datos
Número de Práctica: 5.
Autores: Jose Ignacio Sánchez, Josu Rodríguez, Jonathan Castro ,Alberto Fernández, David Ramírez, Andoni.
Fecha: 23 de noviembre de 2014.
==================================================================================================================
Recursos utilizados:
	- Windows XP, 8.1 con JDK 7 y eclipse Kepler y Luna.
	- Librería de Weka 3.7.10.
	- ClassifierBasedAttributeSelection1.0.5
	

* Es posible que dé errores a la hora de ejecutar si no se emplea la ruta absoluta
==================================================================================================================
Objetivos:

El objetivo principal de este módulo es  facilitar el trabajo de los clasificadores eliminando atributos
con información irrelevante o repetida.

Para tratar de realizar la mejor selección de atributos posible este módulo utiliza la selección de atributos
basado en un clasificador con dos clasificadores diferentes, Naive Bayes y RandomForest.

Tras crear las dos nuevos conjuntos de instancias (ambos con los atributos seleccionados por los modelos) se 
evalúan mediante el modelo RandomForest comparando la suma de las fmeasure para todas sus clases y guardando 
en un fichero el conjunto de instancias con mayor suma de fmeasure.

Nota: el proceso de selección de atributos puede llevar bastante tiempo con archivos grandes. Se asume que la 
clase es el último atributo.
==================================================================================================================
Argumentos:
	- Fichero .arff de entrenamiento.
	- Fichero .arff de dev. (Optativo)
	- Fichero .arff de test. (Optativo)
	
	- Ejemplo: java -jar AttributeSelection-1.0.jar ScatteringNeutrons_Train.arff ScatteringNeutrons_Dev.arff ScatteringNeutrons_Test.arff
==================================================================================================================
Precondiciones:
	- La clase debe ser el último atributo.
	- La clase no puede ser numérica.

Postcondiciones:
	- Se obtiene un fichero ARFF en la ruta del fichero original con el sufijo attSel.arff, este
	fichero es el fichero con la selección de atributos.
		
==================================================================================================================
Ejemplo de ejecución en GNU/Linux:

Ejecución del programa mediante la línea de comandos:

1. Nos situamos en el directorio del workspace descomprimido, lugar donde está situado el ejecutable en el
subdirectorio bin, SCPA.jar, supondremos que el workspace en está en nuestro directorio personal:

		$cd /home/usuario/SAD_P2_SCPA/bin

2. Le damos los permisos necesarios de ejecución, en caso de que no los tenga ya, para asegurarnos
podemos introducir el siguiente comando:

		$ls -la

En caso de que no tenga los permisos suficientes para poder ejecutarlo se los asignamos:

		$sudo chmod 755 AttributeSelection-1.0.jar

Nota: se necesita la contraseña de super usuario para realizar esta tarea.

3. Volvemos al directorio raíz del workspace e introducimos el comando para ejecutar SCPA.jar:

		cd ..
		$java -jar -Xmx2048m bin/AttributeSelection-1.0.jar "path_train" <path dev> <path test>

Donde le asignaremos suficiente memoria para poder realizar el cómputo sin problemas con el parámetro -Xmx
de la máquina virtual de java.

Los parámetro <path dev> y <path test> son optativos. El parámetro path_train, que es obligatorio, es el path
donde se encuentra el archivo .arff con los datos que se utilizará para el crear el filtro de la selección de
atributos.

En la carpeta examples se encuentran varios archivos .arff con los que realizar el experimento, siendo uno de
ellos ionosphere.arff, además de los resultados de los experimentos ya realizados. Para esto, una vez situados
en el directorio bin, como se ha descrito en los puntos anteriores, introducimos el siguiente comando:

		$java -jar -Xmx2048m bin/examples/AttributeSelection-1.0.jar ScatteringNeutrons_Train.arff ScatteringNeutrons_Dev.arff ScatteringNeutrons_Test.arff

==================================================================================================================
Ejemplo de ejecución en Windows:

0. Asegurarse de tener Java instalado en el ordenador.

1. Pulsamos el botón de Windows + la tecla R y escribimos cmd. También podemos ir al menú de inicio -> ejecutar
y escribir cmd. Se nos abrirá la terminal de Windows.

2. Nos situamos en el directorio donde se encuentra el ejecutable:

		XP: cd C:\
		    cd Documents and Settings\usuario\SAD_P2_SCPA\bin
		7, 8.1: cd C:\
		    C:\Users\Alberto\Desktop

3. Lanzamos el ejecutable AttributeSelection v1.0.jar:

		cd ..
		java -jar -Xmx2048m bin\AttributeSelection-1.0.jar ScatteringNeutrons_Train.arff ScatteringNeutrons_Dev.arff ScatteringNeutrons_Test.arff

Donde le asignaremos suficiente memoria para poder realizar el cómputo sin problemas con el parámetro -Xmx
de la máquina virtual de java.

Los parámetro <path dev> y <path test> son optativos. El parámetro path_train, que es obligatorio, es el path
donde se encuentra el archivo .arff con los datos que se utilizará para el crear el filtro de la selección de
atributos.
