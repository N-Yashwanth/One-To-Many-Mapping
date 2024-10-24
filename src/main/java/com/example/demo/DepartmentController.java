package com.example.demo;

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
@RequestMapping("/api/department")
public class DepartmentController {
	@Autowired
	private DepartmentRepository deptRepo;
	@GetMapping("/all")
	public List<Department> getAllDepartments(){
		List<Department> departments=deptRepo.findAll();
		System.out.println("Departments: "+departments.size());
		return departments;
	}
	@GetMapping("/{id}")
	public Department getDepartmentByID(@PathVariable int id) {
		return deptRepo.findById(id).orElse(null);
	}
	@PostMapping
	public Department saveAll(@RequestBody Department dept) {
		Department department=new Department();
		department.setdName(dept.getdName());
		List<Employee> empList=new ArrayList<Employee>();
		for(Employee emp:dept.getEmployees()) {
			Employee emps=new Employee();
			emps.setDepartment(department);
			emps.seteName(emp.geteName());
			empList.add(emps);
		}
		department.setEmployees(empList);
		return deptRepo.save(department);
	}
	@PutMapping("/{id}")
	public Department updateDepartment(@PathVariable int id, @RequestBody Department dept) {
		Department department=deptRepo.findById(id).orElseThrow();
		department.setdName(dept.getdName());
		department.setEmployees(dept.getEmployees());
		return deptRepo.save(department);
	}
	@DeleteMapping("/{id}")
	public void deleteDepartment(@PathVariable int id) {
		deptRepo.deleteById(id);
	}
	
	@GetMapping("/page/{pageNo}/{pageSize}")
	public List<Department> getPaginated(@PathVariable int pageNo, @PathVariable int pageSize){
		Pageable pageable=PageRequest.of(pageNo, pageSize);
		Page<Department> result=deptRepo.findAll(pageable);
		return result.hasContent()?result.getContent():new ArrayList<Department>();
	}
	@GetMapping("/sort")
	public List<Department> getSorted(@RequestParam String field, @RequestParam String direction){
		Direction sortDirection=direction.equalsIgnoreCase("desc")?Direction.DESC:Direction.ASC;
		return deptRepo.findAll(Sort.by(sortDirection, field));
	}
	@GetMapping("/page/{pageNo}/{pageSize}/sort")
	public List<Department> getPaginatedandSorted(@PathVariable int pageNo, @PathVariable int pageSize, @RequestParam String field, @RequestParam String direction){
		Sort sort=direction.equalsIgnoreCase(Direction.ASC.name())?Sort.by(field).ascending():Sort.by(field).descending();
		Pageable pageable=PageRequest.of(pageNo, pageSize);
		Page<Department> result=deptRepo.findAll(pageable);
		return result.hasContent()?result.getContent():new ArrayList<Department>();
	}
}
