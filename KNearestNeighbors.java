public class KNearestNeighbors {
  public static void main (String[] args) {

    String trainFilename = args[0];
    String testFilename = args[1];
    int numOfNeighbors = args[2];

    KNNModel kNNModel = new KNNModel();

    kNNModel.train(trainFilename);
    kNNModel.test(testFilename, numOfNeighbors);

  }
}
