import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import com.hyperion.odi.common.ODIConstants;


public class ConnProperties {

    ConnProperties()   {

    }

    public static Properties getProperties() throws IOException {
        return new PropertyLoader().getProperties();
    }
    /**
     * 获取 Essbase 连接信息。
     *
     * @return Essbase 连接信息的 HashMap
     * @throws IOException 
     */
    public static HashMap<String, Object> getEssbaseProp() throws IOException {
        HashMap<String, Object> essProps = new HashMap<>();
        // 设置 Essbase 连接属性
        essProps.put(ODIConstants.SERVER, getProperties().getProperty("ess_olapSvrName"));
        essProps.put(ODIConstants.PORT, getProperties().getProperty("ess_olapSvrPort"));
        essProps.put(ODIConstants.USER, getProperties().getProperty("ess_userName"));
        essProps.put(ODIConstants.PASSWORD, getProperties().getProperty("ess_password"));
        essProps.put(ODIConstants.APPLICATION_NAME, getProperties().getProperty("ess_appName"));
        essProps.put(ODIConstants.DATABASE_NAME, getProperties().getProperty("ess_cubeName"));

        return essProps;
    }

    /**
     * 获取数据库连接信息。
     * 
     * @return 数据库连接信息的HashMap。
     * @throws SQLException 如果数据库连接失败。
     * @throws IOException 
     */
    public static HashMap<String, Object> getTargrtDatabaseProp() throws SQLException, IOException {
        // 获取临时数据库连接属性
        HashMap<String, Object> databaseMap = new HashMap<>();
        String dbUrl = getProperties().getProperty("target_db_url");
        String dbUserName = getProperties().getProperty("target_db_username");
        String dbPassword = getProperties().getProperty("target_db_password");
        String targetTable = getProperties().getProperty("target_db_table");
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
     * @throws SQLException 如果获取连接失败，则抛出 SQLException
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    public static Connection getSrcDatabaseProp() throws SQLException, IOException, ClassNotFoundException {
        // 获取到数据库连接
        String dbUrl = getProperties().getProperty("src_db_url");
        String dbUserName = getProperties().getProperty("src_db_username");
        String dbPassword = getProperties().getProperty("src_db_password");
        // Class.forName("oracle.jdbc.OracleDriver");
        Connection conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);

        return conn;
    }
}