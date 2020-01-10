package org.ftd.educational.oldschool.dao.interfaces;

import java.util.List;
import org.ftd.educational.oldschool.dao.abstracts.AbstractEntity;
import org.ftd.educational.oldschool.dao.exceptions.NonexistentEntityException;

/**
 *
 * @author Fabio Tavares Dippold
 * @version 2017-03-07  - 2.0.1
 * 
 */
public interface IDAO {
    
    void create(AbstractEntity o);
    void edit(AbstractEntity o) throws NonexistentEntityException, Exception;
    void destroy(Long id) throws NonexistentEntityException;
    List<AbstractEntity> findAll();
    AbstractEntity find(Long id);
    int count();
    
}
