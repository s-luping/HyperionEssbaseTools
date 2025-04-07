import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import com.hyperion.odi.common.ODIConstants;
import com.hyperion.odi.common.ODIStatistics;
import com.hyperion.odi.connection.HypAppConnectionFactory;
import com.hyperion.odi.connection.IHypAppWriter;

/**
 * 类 LoadDataToEssbase 用于加载数据到 Essbase 应用。
 */
public class HyperionEssbaseLoad {
    // 日志记录器
    private static final Logger logger = Logger.getLogger(HyperionEssbaseLoad.class.getName());

    /**
     * 主方法，负责从数据库加载数据并将其写入到 Essbase 应用。
     */
    public static void loadMain(String args[]) {
        try {
            logger.info("开始 ------------------------");
            // 获取 Essbase 连接信息
            HashMap<String, Object> essProps = new HashMap<>();
            essProps = ConnProperties.EssbaseConnection();
            essProps.put(ODIConstants.WRITER_TYPE, "DATA_WRITER");

            logger.info("获取 Essbase 连接信息");

            // 获取数据库连接和查询信息
            HashMap<String, Object> srcProps = ConnProperties.getSrcDatabaseProp(args);
            logger.info("获取 数据库 连接信息");

            IHypAppWriter essbaseAppWriter = HypAppConnectionFactory.getAppWriter(HypAppConnectionFactory.APP_ESSBASE,
                    essProps);
            logger.info("初始化 Essbase 应用写入器并建立连接");

            // 加载数据
            HashMap<String, Object> loadOptions = new HashMap<>();
            loadOptions = getLoadOptions();
            essbaseAppWriter.beginLoad(loadOptions);
            logger.info("获取加载选项");

            String loadString = loadData(srcProps, essbaseAppWriter, args);
            logger.info(loadString);
        } catch (Exception e) {
            logger.severe("异常: " + e.getMessage());
            e.printStackTrace(); // 仅在开发阶段打印堆栈跟踪
        }
        logger.info("结束 ------------------------");
    }

    /**
     * 从数据库加载数据并写入 Essbase。
     *
     * @param srcConn          数据库连接
     * @param essbaseAppWriter Essbase 应用写入器
     * @return 加载状态信息
     * @throws Exception
     */
    public static String loadData(HashMap<String, Object> srcProps, IHypAppWriter essbaseAppWriter, String args[])
            throws Exception {
        // 获取查询sql
        String querySQL = srcProps.get("querysql").toString();
        System.out.println(querySQL);
        Connection conn = (Connection) srcProps.get("conn");
        logger.info("开始加载数据 ------------------------");
        ODIStatistics status = new ODIStatistics();
        try {
            Statement stmt2 = conn.createStatement();
            stmt2.setFetchSize(1);
            ResultSet rs2 = stmt2.executeQuery(querySQL);
            // ResultSet rs1 = stmt2.executeQuery(sql_Missing);
            // 假设这里有将结果集加载到Essbase的逻辑
            // status = essbaseAppWriter.loadData(rs1);
            status = essbaseAppWriter.loadData(rs2);
            essbaseAppWriter.endLoad();
            rs2.close();
            stmt2.close();
        } catch (SQLException e) {
            logger.severe("SQL异常: " + e.getMessage());
            throw new SQLException(e); // 重新抛出异常以便调用者能够处理
        }
        return status.toString();
    }

    /**
     * 获取加载选项。
     *
     * @return 加载选项的 HashMap
     */
    public static HashMap<String, Object> getLoadOptions() {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        Properties prop = new PropertyLoader().ETLConfig();
        // 定义并设置加载选项
        String rulesFile = prop.getProperty("ess_load_rule");
        String ruleSeparator = ",";
        String clearDatabase = "None";
        String calcScript = "";
        int maxErrors = 1;
        String errFileName = "../logs/" + prop.getProperty("ess_load_rule") + ft.format(new java.util.Date()) + ".err";
        String logFileName = "../logs/" + prop.getProperty("ess_load_rule") + ft.format(new java.util.Date()) + ".log";
        String errColDelimiter = ",";
        String errRowDelimiter = "\r\n";
        String errTextDelimiter = "'";
        int commitInterval = 1000;
        String preMaxlScript = "";
        String postMaxlScript = "";
        int bufferId = 1;
        int bufferSize = 80;
        int groupId = 1;

        HashMap<String, Object> loadOptions = new HashMap<>();
        // 设置加载选项
        loadOptions.put(ODIConstants.CLEAR_DATABASE, clearDatabase);
        loadOptions.put(ODIConstants.CALCULATION_SCRIPT, calcScript);
        loadOptions.put(ODIConstants.RULES_FILE, rulesFile);
        loadOptions.put(ODIConstants.LOG_ENABLED, true);
        loadOptions.put(ODIConstants.LOG_FILE_NAME, logFileName);
        loadOptions.put(ODIConstants.MAXIMUM_ERRORS_ALLOWED, maxErrors);
        loadOptions.put(ODIConstants.LOG_ERRORS, true);
        loadOptions.put(ODIConstants.ERROR_LOG_FILENAME, errFileName);
        loadOptions.put(ODIConstants.RULE_SEPARATOR, ruleSeparator);
        loadOptions.put(ODIConstants.ERR_COL_DELIMITER, errColDelimiter);
        loadOptions.put(ODIConstants.ERR_ROW_DELIMITER, errRowDelimiter);
        loadOptions.put(ODIConstants.ERR_TEXT_DELIMITER, errTextDelimiter);
        loadOptions.put(ODIConstants.ERR_LOG_HEADER_ROW, false);
        loadOptions.put(ODIConstants.COMMIT_INTERVAL, commitInterval);
        loadOptions.put(ODIConstants.RUN_CALC_SCRIPT_ONLY, false);
        loadOptions.put(ODIConstants.PRE_LOAD_MAXL_SCRIPT, preMaxlScript);
        loadOptions.put(ODIConstants.POST_LOAD_MAXL_SCRIPT, postMaxlScript);
        loadOptions.put(ODIConstants.ABORT_ON_PRE_MAXL_ERROR, false);
        loadOptions.put(ODIConstants.BUFFER_SIZE, bufferSize);
        loadOptions.put(ODIConstants.BUFFER_ID, bufferId);
        loadOptions.put(ODIConstants.GROUP_ID, groupId);
        loadOptions.put(ODIConstants.UNICODE_IN_DATA, false);
        return loadOptions;
    }

}
