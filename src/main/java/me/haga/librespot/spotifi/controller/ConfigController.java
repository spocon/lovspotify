package me.haga.librespot.spotifi.controller;


import me.haga.librespot.spotifi.model.ConfigData;
import me.haga.librespot.spotifi.model.GeneralResponse;
import me.haga.librespot.spotifi.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("config")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping(value = "data",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getConfig() {
         return ResponseEntity.ok(configService.getConfigData());
    }

    @PostMapping(value = "data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setConfig(@RequestBody ConfigData configData) {
        configService.setConfigData(configData);
       return ResponseEntity.ok(new GeneralResponse(2000, "Config stored"));
    }

}
