package org.ftd.educational.oldschool.dao.abstracts;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Fabio Tavares Dippold
 * @version 2017-03-07 - 2.0.1
 * @see Long id -> BIGINT / PK / NN / UQ / UN / ZF / AI
 * @see String name -> VARCHAR(45) / NN / UQ
 *
 */
public abstract class AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String name;
    private Date createdIn; 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedIn() {
        return createdIn;
    }

    public void setCreatedIn(Date createdIn) {
        this.createdIn = createdIn;
    }    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getVersion());
        sb.append(" [");
        sb.append(this.getId());
        sb.append("] [");
        sb.append(this.getName());
        sb.append("] [");        
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");         
        sb.append(fmt.format(this.getCreatedIn()));
        sb.append("]");
        
        return sb.toString();
    }    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AbstractEntity)) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }    
    
    public boolean hasEqualMapping(AbstractEntity vo) {
        if (!Objects.equals(vo.getId(), this.getId())) {
            return (false);
        }
        if (this.getName() == null) {
            if (vo.getName()!= null) {
                return (false);
            }
        } else if (!this.getName().equals(vo.getName())) {
            return (false);
        }

        return true;
    }

    public String getVersion() {
        return "AbstractEntity ver 2.0.1";
    }    
    
}
