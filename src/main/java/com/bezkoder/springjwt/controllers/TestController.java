package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Message;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.MessageRepository;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	MessageRepository messageRepository;
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	@GetMapping("/getAllUser")
	public List<User> getAllUser(){
		return  this.userRepository.findAll();

	}
	@GetMapping("/getUserById/{id}")
	public User getUserById(@PathVariable(name = "id") long id){
		return this.userRepository.findById(id).get();
	}
	@PostMapping("sendMessage/{id}")
	public void sendMessage(@PathVariable(name = "id") long id,@RequestBody Message message){
		User user = this.userRepository.findById(id).get();
		message.setUser(user);
		this.messageRepository.save(message);

		user.getMessages().add(message);
		this.userRepository.save(user);


	}
	@GetMapping("/getMessagesByUserId/{id}")
	public List<Message> getMessagesByUserId(@PathVariable(name = "id") long id){
		return this.userRepository.findById(id).get().getMessages();
	}
	@GetMapping("/getUserByUserName/{username}")
	public User getUserByUserName(@PathVariable(name = "username") String username){
		return this.userRepository.findByUsername(username).get();
	}
	@DeleteMapping("/deleteUser/{id}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
		try {
			userRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




}
