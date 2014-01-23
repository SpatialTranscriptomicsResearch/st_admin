/*
*Copyright © 2012 Spatial Transcriptomics AB
*Read LICENSE for more information about licensing terms
*Contact: Jose Fernandez Navarro <jose.fernandez.navarro@scilifelab.se>
* 
*/

package com.spatialtranscriptomics.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.elasticmapreduce.model.JobFlowDetail;
import com.spatialtranscriptomics.exceptions.GenericException;
import com.spatialtranscriptomics.exceptions.GenericExceptionResponse;
import com.spatialtranscriptomics.model.Experiment;
import com.spatialtranscriptomics.model.ExperimentForm;
import com.spatialtranscriptomics.serviceImpl.EMRServiceImpl;
import com.spatialtranscriptomics.serviceImpl.ExperimentServiceImpl;
import com.spatialtranscriptomics.serviceImpl.S3ServiceImpl;

/**
 * This class is Spring MVC controller class for the URL "/experiment". It implements the methods available at this URL and returns views (.jsp pages) with models.
 */

@Controller
@RequestMapping("/experiment")
public class ExperimentController {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(ExperimentController.class);

	@Autowired
	ExperimentServiceImpl experimentService;

	@Autowired
	EMRServiceImpl emrService;

	@Autowired
	S3ServiceImpl s3Service;

	// get
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable String id) {

		Experiment exp = experimentService.find(id);
		ModelAndView success = new ModelAndView("experimentshow", "experiment",
				exp);

		JobFlowDetail jobFlow = emrService.findJobFlow(exp.getEmr_Jobflow_id());
		success.addObject("jobflow", jobFlow);

		return success;
	}

	// list
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView list() {

		return new ModelAndView("experimentlist", "experimentList",
				experimentService.list());
	}

	// create
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		return new ModelAndView("experimentcreate", "experimentform",
				new ExperimentForm());
	}

	// create submit
	@RequestMapping(value = "/submitcreate", method = RequestMethod.POST)
	public ModelAndView submitCreate(
			@ModelAttribute("experimentform") @Valid ExperimentForm form,
			BindingResult result) {

		if (result.hasErrors()) {
			ModelAndView model = new ModelAndView("experimentcreate",
					"experimentform", form);
			model.addObject("errors", result.getAllErrors());
			return model;
		}

		// create experiment
		Experiment experiment = new Experiment();
		experiment.setName(form.getExperimentName());
		experiment.setCreated(new Date());
		experiment = experimentService.add(experiment);

		// create EMR jobflow
		String emrJobFlowId = emrService.startJobFlow(form, experiment.getId());

		// Delete experiment and return error if EMR jobflow could not be
		// started
		if (emrJobFlowId == null) {
			ModelAndView awsFail = new ModelAndView("experimentcreate",
					"experimentform", form);
			experimentService.delete(experiment.getId());
			awsFail.addObject("errors", "Could not start EMR Job. Try again.");
			return awsFail;
		}

		// update experiment with Jobflow ID
		experiment.setEmr_Jobflow_id(emrJobFlowId);
		experimentService.update(experiment);

		ModelAndView success = new ModelAndView("experimentlist",
				"experimentList", experimentService.list());
		success.addObject("msg", "Experiment started.");

		return success;

	}

	// stop and delete
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable String id) {

		Experiment exp = experimentService.find(id);

		if (exp.getEmr_Jobflow_id() != null) {
			emrService.stopJobFlow(exp.getEmr_Jobflow_id());
		}

		s3Service.deleteExperimentData(id);
		experimentService.delete(id);

		ModelAndView success = new ModelAndView("experimentlist",
				"experimentList", experimentService.list());
		success.addObject("msg", "Experiment deleted.");
		return success;
	}

	// download output
	@RequestMapping(value = "/{id}/output", method = RequestMethod.GET, produces = "text/json")
	public @ResponseBody
	HttpEntity<byte[]> getOutput(
			@RequestParam(value = "format", required = true) String format,
			@PathVariable String id) {
		try {
			HttpHeaders header = new HttpHeaders();
			byte[] wb;

			if (format.equals("json")) {

				wb = IOUtils.toByteArray(s3Service.getFeaturesAsJson(id));
				header.setContentType(new MediaType("text", "json"));
				header.set("Content-Disposition",
						"attachment; filename=output_" + id + ".json");
			} else {

				wb = IOUtils.toByteArray(s3Service.getFeaturesAsCSV(id));
				header.setContentType(new MediaType("text", "csv"));
				header.set("Content-Disposition",
						"attachment; filename=output_" + id + ".csv");
			}

			header.setContentLength(wb.length);

			return new HttpEntity<byte[]>(wb, header);

		} catch (Exception e) {
			GenericExceptionResponse resp = new GenericExceptionResponse();
			resp.setError("Parse error");
			resp.setError_description("Could not parse experiment output. Does the output exist?");
			throw new GenericException(resp);
		}
//		return null;
	}

	// Populate Choice fields here

	@ModelAttribute("numNodesChoices")
	public Map<String, String> populateNumNodesChoices() {
		Map<String, String> choices = new LinkedHashMap<String, String>();

		for (int i = 1; i <= 30; i++) {
			choices.put(String.valueOf(i), String.valueOf(i));
		}

		return choices;
	}

	@ModelAttribute("nodeTypeChoices")
	public Map<String, String> populateNodeTypeChoices() {
		Map<String, String> choices = new LinkedHashMap<String, String>();

		InstanceType[] instanceTypes = com.amazonaws.services.ec2.model.InstanceType
				.values();

		for (InstanceType t : instanceTypes) {
			choices.put(t.toString(), t.name());
		}

		return choices;
	}

	@ModelAttribute("folderChoices")
	public Map<String, String> populateFolderChoices() {
		Map<String, String> choices = new LinkedHashMap<String, String>();

		List<String> l = s3Service.getInputFolders();

		for (String t : l) {
			choices.put(t, t);
		}

		return choices;
	}

	@ModelAttribute("idFileChoices")
	public Map<String, String> populateIdFileChoices() {
		Map<String, String> choices = new LinkedHashMap<String, String>();

		List<String> l = s3Service.getIDFiles();

		for (String t : l) {
			choices.put(t, t);
		}

		return choices;
	}

	@ModelAttribute("refAnnotationChoices")
	public Map<String, String> populateRefAnnotationChoices() {
		Map<String, String> choices = new LinkedHashMap<String, String>();

		List<String> l = s3Service.getReferenceAnnotation();

		for (String t : l) {
			choices.put(t, t);
		}

		return choices;
	}

	@ModelAttribute("refGenomeChoices")
	public Map<String, String> populateRefGenomeChoices() {
		Map<String, String> choices = new LinkedHashMap<String, String>();

		List<String> l = s3Service.getReferenceGenome();

		for (String t : l) {
			choices.put(t, t);
		}

		return choices;
	}

	@ModelAttribute("bowtieFileChoices")
	public Map<String, String> populateBowtieFileChoices() {
		Map<String, String> choices = new LinkedHashMap<String, String>();

		List<String> l = s3Service.getBowtieFiles();

		for (String t : l) {
			choices.put(t, t);
		}

		return choices;
	}

	@ModelAttribute("htseqAnnotationChoices")
	public Map<String, String> populateHtseqAnnotationChoices() {
		Map<String, String> choices = new LinkedHashMap<String, String>();

		choices.put("union", "union"); 
		//choices.put("intersection-nonempty", "intersection-nonempty"); //hardcoded in jsp view, for pre-selection
		choices.put("intersection-strict", "intersection-strict");

		return choices;
	}

}
