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

import java.util.Optional;

@Controller
@RequestMapping(path = "/v1/diff")
public class DiffController {

    @Autowired
    private DiffRepository diffRepository;

    @Autowired
    private DiffService diffService;

    /***
     * Restful endpoint to submit the left base 64 encoded string
     * @param id The id that should match the id used in the right post and in the get for the results
     * @param base64encodedString The json containing the base 64 encoded string
     * @return The status of the current Entity, detailing if both left and right have been submitted for this id
     */
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

    /***
     * Restful endpoint to submit the right base 64 encoded string
     * @param id The id that should match the id used in the left post and in the get for the results
     * @param base64encodedString The json containing the base 64 encoded string
     * @return The status of the current Entity, detailing if both left and right have been submitted for this id
     */
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

    /***
     * Get endpoint to get the result of the diff of the left and right strings
     * @param id The id that was used to submit the left and the right string
     * @return a Json object containing a description and a result of the comparison
     */
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
