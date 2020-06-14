package com.xupu.locationmap.projectmanager.view;

public class DatabaseField {

    private String name;
    private String alias;
    private boolean ismust;

    public DatabaseField(String name) {
        this.name = name;
    }

    public DatabaseField(String name, String alias, boolean ismust) {
        this.name = name;
        this.alias = alias;
        this.ismust = ismust;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsmust() {
        return ismust;
    }

    public void setIsmust(boolean ismust) {
        this.ismust = ismust;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public static class Builder {
        private String name;
        private String alias;
        private boolean ismust;

        public Builder(String name) {
            this.name = name;
        }

        public Builder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public Builder setIsmust(boolean ismust) {
            this.ismust = ismust;
            return this;
        }

        public DatabaseField build() {
            DatabaseField databaseField = new DatabaseField(name, alias, ismust);
            return databaseField;
        }
    }
}
