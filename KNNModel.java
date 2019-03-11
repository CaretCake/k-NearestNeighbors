import java.util.ArrayList;
import java.util.PriorityQueue;
import javafx.util.Pair;
import java.util.Comparator;
import java.util.HashMap;

public class KNNModel {
  private ArrayList<Feature> features;
	private ArrayList<ArrayList<Double>> dataInstances;

  /* Parses and stores feature names and data instances from training dataset .arff file for model
   *
   * @param filename string containing name of the .arff file containing the training dataset
   */
  public void train(String filename) {
      try {
      // parse data from filename
      ArffParser parser = new ArffParser();
      parser.setInputFilename(filename);
      parser.parseDataFromArffFile();
      // store it
      features = parser.getFeatures();
      dataInstances = parser.getDataInstances();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  /* Parses and stores feature names and data instances from test dataset .arff file, then iterates
   * through each test data instance finds specified number of closest neighbors that instance using
   * specified distance formula, and finally makes prediction based on the k closest neighbors. Prints
   * final accuracy or root mean squared deviation.
   *
   * @param filename string containing name of the .arff file containing the training dataset
   * @param neighbors the number of neighbors to be considered for each test data instance
   * @param distanceType integer indicating which distance formula to use [0 - Euclidean, 1 - Manhattan]
   */
  public void test(String filename, int neighbors, int distanceType) {
    // parse data from filename
    try {
      ArffParser parser = new ArffParser();
      parser.setInputFilename(filename);
      parser.parseDataFromArffFile();
      ArrayList<Feature> testFeatures = parser.getFeatures();
    	ArrayList<ArrayList<Double>> testDataInstances = parser.getDataInstances();
      double correctPredictionCount = 0;
      double rmse = 0;

      // for every instance in filename data
      for (ArrayList<Double> testInstance : testDataInstances) {
        // create a queue of some sort to store k closest neighbors
        PriorityQueue<Pair<ArrayList<Double>, Double>> nearestNeighbors = new PriorityQueue<Pair<ArrayList<Double>, Double>>(neighbors, new Comparator<Pair<ArrayList<Double>, Double>>() {
          public int compare(Pair<ArrayList<Double>, Double> lhs, Pair<ArrayList<Double>, Double> rhs) {
              if (lhs.getValue() < rhs.getValue()) return +1;
              if (lhs.getValue().equals(rhs.getValue())) return 0;
              return -1;
          }
        });
        // for every instance in stored data
        for (ArrayList<Double> instance : dataInstances) {
          // check euclidean distance from test data instance
          double distance = 0;
          if (distanceType == 0) { // Euclidean
            for (int i = 0; i < instance.size(); i++) {
              distance += Math.pow(testInstance.get(i) - instance.get(i), 2);
            }
            distance = Math.sqrt(distance);
          } else if (distanceType == 1)  { // Manhattan
            for (int i = 0; i < instance.size(); i++) {
              distance += Math.abs(testInstance.get(i) - instance.get(i));
            }
          }
          Pair temp = new Pair(instance, distance);
          if (nearestNeighbors.size() < neighbors || nearestNeighbors.peek().getValue() >= distance) {
            nearestNeighbors.add(temp);
          }
          if (nearestNeighbors.size() > neighbors) { // if over specified number of neighbors to check
            nearestNeighbors.poll(); // remove furthest neighbor
          }
        }

        // predict target feature value based on closest neighbors queue
        double predictedTargetFeatureValue  = -1;
        if (features.get(features.size() - 1) instanceof CategoricalFeature) { // categorical, get mode
          predictedTargetFeatureValue = getModeOfTargetFeature(nearestNeighbors);
          if (predictedTargetFeatureValue == testInstance.get(testInstance.size() - 1)) {
            correctPredictionCount++;
          }
        } else { // numeric, get mean
          predictedTargetFeatureValue = getMeanOfTargetFeature(nearestNeighbors);
          rmse += Math.pow((predictedTargetFeatureValue - testInstance.get(testInstance.size() - 1)), 2);
        }
      }

      if (features.get(features.size() - 1) instanceof CategoricalFeature) { // categorical, calc accuracy
        double accuracy = correctPredictionCount / testDataInstances.size();
        System.out.println("Accuracy when considering " + neighbors + " neighbors: " + (accuracy * 100) + "%");
      } else { // numeric, calc root mean squared error
        rmse = Math.pow(rmse / testDataInstances.size(), 0.5);
        System.out.println("RMSE when considering " + neighbors + " neighbors: " + rmse);
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  /* Returns the mode of the nearest neighbors' categorical target feature values.
   *
   * @param neighbors a priority queue containing pairs of the k nearest neighbor data instances
   *    and their distance from particular test data instance
   * @return a double containing the mode of the nearest neighbors' target feature values
   */
  private double getModeOfTargetFeature(PriorityQueue<Pair<ArrayList<Double>, Double>> neighbors) {
    HashMap<Double, Integer> modeMap = new HashMap<Double, Integer>();
    int maxOccurrences = 0;
    double mode = -1;

    while (neighbors.size() > 0) {
      int targetFeatureIndex = neighbors.peek().getKey().size() - 1;
      double dataValue = neighbors.poll().getKey().get(targetFeatureIndex);
      int total = 0;
      if (modeMap.get(dataValue) != null) {
        total = modeMap.get(dataValue);
        total++;
        modeMap.put(dataValue, total);
      } else {
        modeMap.put(dataValue, 1);
        total = 1;
      }
      if (total > maxOccurrences) {
        maxOccurrences = total;
        mode = dataValue;
      }
    }

    return mode;
  }

  /* Returns the mean of the nearest neighbors' numeric target feature values.
   *
   * @param neighbors a priority queue containing pairs of the k nearest neighbor data instances
   *    and their distance from particular test data instance
   * @return a double containing the mean of the nearest neighbors' target feature values
   */
  private double getMeanOfTargetFeature(PriorityQueue<Pair<ArrayList<Double>, Double>> neighbors) {
    int numberOfInstances = neighbors.size();
    double mean = 0;

    while (neighbors.size() > 0) {
      int targetFeatureIndex = neighbors.peek().getKey().size() - 1;
      mean += neighbors.poll().getKey().get(targetFeatureIndex);
    }

    return mean / numberOfInstances;
  }

}
