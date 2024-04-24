工具作用
以往海波龙预算接口开发都严重依赖ODI，过程繁琐，
此工具可直接对Essbase数据库进行数据导入导出

如何使用
## 配置文件config.properties信息
1、维护Essbase连接信息
2、维护config.properties 中ess_dimensions

## 向Essbase中导入数据
1、维护config.properties 中Source Database Info信息
2、根据config.properties 中ess_dimensions字段在源数据库创建数据库表参考 CreateTable.sql
导出数据库数据到data.csv，其中字段顺序需与ess_dimensions排序一致
3、到Essbase数据库使用data.csv（需根据实际数据库字段修改）创建ESS_LOAD.rul规则


## 从Essbase中导出数据
1、维护config.properties 中# Target Database Info信息
2、根据config.properties 中ess_dimensions字段在源数据库创建数据库表参考 CreateTable.sql
3、在Essbase数据库EAS面板编辑ESS_EXP.rep规则其中19行需修改维度顺序与ess_dimensions排序一致