import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Logger;

import com.hyperion.odi.common.ODIConstants;
import com.hyperion.odi.connection.HypAppConnectionFactory;
import com.hyperion.odi.connection.IHypAppReader;

/**
 * 从Essbase提取数据的类。
 */
public class HyperionEssbaseExtract {
    private static final Logger logger = Logger.getLogger(HyperionEssbaseExtract.class.getName());

    /**
     * 主要的提取方法。
     */
    public static void extractMain() {
        try {
            logger.info("begin ------------------------");
            // 获取Essbase连接信息
            HashMap<String, Object> srcProps = new HashMap<>();
            srcProps = ConnProperties.getEssbaseProp();
            srcProps.put(ODIConstants.READER_TYPE, "DATA_READER");
            logger.info("获取Essbase连接信息");

            // 初始化Essbase应用读取器并连接
            IHypAppReader essbaseAppReader = HypAppConnectionFactory.getAppReader(HypAppConnectionFactory.APP_ESSBASE,
                    srcProps);
            logger.info("初始化Essbase应用读取器并连接");

            // 获取数据库连接信息
            HashMap<String, Object> databaseMap = ConnProperties.getTargrtDatabaseProp();
            logger.info("目标数据库连接信息");

            // 获取提取选项
            HashMap<String, Object> extractOptions = new HashMap<>();
            extractOptions = getExtractOptions(databaseMap);
            essbaseAppReader.beginExtract(extractOptions);
            logger.info("获取提取选项");

            logger.info(String.valueOf(essbaseAppReader.extract()));
            logger.info("提取状态");
            System.out.println("end ------------------------");
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        logger.info("end final------------------------");
    }


    /**
     * 获取提取选项。
     * 
     * @param databaseMap 包含数据库连接信息的HashMap。
     * @return 提取选项的HashMap。
     * @throws SQLException 如果处理提取选项时发生SQL异常。
     * @throws IOException 
     */
    public static HashMap<String, Object> getExtractOptions(HashMap<String, Object> databaseMap) throws SQLException, IOException {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd_hh_mm_ss");
        // 提取LKM选项
        String preMaxl = "";
        String postMaxl = "";
        String extractionType = "ReportScript";
        String extractionQuery = "ESS_EX2";
        String colDelimiter = "\t";
        String dataFileInCalc = "";
        String preCalcScript = "";
        String logFileName = "./ESS_EXP_"+ft.format(new java.util.Date())+".log";
        
        // 错误记录处理设置
        int maxErrors = 1;
        String errFileName = "./ESS_EXP_"+ft.format(new java.util.Date())+".err";
        String errColDelimiter = ",";
        String errRowDelimiter = "\r\n";
        String errTextDelimiter = "\"";
        
        // 设置提取选项
        HashMap<String, Object> extractOptions = new HashMap<>();
        // 通用ODI知识模块选项
        extractOptions.put(ODIConstants.LOG_ENABLED, true);
        extractOptions.put(ODIConstants.LOG_FILE_NAME, logFileName);
        extractOptions.put(ODIConstants.ABORT_ON_PRE_MAXL_ERROR, true);
        extractOptions.put(ODIConstants.PRE_EXTRACT_MAXL, preMaxl);
        extractOptions.put(ODIConstants.POST_EXTRACT_MAXL, postMaxl);
        extractOptions.put(ODIConstants.EXTRACTION_QUERY_TYPE, extractionType);
        extractOptions.put(ODIConstants.EXTRACTION_QUERY_FILE, extractionQuery);
        extractOptions.put(ODIConstants.EXT_COL_DELIMITER, colDelimiter);
        extractOptions.put(ODIConstants.EXTRACT_DATA_FILE_IN_CALC_SCRIPT, dataFileInCalc);
        extractOptions.put(ODIConstants.PRE_CALCULATION_SCRIPT, preCalcScript);
        // 错误记录选项
        extractOptions.put(ODIConstants.MAXIMUM_ERRORS_ALLOWED, maxErrors);
        extractOptions.put(ODIConstants.LOG_ERRORS, true);
        extractOptions.put(ODIConstants.ERROR_LOG_FILENAME, errFileName);
        extractOptions.put(ODIConstants.ERR_COL_DELIMITER, errColDelimiter);
        extractOptions.put(ODIConstants.ERR_ROW_DELIMITER, errRowDelimiter);
        extractOptions.put(ODIConstants.ERR_TEXT_DELIMITER, errTextDelimiter);
        extractOptions.put(ODIConstants.ERR_LOG_HEADER_ROW, true);
        // Essbase特定加载选项
        extractOptions.put(ODIConstants.DATABASE_CONNECTION, databaseMap.get("conn"));
        extractOptions.put(ODIConstants.MAPPED_COLUMN_NAMES, databaseMap.get("mappingColumns"));
        extractOptions.put(ODIConstants.STAGING_TABLE_NAME, databaseMap.get("targetTable"));
        return extractOptions;
    }
}