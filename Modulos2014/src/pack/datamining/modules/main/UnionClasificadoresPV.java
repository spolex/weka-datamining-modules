package pack.datamining.modules.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class UnionClasificadoresPV {

	public static void main(String[] args) {

		if(args.length!=0 && args.length != 1)
		{
			/*Comprobar si se ha pedido la ayuda*/
			if(buscarParametro("-h", args) != -1)
			{
				//Se imprime y sale el programa
				help();
				System.exit(0);
			}
			
			/**************Objetos*******************/
			String pathConfiguracion = args[0];
			Scanner[] ficherosFuente;
			Double[] ponderaciones;
			String[] clases;
			
			/************Abrir el fichero de parámetros y cargar un arraylist de strings (lineas)***/
			ArrayList<String> lineas = new ArrayList<String>();
			File archivo = new File(pathConfiguracion);
			try {
				Scanner lector = new Scanner(archivo);
				
				while(lector.hasNext())
				{
					lineas.add(lector.nextLine());
				}
				
				//cierro el fichero
				lector.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			/********************Cargar ahora los descriptores y las ponderaciones***********************/
			
			ficherosFuente = new Scanner[lineas.size()];
			ponderaciones = new Double[lineas.size()];
			
			for(int i = 0; i < lineas.size();i++)
			{
				//Cojo una linea y la troceo por ";"
				String[] partes = lineas.get(i).split(";");
				
				//Con la primera parte cargo el fichero
				File file = new File(partes[0]);
				try {
					Scanner sc = new Scanner(file);
					ficherosFuente[i] = sc;
				} catch (FileNotFoundException e) {
					System.err.println("Error al abrir uno de los ficheros. El programa no puede continuar");
					e.printStackTrace();
					System.exit(1);
				}
				
				//Con la segunda obtengo su ponderación correspondiente
				try{
					ponderaciones[i] = Double.parseDouble(partes[1]);
				} catch(NumberFormatException e) {
					System.err.println("Error al cargar los parámetros de los ficheros. El programa no puede continuar");
					e.printStackTrace();
					System.exit(1);
				}
			}
			
			/******************Cargar los valores de las clases y avanzar una linea en cada descriptor***/
			
			//Cogemos el primer descriptor y obtenemos la primera linea
			String lineaClases = ficherosFuente[0].nextLine();
			//la troceamos por ";"
			clases = lineaClases.split(";");
			
			//En el resto de ficheros cogemos la primera línea y comprobamos que es igual a la del primer fichero
			for(int i = 1; i<ficherosFuente.length;i++)
			{
				String[] clases2 = ficherosFuente[i].nextLine().split(";");
					
					if(clases.length == clases2.length)
					{
						for(int j=0; j<clases.length;j++)
						{
							//Si no coinciden las clases
							if(!clases[j].equals(clases2[j]))
							{
								System.err.println("Los ficheros a unir no son compatibles, sus clases no son iguales \n el programa no puede continuar");
								System.exit(1);
							}
						}
					}
					else
					{
						System.err.println("Los ficheros a unir no son compatibles, no tienen el mismo número de clases \n el programa no puede continuar");
						System.exit(1);
					}
			}
			
			/**********Crear los ficheros de salida de predicciones y predicciones con probabilidad******/
			
			PrintStream predicciones = crearFichero("predicciones-union.arff", "");
			PrintStream prediccionesProb = crearFichero("predicciones-union-ConProb.arff", "");
			//Poner en la primera linea los valores posibles de las clases
			prediccionesProb.println(lineaClases);
			
			
			
			/*************************Algoritmo**********************************************************/
			String error ="";
			//Suponemos que todos los ficheros son iguales, realizamos el proceso hasta que el primero se quede sin lineas
				while(ficherosFuente[1].hasNext())
				{
					Double[][] matriz = new Double[ficherosFuente.length][clases.length];
					
					//Recorrer cada fichero
					for(int fich=0;fich<ficherosFuente.length;fich++)
					{
						//Cargar una linea del fichero dividida por ";"
						String[] linea = ficherosFuente[fich].nextLine().split(";");
						//Comprobar que la longitud del array es igual al nº de clases
						if(linea.length!=clases.length)
						{
							//salir por error
							System.err.println("Hay errores en los ficheros, hay instancias para las que no hay probabilidad para cada clase");
							System.err.println("El programa no puede continuar");
							System.exit(1);
						}
						//Cada uno de los string pasarlo a double, cargando la matriz [fichero][clases]
						for(int prob=0;prob<linea.length;prob++)
						{
							try{
								matriz[fich][prob] = Double.parseDouble(linea[prob]);
								//comprobar que sea una probabilidad, rango [0,1]
								if(matriz[fich][prob] < 0 || matriz[fich][prob] > 1)
								{
									System.err.println("El grado de pertenencia no es una probabilidad, no entra en el intervalo [0,1]");
									System.err.println("El programa no puede continuar");
									System.exit(1);
								}
							} catch (NumberFormatException e)
							{
								System.err.println("Hay probabilidades no expresadas en formato correcto, el programa no puede continuar");
								System.exit(1);
							}
						}
					}
					
					
					
					//Hacer los calculos y obtener un nuevo array[clases] con las nuevas probabilidades
					Double[] union = new Double[clases.length];
					
						//recorrer la matriz haciendo la media ponderada para cada clase al array ponderaciones
						//recorremos para cada clase (columnas)
						for(int clase=0; clase< matriz[0].length;clase++)
						{
							//hacer la media ponderada de las casillas de la columna
							double media=0.0;
							
							for(int fila=0;fila<matriz.length;fila++)
							{
								media += matriz[fila][clase] * ponderaciones[fila];
							}
							
							//Guardamos la nueva ponderación
							union[clase] = media;
						}
					//Imprimir las probabilidades al fichero
						for(int pos=0;pos<union.length;pos++)
						{
							if(pos == union.length-1) prediccionesProb.print(union[pos]);
							else prediccionesProb.println(union[pos]+";");
						}
						prediccionesProb.println();
						
					//Obtener la clase con mayor probabilidad e imprimirla en el fichero de predicciones "simples"
						int indMax=0;
						for(int pos=0;pos<union.length;pos++)
						{
							if(union[pos] > union[indMax]) indMax = pos;
						}
						predicciones.println(clases[indMax]);
					
				}
			
			//Cerrar todos los ficheros
			System.out.println("Cerrando ficheros...");
			for(int i = 0; i< ficherosFuente.length;i++)
			{
				ficherosFuente[i].close();
			}
			
			predicciones.close();
			prediccionesProb.close();
			
			System.out.println("La unión de clasificadores ha finalizado "+error);
		}
		else
		{
			System.err.println("Los parámetros especificados no son correctos, el programa no puede continuar");
			help();
			System.exit(1);
		}

	}

	/**
	 * Dado un string, se busca su posición dentro del array
	 * @param pStr 
	 * @param args
	 * el array en el que buscar
	 * @return
	 * la posición del string dentro del array
	 * -1 en caso de que no se encuentre el elemento
	 */
	private static int buscarParametro(String pStr, String[] array) {
		//comprobamos que el array tenga elementos
		if(array.length != 0)
		{
			//recorremos el array buscando el elemento
			
			int i = 0;
			boolean enc = false;
			
			while(!enc && i < array.length)
			{
				if(array[i].equals(pStr))
				{
					enc = true;
				}
				else
				{
					i++;
				}
			}
			
			if(!enc)
			{
				i = -1;
			}
			
			return i;
		}
		return -1;
	}
	
	/**
	 * Imprime la ayuda del programa
	 */
	private static void help()
	{
		System.out.println("\nObjetivo:"
				+ "\n\tObtener las predicciones de un test uniendo las predicciones de varios clasificadores. Se implementa mediante la unión de clasificadores en paralelo por votación."
				+ "\nEjecución:"
				+ "\n\tComando: java -jar MDP5_union_PV.jar Fichero_Parametros [-h]"
				+ "\n\tArgumentos:"
				+ "\n\t\tFichero_Parametros"
				+ "\n\t\t\tFichero de texto que contiene los parámetros que cargará el programa."
				+ "\n\t\t\tEl formato es: 	-En cada línea se representa información correspondiente a un clasificador"
				+ "\n\t\t\t\t-Por cada clasificador se especifica la ruta del fichero de predicciones correspondiente y su ponderación al realizar la unión (separadas por ;)"
				+ "\n\t\t\t\tEjemplo:"
				+ "\n\t\t\t\t\truta/predmod1.txt;0.2"
				+ "\n\t\t\t\t\truta/predmod2.txt;0.5"
				+ "\n\t\t\t\t\t..."
				+ "\n\t\t[-h]"
				+ "\n\t\t\timprime la ayuda para la ejecución del programa. Si este parámetro se especifica, finaliza el programa tras mostrar la ayuda, independientemente del resto de parámetros especificados"
				+ "\nPrecondiciones:"
				+ "\n\tDisponer de la máquina virtual JAVA v7"
				+ "\n\tEl fichero de parámetros debe estar especificado de forma correcta"
				+ "\n\tLos ficheros de predicciones deben cumplir el siguiente formato, donde las probabilidades de pertenencia a cada clase se especifican para cada instancia en una línea:"
				+ "\n\t\tFichero_Predicciones (separador = ;):"
				+ "\n\t\t\tClase1;Clase2;ClaseN"
				+ "\n\t\t\tp1;p2;pn"
				+ "\n\t\t\t..."
				+ "\n\t\tEn todos los ficheros a usar, una instancia concreta ocupa la misma línea."
				+ "\nPostcondiciones:"
				+ "\n\tSe generará un fichero con extensión \"unionPV-pred-prob.txt\" que contendrá una fila por cada instancia de test en la que se especifica el valor de la probabilidad de pertenencia a cada una de las clases posibles."
						+ "\n\tSe generará un fichero \"prediccion-union.txt\" que contendrá por cada fila la clase predicha para una instancia.");
	}
	
	/**
	 * Crea un nuevo fichero y devuelve su descriptor para escribir en el mismo
	 * @param pPathOriginal
	 * El path del fichero original, con extensión
	 * @param pSufijo
	 * El sufijo que tendrá el nuevo fichero
	 * @return
	 * El descriptor del fichero para comenzar su escritura. null si no se puede crear
	 */
	private static PrintStream crearFichero(String pPathOriginal, String pSufijo)
	{
		String path = pPathOriginal.substring(0, (pPathOriginal.length() - 5));
		path = path + pSufijo;
		
		File f = new File(path);
		PrintStream ps;
		try {
			ps = new PrintStream(f);
			return ps;
		} catch (FileNotFoundException e) {
			System.err.println("No se ha podido crear el fichero de guardado");
			System.exit(1);
		}
		
		return null;
	}

}
