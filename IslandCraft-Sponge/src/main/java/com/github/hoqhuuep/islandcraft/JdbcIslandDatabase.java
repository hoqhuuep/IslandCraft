package com.github.hoqhuuep.islandcraft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.spongepowered.api.service.sql.SqlService;

import com.github.hoqhuuep.islandcraft.core.IslandDatabase;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class JdbcIslandDatabase implements IslandDatabase {
	private final DataSource dataSource;
	private final String username;
	private final String password;

	public JdbcIslandDatabase(SqlService sqlService, CommentedConfigurationNode config) throws SQLException {
		String url = config.getNode("url").getString("jdbc:h2:./islandcraft");
		username = config.getNode("username").getString(null);
		password = config.getNode("password").getString(null);
		dataSource = sqlService.getDataSource(url);
		createTablesIfTheyDoNotExist();
	}

	Connection getConnection() throws SQLException {
		if (username != null && password != null) {
			return dataSource.getConnection(username, password);
		}
		return dataSource.getConnection();
	}
	
	private void createTablesIfTheyDoNotExist() throws SQLException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS islandcraft_core (world_name VARCHAR(255) NOT NULL, center_x INTEGER NOT NULL, center_z INTEGER NOT NULL, island_seed BIGINT, generator VARCHAR(65535))")) {
			statement.executeUpdate();
		}
	}

	@Override
	public void save(String worldName, int centerX, int centerZ, long islandSeed, String generator) {
		if (load(worldName, centerX, centerZ) != null) {
			// Already exists => UPDATE
			try (Connection connection = getConnection();
					PreparedStatement statement = connection.prepareStatement("UPDATE islandcraft_core SET island_seed=?, generator=? WHERE world_name=? AND center_x=? AND center_z=?")) {
				statement.setLong(1, islandSeed);
				statement.setString(2, generator);
				statement.setString(3,  worldName);
				statement.setInt(4, centerX);
				statement.setInt(5, centerZ);
				statement.executeUpdate();
			} catch (SQLException e) {
				// Nothing we can do about this...
				e.printStackTrace();
			}
		} else {
			// Does not already exist => INSERT
			try (Connection connection = getConnection();
					PreparedStatement statement = connection.prepareStatement("INSERT INTO islandcraft_core (world_name, center_x, center_z, island_seed, generator) VALUES (?, ?, ?, ?, ?)")) {
				statement.setString(1,  worldName);
				statement.setInt(2, centerX);
				statement.setInt(3, centerZ);
				statement.setLong(4, islandSeed);
				statement.setString(5, generator);
				statement.executeUpdate();
			} catch (SQLException e) {
				// Nothing we can do about this...
				e.printStackTrace();
			}
		}
	}

	@Override
	public Result load(String worldName, int centerX, int centerZ) {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT island_seed, generator FROM islandcraft_core WHERE world_name=? AND center_x=? AND center_z=?")) {
			statement.setString(1, worldName);
			statement.setInt(2, centerX);
			statement.setInt(3, centerZ);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					long islandSeed = result.getLong(1);
					String generator = result.getString(2);
					return new Result(islandSeed, generator);
				}
			}
		} catch (SQLException e) {
			// Nothing we can do about this...
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isEmpty(String worldName) {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM islandcraft_core WHERE world_name=?")) {
			statement.setString(1, worldName);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					return result.getInt(1) == 0;
				}
			}
		} catch (SQLException e) {
			// Nothing we can do about this...
			e.printStackTrace();
		}
		return false;
	}
}
