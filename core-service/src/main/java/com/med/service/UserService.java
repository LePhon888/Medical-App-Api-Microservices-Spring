package com.med.service;
import com.med.dto.UserDTO;
import net.coobird.thumbnailator.Thumbnails;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.med.model.Provider;
import com.med.model.User;
import com.med.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
//public class UserService implements UserDetailsService {
public class UserService {
@Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public User getById (int id) {
        return userRepository.findById(id).orElse(null);
    }
    public User getUserByEmail (String email) {
        return this.userRepository.getUserByEmail(email);
    }
    public User create (User u) {
        return this.userRepository.save(u);
    }
    public Page<User> getUsers (Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    public User registerUser(Map<String, String> params) {
        User u = new User();
        u.setEmail(params.get("email"));
        u.setPassword(this.passwordEncoder.encode(params.get("password")));
        u.setUserRole("ROLE_PATIENT");
        u.setProvider(Provider.LOCAL);
        u.setIsActive((short) 1);
        return this.userRepository.save(u);
    }

    public boolean authUser (String email, String password) {
        User u = this.userRepository.getUserByEmail(email);
        return passwordEncoder.matches(password, u.getPassword());
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;
//    }
//    public void processOAuthPostLogin(DefaultOidcUser oidcUser) throws ParseException {
//        User existUser = userRepository.getUserByEmail(oidcUser.getEmail());
//
//        if (existUser == null) {
//            User newUser = new User();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String birthdate = oidcUser.getBirthdate();
//            if (birthdate != null) {
//                Date date = dateFormat.parse(birthdate);
//                newUser.setBirthday(date);
//            }
//
//            newUser.setFirstName(oidcUser.getFullName());
//            if (oidcUser.getGender() != null)
//                newUser.setGender(Integer.valueOf(oidcUser.getGender()));
//            newUser.setPhoneNumber(oidcUser.getPhoneNumber());
//            newUser.setEmail(oidcUser.getEmail());
//            newUser.setIsActive((short) 1);
//            newUser.setUserRole("ROLE_PATIENT");
//            newUser.setProvider(Provider.GOOGLE);
//            userRepository.save(newUser);
//        }
//    }
//    public void register(Map<String, String> params, String siteURL)
//            throws UnsupportedEncodingException, MessagingException {
//        User u = new User();
//        u.setEmail(params.get("email"));
//        u.setFirstName(params.get("firstName"));
//        u.setPassword(this.passwordEncoder.encode(params.get("password")));
//        u.setUserRole("ROLE_PATIENT");
//        u.setProvider(Provider.LOCAL);
//        u.setIsActive((short) 1);
//
//        //"java.util.Random@775824f"
//        String randomCode = new Random(64).toString().substring(17);
//        System.out.println("randomCode " + randomCode);
//        u.setCode(Integer.valueOf(randomCode));
//        u.setEnabled(false);
//
//        this.userRepository.save(u);
//
//        sendVerificationEmail(u, siteURL);
//    }

//    private void sendVerificationEmail(User user, String siteURL)
//            throws MessagingException, UnsupportedEncodingException {
//        String toAddress = user.getEmail();
//        String fromAddress = "lephon888@gmail.com";
//        String senderName = "Medical App";
//        String subject = "Xác thực tài khoản Medical App";
//        String content = "Gửi [[name]],<br>"
//                + "Vui lòng nhấn vào vefiry để hoàn tất quá trình đăng ký tài khoản của bạn:<br>"
//                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
//                + "Cảm ơn,<br>"
//                + "Medical App.";
//
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//
//        helper.setFrom(fromAddress, senderName);
//        helper.setTo(toAddress);
//        helper.setSubject(subject);
//
//        content = content.replace("[[name]]", user.getFirstName());
////        String verifyURL = siteURL + "/auth/verify?code=" + user.getVerificationCode();
//
////        String verifyURL = "http://192.168.1.4:8080" + "/auth/verify?code=" + user.getVerificationCode();
//        String verifyURL = "http://192.168.43.97" + "/auth/verify?code=" + user.getVerificationCode();
//
//
//        content = content.replace("[[URL]]", verifyURL);
//
//        helper.setText(content, true);
//
//        mailSender.send(message);
//
//    }
//    public boolean verify(String verificationCode) {
//        User user = this.userRepository.findByVerificationCode(verificationCode);
//
//        if (user == null || user.isEnabled()) {
//            return false;
//        } else {
//            user.setVerificationCode(verificationCode);
//            user.setEnabled(true);
//            this.userRepository.save(user);
//
//            return true;
//        }
//    }

    public boolean delete(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean update(User user) {
        try {
            MultipartFile file = user.getFile();
            if (file != null && !file.isEmpty()) {
                try {

                    ByteArrayOutputStream compressedImageStream = new ByteArrayOutputStream();
                    Thumbnails.of(file.getInputStream())
                            .size(800, 600)
                            .outputQuality(0.8)
                            .toOutputStream(compressedImageStream);


                    Map uploadResult = this.cloudinary.uploader().upload(compressedImageStream.toByteArray(),
                            ObjectUtils.asMap("resource_type", "auto" ));

                    user.setImage(uploadResult.get("secure_url" ).toString());
                } catch (IOException e) {
                    throw new RuntimeException("Error compressing or uploading the image", e);
                }
            }
            if (!user.getPassword().contains("$2a$"))
                user.setPassword(passwordEncoder.encode(user.getPassword()));

            this.userRepository.save(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void updateUserPasswordByEmail(String email, String newPassword) {
        User user = userRepository.getUserByEmail(email);
        if (user != null ){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    @Async
    public CompletableFuture<ResponseEntity<String>> sendEmailOTP(String email) {
        User checkUser = userRepository.getUserByEmail(email);
        if (checkUser != null) {
            kafkaTemplate.send("otp-send", email);
            return CompletableFuture.completedFuture(ResponseEntity.ok("OTP sent successfully"));
        } else {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found"));
        }
    }

    public List<UserDTO> findByParentId(int parentId) {
        return this.userRepository.findByParentId(parentId);
    }

}
