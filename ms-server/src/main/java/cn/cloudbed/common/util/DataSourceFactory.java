package cn.cloudbed.common.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.levigo.jbig2.util.log.Logger;
import com.levigo.jbig2.util.log.LoggerFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


public class DataSourceFactory {
    public static final String DBTYPE_MYSQL = "mysql";
    public static final String DBTYPE_POSTGRES = "postgres";

    public static String DBType = DBTYPE_POSTGRES;

    public static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
    public static final String DRIVER_POSTGRES = "org.postgresql.Driver";
    public static String DriverName;

    public static final String URLTemplate = "jdbc:{dbtype}://{ip}:{port}/{database}?characterEncoding=utf-8";

    private static final Logger LOGGER= LoggerFactory.getLogger(DataSourceFactory.class);

    /*
     * 创建连接池DruidDataSource
     */
    public static Map<String, DruidDataSource> dataSourceMap = new HashMap<>();

    /*
     * 返回连接池对象
     */
    public static  DataSource getDataSource(String dbType, String ip, String port, String dbName, String uname, String pword) {

        synchronized(DataSourceFactory.class) {
            String defaultDbType = DBTYPE_POSTGRES;
            String defaultIp = "localhost";
            String defaultPort = "5432";
            String defaultDbName = "postgres";
            String username = "postgres";
            String password = "postgres";
            String URL=URLTemplate;

            if (dbType != null && (DBTYPE_MYSQL.equals(dbType) || DBTYPE_POSTGRES.equals(dbType))) {
                switch (dbType) {
                    case DBTYPE_MYSQL:
                        DriverName = DRIVER_MYSQL;
                        URL = URL.replace("{dbtype}", "mysql");
                        break;
                    case DBTYPE_POSTGRES:
                        DriverName = DRIVER_POSTGRES;
                        URL = URL.replace("{dbtype}", "postgresql");
                        break;
                }
            }
            if (ip != null)
                URL = URL.replace("{ip}", ip);
            if (port != null)
                URL = URL.replace("{port}", port);
            if (dbName != null)
                URL = URL.replace("{database}", dbName);
            if (uname != null)
                username = uname;
            if (pword != null)
                password = pword;

            LOGGER.debug("::初始化数据库连接："+URL);
            if (dataSourceMap.get(URL) != null) {
                return dataSourceMap.get(URL);
            }
            DruidDataSource dataSource = new DruidDataSource();
            //对连接池对象 进行基本的配置
            dataSource.setUrl(URL); //指定要连接的数据库地址
            dataSource.setUsername(username); //指定要连接数据的用户名
            dataSource.setPassword(password); //指定要连接数据的密码
            dataSourceMap.put(URL, dataSource);
            return dataSource.cloneDruidDataSource();
        }
    }


}