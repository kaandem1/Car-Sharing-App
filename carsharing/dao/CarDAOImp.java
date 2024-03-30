package carsharing.dao;

import carsharing.model.Car;
import carsharing.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAOImp implements CarDAO {

    @Override
    public List<Car> findAllByCompanyId(int companyId) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM CAR WHERE COMPANY_ID = ? AND ID NOT IN (SELECT RENTED_CAR_ID FROM CUSTOMER WHERE RENTED_CAR_ID IS NOT NULL)";

        try (Connection conn = DriverManager.getConnection(Main.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, companyId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Car car = new Car(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("COMPANY_ID"));
                cars.add(car);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cars;
    }


    @Override
    public void add(Car car) {
        String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(Main.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, car.getNAME());
            pstmt.setInt(2, car.getCOMPANY_ID());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Car findById(int id) {
        String sql = "SELECT * FROM CAR WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(Main.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Car(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("COMPANY_ID"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}