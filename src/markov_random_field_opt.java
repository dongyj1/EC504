package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class markov_random_field_opt {
  private static final int src_over_snk = 4;
  private static final int scale = 8;
  //private static final int [] labels = {0, 40, 60, 90, 160, 200, 220,  255};
  private static final int [] labels = {0, 40, 180, 220,120, 80, 200, 255};
  private static int length = 0, width = 0, max = 0;
  private static int src = 0;
  private static int snk = 0;

 

  public static void edge_initial(int[][] pgm, int[][] nlink, int[][] tlink, int max)
  {
      //long start = System.currentTimeMillis();
     int node = 0;
     for(int i = 0; i< length; i++){
  	   for(int j = 0; j< width; j++){
  		   //int nweights = 0;
  		   if(j < width-1)
		  {
			  nlink[node][0]= (max - Math.abs(pgm[i][j] - pgm[i][j+1]))/scale; 
			  //nweights += nlink[node][node + 1];
		  }
  		   else nlink[node][0]=0;
		  if(j > 0)
		  {
			  nlink[node][1]= (max - Math.abs(pgm[i][j] - pgm[i][j-1]))/scale; 
			                
			  //nweights += nlink[node][node - 1];
		  }
		  else nlink[node][1]=0;
		  if(i < length-1 )
		  {
			  nlink[node][2]= (max - Math.abs(pgm[i][j] - pgm[i+1][j]))/scale;
			  //nweights += nlink[node][node + width];
		  }
		  else nlink[node][2]=0;
		  if(i > 0 )
		  {
			  nlink[node][3]= (max - Math.abs(pgm[i][j] - pgm[i-1][j]))/scale;
			  
			  //nweights += nlink[node][node - width];
		  }
		  else nlink[node][3]=0;
		  for(int k = 0; k < labels.length; k++) {
			  tlink[k][node] = (int) (((double)src_over_snk) * ((double) max) * Math.exp(-((double)src_over_snk) *Math.abs(pgm[i][j] - labels[k])/((double) max)));
			//  System.out.print(tlink[k][node]); System.out.print(" ");
		  }    
		 // System.out.println("");
		  node++;
  	   }
      }
  }
  
  public static void cut_initial(int[][] nlink, int[][] tlink, int [] par) {
	  int cost = 0;
	  for(int i = 0; i< length; i++){
	  	   for(int j = 0; j< width; j++){
	  		 int min = Integer.MAX_VALUE;
	  		 int label = 0;
	  		 for(int k = 0; k < tlink.length; k++) {
	  			 if(min > tlink[k][i * width + j]) {
	  				 min = tlink[k][i * width + j];
	  				 label = k;
	  			 }
	  		 } 
	  		 par[i * width + j] = label;
	  		 //System.out.print(label); System.out.print(" ");
	  	   }
	  	  //System.out.println("");
	  }
	  
  }
  
  public static int cost(int[][] nlink, int[][] tlink, int [] par) {
	  int cost = 0;
	  for(int i = 0; i< length; i++){
	  	   for(int j = 0; j< width; j++){
	  		 for(int k = 0; k < tlink.length; k++) {
	  			 cost += tlink[k][i * width + k];
	  			}
	  		 cost -= tlink[par[i*width+j]][i * width + j];
	  		 } 
	  }
	  for(int i = 0; i< length; i++){
	  	   for(int j = 0; j< width; j++){
	  		   if(j < width-1)
			  {
				  if(par[i*width+j]!=par[i*width+j+1]) cost+=nlink[i*width+j][0];
			  }
			  if(j > 0)
			  {
				  if(par[i*width+j]!=par[i*width+j-1]) cost+=nlink[i*width+j][1];
				                
			  }
			  if((i < length-1 ))
			  {
				  if(par[i*width+j]!=par[(i+1)*width+j]) cost+=nlink[i*width+j][2];
			  }
			  if((i > 0 ))
			  {
				  if(par[i*width+j]!=par[(i-1)*width+j]) cost+=nlink[i*width+j][3];
			  }
	  	   }
	  }
	 return cost;
	  
  }
  
  public static void loadGraph(List<dinic.Edge>[] graph, int[][] nlink, int[][] tlink, int [] par, int src, int snk) {
	  for(int i = 0; i < nlink.length; i++) {
		  	int neighbors = 0;
		    if(par[i] == snk || par[i] == src) {
					if(nlink[i][1] != 0) {
						if(par[i-1] == snk || par[i-1] == src) {
							graph[i+1].add(new dinic.Edge(i, graph[i+1-1].size(), nlink[i][1]));
							graph[i+1-1].add(new dinic.Edge(i+1, graph[i+1].size() - 1, nlink[i-1][0]));
						} else {
							neighbors += nlink[i][1];
						}
					
					}
					
					if(nlink[i][3] != 0) {
						if(par[i-width] == snk || par[i-width] == src) {
							graph[i+1].add(new dinic.Edge(i+1-width, graph[i+1-width].size(), nlink[i][3]));
							graph[i+1-width].add(new dinic.Edge(i+1, graph[i+1].size() - 1, nlink[i-width][2]));
						} else {
							neighbors += nlink[i][3];
						}
					
					}
				graph[0].add(new dinic.Edge(i+1, graph[i+1].size(), tlink[src][i] + neighbors));
		    	graph[i+1].add(new dinic.Edge(0, graph[0].size()-1, 0));
				graph[i+1].add(new dinic.Edge(graph.length -1, graph[graph.length -1].size(), tlink[snk][i] + neighbors));
				graph[graph.length-1].add(new dinic.Edge(i+1, graph[i+1].size()-1, 0));
		    }
		    else {
		    	graph[i].add(new dinic.Edge(i, graph[i].size(), 0));
		    	
		    }
	  }
  }
  
  
  public static void recut(List<dinic.Edge>[] graph, int [] par, int src, int snk) {
      for(int i = 0; i < graph[0].size(); i++) {
    	  if(graph[0].get(i).t != 0 && graph[0].get(i).t != graph.length-1) {
    		  if(graph[0].get(i).cap - graph[0].get(i).f != 0) par[graph[0].get(i).t -1]= src;
		  }
      }
      for(int i = 0; i < graph[graph.length-1].size(); i++) {
    	  if(graph[0].get(i).t != 0 && graph[0].get(i).t != graph.length-1) {
	    	  if(graph[graph[graph.length-1].get(i).t].get(graph[graph.length-1].get(i).rev).cap - graph[graph[graph.length-1].get(i).t].get(graph[graph.length-1].get(i).rev).f!= 0) 
	    		  par[graph[graph.length-1].get(i).t-1]= snk;
    	  }
	  }
  }

  
  public static void mincut(int[][] nlink, int[][] tlink, int [] par) {
	  System.out.println("mincut");
	  int diff = Integer.MAX_VALUE;
	  int iter_max = 40;
	  int cost = cost(nlink, tlink, par);
	  System.out.print("Initial partition cost is "); System.out.println(cost); 
	  while(Math.abs(diff) > 10 || iter_max>=0) {
		  System.out.print("iteration "); System.out.println(iter_max);
		  int i, j;
		  do{
			  i = (int)(Math.random() * (double)labels.length);
			  j = (int)(Math.random() * (double)labels.length); 
		  } while((i==src && j==snk) || (i==snk && j==src) || (i==j));
		  src = i;
		  snk = j;
		  System.out.print(labels[i]);   System.out.print(" "); 	System.out.println(labels[j]);
          int V = 0;
		  for(i = 0; i < nlink.length; i++) {
			    if(par[i] == snk || par[i] == src) {
			    	V++;
			    }
		  }
		  if(V==0) {
			  System.out.println("No edges found, restart this iteration");
			  continue;
		  }
		  System.out.print(V); System.out.println(" nodes connected to the terminals");
		  List<dinic.Edge>[] graph = dinic.createGraph(nlink.length + 2);
	      loadGraph(graph, nlink, tlink, par, src, snk);
		  int flow = dinic.maxFlow(graph, 0, graph.length-1);
		  System.out.print("flow of the sub partition: "); System.out.println(flow);
		  int [] par_temp = new int[par.length];
		  for(int e=0; e<par.length; e++) par_temp[e]=par[e];
		  recut(graph, par_temp, src, snk);
		  int new_cost = cost(nlink, tlink, par_temp);
		  diff = cost - new_cost;
		  System.out.print("new partition cost is "); System.out.println(new_cost); 
		  System.out.print("cost down "); System.out.println(diff); 
		  if(diff>0) {
			  for(int e=0; e<par.length; e++) par_temp[e]=par[e];
			  cost = new_cost; 
			  write("temp/"+(iter_max)+".pgm", par);
		  } else {
			  System.out.println("no cost down, ignore this cut");
		  }
		  iter_max--;
	  }
	  System.out.println("holy");
	 
	  
  }
  //1111111111111111
  public static void write(String outFile, int[]par)
  {   
  	System.out.println("Start writing " + outFile);

      try
      {
          FileWriter writer = new FileWriter(outFile);
          BufferedWriter buffer = new BufferedWriter(writer);
          buffer.write("P2\n");
          buffer.write(width + " " + length + "\n");
          buffer.write(max + "\n");
         for(int i =0; i<par.length;i++)
          {
      	   if(i!= 0 && i%(width) == 0)	buffer.write("\n");
           buffer.write(Integer.toString(labels[par[i]]) + " ");
             //else buffer.write(org[i/length][i%length] + " ");
             //else if(data[snk][i] != 0)	buffer.write(org[i/length][i%length] + " ");
             //else	System.out.println("Missing Data = " + i);
           }
          buffer.close();
      }
      catch(Exception e)
      {
          System.out.println("An error occurred " + e.getMessage());
      }
      System.out.println("output " + outFile);
  }
  
  
  public static int[][] read(String filename, int[] config)
  {
      //long start = System.currentTimeMillis();

      int line_index = 1;
      int row = 0;
      int col = 0;
      int[][] pgm = null;

      try
      {
          FileReader reader = new FileReader(filename);
          BufferedReader buffer = new BufferedReader(reader);  
          String line = buffer.readLine();
          while(line != null){
        	  Matcher m = Pattern.compile("\\s+|\t|\\s|\n").matcher(line);
              String[] line_content = null;
              line_content = m.replaceAll(" ").split(" ");
              //System.out.println(m.replaceAll(" "));
              if(config[0]==0 && config[1]==0 && line_content.length == 2 && !line_content[0].trim().equals("#"))
              {
            	  
              	config[0] = Integer.parseInt(line_content[0].trim());
              	System.out.println(config[0]);
              	config[1] = Integer.parseInt(line_content[1].trim());
              	System.out.println(config[1]);
                pgm = new int[config[1]][config[0]];
              }
             else if(config[2]==0 && line_content.length ==1 && line_index>=3)
             {
              	config[2] = Integer.parseInt(line_content[0].trim());
              	System.out.println(config[2]);
             }
             else
              {
                  for(int i = 0; i < line_content.length; i++)
                   {
                	  if(line_content[i].trim().equals("")) continue;
                      if(col >= config[0])
                       {
                           col = 0;
                           row++;
                       }
                      if(col < config[0] && row < config[1]){
                          pgm[row][col] = Integer.parseInt(line_content[i].trim());
                       }
                      col++;
                   }
               } 
 
             line = buffer.readLine();
             line_index++;
         }
      }
      catch(FileNotFoundException ex)
      {
          System.out.println("file doesn't exist");
          System.exit(1);
         
      }
      catch(IOException ex)
      {
          System.out.println("file doesn't exist");
          System.exit(1);
      }
      
     System.out.println(pgm.length + " x " + pgm[0].length);
      return pgm;       
  }

  
  public static void write_pgm(String outFile, int[][] pgm)
  {   
  	System.out.println("Start writing " + outFile);

      try
      {
          FileWriter writer = new FileWriter(outFile);
          BufferedWriter buffer = new BufferedWriter(writer);
          buffer.write("P2\n");
          buffer.write(width + " " + length + "\n");
          buffer.write(max + "\n");
         for(int i =0; i<pgm.length;i++) {
         	for(int j = 0; j<pgm[i].length; j++)
         	{
         		buffer.write(Integer.toString(pgm[i][j]) + " ");
	        }
         buffer.write("\n");
      	 }
          buffer.close();
      }
      catch(Exception e)
      {
          System.out.println("An error occurred " + e.getMessage());
      }
      System.out.println("output " + outFile);
  }
  
  // Usage example
  public static void main(String[] args) {
   
    
    long start = System.currentTimeMillis();
    if(args.length == 0) {
    	args = new String[2];
    	args[0] =  "balloons_noisy.ascii.pgm";
    }
    int[][] graph_mat = null;
    String fileName = "images/"+args[0];
    if(fileName.substring(fileName.length()-3).equalsIgnoreCase("pgm"))
    {  
        int[] config = {0, 0, 0};
        int [][] pgm = read(fileName, config);
        //////////////////
        width = config[0];
        length = config[1];
        max = config[2];
        //write_pgm("lena1.pgm", pgm);
        System.out.println("lenth:"+Integer.toString(length) + " width:" + Integer.toString(width) + " max:" + Integer.toString(max));
        int total = length * width + labels.length;
        int[][] nlink = new int[length*width][4];
        int[][] tlink = new int [labels.length][length*width];
        int [] par = new int[length*width]; 
        
        edge_initial(pgm, nlink, tlink, max);
        pgm = null;
        cut_initial(nlink, tlink, par);
        
        mincut(nlink, tlink, par);
        
        
        
        long end = System.currentTimeMillis();
        System.out.print(end-start);
        System.out.println("ms");
        fileName = "images/" + "output_"+args[0];  
        System.out.println("Ready for writing");
        write(fileName, par);
 //       System.out.println("Ford Fulkerson Max flow = " + ford_fulkerson.fordFulkerson(graph_mat)); 
 //       fileName = args[1];
 //       ford_fulkerson.write(fileName, length, width, max, graph_mat, pgm);
    }
    else
    {
        System.out.println("Please provide a .pgm file");
    }
  }
}
