package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Curricula;
import domain.Hacker;
import domain.PersonalData;

import services.CurriculaService;
import services.HackerService;
import services.PersonalDataService;

@Controller
@RequestMapping("/personalData")
public class PersonalDataController extends AbstractController {

	@Autowired
	private PersonalDataService			personalDataService;
	
	@Autowired
	private HackerService				hackerService;
	
	@Autowired
	private CurriculaService     curriculaService;
	
	@Autowired
	private CurriculaController    curriculaController;



	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final PersonalData personalData = this.personalDataService.create();
		result = this.createEditModelAndView(personalData);
		return result;
	}

	//Updating
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int personalDataId) {
		ModelAndView result;
		try {
			final PersonalData personalData;
			final Hacker hacker = this.hackerService.findByPrincipal();
			personalData = this.personalDataService.findOne(personalDataId);
			final Curricula curricula = this.curriculaService.findCurriculaByHacker(hacker.getId());
			Assert.isTrue(curricula.getPersonalRecord().equals(personalData), "This personal data is not of your property");
			result = this.createEditModelAndView(personalData);
		} catch (final Exception e) {
			result = new ModelAndView("administrator/error");
			result.addObject("trace", e.getMessage());
		}

		return result;
	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final PersonalData personalData, final BindingResult bindingResult) {
		ModelAndView result;
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(personalData);
		else
			try {
				this.personalDataService.save(personalData);
				result = this.curriculaController.list();
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(personalData, "general.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int personalDataId) {

		ModelAndView res;

		final PersonalData personalData = this.personalDataService.findOne(personalDataId);

		if (personalData != null) {

			res = new ModelAndView("personalData/display");
			res.addObject("personalData", personalData);
			res.addObject("buttons", false);

		} else
			res = new ModelAndView("redirect:/misc/403.jsp");

		return res;

	}

	protected ModelAndView createEditModelAndView(final PersonalData personalData) {
		ModelAndView result;

		result = this.createEditModelAndView(personalData, null);

		return result;
	}
	// Edition ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final PersonalData personalData, final String message) {
		ModelAndView result;

		result = new ModelAndView("personalData/edit");
		result.addObject("personalData", personalData);
		result.addObject("message", message);

		return result;

	}
}
