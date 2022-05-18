package com.codegym.final_examcity.controller;

import com.codegym.final_examcity.model.City;
import com.codegym.final_examcity.model.CityForm;
import com.codegym.final_examcity.model.Country;
import com.codegym.final_examcity.service.city.ICityService;
import com.codegym.final_examcity.service.country.ICountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/city")
@CrossOrigin("*")
public class CityController {

    @Autowired
    private ICityService cityService;

    @Autowired
    private ICountryService countryService;

    @Autowired
    Environment env;

    @ModelAttribute("countries")
    private Iterable<Country> countries(){
        return countryService.findAll();
    }

    @GetMapping("/country")
    public ResponseEntity<Iterable<Country>> showAllCountry(){
        return new ResponseEntity<>(countryService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<City> saveCity(@ModelAttribute CityForm cityForm){
        MultipartFile multipartFile = cityForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpLoad = env.getProperty("upload.path");
        try{
            FileCopyUtils.copy(multipartFile.getBytes(),new File(fileUpLoad+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        City city = new City(cityForm.getNameCity(),fileName,cityForm.getAcreage(),cityForm.getPopulation(),cityForm.getGDP(),cityForm.getDescription(),cityForm.getCountry());
        cityService.save(city);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/list")
    public  ResponseEntity<Iterable<City>> showAll(){
        return new ResponseEntity<>(cityService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<City> deleteCity(@PathVariable Long id) {
        Optional<City> cityOptional = cityService.findById(id);
        if (!cityOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        cityService.remove(id);
        return new ResponseEntity<>(cityOptional.get(), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> findOne(@PathVariable Long id){
        City city = cityService.findById(id).get();
        return new ResponseEntity<>(city,HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @ModelAttribute CityForm cityForm){
        Optional<City> cityOptional = cityService.findById(id);
        cityForm.setId(cityOptional.get().getId());
        MultipartFile multipartFile = cityForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        String fileUpLoad = env.getProperty("upload.path");
        City existCity = new City(id,cityForm.getNameCity(),fileName,cityForm.getAcreage(),cityForm.getPopulation(),cityForm.getGDP(),cityForm.getDescription(),cityForm.getCountry());
        try{
            FileCopyUtils.copy(multipartFile.getBytes(),new File(fileUpLoad+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (existCity.getImage().equals("filename.jpg")){
            existCity.setImage(cityOptional.get().getImage());
        }
        return new ResponseEntity<>(cityService.save(existCity),HttpStatus.OK);
    }
}
