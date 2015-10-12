package com.stop.task;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import model.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@CrossOrigin
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	private LinkedHashMap<Integer, Task> map = new LinkedHashMap<Integer, Task>();
	
	

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@RequestMapping(value = "/allTasks", method = RequestMethod.GET)
	public @ResponseBody List<Task> getAll() {
		if(map.isEmpty()){
			Task task =new Task(1,"debugging","active");
			map.put(1,task);
		}
		
		return new ArrayList<Task>(map.values());
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public @ResponseBody Boolean insert(@RequestBody Task task) {
		if (task.getTaskStatus().equals("active") || task.getTaskStatus().equals("completed")) {
			if(map.containsKey(task.getTaskId())){
				logger.info("first");
				return false;
			}
			map.put(task.getTaskId(), task);
			return true;
		} else{
			logger.info("second");
			return false;
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Boolean update(@RequestBody Task task) {
		if (task.getTaskStatus().equals("active") || task.getTaskStatus().equals("completed")) {
			if (map.containsKey(task.getTaskId())) {
				map.put(task.getTaskId(), task);
				return true;
			} else
				return false;
		}else
			return false;
	}

	@RequestMapping(value = "/delete/{taskId}", method = RequestMethod.POST)
	public @ResponseBody Boolean delete(@PathVariable("taskId") int taskId) {
		if(map.containsKey(taskId)){
			Task task= map.get(taskId);
			task.setTaskStatus("deleted");
			return true;
		}else
		return false;
	}
}
