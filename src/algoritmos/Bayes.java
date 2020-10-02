package algoritmos;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class Bayes {

    public static double[] naiveBayes(double[]caracteristicas) {
        double[] retorno = {0,0};
        try {
            //*******carregar arquivo de características
            ConverterUtils.DataSource ds = new ConverterUtils.DataSource("caracteristicas_trabalho.arff");
            Instances instancias = ds.getDataSet();
            //instancias.setClassIndex(6);
            instancias.setClassIndex(instancias.numAttributes()-1);

            //Classifica com base nas características da imagem selecionada
            NaiveBayes nb = new NaiveBayes();
            nb.buildClassifier(instancias);//aprendizagem (tabela de probabilidades)


            Instance novo = new DenseInstance(instancias.numAttributes());
            novo.setDataset(instancias);
            novo.setValue(0, caracteristicas[0]);
            novo.setValue(1, caracteristicas[1]);
            novo.setValue(2, caracteristicas[2]);
            novo.setValue(3, caracteristicas[3]);
            novo.setValue(4, caracteristicas[4]);
            novo.setValue(5, caracteristicas[5]);

            retorno = nb.distributionForInstance(novo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
