import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import com.hyperion.odi.common.ODIConstants;

public class ConnProperties {

    ConnProperties() {

    }

    public static Properties getProperties() {
        return new PropertyLoader().ETLConfig();
    }

    /**
     * 获取 Essbase 连接信息。
     *
     * @return Essbase 连接信息的 HashMap
     * @throws IOException
     */
    public static HashMap<String, Object> EssbaseConnection() {
        try {
            Properties properties = getProperties();
            HashMap<String, Object> essProps = new HashMap<>();
            // 设置 Essbase 连接属性
            essProps.put(ODIConstants.SERVER, properties.getProperty("ess_olapSvrName"));
            essProps.put(ODIConstants.PORT, properties.getProperty("ess_olapSvrPort"));
            essProps.put(ODIConstants.USER, properties.getProperty("ess_userName"));
            essProps.put(ODIConstants.PASSWORD, properties.getProperty("ess_password"));
            essProps.put(ODIConstants.APPLICATION_NAME, properties.getProperty("ess_appName"));
            essProps.put(ODIConstants.DATABASE_NAME, properties.getProperty("ess_cubeName"));

            return essProps;
        } catch (Exception e) {
            System.out.println("Failed to load Essbase connection properties.");
            throw new RuntimeException("Failed to load Essbase connection properties.", e);
        }
    }

    /**
     * 获取数据库连接信息。
     * 
     * @return 数据库连接信息的HashMap。
     * @throws SQLException 如果数据库连接失败。
     * @throws IOException
     */
    public static HashMap<String, Object> getTargrtDatabaseProp() throws Exception {
        Properties properties = getProperties();
        // 获取临时数据库连接属性
        HashMap<String, Object> databaseMap = new HashMap<>();
        String dbUrl = properties.getProperty("target_db_url");
        String dbUserName = properties.getProperty("target_db_username");
        String dbPassword = properties.getProperty("target_db_password");
        String targetTable = properties.getProperty("target_db_table");
        String mappingColumns = new PropertyLoader().getDimensionMapping();
        Connection conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        // 获取到临时数据库的连接
        databaseMap.put("conn", conn);
        databaseMap.put("targetTable", targetTable);
        databaseMap.put("mappingColumns", mappingColumns);

        return databaseMap;
    }

    /**
     * 获取数据库连接。
     *
     * @return 数据库连接对象
     * @throws Exception
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static HashMap<String, Object> getSrcDatabaseProp(String args[]) throws Exception {
        Properties properties = getProperties();
        // 获取临时数据库连接属性
        HashMap<String, Object> databaseMap = new HashMap<>();
        // 获取到数据库连接
        String dbUrl = properties.getProperty("src_db_url");
        String dbUserName = properties.getProperty("src_db_username");
        String dbPassword = properties.getProperty("src_db_password");
        String srcTable = properties.getProperty("src_db_table");
        // Class.forName("oracle.jdbc.OracleDriver");
        Connection conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        // 获取命令行参数
        if (args.length != 4) {
            throw new Exception(
                    "Usage: java -jar HyperionEssbaseLoad.jar <operation_type> <batch_name> <year_num> <period_num>");
        }
        // 打印参数
        System.out.println("BATCH_NAME: " + args[1]);
        System.out.println("YEARS: " + args[2]);
        System.out.println("PERIODS: " + args[3]);

        // 从配置文件获取维度列和数据表名称
        String columns = new PropertyLoader().getDimensionString();
        StringBuilder queryBuilder = new StringBuilder();

        // 定义 SQL 语句用于从数据库加载数据
        // "'#Missing' AS \"Data\"
        queryBuilder.append("SELECT ");
        queryBuilder.append(columns);
        queryBuilder.append(" FROM ");
        queryBuilder.append(srcTable);
        queryBuilder.append(" WHERE (1=1) ");
        queryBuilder.append("AND (BATCH_NAME='" + args[1] + "' ");
        queryBuilder.append("AND YEARS='" + args[2] + "' ");
        queryBuilder.append("AND PERIODS='" + args[3] + "') ");
        // 将数据库连接、数据表名称、SQL 语句放入 HashMap 中
        databaseMap.put("conn", conn);
        databaseMap.put("querysql", queryBuilder.toString());

        return databaseMap;
    }
}
