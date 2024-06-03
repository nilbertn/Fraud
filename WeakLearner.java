import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class WeakLearner {
    // create a dimension predictor dp, a value predictor vp and a sign predictor sp
    private int dp, vp, sp;
    private int k; // dimensions

    // Helper class with three instance variables: a value, a weight and a label.
    // and a compareTo method in order to sort the weights array.
    private static class DataPoint implements Comparable<DataPoint> {
        int value, label; // value and label
        double weight; // weight

        // initialize
        DataPoint(int value, int label, double weight) {
            this.value = value;
            this.label = label;
            this.weight = weight;
        }

        // compare method two for sorting data points in WeakLearner.
        public int compareTo(DataPoint other) {
            return Integer.compare(this.value, other.value);
        }
    }

    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        if (input == null || weights == null || labels == null)
            throw new IllegalArgumentException("null argument found");
        if (input.length != weights.length || input.length != labels.length)
            throw new IllegalArgumentException("incompatible argument lengths");
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] < 0)
                throw new IllegalArgumentException("weight " + i + "is non negative");
            if (!(labels[i] == 0 || labels[i] == 1))
                throw new IllegalArgumentException("invalid label");
            /* if (input[i] == null)
                    throw new IllegalArgumentException("input " + i + "is null");
            // not sure if we need this check
             */
        }
        int n = input.length;
        this.k = input[0].length;
        double best = Double.NEGATIVE_INFINITY;

        // test dimensions to find best decision stump.
        for (int d = 0; d < k; d++) {
            DataPoint[] dataPoints = new DataPoint[n];
            for (int i = 0; i < n; i++) {
                dataPoints[i] = new DataPoint(
                        input[i][d], labels[i], weights[i]);
            }
            Arrays.sort(dataPoints);

            // maintain two pointers and only consider unique vp values
            double w0 =
                    Arrays.stream(dataPoints).filter(dim -> dim.label == 0).
                          mapToDouble(dim -> dim.weight).sum();
            double w1 =
                    Arrays.stream(dataPoints).filter(dim -> dim.label == 1).
                          mapToDouble(dim -> dim.weight).sum();

            double sum0 = 0.0, sum1 = 0.0;

            // for (int i = 0; i < n; i++)
            int i = 0;
            while (i < n) {
                int v = dataPoints[i].value;

                // find the next point where the value changes
                int j = i;
                while (j < n && dataPoints[j].value == v) {
                    if (dataPoints[j].label == 0)
                        sum0 += dataPoints[j].weight;
                    else sum1 += dataPoints[j].weight;
                    j++;
                }

                // evaluate prediction at each vp with 0 and 1 signs
                double p0 = sum0 +
                        (w1 - sum1);
                double p1 = sum1 +
                        (w0 - sum0);

                if (p0 > best) {
                    best = p0;
                    dp = d;
                    vp = v;
                    sp = 0;  // below vp
                }
                if (p1 > best) {
                    best = p1;
                    dp = d;
                    vp = v;
                    sp = 1;  //
                }

                i = j; // jump to last occurrence of current value
            }
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null)
            throw new IllegalArgumentException("Sample is null");
        // dimension check
        if (sample.length != k)
            throw new IllegalArgumentException("incompatible sample length");
        // case 1: maintain swap prediction sp = 0 when [dp]<= vp
        if (sp == 0) {
            if (sample[dp] <= vp) return 0;
            else return 1;
        }
        // case 2: swap prediction sp = 1 when [dp]<= vp
        else {
            if (sample[dp] <= vp) return 1;
            else return 0;
        }
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return dp;
    }

    // return the value the learner uses to separate the data
    public int valuePredictor() {
        return vp;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return sp;
    }

    // unit testing (required)
    public static void main(String[] args) {
        /* int[][] input = { { 1, 2 }, { 2, 3 }, { 3, 1 } };
        double[] weights = { 0.1, 0.5, 0.3 };
        int[] labels = { 1, 0, 1 };

        WeakLearner learner = new WeakLearner(input, weights, labels);
        int[] testSample = { 2, 2 };
        // calculate accuracy of data set
        int prediction = learner.predict(testSample);
        StdOut.println("Predict " + prediction);
        // use the different predictions types
        StdOut.println("Dimension prediction: " + learner.dimensionPredictor());
        StdOut.println("Threshold prediction: " + learner.valuePredictor());
        StdOut.println("Sign prediction: " + learner.signPredictor());
         */
        In datafile = new In(args[0]);

        int n = datafile.readInt();
        int k = datafile.readInt();

        int[][] input = new int[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                input[i][j] = datafile.readInt();
            }
        }

        int[] labels = new int[n];
        for (int i = 0; i < n; i++) {
            labels[i] = datafile.readInt();
        }

        double[] weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = datafile.readDouble();
        }

        WeakLearner weakLearner = new WeakLearner(input, weights, labels);
        StdOut.printf("vp = %d, dp = %d, sp = %d\n", weakLearner.valuePredictor(),
                      weakLearner.dimensionPredictor(), weakLearner.signPredictor());
        StdOut.println(weakLearner.predict(labels));

    }
}
