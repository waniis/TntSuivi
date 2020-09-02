package com.mycompany.myapp.service;

import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.domain.AdminDeCentre;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.Centre;
import com.mycompany.myapp.domain.Medecin;
import com.mycompany.myapp.domain.Notification;
import com.mycompany.myapp.domain.Patient;
import com.mycompany.myapp.domain.Specialty;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.enumeration.Alcool;
import com.mycompany.myapp.domain.enumeration.Sexe;
import com.mycompany.myapp.domain.enumeration.Tobacco;
import com.mycompany.myapp.repository.AdminDeCentreRepository;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.dto.UserDTO;
import com.mycompany.myapp.repository.MedecinRepository;
import com.mycompany.myapp.repository.PatientRepository;

import io.github.jhipster.security.RandomUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {


    private final AdminDeCentreRepository adminDeCentreRepository;

    private final MedecinRepository medecinRepository;

    private final PatientRepository patientRepository;

    private final  MailService mailService;

    private final NotificationService notificationService;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    public UserService(  AdminDeCentreRepository adminDeCentreRepository , PatientRepository patientRepository,  MedecinRepository medecinRepository , MailService mailService ,NotificationService notificationService , UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager) {
        this.adminDeCentreRepository = adminDeCentreRepository;
        this.patientRepository=patientRepository;
        this.medecinRepository = medecinRepository;
        this.mailService= mailService;
        this.notificationService=notificationService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new UsernameAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated()) {
             return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }


    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

   // function de creation d'un compte medecin 
     public Medecin createMedecin(User userDTO  , String phone , String phone2 , String adress ,Sexe sexe , Centre centre ,Specialty specialty  ) {
        User user = new User();
       
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
       
        String clearPassword = RandomUtil.generatePassword();
        String encryptedPassword = passwordEncoder.encode(clearPassword);
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            Authority authority = new Authority();
            authority.setName(AuthoritiesConstants.MEDECIN);
            authorities.add(authority);
            user.setAuthorities(authorities);
        }
      
        userRepository.save(user);
        this.clearUserCaches(user);
        Medecin newMedecin = new Medecin();
        newMedecin.setUser(user);
        newMedecin.setFullName(user.getFirstName()+" " + user.getLastName());
        newMedecin.setPhone(phone);
        newMedecin.setPhone2(phone2);
        newMedecin.setAdress(adress);
        newMedecin.setSexe(sexe);
        newMedecin.setCentre(centre);
        newMedecin.setSpecialty(specialty);
        medecinRepository.save(newMedecin);
        log.debug("Created Information for UserExtra: {}", user);
        Notification notification = new Notification();
        notification.setDateTime(Instant.now());
        notification.setUser(user);
        notification.setDescription("BienVenue Chez TntSuivi");
        log.debug(clearPassword);
        notificationService.save(notification);
        mailService.sendEmail(user.getEmail(), "Bienvenue", "you login is :"+user.getLogin()+" password :"+clearPassword, false,true);
        mailService.SendMailCreationUser(user.getEmail(), "mail/creationUser", "email.reset.title");
        return newMedecin;

    }

	public Patient createPatient(User userDTO, String phone, String adress, Sexe sexe, Alcool alcool,
			LocalDate startDateAlcool, LocalDate endDateAlcool, Tobacco tobacoo, LocalDate startDateTobacco,
			LocalDate endDateTobacco, Centre centre, Medecin medecin) {
                User user = new User();
       
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                if (userDTO.getLangKey() == null) {
                    user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
                } else {
                    user.setLangKey(userDTO.getLangKey());
                }
                String clearPassword = "sinaw1920";
                //String clearPassword = RandomUtil.generatePassword();
                String encryptedPassword = passwordEncoder.encode(clearPassword);
                user.setPassword(encryptedPassword);
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                user.setActivated(true);
                if (userDTO.getAuthorities() != null) {
                    Set<Authority> authorities = new HashSet<>();
                    Authority authority = new Authority();
                    authority.setName(AuthoritiesConstants.PATIENT);
                    authorities.add(authority);
                    user.setAuthorities(authorities);
                }
              
                userRepository.save(user);
                this.clearUserCaches(user);
                Patient newpatient = new Patient();
                newpatient.setUser(user);
                newpatient.setFullName(user.getFirstName()+" " + user.getLastName());
                newpatient.setPhone(phone);
                newpatient.setAdress(adress);
                newpatient.setSexe(sexe);
                newpatient.setCentre(centre);
                newpatient.setAlcool(alcool);
                newpatient.setStartDateAlcool(startDateAlcool);
                newpatient.setEndDateAlcool(endDateAlcool);
                newpatient.setTobacoo(tobacoo);
                newpatient.setStartDateTobacco(startDateTobacco);
                newpatient.setEndDateTobacco(endDateTobacco);
                newpatient.setMedecin(medecin);
                patientRepository.save(newpatient);
                log.debug("Created Information for UserExtra: {}", user);
                Notification notification = new Notification();
                notification.setDateTime(Instant.now());
                notification.setUser(user);
                notification.setDescription("BienVenue Chez TntSuivi");
                
                
               // notificationService.save(notification);

               // mailService.sendEmail(user.getEmail(), "Bienvenue", "you login is :"+user.getLogin()+" password :"+clearPassword, false,true);
                //mailService.SendMailCreationUser(user.getEmail(), "mail/creationUser", "email.reset.title");
                return newpatient;
	}

	public AdminDeCentre createAdminDeCentre(User userDTO, String phone, Centre centre) {
	    User user = new User();
       
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
       // String clearPassword = RandomUtil.generatePassword();
        String clearPassword ="sinaw1920";
        String encryptedPassword = passwordEncoder.encode(clearPassword);
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            Authority authority = new Authority();
            authority.setName(AuthoritiesConstants.ADMIN_DE_CENTRE);
            authorities.add(authority);
            user.setAuthorities(authorities);
        }
      
        userRepository.save(user);
        this.clearUserCaches(user);
        AdminDeCentre newAdminDeCentre = new AdminDeCentre();
        newAdminDeCentre.setUser(user);
        newAdminDeCentre.setFullName(user.getFirstName()+" " + user.getLastName());
        newAdminDeCentre.setPhone(phone);
        newAdminDeCentre.setCentre(centre);
        adminDeCentreRepository.save(newAdminDeCentre);
        log.debug("Created Information for UserExtra: {}", user);
        Notification notification = new Notification();
        notification.setDateTime(Instant.now());
        notification.setUser(user);
        notification.setDescription("BienVenue Chez TntSuivi");
       // notificationService.save(notification);
       // mailService.sendEmail(user.getEmail(), "Bienvenue", "you login is :"+user.getLogin()+" password :"+clearPassword, false,true);
        //mailService.SendMailCreationUser(user.getEmail(), "mail/creationUser", "email.reset.title");
        return newAdminDeCentre;
	}   
}
