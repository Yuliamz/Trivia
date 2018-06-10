package com.trivia.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.trivia.domain.ClientAnswer;

import com.trivia.repository.ClientAnswerRepository;
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
 * REST controller for managing ClientAnswer.
 */
@RestController
@RequestMapping("/api")
public class ClientAnswerResource {

    private final Logger log = LoggerFactory.getLogger(ClientAnswerResource.class);

    private static final String ENTITY_NAME = "clientAnswer";

    private final ClientAnswerRepository clientAnswerRepository;

    public ClientAnswerResource(ClientAnswerRepository clientAnswerRepository) {
        this.clientAnswerRepository = clientAnswerRepository;
    }

    /**
     * POST  /client-answers : Create a new clientAnswer.
     *
     * @param clientAnswer the clientAnswer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientAnswer, or with status 400 (Bad Request) if the clientAnswer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/client-answers")
    @Timed
    public ResponseEntity<ClientAnswer> createClientAnswer(@Valid @RequestBody ClientAnswer clientAnswer) throws URISyntaxException {
        log.debug("REST request to save ClientAnswer : {}", clientAnswer);
        if (clientAnswer.getId() != null) {
            throw new BadRequestAlertException("A new clientAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClientAnswer result = clientAnswerRepository.save(clientAnswer);
        return ResponseEntity.created(new URI("/api/client-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-answers : Updates an existing clientAnswer.
     *
     * @param clientAnswer the clientAnswer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientAnswer,
     * or with status 400 (Bad Request) if the clientAnswer is not valid,
     * or with status 500 (Internal Server Error) if the clientAnswer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/client-answers")
    @Timed
    public ResponseEntity<ClientAnswer> updateClientAnswer(@Valid @RequestBody ClientAnswer clientAnswer) throws URISyntaxException {
        log.debug("REST request to update ClientAnswer : {}", clientAnswer);
        if (clientAnswer.getId() == null) {
            return createClientAnswer(clientAnswer);
        }
        ClientAnswer result = clientAnswerRepository.save(clientAnswer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clientAnswer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-answers : get all the clientAnswers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clientAnswers in body
     */
    @GetMapping("/client-answers")
    @Timed
    public ResponseEntity<List<ClientAnswer>> getAllClientAnswers(Pageable pageable) {
        log.debug("REST request to get a page of ClientAnswers");
        Page<ClientAnswer> page = clientAnswerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/client-answers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /client-answers/:id : get the "id" clientAnswer.
     *
     * @param id the id of the clientAnswer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientAnswer, or with status 404 (Not Found)
     */
    @GetMapping("/client-answers/{id}")
    @Timed
    public ResponseEntity<ClientAnswer> getClientAnswer(@PathVariable Long id) {
        log.debug("REST request to get ClientAnswer : {}", id);
        ClientAnswer clientAnswer = clientAnswerRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(clientAnswer));
    }

    /**
     * DELETE  /client-answers/:id : delete the "id" clientAnswer.
     *
     * @param id the id of the clientAnswer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/client-answers/{id}")
    @Timed
    public ResponseEntity<Void> deleteClientAnswer(@PathVariable Long id) {
        log.debug("REST request to delete ClientAnswer : {}", id);
        clientAnswerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
