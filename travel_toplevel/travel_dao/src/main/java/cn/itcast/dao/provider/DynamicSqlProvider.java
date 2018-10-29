package cn.itcast.dao.provider;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class DynamicSqlProvider {

    public String findAll(@Param("rname") String rname, @Param("cid") int cid) {

        String sql = new SQL() {
            {
                SELECT("*");
                FROM("tab_route");
                StringBuffer sb = new StringBuffer();
                if (rname != null && rname.length() != 0 && !"null".equals(rname)) {
                    sb.append(" and rname like #{rname}");
                }
                if (cid != 0) {
                    sb.append(" and cid = #{cid}");
                }
                if (!sb.toString().equals("")) {
                    WHERE(sb.toString().replaceFirst("and", ""));
                }
            }
        }.toString();
        return sql;
    }

}
