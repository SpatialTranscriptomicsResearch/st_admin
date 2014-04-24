package com.spatialtranscriptomics.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.spatialtranscriptomics.model.ImageAlignment;
import com.spatialtranscriptomics.model.Chip;
import com.spatialtranscriptomics.model.ImageMetadata;
import com.spatialtranscriptomics.serviceImpl.ChipServiceImpl;
import com.spatialtranscriptomics.serviceImpl.ImageAlignmentServiceImpl;
import com.spatialtranscriptomics.serviceImpl.ImageServiceImpl;

/**
 * This class is Spring MVC controller class for the URL "/imagealignment". It implements the methods available at this URL and returns views (.jsp pages) with models .
 */

@Controller
@RequestMapping("/imagealignment")
public class ImageAlignmentController {

	@Autowired
	ImageAlignmentServiceImpl imagealignmentService;
	
	@Autowired
	ChipServiceImpl chipService;
	
	@Autowired
	ImageServiceImpl imageService;

	
	// get
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable String id) {
		ImageAlignment imal = imagealignmentService.find(id);
		Chip chip = chipService.find(imal.getChip_id());
		ModelAndView success = new ModelAndView("imagealignmentshow", "imagealignment", imal);
		success.addObject("chip", chip);
		return success;
	}

		
	// list
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView success;
		success = new ModelAndView("imagealignmentlist", "imagealignmentList", imagealignmentService.list());
		success.addObject("chips", populateChipChoices());
		return success;
	}

	
	// add
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView add() {
		return new ModelAndView("imagealignmentadd", "imagealignment", new ImageAlignment());
	}

	
	// add submit
	@RequestMapping(value = "/submitadd", method = RequestMethod.POST)
	public ModelAndView submitAdd(@ModelAttribute("imagealignment") @Valid ImageAlignment imal, BindingResult result) {
		if (result.hasErrors()) {
			ModelAndView model = new ModelAndView("imagealignmentadd", "imagealignment", imal);
			model.addObject("errors", result.getAllErrors());
			return model;
		}
		imagealignmentService.create(imal);
		ModelAndView success = new ModelAndView("imagealignmentlist", "imagealignmentList", imagealignmentService.list());
		success.addObject("msg", "ImageAlignment created.");
		return success;

	}

	
	// edit
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable String id) {
		return new ModelAndView("imagealignmentedit", "imagealignment", imagealignmentService.find(id));
	}

	
	// edit submit
	@RequestMapping(value = "/submitedit", method = RequestMethod.POST)
	public ModelAndView submitEdit(
			@ModelAttribute("imagealignment") @Valid ImageAlignment imal, BindingResult result) {
		if (result.hasErrors()) {
			ModelAndView model = new ModelAndView("imagealignmentedit", "imagealignment", imal);
			model.addObject("errors", result.getAllErrors());
			return model;
		}
		imagealignmentService.update(imal);
		ModelAndView success = new ModelAndView("imagealignmentlist", "imagealignmentList", imagealignmentService.list());
		success.addObject("msg", "ImageAlignment saved.");
		return success;
	}

	
	// delete
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable String id) {
		imagealignmentService.delete(id);
		ModelAndView success = new ModelAndView("imagealignmentlist", "imagealignmentList", imagealignmentService.list());
		success.addObject("msg", "ImageAlignment deleted.");
		return success;
	}
	
	
	// populate chip choice fields for form
	@ModelAttribute("chipChoices")
	public Map<String, String> populateChipChoices() {
		Map<String, String> choices = new LinkedHashMap<String, String>();
		List<Chip> l = chipService.list();
		for (Chip t : l) {
			choices.put(t.getId(), t.getName());
		}
		return choices;
	}
	
	
	// populate image choice fields for form
	@ModelAttribute("imageChoices")
	public Map<String, String> populateImageChoices() {
		Map<String, String> choices = new LinkedHashMap<String, String>();
		List<ImageMetadata> l = imageService.list();
		for (ImageMetadata t : l) {
			choices.put(t.getFilename(), t.getFilename());
		}
		return choices;
	}
	
}