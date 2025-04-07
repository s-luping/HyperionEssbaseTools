
public class Main {
    public static void main(String[] args) throws Exception {
        // 获取参数
        if (args.length < 1) {
            System.out.println("please at least input type of operation on essbase：");
            System.out.println("load data args[0] 1");
            System.out.println("extract data args[0] 2");
            throw new Exception();
        }

        if (args[0].equals("1")) {
            System.out.println("开始加载数据到Essbase...");
            // 打印参数
            System.out.println("BATCH_NAME: " + args[1]);
            System.out.println("YEARS: " + args[2]);
            System.out.println("PERIODS: " + args[3]);
            System.out.println("LOAD_FILE_NAME: " + args[4]);
            HyperionEssbaseLoad.loadMain(args);
        } else if (args[0].equals("2")) {
            System.out.println("开始从Essbase提取数据...");
            HyperionEssbaseExtract.extractMain(args[1]);
        }
    }
}
