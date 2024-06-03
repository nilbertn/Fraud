import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

public class Clustering {
    private int m; // locations
    private int k; // // clusters
    private int[] clusters; // array of clusters

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        if (locations == null)
            throw new IllegalArgumentException("locations cannot be null");
        this.m = locations.length;
        if (k < 1 || k > m) throw new IllegalArgumentException(
                "k must be between 1 and m");
        this.k = k;
        // not sure if we to throw a null argument individual or just do it for all
        /* for (int i = 0; i < m; i++) {
            if (locations[i] == null)
                throw new IllegalArgumentException("location at " + i + "is null");
        }

         */
        // edge weighted graph with all paired locations connected
        EdgeWeightedGraph ewg = new EdgeWeightedGraph(m);
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                // calculate euclidean distance
                double weight = locations[i].distanceTo(locations[j]);
                Edge edge = new Edge(i, j, weight);
                ewg.addEdge(edge);
            }
        }

        // compute the minimum spanning tree of the E.W.G using Kruskal's algorithm
        KruskalMST mst = new KruskalMST(ewg);

        // new cluster graph using mst with m-k lowest weight edges
        EdgeWeightedGraph cluster = new EdgeWeightedGraph(m); //
        int edges = 0;
        for (Edge e : mst.edges()) {
            if (edges < m - k) {
                cluster.addEdge(e);
                edges++;
            }
        }
        // connect clusters in graph with exactly k connected components
        // and store in array
        CC cc = new CC(cluster);
        clusters = new int[m];
        for (int i = 0; i < m; i++)
            clusters[i] = cc.id(i);
    }

    // return the cluster of the ith point
    public int clusterOf(int i) {
        if (i < 0 || i > m - 1)
            throw new IllegalArgumentException("poiint is out of desired range");
        return clusters[i];
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        if (input == null)
            throw new IllegalArgumentException("input is null");
        if (input.length != m)
            throw new IllegalArgumentException("input and clusters does not match");
        // calculate the total of each cluster
        int[] reduced = new int[k];
        for (int i = 0; i < m; i++) {
            reduced[clusterOf(i)] += input[i];
        }
        return reduced;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Point2D[] points = {
                new Point2D(0.1, 0.2),
                new Point2D(0.3, 0.2),
                new Point2D(0.4, 0.2),
                new Point2D(0.1, 0.5),
                new Point2D(0.2, 0.8),
                new Point2D(0.5, 0.5)
        };
        Clustering clustering = new Clustering(points, 3);
        int[] transactions = { 3, 4, 2, 3, 6, 2 };
        int[] reduced = clustering.reduceDimensions(transactions);
        // print all clusters
        StdOut.println("\nPrint cluster for each point");
        for (int i = 0; i < points.length; i++) {
            StdOut.println("Point " + (i + 1) + ": " + clustering.clusterOf(i));
        }
        // print reduced dimension
        StdOut.println("\nreduced dimension clusters");
        for (int i = 0; i < reduced.length; i++) {
            StdOut.println("Cluster " + (i + 1) + ": " + reduced[i]);
        }
        // The below are comments taken from ed discussion about the project
        // from Prof. Pedro Paredes

        // My recommendation is to take two arguments, where the first one is a file
        // name and the second one is an integer (k). Then read from the file name to
        // build the array of locations, following the format of the
        // princeton_locations.txt , which comes with an integer first
        // (m , the number of locations) and then m lines each with two doubles
        // separated by a space (the x and y coordinates of each point).
        /* In in = new In(args[0]);
        int k = Integer.parseInt(args[1]);
        int m = Integer.parseInt(StdIn.readLine());
        Point2D[] points = new Point2D[m];
        int count = 0;
        while (!in.isEmpty()) {
            String[] pt = in.readLine().split(" ");
            Point2D point = new Point2D(Double.parseDouble(pt[0]),
                                        Double.parseDouble(pt[1]));
            points[count] = point;
            count++;
        }
        Clustering clustering = new Clustering(points, k);
        StdOut.println("\nPrint cluster for each point");
        for (int i = 0; i < m; i++) {
            StdOut.println("Point " + (i + 1) + ": " + clustering.clusterOf(i));
        }
        int[] transactions = { 3, 4, 2, 3, 0, 2, 4, 3, 7, 4, 8, 3, 5, 6 };
        int[] reduced = clustering.reduceDimensions(transactions);
        StdOut.println("\nreduced dimension clusters");
        for (int i = 0; i < k; i++) {
            StdOut.println("Cluster " + (i + 1) + ": " + reduced[i]);
        }

         */

    }
}
