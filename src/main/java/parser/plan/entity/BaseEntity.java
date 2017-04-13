package parser.plan.entity;

import java.util.Objects;

public abstract class BaseEntity {
    protected String name;

    @Override
    public boolean equals(Object obj) {
        BaseEntity entity = (BaseEntity) obj;
        if (entity == this) {
            return true;
        }
        return this.name.equals(entity.name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(name);
        return hash;
    }
    
   
}
