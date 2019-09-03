package com.aguevara.difftool.controller;

import com.aguevara.difftool.entities.DiffEntity;
import com.aguevara.difftool.model.Result;
import com.aguevara.difftool.model.StringEncoded;
import com.aguevara.difftool.repositories.DiffRepository;
import com.aguevara.difftool.services.DiffResult;
import com.aguevara.difftool.services.DiffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Base64;
import java.util.Optional;

@Controller
@RequestMapping(path = "/v1/diff")
public class DiffController {

    @Autowired
    private DiffRepository diffRepository;

    @Autowired
    private DiffService diffService;

    @PostMapping(path = "{id}/left", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<DiffEntity>
    setLeftDiff(@PathVariable(value = "id") Long id, @RequestBody StringEncoded base64encodedString) {
        try {
            Optional<DiffEntity> optionalDiffEntity = diffRepository.findBydiffid(id);
            if (optionalDiffEntity.isPresent()) {
                DiffEntity diffEntity = optionalDiffEntity.get();
                diffEntity.setDiffleft(base64encodedString.getBase64encoded());
                diffRepository.save(diffEntity);
                return ResponseEntity.ok(diffEntity);
            } else {
                DiffEntity diffEntity = new DiffEntity();
                diffEntity.setDiffid(id);
                diffEntity.setDiffleft(base64encodedString.getBase64encoded());
                diffRepository.save(diffEntity);
                return ResponseEntity.ok(diffEntity);
            }
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "{id}/right", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<DiffEntity>
    setRightDiff(@PathVariable(value = "id") Long id, @RequestBody StringEncoded base64encodedString) {
        try {
            Optional<DiffEntity> optionalDiffEntity = diffRepository.findBydiffid(id);
            if (optionalDiffEntity.isPresent()) {
                DiffEntity diffEntity = optionalDiffEntity.get();
                diffEntity.setDiffright(base64encodedString.getBase64encoded());
                diffRepository.save(diffEntity);
                return ResponseEntity.ok(diffEntity);

            } else {
                DiffEntity diffEntity = new DiffEntity();
                diffEntity.setDiffid(id);
                diffEntity.setDiffright(base64encodedString.getBase64encoded());
                diffRepository.save(diffEntity);
                return ResponseEntity.ok(diffEntity);
            }
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Result> getResults(@PathVariable(value = "id") Long id) {
        Optional<DiffEntity> optionalDiffEntity = diffRepository.findBydiffid(id);
        if(optionalDiffEntity.isPresent()) {
            DiffEntity diffEntity = optionalDiffEntity.get();
            String diffLeft = diffEntity.getDiffleft();
            String diffRight = diffEntity.getDiffright();
            if (StringUtils.isEmpty(diffLeft) || StringUtils.isEmpty(diffRight)) {
                return new ResponseEntity<>(new Result("you have not submitted all the necessary strings",
                        DiffResult.INCOMPLETE), HttpStatus.BAD_REQUEST);
            }
            else {
                Result result = diffService.calculateDiff(diffLeft, diffRight);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }
        else {
            return new ResponseEntity<>(new Result("Id " + id + " not found",
                    DiffResult.ID_NOT_FOUND), HttpStatus.BAD_REQUEST);
        }
    }
}
