工具作用
以往海波龙预算接口开发都严重依赖ODI，过程繁琐，
此工具可直接对Essbase数据库进行数据导入导出

如何使用

1、维护Essbase连接信息
2、维护config.properties 中ess_dimensions，其中年月维度字段必须为：YEARS,PERIODS
3、在EAS控制台创建用于导出的报表脚本和用于导入的规则文件
3、运行命令：

    导入：Usage: java -Xbootclasspath/a:lib/ojdbc8.jar; -jar essbasetools.jar 1 <batch_name> <year_num> <period_num> <load_file_name>
    导出：Usage: java -Xbootclasspath/a:lib/ojdbc8.jar; -jar essbasetools.jar 2 <extrat_file_name>

## 特别注意
1、数据库创建的字段顺序与导入规则和导出规则的顺序一定保持一致，否则无法导出导入

例如
规则文件顺序为：YEARS,PERIODS,SCENARIO,VERSION,ENTITY,PROJECT,ACCOUNT,CURRENCY,BUDGETCENTER,MISC,PHASE,SPARE1,SPARE2,SPARE3,DATA

导入表字段至少包含：BATCH_NAME,YEARS,PERIODS,SCENARIO,VERSION,ENTITY,PROJECT,ACCOUNT,CURRENCY,BUDGETCENTER,MISC,PHASE,SPARE1,SPARE2,SPARE3,DATA
导出表字段至少包含：YEARS,PERIODS,SCENARIO,VERSION,ENTITY,PROJECT,ACCOUNT,CURRENCY,BUDGETCENTER,MISC,PHASE,SPARE1,SPARE2,SPARE3,DATA



