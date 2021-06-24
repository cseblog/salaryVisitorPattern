package com.finxflo.salary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.util.stream.Collectors;

@SpringBootApplication
public class SalaryApplication {

	public static void main(String[] args) {
		List<SalaryApplication.Employee> employees = new ArrayList<>();
		Engineer engineer = new Engineer(10_000, 0);
		Saler saler = new Saler(0, 0.7);
		Manager manager = new Manager(7_000, 0.3);
		employees.add(engineer);
		employees.add(saler);
		employees.add(manager);
		BossVisitor boss = new BossVisitor();
		double sum = employees.stream().mapToDouble(employee -> employee.getSalary(boss)).sum();
		System.out.println("Total salary: " + sum);

		Auditor auditor = new Auditor();
		employees.forEach(employee -> employee.print(auditor));

	}

	interface Visitor {
		public double getSalary(Engineer engineer);
		public double getSalary(Saler saler);
		public double getSalary(Manager manager);
	}
	interface MonitorVistior {
		public void print(Employee employee);
	}

	static class Auditor implements MonitorVistior {

		@Override
		public void print(Employee employee) {
			System.out.println(new Gson().toJson(employee));
		}
	}

	static class BossVisitor implements Visitor {

		@Override
		public double getSalary(Engineer engineer) {
			return  engineer.salary;
		}

		@Override
		public double getSalary(Saler saler) {
			return saler.fixRate * saler.vol;
		}

		@Override
		public double getSalary(Manager manager) {
			return manager.salary + manager.fixRate * manager.vol;
		}
	}

	interface Employee {
		double getSalary(Visitor visitor);
		void print(Auditor visitor);
	}

	static class Engineer implements Employee {
		double salary;
		double fixRate;

		public Engineer(double salary, double fixRate) {
			this.salary = salary;
			this.salary = fixRate;
		}

		@Override
		public double getSalary(Visitor visitor) {
			return visitor.getSalary(this);
		}

		@Override
		public void print(Auditor visitor) {
			visitor.print(this);
		}
	}

	static class Saler implements Employee {
		final double vol = 10_000; //
		double salary;
		double fixRate;

		public Saler(double salary, double fixRate) {
			this.salary = salary;
			this.fixRate = fixRate;
		}

		@Override
		public double getSalary(Visitor visitor) {
			return visitor.getSalary(this);
		}

		@Override
		public void print(Auditor visitor) {
			visitor.print(this);
		}
	}

	static class Manager implements Employee {
		double salary;
		double fixRate;
		final double vol = 100_000; //Manager has volume larger than saler
		public Manager(double salary, double fixRate) {
			this.salary = salary;
			this.fixRate = fixRate;
		}

		@Override
		public double getSalary(Visitor visitor) {
			return visitor.getSalary(this);
		}

		@Override
		public void print(Auditor visitor) {
			visitor.print(this);
		}
	}


}
