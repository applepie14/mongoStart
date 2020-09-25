package com.pro.mongo.web.view;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ViewController {


	@GetMapping("/")
	public String view() {
		return "index";
	}

	@GetMapping("/{path}")
	public String home(@PathVariable String path) {
		return path;
	}

	@GetMapping("/member/reg")
	public String reg() {
		return "/member/reg";
	}

	@GetMapping("/login")
	public String login(Principal principal) {
		if (principal != null) {
			return "redirect:/";
		} else {
			return "login";
		}
	}
}
