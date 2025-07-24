package org.exemple.virementservice;

import org.exemple.virementservice.feign.EmployeeProfileClient;
import org.exemple.virementservice.dtos.EmployeeProfileDTO;
import org.exemple.virementservice.entities.EmployeeMetrics;
import org.exemple.virementservice.entities.Performance;
import org.exemple.virementservice.repos.EmployeeMetricsRepository;
import org.exemple.virementservice.repos.PerformanceRepository;
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
@EnableFeignClients
public class EmployeePerformanceApplication {
    private final Random random = new Random();

    public static void main(String[] args) {
        SpringApplication.run(EmployeePerformanceApplication.class, args);
    }

    /**
     * Bean to initialize employee metrics data using OpenFeign client
     */
    @Bean
    public CommandLineRunner initEmployeeMetricsData(EmployeeMetricsRepository metricsRepository,
                                                     EmployeeProfileClient employeeProfileClient) {
        return args -> {
            if (metricsRepository.count() > 0) {
                return; // Skip if data already exists
            }

            // Get a list of employee IDs to fetch (you'd need to implement or modify this)
            List<Long> employeeIds = getEmployeeIdsList();
            if (employeeIds.isEmpty()) {
                System.out.println("No employee IDs found, skipping metrics initialization");
                return;
            }

            List<EmployeeMetrics> metrics = new ArrayList<>();

            // Generate metrics for each active employee over the last 3 months
            for (Long employeeId : employeeIds) {
                // Check if employee exists and is active
                if (!employeeProfileClient.checkEmployeeExists(employeeId)) {
                    continue;
                }

                EmployeeProfileDTO employee = employeeProfileClient.getEmployeeById(employeeId);
//                if (!employee.isActive()) continue;

                // Create 3 metric entries per employee (last 3 months)
                for (int i = 1; i <= 3; i++) {
                    LocalDate metricDate = LocalDate.now().minusMonths(i);

                    // Generate realistic metrics based on department
                    int tasksCompleted = generateTasksCompleted(employee.getDepartment());
                    int projectsCompleted = random.nextInt(3) + 1;  // 1-3 projects per month
                    double avgTaskTime = generateAvgTaskTime(employee.getDepartment());
                    int clientSatisfaction = random.nextInt(3) + 3; // 3-5 score
                    int teamCollaboration = random.nextInt(3) + 3;  // 3-5 score
                    double innovationScore = generateInnovationScore(employee.getDepartment());

                    String notes = generateMetricNotes(employee.getJobTitle(), tasksCompleted, projectsCompleted);

                    metrics.add(new EmployeeMetrics(
                            null, employee.getId(), metricDate, tasksCompleted, projectsCompleted,
                            avgTaskTime, clientSatisfaction, teamCollaboration, innovationScore, notes
                    ));
                }
            }

            metricsRepository.saveAll(metrics);
            System.out.println("Initialized Employee Metrics data with " + metrics.size() + " records");
        };
    }

    /**
     * Bean to initialize performance review data using OpenFeign client
     */
    @Bean
    public CommandLineRunner initPerformanceData(PerformanceRepository performanceRepository,
                                                 EmployeeProfileClient employeeProfileClient) {
        return args -> {
            if (performanceRepository.count() > 0) {
                return; // Skip if data already exists
            }

            // Get a list of employee IDs to fetch
            List<Long> employeeIds = getEmployeeIdsList();
            if (employeeIds.isEmpty()) {
                System.out.println("No employee IDs found, skipping performance initialization");
                return;
            }

            List<Performance> performances = new ArrayList<>();

            // Moroccan manager names
            String[] reviewers = {
                    "Othmane El Alami", "Samia Naamane", "Mustapha Bensaid",
                    "Leila Haddioui", "Yassine Benabdallah", "Souad El Mansouri",
                    "Mohammed Tazi", "Fatima Benjelloun", "Karim Chaoui",
                    "Nadia Berrada", "Rachid El Fassi", "Amal Zoubeir"
            };

            // Generate performance reviews for each active employee
            for (Long employeeId : employeeIds) {
                // Check if employee exists and is active
                if (!employeeProfileClient.checkEmployeeExists(employeeId)) {
                    continue;
                }

                EmployeeProfileDTO employee = employeeProfileClient.getEmployeeById(employeeId);
//                if (!employee.isActive()) continue;

                // Create last year's performance review
                LocalDate lastYearReview = LocalDate.of(LocalDate.now().getYear() - 1, 12, 15);
                int lastYearRating = random.nextInt(4) + 2; // 2-5 rating
                String reviewer = reviewers[random.nextInt(reviewers.length)];

                String comments = generatePerformanceComments(employee.getJobTitle(), lastYearRating);
                String strengths = generateStrengths(employee.getJobTitle(), lastYearRating);
                String improvements = generateAreasForImprovement(employee.getJobTitle(), lastYearRating);
                String goals = generateGoals(employee.getJobTitle(), employee.getDepartment());

                performances.add(new Performance(
                        null, employee.getId(), lastYearReview, lastYearRating, reviewer,
                        comments, goals, strengths, improvements
                ));

                // Add mid-year review for some employees
                if (random.nextBoolean()) {
                    LocalDate midYearReview = LocalDate.of(LocalDate.now().getYear(), 6, 15);
                    int midYearRating = Math.min(5, lastYearRating + random.nextInt(3) - 1); // Slight variation
                    reviewer = reviewers[random.nextInt(reviewers.length)];

                    comments = generatePerformanceComments(employee.getJobTitle(), midYearRating);
                    strengths = generateStrengths(employee.getJobTitle(), midYearRating);
                    improvements = generateAreasForImprovement(employee.getJobTitle(), midYearRating);
                    goals = generateGoals(employee.getJobTitle(), employee.getDepartment());

                    performances.add(new Performance(
                            null, employee.getId(), midYearReview, midYearRating, reviewer,
                            comments, goals, strengths, improvements
                    ));
                }
            }

            performanceRepository.saveAll(performances);
            System.out.println("Initialized Performance data with " + performances.size() + " records");
        };
    }

