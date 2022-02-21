package asia.ptyin.sqloj.engine.sql;

import asia.ptyin.sqloj.engine.result.QueryResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/***
 *
 * @version 0.1.0
 * @author PTYin
 * @since 0.1.0
 */
@Log4j2
class QueryResultTest
{
    static Connection connection;
    static ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void setup() throws SQLException
    {
        connection = DriverManager.getConnection("jdbc:sqlite::resource:db/Chinook.db");
    }

    @Test
    void toJson() throws Exception
    {
        connection.setAutoCommit(false);
        var results = SqlExecutionUtils.execute(connection, """
                select * from albums where AlbumId < 10;
                """);
        connection.rollback();
        connection.close();
        var json = results.get(0).serialize();
        log.info("Generated json: %s".formatted(json));
        var node = mapper.readTree(json);
        // assert on rows
        assertEquals("Balls to the Wall", node.get("rows").get(1).get(1).asText());
        // assert on metadata
        assertEquals("AlbumId", node.get("metadata").get("columnMetadataList").get(0).get("columnLabel").asText());
        assertEquals("INTEGER", node.get("metadata").get("columnMetadataList").get(0).get("columnTypeName").asText());
        assertTrue(node.get("metadata").get("columnMetadataList").get(0).get("autoIncrement").asBoolean());
        var deserializedResult = mapper.readValue(json, QueryResult.class);
        assertEquals(results.get(0).getRows().get(0).get(0), deserializedResult.getRows().get(0).get(0));
        assertEquals(results.get(0).getMetadata().getColumnCount(), deserializedResult.getMetadata().getColumnCount());
        assertEquals(results.get(0).getMetadata().getColumnMetadata(0).getColumnName(), results.get(0).getMetadata().getColumnMetadata(0).getColumnName());
    }
}