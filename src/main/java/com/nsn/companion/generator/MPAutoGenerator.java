package com.nsn.companion.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Collections;

@Component
public class MPAutoGenerator {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://localhost:3306/companion?serverTimezone=UTC", "companion", "passok");

    /**
     * 执行 run
     */
    public static void main(String[] args) throws SQLException {
        //取当前项目根目录
        String rootPath = System.getProperty("user.dir");
        String outputDir = rootPath + "/src/main/java";

        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("jiangqp") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride()// 覆盖已生成文件
                            .outputDir(outputDir); // 指定输出目录
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.nsn.companion")// 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D://")); // 设置mapperXml生成路径
                })
                // 策略配置
                .strategyConfig(builder -> {
                    builder.addInclude("category") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                // 策略配置
                .strategyConfig(builder -> {
                    builder.entityBuilder()
                            .enableLombok(); // 设置开启 lombok 模型
                })
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                   .templateEngine(new BeetlTemplateEngine())
                   .templateEngine(new FreemarkerTemplateEngine())
                 */
                .execute();
    }
}
