package de.lebe.backend.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import de.lebe.backend.domain.MCustomerRfp;
import de.lebe.backend.domain.RepositoryCustomerRfp;

@RestController
public class AdvisorRfpOverview {

	@Autowired
	private RepositoryCustomerRfp rpfRepository;

	@GetMapping("/advisor/rfp")
	public ModelAndView getCustomerRfps(Model model) {
		List<MCustomerRfp> matches = new ArrayList<MCustomerRfp>();
		rpfRepository.findAll().forEach(matches::add);
		
		return new ModelAndView("rfp", "rfps", matches);
	}

	@GetMapping("/advisor/details/{id}")
	public String showDetailsForm(@PathVariable("id") String id, Model model) {
		var rfp = rpfRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rfp Id:" + id));

		model.addAttribute("user", rfp);
		return "details-rfp";
	}
}
