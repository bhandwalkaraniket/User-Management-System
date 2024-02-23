package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.custom_exception.UserHandlingException;
import com.app.dao.UserRepository;
import com.app.pojos.User;

@Service
@Transactional
public class UserServiceImpl implements IUserService {
	// dependency : DAO layer i/f

	@Autowired
	private UserRepository userRepo;

	@Override
	public List<User> getAllUsers() {
		// invokes dao's method
		// findAll() mtd from spring data jpa method by SimpleJpaRepository Class.
		return userRepo.findAll();

	}

	@Override
	public User addUser(User user) {

		return userRepo.save(user);
	}

	@Override
	public String deleteUser(int userId) {
		userRepo.deleteById(userId);
		return "User details Deleted for Id = " + userId;
	}

	@Override
	public User getDetails(int userId) {
		/*
		 * Method of CrudRepository : Optional<T> findById(ID id)--> Returns Optional
		 * with entity in case of id found or returns empty Optional.
		 */
		return userRepo.findById(userId).orElseThrow(() -> new UserHandlingException("Invalid user Id!"));
	}

	@Override
	public User updateDetails(User detachedUser) {
		return userRepo.save(detachedUser);
	}

	/*
	 * When the method annotated with @Transactional rets: JPA Provider (currently
	 * hibernate will end the the tx It will auto commit the Tx : in case of no
	 * RuntimeException or in case of exc : auto rollback occurs.
	 */

}
