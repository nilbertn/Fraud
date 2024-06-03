import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;
import java.util.LinkedList;

public class BoostingAlgorithm {
    private int[][] input; // n reduced transaction
    private double[] weights; // current weight
    private int[] labels; // labels for inputs
    private Clustering cluster; // clustering
    private LinkedList<WeakLearner> wl; // linked list with weak learners data

    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        if (input == null || labels == null || locations == null)
            throw new IllegalArgumentException("null argument found");
        if (input.length != labels.length)
            throw new IllegalArgumentException("incompatible length");
        if (locations.length <= 0)
            throw new IllegalArgumentException("error");
        if (k < 1 || k > locations.length)
            throw new IllegalArgumentException("k is outside the desired range");

        this.input = new int[input.length][k];
        this.labels = labels.clone();
        this.weights = new double[input.length];
        Arrays.fill(this.weights, 1.0 / input.length);  // initialize weights

        // reduce the dimensions of input array
        this.cluster = new Clustering(locations, k);
        for (int i = 0; i < input.length; i++) {
            this.input[i] = cluster.reduceDimensions(input[i]);
        }
        this.wl = new LinkedList<>(); // create new array list of the weak learners
    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        return weights[i];
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        // create new weak learner
        WeakLearner learner = new WeakLearner(input, weights, labels);
        // double weight if the weak learner mislabels it.
        for (int i = 0; i < input.length; i++) {
            int prediction = learner.predict(input[i]);
            if (prediction != labels[i]) {
                weights[i] *= 2.0;
            }
        }

        // re-normalize the weight array, to sum to 1
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i];
        }
        for (int j = 0; j < weights.length; j++) {
            weights[j] /= sum;
        }
        wl.add(learner);
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null)
            throw new IllegalArgumentException("null sample");
        int[] rd = cluster.reduceDimensions(sample); // reduce sample size
        // count number of times model predicts 0 or 1
        int countZero = 0;
        int countOne = 0;
        // int[] count = new int[2];

        for (WeakLearner x : wl) {
            int prediction = x.predict(rd);
            if (prediction == 0) {
                countZero++;
            }
            else {
                countOne++;
            }
        }

        // majority prediction with tie being 0
        if (countZero >= countOne) return 0;
        else return 1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Stopwatch stopwatch = new Stopwatch();
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int t = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();

        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(trainingInput, trainingLabels,
                                                        trainingLocations, k);
        for (int i = 0; i < t; i++)
            model.iterate();

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.getN(); i++)
            if (model.predict(trainingInput[i]) == trainingLabels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.getN();

        // calculate the test data set accuracy
        double testAccuracy = 0;
        for (int i = 0; i < testing.getN(); i++)
            if (model.predict(testingInput[i]) == testingLabels[i])
                testAccuracy += 1;
        testAccuracy /= testing.getN();
        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model: " + testAccuracy);

        // call the model weight of method with argument 1
        StdOut.println(model.weightOf(1));

        StdOut.println("time elapsed: " + stopwatch.elapsedTime());
    }
}

