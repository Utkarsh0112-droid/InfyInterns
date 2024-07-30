package com.infy.infyinterns.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.service.ProjectAllocationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/infyinterns")
public class ProjectAllocationAPI
{
	@GetMapping(path="hello")
	public String Hello() {
		return "Hello World";
	}
	@Autowired
	Environment environment;
	
	@Autowired
	ProjectAllocationService projectAllocationService;
    
	// add new project along with mentor details
	@PostMapping("/project")
    public ResponseEntity<String> allocateProject(@RequestBody @Valid ProjectDTO project) throws InfyInternException
    {
		Integer projectId  = projectAllocationService.allocateProject(project);
		String successMessage = environment.getProperty("API.ALLOCATION_SUCCESS")+projectId;
		
	return new ResponseEntity<>(successMessage,HttpStatus.CREATED);
    }

    // get mentors based on idea owner
	@GetMapping("/mentors/{numberOfProjectsMentored}")
    public ResponseEntity<List<MentorDTO>> getMentors(@PathVariable Integer numberOfProjectsMentored) throws InfyInternException
    {
		List<MentorDTO> mentors = projectAllocationService.getMentors(numberOfProjectsMentored);
		
	return new ResponseEntity<>(mentors,HttpStatus.OK);
    }

    // update the mentor of a project
	@PutMapping("project/{ projectId }/{ mentorId }")
    public ResponseEntity<String> updateProjectMentor( @PathVariable Integer projectId,
						     @PathVariable @Valid Integer mentorId) throws InfyInternException
    {
    	projectAllocationService.updateProjectMentor(projectId, mentorId);
    	String successMessage = environment.getProperty(" API.PROJECT_UPDATE_SUCCESS ");
    	
	return new ResponseEntity<>(successMessage,HttpStatus.OK) ;
    }

    // delete a project
	@DeleteMapping("project/{ projectId }")
    public ResponseEntity<String> deleteProject(Integer projectId) throws InfyInternException
    {
		projectAllocationService.deleteProject(projectId);
		String successMessage = environment.getProperty("API.PROJECT_DELETE _SUCCESS");
	return new ResponseEntity<>(successMessage,HttpStatus.OK);
    }

}
