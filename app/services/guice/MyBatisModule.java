package services.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import play.db.Database;

import javax.sql.DataSource;

public class MyBatisModule extends org.mybatis.guice.MyBatisModule {

    @Override
    protected void initialize() {
        environmentId( "production" );
        bindDataSourceProviderType( PlayDataSourceProvider.class );
        bindTransactionFactoryType( JdbcTransactionFactory.class );

        addMapperClasses( "services.db.mapper" );
        addSimpleAliases( "services.db.entity" );
    }

    @Singleton
    public static class PlayDataSourceProvider implements Provider< DataSource > {
        private final Database db;

        @Inject
        public PlayDataSourceProvider( final Database db ) { this.db = db; }

        @Override
        public DataSource get() { return db.getDataSource(); }
    }
}
