package controllers;

import db.DBHelper;
import models.Department;
import models.Manager;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class ManagersController {

    public ManagersController() {
        this.setupEndpoints();
    }

    private void setupEndpoints(){

        get("/managers", (req, res) -> { // get verb, set to /managers route with request & response
            Map<String, Object> model = new HashMap(); // creating new hashmap with string and object key/value
            model.put("template", "templates/managers/index.vtl"); // put

            List<Manager> managers = DBHelper.getAll(Manager.class);
            model.put("managers", managers);

            return new ModelAndView(model,"templates/layout.vtl");


        }, new VelocityTemplateEngine());




        get("/managers/new", (req, res) -> {
            HashMap<String, Object> model = new HashMap();

            List<Department> departments = DBHelper.getAll(Department.class);
            model.put("departments", departments);

            model.put("template", "templates/managers/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");

        }, new VelocityTemplateEngine());




        post("/managers", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String firstName = req.queryParams("first-name");
            String lastName = req.queryParams("last-name");
            int salary = Integer.parseInt(req.queryParams("salary"));
            double budget = Double.parseDouble(req.queryParams("budget"));
            int departmentId = Integer.parseInt(req.queryParams("department"));
            Department department = DBHelper.find(departmentId, Department.class);

            Manager manager = new Manager(firstName, lastName, salary, department, budget);
            DBHelper.save(manager);

            res.redirect("/managers");
            return null;

        }, new VelocityTemplateEngine());

    }

}
