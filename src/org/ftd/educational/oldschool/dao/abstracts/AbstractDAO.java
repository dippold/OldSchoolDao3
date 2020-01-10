package org.ftd.educational.oldschool.dao.abstracts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.ftd.educational.oldschool.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author Fabio Tavares Dippold
 * @version 2017-03-07 - 2.0.1
 *
 */
public abstract class AbstractDAO {
    private final String TABLE_NAME;
    private Connection conn;

    public AbstractDAO(Connection conn, String tableName) {
        this.setConn(conn);
        this.TABLE_NAME = tableName;
    }

    public synchronized void create(AbstractEntity o) throws SQLException {
        PreparedStatement pst = null;
        try {
            pst = this.getStmtToCreate(o);
            if (pst.executeUpdate() > 0) {
                ResultSet generatedKeys = pst.getGeneratedKeys();
                if (null != generatedKeys && generatedKeys.next()) {
                    o.setId(generatedKeys.getLong(1));
                }
            } else {
                throw new SQLException("Creating " + this.TABLE_NAME + " failed, no rows affected.");
            }
        } finally {
            this.close(pst);
        }
    }

    public void edit(AbstractEntity o) throws SQLException, NonexistentEntityException {
        PreparedStatement pst = null;
        try {
            pst = this.getStmtToEdit(o);
            int rowcount = pst.executeUpdate();
            if (rowcount == 0) {
                throw new NonexistentEntityException("Object could not be saved! (PrimaryKey not found)");
            }
            if (rowcount > 1) {
                throw new SQLException("PrimaryKey Error when updating DB! (Many objects were affected!)");
            }
        } finally {
            this.close(pst);
        }
    }

    public List<AbstractEntity> findAll() throws SQLException {
        List<AbstractEntity> searchResults = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = this.getConn().prepareStatement(this.getQueryToFindAll());
            rs = pst.executeQuery();
            while (rs.next()) {
                searchResults.add(this.fillObject(rs));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            this.close(rs, pst);
        }

        return searchResults;
    }

    public AbstractEntity find(Long id) throws NonexistentEntityException, SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        AbstractEntity o = null;
        try {
            pst = this.getConn().prepareStatement(this.getQueryToFind());
            pst.setLong(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                o = this.fillObject(rs);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            this.close(rs, pst);
        }

        return o;
    }

    public void destroy(Long id) throws NonexistentEntityException, SQLException {
        PreparedStatement pst = null;
        try {
            pst = this.getConn().prepareStatement(this.getQueryToDestroy());
            pst.setLong(1, id);
            int rowcount = pst.executeUpdate();
            if (rowcount == 0) {
                throw new NonexistentEntityException("Object could not be deleted! (PrimaryKey not found)");
            }
            if (rowcount > 1) {
                throw new SQLException("PrimaryKey Error when updating DB! (Many objects were deleted!)");
            }
        } finally {
            this.close(pst);
        }
    }

