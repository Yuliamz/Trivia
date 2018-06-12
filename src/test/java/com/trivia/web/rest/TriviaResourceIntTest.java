package com.trivia.web.rest;

import com.trivia.TriviaApp;

import com.trivia.domain.Trivia;
import com.trivia.repository.ClientAnswerRepository;
import com.trivia.repository.TriviaRepository;
import com.trivia.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.trivia.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TriviaResource REST controller.
 *
 * @see TriviaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TriviaApp.class)
public class TriviaResourceIntTest {

    private static final Instant DEFAULT_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    @Autowired
    private TriviaRepository triviaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTriviaMockMvc;

    private Trivia trivia;
    private ClientAnswerRepository clientAnswerRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TriviaResource triviaResource = new TriviaResource(triviaRepository, clientAnswerRepository);
        this.restTriviaMockMvc = MockMvcBuilders.standaloneSetup(triviaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trivia createEntity(EntityManager em) {
        Trivia trivia = new Trivia()
            .start(DEFAULT_START)
            .duration(DEFAULT_DURATION)
            .level(DEFAULT_LEVEL);
        return trivia;
    }

    @Before
    public void initTest() {
        trivia = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrivia() throws Exception {
        int databaseSizeBeforeCreate = triviaRepository.findAll().size();

        // Create the Trivia
        restTriviaMockMvc.perform(post("/api/trivias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trivia)))
            .andExpect(status().isCreated());

        // Validate the Trivia in the database
        List<Trivia> triviaList = triviaRepository.findAll();
        assertThat(triviaList).hasSize(databaseSizeBeforeCreate + 1);
        Trivia testTrivia = triviaList.get(triviaList.size() - 1);
        assertThat(testTrivia.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testTrivia.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testTrivia.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void createTriviaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = triviaRepository.findAll().size();

        // Create the Trivia with an existing ID
        trivia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTriviaMockMvc.perform(post("/api/trivias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trivia)))
            .andExpect(status().isBadRequest());

        // Validate the Trivia in the database
        List<Trivia> triviaList = triviaRepository.findAll();
        assertThat(triviaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = triviaRepository.findAll().size();
        // set the field null
        trivia.setStart(null);

        // Create the Trivia, which fails.

        restTriviaMockMvc.perform(post("/api/trivias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trivia)))
            .andExpect(status().isBadRequest());

        List<Trivia> triviaList = triviaRepository.findAll();
        assertThat(triviaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = triviaRepository.findAll().size();
        // set the field null
        trivia.setDuration(null);

        // Create the Trivia, which fails.

        restTriviaMockMvc.perform(post("/api/trivias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trivia)))
            .andExpect(status().isBadRequest());

        List<Trivia> triviaList = triviaRepository.findAll();
        assertThat(triviaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = triviaRepository.findAll().size();
        // set the field null
        trivia.setLevel(null);

        // Create the Trivia, which fails.

        restTriviaMockMvc.perform(post("/api/trivias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trivia)))
            .andExpect(status().isBadRequest());

        List<Trivia> triviaList = triviaRepository.findAll();
        assertThat(triviaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTrivias() throws Exception {
        // Initialize the database
        triviaRepository.saveAndFlush(trivia);

        // Get all the triviaList
        restTriviaMockMvc.perform(get("/api/trivias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trivia.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }

    @Test
    @Transactional
    public void getTrivia() throws Exception {
        // Initialize the database
        triviaRepository.saveAndFlush(trivia);

        // Get the trivia
        restTriviaMockMvc.perform(get("/api/trivias/{id}", trivia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trivia.getId().intValue()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }

    @Test
    @Transactional
    public void getNonExistingTrivia() throws Exception {
        // Get the trivia
        restTriviaMockMvc.perform(get("/api/trivias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrivia() throws Exception {
        // Initialize the database
        triviaRepository.saveAndFlush(trivia);
        int databaseSizeBeforeUpdate = triviaRepository.findAll().size();

        // Update the trivia
        Trivia updatedTrivia = triviaRepository.findOne(trivia.getId());
        // Disconnect from session so that the updates on updatedTrivia are not directly saved in db
        em.detach(updatedTrivia);
        updatedTrivia
            .start(UPDATED_START)
            .duration(UPDATED_DURATION)
            .level(UPDATED_LEVEL);

        restTriviaMockMvc.perform(put("/api/trivias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTrivia)))
            .andExpect(status().isOk());

        // Validate the Trivia in the database
        List<Trivia> triviaList = triviaRepository.findAll();
        assertThat(triviaList).hasSize(databaseSizeBeforeUpdate);
        Trivia testTrivia = triviaList.get(triviaList.size() - 1);
        assertThat(testTrivia.getStart()).isEqualTo(UPDATED_START);
        assertThat(testTrivia.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testTrivia.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingTrivia() throws Exception {
        int databaseSizeBeforeUpdate = triviaRepository.findAll().size();

        // Create the Trivia

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTriviaMockMvc.perform(put("/api/trivias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trivia)))
            .andExpect(status().isCreated());

        // Validate the Trivia in the database
        List<Trivia> triviaList = triviaRepository.findAll();
        assertThat(triviaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTrivia() throws Exception {
        // Initialize the database
        triviaRepository.saveAndFlush(trivia);
        int databaseSizeBeforeDelete = triviaRepository.findAll().size();

        // Get the trivia
        restTriviaMockMvc.perform(delete("/api/trivias/{id}", trivia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Trivia> triviaList = triviaRepository.findAll();
        assertThat(triviaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trivia.class);
        Trivia trivia1 = new Trivia();
        trivia1.setId(1L);
        Trivia trivia2 = new Trivia();
        trivia2.setId(trivia1.getId());
        assertThat(trivia1).isEqualTo(trivia2);
        trivia2.setId(2L);
        assertThat(trivia1).isNotEqualTo(trivia2);
        trivia1.setId(null);
        assertThat(trivia1).isNotEqualTo(trivia2);
    }
}
