// package com.Pahana_Edu_Backend.Customer.serviceImpl;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Service;

// import com.Pahana_Edu_Backend.Customer.entity.Customer;
// import com.Pahana_Edu_Backend.Customer.repository.CustomerRepository;
// import com.Pahana_Edu_Backend.Customer.service.CustomerService;

// import jakarta.mail.MessagingException;
// import jakarta.mail.internet.MimeMessage;

// @Service
// public class CustomerServiceImpl implements CustomerService {

//     @Autowired
//     private CustomerRepository customerRepository;

//     @Autowired
//     private JavaMailSender mailSender;

//     @Override
//     public Customer addCustomer(Customer customer) {
//         customer.setStatus("PENDING");
//         return customerRepository.save(customer);
//     }

//     @Override
//     public List<Customer> getAllCustomers() {
//         return customerRepository.findAll();
//     }

//     @Override
//     public Optional<Customer> getCustomerById(String customerId) {
//         return customerRepository.findById(customerId);
//     }

//     @Override
//     public void deleteCustomer(String customerId) {
//         customerRepository.deleteById(customerId);
//     }

//     @Override
//     public Customer updateCustomer(String customerId, Customer customer) {
//         Optional<Customer> existingCustomer = customerRepository.findById(customerId);

//         if (existingCustomer.isPresent()) {
//             Customer custToUpdate = existingCustomer.get();

//             // Update fields
//             custToUpdate.setCustomerName(customer.getCustomerName());
//             custToUpdate.setCustomerEmail(customer.getCustomerEmail());
//             custToUpdate.setAddress(customer.getAddress());
//             custToUpdate.setStatus(customer.getStatus());
//             custToUpdate.setUserName(customer.getUserName());
//             custToUpdate.setPassword(customer.getPassword());
//             custToUpdate.setCustomerPhone(customer.getCustomerPhone());
//             if (customer.getProfileImage() != null) {
//                 custToUpdate.setProfileImage(customer.getProfileImage());
//             }

//             return customerRepository.save(custToUpdate);
//         } else {
//             throw new RuntimeException("Customer not found with id: " + customerId);
//         }
//     }

//     @Override
//     public Customer updateProfileImage(String customerId, String profileImage) {
//         Optional<Customer> existingCustomer = customerRepository.findById(customerId);

//         if (existingCustomer.isPresent()) {
//             Customer custToUpdate = existingCustomer.get();
//             custToUpdate.setProfileImage(profileImage);
//             return customerRepository.save(custToUpdate);
//         } else {
//             throw new RuntimeException("Customer not found with id: " + customerId);
//         }
//     }

//     @Override
//     public boolean existsByUserName(String userName) {
//         return customerRepository.existsByUserName(userName);
//     }

//     @Override
//     public Customer verifyCustomer(String customerId) {
//         Optional<Customer> existingCustomer = customerRepository.findById(customerId);

//         if (existingCustomer.isPresent()) {
//             Customer customer = existingCustomer.get();
//             customer.setStatus("VERIFIED");
//             Customer updatedCustomer = customerRepository.save(customer);

//             // Send verification email
//             sendVerificationEmail(customer.getCustomerEmail(), customer.getCustomerName());
//             return updatedCustomer;
//         } else {
//             throw new RuntimeException("Customer not found with id: " + customerId);
//         }
//     }

//     // private void sendVerificationEmail(String email, String name) {
//     //     SimpleMailMessage message = new SimpleMailMessage();
//     //     message.setTo(email);
//     //     message.setSubject("Account Verification");
//     //     message.setText(String.format(
//     //         "Dear %s,\n\nYour account has been successfully verified by our admin team. " +
//     //         "You can now access all features of our platform.\n\nBest regards,\nPahana Edu Team",
//     //         name
//     //     ));
//     //     mailSender.send(message);
//     // }

//        private void sendVerificationEmail(String email, String name) {
//         try {
//             MimeMessage message = mailSender.createMimeMessage();
//             MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

