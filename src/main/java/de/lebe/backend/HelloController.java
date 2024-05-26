package de.lebe.backend;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.graph.models.Authentication;

@RestController
public class HelloController {

	@GetMapping
	public String index(Model model, Authentication user) {
		model.addAttribute("user", user);
		return "index";
	}
}