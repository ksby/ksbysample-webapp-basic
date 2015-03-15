package ksbysample.webapp.basic.test;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
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
import java.nio.file.Files;

@Component
public class TestDataResource extends ExternalResource {

    @Autowired
    private DataSource dataSource;

    private File backupFile;

    @Override
    protected void before() throws Exception {
        IDatabaseConnection conn = null;
        try {
            conn = new DatabaseConnection(dataSource.getConnection());

            // バックアップを取得する
            QueryDataSet partialDataSet = new QueryDataSet(conn);
            partialDataSet.addTable("user");
            partialDataSet.addTable("user_role");
            partialDataSet.addTable("country");
            ReplacementDataSet replacementDatasetBackup = new ReplacementDataSet(partialDataSet);
            replacementDatasetBackup.addReplacementObject("", "[null]");
            backupFile = File.createTempFile("world_backup", "xml");
            try (FileOutputStream fos = new FileOutputStream(backupFile)) {
                FlatXmlDataSet.write(replacementDatasetBackup, fos);
            }

            // テストデータに入れ替える
            IDataSet dataSet = new CsvDataSet(new File("src/test/resources/testdata"));
            ReplacementDataSet replacementDataset = new ReplacementDataSet(dataSet);
            replacementDataset.addReplacementObject("[null]", null);
            DatabaseOperation.CLEAN_INSERT.execute(conn, replacementDataset);
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
                    ReplacementDataSet replacementDatasetRestore = new ReplacementDataSet(dataSet);
                    replacementDatasetRestore.addReplacementObject("[null]", null);
                    DatabaseOperation.CLEAN_INSERT.execute(conn, replacementDatasetRestore);
                }
            } finally {
                if (backupFile != null) {
                    Files.delete(backupFile.toPath());
                    backupFile = null;
                }
                try {
                    if (conn != null) conn.close();
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
