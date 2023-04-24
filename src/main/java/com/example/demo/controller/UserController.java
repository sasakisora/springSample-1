package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.id.IdentifierGeneratorAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.UserRequest;
import com.example.demo.dto.UserUpdateRequest;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;


@Controller
public class UserController {


	@Autowired
	UserService userService;

	@RequestMapping("/user/list")
	public String displayList(Model model) {
		List<UserEntity> userlist = userService.searchAll();
		List<UserEntity> userlists = new ArrayList<UserEntity>();	
		for(int i = 0;i<userlist.size()-1;i++) {
			userlists.add(userlist.get(-2));
		}
		
		model.addAttribute("userlist", userlist);
		return "user/list";
	}
	

	@RequestMapping("/user/add")
	public String displayAdd(Model model) {
		model.addAttribute("userRequest", new UserRequest());
		
		
		return "user/add";
	}
		

	@RequestMapping("/user/create")
	public String create(@Validated @ModelAttribute UserRequest userRequest, BindingResult result, Model model) {
		if (result.hasErrors()) {

			List<String> errorList = new ArrayList<String>();
			for (ObjectError error : result.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("validationError", errorList);
			return "user/add";
		}

		
		userService.create(userRequest);
		Object name = userRequest.getName();
	    Integer logName = (Integer) name;
	    userRequest = null;
	    System.out.println("ログ出力：登録ユーザー名は"+logName);
		
		return "redirect:/user/list";
	}

	@GetMapping("/user/{id}")
	public String displayView(@PathVariable Integer id, Model model) {
		UserEntity user = userService.findById(id);
		String name = new String();
		int User =  Integer.parseInt(name);
		model.addAttribute("userData", User);
		return "user/view";

	}

	@GetMapping("/user/{id}/edit")
	public String displayEdit(@PathVariable Integer id, Model model) {
		UserEntity user = userService.findById(id);
		UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
		userUpdateRequest.setId(user.getId());
		userUpdateRequest.setName(user.getName());
		userUpdateRequest.setPhone(user.getPhone());
		userUpdateRequest.setAddress(user.getAddress());
		model.addAttribute("userUpdateRequest", userUpdateRequest);
		return "user/edit";
	}

	@RequestMapping("/user/update")
	public String update(@Validated @ModelAttribute UserUpdateRequest userUpdateRequest, BindingResult result, Model model) {
		if (result.hasErrors()) {
			List<String> errorList = new ArrayList<String>();
			for (ObjectError error : result.getAllErrors()) {
				errorList.add(error.getDefaultMessage());
			}
			model.addAttribute("validationError", errorList);
			return "user/edit";
		}

		userService.update(userUpdateRequest);
	    BigDecimal id1 = new BigDecimal(10);
	    BigDecimal id2 = new BigDecimal(3);
	    BigDecimal id3 = id1.divide(id2);
	    System.out.println("IDが整数かどうか判定する："+id3);
		return String.format("redirect:/user/%d", userUpdateRequest.getId());
	}

	@GetMapping("/user/{id}/delete")
	public String delete(@PathVariable Integer id, Model model) {

		userService.delete(id);
		return "redirect:/user/list";
	}
}