    public void destroyAll(Connection conn) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = this.getConn().prepareStatement(this.getQueryToDestroyAll());
            int rowcount = this.databaseUpdate(conn, stmt);
        } finally {
            this.close(stmt);
        }
    }

    public int count() throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        int allRows = 0;
        try {
            pst = this.conn.prepareStatement(this.getQueryToCount());
            rs = pst.executeQuery();
            if (rs.next()) {
                allRows = rs.getInt("count");
            }
            return allRows;
        } catch (SQLException e) {
            throw e;
        } finally {
            this.close(rs, pst);
        }
    }

    /*
        UTILITIES MEMBERS...
     */
    /**
     * databaseUpdate-method. This method is a helper method for internal use.
     * It will execute all database handling that will change the information in
     * tables. SELECT queries will not be executed here however. The return
     * value indicates how many rows were affected. This method will also make
     * sure that if cache is used, it will reset when data changes.
     *
     * @param conn This method requires working database connection.
     * @param stmt This parameter contains the SQL statement to be excuted.
     * @return
     * @throws java.sql.SQLException
     */
    protected int databaseUpdate(Connection conn, PreparedStatement stmt) throws SQLException {
        int result = stmt.executeUpdate();

        return result;
    }

    protected PreparedStatement getStmtToCreate(AbstractEntity o) throws SQLException {
        //String query = "INSERT INTO " + this.TABLE_NAME + " (NAME) VALUES(?)";
        PreparedStatement pst = this.getConn().prepareStatement(this.getQueryToCreate(), Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, o.getName());

        return pst;
    }

    protected PreparedStatement getStmtToEdit(AbstractEntity o) throws SQLException {
        //String query = "UPDATE " + this.TABLE_NAME + " SET NAME = ? WHERE (ID = ?)";
        PreparedStatement pst = this.getConn().prepareStatement(this.getQueryToEdit());
        pst.setString(1, o.getName());
        pst.setLong(2, o.getId());

        return pst;
    }
    
    protected void closeConnection() throws SQLException {
        try {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    protected void close(ResultSet rs) throws SQLException {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    protected void close(PreparedStatement pst) throws SQLException {
        try {
            if (pst != null && !pst.isClosed()) {
                pst.close();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    protected void close(ResultSet rs, PreparedStatement pst) throws SQLException {
        try {
            this.close(rs);
            this.close(pst);
        } catch (SQLException e) {
            throw e;
        }
    }

    protected void closeAll(ResultSet rs, PreparedStatement pst) throws SQLException {
        try {
            this.close(rs);
            this.close(pst);
            this.closeConnection();
        } catch (SQLException e) {
            throw e;
        }
    }    
    

    /*
        ABSTRACTS MEMBERS...
     */
    public abstract AbstractEntity getNewInstance();

    public abstract AbstractEntity fillObject(ResultSet rs) throws SQLException;

    /*
        GETTERS AND SETTERS MEMBERS...
     */
    protected Connection getConn() {
        return conn;
    }

    protected final void setConn(Connection conn) {
        this.conn = conn;
    }

    protected String getTableName() {
        return TABLE_NAME;
    }

    protected String getQueryToCreate() {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(this.TABLE_NAME);
        sb.append(" (NAME) VALUES(?) ");
        
        return sb.toString();
    }
    
    protected String getQueryToEdit() {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(this.TABLE_NAME);
        sb.append(" SET NAME = ? WHERE (ID = ?)");
        
        return sb.toString();
    }
        
    protected String getQueryToFindAll() {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(this.TABLE_NAME);
        sb.append(" ORDER BY ID ASC");
        
        return sb.toString();        
    }

    protected String getQueryToFind() {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(this.TABLE_NAME);
        sb.append(" WHERE ID=?");
        
        return sb.toString();        
    }
    
    protected String getQueryToDestroy() {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(this.TABLE_NAME);
        sb.append(" WHERE ID=?");
        
        return sb.toString();        
    }    
    
    protected String getQueryToDestroyAll() {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(this.TABLE_NAME);
        
        return sb.toString();        
    }    
    
    protected String getQueryToCount() {
        StringBuilder sb = new StringBuilder("SELECT COUNT(*) AS count FROM ");
        sb.append(this.TABLE_NAME);
        
        return sb.toString();        
    }    
    
    /* DAO-GEN GENERIC MEMBERS... */
    /**
     * getObject-method. This will create and load valueObject contents from
     * database using given Primary-Key as identifier. This method is just a
     * convenience method for the real load-method which accepts the valueObject
     * as a parameter. Returned valueObject will be created using the
     * createValueObject() method.
     *
     * @param conn
     * @param id
     * @return
     * @throws
     * org.ftd.educational.oldschool.dao.exceptions.NonexistentEntityException
     * @throws java.sql.SQLException
     */
    protected AbstractEntity getObject(Connection conn, Long id) throws NonexistentEntityException, SQLException {
        AbstractEntity o = this.getNewInstance();
        o.setId(id);
        this.load(conn, o);

        return o;
    }

    /**
     * load-method. This will load valueObject contents from database using
     * Primary-Key as identifier. Upper layer should use this so that
     * valueObject instance is created and only primary-key should be specified.
     * Then call this method to complete other persistent information. This
     * method will overwrite all other fields except primary-key and possible
     * runtime variables. If load can not find matching row, NotFoundException
     * will be thrown.
     *
     * @param conn This method requires working database connection.
     * @param o
     * @throws
     * org.ftd.educational.oldschool.dao.exceptions.NonexistentEntityException
     * @throws java.sql.SQLException
     */
    protected void load(Connection conn, AbstractEntity o) throws NonexistentEntityException, SQLException {
        String query = "SELECT * FROM teste WHERE (ID = ? ) ";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(query);
            stmt.setLong(1, o.getId());
            singleQuery(conn, stmt, o);
        } finally {
            this.close(stmt);
        }
    }

    /**
     * databaseQuery-method. This method is a helper method for internal use. It
     * will execute all database queries that will return only one row. The
     * resultset will be converted to valueObject. If no rows were found,
     * NotFoundException will be thrown.
     *
     * @param conn This method requires working database connection.
     * @param stmt This parameter contains the SQL statement to be excuted.
     * @param o
     * @throws
     * org.ftd.educational.oldschool.dao.exceptions.NonexistentEntityException
     * @throws java.sql.SQLException
     */
    protected void singleQuery(Connection conn, PreparedStatement stmt, AbstractEntity o)
            throws NonexistentEntityException, SQLException {
        ResultSet result = null;
        try {
            result = stmt.executeQuery();
            if (result.next()) {
                o.setId(result.getLong("ID"));
                o.setName(result.getString("NAME"));
                o.setCreatedIn(result.getDate("CREATEDIN"));
            } else {
                throw new NonexistentEntityException("Object Not Found!");
            }
        } finally {
            this.close(result, stmt);
        }
    }

    /**
     * databaseQuery-method. This method is a helper method for internal use. It
     * will execute all database queries that will return multiple rows. The
     * resultset will be converted to the List of valueObjects. If no rows were
     * found, an empty List will be returned.
     *
     * @param conn This method requires working database connection.
     * @param stmt This parameter contains the SQL statement to be excuted.
     * @return
     * @throws java.sql.SQLException
     */
    protected List<AbstractEntity> listQuery(Connection conn, PreparedStatement stmt) throws SQLException {
        List<AbstractEntity> searchResults = new ArrayList();
        ResultSet result = null;
        try {
            result = stmt.executeQuery();
            while (result.next()) {
                AbstractEntity o = this.getNewInstance();
                searchResults.add(this.fillObject(result));
            }
        } finally {
            if (result != null) {
                result.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        return searchResults;
    }

    /**
     * searchMatching-Method. This method provides searching capability to get
     * matching valueObjects from database. It works by searching all objects
     * that match permanent instance variables of given object. Upper layer
     * should use this by setting some parameters in valueObject and then call
     * searchMatching. The result will be 0-N objects in a List, all matching
     * those criteria you specified. Those instance-variables that have NULL
     * values are excluded in search-criteria.
     *
     * @param conn This method requires working database connection.
     * @param o
     * @return
     * @throws java.sql.SQLException
     */
    public List<AbstractEntity> searchMatching(Connection conn, AbstractEntity o) throws SQLException {
        List<AbstractEntity> searchResults;
        boolean first = true;

        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(this.TABLE_NAME);
        sql.append(" WHERE 1=1 ");

        if (o.getId() != 0) {
            if (first) {
                first = false;
            }
            sql.append("AND ID = ");
            sql.append(o.getId());
            sql.append(" ");
        }

        if (o.getName().equals("") && o.getName() != null) {
            if (first) {
                first = false;
            }
            sql.append("AND NAME LIKE '");
            sql.append(o.getName());
            sql.append("%' ");
        }

        sql.append("ORDER BY ID ASC ");

        // Prevent accidential full table results.
        // Use loadAll if all rows must be returned.
        if (first) {
            searchResults = new ArrayList();
        } else {
            searchResults = listQuery(conn, conn.prepareStatement(sql.toString()));
        }

        return searchResults;
    }

}
