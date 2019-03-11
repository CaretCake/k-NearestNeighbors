# k-NearestNeighbors
Java implementation of k-Nearest Neighbors algorithm with options to choose Euclidean or Manhattan distance.

## Description

A Java program which reads in data from a .arff file containing a training dataset and then creates a k-nearest neighbors model based on said data. It will then test the model given a specified number of neighbors to check against on another .arff file containing a test dataset. Finally, it will print out the root mean squared deviation or accuracy, depending on if the target feature is categorical or numeric. Currently, it will only work with categorical and numeric features.

## Getting Started

### Installing

* Just download all files into a folder with the .arff files containing your testing and training datasets

### Executing program

* Compile and run the program from a terminal with the following commands:
```
javac Feature.java NumericFeature.java CategoricalFeature.java ArffParser.java KNNModel.java KNearestNeighbors.java
java KNearestNeighbors [TRAINING_DATASET_FILENAME].arff [TESTING_DATASET_FILENAME].arff [NUMBER OF NEIGHBORS]
```
OR
```
javac Feature.java NumericFeature.java CategoricalFeature.java ArffParser.java KNNModel.java KNearestNeighbors.java
java KNearestNeighbors [TRAINING_DATASET_FILENAME].arff [TESTING_DATASET_FILENAME].arff [NUMBER OF NEIGHBORS] [DISTANCE TYPE ('EUC' or 'MAN')]
```

* Example
```
javac Feature.java NumericFeature.java CategoricalFeature.java ArffParser.java KNNModel.java KNearestNeighbors.java
java KNearestNeighbors diseaseTrainingData.arff diseaseTestData.arff 5 EUC
java KNearestNeighbors diseaseTrainingData.arff diseaseTestData.arff 5 MAN
```
