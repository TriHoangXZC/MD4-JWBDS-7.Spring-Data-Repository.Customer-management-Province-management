package com.codegym.controller;

import com.codegym.model.Customer;
import com.codegym.model.Province;
import com.codegym.service.customer.ICustomerService;
import com.codegym.service.province.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IProvinceService provinceService;

    @GetMapping("/customers/list")
    public ModelAndView showListCustomer() {
        ModelAndView modelAndView = new ModelAndView("/customer/list");
        Iterable<Customer> customers = customerService.findAll();
        Iterable<Province> provinces = provinceService.findAll();
        modelAndView.addObject("customers", customers);
        modelAndView.addObject("provinces", provinces);
        return modelAndView;
    }

    @GetMapping("/customers/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView(("/customer/create"));
        Iterable<Province> provinces = provinceService.findAll();
        modelAndView.addObject("provinces", provinces);
        modelAndView.addObject("customer", new Customer());
        return modelAndView;
    }

    @PostMapping("/customers/create")
    public ModelAndView createCustomer(@ModelAttribute Customer customer) {
        ModelAndView modelAndView = new ModelAndView("redirect:/customers/list");
        customerService.save(customer);
        return modelAndView;
    }

    @GetMapping("/customers/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (!customer.isPresent()) {
            return new ModelAndView("/error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/customer/edit");
        modelAndView.addObject("customer", customer.get());
        Iterable<Province> provinces = provinceService.findAll();
        modelAndView.addObject("provinces", provinces);
        return modelAndView;
    }

    @PostMapping("/customers/edit/{id}")
    public ModelAndView editCustomer(@PathVariable Long id, @ModelAttribute Customer customer) {
        Optional<Customer> customerOptional = customerService.findById(id);
        if (!customerOptional.isPresent()) {
            return new ModelAndView("/error-404");
        }
        Customer oldCustomer = customerOptional.get();
        oldCustomer.setFirstName(customer.getFirstName());
        oldCustomer.setLastName(customer.getLastName());
        oldCustomer.setProvince(customer.getProvince());
        customerService.save(oldCustomer);
        return new ModelAndView("redirect:/customers/list");
    }

    @GetMapping("/customers/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (!customer.isPresent()) {
            return new ModelAndView("error-404");
        }
        ModelAndView modelAndView= new ModelAndView("/customer/delete");
        modelAndView.addObject("customer", customer.get());
        Iterable<Province> provinces = provinceService.findAll();
        modelAndView.addObject("provinces", provinces);
        return modelAndView;
    }

    @PostMapping("/customers/delete/{id}")
    public ModelAndView deleteCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (!customer.isPresent()) {
            return new ModelAndView("error-404");
        }
        customerService.remove(id);
        return new ModelAndView("redirect:/customers/list");
    }


}