    /**
     * Helper method to get a list of employee IDs
     * You would need to implement this based on your specific requirements
     */
    private List<Long> getEmployeeIdsList() {
        // This is a placeholder - you would need to implement this
        // Could be a range of IDs, a call to another service, etc.
        List<Long> ids = new ArrayList<>();
        // Example: Add IDs from 1 to 20
        for (long i = 1; i <= 20; i++) {
            ids.add(i);
        }
        return ids;
    }

    // Helper methods to generate realistic data (kept from your original code)

    private int generateTasksCompleted(String department) {
        // Different departments have different task loads
        switch (department) {
            case "IT Department":
                return random.nextInt(15) + 15; // 15-30 tasks
            case "Marketing":
                return random.nextInt(10) + 10; // 10-20 tasks
            case "Finance":
                return random.nextInt(8) + 12; // 12-20 tasks
            case "Human Resources":
                return random.nextInt(10) + 8; // 8-18 tasks
            default:
                return random.nextInt(10) + 10; // 10-20 tasks
        }
    }

    private double generateAvgTaskTime(String department) {
        // Average task completion time in hours
        switch (department) {
            case "IT Department":
                return 2.0 + (random.nextInt(30) / 10.0); // 2.0-5.0 hours
            case "Marketing":
                return 3.0 + (random.nextInt(40) / 10.0); // 3.0-7.0 hours
            case "Finance":
                return 4.0 + (random.nextInt(30) / 10.0); // 4.0-7.0 hours
            default:
                return 2.5 + (random.nextInt(35) / 10.0); // 2.5-6.0 hours
        }
    }

    private double generateInnovationScore(String department) {
        // Innovation score (out of 5)
        if ("R&D".equals(department) || "IT Department".equals(department)) {
            return 3.5 + (random.nextInt(15) / 10.0); // 3.5-5.0
        } else {
            return 2.5 + (random.nextInt(20) / 10.0); // 2.5-4.5
        }
    }

    private String generateMetricNotes(String jobTitle, int tasksCompleted, int projectsCompleted) {
        String[] noteTemplates = {
                "Completed %d tasks and contributed to %d projects. Shows consistent performance.",
                "Handled %d tasks with attention to detail. Successfully delivered %d projects.",
                "Managed %d tasks effectively. Demonstrated strong skills in %d project deliveries.",
                "Executed %d tasks with high quality. Made significant contributions to %d projects.",
                "Processed %d tasks according to standards. Supported team in completing %d projects."
        };

        String template = noteTemplates[random.nextInt(noteTemplates.length)];
        return String.format(template, tasksCompleted, projectsCompleted);
    }

    private String generatePerformanceComments(String jobTitle, int rating) {
        if (rating >= 4) {
            String[] comments = {
                    "Excellent performance in all aspects of the job. Consistently exceeds expectations.",
                    "Outstanding contributor who raises the bar for the entire team.",
                    "Delivers exceptional results and serves as a model employee.",
                    "Consistently performs above expectations and adds significant value to the team.",
                    "Shows remarkable commitment and produces excellent work consistently."
            };
            return comments[random.nextInt(comments.length)];
        } else if (rating == 3) {
            String[] comments = {
                    "Meets expectations consistently. Reliable team member.",
                    "Solid performer who fulfills all job requirements.",
                    "Competent and dependable. Delivers as expected.",
                    "Performs duties adequately and contributes positively to the team.",
                    "Demonstrates satisfactory performance across responsibilities."
            };
            return comments[random.nextInt(comments.length)];
        } else {
            String[] comments = {
                    "Performance needs improvement in several key areas.",
                    "Not consistently meeting job requirements. Requires more supervision than expected.",
                    "Shows potential but needs to develop stronger skills and work habits.",
                    "Struggling with some aspects of the role. Development plan needed.",
                    "Some performance gaps need to be addressed promptly."
            };
            return comments[random.nextInt(comments.length)];
        }
    }

