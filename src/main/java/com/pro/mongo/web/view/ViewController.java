package com.pro.mongo.web.view;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pro.mongo.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ViewController {
	private final BoardService testServ;

	@GetMapping("/")
	public String view() {
		return "index";
	}

	@GetMapping("/{path}")
	public String home(@PathVariable String path) {
		return path;
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
