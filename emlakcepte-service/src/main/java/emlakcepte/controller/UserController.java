package emlakcepte.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import emlakcepte.model.Payment;
import emlakcepte.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import emlakcepte.dto.model.request.UserRequest;
import emlakcepte.dto.model.response.UserResponse;
import emlakcepte.service.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	private final UserService userService;
	private final Logger logger = Logger.getLogger(UserController.class.getName());
	public UserController(UserService userService)
	{
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAll()
	{
		return ResponseEntity.ok(userService.getAll());
	}

	@PostMapping
	public ResponseEntity<UserResponse> create(@RequestBody UserRequest userRequest) {
		UserResponse userResponse = userService.createUser(userRequest);

		logger.log(Level.INFO, "user created: {0}", userResponse);
		return ResponseEntity.ok(userResponse);
	}
	@GetMapping(value = "/{email}")
	public ResponseEntity<UserResponse> getByEmail(@PathVariable String email)
	{
		return ResponseEntity.ok(userService.getByEmail(email));
	}

	@PutMapping
	public ResponseEntity<UserResponse> update(@RequestBody User userUpdate)   //UserUpdateRequest userUpdateRequest)
	{
		var user = userService.update(userUpdate.getId(),userUpdate);
		return ResponseEntity.ok(user);
	}

	@PostMapping("/process/{cardNo}/{amount}")
	public ResponseEntity<Payment> processPayment (@RequestBody User user, @PathVariable String cardNo, @PathVariable Integer amount) throws Exception
	{
		if (Objects.isNull(user.getPackageExpireDate()))
			user.setPackageExpireDate(LocalDateTime.now());

		return new ResponseEntity<>(userService.processPayment(user, cardNo, amount), HttpStatus.ACCEPTED);

	}

	@PostMapping("/login")
	public ResponseEntity<UserRequest> login (@RequestBody UserRequest user) throws Exception
	{
		if (Objects.nonNull(user)) {
			userService.login(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}

		return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);

	}



}