//             helper.setTo(email);
//             helper.setSubject("Welcome to Pahana Edu - Account Verified!");
//             helper.setFrom("no-reply@pahanaedu.com"); // Update with your sender email

//             String htmlContent = """
//                 <!DOCTYPE html>
//                 <html lang="en">
//                 <head>
//                     <meta charset="UTF-8">
//                     <meta name="viewport" content="width=device-width, initial-scale=1.0">
//                     <style>
//                         body {
//                             margin: 0;
//                             padding: 0;
//                             font-family: 'Arial', sans-serif;
//                             background-color: #f4f4f4;
//                         }
//                         .container {
//                             max-width: 600px;
//                             margin: 20px auto;
//                             background-color: #ffffff;
//                             border-radius: 8px;
//                             overflow: hidden;
//                             box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
//                         }
//                         .header {
//                             background: linear-gradient(90deg, #FBBF24, #D97706);
//                             padding: 20px;
//                             text-align: center;
//                         }
//                         .header img {
//                             max-width: 150px;
//                             height: auto;
//                         }
//                         .content {
//                             padding: 30px;
//                             color: #333333;
//                             line-height: 1.6;
//                         }
//                         .content h1 {
//                             font-size: 24px;
//                             color: #1F2937;
//                             margin-bottom: 20px;
//                         }
//                         .content p {
//                             font-size: 16px;
//                             margin-bottom: 20px;
//                         }
//                         .button {
//                             display: inline-block;
//                             padding: 12px 24px;
//                             background-color: #FBBF24;
//                             color: #000000;
//                             text-decoration: none;
//                             border-radius: 5px;
//                             font-weight: bold;
//                             font-size: 16px;
//                         }
//                         .footer {
//                             background-color: #1F2937;
//                             color: #ffffff;
//                             padding: 20px;
//                             text-align: center;
//                             font-size: 14px;
//                         }
//                         .footer a {
//                             color: #FBBF24;
//                             text-decoration: none;
//                         }
//                         @media only screen and (max-width: 600px) {
//                             .container {
//                                 margin: 10px;
//                             }
//                             .header img {
//                                 max-width: 120px;
//                             }
//                             .content h1 {
//                                 font-size: 20px;
//                             }
//                             .content p {
//                                 font-size: 14px;
//                             }
//                             .button {
//                                 padding: 10px 20px;
//                                 font-size: 14px;
//                             }
//                         }
//                     </style>
//                 </head>
//                 <body>
//                     <div class="container">
//                         <div class="header">
//                             <img src="https://via.placeholder.com/150x50?text=Pahana+Edu" alt="Pahana Edu Logo" />
//                         </div>
                   
//                         <div class="footer">
//                             <p>Thank you for choosing Pahana Edu!</p>
//                             <p>Need help? <a href="mailto:support@pahanaedu.com">Contact Support</a></p>
//                             <p>&copy; 2025 Pahana Edu. All rights reserved.</p>
//                         </div>
//                     </div>
//                 </body>
//                 </html>
//                 """.formatted(name);

//             helper.setText(htmlContent, true); // true indicates HTML content
//             mailSender.send(message);
//         } catch (MessagingException e) {
//             throw new RuntimeException("Failed to send verification email: " + e.getMessage());
//         }
//     }
// }

// package com.Pahana_Edu_Backend.Customer.serviceImpl;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Service;

// import com.Pahana_Edu_Backend.Customer.entity.Customer;
// import com.Pahana_Edu_Backend.Customer.repository.CustomerRepository;
// import com.Pahana_Edu_Backend.Customer.service.CustomerService;

// import jakarta.mail.MessagingException;
// import jakarta.mail.internet.MimeMessage;

// @Service
// public class CustomerServiceImpl implements CustomerService {

//     @Autowired
//     private CustomerRepository customerRepository;

//     @Autowired
//     private JavaMailSender mailSender;

