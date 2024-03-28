package com.ccr.mvp.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ccr.mvp.dto.JwtAuthenticationResponse;
import com.ccr.mvp.dto.SignInRequest;
import com.ccr.mvp.dto.SignUpRequest;
import com.ccr.mvp.model.Candidate;
import com.ccr.mvp.model.Company;
import com.ccr.mvp.model.Image;
import com.ccr.mvp.model.Recruiter;
import com.ccr.mvp.model.Role;
import com.ccr.mvp.model.User;
import com.ccr.mvp.repository.RecruiterRepository;
import com.ccr.mvp.repository.UserRepository;
import com.ccr.mvp.service.AuthenticationService;
import com.ccr.mvp.service.CandidateService;
import com.ccr.mvp.service.CompanyService;
import com.ccr.mvp.service.JwtService;
import com.ccr.mvp.service.RecruiterService;
import com.ccr.mvp.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository userRepository;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final CandidateService candidateService;
	private final ImageService imageService;
	private final CompanyService companyService;
	private final RecruiterService recruiterService;

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	RecruiterRepository recruiterRepository;

	public JwtAuthenticationResponse signin(SignInRequest request) {
		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			var user = userRepository.findByEmail(request.getEmail())
					.orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

			var recruiter = user.getRecruiter();
			if (recruiter != null) {
				var company = recruiter.getCompany();
				if (company != null) {
					if (!company.getRegistrationApproval()) {
						throw new IllegalArgumentException("Company registration not approved.");
					}
				}
			}

			var jwt = jwtService.generateToken(user);

			return JwtAuthenticationResponse.builder().token(jwt).email(user.getEmail()).role(user.getRole())
					.userId(user.getUserId()).userName(user.getUserName()).build();
		} catch (Exception e) {
			System.out.println(e);
			throw new IllegalArgumentException("Something Wrong");
		}

	}

