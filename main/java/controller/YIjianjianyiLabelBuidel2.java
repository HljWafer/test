package controller;

import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.OneVsRest;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Created by wafer on 17/8/7.
 */
@Service
public class YIjianjianyiLabelBuidel2 {

    HashMap<String, PipelineModel> map = new HashMap();
    SparkSession spark;
    String yuan = "/home/wafer/Data/MLlib/yuan/allJson_typeStr";
    String out = "/home/wafer/Data/MLlib/逻辑回归typeStr";

//    @PostConstruct
public void init() {
        spark = SparkSession
                .builder()
                .appName("JavaRandomForestClassifierExample").config("spark.network.timeout", 500)
                .getOrCreate();

        StructType add = new StructType().add("content", DataTypes.createArrayType(DataTypes.StringType))
                .add("type", DataTypes.StringType);
        Dataset<Row> json = spark.read().schema(add).json(yuan);
//        StructType add = new StructType().add("content", DataTypes.createArrayType(DataTypes.StringType)).add("总裁办", DataTypes.IntegerType);
//        Dataset<Row> json = spark.readStream().schema(add).json(yuan);

        json = json.randomSplit(new double[]{0.3, 0.7})[1];
        Dataset<Row>[] datasets = json.randomSplit(new double[]{0.3, 0.3});
        datasets[0] = json;
        get21(datasets[0], datasets[1]);
        System.out.println(sb.toString() + "nnnnnnn");
    }


    private double get21(Dataset<Row> json, Dataset<Row> json1) {
        try {
            model = PipelineModel.load(out);
        } catch (Exception e) {

        }
        if (model == null) {
            CountVectorizerModel cvModel = new CountVectorizer()
                    .setInputCol("content")
                    .setOutputCol("features")
                    .setVocabSize(800)
                    .setMinDF(1)
                    .fit(json);


            StringIndexerModel indexer = new StringIndexer()
                    .setInputCol("type")
                    .setOutputCol("category")
                    .fit(json);

            LogisticRegression classifier = new LogisticRegression()
                    .setMaxIter(800)
                    .setTol(1.8E-3)
                    .setFitIntercept(true);
            OneVsRest ccc = new OneVsRest().setClassifier(classifier).setLabelCol("category")
                    .setFeaturesCol("features");


            IndexToString labelConverter = new IndexToString()
                    .setInputCol("prediction")
                    .setOutputCol("predictedLabel")
                    .setLabels(indexer.labels());

            Pipeline pipeline = new Pipeline()
                    .setStages(new PipelineStage[]{cvModel, indexer, ccc, labelConverter});
            model = pipeline.fit(json);
            try {
                model.save(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Dataset<Row> transform = model.transform(json1);
        double accuracy = 1;
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("category")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        accuracy = evaluator.evaluate(transform);
        sb.append(accuracy).append("///");

        return accuracy;
    }

    PipelineModel model;
    static StringBuilder sb = new StringBuilder();


    /**
     * quzayin_typeStr
     *
     * @param content
     * @return
     */
    public String get(String content) {
        if (yuan.contains("quzayin")) {
            if (content.contains("建议，内容如下：\n----------------------------------------------------------------------------\n")) {
                String[] split = content.split("建议，内容如下：\n----------------------------------------------------------------------------\n");
                content = split[1];
            }
            if (content.contains("建议，内容如下:\n----------------------------------------------------------------------------\n")) {
                String[] split = content.split("建议，内容如下:\n----------------------------------------------------------------------------\n");
                content = split[1];
            }
            if (content.contains("建议，内容如下：\n----------------------------------------------------------------------------\n")) {
                String[] split = content.split("建议，内容如下：\n----------------------------------------------------------------------------\n");
                content = split[1];
            }
            if (content.contains("建议, 内容如下:\n----------------------------------------------------------------------------\n")) {
                String[] split = content.split("建议, 内容如下:\n----------------------------------------------------------------------------\n");
                content = split[1];
            }
            if (content.contains("\n----------------------------------------------------------------------------\n发送")) {
                String[] split = content.split("\n----------------------------------------------------------------------------\n发送");
                content = split[0];
            }
            if (content.contains("\n----------------------------------------------------------------------------\n20")) {
                String[] split = content.split("\n----------------------------------------------------------------------------\n20");
                content = split[0];
            }
            if (content.contains("，内容如下：")) {
                String[] split = content.split("，内容如下：");
                content = split[1];
            }
            if (content.contains(", 内容如下:\n")) {
                String[] split = content.split(", 内容如下:\n");
                content = split[1];
            }
            if (content.contains("\n电话:")) {
                String[] split = content.split("\n电话:");
                content = split[0];
            }
            if (content.contains("\n电话：")) {
                String[] split = content.split("\n电话：");
                content = split[0];
            }
            if (content.contains("\n所提交的空间:")) {
                String[] split = content.split("\n所提交的空间:");
                content = split[0];
            }
        }
        ArrayList<String> strings = new ArrayList<String>();
        List<com.hankcs.hanlp.seg.common.Term> termList = StandardTokenizer.segment(content);
        String[] term = new String[termList.size()];
        for (int i = 0; i < termList.size(); i++) {
            term[i] = termList.get(i).word;
        }
        List<Row> data = Arrays.asList(RowFactory.create(Arrays.asList(term)));
        StructType schema = new StructType(new StructField[]{
                new StructField("content", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
        });
        Dataset<Row> dataFrame = spark.createDataFrame(data, schema);

        Set<Map.Entry<String, PipelineModel>> set = map.entrySet();
        for (Map.Entry<String, PipelineModel> entry : set) {
            strings.add(entry.getKey());
        }
        return transform(model, dataFrame);
    }


    private String transform(PipelineModel model, Dataset<Row> dataFrame) {
        Dataset<Row> predictions = model.transform(dataFrame);
        return predictions.select("predictedLabel").first().toString();
    }

}
