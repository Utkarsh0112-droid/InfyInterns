package com.infy.infyinterns.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.entity.Project;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.repository.ProjectRepository;

import jakarta.transaction.Transactional;

@Service(value = "projectService")
@Transactional
public class ProjectAllocationServiceImpl implements ProjectAllocationService {

	@Autowired
	MentorRepository mentorRepo;
	
	@Autowired
	ProjectRepository projectRepo;

	@Override
	public Integer allocateProject(ProjectDTO project) throws InfyInternException {
		Optional<Mentor> op = mentorRepo.findById(project.getMentorDTO().getMentorId());
		if(op.isEmpty()) {
			throw new InfyInternException("Service.MENTOR_NOT_FOUND");
		}
		Mentor mentor = op.get();
		if(mentor.getNumberOfProjectsMentored()>=3) {
			throw new InfyInternException("Service.CANNOT_ALOCATE_PROJECT");
		}
		Project newproject = new Project();
		newproject.setMentor(mentor);
		newproject.setIdeaOwner(project.getIdeaOwner());
		newproject.setProjectId(project.getProjectId());
		newproject.setProjectName(project.getProjectName());
		newproject.setReleaseDate(project.getReleaseDate());
		
		// save project
		
		projectRepo.save(newproject);
		
		// Increment number or project for that mentor by 1
		mentor.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored()+1);
		// mentor save
		mentorRepo.save(mentor);
		// return project id
		
		
		return newproject.getProjectId();
	}

	@Override
	public List<MentorDTO> getMentors(Integer numberOfProjectsMentored) throws InfyInternException {
		List<Mentor> mentorlist = mentorRepo.findAllByNumberOfProjectsMentored(numberOfProjectsMentored);
		if(mentorlist.isEmpty()) {
			throw new InfyInternException("Service.MENTOR_NOT_FOUND");
		}
		List<MentorDTO> mentorDTO = new ArrayList<>();
		for(Mentor mentor : mentorlist) {
			
		}
		
		
		return null;
	}

	@Override
	public void updateProjectMentor(Integer projectId, Integer mentorId) throws InfyInternException {

	}

	@Override
	public void deleteProject(Integer projectId) throws InfyInternException {

	}
	
	private MentorDTO createDTO(Mentor mentor) {
		MentorDTO mentorDTO = new MentorDTO();
		
		mentorDTO.setMentorId(mentor.getMentorId());
		mentorDTO.setMentorName(mentor.getMentorName());
		mentorDTO.setNumberOfProjectsMentored(mentor.getNumberOfProjectsMentored());
		return mentorDTO;
	}
}