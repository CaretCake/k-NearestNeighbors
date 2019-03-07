class Feature {
  protected String name;

  public Feature(String featureName) {
    this.name = featureName;
  }

  public String getFeatureName() {
    return this.name;
  }

  public String toString() {
    return ("FeatureName: " + this.name);
  }
}
