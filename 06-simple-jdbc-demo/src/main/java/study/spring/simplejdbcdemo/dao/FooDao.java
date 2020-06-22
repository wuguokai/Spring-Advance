package study.spring.simplejdbcdemo.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.spring.simplejdbcdemo.dto.Foo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author wuguokai
 */
@Slf4j
@Repository
public class FooDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void insertData() {
        Arrays.asList("a", "b").forEach(bar -> {
            jdbcTemplate.update("INSERT INTO FOO (BAR) VALUES (?)", bar);
        });

        HashMap<String, String> row = new HashMap<>();
        row.put("BAR", "d");
        Number id = simpleJdbcInsert.executeAndReturnKey(row);
        log.info("id of d: {}", id.longValue());
    }

    public void batchInsert() {
        List<String> test1 = Arrays.asList("wgk1", "wgk2");
        jdbcTemplate.batchUpdate("insert into foo (bar) values (?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setString(1, test1.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return 2;
                    }
                });

        List<Foo> list = new ArrayList<>();
        list.add(Foo.builder().id(100L).bar("b-100").build());
        list.add(Foo.builder().id(101L).bar("B-101").build());
        namedParameterJdbcTemplate.batchUpdate("insert into foo (id, bar) values (:id, :bar)",
                SqlParameterSourceUtils.createBatch(list));
    }

    public void listData() {
        log.info("Count: {}",
                jdbcTemplate.queryForObject("select count(*) from foo", Long.class));

        List<String> list = jdbcTemplate.queryForList("select bar from foo", String.class);
        list.forEach(s -> log.info("Bar: {}", s));
        List<Foo> fooList = jdbcTemplate.query("select * from foo", new RowMapper<Foo>() {
            @Override
            public Foo mapRow(ResultSet resultSet, int i) throws SQLException {
                return Foo.builder()
                        .id(resultSet.getLong(1))
                        .bar(resultSet.getString(2))
                        .build();
            }
        });

    }
}
