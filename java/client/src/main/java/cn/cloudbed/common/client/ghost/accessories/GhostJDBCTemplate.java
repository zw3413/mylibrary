package cn.cloudbed.common.client.ghost.accessories;

import cn.cloudbed.common.util.DataSourceFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class GhostJDBCTemplate {
    private static DataSource ds;

    static String remoteIp="139.196.142.70";
    static String postgresqlDBName="mylibrary";
    static String postgresqlUsername="postgres";
    static String postgresqlPassword="Good@2020";
    static String postgresqlPort="5432";

    static String mysqlDBName="ghost_prod";
    static String mysqlUsername="root";
    static String mysqlPassword="Good@2020";
    static String mysqlPort="3306";

    public static JdbcTemplate getTemplate(DataSource dataSource){
        if(dataSource!=null) ds=dataSource;
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        jdbcTemplate.setDataSource(ds);
        return jdbcTemplate;
    }

    private static JdbcTemplate postgresqlJdbcTemplate=null;
    public static JdbcTemplate getPostgresqlJdbcTemplate(){
        if (postgresqlJdbcTemplate == null) {
            DataSource ds= DataSourceFactory.getDataSource(DataSourceFactory.DBTYPE_POSTGRES, remoteIp, postgresqlPort, postgresqlDBName, postgresqlUsername, postgresqlPassword);
            postgresqlJdbcTemplate = GhostJDBCTemplate.getTemplate(ds);
        }
        return postgresqlJdbcTemplate;
    }
    private static JdbcTemplate mysqlJdbcTemplate=null;
    public static JdbcTemplate getMysqlJdbcTemplate(){
        if (mysqlJdbcTemplate == null) {
            DataSource ds=DataSourceFactory.getDataSource(DataSourceFactory.DBTYPE_MYSQL, remoteIp, mysqlPort, mysqlDBName, mysqlUsername, mysqlPassword);
            mysqlJdbcTemplate = GhostJDBCTemplate.getTemplate(ds);
        }
        return mysqlJdbcTemplate;
    }
}
