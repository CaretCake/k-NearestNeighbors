import java.util.ArrayList;
import java.util.Arrays;

class CategoricalFeature extends Feature {
  protected ArrayList<String> categoricalOptions;

  /* Creates a CategoricalFeature, parsing out the categorical feature's options and storing them
   * in an arraylist to be "mapped" to int values for easier use.
	 *
 	 * @param featureName string containing the name of the feature
 	 * @param a string containing the categorical options for the feature
   */
  public CategoricalFeature(String featureName, String categoricalOptionsString) {
    super(featureName);
    categoricalOptionsString = categoricalOptionsString.substring(1, categoricalOptionsString.length()-1);
		this.categoricalOptions = new ArrayList(Arrays.asList(categoricalOptionsString.split(",")));
  }

  public String getNameOfCategoricalFeatureValue(int index) {
    return this.categoricalOptions.get(index);
  }

  public int getIndexOfCategoricalFeatureValue(String categoricalValue) {
    return this.categoricalOptions.indexOf(categoricalValue);
  }

  @Override
  public String toString() {
    return ("FeatureName: " + this.name +
            " options: " + this.categoricalOptions);
  }
}
