==================================================================================================================
Asignatura: Miner�a de Datos
N�mero de Pr�ctica: 5.
Autores: Jose Ignacio S�nchez, Josu Rodr�guez, Jonathan Castro, Alberto Fern�ndez, David Ram�rez, Andoni.
Fecha: 23 de noviembre de 2014.
==================================================================================================================
Recursos utilizados:
	- Windows XP, 8.1 con JDK 7 y eclipse Kepler y Luna.
	- Librer�a de Weka 3.7.10.
	- ClassifierBasedAttributeSelection1.0.5
	

* Es posible que d� errores a la hora de ejecutar si no se emplea la ruta absoluta
==================================================================================================================
Objetivos:

El objetivo principal de este m�dulo es convertir un atributo num�rico (normalmente la clase) en un atributo
nominal.

Es posible definir tanto el n�mero de intervalos que se quieren generar as� como el atributo que se quiere
discretizar.

Nota: si no se indica un valor para los intervalos y el atributo se discretizara el ultimo atributo con 
10 intervalos 
==================================================================================================================
Argumentos:
	- Fichero .arff a discretizar.
	- (Opcional) Opciones a considerar
		-I #: el n�mero m�ximo de intervalos que se van a generar
		-C #: la posici�n del atributo que se va a discretizar (se empieza a contar desde 1)
	- (Opcional) Opciones a considerar
		-I #: el n�mero m�ximo de intervalos que se van a generar
		-C #: la posici�n del atributo que se va a discretizar (se empieza a contar desde 1)
	- Ejemplo: java -jar Discretization v1.0.jar ScatteringNeutrons_Train.arff -I 5 -C 14
==================================================================================================================
Precondiciones:
	- Las opciones a considerar tienen que estar en may�sculas.

Postcondiciones:
	- Se obtiene un fichero ARFF en la ruta del fichero original con el sufijo .DiscretizedClass.arff, este
	fichero es el fichero con el atributo discretizado
		
==================================================================================================================
Ejemplo de ejecuci�n en GNU/Linux:

Ejecuci�n del programa mediante la l�nea de comandos:

1. Nos situamos en el directorio del workspace descomprimido, lugar donde est� situado el ejecutable en el
subdirectorio bin, Discretize-1.0.jar, supondremos que el workspace en est� en nuestro directorio personal:

		$cd /home/usuario/SAD_P2_SCPA/bin

2. Le damos los permisos necesarios de ejecuci�n, en caso de que no los tenga ya, para asegurarnos
podemos introducir el siguiente comando:

		$ls -la

En caso de que no tenga los permisos suficientes para poder ejecutarlo se los asignamos:

		$sudo chmod 755 Discretize-1.0.jar

Nota: se necesita la contrase�a de super usuario para realizar esta tarea.

3. Volvemos al directorio ra�z del workspace e introducimos el comando para ejecutar Discretize-1.0.jar:

		cd ..
		$java -jar -Xmx2048m bin/Discretize-1.0.jar "path_train" "path_test" <argumentos>

Donde le asignaremos suficiente memoria para poder realizar el c�mputo sin problemas con el par�metro -Xmx
de la m�quina virtual de java.

El par�metro  <argumentos> es optativo. El par�metro path_train, que es obligatorio, es el path
donde se encuentra el archivo .arff con los datos que se utilizar� para el experimento.

En la carpeta examples se encuentran varios archivos .arff con los que realizar el experimento, siendo uno de
ellos ionosphere.arff, adem�s de los resultados de los experimentos ya realizados. Para esto, una vez situados
en el directorio bin, como se ha descrito en los puntos anteriores, introducimos el siguiente comando:

		$java -jar -Xmx2048m bin/Discretize-1.0.jar examples/ionosphere.arff

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

3. Lanzamos el ejecutable Discretize v1.0.jar:

		cd ..
		java -jar -Xmx2048m bin\SCPA.jar "pathARFF" [Argumento] [Argumento]

Donde le asignaremos suficiente memoria para poder realizar el c�mputo sin problemas con el par�metro -Xmx
de la m�quina virtual de java.

Los par�metros [Argumento] son optativos. El par�metro pathARFF, que es obligatorio, es el path
donde se encuentra el archivo .arff con los datos que se discretizaran.
