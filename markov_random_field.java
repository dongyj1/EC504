
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;



public class markov_random_field {
  private static final int src_over_snk = 4;
  private static final int scale = 4;
  private static final int [] labels = {0, 40, 60, 90, 160, 255};
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
			  nlink[node][node + 1]= (max - Math.abs(pgm[i][j] - pgm[i][j+1]))/scale; 
			  //nweights += nlink[node][node + 1];
		  }
		  if(j > 0)
		  {
			  nlink[node][node - 1]= (max - Math.abs(pgm[i][j] - pgm[i][j-1]))/scale; 
			                
			  //nweights += nlink[node][node - 1];
		  }
		  if(i < length-1 )
		  {
			  nlink[node][node + width]= (max - Math.abs(pgm[i][j] - pgm[i+1][j]))/scale;
			  //nweights += nlink[node][node + width];
		  }
		  if(i > 0 )
		  {
			  nlink[node][node - width]= (max - Math.abs(pgm[i][j] - pgm[i-1][j]))/scale;
			  
			  //nweights += nlink[node][node - width];
		  }
		  for(int k = 0; k < labels.length; k++) {
			  tlink[k][node] = (int) (((double)src_over_snk) *( (double) max) * Math.exp(-Math.abs(pgm[i][j] - labels[k])/((double) max)));
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
	  		 int min = Integer.MIN_VALUE;
	  		 int label = 0;
	  		 for(int k = 0; k < tlink.length; k++) {
	  			 if(min < tlink[k][i * width + j]) {
	  				 min = tlink[k][i * width + j];
	  				 label = k;
	  			 }
	  		 } 
	  		 par[i * width + j] = label;
	  		 System.out.print(label); System.out.print(" ");
	  	   }
	  	  System.out.println("");
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
				  if(par[i*width+j]!=par[i*width+j+1]) cost+=nlink[i*width+j][i*width+j+1];
			  }
			  if(j > 0)
			  {
				  if(par[i*width+j]!=par[i*width+j-1]) cost+=nlink[i*width+j][i*width+j-1];
				                
			  }
			  if((i < length-1 ))
			  {
				  if(par[i*width+j]!=par[(i+1)*width+j]) cost+=nlink[i*width+j][(i+1)*width+j];
			  }
			  if((i > 0 ))
			  {
				  if(par[i*width+j]!=par[(i-1)*width+j]) cost+=nlink[i*width+j][(i-1)*width+j];
			  }
	  	   }
	  }
	 return cost;
	  
  }
  
  public static void loadGraph(List<dinic.Edge>[] graph, int[][] nlink, int[][] tlink, int [] par, int src, int snk) {
	  for(int i = 0; i < nlink.length; i++) {
		  	int neighbors = 0;
		    if(par[i] == snk || par[i] == src) {
				for(int j = 0; j < i; j++) {
					if(nlink[i][j] != 0) {
						if(par[j] == snk || par[j] == src) {
							graph[i+1].add(new dinic.Edge(j+1, graph[j+1].size(), nlink[i][j]));
							graph[j+1].add(new dinic.Edge(i+1, graph[i+1].size() - 1, nlink[j][i]));
						} else {
							neighbors += nlink[i][j];
						}
					}
				}
				graph[0].add(new dinic.Edge(i+1, graph[i+1].size(), tlink[src][i] + neighbors));
		    	graph[i+1].add(new dinic.Edge(0, graph[0].size()-1, tlink[src][i] + neighbors));
				graph[i+1].add(new dinic.Edge(graph.length -1, graph[graph.length -1].size(), tlink[snk][i] + neighbors));
				graph[graph.length-1].add(new dinic.Edge(i+1, graph[i+1].size()-1, tlink[snk][i] + neighbors));
		    }
	  }
  }
  
  
  public static void recut(List<dinic.Edge>[] graph, int[][] nlink, int[][] tlink, int [] par, int src, int snk) {
      for(int i = 0; i < graph[0].size(); i++) {
		  if(graph[0].get(i).cap - graph[0].get(i).f == 0) par[graph[0].get(i).t-1] = snk;
		  else par[graph[0].get(i).t-1] = src;
      }
     
  }
  
  public static void mincut(int[][] nlink, int[][] tlink, int [] par) {
	  System.out.println("mincut");
	  int diff = Integer.MAX_VALUE;
	  int iter_max = 1000;
	  int cost = cost(nlink, tlink, par);
	  
	  while(Math.abs(diff) > 10 && iter_max!=0) {
		  System.out.println("iteration "); System.out.println(iter_max);
		  System.out.print("Current cut "); System.out.println(cost);
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
		  if(V==0) continue;
		  System.out.print(V); System.out.println(" nodes connected to the terminals");
		  List<dinic.Edge>[] graph = dinic.createGraph(V + 2);
	      loadGraph(graph, nlink, tlink, par, src, snk);
		  int flow = dinic.maxFlow(graph, 0, graph.length-1);
		  System.out.print("flow of the sub partition: "); System.out.println(flow);
		  recut(graph, nlink, tlink, par, src, snk);
		  int new_cost = cost(nlink, tlink, par);
		  diff = cost - new_cost;
		  System.out.print("new partition cost is "); System.out.println(new_cost); 
		  System.out.print("cost down "); System.out.println(diff); 
		  cost = new_cost; 
		  iter_max--;
	  }
	  System.out.println("holy");
	 
	  
  }
  
  
  // Usage example
  public static void main(String[] args) {
   
    
    long start = System.currentTimeMillis();
    args = new String[2];
    args[0] = "IMG_2208.pgm";
    args[1] = "k_segmentation.pgm";
    int[][] graph_mat = null;
    String fileName = args[0];
    if(fileName.substring(fileName.length()-3).equalsIgnoreCase("pgm"))
    {  
        int[] config = new int[3];
        int [][] pgm = dinic.read(fileName, config);
        width = config[0];
        length = config[1];
        max = config[2];
        System.out.println("lenth:"+Integer.toString(length) + " width:" + Integer.toString(width) + " max:" + Integer.toString(max));
        int total = length * width + labels.length;
        int[][] nlink = new int[length*width][length*width];
        int[][] tlink = new int [labels.length][length*width];
        int [] par = new int[length*width]; 
        
        edge_initial(pgm, nlink, tlink, max);
    
        cut_initial(nlink, tlink, par);
        
        mincut(nlink, tlink, par);
        
        
        
        long end = System.currentTimeMillis();
        System.out.print(end-start);
        System.out.println("ms");
        int[] graph_out = new int[total];
        fileName = args[1];  
        System.out.println("Ready for writing");
        dinic.write(fileName, length, width, max, graph_out, pgm);
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
