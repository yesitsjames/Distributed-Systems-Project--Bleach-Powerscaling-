package backend;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum PowerDao {
	instance;

	private Connection connection;

	private Map<Integer, Power> powersMap = new HashMap<Integer, Power>();
	 private ParsePowers parser; 
	private PowerDao() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oneDB", "SA", "Passw0rd");
		

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Power> getPowers() {
		List<Power> powers = new ArrayList<Power>();

		try (Statement stmt = connection.createStatement(); 
				ResultSet rs = stmt.executeQuery("SELECT * FROM power")) {
			while (rs.next()) {
				Power p = new Power();
				p.setId(rs.getInt("id"));
				p.setSquad(rs.getString("squad"));
				p.setName(rs.getString("name"));
				p.setLevel(rs.getInt("level"));
				p.setPosition(rs.getString("position"));
				p.setAge(rs.getInt("age"));
				p.setGender(rs.getString("gender"));
				powers.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		powers.addAll(powersMap.values());
		return powers;
	}

	public Power getPower(int id) {
		try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM power WHERE id = ?")) {
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Power p = new Power();
				p.setId(rs.getInt("id"));
				p.setSquad(rs.getString("squad"));
				p.setName(rs.getString("name"));
				p.setLevel(rs.getInt("level"));
				p.setPosition(rs.getString("position"));
				p.setAge(rs.getInt("age"));
				p.setGender(rs.getString("gender"));
				
				return p;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return powersMap.get(id);
	}

	public Power addPower(Power power) {

		
		try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO power (id, name, squad, level, position, age, gender) VALUES (?, ?,?,?, ? ,? ,?)", Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, power.getId());
			stmt.setString(2, power.getName());
            stmt.setString(3, power.getSquad());
            stmt.setInt(4, power.getLevel());
            stmt.setString(5, power.getPosition());
            stmt.setInt(6, power.getAge());
            stmt.setString(7, power.getGender());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return power;
	}

	public Power deletePower(int id) {
		try (Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("DELETE FROM power WHERE id ="+id)) {
			rs.next();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return powersMap.remove(id);
	}
	public Power deleteAllPower() {
		try (Statement stmt = connection.createStatement();
				ResultSet rs = stmt.executeQuery("DELETE FROM power")) {
			rs.next();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	public Power updatePower(Power newPower) {

		
		
		try (PreparedStatement stmt = connection.prepareStatement("UPDATE power SET name = ?, squad= ?, level= ?, position= ?, age= ?, gender= ? WHERE ID = ?", Statement.RETURN_GENERATED_KEYS)) {
		
			stmt.setString(1, newPower.getName());
            stmt.setString(2, newPower.getSquad());
            stmt.setInt(3, newPower.getLevel());
            stmt.setString(4, newPower.getPosition());
            stmt.setInt(5, newPower.getAge());
            stmt.setString(6, newPower.getGender());
        	stmt.setInt(7, newPower.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return newPower;
	}
	


}