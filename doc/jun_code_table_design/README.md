#### 介绍
根据SQL文档 （Excel）表格 自动生成SQL创建脚本 一键执行创建数据库。

#### 特点：
1. 可根据Excel表格生成SQL文件导出到本地
2. 自动创建数据表
3. 根据数据表自动创建Mybatis Plus项目代码 包含（实体类，mapper文件与XML，service层文件，controller文件）


模板Excel.xlsx
![输入图片说明](https://011312_2b1eb642_1635778.png "屏幕截图.png")


### 编译Jar后的配置文件内容：


config.properties
```
#配置Excel导入和导出的配置
excelPath=/Users/test/Downloads/基于内容推荐的阅读APP.xlsx
#导出的文件路径
exportSqlDir=/Users/test/Downloads/
#是否导出脚本到文件
outputScriptFile=false

#是否自动构建Java项目
buildJava=true
#	Java构建配置
#Java的构建包名
buildConf.packageName=com.jun.plugin.book.app
#注释作者
buildConf.author=wujun
#要忽略生成的表名 逗号分隔
buildConf.excludeTable=

# 配置数据源 
# 是否开启自动创建数据库
autoRunScript=true
#数据源配置
dataSource.host=127.0.0.1
dataSource.port=3306
dataSource.database=book_recommed_app
dataSource.user=root
dataSource.password=wujun
```

### 下载直接用

使用说明：
解压后 配置config.properties后
直接java -jar xxxx.jar 即可





jun_code_table_design\
	SqlGeneratorExcel   整合导入导出，替换为easyexcel


https://github.com/alibaba/easyexcel
 
https://blog.csdn.net/qq_35219282/article/details/108593454
 
 
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/CellDataDemoHeadDataListener.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/CellDataReadDemoData.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/ConverterData.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/ConverterDataListener.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/CustomStringStringConverter.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/DemoDAO.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/DemoData.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/DemoDataListener.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/DemoExceptionListener.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/DemoHeadDataListener.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/ExceptionDemoData.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/IndexOrNameData.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/IndexOrNameDataListener.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/tableModelDataListener.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/ReadTest.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/TableData.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/TableDataListener2.java
/easyexcel/src/test/java/com/alibaba/easyexcel/test/table/TableTest.java
 
public class TableTest {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(TableTest.class);
 
    /**
     * 不创建对象的读
     */
    @Test
    public void noModelRead() {
        String fileName = TestFileUtil.getPath() + "demo" + File.separator + "table_designer.xlsx";
//        String fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
        // 这里 只要，然后读取第一个sheet 同步读取会自动finish
        EasyExcel.read(fileName, new TableDataListener2()).sheet().doRead();
    }
}
 
 
 
 
 
package com.alibaba.easyexcel.test.table;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.fastjson.JSON;
 
/**
* 直接用map接收数据
*
* @author Jiaju Zhuang
*/
public class TableDataListener2 extends AnalysisEventListener<Map<Integer, String>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableDataListener2.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
 
    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
System.out.println(" 行："+context.readRowHolder().getRowIndex());
System.out.println(" 对象："+data);
System.out.println("Map 结构："+context.readRowHolder().getCellMap());
       
        int sheetNo = context.readSheetHolder().getSheetNo();
        if (sheetNo == 0) {
            // XXXX表
//            GoodsReserve goodsReserve = new GoodsReserve();
            // 获取行的索引
            int index = context.readRowHolder().getRowIndex();
            // 获取该行的map数据
            Map<Integer, Cell> map = context.readRowHolder().getCellMap();
            // excel最后一行备注信息
            String remark = "备注：以上为数据填写区域，各项单元格必填。";
            // 取指定行列的值
            if (index == 1) {
                // 取所属辖区和填表日期
                String area = context.readRowHolder().getCellMap().get(1).toString();
                // 提取省市区
                area = area.replaceAll("省（直辖市、自治区）",",").replaceAll("市（州、盟）",",").replaceAll("县（市、区、旗）","").replaceAll(" ","");
                // 取填表日期
                String tableDate = context.readRowHolder().getCellMap().get(4).toString().replaceAll("填表日期：","").replaceAll(" ","");
System.out.println(" 内容1："+area+"=="+tableDate);
//                warReserve.setArea(area);
//                warReserve.setTableDate(tableDate);
            }else if (index == 3) {
                // 取仓库名称, 储备库类型, 编号, 详细地址, 管理单位
                String storeName = context.readRowHolder().getCellMap().get(0).toString();
                String storeType = context.readRowHolder().getCellMap().get(1).toString();
                String number = context.readRowHolder().getCellMap().get(2).toString();
                String address = context.readRowHolder().getCellMap().get(3).toString();
                String managementUnit = context.readRowHolder().getCellMap().get(4).toString();
System.out.println(" 内容2："+storeName+"=="+storeType+"=="+number+"=="+address+"=="+managementUnit);
//                warReserve.setStoreName(storeName);
//                warReserve.setStoreType(storeType);
//                warReserve.setNumber(number);
//                warReserve.setAddress(address);
//                warReserve.setManagementUnit(managementUnit);
            }else if (index == 6) {
                // 取经度, 纬度, 负责人姓名，负责人电话
                String longitude = context.readRowHolder().getCellMap().get(0).toString();
                String latitude = context.readRowHolder().getCellMap().get(1).toString();
                String name = context.readRowHolder().getCellMap().get(2).toString();
                String phone = context.readRowHolder().getCellMap().get(3).toString();
System.out.println(" 内容3："+longitude+"=="+latitude+"=="+name+"=="+phone);
//                warReserve.setLongitude(longitude);
//                warReserve.setLatitude(latitude);
//                warReserve.setName(name);
//                warReserve.setPhone(phone);
            }else if (index >= 9 && !map.get(0).toString().equals(remark)) {
                // 取主要储备物资中名称, 型号, 数量, 计量单位
                String goodsName = context.readRowHolder().getCellMap().get(0).toString();
                String goodsModel = context.readRowHolder().getCellMap().get(2).toString();
                String goodsNumber = context.readRowHolder().getCellMap().get(3).toString();
                String goodsUnit = context.readRowHolder().getCellMap().get(4).toString();
System.out.println(" 内容4："+goodsName+"=="+goodsModel+"=="+goodsNumber+"=="+goodsUnit);
//                goodsReserve.setGoodsName(goodsName);
//                goodsReserve.setGoodsModel(goodsModel);
//                goodsReserve.setGoodsNumber(goodsNumber);
//                goodsReserve.setGoodsUnit(goodsUnit);
//                list.add(goodsReserve);
            }
//            else if (map!=null&&map.get(0).toString().equals(remark)){
//                System.out.println(remark);
//            }
            
        }
//        

