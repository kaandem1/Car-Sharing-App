package carsharing.ui;

import carsharing.Main;
import carsharing.dao.CarDAO;
import carsharing.dao.CarDAOImp;
import carsharing.dao.CompanyDAO;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInt implements CompanyDAO {
    private final CarDAO carDao = new CarDAOImp();
    Scanner scanner = new Scanner(System.in);
    private List<Company> companies;
    private final String  companyList = "1. Company list";
    private final String  createCompany = "2. Create a company";
    private final String  backStr = "0. Back";

    public UserInt() {
    }

    @Override
    public List<Company> findAll() {
        String sql = "SELECT * FROM COMPANY ORDER BY ID";
        List<Company> companies = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(Main.getDbUrl());
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Company company = new Company(rs.getInt("ID"), rs.getString("NAME"));
                companies.add(company);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return companies;
    }

    private Company findByIdInternal(int id) {
        for (Company company : companies) {
            if (id == company.getID()) {
                return company;
            }
        }
        return null;
    }

    @Override
    public Company findById(int id) {
        String sql = "SELECT * FROM COMPANY WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(Main.getDbUrl());
             PreparedStatement pstmt  = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Company company = new Company(rs.getInt("ID"), rs.getString("NAME"));
                return company;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void add(Company company) {
        String sql = "INSERT INTO COMPANY (NAME) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(Main.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, company.getNAME());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void listCars(int companyId) {

        List<Car> cars = carDao.findAllByCompanyId(companyId);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            int index = 1;
            for (Car car : cars) {
                System.out.printf("%d. %s\n", index++, car.getNAME());
            }
        }
    }

    private void createCar(int companyId) {
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        carDao.add(new Car(0, carName, companyId));
        System.out.println("The car was added!");
    }

    public void printFirstMenu() {
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");
    }


    private void printSecondMenu() {
        System.out.printf("%s\n%s\n%s\n", companyList, createCompany, backStr);
        while (true) {
            String input = scanner.next();
            switch (input) {
                case "0":
                    printFirstMenu();
                    return;
                case "1":
                    companies = findAll();
                    if (companies.isEmpty()) {
                        System.out.println("The company list is empty!");
                    } else {
                        for (int i = 0; i < companies.size(); i++) {
                            System.out.printf("%d. %s\n", i + 1, companies.get(i).getNAME());
                        }
                        System.out.println("0. Back");
                        int choice = scanner.nextInt();
                        scanner.nextLine();
                        if (choice == 0) {
                            break;
                        } else if (choice > 0 && choice <= companies.size()) {
                            companyMenu(companies.get(choice - 1).getID());
                        } else {
                            System.out.println("Invalid option. Please try again.");
                        }
                    }
                    break;
                case "2":
                    scanner.nextLine();
                    System.out.println("Enter the company name:");
                    String companyName = scanner.nextLine();
                    add(new Company(0, companyName));
                    System.out.println("The company was created!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    private void printSecondMenuText(){
        System.out.printf("%s\n%s\n%s\n", companyList, createCompany,backStr);
    }

    private void companyMenu(int companyId) {
        Company company = findById(companyId);
        if (company == null) {
            System.out.println("Company not found!");
            return;
        }

        while (true) {
            System.out.printf("'%s' company:\n1. Car list\n2. Create a car\n0. Back\n", company.getNAME());
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    listCars(companyId);
                    break;
                case "2":
                    createCar(companyId);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        String customerName = scanner.nextLine();
        String sql = "INSERT INTO CUSTOMER (NAME) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(Main.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customerName);
            pstmt.executeUpdate();
            System.out.println("The customer was added!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private List<Customer> findAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM CUSTOMER";
        try (Connection conn = DriverManager.getConnection(Main.getDbUrl());
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(new Customer(rs.getInt("ID"), rs.getString("NAME"), (Integer) rs.getObject("RENTED_CAR_ID")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customers;
    }

    private void loginAsCustomer() {
        List<Customer> customers = findAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            return;
        }

        System.out.println("Choose a customer:");
        int index = 1;
        for (Customer customer : customers) {
            System.out.printf("%d. %s\n", index++, customer.getNAME());
        }
        System.out.println("0. Back");

        int choice = Integer.parseInt(scanner.nextLine());
        if (choice == 0) {
            return;
        } else if (choice > 0 && choice <= customers.size()) {
            customerMenu(customers.get(choice - 1));
        } else {
            System.out.println("Invalid option. Please try again.");
        }
    }

    private void rentCar(Customer customer) {
        if (customer.getRENTED_CAR_ID() != null) {
            System.out.println("You've already rented a car!");
            return;
        }

        List<Company> companies = findAll();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            return;
        }

        System.out.println("Choose a company:");
        for (int i = 0; i < companies.size(); i++) {
            System.out.println((i + 1) + ". " + companies.get(i).getNAME());
        }
        System.out.println("0. Back");

        int companyChoice = Integer.parseInt(scanner.nextLine());
        if (companyChoice == 0) {
            return;
        }

        Company selectedCompany = companies.get(companyChoice - 1);
        List<Car> availableCars = carDao.findAllByCompanyId(selectedCompany.getID());
        if (availableCars.isEmpty()) {
            System.out.println("No available cars in the '" + selectedCompany.getNAME() + "' company.");
            return;
        }

        System.out.println("Choose a car:");
        for (int i = 0; i < availableCars.size(); i++) {
            System.out.println((i + 1) + ". " + availableCars.get(i).getNAME());
        }
        System.out.println("0. Back");

        int carChoice = Integer.parseInt(scanner.nextLine());
        if (carChoice == 0) {
            return;
        }

        Car selectedCar = availableCars.get(carChoice - 1);
        updateCustomerCar(customer, selectedCar.getID());
        System.out.println("You rented '" + selectedCar.getNAME() + "'");
    }

    private void returnCar(Customer customer) {
        if (customer.getRENTED_CAR_ID() == null) {
            System.out.println("You didn't rent a car!");
            return;
        }

        updateCustomerCar(customer, null);
        System.out.println("You've returned a rented car!");
    }

    private void myRentedCar(Customer customer) {
        if (customer.getRENTED_CAR_ID() == null) {
            System.out.println("You didn't rent a car!");
            return;
        }

        Car rentedCar = carDao.findById(customer.getRENTED_CAR_ID());
        Company carCompany = findById(rentedCar.getCOMPANY_ID());

        System.out.println("Your rented car:");
        System.out.println(rentedCar.getNAME());
        System.out.println("Company:");
        System.out.println(carCompany.getNAME());
    }

    private void updateCustomerCar(Customer customer, Integer carId) {
        String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(Main.getDbUrl());
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (carId != null) {
                pstmt.setInt(1, carId);
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setInt(2, customer.getID());
            pstmt.executeUpdate();
            customer.setRENTED_CAR_ID(carId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void customerMenu(Customer customer) {
        while (true) {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    rentCar(customer);
                    break;
                case 2:
                    returnCar(customer);
                    break;
                case 3:
                    myRentedCar(customer);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public void app() {
        while (true) {
            printFirstMenu();
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    printSecondMenu();
                    break;
                case "2":
                    loginAsCustomer();
                    break;
                case "3":
                    createCustomer();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

}