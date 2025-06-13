package com.demo.e_commerce.Controller;

import org.springframework.http.HttpHeaders;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.e_commerce.Models.ERole;
import com.demo.e_commerce.Models.Role;
import com.demo.e_commerce.Models.User;
import com.demo.e_commerce.Payload.Request.LoginRequest;
import com.demo.e_commerce.Payload.Request.SignupRequest;
import com.demo.e_commerce.Payload.Respone.MessageResponse;

import com.demo.e_commerce.Repository.RoleRepository;
import com.demo.e_commerce.Repository.UserRepository;
import com.demo.e_commerce.Service.UserDetailsImpl;
import com.demo.e_commerce.jwt.JwtUtils;

import jakarta.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
            RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }
    // @PostMapping("/signin")
    // public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest) {
    // // gửi thông tin đăng nhập từ client lên server
    //     // Kiểm tra thông tin đăng nhập, xác thực username và password
    //     Authentication authentication = authenticationManager
    //         .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    //     // Nếu thông tin đăng nhập hợp lệ, lưu thông tin xác thực vào SecurityContext
    //     SecurityContextHolder.getContext().setAuthentication(authentication);
    //     // tạo một đối tượng UserDetailsImpl từ thông tin xác thực
    //     UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    //     // Tạo một cookie JWT từ thông tin người dùng và gửi lại cho request tiếp theo
    //     ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
    //     // Lấy danh sách quyền của người dùng và chuyển đổi thành danh sách chuỗi
    //     List<String> roles = userDetails.getAuthorities().stream()
    //         .map(item -> item.getAuthority())
    //         .collect(Collectors.toList());
    //     // Trả về ResponseEntity với mã trạng thái OK, cookie JWT và thông tin người dùng
    //     return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
    //         .body(new UserInfoResponse(userDetails.getId(),
    //                                 userDetails.getUsername(),
    //                                 userDetails.getEmail(),
    //                                 roles, jwtCookie.getValue()));
    // }
  // @PostMapping("/signup")
  // public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
  //   if (userRepository.existsByUsername(signUpRequest.getUsername())) {
  //     return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
  //   }

  //   if (userRepository.existsByEmail(signUpRequest.getEmail())) {
  //     return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
  //   }

  //   // Create new user's account
  //   User user = new User(signUpRequest.getFirstName(),
  //                       signUpRequest.getLastName(),
  //                       signUpRequest.getUsername(),
  //                       signUpRequest.getEmail(),
  //                       encoder.encode(signUpRequest.getPassword()));

  //   Set<String> strRoles = signUpRequest.getRole();
  //   Set<Role> roles = new HashSet<>();

  //   if (strRoles == null) {
  //     Role userRole = roleRepository.findByName(ERole.ROLE_USER)
  //         .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
  //     roles.add(userRole);
  //   } else {
  //     strRoles.forEach(role -> {
  //       switch (role) {
  //       case "admin":
  //         Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
  //             .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
  //         roles.add(adminRole);

  //         break;
  //       default:
  //         Role userRole = roleRepository.findByName(ERole.ROLE_USER)
  //             .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
  //         roles.add(userRole);
  //       }
  //     });
  //   }

  //   user.setRoles(roles);
  //   userRepository.save(user);

  //   return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  // }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }

  @GetMapping("/signin")
  public String showLoginForm(Model m) {
    m.addAttribute("loginRequest", new LoginRequest());
    return "auth/signin"; // Tên file HTML
  }

    @GetMapping("/signup")
    public String register(Model m) {
      m.addAttribute("signupRequest", new SignupRequest());
      return "auth/signup";
    }
  @PostMapping("/signin-form")
  public String loginWithForm(@ModelAttribute LoginRequest loginRequest,
                              HttpServletResponse response,
                              Model model) {
      try {
          Authentication authentication = authenticationManager
              .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

          SecurityContextHolder.getContext().setAuthentication(authentication);
          UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
          ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
          response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

          return "redirect:/auth/home";
      } catch (Exception e) {
          model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
          return "auth/signin";
      }
  }

  @GetMapping("/home")
  public String home() {
      return "home/index";
  }
@PostMapping("/signup-form")
public String signupForm(@ModelAttribute SignupRequest signUpRequest,
                         Model model) {

    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
        model.addAttribute("error", "Tên đăng nhập đã được sử dụng!");
        return "auth/signup"; // Hiển thị lại form đăng ký kèm lỗi
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
        model.addAttribute("error", "Email đã được sử dụng!");
        return "auth/signup"; // Hiển thị lại form đăng ký kèm lỗi
    }

    // Tạo user mới
    User user = new User(
        signUpRequest.getFirstName(),
        signUpRequest.getLastName(),
        signUpRequest.getUsername(),
        signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword())
    );

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
            .orElseThrow(() -> new RuntimeException("Role không tồn tại!"));
        roles.add(userRole);
    } else {
        strRoles.forEach(role -> {
            switch (role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Role không tồn tại!"));
                    roles.add(adminRole);
                    break;
                default:
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Role không tồn tại!"));
                    roles.add(userRole);
            }
        });
    }

    user.setRoles(roles);
    userRepository.save(user);

    // Chuyển hướng sang trang đăng nhập sau khi đăng ký thành công
    return "redirect:/auth/signin";
}


}
