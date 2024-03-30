package carsharing.dao;

import carsharing.model.Company;

import java.util.List;

public interface CompanyDAO {

    List<Company> findAll();
    Company findById(int id);
    void add(Company developer);

}