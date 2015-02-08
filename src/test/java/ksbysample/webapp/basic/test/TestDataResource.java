package ksbysample.webapp.basic.test;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;

@Component
public class TestDataResource extends ExternalResource {

    @Autowired
    private DataSource dataSource;

    private File backupFile;

    @Override
    protected void before() throws Throwable {
        IDatabaseConnection conn = null;
        try {
            conn = new DatabaseConnection(dataSource.getConnection());

            // バックアップを取得する
            QueryDataSet partialDataSet = new QueryDataSet(conn);
            partialDataSet.addTable("user");
            partialDataSet.addTable("user_role");
            backupFile = File.createTempFile("world_backup", "xml");
            FlatXmlDataSet.write(partialDataSet, new FileOutputStream(backupFile));

            // テストデータに入れ替える
            IDataSet dataset = new CsvDataSet(new File("src/test/resources/testdata"));
            DatabaseOperation.CLEAN_INSERT.execute(conn, dataset);
        }
        finally {
            if (conn != null) conn.close();
        }
    }

    @Override
    protected void after() {
        try {
            IDatabaseConnection conn = null;
            try {
                conn = new DatabaseConnection(dataSource.getConnection());

                // バックアップからリストアする
                if (backupFile != null) {
                    IDataSet dataSet = new FlatXmlDataSetBuilder().build(backupFile);
                    DatabaseOperation.CLEAN_INSERT.execute(conn, dataSet);
                    backupFile.delete();
                    backupFile = null;
                }
            }
            finally {
                if (conn != null) conn.close();
            }
        }
        catch (Exception ignored) {
        }
    }

}
