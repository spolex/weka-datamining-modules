==================================================================================================================
Asignatura: Minería de datos.
Número de Práctica: 5.
Autor: Jonathan Castro.
Fecha: 7 de diciembre de 2014.
==================================================================================================================
Recursos utilizados:
	- Linux Mint 17 con JDK 7 y eclipse Luna.
	- Windows 7* con JDK 8.
	- Librería de Weka 3.6.11 y 3.7.11.
	- Librería de LibSVM 3.17.
==================================================================================================================
Objetivos:

El objetivo principal del sistema es optimizar los parámetros de un modelo basado One-class en las máquinas de
soporte vectorial (SVM), utilizando un barrido de parámetros ad-hoc observando las instancias correcta e
incorrectamente clasificadas. Tras entrenar el modelo, se realiza la predicción del conjunto de test, de ser
pedida, y el modelo entrenado.

Además, es posible obtener un fichero con instancias con los outliers eliminados.

Nota: lo más seguro es que no se pueda arrancar el programa con los conjuntos de instancias grandes por falta
de memoria. ¿Cómo se sabe si hace falta más? El programa parará sin dar resultado alguno.
==================================================================================================================
Argumentos:

1. Para detección de outilers:
	- Fichero .arff.

2. Para entrenar el modelo:
	- Fichero .arff de entrenamiento.
	- Fichero .arff de Dev.

3. Para entrenar el modelo y evaluar un conjunto de test:
	- Fichero .arff de entrenamiento.
	- Fichero .arff de Dev.
	- Fichero .arff de test.

Nota: dependiendo del número de parámetros introducidos, hará una opción u otra.

Ejemplos:
	1. java [-Xmx2048m] -jar oneclass.jar train.e2e.wOOV.obfuscated.arff
	2. java [-Xmx2048m] -jar oneclass.jar train.e2e.wOOV.obfuscated.arff dev.e2e.wOOV.obfuscated.arff
	3. java [-Xmx2048m] -jar oneclass.jar train.e2e.wOOV.obfuscated.arff dev.e2e.wOOV.obfuscated.arff
	   test.e2e.wOOV.obfuscated.arff
==================================================================================================================
Precondiciones:
	- La clase en los ficheros .arff debe encontrarse en última posición.
	- La clase debe ser binaria.

Postcondiciones:
	1. Se obtiene un fichero con el conjunto de instancias con los outliers eliminados.
	   nombreDelFichero_SinOutliers.txt.
	2 y 3. Se obtienen varios ficheros:
		- Modelo entrenado: fecha-hora_OneClass.model
		- Fichero de texto con los resultados obtenidos: fecha-hora_OneClassPredictions.txt
		- Fichero con el conjunto de instancias positivas: nombreDelFichero-Positivos.arff
==================================================================================================================
Ejemplo de ejecución en GNU/Linux:

Ejecución del programa mediante la línea de comandos:

1. Nos situamos en el directorio del workspace descomprimido, lugar donde está situado el ejecutable en el
subdirectorio executables, oneclass.jar, supondremos que el workspace en está en nuestro directorio personal:

		$cd /home/usuario/Modulos2014/executables

2. Le damos los permisos necesarios de ejecución, en caso de que no los tenga ya, para asegurarnos
podemos introducir el siguiente comando:

		$ls -la

En caso de que no tenga los permisos suficientes para poder ejecutarlo se los asignamos:

		$sudo chmod 755 SCPA.jar

Nota: se necesita la contraseña de super usuario para realizar esta tarea.

3. Volvemos al directorio raíz del workspace e introducimos el comando para ejecutar SCPA.jar:

		cd ..
		$java -jar [-Xmx2048m] execuables/oneclass.jar "path_train" ["path_dev"] ["path_test"]

Donde le asignaremos suficiente memoria para poder realizar el cómputo sin problemas con el parámetro -Xmx
de la máquina virtual de java.

El parámetro "path_dev" y "path_test" se pondrán para realizar la segunda o tercera opción del programa.
El parámetro path_train, que es obligatorio, es el path donde se encuentra el archivo .arff con los datos que
se utilizará para el experimento o puede ser el fichero con el conjunto de instancias al que le queremos eliminar
los outliers.

En la carpeta examples se encuentran varios archivos .arff con los que realizar el experimento, siendo uno de
ellos ionosphere.arff, además de los resultados de los experimentos ya realizados. Para ésto, una vez situados
en el directorio bin, como se ha descrito en los puntos anteriores, introducimos el siguiente comando:

		$java -jar -Xmx2048m execuables/oneclass.jar examples/train.e2e.wOOV.obfuscated.arff

Los resultados con los que cotejar se encuentran en el mismo directorio.
==================================================================================================================
Ejemplo de ejecución en Windows:

0. Asegurarse de tener Java instalado en el ordenador.

