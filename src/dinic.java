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


public class dinic {
  private static final int src_over_snk = 4;
  private static final int scale = 4;
  private static final int src = 0;
  static class Edge {
    int t, rev, cap, f;

    public Edge(int t, int rev, int cap) {
      this.t = t; //the target node  
      this.rev = rev; //this edge is the reversion of the revth edge in the edge list of target node t
      this.cap = cap; // the capacity of this edge
    }
  }

  public static List<Edge>[] createGraph(int nodes) {
    List<Edge>[] graph = new List[nodes];
    for (int i = 0; i < nodes; i++)
      graph[i] = new ArrayList<>();
    return graph;
  }

  public static void addEdge(List<Edge>[] graph, int s, int t, int cap) {
    graph[s].add(new Edge(t, graph[t].size(), cap));
    graph[t].add(new Edge(s, graph[s].size() - 1, 0));
  }
  
  public static void addGraph(List<Edge>[] graph, int[][] graph_mat) {
	for(int i = 0; i < graph_mat.length; i++) {
		for(int j = i; j < graph_mat[i].length; j++) {
			if(graph_mat[i][j] != 0) {
				graph[i].add(new Edge(j, graph[j].size(), graph_mat[i][j]));
				graph[j].add(new Edge(i, graph[i].size() - 1, graph_mat[i][j]));
			}
		}
	}
  }
  

  static boolean dinicBfs(List<Edge>[] graph, int src, int dest, int[] dist) {
    Arrays.fill(dist, -1);
    dist[src] = 0;
    int[] Q = new int[graph.length]; //a queue to store the searched
    int sizeQ = 0;
    Q[sizeQ++] = src; // add source to the queue
    for (int i = 0; i < sizeQ; i++) {
      int u = Q[i]; //start from the last node which has been searched
      for (Edge e : graph[u]) { //edge e from node u
        if (dist[e.t] < 0 && e.f < e.cap) {  //e's target hasn't been visited, then set to be u's next
          dist[e.t] = dist[u] + 1; //distance of the target from source is 1 unit further than u
          Q[sizeQ++] = e.t; // add this searched target to the queue
        }
      }
    }
    return dist[dest] >= 0; //check if there is a path from source to sink
  }

  static int dinicDfs(List<Edge>[] graph, int[] ptr, int[] dist, int dest, int u, int f) {
    if (u == dest)
      return f;
    for (; ptr[u] < graph[u].size(); ++ptr[u]) {
      Edge e = graph[u].get(ptr[u]);
      if (dist[e.t] == dist[u] + 1 && e.f < e.cap) {  //don't need to be the nodes in the BFS queue, it's OK if it's neighbor and the distance is 1 unit further
        int df = dinicDfs(graph, ptr, dist, dest, e.t, Math.min(f, e.cap - e.f));
        if (df > 0) {
          e.f += df;
          graph[e.t].get(e.rev).f -= df;
          return df;
        }
      }
    }
    return 0;
  }

