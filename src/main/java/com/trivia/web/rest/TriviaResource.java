package com.trivia.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.trivia.domain.Trivia;

import com.trivia.repository.TriviaRepository;
import com.trivia.web.rest.errors.BadRequestAlertException;
import com.trivia.web.rest.util.HeaderUtil;
import com.trivia.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Trivia.
 */
@RestController
@RequestMapping("/api")
public class TriviaResource {

    private final Logger log = LoggerFactory.getLogger(TriviaResource.class);

    private static final String ENTITY_NAME = "trivia";

    private final TriviaRepository triviaRepository;

    public TriviaResource(TriviaRepository triviaRepository) {
        this.triviaRepository = triviaRepository;
    }

    /**
     * POST  /trivias : Create a new trivia.
     *
     * @param trivia the trivia to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trivia, or with status 400 (Bad Request) if the trivia has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/trivias")
    @Timed
    public ResponseEntity<Trivia> createTrivia(@Valid @RequestBody Trivia trivia) throws URISyntaxException {
        log.debug("REST request to save Trivia : {}", trivia);
        if (trivia.getId() != null) {
            throw new BadRequestAlertException("A new trivia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Trivia result = triviaRepository.save(trivia);
        return ResponseEntity.created(new URI("/api/trivias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trivias : Updates an existing trivia.
     *
     * @param trivia the trivia to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trivia,
     * or with status 400 (Bad Request) if the trivia is not valid,
     * or with status 500 (Internal Server Error) if the trivia couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/trivias")
    @Timed
    public ResponseEntity<Trivia> updateTrivia(@Valid @RequestBody Trivia trivia) throws URISyntaxException {
        log.debug("REST request to update Trivia : {}", trivia);
        if (trivia.getId() == null) {
            return createTrivia(trivia);
        }
        Trivia result = triviaRepository.save(trivia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trivia.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trivias : get all the trivias.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trivias in body
     */
    @GetMapping("/trivias")
    @Timed
    public ResponseEntity<List<Trivia>> getAllTrivias(Pageable pageable) {
        log.debug("REST request to get a page of Trivias");
        Page<Trivia> page = triviaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trivias");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /trivias/:id : get the "id" trivia.
     *
     * @param id the id of the trivia to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trivia, or with status 404 (Not Found)
     */
    @GetMapping("/trivias/{id}")
    @Timed
    public ResponseEntity<Trivia> getTrivia(@PathVariable Long id) {
        log.debug("REST request to get Trivia : {}", id);
        Trivia trivia = triviaRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trivia));
    }

    /**
     * DELETE  /trivias/:id : delete the "id" trivia.
     *
     * @param id the id of the trivia to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/trivias/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrivia(@PathVariable Long id) {
        log.debug("REST request to delete Trivia : {}", id);
        triviaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
