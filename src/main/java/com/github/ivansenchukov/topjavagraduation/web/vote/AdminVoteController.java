package com.github.ivansenchukov.topjavagraduation.web.vote;


import com.github.ivansenchukov.topjavagraduation.exception.RestrictedOperationException;
import com.github.ivansenchukov.topjavagraduation.model.Vote;
import com.github.ivansenchukov.topjavagraduation.web.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// todo - should add example values for Swagger in the future
@Api(description = "Endpoint fo Admins to work with Votes")
@RestController
@RequestMapping(value = AdminVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteController extends AbstractVoteController {

    public static final String REST_URL = WebUtil.ADMIN_URL + "/votes";


    //<editor-fold desc="GET">

    @ApiOperation(value = "Return a single Vote by ID")
    @Override
    @GetMapping("/{voteId}")
    public Vote get(
            @ApiParam(required = true, value = "ID of wanted Vote")
            @PathVariable int voteId
    ) {
        return super.get(voteId);
    }


    @Override
    @ApiOperation(value = "Returns the history of all Votes of given User")
    @GetMapping("/by_user")
    public List<Vote> getByUserId(
            @ApiParam(required = true, value = "ID of User, which history of Votes we want to see.")
            @RequestParam
                    int userId) {
        return super.getByUserId(userId);
    }

    //</editor-fold>


    //<editor-fold desc="DELETE">

    @Override
    @ApiOperation(value = "Delete Vote by given ID. Admin can delete Votes of all Users in All time")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @ApiParam(required = true, value = "ID of Vote to Delete")
            @PathVariable int id
    ) {
        super.delete(id);
    }

    //</editor-fold>
}