  public static int maxFlow(List<Edge>[] graph, int src, int dest) {
    int flow = 0;
    int[] dist = new int[graph.length];
    while (dinicBfs(graph, src, dest, dist)) {
      int[] ptr = new int[graph.length];
      while (true) {
        int df = dinicDfs(graph, ptr, dist, dest, src, Integer.MAX_VALUE);
        if (df == 0)
          break;
        flow += df;
      }
    }
    return flow;
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
        	  Matcher m = Pattern.compile("\\s+|\t|\\s").matcher(line);
              String[] line_content = null;
              line_content = m.replaceAll(" ").split(" ");
              //System.out.println(m.replaceAll(" "));
              if(config[0]==0 && config[1]==0 && line_content.length == 2)
              {
            	  
              	config[0] = Integer.parseInt(line_content[0].trim());
              	config[1] = Integer.parseInt(line_content[1].trim());
                pgm = new int[config[1]][config[0]];
              }
             else if(config[2]==0 && line_content.length ==1 && line_index>=3)
             {
              	config[2] = Integer.parseInt(line_content[0].trim());
             }
             else
              {
                  for(int i = 0; i < line_content.length; i++)
                   {
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

  public static void edge_matrix(int[][] pgm, int[][] edgs, int length, int width, int max)
  {
      //long start = System.currentTimeMillis();
      int node = 1;
      int sum = 0;
     for(int i =0; i< pgm.length; i++){
  	   for(int j = 0; j< pgm[0].length; j++){
              sum += pgm[i][j];
              if(j < length-1)
              {
              	edgs[node][node + 1] = (max - Math.abs(pgm[i][j] - pgm[i][j+1]))/scale;   
              }
              if(j > 0)
              {
                  edgs[node][node - 1] = (max - Math.abs(pgm[i][j] - pgm[i][j-1]))/scale;               

              }
              if((i < width -1 ))
              {
              	edgs[node][node + length] = (max - Math.abs(pgm[i][j] - pgm[i+1][j]))/scale; 
              }
              if((i > 0 ))
              {
                  edgs[node][node - length] = (max - Math.abs(pgm[i][j] - pgm[i-1][j]))/scale;

              }
              node++;           
          }
      }
     
      int avg = sum/node;
     	node = 1;
      for(int i =0; i< pgm.length; i++)
      {
          for(int j = 0; j< pgm[0].length; j++)
          {
              if(pgm[i][j]>= (avg))
              {
                 edgs[node][edgs.length - 1] = src_over_snk * max;
                 edgs[src][node] = max;
              } else {
                edgs[node][edgs.length - 1] = max;
                edgs[src][node] = max * src_over_snk;
              }
              node++;
          }
       }
  }
 
  
  public static void write(String outFile, int length, int width, int max, int[]data, int [][]org)
  {   
  	System.out.println("Start writing " + outFile);
      long start = System.currentTimeMillis();
  
      //get the src and sink
      int src = 0 ;
      int snk = 1;
      
      try
      {
          FileWriter writer = new FileWriter(outFile);
          BufferedWriter buffer = new BufferedWriter(writer);
          buffer.write("P2\n");
          buffer.write(length + " " + width + "\n");
          buffer.write(max + "\n");
         for(int i =1; i<(data.length-2);i++)
          {
      	   if(i!= 1 && i%(length) == 0)	buffer.write("\n");
             if(data[i] == 0)	buffer.write("0 ");
             else buffer.write(max + " ");
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
  
  
  // Usage example
  public static void main(String[] args) {
    List<Edge>[] graph = createGraph(3);
    addEdge(graph, 0, 1, 3);
    addEdge(graph, 0, 2, 2);
    addEdge(graph, 1, 2, 2);
    System.out.println(maxFlow(graph, 0, 2));
    
    
    long start = System.currentTimeMillis();
    args = new String[2];
    args[0] = "IMG_7734.pgm";
    args[1] = "Dinic.pgm";
    int[][] graph_mat = null;
    String fileName = args[0];
    if(fileName.substring(fileName.length()-3).equalsIgnoreCase("pgm"))
    { 
        int[] config = new int[3];
        int [][] pgm = read(fileName, config);
        int length = config[0];
        int width = config[1];
        int max = config[2];
        int total = length * width + 2;
        graph_mat = new int[total][total];
        edge_matrix(pgm, graph_mat, length, width, max);
        //ImageSegmentation.createAdjMatrix(pgm, graph, length, width, max);
        
        graph = createGraph(graph_mat.length);
        addGraph(graph, graph_mat);
        System.out.println("Dinic Max flow = " + maxFlow(graph, 0, graph_mat.length-1));
        long end = System.currentTimeMillis();
        System.out.print(end-start);
        System.out.println("ms");
        int[] graph_out = new int[total];
        for(int i = 0; i < total-2 ; i++) {
  		  graph_out[i] = graph[0].get(i).cap - graph[0].get(i).f;
  		  //System.out.println(graph_out[i]);
  		  //graph_out[1][graph.length-1] = graph[graph[graph.length-1].get(i).t].get(graph[graph.length-1].get(i).rev).cap - graph[graph[graph.length-1].get(i).t].get(graph[graph.length-1].get(i).rev).f;
  		 }

        fileName = args[1];  
        System.out.println("Ready for writing");
        write("Dinic2.pgm", length, width, max, graph_out, pgm);
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
