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
public class ProvinceController {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IProvinceService provinceService;

    @GetMapping("/provinces/list")
    public ModelAndView showListProvince() {
        ModelAndView modelAndView = new ModelAndView("/province/list");
        Iterable<Province> provinces = provinceService.findAll();
        modelAndView.addObject("provinces", provinces);
        return modelAndView;
    }

    @GetMapping("/provinces/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/province/create");
        modelAndView.addObject("province", new Province());
        return modelAndView;
    }

    @PostMapping("/provinces/create")
    public ModelAndView createProvince(@ModelAttribute Province province) {
        provinceService.save(province);
        return new ModelAndView("redirect:/provinces/list");
    }

    @GetMapping("/provinces/edit/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Optional<Province> province = provinceService.findById(id);
        if (!province.isPresent()) {
            return new ModelAndView("/error-404");
        }
        ModelAndView modelAndView = new ModelAndView("/province/edit");
        modelAndView.addObject("province", province.get());
        return modelAndView;
    }

    @PostMapping("/provinces/edit/{id}")
    public ModelAndView editProvince(@PathVariable Long id, @ModelAttribute Province province) {
        Optional<Province> provinceOptional = provinceService.findById(id);
        if (!provinceOptional.isPresent()) {
            return new ModelAndView("/error-404");
        }
        Province oldProvince = provinceOptional.get();
        oldProvince.setName(province.getName());
        provinceService.save(oldProvince);
        return new ModelAndView("redirect:/provinces/list");
    }

    @GetMapping("/provinces/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Optional<Province> province = provinceService.findById(id);
        if (!province.isPresent()) {
            return new ModelAndView("/error-404");
        }
        return new ModelAndView("/province/delete", "province", province.get());
    }

    @PostMapping("/provinces/delete/{id}")
    public ModelAndView deleteProvince(@PathVariable Long id) {
        Optional<Province> provinceOptional = provinceService.findById(id);
        if (!provinceOptional.isPresent()) {
            return new ModelAndView("/error-404");
        }
        provinceService.remove(id);
        return new ModelAndView("redirect:/provinces/list");
    }

    @GetMapping("/provinces/find/{id}")
    public ModelAndView showAllCustomerByProvince(@PathVariable Long id) {
        Optional<Province> provinceOptional = provinceService.findById(id);
        if (!provinceOptional.isPresent()) {
            return new ModelAndView("error-404");
        }
        Province province = provinceOptional.get();
        Iterable<Customer> customers = customerService.findAllByProvince(province);
        return new ModelAndView("/province/find", "customers", customers);
    }
}
