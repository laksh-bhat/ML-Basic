package cs475.classifier.supervisedlearning.knn;

import cs475.dataobject.Instance;
import cs475.dataobject.label.Label;
import cs475.dataobject.label.RegressionLabel;

import java.util.SortedMap;
import java.util.TreeMap;


public class SimpleKNNPredictor extends KNNPredictor
{
    @Override
    public Label predict ( Instance instance )
    {
        SortedMap<Double, Label> neighborDistanceWithPrediction  = new TreeMap<Double, Label>();
        for ( Instance trainingInstance : dataset )
        {
            neighborDistanceWithPrediction.put( computeDifferenceNorm( instance.getFeatureVector(),
                    trainingInstance.getFeatureVector() ), trainingInstance.getLabel() );
        }
        return predictLabel( getNumberOfNearestNeighbors(), neighborDistanceWithPrediction );
    }

    protected Label predictLabel ( int k, SortedMap<Double, Label> neighborDistanceWithPrediction )
    {
        double prediction = 0;
        for ( int i = 0 ; i < k ; i++ )
        {
            Label nearestNeighbor = neighborDistanceWithPrediction.get( neighborDistanceWithPrediction
                    .firstKey() );
            neighborDistanceWithPrediction.remove( neighborDistanceWithPrediction.firstKey() );
            prediction += nearestNeighbor.getLabelValue();
        }
        return new RegressionLabel( prediction / k );
    }
}

