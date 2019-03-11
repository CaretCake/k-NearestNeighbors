public class KNearestNeighbors {
  public static void main (String[] args) {

    String trainFilename = args[0];
    String testFilename = args[1];
    int numOfNeighbors = Integer.parseInt(args[2]);

    if (numOfNeighbors < 1) {
      System.out.println("Cannot use less than 1 neighbor.");
      System.exit(0);
    }

    KNNModel kNNModel = new KNNModel();

    kNNModel.train(trainFilename);
    kNNModel.test(testFilename, numOfNeighbors);

  }
}
