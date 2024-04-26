import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
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
            essProps = ConnProperties.getEssbaseProp();
            essProps.put(ODIConstants.WRITER_TYPE, "DATA_WRITER");

            logger.info("获取 Essbase 连接信息");

            // 获取数据库连接
            Connection srcProps = ConnProperties.getSrcDatabaseProp();
            logger.info("获取 数据库 连接信息");

            IHypAppWriter essbaseAppWriter = HypAppConnectionFactory.getAppWriter(HypAppConnectionFactory.APP_ESSBASE, essProps);
            logger.info("初始化 Essbase 应用写入器并建立连接");

            // 加载数据
            HashMap<String, Object> loadOptions = new HashMap<>();
            loadOptions = getLoadOptions();
            essbaseAppWriter.beginLoad(loadOptions);
            logger.info("获取加载选项");
            
            String loadString = loadData(srcProps, essbaseAppWriter,args);
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
     * @param srcConn 数据库连接
     * @param essbaseAppWriter Essbase 应用写入器
     * @return 加载状态信息
     * @throws Exception 
     */
    public static String loadData(Connection srcConn, IHypAppWriter essbaseAppWriter,String args[]) throws Exception {
        // 获取查询sql
        String sqlData = queryString(args);
        System.out.println(sqlData);
        logger.info("开始加载数据 ------------------------");
        ODIStatistics status = new ODIStatistics();
        try  {
            Statement stmt2 = srcConn.createStatement();
            ResultSet rs2 = stmt2.executeQuery(sqlData);
            stmt2.setFetchSize(1);
            // 假设这里有将结果集加载到Essbase的逻辑
            status = essbaseAppWriter.loadData(rs2);
            System.out.printf("", rs2.getMetaData());;
            essbaseAppWriter.endLoad();
            rs2.close();
            stmt2.close() ;
        } catch (SQLException e) {
            logger.severe("SQL异常: " + e.getMessage());
            throw new SQLException(e); // 重新抛出异常以便调用者能够处理
        }
        return status.toString();
    }

    public static String queryString(String args[]) throws Exception {
        // 获取命令行参数
        if (args.length != 4) {
            logger.severe("ERROR:Missing parameter");
            throw new Exception("Usage: java -jar HyperionEssbaseLoad.jar <operation_type> <batch_name> <year_num> <period_num>");
        }
        // 打印参数
        System.out.println("Batch Name: " + args[1]);
        System.out.println("Year Num: " + args[2]);
        System.out.println("Period Num: " + args[3]);
    
        // 从配置文件获取维度列和数据表名称
        PropertyLoader ploader = new PropertyLoader();
        String columns = ploader.getDimensionString();
        String srcTable = ploader.getProperties().getProperty("src_db_table");
        StringBuilder sb = new StringBuilder();
        
        // 定义 SQL 语句用于从数据库加载数据
        //"'#Missing' AS \"Data\"
        sb.append("SELECT ");
        sb.append(columns);
        sb.append(" FROM ");
        sb.append(srcTable);
        sb.append(" WHERE (1=1) ");
        sb.append("AND (BATCH_NAME='"+args[1]+"' ");
        sb.append("AND YEAR_NUM='"+args[2]+"' ");
        sb.append("AND PERIOD_NUM='"+args[1]+"') ");
        return sb.toString();

    }    /**
     * 获取加载选项。
     *
     * @return 加载选项的 HashMap
     */
    public static HashMap<String, Object> getLoadOptions(){
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd_hh_mm_ss");
        // 定义并设置加载选项
        String rulesFile = "ESS_LOAD" ;
        String ruleSeparator = "," ;
        String clearDatabase = "None" ;
        String calcScript = "" ;
        int maxErrors =  1;
        String errFileName = "./ESS_LOAD_"+ft.format(new java.util.Date())+".err";
        String logFileName = "./ESS_LOAD_"+ft.format(new java.util.Date())+".log";
        String errColDelimiter = ",";
        String errRowDelimiter = "\r\n";
        String errTextDelimiter = "'";
        int commitInterval = 1000;
        String preMaxlScript = "" ;
        String postMaxlScript = "" ;
        int bufferId = 1;
        int bufferSize = 80;
        int groupId = 1;

        HashMap<String,Object> loadOptions = new HashMap<>();
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
        loadOptions.put(ODIConstants.RUN_CALC_SCRIPT_ONLY,false);
        loadOptions.put(ODIConstants.PRE_LOAD_MAXL_SCRIPT,preMaxlScript);
        loadOptions.put(ODIConstants.POST_LOAD_MAXL_SCRIPT,postMaxlScript);
        loadOptions.put(ODIConstants.ABORT_ON_PRE_MAXL_ERROR,false);
        loadOptions.put(ODIConstants.BUFFER_SIZE,bufferSize);
        loadOptions.put(ODIConstants.BUFFER_ID,bufferId);
        loadOptions.put(ODIConstants.GROUP_ID,groupId);
        loadOptions.put(ODIConstants.UNICODE_IN_DATA, false);
        return loadOptions;
    }
    
}