//	@Override
//	public ResponseEntity<?> candidateSignup(SignUpRequest request) {
//		try {
//			Candidate candidate = new Candidate();
//
//			   var user = User.builder()
//		                .userName(request.getUserName())
//		                .email(request.getEmail())
//		                .password(passwordEncoder.encode(request.getPassword()))
//		                .phoneNumber(request.getPhoneNumber())
//		                .role(Role.ROLE_CANDIDATE)
//		                .profilePictureFile(profilePictureFile) // set profile picture file
//		                .build();
//
//		        user = userService.saveUser(user);
//		        candidate = candidateService.candidateRegistration(candidate);
//
//		        // Save profile picture to storage (you need to implement this)
//		        saveProfilePicture(user.getUserId(), profilePictureFile);
//
//		        return ResponseEntity.status(HttpStatus.CREATED).body("Candidate Registered successfully");
//		    } catch (Exception e) {
//		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Wrong");
//		    }
//		}
//
//		private void saveProfilePicture(Long userId, MultipartFile profilePictureFile) {
//		    // Implement logic to save the profile picture to your storage (e.g., AWS S3, local storage, etc.)
//		    // Make sure to handle naming, storage, and retrieval of profile pictures
//		}

	@Override
	public ResponseEntity<?> candidateSignup(String userName, String candidateAadhar, String candidateDob,
			String phoneNumber, String email, String password, MultipartFile imageData) {
		try {
			Candidate candidate = new Candidate();

			var user = User.builder().userName(userName).email(email).password(passwordEncoder.encode(password))
					.phoneNumber(Long.parseLong(phoneNumber)).role(Role.ROLE_CANDIDATE).build();

			user = userService.saveUser(user);

			Image image = new Image();
			image.setData(imageData.getBytes());
			image.setUser(user);
			imageService.saveImage(image);

			candidate.setImage(image);
			candidate = candidateService.candidateRegistration(candidate);

			return ResponseEntity.status(HttpStatus.CREATED).body("Candidate Registered successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Wrong");
		}
	}

	@Override
	public ResponseEntity<?> hrAdminSignup(SignUpRequest request) {
		try {
			var user = User.builder().userName(request.getUserName()).email(request.getEmail())
					.password(passwordEncoder.encode(request.getPassword())).phoneNumber(request.getPhoneNumber())
					.role(Role.ROLE_HRADMIN).build();

			Company company = new Company();
			Recruiter hrRecruiter = new Recruiter();
			long defaultAddedBy = 0;

			company.setCompanyName(request.getCompanyName());
			company.setCompanyAddress(request.getCompanyAddress());
			company.setCompanyPhoneNumber(request.getCompanyPhoneNumber());
			company.setCompanyTan(request.getCompanyTan());
			company.setRegistrationApproval(null);
			hrRecruiter.setApprovePower(true);
			hrRecruiter.setAddedPower(true);
			hrRecruiter.setAddedBy(defaultAddedBy);
			hrRecruiter.setUser(user);
			hrRecruiter.setCompany(company);
			user = userService.saveUser(user);
			company = companyService.companyRegistration(company);
			hrRecruiter.setApprover(user.getUserId());
			hrRecruiter = recruiterService.hrAdminRegistration(hrRecruiter);
			return ResponseEntity.status(HttpStatus.CREATED).body("HRADMIN & Company Registered sucessfully");

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Wrong");
		}
	}

	@Override
	public ResponseEntity<?> recruiterSignup(SignUpRequest request) {
		try {

			User u = new User();
			Long userId = request.getUserId();
			
			//Previous Code
			//u = userRepository.findById(userId).get();
			
			//Code Given by Sonarqube
			Optional<User> optionalUser = userRepository.findById(userId); u = optionalUser.orElse(null); 
			

			


			boolean a = u.getRecruiter().isAddedPower();
			boolean b = u.getRecruiter().isApprovePower();

			if (a == true && b == true) {
				if (request.isAddedPower() == true && request.isApprovePower() == true) {
					bothTrue(request);
				}

				else if (request.isAddedPower() == true && request.isApprovePower() == false) {
					oneTrue(request);
				}

				else if (request.isAddedPower() == false && request.isApprovePower() == false) {
					bothFalse(request);
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong Powers");
				}
			}

			else if (a == true && b == false) {

				if (request.isAddedPower() == true && request.isApprovePower() == false) {
					oneTrue(request);
				} else if (request.isAddedPower() == false && request.isApprovePower() == false) {
					bothFalse(request);
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body("Wrong Powers or You don't have power to add/approve");
				}
			}

			else {

				return ResponseEntity.status(HttpStatus.CREATED).body("You don't have power to add");
			}

		}

		catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Wrong");
		}
		return null;

	}

	public ResponseEntity<?> bothTrue(SignUpRequest request) {
		Recruiter recruiter = new Recruiter();
		Company company = new Company();
		var user = User.builder().userName(request.getUserName()).email(request.getEmail())
				.password(passwordEncoder.encode("123")).phoneNumber(request.getPhoneNumber()).role(Role.ROLE_HRADMIN)
				.build();
		user = userService.saveUser(user);

		recruiter.setAddedPower(true);
		recruiter.setApprovePower(true);

		recruiter.setAddedBy(request.getUserId());
		recruiter.setApprover(user.getUserId());

		/// setcompanyid from the getting hr admin company id

		User user1 = new User();
		Recruiter recruiter1 = new Recruiter();

		user1 = userRepository.findById(request.getUserId()).get();
		recruiter1 = recruiterRepository.findByUser(user1);
		company.setCompanyId(recruiter1.getCompany().getCompanyId());

		recruiter.setCompany(company);
		recruiter.setUser(user);

		recruiter = recruiterService.recruiterRegistration(recruiter);
		return ResponseEntity.status(HttpStatus.CREATED).body("HR Admin Registered sucessfully");
	}

	public ResponseEntity<?> oneTrue(SignUpRequest request) {
		Recruiter recruiter = new Recruiter();
		Company company = new Company();
		User u = new User();
		long userId = request.getUserId();
		u = userRepository.findById(userId).get();
		long approver = u.getRecruiter().getApprover();

		var user = User.builder().userName(request.getUserName()).email(request.getEmail())
				.password(passwordEncoder.encode("123")).phoneNumber(request.getPhoneNumber()).role(Role.ROLE_RECRUITER)
				.build();
		user = userService.saveUser(user);
		recruiter.setAddedPower(true);
		recruiter.setApprovePower(false);
		recruiter.setAddedBy(request.getUserId());
		recruiter.setApprover(approver);

		/// setcompanyid from the getting hr admin company id

		User user1 = new User();
		Recruiter recruiter1 = new Recruiter();

		user1 = userRepository.findById(request.getUserId()).get();
		recruiter1 = recruiterRepository.findByUser(user1);
		company.setCompanyId(recruiter1.getCompany().getCompanyId());

		recruiter.setCompany(company);

		recruiter.setUser(user);
		recruiter = recruiterService.recruiterRegistration(recruiter);
		return ResponseEntity.status(HttpStatus.CREATED).body("Recruiter Registered sucessfully");
	}

	public ResponseEntity<?> bothFalse(SignUpRequest request) {
		Recruiter recruiter = new Recruiter();
		Company company = new Company();
		User u = new User();
		long userId = request.getUserId();
		u = userRepository.findById(userId).get();
		long approver = u.getRecruiter().getApprover();

		var user = User.builder().userName(request.getUserName()).email(request.getEmail())
				.password(passwordEncoder.encode("123")).phoneNumber(request.getPhoneNumber()).role(Role.ROLE_RECRUITER)
				.build();
		user = userService.saveUser(user);
		recruiter.setAddedPower(false);
		recruiter.setApprovePower(false);
		recruiter.setAddedBy(request.getUserId());
		recruiter.setApprover(approver);
		/// setcompanyid from the getting hr admin company id

		User user1 = new User();
		Recruiter recruiter1 = new Recruiter();

		user1 = userRepository.findById(request.getUserId()).get();
		recruiter1 = recruiterRepository.findByUser(user1);
		company.setCompanyId(recruiter1.getCompany().getCompanyId());

		recruiter.setCompany(company);
		recruiter.setUser(user);
		recruiter = recruiterService.recruiterRegistration(recruiter);
		return ResponseEntity.status(HttpStatus.CREATED).body("Recruiter Registered sucessfully");

	}

	@Override
	public ResponseEntity<?> ccrSignup(SignUpRequest request) {
		try {
			var user = User.builder().userName(request.getUserName()).email(request.getEmail())
					.password(passwordEncoder.encode("123")).phoneNumber(request.getPhoneNumber())
					.role(Role.ROLE_CCRADMIN).build();
			user = userService.saveUser(user);
			return ResponseEntity.status(HttpStatus.CREATED).body("CCR ADMIN Registered sucessfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something Wrong");
		}

	}

	// OTP Forgot password

	private Date calculateExpirationTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, 3); // Add 3 minutes to the current time
		return calendar.getTime();
	}

	public ResponseEntity<String> sendOtpByEmail(User user) {
		Session session = entityManager.unwrap(Session.class);

		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<User> cr = cb.createQuery(User.class);

		Root<User> root = cr.from(User.class);
		cr.select(root).where(cb.equal(root.get("email"), user.getEmail()));

		Query query = session.createQuery(cr);

		User retrievedUser = (User) query.getSingleResult();
		System.out.println(retrievedUser.getEmail());

		if (retrievedUser != null) {
			int otp = generateOtp();

			Date expirationTime = calculateExpirationTime();

			retrievedUser.setUserOtp(otp);
			retrievedUser.setOtpExpiration(expirationTime);

			userRepository.save(retrievedUser);
			String i = retrievedUser.getEmail();
			System.out.println(i);
			try {
				sendOtpEmail(i, otp);
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			} catch (MessagingException e) {

				e.printStackTrace();
			}
			session.close();
			return ResponseEntity.ok("OTP sent successfully");

		}

		return null;

	}

	private int generateOtp() {
		int min = 10000;
		int max = 99999;
		int token = (int) (Math.random() * (max - min + 1) + min);

		return token;
	}

	private void sendOtpEmail(String email, int otp) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setFrom("yashporlekar8888@gmail.com", "CCR");
		helper.setTo(email);
		String subject = "Here's the link to reset your password";
		String content = "<p>Hello ,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Here is your OTP: " + otp + "<br>" + "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p>";
		message.setSubject(subject);
		helper.setText(content, true);
		javaMailSender.send(message);
	}

	public ResponseEntity<String> userOtpValidation(User user) {
		Session session = entityManager.unwrap(Session.class);
		try {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<User> cr = cb.createQuery(User.class);
			Root<User> root = cr.from(User.class);
			cr.select(root).where(cb.equal(root.get("userOtp"), user.getUserOtp()));
			Query query = session.createQuery(cr);

			User result = (User) query.getSingleResult();

			if (result != null) {
				Date currentTimestamp = new Date(); // Current time
				if (currentTimestamp.before(result.getOtpExpiration())) {
					// OTP is still valid, allow the password change
					// Perform password change logic here using the 'result' object

					session.close();
					return ResponseEntity.status(HttpStatus.OK).body("You Have Entered Correct OTP....");
				} else {
					// OTP has expired

					session.close();
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body("OTP has expired. Please request a new OTP.");
				}
			}

//				session.close();
//				return ResponseEntity.status(HttpStatus.OK).body("You Have Entered Correct OTP....");
		} catch (Exception e) {
			session.close();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please Enter Correct OTP....");
		}
		return null;

	}

	public ResponseEntity<String> userChangePassword(User user) {

		Session session = entityManager.unwrap(Session.class);
		try {
			CriteriaBuilder cb = session.getCriteriaBuilder();
			CriteriaQuery<User> cr = cb.createQuery(User.class);

			Root<User> root = cr.from(User.class);
			cr.select(root).where(cb.equal(root.get("email"), user.getEmail()));

			Query query = session.createQuery(cr);

			User retrievedUser = (User) query.getSingleResult();

			if (retrievedUser != null) {
				retrievedUser.setPassword(passwordEncoder.encode(user.getPassword()));
				retrievedUser.setUserOtp(null);
				userRepository.save(retrievedUser);

				session.close();

				return ResponseEntity.status(HttpStatus.CREATED).body("Password Changed Sucessfully");
			}

		} catch (Exception e) {
			session.close();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Something wrong");
		}
		return null;

	}

//	
//	
//	 public ResponseEntity<?> signout(String token) {
//	        // Extract the token from the Authorization header.
//		 
//	
//		String jwtToken = token.substring(7); // Remove "Bearer " prefix
//System.out.println("Token is"+jwtToken);
//	        // Add the JWT token to the list of invalidated tokens.
//	       
//	        
//
//	        return ResponseEntity.ok("User successfully signed out.");
//	    }

}