//     @Override
//     public Customer addCustomer(Customer customer) {
//         customer.setStatus("PENDING");
//         return customerRepository.save(customer);
//     }

//     @Override
//     public List<Customer> getAllCustomers() {
//         return customerRepository.findAll();
//     }

//     @Override
//     public Optional<Customer> getCustomerById(String customerId) {
//         return customerRepository.findById(customerId);
//     }

//     @Override
//     public void deleteCustomer(String customerId) {
//         customerRepository.deleteById(customerId);
//     }

//     @Override
//     public Customer updateCustomer(String customerId, Customer customer) {
//         Optional<Customer> existingCustomer = customerRepository.findById(customerId);

//         if (existingCustomer.isPresent()) {
//             Customer custToUpdate = existingCustomer.get();

//             // Update only non-null fields to prevent overwriting with null
//             if (customer.getCustomerName() != null) {
//                 custToUpdate.setCustomerName(customer.getCustomerName());
//             }
//             if (customer.getCustomerEmail() != null) {
//                 custToUpdate.setCustomerEmail(customer.getCustomerEmail());
//             }
//             if (customer.getAddress() != null) {
//                 custToUpdate.setAddress(customer.getAddress());
//             }
//             if (customer.getStatus() != null) {
//                 custToUpdate.setStatus(customer.getStatus());
//             }
//             if (customer.getUserName() != null) {
//                 custToUpdate.setUserName(customer.getUserName());
//             }
//             if (customer.getPassword() != null) {
//                 custToUpdate.setPassword(customer.getPassword());
//             }
//             if (customer.getCustomerPhone() != null) {
//                 custToUpdate.setCustomerPhone(customer.getCustomerPhone());
//             }
//             if (customer.getProfileImage() != null) {
//                 custToUpdate.setProfileImage(customer.getProfileImage());
//             }

//             return customerRepository.save(custToUpdate);
//         } else {
//             throw new RuntimeException("Customer not found with id: " + customerId);
//         }
//     }

//     @Override
//     public Customer updateProfileImage(String customerId, String profileImage) {
//         Optional<Customer> existingCustomer = customerRepository.findById(customerId);

//         if (existingCustomer.isPresent()) {
//             Customer custToUpdate = existingCustomer.get();
//             custToUpdate.setProfileImage(profileImage);
//             return customerRepository.save(custToUpdate);
//         } else {
//             throw new RuntimeException("Customer not found with id: " + customerId);
//         }
//     }

//     @Override
//     public boolean existsByUserName(String userName) {
//         return customerRepository.existsByUserName(userName);
//     }

//     @Override
//     public Customer verifyCustomer(String customerId) {
//         Optional<Customer> existingCustomer = customerRepository.findById(customerId);

//         if (existingCustomer.isPresent()) {
//             Customer customer = existingCustomer.get();
//             customer.setStatus("VERIFIED");
//             Customer updatedCustomer = customerRepository.save(customer);

//             // Send verification email
//             sendVerificationEmail(customer.getCustomerEmail(), customer.getCustomerName());
//             return updatedCustomer;
//         } else {
//             throw new RuntimeException("Customer not found with id: " + customerId);
//         }
//     }

//     private void sendVerificationEmail(String email, String name) {
//         try {
//             MimeMessage message = mailSender.createMimeMessage();
//             MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

//             helper.setTo(email);
//             helper.setSubject("Welcome to Pahana Edu - Account Verified!");
//             helper.setFrom("no-reply@pahanaedu.com");

