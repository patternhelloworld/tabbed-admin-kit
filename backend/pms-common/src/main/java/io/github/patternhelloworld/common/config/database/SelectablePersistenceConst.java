package io.github.patternhelloworld.common.config.database;

public enum SelectablePersistenceConst {

    MYSQL_8("io.github.patternhelloworld.tak.config.database.dialect.CustomMySQL8Dialect"),
    MYSQL_8_HQ("io.github.patternhelloworld.tak.config.database.dialect.CustomMySQL8Dialect"),
    MSSQL("io.github.patternhelloworld.tak.config.database.dialect.CustomSQLServerDialect");

    private final String value;

    SelectablePersistenceConst(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
