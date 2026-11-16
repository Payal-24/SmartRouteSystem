import java.util.*;
public class Dijkstra{
    static class Node{
        int vertex;
        int distance;
        Node(int vertex,int distance){
            this.vertex=vertex;
            this.distance=distance;
        }
    }
    public static int[] shortestPath(int n,List<List<Node>> graph,int source){
        int dist[]=new int[n];
        Arrays.fill(dist,Integer.MAX_VALUE);
        PriorityQueue<Node> pq=new PriorityQueue<>((a,b)->a.distance-b.distance);
        dist[source]=0;
        pq.add(new Node(source,0));
        while(!pq.isEmpty()){
            Node current=pq.poll();
            int u=current.vertex;
            if(current.distance>dist[u]){
                continue;
            }
            for(Node neighbor:graph.get(u)){
                int v=neighbor.vertex;
                int w=neighbor.distance;
                if(dist[u]+w<dist[v]){
                    dist[v]=dist[u]+w;
                    pq.add(new Node(v,dist[v]));
                }
            }
        }
        return dist;

    }
    public static void main(String args[]){
        int n=5;
        List<List<Node>> graph=new ArrayList<>();
        for(int i=0;i<n;i++){
            graph.add(new ArrayList<>());
        }
        graph.get(0).add(new Node(3,4));
        graph.get(0).add(new Node(2, 1));
        graph.get(2).add(new Node(1, 2));
        graph.get(1).add(new Node(3, 1));
        graph.get(2).add(new Node(3, 5));
        graph.get(3).add(new Node(4, 3));
        int []result=shortestPath(n,graph,0);
        System.out.println(Arrays.toString(result));
    }
}