//             String htmlContent = """
//                 <!DOCTYPE html>
//                 <html lang="en">
//                 <head>
//                     <meta charset="UTF-8">
//                     <meta name="viewport" content="width=device-width, initial-scale=1.0">
//                     <style>
//                         body {
//                             margin: 0;
//                             padding: 0;
//                             font-family: 'Arial', sans-serif;
//                             background-color: #f4f4f4;
//                         }
//                         .container {
//                             max-width: 600px;
//                             margin: 20px auto;
//                             background-color: #ffffff;
//                             border-radius: 8px;
//                             overflow: hidden;
//                             box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
//                         }
//                         .header {
//                             background: linear-gradient(90deg, #FBBF24, #D97706);
//                             padding: 20px;
//                             text-align: center;
//                         }
//                         .header img {
//                             max-width: 150px;
//                             height: auto;
//                         }
//                         .content {
//                             padding: 30px;
//                             color: #333333;
//                             line-height: 1.6;
//                         }
//                         .content h1 {
//                             font-size: 24px;
//                             color: #1F2937;
//                             margin-bottom: 20px;
//                         }
//                         .content p {
//                             font-size: 16px;
//                             margin-bottom: 20px;
//                         }
//                         .button {
//                             display: inline-block;
//                             padding: 12px 24px;
//                             background-color: #FBBF24;
//                             color: #000000;
//                             text-decoration: none;
//                             border-radius: 5px;
//                             font-weight: bold;
//                             font-size: 16px;
//                         }
//                         .footer {
//                             background-color: #1F2937;
//                             color: #ffffff;
//                             padding: 20px;
//                             text-align: center;
//                             font-size: 14px;
//                         }
//                         .footer a {
//                             color: #FBBF24;
//                             text-decoration: none;
//                         }
//                         @media only screen and (max-width: 600px) {
//                             .container {
//                                 margin: 10px;
//                             }
//                             .header img {
//                                 max-width: 120px;
//                             }
//                             .content h1 {
//                                 font-size: 20px;
//                             }
//                             .content p {
//                                 font-size: 14px;
//                             }
//                             .button {
//                                 padding: 10px 20px;
//                                 font-size: 14px;
//                             }
//                         }
//                     </style>
//                 </head>
//                 <body>
//                     <div class="container">
//                         <div class="header">
//                             <img src="https://via.placeholder.com/150x50?text=Pahana+Edu" alt="Pahana Edu Logo" />
//                         </div>
//                         <div class="content">
//                             <h1>Welcome, %s!</h1>
//                             <p>Your account has been successfully verified by our admin team. You can now access all features of our platform.</p>
//                             <p>Explore our educational resources and start your learning journey with us!</p>
//                             <a href="http://localhost:3000/login" class="button">Log In to Your Account</a>
//                         </div>
//                         <div class="footer">
//                             <p>Thank you for choosing Pahana Edu!</p>
//                             <p>Need help? <a href="mailto:support@pahanaedu.com">Contact Support</a></p>
//                             <p>© 2025 Pahana Edu. All rights reserved.</p>
//                         </div>
//                     </div>
//                 </body>
//                 </html>
//                 """.formatted(name);

//             helper.setText(htmlContent, true);
//             mailSender.send(message);
//         } catch (MessagingException e) {
//             throw new RuntimeException("Failed to send verification email: " + e.getMessage());
//         }
//     }
// }