1. Pulsamos el botón de Windows + la tecla R y escribimos cmd. También podemos ir al menú de inicio -> ejecutar
y escribir cmd. Se nos abrirá la terminal de Windows.

2. Nos situamos en el directorio del workspace descomprimido, lugar donde está situado el ejecutable en el
subdirectorio executables, oneclass.jar, supondremos que el workspace en está en nuestro directorio personal:

		cd C:\
		cd Users\usuario\Modulos2014

3. Introducimos el comando para ejecutar SCPA.jar:

		java -jar [-Xmx2048m] execuables/oneclass.jar "path_train" ["path_dev"] ["path_test"]

Donde le asignaremos suficiente memoria para poder realizar el cómputo sin problemas con el parámetro -Xmx
de la máquina virtual de java.

El parámetro "path_dev" y "path_test" se pondrán para realizar la segunda o tercera opción del programa.
El parámetro path_train, que es obligatorio, es el path donde se encuentra el archivo .arff con los datos que
se utilizará para el experimento o puede ser el fichero con el conjunto de instancias al que le queremos eliminar
los outliers.

En la carpeta examples se encuentran varios archivos .arff con los que realizar el experimento, siendo uno de
ellos ionosphere.arff, además de los resultados de los experimentos ya realizados. Para ésto, una vez situados
en el directorio bin, como se ha descrito en los puntos anteriores, introducimos el siguiente comando:

		$java -jar -Xmx2048m execuables/oneclass.jar examples/train.e2e.wOOV.obfuscated.arff

Los resultados con los que cotejar se encuentran en el mismo directorio.
==================================================================================================================
Ejemplo de resultados:

java -jar ./bin/SCPA.jar examples/train.e2e.wOOV.obfuscated.arffattSel.arff examples/dev.e2e.wOOV.obfuscated.arffattSel.arff
examples/test.e2e.wOOV.obfuscated.arffattSel.arff

+++ One-class. +++

--- Instancias leídas. ---

+++ Realización del test completo de One-class. +++

--- Instancias barajadas. ---

--- Barrido del parámetro nu del clasificador One-class. ---

Barrido 1 de 100...
 - Nu: 0,001.
 * Instancias correctamente clasificadas: 0.

Barrido 2 de 100...
 - Nu: 0,002.
 * Instancias correctamente clasificadas: 16987.

Barrido 3 de 100...
 - Nu: 0,003.
 * Instancias correctamente clasificadas: 16697.

Barrido 4 de 100...
 - Nu: 0,004.
 * Instancias correctamente clasificadas: 16987.

Barrido 5 de 100...
 - Nu: 0,005.
 * Instancias correctamente clasificadas: 16697.

Barrido 6 de 100...
 - Nu: 0,006.
 * Instancias correctamente clasificadas: 17036.

Barrido 7 de 100...
 - Nu: 0,007.
 * Instancias correctamente clasificadas: 290.

Barrido 8 de 100...
 - Nu: 0,008.
 * Instancias correctamente clasificadas: 17036.

Barrido 9 de 100...
 - Nu: 0,009.
 * Instancias correctamente clasificadas: 16697.

Barrido 10 de 100...
 - Nu: 0,010.
 * Instancias correctamente clasificadas: 339.

Barrido 11 de 100...
 - Nu: 0,011.
 * Instancias correctamente clasificadas: 16697.

Barrido 12 de 100...
 - Nu: 0,012.
 * Instancias correctamente clasificadas: 16697.

Barrido 13 de 100...
 - Nu: 0,013.
 * Instancias correctamente clasificadas: 16697.

Barrido 14 de 100...
 - Nu: 0,014.
 * Instancias correctamente clasificadas: 17036.

Barrido 15 de 100...
 - Nu: 0,015.
 * Instancias correctamente clasificadas: 339.

Barrido 16 de 100...
 - Nu: 0,016.
 * Instancias correctamente clasificadas: 17036.

Barrido 17 de 100...
 - Nu: 0,017.
 * Instancias correctamente clasificadas: 17036.

Barrido 18 de 100...
 - Nu: 0,018.
 * Instancias correctamente clasificadas: 16987.

Barrido 19 de 100...
 - Nu: 0,019.
 * Instancias correctamente clasificadas: 16697.

Barrido 20 de 100...
 - Nu: 0,020.
 * Instancias correctamente clasificadas: 290.

--- Parada forzada de barrido. Obteniendo datos a guardar. ---

Fichero de modelo creado en: Modelos/20141206-1417_OneClass.model.
Fichero de predicciones creado en: Modelos/20141206-1417_OneClassPredictions.txt
.

Nuevo fichero: train.e2e.wOOV.obfuscated.arffattSel-Positivos.arff

--- Test del modelo One-clas realizado. ---
