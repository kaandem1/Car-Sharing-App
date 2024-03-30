package carsharing.dao;

import carsharing.model.Car;
import carsharing.model.Company;

import java.util.List;

public interface CarDAO {
    List<Car> findAllByCompanyId(int companyId);
    void add(Car car);

    Car findById(int id);
}