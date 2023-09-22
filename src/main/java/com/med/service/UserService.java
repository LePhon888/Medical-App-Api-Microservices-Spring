package com.med.service;

import com.med.model.Provider;
import com.med.model.User;
import com.med.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JavaMailSender mailSender;

    public User getById (int id) {
        return userRepository.findById(id).orElse(null);
    }
    public User getUserByEmail (String email) {
        return this.userRepository.getUserByEmail(email);
    }
    public User create (User u) {
        return this.userRepository.save(u);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
    public void processOAuthPostLogin(DefaultOidcUser oidcUser) throws ParseException {
        User existUser = userRepository.getUserByEmail(oidcUser.getEmail());

        if (existUser == null) {
            User newUser = new User();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String birthdate = oidcUser.getBirthdate();
            if (birthdate != null) {
                Date date = dateFormat.parse(birthdate);
                newUser.setBirthday(date);
            }

            newUser.setFirstName(oidcUser.getFullName());
            if (oidcUser.getGender() != null)
                newUser.setGender(Integer.valueOf(oidcUser.getGender()));
            newUser.setPhoneNumber(oidcUser.getPhoneNumber());
            newUser.setEmail(oidcUser.getEmail());
            newUser.setIsActive((short) 1);
            newUser.setUserRole("ROLE_PATIENT");
            newUser.setProvider(Provider.GOOGLE);
            userRepository.save(newUser);
        }
    }
    public void register(Map<String, String> params, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        User u = new User();
        u.setEmail(params.get("email"));
        u.setFirstName(params.get("firstName"));
        u.setPassword(this.passwordEncoder.encode(params.get("password")));
        u.setUserRole("ROLE_PATIENT");
        u.setProvider(Provider.LOCAL);
        u.setIsActive((short) 1);

        //"java.util.Random@775824f"
        String randomCode = new Random(64).toString().substring(17);
        System.out.println("randomCode " + randomCode);
        u.setVerificationCode(randomCode);
        u.setEnabled(false);

        this.userRepository.save(u);

        sendVerificationEmail(u, siteURL);
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "lephon888@gmail.com";
        String senderName = "Medical App";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Medical App.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());
        String verifyURL = siteURL + "/auth/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }
    public boolean verify(String verificationCode) {
        User user = this.userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            this.userRepository.save(user);

            return true;
        }

    }
}
