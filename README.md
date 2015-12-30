# JxlExcel
excel reader and writer wrapped over jxl lib
##简介
　　基于jxl封装的java excel读写库，特性如下：

 - 可以像读写文件一样方便的读取数据和写入数据
 - 数据支持数组、map、bean三种方式
 - 支持xml配置excel模板

##如何使用？
###模板定义
在类路径下新建一个jxl-excel.xml的模板配置文件，输入如下的配置文件内容，即可定义一个excel模板。

    <?xml version="1.0" encoding="UTF-8" ?>
    <templates>
    <template name="testRead">
        <titleRow>
            <titleCol span="6">span标题</titleCol>
        </titleRow>
        <titleRow>
            <titleCol>编制单位（盖章）：</titleCol>
            <titleCol></titleCol>
            <titleCol span="3">编制日期：</titleCol>
            <titleCol>单位：元</titleCol>
        </titleRow>
        <titleRow>
            <titleCol>地区</titleCol>
            <titleCol>姓名</titleCol>
            <titleCol>身份证</titleCol>
            <titleCol>账号</titleCol>
            <titleCol>金额</titleCol>
            <titleCol>备注</titleCol>
        </titleRow>
        <dataRow>
            <dataCol>area</dataCol>
            <dataCol>name</dataCol>
            <dataCol>idCard</dataCol>
            <dataCol>bankAccount</dataCol>
            <dataCol>amount</dataCol>
            <dataCol>remark</dataCol>
        </dataRow>
    </template>
    </templates>

 - 模板（template）

      template元素用于定义一个模板，包含一个属性name，用于唯一标识该模板

 - 标题行（tittleRow）

   使用titleRow可以定义多行标题，每行标题又包含多个标题列（titleCol），标题列可以像html table的td一样定义span属性，表示该列占用几列空间

 - 数据行（dataRow）
   
  数据行用于定义Java bean或者Map中的属性在模板中的显示或读写顺序，数据行中的dataCol不支持span

###读数据

    InputStream is = JxlExcelReaderTest.class
                .getResourceAsStream("/testRead.xls");
    JxlExcelReader reader = new JxlExcelReader(is);
    reader.setExcelTemplate("testRead");
    List<String[]> datas = reader.readArrays();
    //List<Account> beans = reader.readBeans(Account.class);
    //List<Map<String, Object>> maps = reader.readMaps();         

###写数据
    File tmp = new File("testWriteData.xls");
    if (tmp.exists()) {
	tmp.delete();
    }
    tmp.createNewFile();
    JxlExcelWriter jxlExcelWriter = new JxlExcelWriter(tmp);
    jxlExcelWriter.setExcelTemplate("testRead");
    List<String[]> accounts = new ArrayList<String[]>();
    //省略测试数据生成...根据不同需要生成不同类型的数据
    jxlExcelWriter.writeArrays(accounts);
    //jxlExcelWriter.writeBeans(accounts);
    //jxlExcelWriter.writeMaps(accounts);

###生成excel模板
    File tmp = new File("testWriteTemplate.xls");
    if (tmp.exists()) {
	tmp.delete();
    }
    tmp.createNewFile();
    JxlExcelWriter jxlExcelWriter = new JxlExcelWriter(tmp);
    jxlExcelWriter.setExcelTemplate("testRead");
    jxlExcelWriter.writeTemplate();

##更多内容请关注
[XetLab][1]

  [1]: http://www.xetlab.com
