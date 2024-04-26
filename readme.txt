工具作用
以往海波龙预算接口开发都严重依赖ODI，过程繁琐，
此工具可直接对Essbase数据库进行数据导入导出

如何使用
## 配置文件config.properties信息
1、维护Essbase连接信息
2、维护config.properties 中ess_dimensions，其中年月维度字段必须为：YEARS,PERIODS
3、运行命令：
    导入：Usage: java -jar HyperionEssbaseLoad.jar 1 <batch_name> <year_num> <period_num>
    导出：Usage: java -jar HyperionEssbaseLoad.jar 2 
## 向Essbase中导入数据
1、维护config.properties 中Source Database Info信息
2、根据config.properties 中ess_dimensions字段在源数据库创建数据库表参考 CreateTable.sql
导出数据库数据到data.csv，其中字段顺序需与ess_dimensions排序一致
3、到Essbase数据库使用data.csv（需根据实际数据库字段修改）创建ESS_LOAD.rul规则


## 从Essbase中导出数据
1、维护config.properties 中# Target Database Info信息
2、根据config.properties 中ess_dimensions字段在源数据库创建数据库表参考 CreateTable.sql
3、在Essbase数据库EAS面板编辑ESS_EXP.rep规则其中19行需修改维度顺序与ess_dimensions排序一致


## 例
ess_dimensions=YEARS,PERIODS,SCENARIO,VERSION,ENTITY,PROJECT,ACCOUNT,CURRENCY,BUDGETCENTER,MISC,PHASE,SPARE1,SPARE2,SPARE3,DATA

则源表字段至少包含：BATCH_NAME,YEARS,PERIODS,SCENARIO,VERSION,ENTITY,PROJECT,ACCOUNT,CURRENCY,BUDGETCENTER,MISC,PHASE,SPARE1,SPARE2,SPARE3,DATA
导出表字段至少包含：YEARS,PERIODS,SCENARIO,VERSION,ENTITY,PROJECT,ACCOUNT,CURRENCY,BUDGETCENTER,MISC,PHASE,SPARE1,SPARE2,SPARE3,DATA

## Essbase数据库需要创建ESS_LOAD.rul规则和ESS_EXP.rep规则
ESS_LOAD.rul
创建data.csv文件（字段顺序与ess_dimensions排序一致）内容如下：

YEARS,PERIODS,SCENARIO,VERSION,ENTITY,PROJECT,ACCOUNT,CURRENCY,BUDGETCENTER,MISC,PHASE,SPARE1,SPARE2,SPARE3,DATA
FY22,Jan,Actual,Final,EE00,PJT00,AA000203,CNY,BC00,PH00,NOM,NOSP1,NOSP2,NOSP3,1120263.67

在EAS界面中创建ESS_LOAD.rul规则

ESS_EXP.rep
在EAS界面中创建ESS_EXP.rep规则，内容如下：
//ESS_LOCALE SimplifiedChinese_China.MS936@Binary
{
TABDELIMIT //TABDELIMIT命令在列之间放置制表符
DECIMAL 2 //小数位
ROWREPEAT //文本不聚合
SUPHEADING //禁止在每页顶部显示默认标题
SUPBRACKETS //禁止在负数周围显示圆括号
NOINDENTGEN //不缩进
SUPCOMMAS //禁止在大于999的数字中显示逗号
SUPFEED //每当页面上的行数超过当前PAGELENGTH设置时，禁止自动插入物理分页符
//OUTALTNAMES//输出别名
SUPMISSINGROWS //过滤MISSING
SUPZEROROWS  //过滤0
//MISSINGTEXT  0
}
<ROW ( "YEAR","PERIOD","SCENARIO","VERSION","ENTITY","PROJECT","ACCOUNT","CURRENCY","BUDGETCENTER","MISC","PHASE","SPARE1","SPARE2","SPARE3") 
/*Account*/ 
//<IDESCENDANTS "AA00" /*经营类科目*/
"AA0001"/*收入合计*/
"AA000101"/*外部收入*/
"AA000102"/*内部收入*/
"AA0002" /*变动成本合计 */
"AA000201"/*收入分成*/
"AA000202"/*内部收入分成*/
"AA000203"/*二道税*/
"AA000204"/*渠道费*/
"AA000205"/*影院采购成本*/
"AA000206"/*其他外部交易成本*/
"AA0003"/*费用合计*/
"AA000301"/*薪酬及福利费*/
"AA00030105"/*其他奖金*/
"AA00030106"/*项目奖金*/
"AA000302"/*广告及市场费*/
"AA00030202"/*市场广告-搜索导航*/
"AA00030203"/*市场广告-用户集中*/
"AA000303"/*内容及版权费*/
"AA00030301"/*版权金总额*/
"AA00030302"/*外包费*/
"AA00030312"/*视频许可证*/
"AA000304"/*通讯及带宽*/
"AA000304"/*通讯及带宽费*/
"AA000305"/*差旅及应酬费*/
"AA000306"/*其他员工福利费*/
"AA000307"/*专业咨询费*/
"AA000308"/*办公设施费*/
"AA000309"/*会议费*/
"AA000310"/*培训费*/
"AA000311"/*办公费*/
"AA000312"/*折旧费*/
"AA000313"/*坏账损失*/
"AA000314"/*其他费用*/
"AA000601"/*BU内分摊*/
"AA0008"/*所得税*/
"AA0009"/*利息收入/费用*/
"AA0010"/*其他收入/费用*/
"AA0108"/*HC*/
"AA0611"/*项目利润*/
"AA0612"/*净收入*/
/*Periods*/
&CurMonth 
/*Years*/
&ActYear
/*Project*/
"PJT00" 
"PJTotal"/*项目汇总*/
/*Entity*/
//<IDESCENDANTS "EETotal"
"EETotal"
/*Scenario*/
"AnnualBudget"
/*Version*/
"Final"
/*Spare1*/
 "NOSP1"
/*Spare2*/
 "NOSP2" "SP201"
/*Spare3*/
 "NOSP3"
/*Phase*/
 "PHTotal"
/*BudgetCenter*/
 "BCTotal"
/*Currency*/
 "USD" "CNY"
/*Misc*/
"NOM"
 !

