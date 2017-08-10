package controller;

import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.OneVsRest;
import org.apache.spark.ml.feature.CountVectorizer;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by wafer on 17/8/7.
 */
@Service
public class YIjianjianyiLabelBuidel {

    HashMap<String, PipelineModel> map = new HashMap();
    SparkSession spark;
    String yuan = "/home/wafer/Data/MLlib/yuan/allJson_typeArray";
    String out = "/home/wafer/Data/MLlib/allJson_typeArray/";

//    @PostConstruct
    public void init() {
        spark = SparkSession
                .builder()
                .appName("JavaRandomForestClassifierExample").config("spark.network.timeout", 500)
                .getOrCreate();

        StructType add = new StructType().add("content", DataTypes.createArrayType(DataTypes.StringType))
                .add("总裁办", DataTypes.IntegerType)
                .add("随访", DataTypes.IntegerType)
                .add("家庭医生", DataTypes.IntegerType)
                .add("点评", DataTypes.IntegerType)
                .add("服务供应部", DataTypes.IntegerType)
                .add("处方运营", DataTypes.IntegerType)
                .add("付费文章", DataTypes.IntegerType)
                .add("分诊", DataTypes.IntegerType)
                .add("转诊", DataTypes.IntegerType)
                .add("会诊出差", DataTypes.IntegerType)
                .add("APP报错", DataTypes.IntegerType)
                .add("产品", DataTypes.IntegerType)
                .add("客服中心", DataTypes.IntegerType)
                .add("患者运营", DataTypes.IntegerType)
                .add("验证码", DataTypes.IntegerType)
                .add("商务运营部", DataTypes.IntegerType)
                .add("诊后报到", DataTypes.IntegerType)
                .add("团队接诊", DataTypes.IntegerType)
                .add("电话咨询", DataTypes.IntegerType)
                .add("内容", DataTypes.IntegerType)
                .add("认证数据", DataTypes.IntegerType)
                .add("敏感词", DataTypes.IntegerType)
                .add("意见建议", DataTypes.IntegerType);
        Dataset<Row> json = spark.read().schema(add).json(yuan);
        json = json.randomSplit(new double[]{0.3, 0.7})[1];
        Dataset<Row>[] datasets = json.randomSplit(new double[]{0.3, 0.3});
        datasets[0] = json;
        double d =
                get22(datasets[0], datasets[1], "总裁办") *
                        get22(datasets[0], datasets[1], "随访") *
                        get22(datasets[0], datasets[1], "家庭医生") *
                        get22(datasets[0], datasets[1], "点评") *
                        get22(datasets[0], datasets[1], "服务供应部") *
                        get22(datasets[0], datasets[1], "处方运营") *
                        get22(datasets[0], datasets[1], "付费文章") *
                        get22(datasets[0], datasets[1], "分诊") *
                        get22(datasets[0], datasets[1], "转诊") *
                        get22(datasets[0], datasets[1], "会诊出差") *
                        get22(datasets[0], datasets[1], "APP报错") *
                        get22(datasets[0], datasets[1], "产品") *
                        get22(datasets[0], datasets[1], "客服中心") *
                        get22(datasets[0], datasets[1], "患者运营") *
                        get22(datasets[0], datasets[1], "验证码") *
                        get22(datasets[0], datasets[1], "商务运营部") *
                        get22(datasets[0], datasets[1], "诊后报到") *
                        get22(datasets[0], datasets[1], "团队接诊") *
                        get22(datasets[0], datasets[1], "电话咨询") *
                        get22(datasets[0], datasets[1], "内容") *
                        get22(datasets[0], datasets[1], "认证数据") *
                        get22(datasets[0], datasets[1], "敏感词") *
                        get22(datasets[0], datasets[1], "意见建议");
        System.out.println(sb.toString() + "nnnnnnn" + d);
    }


