import org.apache.spark.ml.feature.Word2Vec;
import org.apache.spark.ml.feature.Word2VecModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by wafer on 17/8/4.
 */
public class Test {
    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaRandomForestClassifierExample")
                .getOrCreate();
        List<Row> data = Arrays.asList(
                RowFactory.create(Arrays.asList("Hi I heard about Spark".split(" "))),
                RowFactory.create(Arrays.asList("I wish Java could use case classes".split(" "))),
                RowFactory.create(Arrays.asList("Logistic regression models are neat".split(" ")))
        );
        StructType schema = new StructType(new StructField[]{
                new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
        });
        Dataset<Row> documentDF = spark.createDataFrame(data, schema);

        Word2Vec word2Vec = new Word2Vec()
                .setInputCol("text")
                .setOutputCol("result")
                .setVectorSize(3)
                .setMinCount(0);

        Word2VecModel model = word2Vec.fit(documentDF);
        Dataset<Row> result = model.transform(documentDF);
        Dataset<Row> result2 = null;
//            model.save("/Users/wafer/Downloads/spark-2.1.1-bin-hadoop2.6/data/mllib/test1");

        Word2VecModel load = Word2VecModel.load("/Users/wafer/Downloads/spark-2.1.1-bin-hadoop2.6/data/mllib/test");
        result2 = model.transform(documentDF);

        for (Row row : result2.collectAsList()) {
            List<String> text = row.getList(0);
            Vector vector = (Vector) row.get(1);
            System.out.println("Text2: " + text + " => \nVector: " + vector + "\n");
        }

        for (Row row : result.collectAsList()) {
            List<String> text = row.getList(0);
            Vector vector = (Vector) row.get(1);
            System.out.println("Text:1 " + text + " => \nVector: " + vector + "\n");
        }

    }
}
