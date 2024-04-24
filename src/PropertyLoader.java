import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    /**
     * 
     * 从当前的类加载器的getResourcesAsStream来获取
     * InputStream inputStream =
     * this.getClass().getClassLoader().getResourceAsStream(name)
     *
     * @throws IOException
     */
    public  Properties getProperties() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream("./config.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
    public String getDimensionString() throws IOException {
        Properties properties = getProperties();
        String rawDimName = properties.getProperty("ess_dimensions");
        String newDimName = "";
        for (String split : rawDimName.split(",")) {
            newDimName = newDimName + "\"" + split + "\"" + ",";
            // String.join(str, "\""+split+"\"");
        };
        return newDimName.substring(0, newDimName.length()-1);
    }
    public String getDimensionMapping() throws IOException {
        Properties properties = getProperties();
        String rawDimName = properties.getProperty("ess_dimensions");
        String newDimName = "";
        for (String split : rawDimName.split(",")) {
            newDimName = newDimName + split + "=" + split + ",";
            // String.join(str, "\""+split+"\"");
        };
        return newDimName.substring(0, newDimName.length()-1);
    }

}