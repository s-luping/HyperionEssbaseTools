import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertyLoader {
    private static final Logger logger = Logger.getLogger(PropertyLoader.class.getName());

    /**
     * 
     * 从当前的类加载器的getResourcesAsStream来获取
     * InputStream inputStream =
     * this.getClass().getClassLoader().getResourceAsStream(name)
     * 
     * @throws Exception
     *
     */

    public Properties ETLConfig() {
        Properties properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream("config/config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("获取配置文件失败");
            e.printStackTrace();
        }
        logger.info("获取配置文件");
        return properties;
    }

    public String getDimensionString() {
        Properties properties = ETLConfig();
        String HPDims = properties.getProperty("ess_dimensions");
        String Processed_HPDims = "";
        for (String split : HPDims.split(",")) {
            Processed_HPDims = Processed_HPDims + split + ",";
        }
        ;
        Processed_HPDims = Processed_HPDims.substring(0, Processed_HPDims.length() - 1);
        logger.info("获取维度查询字符串:" + Processed_HPDims);
        return Processed_HPDims;
    }

    public String getDimensionMapping() {
        String HPDims_Mapping = "";
        Properties properties = ETLConfig();
        String HPDims = properties.getProperty("ess_dimensions");
        for (String split : HPDims.split(",")) {
            HPDims_Mapping = HPDims_Mapping + split + "=" + split + ",";
        }
        ;
        HPDims_Mapping = HPDims_Mapping.substring(0, HPDims_Mapping.length() - 1);
        logger.info("获取维度映射:" + HPDims_Mapping);
        return HPDims_Mapping;
    }

}
