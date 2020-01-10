package org.ftd.educational.oldschool.dao.samples;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.ftd.educational.oldschool.dao.abstracts.AbstractDAO;
import org.ftd.educational.oldschool.dao.abstracts.AbstractEntity;

/**
 *
 * @author Fabio Tavares Dippold
 * @version 2017-03-07 - 2.0.1
 *
 */
public class UserDAO extends AbstractDAO {

    public UserDAO(Connection conn, String tableName) {
        super(conn, tableName);
    }

    @Override
    public AbstractEntity fillObject(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("ID"));
        user.setName(rs.getString("NAME"));
        user.setCreatedIn(rs.getDate("CREATEDIN"));
        
        return user;
    }

    @Override
    public AbstractEntity getNewInstance() {
        User o = new User();
        o.setCreatedIn(new Date());
        
        return o;
    }
    
}
