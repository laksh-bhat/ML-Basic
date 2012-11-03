package cs475.classify.neuralnetwork;

import cs475.classify.generalizedlearningmodels.PerceptronPredictor;
import cs475.dataobject.Instance;
import cs475.dataobject.label.Label;

import java.util.ArrayList;
import java.util.List;


public class EnsembleFeatureBaggingPredictor extends EnsemblePredictor
{
    public void train ( List<Instance> instances )
    {
        int noOfClassifiers = getNoOfClassifiers();
        trainClassifiers( instances, noOfClassifiers );
        int trainingIterations = getTrainingIterations();

        while ( trainingIterations-- > 0 )
        {
            for ( Instance instance : instances )
            {
                Label yi = instance.getLabel();
                Label yCap = predictLabel( noOfClassifiers, instance );

                if ( yCap.getLabelValue() != yi.getLabelValue() )
                {
                    updateWeightOfClassifier( noOfClassifiers, instance );
                }
            }
        }
    }

    @Override
    // implements feature bagging
    protected List<Instance> getInstancesBasedOnEnsembleAlgorithm ( int k, List<Instance> instances )
    {
        int totalFeatures = PerceptronPredictor.getTotalNoOfFeatures(instances);
        List<Instance> featureBag = new ArrayList<Instance>();
        for ( Instance instance : instances )
        {
            try
            {
                Instance clone = ( Instance ) instance.clone();
                for ( int feature = 0 ; feature < totalFeatures ; feature++ )
                {
                    if ( feature % k == k - 1 )
                    {
                        if ( clone.getFeatureVector().getFeatureVectorKeys().contains( feature ) )
                        {
                            clone.getFeatureVector().featureVector.remove( feature );
                        }
                    }
                }
                featureBag.add( clone );
            }
            catch ( CloneNotSupportedException ignored )
            {
            }
        }
        return featureBag;
    }
}