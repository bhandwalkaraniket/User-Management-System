package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ErrorResponse;
import com.app.dto.ResponseDTO;
import com.app.pojos.User;
import com.app.service.IUserService;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserRestController {
	// dependency : Service Layer i/f
	@Autowired
	private IUserService userService;

	public UserRestController() {
		System.out.println("in ctor of " + getClass().getName());
	}

	// add REST API Endpoint : for getting all Users
	@GetMapping
	public List<User> fetchAllUsers() {
		System.out.println("in fetch all users");
		return userService.getAllUsers();

	}

	// add REST API Endpoint : for adding new user (Creating new User)
	/*
	 * @PostMapping public User addNewUserDetails(@RequestBody User tranientUser) {
	 * // what goes in is transient pojo and what comes out is detached pojo
	 * System.out.println("in add user details " + tranientUser); // invoke's
	 * service layer method for saving user details return
	 * userService.addUser(tranientUser); }
	 */

	// add REST API Endpoint : for adding new user (Creating new User) that retn to
	// the front HTTP status code + resp body
	// API : o.s.http.ResponseEntity

	/*
	 * public ResponseEntity(@Nullable T body, HttpStatus status) Create a new
	 * ResponseEntity with the given body and status code, and no headers.
	 */
	@PostMapping
	public ResponseEntity<?> addNewUserDetails(@RequestBody User tranientUser) {
		// what goes in is transient pojo and what comes out is detached pojo
		System.out.println("in add user details " + tranientUser);
		// invoke's service layer method for saving user details
		try {
			return new ResponseEntity<>(userService.addUser(tranientUser), HttpStatus.CREATED);
		} catch (RuntimeException e) {
			System.out.println("error in add " + e);
			return new ResponseEntity<>(new ErrorResponse("Adding User Failed!", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// add REST API to delete user details by Id
	@DeleteMapping("/{userId}") // URI template variable-->how to inject this uri into method argument is :
								// PathVariable
	public ResponseEntity<ResponseDTO> deleteUserDetails(@PathVariable int userId) {
		System.out.println("in delete user details " + userId);
		// invoke's service layer method for deleting user details
		// return new ResponseEntity<>(new ResponseDTO(userService.deleteUser(userId)),
		// HttpStatus.OK);
		return ResponseEntity.ok(new ResponseDTO(userService.deleteUser(userId)));
	}

	// add REST API to get user details by id.
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserDetails(@PathVariable int id) {
		System.out.println("in get user details " + id);
		// invoke service layer method to get user details
		try {
			return ResponseEntity.ok(userService.getDetails(id));

		} catch (RuntimeException e) {
			System.out.println("error in get " + e);
			return new ResponseEntity<>(new ErrorResponse("Fetching User Details Failed!", e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	// add REST API Endpoint to update existing user details.
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUserDetails(@RequestBody User detachedUser, @PathVariable int id) {
		System.out.println("in update user details " + detachedUser + " " + id);
		try {
			// invoke's service layer method for validating user id
			User existingUser = userService.getDetails(id);
			// => user is valid, invoke setters to update the state
			// existingUser=>user details fetch from DB (stale)
			// detachedUser =>updated user details coming from front-end.
			return ResponseEntity.ok(userService.updateDetails(detachedUser));
		} catch (RuntimeException e) {
			System.out.println("error in update " + e);
			return new ResponseEntity<>(new ErrorResponse("Updating User Details Failed!", e.getMessage()),
					HttpStatus.BAD_REQUEST);

		}

	}

}