package com.Pahana_Edu_Backend.Customer.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Pahana_Edu_Backend.Customer.entity.Customer;
import com.Pahana_Edu_Backend.Customer.entity.PasswordResetToken;
import com.Pahana_Edu_Backend.Customer.repository.CustomerRepository;
import com.Pahana_Edu_Backend.Customer.repository.PasswordResetTokenRepository;
import com.Pahana_Edu_Backend.Customer.service.CustomerService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Customer addCustomer(Customer customer) {
        customer.setStatus("PENDING");
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(String customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public Customer updateCustomer(String customerId, Customer customer) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);

        if (existingCustomer.isPresent()) {
            Customer custToUpdate = existingCustomer.get();

            // Update only non-null fields to prevent overwriting with null
            if (customer.getCustomerName() != null) {
                custToUpdate.setCustomerName(customer.getCustomerName());
            }
            if (customer.getCustomerEmail() != null) {
                custToUpdate.setCustomerEmail(customer.getCustomerEmail());
            }
            if (customer.getAddress() != null) {
                custToUpdate.setAddress(customer.getAddress());
            }
            if (customer.getStatus() != null) {
                custToUpdate.setStatus(customer.getStatus());
            }
            if (customer.getUserName() != null) {
                custToUpdate.setUserName(customer.getUserName());
            }
            if (customer.getPassword() != null) {
                custToUpdate.setPassword(customer.getPassword());
            }
            if (customer.getCustomerPhone() != null) {
                custToUpdate.setCustomerPhone(customer.getCustomerPhone());
            }
            if (customer.getProfileImage() != null) {
                custToUpdate.setProfileImage(customer.getProfileImage());
            }

            return customerRepository.save(custToUpdate);
        } else {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }
    }

    @Override
    public Customer updateProfileImage(String customerId, String profileImage) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);

        if (existingCustomer.isPresent()) {
            Customer custToUpdate = existingCustomer.get();
            custToUpdate.setProfileImage(profileImage);
            return customerRepository.save(custToUpdate);
        } else {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }
    }

    @Override
    public boolean existsByUserName(String userName) {
        return customerRepository.existsByUserName(userName);
    }

    @Override
    public Customer verifyCustomer(String customerId) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);

        if (existingCustomer.isPresent()) {
            Customer customer = existingCustomer.get();
            customer.setStatus("VERIFIED");
            Customer updatedCustomer = customerRepository.save(customer);

            // Send verification email
            sendVerificationEmail(customer.getCustomerEmail(), customer.getCustomerName());
            return updatedCustomer;
        } else {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }
    }

    private void sendVerificationEmail(String email, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Welcome to Pahana Edu - Account Verified!");
            helper.setFrom("no-reply@pahanaedu.com");

            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <style>
                            body {
                                margin: 0;
                                padding: 0;
                                font-family: 'Arial', sans-serif;
                                background-color: #f4f4f4;
                            }
                            .container {
                                max-width: 600px;
                                margin: 20px auto;
                                background-color: #ffffff;
                                border-radius: 8px;
                                overflow: hidden;
                                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                            }
                            .header {
                                background: linear-gradient(90deg, #FBBF24, #D97706);
                                padding: 20px;
                                text-align: center;
                            }
                            .header img {
                                max-width: 150px;
                                height: auto;
                            }
                            .content {
                                padding: 30px;
                                color: #333333;
                                line-height: 1.6;
                            }
                            .content h1 {
                                font-size: 24px;
                                color: #1F2937;
                                margin-bottom: 20px;
                            }
                            .content p {
                                font-size: 16px;
                                margin-bottom: 20px;
                            }
                            .button {
                                display: inline-block;
                                padding: 12px 24px;
                                background-color: #FBBF24;
                                color: #000000;
                                text-decoration: none;
                                border-radius: 5px;
                                font-weight: bold;
                                font-size: 16px;
                            }
                            .footer {
                                background-color: #1F2937;
                                color: #ffffff;
                                padding: 20px;
                                text-align: center;
                                font-size: 14px;
                            }
                            .footer a {
                                color: #FBBF24;
                                text-decoration: none;
                            }
                            @media only screen and (max-width: 600px) {
                                .container {
                                    margin: 10px;
                                }
                                .header img {
                                    max-width: 120px;
                                }
                                .content h1 {
                                    font-size: 20px;
                                }
                                .content p {
                                    font-size: 14px;
                                }
                                .button {
                                    padding: 10px 20px;
                                    font-size: 14px;
                                }
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <img src="https://via.placeholder.com/150x50?text=Pahana+Edu" alt="Pahana Edu Logo" />
                            </div>
                            <div class="content">
                                <h1>Welcome, %s!</h1>
                                <p>Your account has been successfully verified by our admin team. You can now access all features of our platform.</p>
                                <p>Explore our educational resources and start your learning journey with us!</p>
                                <a href="http://localhost:3000/login" class="button">Log In to Your Account</a>
                            </div>
                            <div class="footer">
                                <p>Thank you for choosing Pahana Edu!</p>
                                <p>Need help? <a href="mailto:support@pahanaedu.com">Contact Support</a></p>
                                <p>© 2025 Pahana Edu. All rights reserved.</p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(name);

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email: " + e.getMessage());
        }
    }

    @Override
    public void generateResetCode(String userName, String email) {
        Optional<Customer> customerOpt = customerRepository.findByUserName(userName);
        if (customerOpt.isEmpty() || !customerOpt.get().getCustomerEmail().equals(email)) {
            throw new RuntimeException("Invalid username or email");
        }

        // Delete any existing tokens for this user
        passwordResetTokenRepository.deleteByUserName(userName);

        // Generate a 4-digit code
        String code = String.format("%04d", new Random().nextInt(10000));
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10); // Code valid for 10 minutes
        PasswordResetToken resetToken = new PasswordResetToken(code, userName, expiryDate);
        passwordResetTokenRepository.save(resetToken);

        // Send code via email
        sendResetCodeEmail(email, customerOpt.get().getCustomerName(), code);
    }

    private void sendResetCodeEmail(String email, String name, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Pahana Edu - Password Reset Code");
            helper.setFrom("no-reply@pahanaedu.com");
            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <style>
                            body {
                                margin: 0;
                                padding: 0;
                                font-family: 'Arial', sans-serif;
                                background-color: #f4f4f4;
                            }
                            .container {
                                max-width: 600px;
                                margin: 20px auto;
                                background-color: #ffffff;
                                border-radius: 8px;
                                overflow: hidden;
                                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                            }
                            .header {
                                background: linear-gradient(90deg, #FBBF24, #D97706);
                                padding: 20px;
                                text-align: center;
                            }
                            .header img {
                                max-width: 150px;
                                height: auto;
                            }
                            .content {
                                padding: 30px;
                                color: #333333;
                                line-height: 1.6;
                            }
                            .content h1 {
                                font-size: 24px;
                                color: #1F2937;
                                margin-bottom: 20px;
                            }
                            .content p {
                                font-size: 16px;
                                margin-bottom: 20px;
                            }
                            .code {
                                font-size: 24px;
                                font-weight: bold;
                                color: #FBBF24;
                                text-align: center;
                                margin: 20px 0;
                            }
                            .footer {
                                background-color: #1F2937;
                                color: #ffffff;
                                padding: 20px;
                                text-align: center;
                                font-size: 14px;
                            }
                            .footer a {
                                color: #FBBF24;
                                text-decoration: none;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <img src="https://via.placeholder.com/150x50?text=Pahana+Edu" alt="Pahana Edu Logo" />
                            </div>
                            <div class="content">
                                <h1>Password Reset Code</h1>
                                <p>Dear %s,</p>
                                <p>We received a request to reset your password. Use the following code to reset your password:</p>
                                <div class="code">%s</div>
                                <p>This code will expire in 10 minutes. If you did not request a password reset, please ignore this email.</p>
                            </div>
                            <div class="footer">
                                <p>Thank you for choosing Pahana Edu!</p>
                                <p>Need help? <a href="mailto:support@pahanaedu.com">Contact Support</a></p>
                                <p>© 2025 Pahana Edu. All rights reserved.</p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(name, code);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reset code email: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyResetCode(String userName, String code) {
        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepository.findByToken(code);
        if (!tokenOpt.isPresent() || !tokenOpt.get().getUserName().equals(userName) || 
            tokenOpt.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }

    @Override
    public void resetPasswordWithCode(String userName, String code, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepository.findByToken(code);
        if (!tokenOpt.isPresent() || !tokenOpt.get().getUserName().equals(userName) || 
            tokenOpt.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired code");
        }
        Optional<Customer> customerOpt = customerRepository.findByUserName(userName);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setPassword(passwordEncoder.encode(newPassword));
            customerRepository.save(customer);
            passwordResetTokenRepository.deleteByUserName(userName); // Remove code after use
        } else {
            throw new RuntimeException("Customer not found");
        }
    }
}