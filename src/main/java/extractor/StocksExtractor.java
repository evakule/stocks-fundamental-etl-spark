package extractor;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

public class StocksExtractor implements Extractor<Dataset<Row>> {

  private final Properties props;
  private final SparkSession spark;
  private Dataset<Row> sourceData = null;

  public StocksExtractor(Properties props) {
    this.props = props;
    this.spark = SparkSession.builder()
        .appName(props.getProperty("name"))
        .master("local[*]")
        .getOrCreate();
  }

  public StocksExtractor(Properties props, SparkSession spark) {
    this.props = props;
    this.spark = spark;
  }

  public Dataset<Row> getSourceData() {
    if (sourceData == null) {
      this.sourceData = this.spark.read()
          .option("multiline", "true")
          .format(props.getProperty("format"))
          .load(props.getProperty("data-path"));
    }
    return sourceData;
  }
}
