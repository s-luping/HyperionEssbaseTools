import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.essbase.api.base.EssException;
import com.essbase.api.dataquery.IEssCubeView;
import com.essbase.api.dataquery.IEssGridView;
import com.essbase.api.dataquery.IEssOpZoomIn;
import com.essbase.api.dataquery.IEssOperation;
import com.essbase.api.domain.IEssDomain;
import com.essbase.api.session.IEssbase;

public class CellQueryTest {

        public static void getEssbaseDataByValue() throws Exception {
        PropertyLoader prop = new PropertyLoader();
        Properties properties = prop.getProperties();
        List<List<String>> columns = initializeColumns();
        IEssbase ess = IEssbase.Home.create(IEssbase.JAPI_VERSION);
        IEssDomain dom = ess.signOn(properties.getProperty("ess_userName"), 
        properties.getProperty("ess_password"), 
        false, null, 
        properties.getProperty("ess_provider"));
        IEssCubeView cv = dom.openCubeView("Essbase Query", 
        properties.getProperty("ess_olapSvrName"), 
        properties.getProperty("ess_appName"), 
        properties.getProperty("ess_cubeName"));
        cv = configureCubeView(cv);

        performCubeViewOperation(cv, columns);
    }
    private static IEssCubeView configureCubeView(IEssCubeView cv) {
        try {
            cv.setIncludeSelection(true);
            cv.setSuppressMissing(true);
            cv.setSuppressZero(true);
            cv.setDrillLevel(IEssOpZoomIn.EEssZoomInPreference.BOTTOM_LEVEL);
            cv.updatePropertyValues();
        } catch (EssException e) {
            e.printStackTrace();
        }
        
        return cv;
    }

    private static List<List<String>> initializeColumns() {
        // List<List<String>> columns
        //构建cells
        List<List<String>> table = new ArrayList<>();
        List<String> column = new ArrayList<>();
        List<String> row = new ArrayList<>();
        column.add("Fy24");
        column.add("Feb");
        column.add("Actual");
        column.add("Final");
        column.add("EETotal");
        column.add("CNY");
        column.add("BCTotal");
        column.add("PHTotal");
        column.add("PJTotal");
        column.add("NOM");
        column.add("NOSP1");
        column.add("NOSP2");
        column.add("NOSP3");
        table.add(column);
        row.add("AA0001");
        row.add("AA0002");
        table.add(row);
        return table;
    }
    private static void performCubeViewOperation(IEssCubeView cv, List<List<String>> columns) throws EssException {
        IEssGridView grid = cv.getGridView();

        setEssbaseGridView(grid,columns);
        IEssOperation op = null;

        op = cv.createIEssOpRetrieve();
        cv.performOperation(op);
        int cntRows = grid.getCountRows();
        int cntCols = grid.getCountColumns();

        System.out.println("Result");
       for (int i = 0; i < cntRows; i++) {
           for (int j = 0; j < cntCols; j++) {
               System.out.print(grid.getValue(i, j)+"\t");
           }
       }

    }

    public static void setEssbaseGridView(IEssGridView grid, List<List<String>> table) throws EssException {
        //设置grid 表格大小 多少行 多少列
        // grid.setSize(table.get(0).size(),table.get(1).size());
        // for (int j = 0; j < table.get(0).size(); j++) {
        //     //设置 表格 表头 行 列 数值
        //     grid.setValue(j, 1, table.get(0).get(j));
        // }
       for (int rc = 0; rc < table.get(1).size(); rc++) {
           for (int cc = 0; cc < table.get(0).size(); cc++) {
            //    titleRow = columns.get(i - 1).size();
               //设置 表格 表头 行 列 数值
               grid.setValue(rc, cc, table.get(rc).get(cc));
           }
       }

    }

}