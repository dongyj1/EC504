package src;

public class push_relabel_v1 {

  int[][] cap;
  int[][] f;

  public void init(int nodes) {
    cap = new int[nodes][nodes];

  }

  public void addEdge(int s, int t, int capacity) {
    cap[s][t] = capacity;
  }

  public int maxFlow(int s, int t) {
    int n = cap.length;
    int[] h = new int[n];
    h[s] = n - 1; //height of source

    int[] maxh = new int[n]; //height rank from low to 
    quick_sort ob = new quick_sort();
    f = new int[n][n]; //flow matrix
    int[] e = new int[n]; //excess flow

    for (int i = 0; i < n; ++i) {
      f[s][i] = cap[s][i];
      f[i][s] = -f[s][i];
      e[i] = cap[s][i]; //only nodes connected with source have excess flow
    }

    for (int sz = 0;;) {
      if (sz == 0) {
        for (int i = 0; i < n; ++i) //search all nodes
          if (i != s && i != t && e[i] > 0) { //if not source/sink nodes have excess flow
            maxh[sz++] = i; //highest node and nodes after it will be stored in the maxh, nodes before the highest is gone
          }//maxh[sz] is empty, maxh[sz-1] has value
      }
      if (sz == 0)
        break;//if sz is still 0, no node have excess flow, exit algorithm
      ob.sort(maxh, 0, maxh.length-1);    
      while (sz != 0) { //until reach the highest node
        int i = maxh[sz-1]; //begin from the last one in maxh
        boolean pushed = false; //reset push flag
        for (int j = n-1; j >=0 && e[i] > 0; --j) {	//search all nodes if this node still has excess flow
          if (h[i] == h[j] + 1 && cap[i][j] - f[i][j] > 0) { //find a neighbor node that is 1 lower
            int df = Math.min(cap[i][j] - f[i][j], e[i]);  //push flow, either no excess flow or (i,j) is full
            f[i][j] += df; //i,j flow increases
            f[j][i] -= df; //j, i flow decreases
            e[i] -= df; //excess flow in i decreases
            e[j] += df; //excess flow in j increases
            if (e[i] == 0)
              --sz; //if the excess flow is 0, find next node in maxh
            pushed = true; //a push happened
          }
        }
        if (!pushed) {  //no push happened for i, but i surely has excess flow, this means that i needs to be relabeled
          h[i] = Integer.MAX_VALUE; //set h the highest
          for (int j = 0; j < n; ++j)
            if (h[i] > h[j] + 1 && cap[i][j] - f[i][j] > 0) //check if i=min(j+1|(i,j) in e)
              h[i] = h[j] + 1;
          if (h[i] > h[maxh[0]]) { //if i becomes the highest, need to re-choose the highest
            sz = 0;
            break;
         }
        }
      }
    }

    int flow = 0;
    for (int i = 0; i < n; i++)
      flow += f[s][i];

    return flow;
  }

  // Usage example
  public static void main(String[] args) {
    int[][] capacity = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
    int n = capacity.length;
    push_relabel flow = new push_relabel();
    flow.init(n);
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        if (capacity[i][j] != 0)
          flow.addEdge(i, j, capacity[i][j]);
    System.out.println(4 == flow.maxFlow(0, 2));
    
    long start = System.currentTimeMillis();
    args = new String[2];
    args[0] = "IMG_7734.pgm";
    args[1] = "push_relabel.pgm";
    int[][] graph_mat = null;
    String fileName = args[0];
    if(fileName.substring(fileName.length()-3).equalsIgnoreCase("pgm"))
    { 
        int[] config = new int[3];
        int [][] pgm = ford_fulkerson.read(fileName, config);
        int length = config[0];
        int width = config[1];
        int max = config[2];
        int total = length * width + 2;
        flow = new push_relabel();
        flow.init(total);
        graph_mat = new int[total][total];
        ford_fulkerson.edge_matrix(pgm, graph_mat, length, width, max);
        //ImageSegmentation.createAdjMatrix(pgm, graph, length, width, max);
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < total; j++) {
              if (graph_mat[i][j] != 0) {
                flow.addEdge(i, j, graph_mat[i][j]);
              }
            }
        }
        System.out.println(flow.maxFlow(0, total-1));
        for (int i = 0; i < total; i++) {
        	for(int j = 0; j< total; j++) {
        	graph_mat[i][j]=flow.cap[i][j]-flow.f[i][j];
        	}
        }
        int parent[] = new int[total];
        System.out.println(ford_fulkerson.path(graph_mat, parent)); 
        long end = System.currentTimeMillis();
        System.out.print(end-start);
        System.out.println("ms");
    }
    else
    {
        System.out.println("Please provide a .pgm file");
    }
  }
}
