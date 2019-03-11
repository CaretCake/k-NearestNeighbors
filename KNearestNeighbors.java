public class KNearestNeighbors {
  public static void main (String[] args) {

    String trainFilename = args[0];
    String testFilename = args[1];
    int numOfNeighbors = Integer.parseInt(args[2]);
    int distanceType = 0;
    if (args.length > 3) {
      distanceType = getDistanceTypeInteger(args[3].trim());
    }

    if (numOfNeighbors < 1) {
      System.out.println("Cannot use less than 1 neighbor.");
      System.exit(0);
    }

    KNNModel kNNModel = new KNNModel();

    kNNModel.train(trainFilename);
    kNNModel.test(testFilename, numOfNeighbors, distanceType);

  }

  /* Converts user's string input to correct integer value for chosen distance type.
	 *
 	 * @param input a string containing the user's specified distance type
 	 * @return an int representing chosen distance type
   */
  public static int getDistanceTypeInteger (String input) {
    if (input.equals("Euc")) {
      return 0;
    } else if (input.equals("Man")) {
      return 1;
    } else {
      System.out.println("Distance type must be entered as 'Euc' (Euclidean) or 'Man' (Manhattan)");
      System.exit(0);
      return 0;
    }
  }
}