    private String generateStrengths(String jobTitle, int rating) {
        String[] techStrengths = {
                "Strong technical skills in relevant technologies.",
                "Excellent problem-solving abilities.",
                "Great attention to detail in code development.",
                "Quick learner with new technologies.",
                "Effective debugging and troubleshooting skills."
        };

        String[] marketingStrengths = {
                "Creative campaign development.",
                "Excellent understanding of market trends.",
                "Strong social media marketing skills.",
                "Exceptional content creation abilities.",
                "Data-driven approach to marketing strategy."
        };

        String[] generalStrengths = {
                "Strong communication skills.",
                "Great team player.",
                "Excellent time management.",
                "Takes initiative on projects.",
                "Adaptable to changing priorities.",
                "Positive attitude and professional demeanor."
        };

        StringBuilder strengths = new StringBuilder();

        // Add 1-2 job-specific strengths
        if (jobTitle.contains("Engineer") || jobTitle.contains("Developer") || jobTitle.contains("IT")) {
            strengths.append(techStrengths[random.nextInt(techStrengths.length)]).append(" ");
        } else if (jobTitle.contains("Marketing")) {
            strengths.append(marketingStrengths[random.nextInt(marketingStrengths.length)]).append(" ");
        }

        // Add 1-2 general strengths
        strengths.append(generalStrengths[random.nextInt(generalStrengths.length)]);

        return strengths.toString();
    }

    private String generateAreasForImprovement(String jobTitle, int rating) {
        if (rating >= 4) {
            String[] areas = {
                    "Could mentor junior team members more actively.",
                    "Consider taking on more leadership responsibilities.",
                    "Explore opportunities to share knowledge more broadly.",
                    "Look for ways to contribute to strategic initiatives.",
                    "Could improve documentation habits for complex work."
            };
            return areas[random.nextInt(areas.length)];
        } else if (rating == 3) {
            String[] areas = {
                    "Needs to improve communication with stakeholders.",
                    "Should work on prioritization of tasks.",
                    "Could benefit from more structured approach to problem-solving.",
                    "Time management could be improved.",
                    "Should take more initiative on projects."
            };
            return areas[random.nextInt(areas.length)];
        } else {
            String[] areas = {
                    "Needs significant improvement in meeting deadlines.",
                    "Quality of work needs to be more consistent.",
                    "Must improve communication with team members.",
                    "Needs to develop stronger technical skills.",
                    "Should be more receptive to feedback and coaching."
            };
            return areas[random.nextInt(areas.length)];
        }
    }

    private String generateGoals(String jobTitle, String department) {
        String[] techGoals = {
                "Complete advanced certification in relevant technology.",
                "Improve code review process for the team.",
                "Reduce system downtime by 15% through preventative maintenance.",
                "Implement new automated testing framework.",
                "Successfully deliver the upcoming major system upgrade."
        };

        String[] marketingGoals = {
                "Increase social media engagement by 20%.",
                "Launch 3 successful marketing campaigns for new products.",
                "Improve customer retention metrics by 10%.",
                "Develop comprehensive content strategy for the year.",
                "Establish measurement framework for marketing ROI."
        };

        String[] financeGoals = {
                "Reduce processing time for monthly reports by 25%.",
                "Implement new budgeting software successfully.",
                "Improve financial forecasting accuracy by 15%.",
                "Develop more efficient expense reporting process.",
                "Complete advanced financial analysis training."
        };

        String[] generalGoals = {
                "Improve collaboration with cross-functional teams.",
                "Develop leadership skills through project management.",
                "Increase productivity by 10% through better processes.",
                "Complete professional development program.",
                "Mentor a junior team member successfully."
        };

        StringBuilder goals = new StringBuilder();

        // Add 1-2 department-specific goals
        if ("IT Department".equals(department)) {
            goals.append(techGoals[random.nextInt(techGoals.length)]).append(" ");
        } else if ("Marketing".equals(department)) {
            goals.append(marketingGoals[random.nextInt(marketingGoals.length)]).append(" ");
        } else if ("Finance".equals(department)) {
            goals.append(financeGoals[random.nextInt(financeGoals.length)]).append(" ");
        }

        // Add 1 general goal
        goals.append(generalGoals[random.nextInt(generalGoals.length)]);

        return goals.toString();
    }
}