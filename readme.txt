Programming Assignment 7: Fraud Detection


PLEASE READ THE COMMENTS SECTION AT THE END OF THIS READ ME
/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */
The clustering constructor organizes a given set of points into clusters
by constructing an MST.
The constructor takes an array of `Point2D` objects representing locations and an
integer `k` representing the number of desired clusters.
Using the  locations, a complete graph is constructed where each node is a location
and edges between nodes are weighted by the distance between the points.
Then, Kruskal's algorithm is applied to find the minimum spanning tree to get the
 minimum possible total edge weight.
Then, the algorithm removes the m-k smallest edges from the MST. This splits the
 tree into k connected components.

/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */
The `WeakLearner` constructor uses a decision stump algorithm with a focus on
maximizing the weighted accuracy of predictions.
The constructor iterates over each dimension of the input data. For each dimension,
it sorts the transaction summaries based on the values in that dimension.
Then, for each possible threshold the constructor calculates the weighted accuracy
for both 0 or 1. This involves summing the weights of correctly classified inputs
for both scenarios - reverse threshold and normal threshold.
Then, the decision stump parameters (`dp`, `vp`, `sp`) are updated whenever a
new configuration with higher weighted accuracy is found.

/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 *  (Note: if you implemented the constructor of WeakLearner in O(kn^2) time
 *  you should use the small_training.txt and small_test.txt datasets instead,
 *  otherwise this will take too long)
 **************************************************************************** */

      k          T         test accuracy       time (seconds)
   ----- ---------------------------------------------------------------------
        5       20          0.775               0.124
        5       30          0.7875              0.149
        5       80          0.775               0.251
        5       50          0.8                 0.192
        5       55          0.8                 0.19
        5       52          0.8                 0.196
        10      52          0.9125              0.302
        20      52          0.95                0.501
/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */
k = 20, T = 52, test accuracy = 0.95, time = 0.501
1. Since we used small txt and small training, we found that maximizing k
maximizes our test accuracy. Also, using k = 5, we found the optimal T value
by plotting where accuracy goes down and up.
2. Smaller T will lead to lower test accuracy since we run fewer iterations
that train our weak learner and boosting algorithm. Obviously, with more data,
the more accurate our predictions will become!
3. Small/Big k leads to lower test accuracy. Small k implies that each data point
has more weight in affecting the prediction. This is essentially underfitting,
such that the model is too generalized.
For Big K, the model will start to fit with the noise and outliers in the data set
(as more data means more outliers). Big k also means greater fluctuation in the
number of data points in each cluster, which may degrade the learning algorithm's
ability to make accurate predictions.

After solving th ecomplexity for knlogn time, we ran a few tests in order to
find out the difference and found the following test results
java-algs4 BoostingAlgorithm large_training.txt large_test.txt 50 100
Training accuracy of model: 0.9903125
Test accuracy of model: 0.9775
7.26836493708367E-7
time elapsed: 1.793
k = 50, T = 100, test accuracy = 0.9775, time = 1.793
when we deviated from the value of 100 the accuracy deviated thus this was the
true value that maximises accuracy
/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
We had two lab TAs look at our Boosting Algorithm class (which passes local tests
we ran on our terminal) and they couldn't figure out what was wrong. They both
thought that our implementation was correct, and I think our structure is
largely correct in general. Thus, I am unsure why we are not passing some
tests on tigerfile for the boosting algorithm and hope that we don't get
docked points for it!
/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
Debugging boosting and passing O(nklogn) timing on weaklearner.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
We had two lab TAs look at our Boosting Algorithm class (which passes local tests
we ran on our terminal) and they couldn't figure out what was wrong. They both
thought that our implementation was correct, and I think our structure is
largely correct in general. We followed all parts of the api and assignment
specifications correctly (in our eyes). Thus, I am unsure why we are not passing some
tests on tigerfile for the boosting algorithm and hope that we don't get
docked points for it (at least not too many)!
