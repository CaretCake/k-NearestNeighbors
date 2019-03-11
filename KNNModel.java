import java.util.ArrayList;
import java.util.PriorityQueue;
import javafx.util.Pair;
import java.util.Comparator;
import java.util.HashMap;

public class KNNModel {
  private ArrayList<Feature> features;
	private ArrayList<ArrayList<Double>> dataInstances;

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

  public void test(String filename, int neighbors) {
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
          for (int i = 0; i < instance.size(); i++) {
            distance += Math.pow(testInstance.get(i) - instance.get(i), 2);
          }
          distance = Math.sqrt(distance);
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
        System.out.println("accuracy: " + (accuracy * 100) + "%");
      } else { // numeric, calc root mean squared error
        rmse = Math.pow(rmse / testDataInstances.size(), 0.5);
        System.out.println("rmse: " + rmse);
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }

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
