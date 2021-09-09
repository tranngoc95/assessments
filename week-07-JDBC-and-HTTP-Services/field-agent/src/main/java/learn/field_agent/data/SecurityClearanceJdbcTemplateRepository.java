package learn.field_agent.data;

import learn.field_agent.data.mappers.SecurityClearanceMapper;
import learn.field_agent.models.SecurityClearance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SecurityClearanceJdbcTemplateRepository implements SecurityClearanceRepository {

    private final JdbcTemplate jdbcTemplate;

    public SecurityClearanceJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SecurityClearance findById(int securityClearanceId) {

        final String sql = "select security_clearance_id, name security_clearance_name "
                + "from security_clearance "
                + "where security_clearance_id = ?;";

        return jdbcTemplate.query(sql, new SecurityClearanceMapper(), securityClearanceId)
                .stream()
                .findFirst().orElse(null);
    }

    @Override
    public SecurityClearance findByName(String name) {
        final String sql = "select security_clearance_id, name security_clearance_name " +
                "from security_clearance " +
                "where name = ?";

        return jdbcTemplate.query(sql, new SecurityClearanceMapper(), name).stream().findFirst().orElse(null);
    }

    @Override
    public List<SecurityClearance> findAll() {
        final String sql = "select security_clearance_id, `name` security_clearance_name from security_clearance";
        return jdbcTemplate.query(sql, new SecurityClearanceMapper());
    }

    @Override
    public SecurityClearance add(SecurityClearance clearance) {

        final String sql = "insert into security_clearance (`name`) values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, clearance.getName());
            return ps;
        }, keyHolder);

        if (rowAffected <=0){
            return null;
        }

        clearance.setSecurityClearanceId(keyHolder.getKey().intValue());
        return clearance;
    }

    @Override
    public boolean update(SecurityClearance clearance) {

        final String sql = "update security_clearance set " +
                "name = ? where security_clearance_id = ?;";

        return jdbcTemplate.update(sql, clearance.getName(), clearance.getSecurityClearanceId()) > 0;
    }

    @Override
    public boolean safeDeleteById(int clearanceId) {
        return jdbcTemplate.update(
                "delete from security_clearance where security_clearance_id = ?;", clearanceId) > 0;
    }

    @Override
    @Transactional
    public boolean fullDeleteById(int clearanceId) {
        jdbcTemplate.update("delete from agency_agent where security_clearance_id = ?;", clearanceId);
        return safeDeleteById(clearanceId);
    }

    @Override
    public boolean checkReferenceKey(int clearanceId){

        final String sql = "select count(*) " +
                    "from agency_agent where security_clearance_id = ?;";

        return jdbcTemplate.queryForObject(sql, Integer.class, clearanceId) > 0;
    }
}
