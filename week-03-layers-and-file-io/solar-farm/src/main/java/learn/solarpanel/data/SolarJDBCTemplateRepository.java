package learn.solarpanel.data;

import learn.solarpanel.models.MaterialType;
import learn.solarpanel.models.SolarPanel;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SolarJDBCTemplateRepository implements SolarRepositories{

    private final JdbcTemplate jdbcTemplate;

    public SolarJDBCTemplateRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    private final RowMapper<SolarPanel> mapper = (resultSet, rowNum) -> {
        SolarPanel panel = new SolarPanel();
        panel.setSolarID(resultSet.getInt("solar_id"));
        panel.setSection(resultSet.getString("section"));
        panel.setRow(resultSet.getInt("panel_row"));
        panel.setCol(resultSet.getInt("panel_col"));
        panel.setYearInstalled(resultSet.getInt("year_installed"));
        panel.setMaterial(MaterialType.valueOf(resultSet.getString("material")));
        panel.setTracking(resultSet.getBoolean("is_tracking"));
        return panel;
    };

    @Override
    public SolarPanel add(SolarPanel panel) throws DataAccessException {
        final String sql =
                "insert into solar_panel (section, panel_row, panel_col, year_installed, material, is_tracking)"
                + " values (?,?,?,?,?,?);";
        KeyHolder keyHolder  = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, panel.getSection());
            ps.setInt(2, panel.getRow());
            ps.setInt(3, panel.getCol());
            ps.setInt(4, panel.getYearInstalled());
            ps.setString(5, panel.getMaterial().toString());
            ps.setBoolean(6, panel.getTracking());
            return ps;
        }, keyHolder);

        if (rowsAffected <=0) {
            return null;
        }
        panel.setSolarID(keyHolder.getKey().intValue());
        return panel;
    }

    @Override
    public List<SolarPanel> findAll() throws DataAccessException {
        final String sql = "select solar_id, section, panel_row, panel_col, " +
                "year_installed, material, is_tracking from solar_panel;";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public List<SolarPanel> findBySection(String section) throws DataAccessException {
        final String sql = "select solar_id, section, panel_row, panel_col, " +
                "year_installed, material, is_tracking from solar_panel where section=?;";
        return jdbcTemplate.query(sql, mapper, section);
    }

    @Override
    public SolarPanel findOne(String section, int row, int col) throws DataAccessException {
        final String sql = "select solar_id, section, panel_row, panel_col, " +
                "year_installed, material, is_tracking from solar_panel " +
                "where section=? and panel_row=? and panel_col=?;";
        try {
            return jdbcTemplate.queryForObject(sql, mapper, section, row, col);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<SolarPanel> findByYearRange(int yearMin, int yearMax) throws DataAccessException {
        final String sql = "select solar_id, section, panel_row, panel_col, " +
                "year_installed, material, is_tracking from solar_panel " +
                "where year_installed between ? and ?;";
        return jdbcTemplate.query(sql, mapper, yearMin, yearMax);
    }

    @Override
    public List<SolarPanel> findByMaterial(MaterialType materialType) throws DataAccessException {
        final String sql = "select solar_id, section, panel_row, panel_col, " +
                "year_installed, material, is_tracking from solar_panel " +
                "where material=?;";
        return jdbcTemplate.query(sql, mapper, materialType.toString());
    }

    @Override
    public boolean update(SolarPanel panel) throws DataAccessException {
        String sql = "update solar_panel set "
                + "section = ?, "
                + "panel_row = ?, "
                + "panel_col = ?, "
                + "year_installed = ?, "
                + "material = ?, "
                + "is_tracking = ? "
                + "where solar_id = ?;";

        int rowUpdated = jdbcTemplate.update(sql, panel.getSection(),
                panel.getRow(), panel.getCol(), panel.getYearInstalled(),
                panel.getMaterial().getName(),
                panel.getTracking(), panel.getSolarID());

        return rowUpdated > 0;
    }

    @Override
    public boolean delete(String section, int row, int col) throws DataAccessException {
        String sql2 = "delete from solar_panel where section = ? " +
                "and panel_row = ? " +
                "and panel_col = ?;";

        int rowUpdated = jdbcTemplate.update(sql2, section, row, col);

        return rowUpdated > 0;
    }
}
