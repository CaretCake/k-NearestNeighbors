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
    // for every instance in filename data
      // create a queue of some sort to store k closest neighbors
      // for every instance in stored data
        // check euclidean distance from test data instance
        // if distance to stored data instance < any in queue
          // add stored data instance to queue of closest neighbors
          // remove furthest distance stored data instance from queue
      // predict target feature value based on closest neighbors queue
    // calculate accuracy + print it (root mean squared error if target is continuous)
  }

}
