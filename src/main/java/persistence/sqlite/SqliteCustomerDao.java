package persistence.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Restaurante_Completo.LeCartoon_Original.Costumer;
import persistence.ConnectionFactory;
import persistence.CustomerDao;
import persistence.DaoException;

// Esta clase y package es por que seguimos trabajando con sqlite , existira su parte Oracle

public class SqliteCustomerDao implements CustomerDao {

	@Override
	public Costumer login(String email, String password) {
		String sql = "SELECT ID , NAME , LASTNAME, PHONE,EMAIL,PASSWORD ,ADDRESS " 
	+ "FROM COSTUMERS WHERE EMAIL = ? AND PASSWORD = ?";
	try(Connection con = ConnectionFactory.getConnection();
			PreparedStatement ps = con.prepareStatement(sql)){
		ps.setString(1,email);
		ps.setString(2, password);
		try(ResultSet rs = ps.executeQuery()){
			return rs.next() ? mapear(rs) : null; 
		}
	} catch(SQLException e) {
		throw new DaoException("No se pudo iniciar sesion",e);
	}
	
	}

	@Override
    public int registrar(Costumer nuevo) {
        String sql = "INSERT INTO COSTUMERS (NAME, LASTNAME, PHONE, EMAIL, PASSWORD, ADDRESS) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nuevo.getName());
            ps.setString(2, nuevo.getLastName());
            ps.setString(3, nuevo.getPhone());
            ps.setString(4, nuevo.getEmail());
            ps.setString(5, nuevo.getPassword());
            ps.setString(6, nuevo.getAddress());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            throw new DaoException("La BD no regresó el id del cliente", null);
        } catch (SQLException e) {
            throw new DaoException("No se pudo registrar al cliente", e);
        }
    }

    @Override
    public void actualizarPerfil(Costumer cliente) {
        String sql = "UPDATE COSTUMERS SET NAME = ?, LASTNAME = ?, PHONE = ? WHERE ID = ?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getName());
            ps.setString(2, cliente.getLastName());
            ps.setString(3, cliente.getPhone());
            ps.setInt(4, cliente.getId());
            if (ps.executeUpdate() == 0) {
                throw new DaoException("No existe el cliente con id " + cliente.getId(), null);
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar el perfil", e);
        }
    }

    /** Convierte el renglón actual del ResultSet en un Costumer. */
    private Costumer mapear(ResultSet rs) throws SQLException {
        return new Costumer(
                rs.getInt("ID"),
                rs.getString("NAME"),
                rs.getString("LASTNAME"),
                rs.getString("PHONE"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD"),
                rs.getString("ADDRESS"));
    }
}