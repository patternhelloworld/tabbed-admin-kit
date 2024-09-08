package com.autofocus.pms.common.config.database;

public enum SelectablePersistenceConst {

    MYSQL_8("com.autofocus.pms.merz.belotero.config.database.dialect.CustomMySQL8Dialect"),
    MYSQL_8_HQ("com.autofocus.pms.hq.config.database.dialect.CustomMySQL8Dialect"),
    MSSQL("com.autofocus.pms.merz.belotero.config.database.dialect.CustomSQLServerDialect");

    private final String value;

    SelectablePersistenceConst(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
