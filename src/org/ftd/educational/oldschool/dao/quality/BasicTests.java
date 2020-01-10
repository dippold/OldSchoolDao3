package org.ftd.educational.oldschool.dao.quality;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ftd.educational.oldschool.dao.abstracts.AbstractEntity;
import org.ftd.educational.oldschool.dao.exceptions.NonexistentEntityException;
import org.ftd.educational.oldschool.dao.samples.User;
import org.ftd.educational.oldschool.dao.samples.UserDAO;
import org.ftd.educational.oldschool.dao.services.ConnectionFactory;
import org.ftd.educational.oldschool.dao.services.Schema;

/**
 *
 * @author Fabio Tavares Dippold
 * @version 2017-03-07
 * 
 */

// You can use these methods for transaction:
// ==========================================
// 1. you must create the connection object like con
// 2. conn.setAutoCommit(false);
// 3. run your queries
// 4. if all is true conn.commit();
// 5. else conn.rollback();

public class BasicTests {
    
    public static Connection conn;
    
    public static void main(String[] args) {

        conn = ConnectionFactory.getReadOnlyConnection(Schema.DATA_BASE1);
        
        try {
            
//            conn = ConnectionService.getInstance().getConnection();
//            conn.setReadOnly(true);
            
//            countUserTest();
//            deleteAllUser();
//            countUserTest();
//            createMultiUsers(1000, "Lazaro");
            countUserTest();
//            updateAllUsers();

//            createUserTest();

//              createMultiUsers(1000, "Lazaro");
//            countUserTest();
            findUser(5040L);
            findAllUsersTest();

//            conn.commit();
        } catch (SQLException | NonexistentEntityException  ex) {
            Logger.getLogger(BasicTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void findUser(Long id) throws SQLException, NonexistentEntityException {
        System.out.print("findUser(Long id): -> ");
        UserDAO dao = new UserDAO(conn, "USER");
        User o = (User) dao.find(id);
        if (o != null) {
            System.out.print("findUser(Long id): -> " + o);
        } else {
            System.out.print("findUser(Long id): -> Não encontrei o ID:" + id);
        }
    }
    
    private static void findAllUsersTest() throws SQLException {
        System.out.println("\nfindAllUsersTest(): ->");
        UserDAO dao = new UserDAO(conn, "USER");        
        List<AbstractEntity> lst = dao.findAll();
        for (AbstractEntity o:lst) {
            System.out.println(o);
        }
    }

    private static void createUserTest() throws SQLException {
        String nomes = "Fabio3;Liane3;Marli3;Rafaella3;Galateo3;Dore3";
        String[] arrayNomes = nomes.split(";");
        UserDAO dao = new UserDAO(conn, "USER");
        for (int i=1; i<arrayNomes.length; i++) {
            User o = new User();
            o.setName(arrayNomes[i]);
            o.setCreatedIn(new Date());
            dao.create(o);
            System.out.println(o);
        }
    }
    
    private static void countUserTest() throws SQLException {
        UserDAO dao = new UserDAO(conn, "USER");
        int count = dao.count();        
        System.out.println("CountUserTest(): -> Encontrei " + count + " usuários cadastrados !");
    }

    private static void createMultiUsers(int quantity, String defaultName) throws SQLException {        
        System.out.println("\ncreateMultiUsers(): -> " + quantity + " ...");
        UserDAO dao = new UserDAO(conn, "USER"); 
        for (int i=1; i<=quantity; i++) {
            User o = new User();
            o.setName(defaultName + " - " + i);
            o.setCreatedIn(new Date());
            dao.create(o);
            System.out.println(o);
        }
    }

    private static void deleteAllUser() throws SQLException {        
        UserDAO dao = new UserDAO(conn, "USER");
        dao.destroyAll(conn);
        System.out.println("\ndeleteAllUser(): -> Tudo deletado!" );
    }

    private static void updateAllUsers() throws SQLException, NonexistentEntityException {
        System.out.println("\nupdateAllUsers(): ->");
        UserDAO dao = new UserDAO(conn, "USER");        
        List<AbstractEntity> lst = dao.findAll();
        dao = new UserDAO(conn, "USER"); 
        for (AbstractEntity o:lst) {
            o.setName(o.getName() + ".");
            dao.edit(o);
            System.out.println(o);
        }        
    }
    
}
