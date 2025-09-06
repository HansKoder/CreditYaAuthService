package org.pragma.creditya.model.role;
import lombok.Getter;
import org.pragma.creditya.model.shared.model.valueobject.RoleId;
import org.pragma.creditya.model.role.valueobject.RoleName;
import org.pragma.creditya.model.shared.model.entity.AggregateRoot;

@Getter
public class Role extends AggregateRoot<RoleId> {

    private final RoleName name;

    private Role(RoleBuilder builder) {
        this.name = builder.name;
        this.setId(builder.id);
    }

    public static final class RoleBuilder {
        private RoleName name;
        private RoleId id;

        private RoleBuilder() {
        }

        public static RoleBuilder aRole() {
            return new RoleBuilder();
        }

        public RoleBuilder name(String name) {
            this.name = new RoleName(name);
            return this;
        }

        public RoleBuilder id(Long id) {
            this.id = new RoleId(id);
            return this;
        }

        public Role build() {
            return new Role(this);
        }
    }
}
