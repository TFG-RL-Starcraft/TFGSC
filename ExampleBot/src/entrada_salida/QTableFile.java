package entrada_salida;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter; 
import java.io.PrintWriter;
import java.util.StringTokenizer;

import q_learning.QTable;
import q_learning.QTable_Array;


public class QTableFile {
	
	
	public static void escribirTabla(QTable t){
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("C:\\Users\\Alberto\\Desktop\\prueba.txt");
            pw = new PrintWriter(fichero);
 
            
            pw.println(t.getTam() + "\n" + t.getAcc());
            
            for(int i = 0; i < t.getTam();i++){
            	for(int j = 0; j < t.getAcc(); j++){
            		pw.print(t.get(i, j));
            		if(j!=t.getAcc()-1){
            			pw.print(" ");
            		}
            	}
            	pw.println();
            }
            
            
            /*for (int i = 0; i < t.getTam(); i++){
                pw.println(t.line(0));
            }*/
 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
	}
	
	public static QTable leeTabla(){
		FileReader fr = null;
		QTable q = null;
		
		try {
	         // Apertura del fichero y creacion de BufferedReader para poder
	         // hacer una lectura comoda (disponer del metodo readLine()).
	         QTableFile archivo = new QTableFile ("C:\\Users\\Alberto\\Desktop\\prueba.txt");
	         fr = new FileReader (archivo);
	         BufferedReader br = new BufferedReader(fr);
	         
	         
	         // Lectura del fichero
	         String linea =br.readLine();
	         int H = Integer.parseInt(linea);
	         linea = br.readLine();
	         int W = Integer.parseInt(linea);
	         q = new QTable_Array(H,W);
	         while((linea=br.readLine())!=null){
	        	 double d[] = parse(linea);
	        	 for(int i = 0; i < H; i++){
	            	for(int j = 0; j < d.length; j++){
	            		q.set(i, j, d[j]);
	            	}
	            }
	         }
	      }
	      catch(Exception e){
	         e.printStackTrace();
	      }finally{
	         // En el finally cerramos el fichero, para asegurarnos
	         // que se cierra tanto si todo va bien como si salta 
	         // una excepcion.
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
		
		return q;
	}
	
	private static int[] pLinea(String s){
		StringTokenizer st = new StringTokenizer(s," ");
		
		int num= st.countTokens();
		String tokens[] = new String[num];
		int i=0;
		while(st.hasMoreTokens())
		{
			tokens[i]= st.nextToken();
			i++;
		}
		
		int d[]  = new int[num];
		
		for(int j = 0; j < num;j++)
		{
			d[j]= Integer.valueOf(tokens[j]);
		}	
	
		
		return d;
	}
	
	private static double[] parse(String s)
	{
		StringTokenizer st = new StringTokenizer(s," ");
		
		int num= st.countTokens();
		String tokens[] = new String[num];
		int i=0;
		while(st.hasMoreTokens())
		{
			tokens[i]= st.nextToken();
			i++;
		}
		
		double d[]  = new double[num];
		
		for(int j = 0; j < num;j++)
		{
			d[j]= Double.valueOf(tokens[j]);
		}	
	
		
		return d;
	}
}