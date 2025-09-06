package ru.aston.builder;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class User {
    private final String id;
    private final String name;
    private final String email;
    private final Set<String> roles;

    private User(String id, String name, String email, Set<String> roles) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.email = email;

        this.roles = roles == null
                ? Set.of()
                : Collections.unmodifiableSet(new LinkedHashSet<>(roles));

        validate();
    }

    private void validate() {
        if (id.isBlank()) throw new IllegalStateException("id must not be blank");
        if (name.isBlank()) throw new IllegalStateException("name must not be blank");
        if (email != null && !email.contains("@")) {
            throw new IllegalStateException("invalid email: " + email);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static final class Builder {
        private String id;
        private String name;
        private String email;
        private Set<String> roles;

        private Builder() {
        }

        private Builder(User src) {
            this.id = src.id;
            this.name = src.name;
            this.email = src.email;
            this.roles = new LinkedHashSet<>(src.roles);
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        @SuppressWarnings("unused")
        public Builder roles(Collection<String> roles) {
            this.roles = (roles == null) ? null : new LinkedHashSet<>(roles);
            return this;
        }

        public Builder addRole(String role) {
            if (role != null && !role.isBlank()) {
                if (this.roles == null) this.roles = new LinkedHashSet<>();
                this.roles.add(role);
            }
            return this;
        }

        public User build() {
            return new User(id, name, email, roles);
        }

        @Override
        public String toString() {
            return "User.Builder(id=" + id + ", name=" + name +
                    ", email=" + email + ", roles=" + roles + ")";
        }
    }

    @SuppressWarnings("unused")
    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    @SuppressWarnings("unused")
    public String email() {
        return email;
    }

    @SuppressWarnings("unused")
    public Set<String> roles() {
        return roles;
    }

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name +
                "', email='" + email + "', roles=" + roles + "}";
    }
}