    /*
    0.9963523709588767///0.9986633688102733///0.9900689551791335///0.9879328436516265///0.9561659921051316///0.9639109578773797///0.9995502923099985///0.9431494528556438///0.9777019937040924///0.995502923099985///0.9991505521411083///0.9784015389996502///0.9947159346424824///0.9143806525758257///0.9992879628241643///0.9857717483635637///0.99198021286164///0.9999875081197221///0.9626242942087643///0.9986258931694398///0.9397141857792435///0.9771898266127017///0.9984884824863839///nnnnnnn0.630297456781
     */
    private double get22(Dataset<Row> json, Dataset<Row> json1, String label) {
        PipelineModel model = null;
        try {
            model = PipelineModel.load(out + label);

        } catch (Exception e) {

        }
        if (model == null) {
            CountVectorizerModel cvModel = new CountVectorizer()
                    .setInputCol("content")
                    .setOutputCol("features")
                    .setVocabSize(800)
                    .setMinDF(2)
                    .fit(json);

            LogisticRegression classifier = new LogisticRegression()
                    .setMaxIter(800)
                    .setTol(1E-3)
                    .setFitIntercept(true);
            OneVsRest ccc = new OneVsRest().setClassifier(classifier).setLabelCol(label)
                    .setFeaturesCol("features");


//        int[] layers = new int[]{300, 300, 300, 300};
//        MultilayerPerceptronClassifier ccc = new MultilayerPerceptronClassifier()
//                .setLayers(layers)
//                .setLabelCol(label)
//                .setFeaturesCol("features");


//        //随机森林 0.328
//        RandomForestClassifier ccc = new RandomForestClassifier()
//                .setLabelCol(label)
//                .setFeaturesCol("features");

            Pipeline pipeline = new Pipeline()
                    .setStages(new PipelineStage[]{cvModel, ccc});
            try {
                model = pipeline.fit(json);
                model.save(out + label);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        map.put(label, model);
//        Dataset<Row> transform = model.transform(json1);
//        double accuracy = 1;
//        try {
//
//            MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
//                    .setLabelCol(label)
//                    .setPredictionCol("prediction")
//                    .setMetricName("accuracy");
//            accuracy = evaluator.evaluate(transform);
//            sb.append(accuracy).append("///");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return 1;
    }

    static StringBuilder sb = new StringBuilder();

    public ArrayList<String> get(String content) {
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
            if (transform(entry.getKey(), entry.getValue(), dataFrame)) {
                strings.add(entry.getKey());
            }
        }
        return strings;
    }


    private boolean transform(String label, PipelineModel model, Dataset<Row> dataFrame) {
        Dataset<Row> predictions = model.transform(dataFrame);
        return predictions.select("prediction").first().getDouble(0) > 0.5;
    }

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaRandomForestClassifierExample")
                .getOrCreate();
        StructType add = new StructType().add("content", DataTypes.createArrayType(DataTypes.StringType))
                .add("总裁办", DataTypes.IntegerType)
                .add("随访", DataTypes.IntegerType)
                .add("家庭医生", DataTypes.IntegerType)
                .add("点评", DataTypes.IntegerType)
                .add("服务供应部", DataTypes.IntegerType)
                .add("处方运营", DataTypes.IntegerType)
                .add("付费文章", DataTypes.IntegerType)
                .add("分诊", DataTypes.IntegerType)
                .add("转诊", DataTypes.IntegerType)
                .add("会诊出差", DataTypes.IntegerType)
                .add("APP报错", DataTypes.IntegerType)
                .add("产品", DataTypes.IntegerType)
                .add("客服中心", DataTypes.IntegerType)
                .add("患者运营", DataTypes.IntegerType)
                .add("验证码", DataTypes.IntegerType)
                .add("商务运营部", DataTypes.IntegerType)
                .add("诊后报到", DataTypes.IntegerType)
                .add("团队接诊", DataTypes.IntegerType)
                .add("电话咨询", DataTypes.IntegerType)
                .add("总裁内容办", DataTypes.IntegerType)
                .add("认证数据", DataTypes.IntegerType)
                .add("敏感词", DataTypes.IntegerType)
                .add("意见建议", DataTypes.IntegerType);
        String yuan = "/Users/wafer/Desktop/MLlib/allJson1";
        Dataset<Row> json = spark.readStream().schema(add).json(yuan);

        CountVectorizerModel cvModel = new CountVectorizer()
                .setInputCol("content")
                .setOutputCol("features")
                .setVocabSize(800)
                .setMinDF(2)
                .fit(json);

        LogisticRegression classifier = new LogisticRegression()
                .setMaxIter(800)
                .setTol(1E-3)
                .setFitIntercept(true);
        OneVsRest ccc = new OneVsRest().setClassifier(classifier).setLabelCol("随访")
                .setFeaturesCol("features");
        Pipeline pipeline = new Pipeline()
                .setStages(new PipelineStage[]{cvModel, ccc});
        PipelineModel fit = pipeline.fit(json);

    }

}
