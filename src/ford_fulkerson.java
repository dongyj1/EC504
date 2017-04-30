package src;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ford_fulkerson
{    
    private static final int src_over_snk = 4;
    private static final int scale = 2;
    private static final int src = 0;
    //long start = System.currentTimeMillis();
    public static void main(String[] args) 
    {
    	args = new String[2];
        args[0] = "DAISY_30.pgm";
        args[1] = "output_DAISY_30.pgm";
        int[][] graph = null;
        String fileName = args[0];
        if(fileName.substring(fileName.length()-3).equalsIgnoreCase("pgm"))
        { 
            int[] config = new int[3];
            int [][] pgm = read(fileName, config);
            int length = config[0];
            int width = config[1];
            int max = config[2];
            int total = length * width + 2;
            graph = new int[total][total];
            edge_matrix(pgm, graph, length, width, max);
            //ImageSegmentation.createAdjMatrix(pgm, graph, length, width, max);
            System.out.println("Max flow = " + fordFulkerson(graph)); 
            for (int i = 0; i < total; i++) {
                for (int j = 0; j < total; j++) {
                	System.out.print(graph[i][j]);
                	System.out.print(" ");
                }
                System.out.print("\n");
            }
            fileName = args[1];
            write(fileName, length, width, max, graph, pgm);
        }
        else
        {
            System.out.println("Please provide a .pgm file");
        }
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
                String[] line_content = null;
                if(line_index == 3) {
                  line_content = line.split(" ");
                }
                else{
                  line_content = line.split("\t");
                }
                if(line_index ==  3 && line_content.length >= 2)
                {
                	config[0] = Integer.parseInt(line_content[0].trim());
                	config[1] = Integer.parseInt(line_content[1].trim());
                    pgm = new int[config[1]][config[0]];
                }
               else if(line_index == 4 && line_content.length > 0)
               {
                	config[2] = Integer.parseInt(line_content[0].trim());
               }
                else if(line_index > 4)
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
   
    public static void write(String outFile, int length, int width, int max, int[][]data, int [][]org)
    {   
    	System.out.println("Start writing " + outFile);
        long start = System.currentTimeMillis();
    
        //get the src and sink
        int src = 0 ;
        int snk = data.length-1;
        
        try
        {
            FileWriter writer = new FileWriter(outFile);
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.write("P2\n");
            buffer.write(length + " " + width + "\n");
            buffer.write(max + "\n");
           for(int i =1; i<(data.length-1);i++)
            {
        	   if(i!= 1 && i%(length) == 0)	buffer.write("\n");
               if(data[src][i] != 0)	buffer.write("0 ");
               else if(data[i][snk] != 0)	buffer.write(org[i/length][i%length] + " ");
               else	System.out.println("Missing Data = " + i);
             }
            buffer.close();
        }
        catch(Exception e)
        {
            System.out.println("An error occurred " + e.getMessage());
        }
        System.out.println("output " + outFile);
    }
    
    public static int fordFulkerson(int[][] g)
    {
       
       int parent[] = new int[g.length];
       int max_flow = 0;
       int i, j;
       while(path(g, parent)) {
    	   
    	   int path_flow = Integer.MAX_VALUE;
    	   for(i = g.length-1; i!= 0; i = parent[i]) {
    		   j = parent[i];
    		   path_flow = Math.min(path_flow, g[j][i]);
    	   }
    	   for(i = g.length-1; i!= 0; i = parent[i]) {
    		   j = parent[i];
    		   g[j][i] -= path_flow;
    		   g[i][j] += path_flow;
    	   }
    	   max_flow += path_flow;
       }
        
      return max_flow;  
    }
    
   public static boolean path(int [][]g, int parent[]) {
    	int src = 0;
    	int des = g.length-1;
    	boolean visited[] = new boolean[g.length];
    	for(int i= 0; i<g.length; i++) {
    		visited[i] = false;
    	}
    	LinkedList<Integer> queue = new LinkedList<Integer>();
    	queue.push(src);
    	visited[src] = true;
    	parent[src] = -1;
    	while(!queue.isEmpty()) {
    		int j = queue.pop();
    		for(int i = 0; i< g.length; i++) {
    			if(visited[i] == false && g[j][i] > 0) {
    				queue.push(i);
    				parent[i] = j;
    				visited[i] = true;
    			}
    		}
    	}
    	return (visited[des] == true);
    }    
}

