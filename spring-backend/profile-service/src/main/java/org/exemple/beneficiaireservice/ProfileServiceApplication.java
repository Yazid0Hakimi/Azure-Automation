package org.exemple.beneficiaireservice;

import org.exemple.beneficiaireservice.entities.Employee;
import org.exemple.beneficiaireservice.repos.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class ProfileServiceApplication {
    public static void main(String[] args) {
        final Random random = new Random();
        SpringApplication.run(ProfileServiceApplication.class, args);
    }

    /**
     * Bean to initialize employee data with Moroccan names, addresses, etc.
     */
    @Bean
    public CommandLineRunner initEmployeeData(EmployeeRepository employeeRepository) {
        return args -> {
            if (employeeRepository.count() > 0) {
                return; // Skip if data already exists
            }

            List<Employee> employees = new ArrayList<>();

            // Create employees with Moroccan data
            employees.add(new Employee(null, "Mohammed", "El Fassi", "mohammed.elfassi@example.ma",
                    "+212 6-61-45-78-23", "Software Engineer", "IT Department",
                    LocalDate.of(2020, 5, 15), "17 Rue Tarik Ibn Ziad, Casablanca", true));

            employees.add(new Employee(null, "Fatima", "Benkirane", "fatima.benkirane@example.ma",
                    "+212 6-70-23-45-67", "Marketing Manager", "Marketing",
                    LocalDate.of(2019, 3, 10), "23 Avenue Hassan II, Rabat", true));

            employees.add(new Employee(null, "Youssef", "Alaoui", "youssef.alaoui@example.ma",
                    "+212 6-55-78-32-19", "Financial Analyst", "Finance",
                    LocalDate.of(2021, 1, 20), "8 Boulevard Mohammed V, Marrakech", true));

            employees.add(new Employee(null, "Amina", "Tazi", "amina.tazi@example.ma",
                    "+212 6-62-10-45-87", "HR Specialist", "Human Resources",
                    LocalDate.of(2018, 9, 5), "12 Rue Ibn Battouta, Fès", true));

            employees.add(new Employee(null, "Karim", "Berrada", "karim.berrada@example.ma",
                    "+212 6-67-89-34-21", "Project Manager", "Operations",
                    LocalDate.of(2017, 11, 25), "45 Avenue des FAR, Tanger", true));

            employees.add(new Employee(null, "Laila", "Chraibi", "laila.chraibi@example.ma",
                    "+212 6-54-32-67-89", "Customer Service Representative", "Customer Support",
                    LocalDate.of(2022, 4, 12), "3 Rue Mohammed El Beqqal, Agadir", true));

            employees.add(new Employee(null, "Hamza", "El Idrissi", "hamza.elidrissi@example.ma",
                    "+212 6-78-56-34-21", "Sales Executive", "Sales",
                    LocalDate.of(2019, 7, 8), "19 Boulevard Zerktouni, Casablanca", true));

            employees.add(new Employee(null, "Nadia", "Benjelloun", "nadia.benjelloun@example.ma",
                    "+212 6-61-23-78-90", "Quality Assurance Specialist", "IT Department",
                    LocalDate.of(2020, 10, 17), "27 Rue Abou Alaa Maari, Rabat", true));

            employees.add(new Employee(null, "Omar", "Bensouda", "omar.bensouda@example.ma",
                    "+212 6-70-45-67-89", "Business Analyst", "Strategy",
                    LocalDate.of(2021, 6, 30), "14 Avenue Mohammed VI, Marrakech", true));

            employees.add(new Employee(null, "Zineb", "Ouazzani", "zineb.ouazzani@example.ma",
                    "+212 6-55-76-12-34", "Research Scientist", "R&D",
                    LocalDate.of(2018, 8, 22), "9 Rue Ibn Rochd, Meknès", true));

            // Add some inactive employees
            employees.add(new Employee(null, "Rachid", "El Amrani", "rachid.elamrani@example.ma",
                    "+212 6-62-88-99-77", "System Administrator", "IT Operations",
                    LocalDate.of(2019, 2, 15), "31 Boulevard Moulay Youssef, Casablanca", false));

            employees.add(new Employee(null, "Samira", "Bennani", "samira.bennani@example.ma",
                    "+212 6-67-33-44-55", "Graphic Designer", "Marketing",
                    LocalDate.of(2020, 7, 10), "5 Rue Moulay Rachid, Tétouan", false));

            employeeRepository.saveAll(employees);
            System.out.println("Initialized Employee data with " + employees.size() + " records");
        };
    }


}