import java.util.ArrayList;

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

      // for every instance in filename data
      for (ArrayList<Double> testInstance : testDataInstances) {
        // create a queue of some sort to store k closest neighbors
        ArrayList<Double> distances = new ArrayList<Double>();
        // for every instance in stored data
        for (ArrayList<Double> instance : dataInstances) {
          // check euclidean distance from test data instance
          double distance = 0;
          for (int i = 0; i < instance.size(); i++) {
            distance += Math.pow(testInstance.get(i) - instance.get(i), 2);
          }
          distance = Math.sqrt(distance);
          distances.add(distance);
          System.out.println(distance);
          // if distance to stored data instance < any in queue
            // add stored data instance to queue of closest neighbors
            // remove furthest distance stored data instance from queue
        }
        // predict target feature value based on closest neighbors queue
      }
      // calculate accuracy + print it (root mean squared error if target is continuous)
    } catch (Exception e) {
      System.out.println(e);
    } 
  }

}
