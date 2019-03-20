import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;

public class ArffParser {
	private String inputFilename;
	private ArrayList<Feature> features = new ArrayList<Feature>();
	private ArrayList<ArrayList<Double>> dataInstances = new ArrayList<ArrayList<Double>>();

	/* Adds a data instance to the dataInstances ArrayList and handles adding categorical options
	 * if needed.
	 *
	 * @param dataInstance the comma separated string containing the data instance's values
	 */
	private void addDataInstance(String dataInstance) {
		String[] data = dataInstance.split(",");
		ArrayList<Double> processedDataInstance = new ArrayList<Double>();
		double processedValue = 0;
		for (int i = 0; i < data.length; i++) {
      		Feature feat = features.get(i);
			if (feat instanceof CategoricalFeature) {
        			CategoricalFeature fe = (CategoricalFeature)features.get(i);
				processedValue = fe.getIndexOfCategoricalFeatureValue(data[i]);
				processedDataInstance.add(processedValue);
			} else {
				processedValue = Double.parseDouble(data[i]);
				processedDataInstance.add(processedValue);
			}
		}
		this.dataInstances.add(processedDataInstance);
	}

	/* Parses data from .arff file at inputFilename, storing numeric/categorical features, categorical
	 * feature options, and data instances accordingly. */
	public void parseDataFromArffFile() throws Exception {
		try {
			Scanner in = new Scanner(new File(inputFilename), "UTF-8");
			in.nextLine();

			while(in.hasNextLine()) {
				String line = in.nextLine().trim();
				if (line.length() > 0 && line.charAt(0) != '%' && !line.contains("@data") && !line.contains("@relation")) {
					if (line.contains("@attribute")) {
						String[] lineSplitOnSpace = line.split(" ");
						switch (lineSplitOnSpace[2].trim()) {
							case "numeric":
                						Feature numFeature = new NumericFeature(lineSplitOnSpace[1].trim());
								this.features.add(numFeature);
								break;
							default:
                						Feature catFeature = new CategoricalFeature(lineSplitOnSpace[1].trim(), lineSplitOnSpace[2].trim());
               							this.features.add(catFeature);
								break;
						}
					} else {
						this.addDataInstance(line);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Issue reading in input file:" + e);
			System.exit(0);
		}
	}

	public void setInputFilename(String filename) {
		this.inputFilename = filename;
	}

  public ArrayList<ArrayList<Double>> getDataInstances() {
    return this.dataInstances;
  }

  public ArrayList<Feature> getFeatures() {
    return this.features;
  }

}
