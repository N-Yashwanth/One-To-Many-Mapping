package com.example.demo;

//import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
	@Autowired
	private EmployeeRepository empRepo;
	
	@GetMapping("/all")
	public List<Employee> getEmployee(){
		return empRepo.findAll();
	}
	@GetMapping("/{id}")
	public Employee getEmployeeByID(@PathVariable int id) {
		return empRepo.findById(id).orElse(null);
	}
	@PostMapping
	public Employee saveAll(@RequestBody Employee emp) {
		return empRepo.save(emp);
	}
	@PutMapping("/{id}")
	public Employee updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
		Employee emp=empRepo.findById(id).orElseThrow();
		emp.seteName(employee.geteName());
		emp.setDepartment(employee.getDepartment());
		return empRepo.save(emp);
	}
	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable int id) {
		empRepo.deleteById(id);
	}
	
	@GetMapping("/page/{pageNo}/{pageSize}")
	public List<Employee> getPaginated(@PathVariable int pageNo, @PathVariable int pageSize){
		Pageable pageable=PageRequest.of(pageNo, pageSize);
		Page<Employee> result=empRepo.findAll(pageable);
		return result.hasContent()?result.getContent():new ArrayList<Employee>();
	}
	@GetMapping("/sort")
	public List<Employee> sortEmployee(@RequestParam String field, @RequestParam String direction){
		Direction sortDirection=direction.equalsIgnoreCase("desc")?Direction.DESC:Direction.ASC;
		return empRepo.findAll((Sort.by(sortDirection,field)));
	}
	@GetMapping("/page/{pageNo}/{pageSize}/sort")
	public List<Employee> getPaginationandSorting(@PathVariable int pageNo, @PathVariable int pageSize, @RequestParam String field, @RequestParam String direction){
		Sort sort=direction.equalsIgnoreCase(Direction.ASC.name())?Sort.by(field).ascending():Sort.by(field).descending();
		Pageable pageable=PageRequest.of(pageNo, pageSize);
		Page<Employee> result=empRepo.findAll(pageable);
		return result.hasContent()?result.getContent():new ArrayList<Employee>();
	}
}
